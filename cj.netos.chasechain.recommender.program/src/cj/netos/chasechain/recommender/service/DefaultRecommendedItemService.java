package cj.netos.chasechain.recommender.service;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.chasechain.recommender.AbstractService;
import cj.netos.chasechain.recommender.ContentItem;
import cj.netos.chasechain.recommender.IRecommendedItemService;
import cj.netos.chasechain.recommender.RecommendedItem;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.gson2.com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CjService(name = "defaultRecommendedItemService")
public class DefaultRecommendedItemService extends AbstractService implements IRecommendedItemService {
    @Override
    public List<RecommendedItem> listRecommended(String person, Set<String> itemIds) throws CircuitException {
        ICube cube = cubePerson(person);
        String cjql = String.format("select {'tuple':'*'}.sort({'tuple.ctime':-1}) from tuple %s %s where {'tuple.item':{'$in':%s}}",
                RecommendedItem._COL_NAME, RecommendedItem.class.getName(), new Gson().toJson(itemIds));
        IQuery<RecommendedItem> query = cube.createQuery(cjql);
        List<IDocument<RecommendedItem>> list = query.getResultList();
        List<RecommendedItem> items = new ArrayList<>();
        for (IDocument<RecommendedItem> document : list) {
            items.add(document.tuple());
        }
        return items;
    }

    @Override
    public void addRecommendItems(String person, Set<String> itemIds) throws CircuitException {
        ICube cube = cubePerson(person);
        for (String item : itemIds) {
            RecommendedItem recommendedItem = new RecommendedItem();
            recommendedItem.setCtime(System.currentTimeMillis());
            recommendedItem.setItem(item);
            cube.saveDoc(RecommendedItem._COL_NAME, new TupleDocument<>(recommendedItem));
        }
    }
}
