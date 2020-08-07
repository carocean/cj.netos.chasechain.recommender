package cj.netos.chasechain.recommender.ports;

import cj.netos.chasechain.recommender.*;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/trafficPool.ports")
public class DefaultTrafficPoolPorts implements ITrafficPoolPorts {

    @CjServiceRef(refByName = "defaultTrafficPoolService")
    ITrafficPoolService trafficPoolService;
    @CjServiceRef(refByName = "defaultContentBoxService")
    IContentBoxService contentBoxService;
    @CjServiceRef(refByName = "defaultContentItemService")
    IContentItemService contentItemService;

    @CjServiceRef(refByName = "defaultItemBehaviorService")
    IItemBehaviorService itemBehaviorService;

    @Override
    public TrafficPool getCountryPool(ISecuritySession securitySession) throws CircuitException {
        return trafficPoolService.getCountryPool();
    }

    @Override
    public List<TrafficPool> pageChildrenPool(ISecuritySession securitySession, String pool, int limit, long offset) throws CircuitException {
        return trafficPoolService.pageChildrenPool(pool, limit, offset);
    }

    @Override
    public long countContentProvidersOfPool(ISecuritySession securitySession, String pool) throws CircuitException {
        return trafficPoolService.countContentProvidersOfPool(pool);
    }

    @Override
    public TrafficPool getPool(ISecuritySession securitySession, String pool) throws CircuitException {
        return trafficPoolService.getPool(pool);
    }

    @Override
    public ContentBox getContentBox(ISecuritySession securitySession, String pool, String box) throws CircuitException {
        return contentBoxService.getContentBox(pool, box);
    }

    @Override
    public List<ContentBox> pageContentBox(ISecuritySession securitySession, String pool, int limit, long offset) throws CircuitException {
        return contentBoxService.pageContentBox(pool, limit, offset);
    }

    @Override
    public long countContentItemsOfBox(ISecuritySession securitySession, String pool, String box) throws CircuitException {
        return contentItemService.countContentItemsOfBox(pool, box);
    }

    @Override
    public long countContentItemsOfPool(ISecuritySession securitySession, String pool) throws CircuitException {
        return contentItemService.countContentItemsOfPool(pool);
    }

    @Override
    public ContentItem getContentItem(ISecuritySession securitySession, String pool, String item) throws CircuitException {
        return contentItemService.getContentItem(pool, item);
    }

    @Override
    public List<ContentItem> pageContentItemOfBox(ISecuritySession securitySession, String pool, String box, int limit, long offset) throws CircuitException {
        return contentItemService.pageContentItemOfBox(pool, box, limit, offset);
    }

    @Override
    public List<ContentItem> pageContentItemOfProvider(ISecuritySession securitySession, String pool, String provider, int limit, long offset) throws CircuitException {
        return contentItemService.pageContentItemOfProvider(pool, provider, limit, offset);
    }

    @Override
    public List<ContentItem> pageContentItem(ISecuritySession securitySession, String pool, int limit, long offset) throws CircuitException {
        return contentItemService.pageContentItem(pool, limit, offset);
    }

    @Override
    public List<String> pageContentProvider(ISecuritySession securitySession, String pool, int limit, long offset) throws CircuitException {
        return trafficPoolService.pageContentProvider(pool, limit, offset);
    }

    @Override
    public TrafficeBashboradResult getTrafficDashboard(ISecuritySession securitySession, String pool) throws CircuitException {
        TrafficDashboardPointer pointer = trafficPoolService.getTrafficDashboard(pool);
        return pointer.copyTo(pool);
    }

    @Override
    public ItemBehavior getItemInnateBehavior(ISecuritySession securitySession, String pool, String item) throws CircuitException {
        return itemBehaviorService.getItemInnateBehavior(pool, item);
    }

    @Override
    public ItemBehavior getItemInnerBehavior(ISecuritySession securitySession, String pool, String item) throws CircuitException {
        return itemBehaviorService.getItemBehavior(pool, item);
    }

    @Override
    public List<BehaviorDetails> pageBehave(ISecuritySession securitySession, String pool, String item, String behave, int limit, long offset) throws CircuitException {
        return itemBehaviorService.pageBehave(pool, item, behave, limit, offset);
    }

    @Override
    public long hasBehave(ISecuritySession securitySession, String pool, String item, String behave) throws CircuitException {
        return itemBehaviorService.hasBehave(securitySession.principal(), pool, item, behave);
    }

    @Override
    public void doBehave(ISecuritySession securitySession, String pool, String item, String behave, String attachment) throws CircuitException {
        itemBehaviorService.addBehave(pool, item, securitySession.principal(), behave, attachment);
    }

    @Override
    public void undoBehave(ISecuritySession securitySession, String pool, String item, String behave) throws CircuitException {
        itemBehaviorService.removeBehave(pool, item, securitySession.principal(), behave);
    }
}
