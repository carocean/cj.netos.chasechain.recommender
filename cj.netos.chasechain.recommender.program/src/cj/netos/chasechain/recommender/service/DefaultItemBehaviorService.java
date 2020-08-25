package cj.netos.chasechain.recommender.service;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.chasechain.recommender.*;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@CjService(name = "defaultItemBehaviorService")
public class DefaultItemBehaviorService extends AbstractService implements IItemBehaviorService {

    @Override
    public void update(String trafficPool, String item, long likes, long comments, long recommends) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        if (!exists(trafficPool, item)) {
            addItemBehavior(cube, item, likes, comments, recommends);
            return;
        }
        cube.updateDocOne(ItemBehavior._COL_NAME_INNER,
                Document.parse(String.format("{'tuple.item':'%s'}", item)),
                Document.parse(String.format("{'$set':{'tuple.likes':%s,'tuple.comments':%s,'tuple.recommends':%s,'tuple.utime':%s}}", likes, comments, recommends, System.currentTimeMillis())));
    }

    private ItemBehavior addItemBehavior(ICube cube, String item, long likes, long comments, long recommends) {
        ItemBehavior behavior = new ItemBehavior();
        behavior.setComments(comments);
        behavior.setItem(item);
        behavior.setLikes(likes);
        behavior.setRecommends(recommends);
        behavior.setUtime(System.nanoTime());
        cube.saveDoc(ItemBehavior._COL_NAME_INNER, new TupleDocument<>(behavior));
        return behavior;
    }

    private boolean exists(String trafficPool, String item) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        return cube.tupleCount(ItemBehavior._COL_NAME_INNER, String.format("{'tuple.item':'%s'}", item)) > 0;
    }

    @Override
    public void updateLikes(String trafficPool, String item, long likes) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        cube.updateDocOne(ItemBehavior._COL_NAME_INNER,
                Document.parse(String.format("{'tuple.item':'%s'}", item)),
                Document.parse(String.format("{'$set':{'tuple.likes':%s,'tuple.utime':%s}}", likes, System.currentTimeMillis())));
    }

    @Override
    public void updateComments(String trafficPool, String item, long comments) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        cube.updateDocOne(ItemBehavior._COL_NAME_INNER,
                Document.parse(String.format("{'tuple.item':'%s'}", item)),
                Document.parse(String.format("{'$set':{'tuple.comments':%s,'tuple.utime':%s}}", comments, System.currentTimeMillis())));
    }

    @Override
    public void updateRecommends(String trafficPool, String item, long recommends) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        cube.updateDocOne(ItemBehavior._COL_NAME_INNER,
                Document.parse(String.format("{'tuple.item':'%s'}", item)),
                Document.parse(String.format("{'$set':{'tuple.recommends':%s,'tuple.utime':%s}}", recommends, System.currentTimeMillis())));
    }

    @Override
    public void addRecommendBehaveToItems(String person, List<ContentItem> items) throws CircuitException {
        for (ContentItem item : items) {
            addBehave(item.getPool(), item.getId(), person, "recommend", null);
        }
    }

    @Override
    public void addBehave(String trafficPool, String item, String person, String behave, String attachment) throws CircuitException {
        if (!"comment".equals(behave) && hasBehave(person, trafficPool, item, behave) > 0) {
            return;
        }
        ICube cube = cubePool(trafficPool);
        BehaviorDetails details = new BehaviorDetails();
        details.setBehave(behave);
        details.setCtime(System.currentTimeMillis());
        details.setItem(item);
        details.setPerson(person);
        details.setAttachment(attachment);

        cube.saveDoc(BehaviorDetails._COL_NAME, new TupleDocument<>(details));

        ItemBehavior behavior = getItemBehavior(trafficPool, item);
        switch (behave) {
            case "like":
                updateLikes(trafficPool, item, behavior.getLikes() + 1);
                break;
            case "comment":
                updateComments(trafficPool, item, behavior.getComments() + 1);
                break;
            case "recommend":
                updateRecommends(trafficPool, item, behavior.getRecommends() + 1);
                break;
        }
    }

    @Override
    public void removeBehave(String trafficPool, String item, String person, String behave) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        cube.deleteDocOne(BehaviorDetails._COL_NAME, String.format("{'tuple.item':'%s','tuple.behave':'%s','tuple.person':'%s'}", item, behave, person));
        ItemBehavior behavior = getItemBehavior(trafficPool, item);
        switch (behave) {
            case "like":
                updateLikes(trafficPool, item, behavior.getLikes() - 1);
                break;
            case "comment":
                updateComments(trafficPool, item, behavior.getComments() - 1);
                break;
            case "recommend":
                updateRecommends(trafficPool, item, behavior.getRecommends() - 1);
                break;
        }
    }

    @Override
    public long hasBehave(String principal, String pool, String item, String behave) throws CircuitException {
        ICube cube = cubePool(pool);
        return cube.tupleCount(BehaviorDetails._COL_NAME, String.format("{'tuple.item':'%s','tuple.behave':'%s','tuple.person':'%s'}", item, behave, principal));
    }

    @Override
    public List<BehaviorDetails> pageBehave(String trafficPool, String item, String behave, int limit, long offset) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple %s %s where {'tuple.item':'%s','tuple.behave':'%s'}",
                limit, offset,
                BehaviorDetails._COL_NAME, BehaviorDetails.class.getName(), item, behave);
        IQuery<BehaviorDetails> query = cube.createQuery(cjql);
        List<IDocument<BehaviorDetails>> list = query.getResultList();
        List<BehaviorDetails> behaviors = new ArrayList<>();
        for (IDocument<BehaviorDetails> document : list) {
            behaviors.add(document.tuple());
        }
        return behaviors;
    }

    @Override
    public List<ItemBehavior> pageBehavior(String trafficPool, int limit, long offset) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple %s %s where {}",
                limit, offset,
                ItemBehavior._COL_NAME_INNER, ItemBehavior.class.getName());
        IQuery<ItemBehavior> query = cube.createQuery(cjql);
        List<IDocument<ItemBehavior>> list = query.getResultList();
        List<ItemBehavior> behaviors = new ArrayList<>();
        for (IDocument<ItemBehavior> document : list) {
            behaviors.add(document.tuple());
        }
        return behaviors;
    }

    @Override
    public ItemBehavior getItemBehavior(String trafficPool, String item) throws CircuitException {
        ICube cube = cubePool(trafficPool);
        String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'tuple.item':'%s'}",
                ItemBehavior._COL_NAME_INNER, ItemBehavior.class.getName(), item);
        IQuery<ItemBehavior> query = cube.createQuery(cjql);
        IDocument<ItemBehavior> document = query.getSingleResult();
        if (document == null) {
            return addItemBehavior(cube, item, 0L, 0L, 0L);
        }
        return document.tuple();
    }

    @Override
    public ItemBehavior getItemInnateBehavior(String pool, String item) throws CircuitException {
        ICube cube = cubePool(pool);
        String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'tuple.item':'%s'}",
                ItemBehavior._COL_NAME_INNATE, ItemBehavior.class.getName(), item);
        IQuery<ItemBehavior> query = cube.createQuery(cjql);
        IDocument<ItemBehavior> document = query.getSingleResult();
        if (document == null) {
            return addItemBehavior(cube, item, 0L, 0L, 0L);
        }
        return document.tuple();
    }
}
