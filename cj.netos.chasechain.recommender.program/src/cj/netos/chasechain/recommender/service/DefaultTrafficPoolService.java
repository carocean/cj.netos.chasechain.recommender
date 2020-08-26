package cj.netos.chasechain.recommender.service;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.netos.chasechain.recommender.*;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.util.Encript;
import redis.clients.jedis.JedisCluster;

import java.math.BigInteger;
import java.util.*;

@CjService(name = "defaultTrafficPoolService")
public class DefaultTrafficPoolService extends AbstractService implements ITrafficPoolService, Constants {

    @CjServiceRef(refByName = "mongodb.netos.home")
    ICube home;
    @CjServiceRef(refByName = "@.redis.cluster")
    JedisCluster jedisCluster;

    @Override
    public TrafficPool getTownTrafficPool(String towncode) {
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple %s %s where {'tuple.geoCode':'%s','tuple.level':4}",
                TrafficPool._COL_NAME, TrafficPool.class.getName(), towncode);
        IQuery<TrafficPool> query = home.createQuery(cjql);
        IDocument<TrafficPool> document = query.getSingleResult();
        if (document == null) {
            return null;
        }
        return document.tuple();
    }

    @Override
    public TrafficPool getPool(String pool) {
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple %s %s where {'tuple.id':'%s'}",
                TrafficPool._COL_NAME, TrafficPool.class.getName(), pool);
        IQuery<TrafficPool> query = home.createQuery(cjql);
        IDocument<TrafficPool> document = query.getSingleResult();
        if (document == null) {
            return null;
        }
        return document.tuple();
    }

    @Override
    public List<TrafficPool> getHierarchyGeospherePools(String towncode) {
        List<TrafficPool> pools = new ArrayList<>();
        TrafficPool town = getTownTrafficPool(towncode);
        if (town == null) {
            return pools;
        }
        pools.add(town);
        TrafficPool district = getPool(town.getParent());
        if (district == null) {
            return pools;
        }
        pools.add(district);
        TrafficPool city = getPool(district.getParent());
        if (city == null) {
            return pools;
        }
        pools.add(city);
        TrafficPool province = getPool(city.getParent());
        if (province == null) {
            return pools;
        }
        pools.add(province);
        TrafficPool contry = getPool(province.getParent());
        if (contry == null) {
            return pools;
        }
        pools.add(contry);
        return pools;
    }


    @Override
    public List<TrafficPool> getRecommendPools(String towncode) {
        List<TrafficPool> pools = getHierarchyGeospherePools(towncode);
        TrafficPool normalPool = randomGetNormalTrafficPool();
        if (normalPool != null) {
            pools.add(normalPool);
        }
        return pools;
    }

    private TrafficPool randomGetNormalTrafficPool() {
        String cjql = String.format("select {'tuple.id':1} from tuple %s %s where {'tuple.level':-1}",
                TrafficPool._COL_NAME, HashMap.class.getName());
        IQuery<HashMap<String, String>> query = home.createQuery(cjql);
        List<IDocument<HashMap<String, String>>> documents = query.getResultList();
        if (documents.isEmpty()) {
            return null;
        }
        String md5 = Encript.md5(String.format("%s%s", UUID.randomUUID().toString(), System.currentTimeMillis()));
        int factor = Math.abs(md5.hashCode());
        int index = factor % documents.size();
        String poolId = documents.get(index).tuple().get("id");
        return getPool(poolId);
    }

    @Override
    public TrafficPool getCountryPool() {
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple %s %s where {'tuple.level':0}", TrafficPool._COL_NAME, TrafficPool.class.getName());
        IQuery<TrafficPool> query = home.createQuery(cjql);
        IDocument<TrafficPool> document = query.getSingleResult();
        if (document == null) {
            return null;
        }
        return document.tuple();
    }

    @Override
    public List<TrafficPool> pageChildrenPool(String pool, int limit, long offset) {
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple %s %s where {'tuple.parent':'%s'}",
                limit, offset, TrafficPool._COL_NAME, TrafficPool.class.getName(), pool);
        IQuery<TrafficPool> query = home.createQuery(cjql);
        List<IDocument<TrafficPool>> documents = query.getResultList();
        List<TrafficPool> pools = new ArrayList<>();
        for (IDocument<TrafficPool> document : documents) {
            pools.add(document.tuple());
        }
        return pools;
    }

    @Override
    public List<TrafficPool> pageChildrenPoolByLevel(String pool, int level, int limit, long offset) {
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple %s %s where {'tuple.parent':'%s','tuple.level':%s}",
                limit, offset, TrafficPool._COL_NAME, TrafficPool.class.getName(), pool, level);
        IQuery<TrafficPool> query = home.createQuery(cjql);
        List<IDocument<TrafficPool>> documents = query.getResultList();
        List<TrafficPool> pools = new ArrayList<>();
        for (IDocument<TrafficPool> document : documents) {
            pools.add(document.tuple());
        }
        return pools;
    }

    @Override
    public long countContentProvidersOfPool(String pool) throws CircuitException {
        return cubePool(pool).tupleCount(Constants._COL_NAME_PROVIDER, String.format("{}"));
    }

    @Override
    public List<String> pageContentProvider(String pool, int limit, long offset) throws CircuitException {
        String cjql = String.format("select {'tuple.person':1}.limit(%s).skip(%s) from tuple %s %s where {}",
                limit, offset, Constants._COL_NAME_PROVIDER, HashMap.class.getName());
        IQuery<Map<String, String>> query = cubePool(pool).createQuery(cjql);
        List<IDocument<Map<String, String>>> documents = query.getResultList();
        List<String> persons = new ArrayList<>();
        for (IDocument<Map<String, String>> document : documents) {
            persons.add(document.tuple().get("person"));
        }
        return persons;
    }

    @Override
    public TrafficDashboardPointer getTrafficDashboard(String pool) throws CircuitException {
        ICube cube = cubePool(pool);
        //在etl.work项目中的AbstractService类中已建索引为倒序
        String cjql = String.format("select {'tuple':'*'}.sort({'tuple.lastBubbleTime':-1}).limit(1) from tuple %s %s where {}", TrafficDashboardPointer._COL_NAME, TrafficDashboardPointer.class.getName());
        IQuery<TrafficDashboardPointer> query = cube.createQuery(cjql);
        IDocument<TrafficDashboardPointer> document = query.getSingleResult();
        if (document == null) {
            TrafficDashboardPointer pointer = new TrafficDashboardPointer();
            pointer.setItemCount(new BigInteger("0"));
            ItemBehaviorPointer inner = new ItemBehaviorPointer();
            inner.setComments(new BigInteger("0"));
            inner.setLikes(new BigInteger("0"));
            inner.setRecommends(new BigInteger("0"));
            ItemBehaviorPointer innate = new ItemBehaviorPointer();
            innate.setComments(new BigInteger("0"));
            innate.setLikes(new BigInteger("0"));
            pointer.setInnerBehaviorPointer(inner);
            pointer.setInnateBehaviorPointer(innate);
            pointer.setLastBubbleTime(0L);
            return pointer;
        }
        return document.tuple();

    }
}
