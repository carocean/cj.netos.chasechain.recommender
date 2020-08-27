package cj.netos.chasechain.recommender.service;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.chasechain.recommender.*;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.gson2.com.google.gson.Gson;
import cj.ultimate.util.StringUtil;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@CjService(name = "defaultRecommenderService")
public class DefaultRecommenderService extends AbstractService implements IRecommenderService, Constants {
    @CjServiceRef(refByName = "@.redis.cluster")
    JedisCluster jedisCluster;
    @CjServiceRef(refByName = "defaultTrafficPoolService")
    ITrafficPoolService trafficPoolService;

    @CjServiceRef(refByName = "defaultContentItemService")
    IContentItemService contentItemService;
    @CjServiceRef(refByName = "defaultRecommendedItemService")
    IRecommendedItemService recommendedItemService;
    @CjServiceRef(refByName = "defaultItemBehaviorService")
    IItemBehaviorService itemBehaviorService;

    @Override
    public List<ContentItem> pullItem(String person, String towncode) throws CircuitException {
        //假设各级别池(0,-1,1,2,3,4)的获取比例分别是：2.5	1.5	2	1	1	2	2.5
        //又设每次最多获取30条，则按比例在各级别池中获取
        //用池规则：对于地理池从4级池向父找直到全国0级；对于常规池，则根据常规池数随机选一个
        //缓冲的键是：redis_cacher_pool_item_key+"."+poolId
        //如果位置为空则只在常规池和全国池中找

        RecommenderConfig config = getPersonConfig(person);
        if (config == null) {//默认的
            config = new RecommenderConfig();
            config.setMaxRecommendItemCount(50);
            config.setCountryRecommendWeight(new BigDecimal("1.00"));
            config.setProvinceRecommendWeight(new BigDecimal("1.00"));
            config.setCityRecommendWeight(new BigDecimal("1.00"));
            config.setDistrictRecommendWeight(new BigDecimal("1.00"));
            config.setTownRecommendWeight(new BigDecimal("1.00"));
            config.setNormalRecommendWeight(new BigDecimal("1.00"));
            BigDecimal weightCapacity = new BigDecimal(config.getMaxRecommendItemCount()).
                    divide(new BigDecimal("6.00")/*由于初始化时上面的权之和为6*/, 4, RoundingMode.HALF_DOWN);
            config.setWeightCapacity(weightCapacity);
        }
        String redisKeyPerson = String.format("recommender.person.%s", person);
        jedisCluster.del(redisKeyPerson);
        List<TrafficPool> pools = trafficPoolService.getRecommendPools(towncode);
        BigDecimal weightCapacity = config.getWeightCapacity();
        for (TrafficPool pool : pools) {
            int canFullItemCount = 20;
            switch (pool.getLevel()) {
                case -1:
                    canFullItemCount = (weightCapacity.multiply(config.getNormalRecommendWeight())).intValue();
                    break;
                case 0:
                    canFullItemCount = (weightCapacity.multiply(config.getCountryRecommendWeight())).intValue();
                    break;
                case 1:
                    canFullItemCount = (weightCapacity.multiply(config.getProvinceRecommendWeight())).intValue();
                    break;
                case 2:
                    canFullItemCount = (weightCapacity.multiply(config.getCityRecommendWeight())).intValue();
                    break;
                case 3:
                    canFullItemCount = (weightCapacity.multiply(config.getDistrictRecommendWeight())).intValue();
                    break;
                case 4:
                    canFullItemCount = (weightCapacity.multiply(config.getTownRecommendWeight())).intValue();
                    break;
            }
            CJSystem.logging().info(getClass(), String.format("%s %s[%s]", person, pool.getTitle(), pool.getId()));
            CJSystem.logging().info(getClass(), String.format("\t可取数是%s条", canFullItemCount));
            String key = String.format("%s.%s", redis_cacher_pool_item_key, pool.getId());
            List<String> itemIds = jedisCluster.srandmember(key, canFullItemCount);
            CJSystem.logging().info(getClass(), String.format("\t随机取出%s条", itemIds.size()));
            for (String item : itemIds) {
                jedisCluster.hset(redisKeyPerson, item, pool.getId());
            }
        }

        Set<String> itemIds = jedisCluster.hkeys(redisKeyPerson);
        CJSystem.logging().info(getClass(), String.format("%s 获得的总推荐数：%s", person, itemIds.size()));
        List<RecommendedItem> recommendedItems = recommendedItemService.listRecommended(person, itemIds);
        for (RecommendedItem item : recommendedItems) {
            itemIds.remove(item.getItem());
        }
        recommendedItemService.addRecommendItems(person, itemIds);
        CJSystem.logging().info(getClass(), String.format("%s 去除已推荐过的之后剩：%s", person, itemIds.size()));
        Map<String, List<String>> itemsOfPool = new HashMap<>();//key(pool)
        for (String item : itemIds) {
            String pool = jedisCluster.hget(redisKeyPerson, item);
            if (StringUtil.isEmpty(pool)) {
                continue;
            }
            List<String> _itemIds = itemsOfPool.get(pool);
            if (_itemIds == null) {
                _itemIds = new ArrayList<>();
                itemsOfPool.put(pool, _itemIds);
            }
            _itemIds.add(item);
        }
        jedisCluster.del(redisKeyPerson);

        List<ContentItem> items = new ArrayList<>();
        for (String pool : itemsOfPool.keySet()) {
            List<String> _itemIds = itemsOfPool.get(pool);
            List<ContentItem> _items = contentItemService.listContentItem(pool, _itemIds);
            if (!_items.isEmpty()) {
                items.addAll(_items);
            }
        }

        itemBehaviorService.addRecommendBehaveToItems(person,items);

        CJSystem.logging().info(getClass(), String.format("%s 最终实际的推荐数：%s", person, items.size()));
        return items;
    }

    @Override
    public void configGlobal(RecommenderConfig config) throws CircuitException {
        ICube home = home();
        home.dropTuple(RecommenderConfig._COL_NAME);
        home.saveDoc(RecommenderConfig._COL_NAME, new TupleDocument<>(config));
    }

    @Override
    public RecommenderConfig getGlobalConfig() throws CircuitException {
        ICube home = home();
        String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {}",
                RecommenderConfig._COL_NAME, RecommenderConfig.class.getName());
        IQuery<RecommenderConfig> query = home.createQuery(cjql);
        IDocument<RecommenderConfig> document = query.getSingleResult();
        if (document == null) {
            return null;
        }
        return document.tuple();
    }

    @Override
    public RecommenderConfig getPersonConfig(String principal) throws CircuitException {
        ICube cube = cubePerson(principal);
        if (cube.tupleCount(RecommenderConfig._COL_NAME, String.format("{}")) < 1) {
            //不存在则拷贝一份
            RecommenderConfig config = getGlobalConfig();
            if (config == null) {
                return null;
            }
            cube.saveDoc(RecommenderConfig._COL_NAME, new TupleDocument<>(config));
            return config;
        }
        String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {}",
                RecommenderConfig._COL_NAME, RecommenderConfig.class.getName());
        IQuery<RecommenderConfig> query = cube.createQuery(cjql);
        IDocument<RecommenderConfig> document = query.getSingleResult();
        if (document == null) {
            return null;
        }
        return document.tuple();
    }

    @Override
    public void configPerson(String principal, RecommenderConfig config) throws CircuitException {
        ICube cube = cubePerson(principal);
        cube.dropTuple(RecommenderConfig._COL_NAME);
        cube.saveDoc(RecommenderConfig._COL_NAME, new TupleDocument<>(config));
    }
}
