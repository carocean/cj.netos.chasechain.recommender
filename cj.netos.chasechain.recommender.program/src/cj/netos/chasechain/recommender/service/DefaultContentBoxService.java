package cj.netos.chasechain.recommender.service;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.netos.chasechain.recommender.AbstractService;
import cj.netos.chasechain.recommender.ContentBox;
import cj.netos.chasechain.recommender.ContentBoxAssigner;
import cj.netos.chasechain.recommender.IContentBoxService;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;

import java.util.ArrayList;
import java.util.List;

@CjService(name = "defaultContentBoxService")
public class DefaultContentBoxService extends AbstractService implements IContentBoxService {
    @Override
    public ContentBox getContentBox(String pool, String box) throws CircuitException {
        ICube cube = cubePool(pool);
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple %s %s where {'tuple.id':'%s'}",
                ContentBox._COL_NAME, ContentBox.class.getName(), box);
        IQuery<ContentBox> query = cube.createQuery(cjql);
        IDocument<ContentBox> document = query.getSingleResult();
        if (document == null) {
            return null;
        }
        document.tuple().setPool(pool);
        return document.tuple();
    }

    @Override
    public List<ContentBox> pageContentBox(String pool, int limit, long offset) throws CircuitException {
        ICube cube = cubePool(pool);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple %s %s where {}",
                limit, offset, ContentBox._COL_NAME, ContentBox.class.getName());
        IQuery<ContentBox> query = cube.createQuery(cjql);
        List<IDocument<ContentBox>> documents = query.getResultList();
        List<ContentBox> boxes = new ArrayList<>();
        for (IDocument<ContentBox> boxIDocument : documents) {
            boxIDocument.tuple().setPool(pool);
            boxes.add(boxIDocument.tuple());
        }
        return boxes;
    }

    @Override
    public List<ContentBox> pageContentBoxOfProvider(String pool, String provider, int limit, long offset) throws CircuitException {
        ICube cube = cubePool(pool);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple %s %s where {'tuple.pointer.creator':'%s'}",
                limit, offset, ContentBox._COL_NAME, ContentBox.class.getName(), provider);
        IQuery<ContentBox> query = cube.createQuery(cjql);
        List<IDocument<ContentBox>> documents = query.getResultList();
        List<ContentBox> boxes = new ArrayList<>();
        for (IDocument<ContentBox> boxIDocument : documents) {
            boxIDocument.tuple().setPool(pool);
            boxes.add(boxIDocument.tuple());
        }
        return boxes;
    }

    @Override
    public List<ContentBoxAssigner> pageContentBoxByAssigner(String provider, int limit, long offset) throws CircuitException {
        ICube cube = home();
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple %s %s where {'tuple.pointer.creator':'%s'}",
                limit, offset, ContentBoxAssigner._COL_NAME, ContentBoxAssigner.class.getName(), provider);
        IQuery<ContentBoxAssigner> query = cube.createQuery(cjql);
        List<IDocument<ContentBoxAssigner>> documents = query.getResultList();
        List<ContentBoxAssigner> boxes = new ArrayList<>();
        for (IDocument<ContentBoxAssigner> boxIDocument : documents) {
            boxes.add(boxIDocument.tuple());
        }
        return boxes;
    }
}
