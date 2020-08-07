package cj.netos.chasechain.recommender.service;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.netos.chasechain.recommender.AbstractService;
import cj.netos.chasechain.recommender.ContentItem;
import cj.netos.chasechain.recommender.IContentItemService;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.gson2.com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@CjService(name = "defaultContentItemService")
public class DefaultContentItemService extends AbstractService implements IContentItemService {
    @Override
    public List<ContentItem> listContentItem(String trafficPool, List<String> itemIds) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        String cjql = String.format("select {'tuple':'*'}.sort({'tuple.ctime':-1}) from tuple %s %s where {'tuple.id':{'$in':%s}}",
                ContentItem._COL_NAME, ContentItem.class.getName(), new Gson().toJson(itemIds));
        IQuery<ContentItem> query = cube.createQuery(cjql);
        List<IDocument<ContentItem>> list = query.getResultList();
        List<ContentItem> contentItems = new ArrayList<>();
        for (IDocument<ContentItem> document : list) {
            ContentItem item = document.tuple();
            item.setPool(trafficPool);
            contentItems.add(item);
        }
        return contentItems;
    }

    @Override
    public long countContentItemsOfBox(String pool, String box) throws CircuitException {
        return cubePool(pool).tupleCount(ContentItem._COL_NAME, String.format("{'tuple.box':'%s'}", box));
    }

    @Override
    public long countContentItemsOfPool(String pool) throws CircuitException {
        return cubePool(pool).tupleCount(ContentItem._COL_NAME, String.format("{}"));
    }

    @Override
    public ContentItem getContentItem(String pool, String item) throws CircuitException {
        ICube cube = cubePool(pool);
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple %s %s where {'tuple.id':'%s'}",
                ContentItem._COL_NAME, ContentItem.class.getName(), item);
        IQuery<ContentItem> query = cube.createQuery(cjql);
        IDocument<ContentItem> document = query.getSingleResult();
        if (document == null) {
            return null;
        }
        document.tuple().setPool(pool);
        return document.tuple();
    }

    @Override
    public List<ContentItem> pageContentItemOfBox(String pool, String box, int limit, long offset) throws CircuitException {
        ICube cube = cubePool(pool);
        String cjql = String.format("select {'tuple':'*'}.sort({'tuple.ctime':-1}).limit(%s).skip(%s) from tuple %s %s where {'tuple.box':'%s'}",
                limit, offset, ContentItem._COL_NAME, ContentItem.class.getName(), box);
        IQuery<ContentItem> query = cube.createQuery(cjql);
        List<IDocument<ContentItem>> list = query.getResultList();
        List<ContentItem> contentItems = new ArrayList<>();
        for (IDocument<ContentItem> document : list) {
            ContentItem item = document.tuple();
            item.setPool(pool);
            contentItems.add(item);
        }
        return contentItems;
    }

    @Override
    public List<ContentItem> pageContentItem(String pool, int limit, long offset) throws CircuitException {
        ICube cube = cubePool(pool);
        String cjql = String.format("select {'tuple':'*'}.sort({'tuple.ctime':-1}).limit(%s).skip(%s) from tuple %s %s where {}",
                limit, offset, ContentItem._COL_NAME, ContentItem.class.getName());
        IQuery<ContentItem> query = cube.createQuery(cjql);
        List<IDocument<ContentItem>> list = query.getResultList();
        List<ContentItem> contentItems = new ArrayList<>();
        for (IDocument<ContentItem> document : list) {
            ContentItem item = document.tuple();
            item.setPool(pool);
            contentItems.add(item);
        }
        return contentItems;
    }

    @Override
    public List<ContentItem> pageContentItemOfProvider(String pool, String provider, int limit, long offset) throws CircuitException {
        ICube cube = cubePool(pool);
        String cjql = String.format("select {'tuple':'*'}.sort({'tuple.ctime':-1}).limit(%s).skip(%s) from tuple %s %s where {'tuple.pointer.creator':'%s'}",
                limit, offset, ContentItem._COL_NAME, ContentItem.class.getName(), provider);
        IQuery<ContentItem> query = cube.createQuery(cjql);
        List<IDocument<ContentItem>> list = query.getResultList();
        List<ContentItem> contentItems = new ArrayList<>();
        for (IDocument<ContentItem> document : list) {
            ContentItem item = document.tuple();
            item.setPool(pool);
            contentItems.add(item);
        }
        return contentItems;
    }
}
