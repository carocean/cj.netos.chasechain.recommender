package cj.netos.chasechain.recommender.ports;

import cj.netos.chasechain.recommender.*;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.List;

@CjOpenports(usage = "追链流量池服务")
public interface ITrafficPoolPorts extends IOpenportService {

    @CjOpenport(usage = "获取国家级流量池")
    TrafficPool getCountryPool(ISecuritySession securitySession) throws CircuitException;

    @CjOpenport(usage = "获取下级级流量池")
    List<TrafficPool> pageChildrenPool(ISecuritySession securitySession,
                                       @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                       @CjOpenportParameter(usage = "分页大小", name = "limit") int limit,
                                       @CjOpenportParameter(usage = "页码", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "获取流量池中的内容提供者人数")
    long countContentProvidersOfPool(ISecuritySession securitySession,
                                     @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool
    ) throws CircuitException;

    @CjOpenport(usage = "获取指定流量池")
    TrafficPool getPool(ISecuritySession securitySession,
                        @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool
    ) throws CircuitException;

    @CjOpenport(usage = "获取内容盒")
    ContentBox getContentBox(ISecuritySession securitySession,
                             @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                             @CjOpenportParameter(usage = "内容盒标识", name = "box") String box
    ) throws CircuitException;

    @CjOpenport(usage = "分页获取指定流量池中的内容盒")
    List<ContentBox> pageContentBox(ISecuritySession securitySession,
                                    @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                    @CjOpenportParameter(usage = "分页大小", name = "limit") int limit,
                                    @CjOpenportParameter(usage = "页码", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "获取流量池中的内容盒的内容物数")
    long countContentItemsOfBox(ISecuritySession securitySession,
                                @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                @CjOpenportParameter(usage = "内容盒标识", name = "box") String box
    ) throws CircuitException;

    @CjOpenport(usage = "获取流量池中的内容物数")
    long countContentItemsOfPool(ISecuritySession securitySession,
                                 @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool
    ) throws CircuitException;

    @CjOpenport(usage = "获取内容物，用于跟根据内容物的upstreamPool追溯")
    ContentItem getContentItem(ISecuritySession securitySession,
                               @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                               @CjOpenportParameter(usage = "内容物标识", name = "item") String item
    ) throws CircuitException;

    @CjOpenport(usage = "分页获取指定流量池的内容物")
    List<ContentItem> pageContentItemOfBox(ISecuritySession securitySession,
                                           @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                           @CjOpenportParameter(usage = "内容盒标识", name = "box") String box,
                                           @CjOpenportParameter(usage = "分页大小", name = "limit") int limit,
                                           @CjOpenportParameter(usage = "页码", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "分页获取指定流量池中内容提供商的供的内容物")
    List<ContentItem> pageContentItemOfProvider(ISecuritySession securitySession,
                                                @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                                @CjOpenportParameter(usage = "内容提供商", name = "provider") String provider,
                                                @CjOpenportParameter(usage = "分页大小", name = "limit") int limit,
                                                @CjOpenportParameter(usage = "页码", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "分页获取指定流量池的内容物")
    List<ContentItem> pageContentItem(ISecuritySession securitySession,
                                      @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                      @CjOpenportParameter(usage = "分页大小", name = "limit") int limit,
                                      @CjOpenportParameter(usage = "页码", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "分页获取指定流量池的内容提供者")
    List<String> pageContentProvider(ISecuritySession securitySession,
                                     @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                     @CjOpenportParameter(usage = "分页大小", name = "limit") int limit,
                                     @CjOpenportParameter(usage = "页码", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "获取流量池的流量仪表盘")
    TrafficeBashboradResult getTrafficDashboard(ISecuritySession securitySession,
                                                @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool
    ) throws CircuitException;

    @CjOpenport(usage = "获取内容物的先天行为")
    ItemBehavior getItemInnateBehavior(ISecuritySession securitySession,
                                       @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                       @CjOpenportParameter(usage = "内容物标识", name = "item") String item
    ) throws CircuitException;

    @CjOpenport(usage = "获取内容物的内部行为")
    ItemBehavior getItemInnerBehavior(ISecuritySession securitySession,
                                      @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                      @CjOpenportParameter(usage = "内容物标识", name = "item") String item
    ) throws CircuitException;

    @CjOpenport(usage = "分页获取内容物的行为明细")
    List<BehaviorDetails> pageBehave(ISecuritySession securitySession,
                                     @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                                     @CjOpenportParameter(usage = "内容物标识", name = "item") String item,
                                     @CjOpenportParameter(usage = "行为，仅支持：like,comment,recommend", name = "behave") String behave,
                                     @CjOpenportParameter(usage = "分页大小", name = "limit") int limit,
                                     @CjOpenportParameter(usage = "页码", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "判断用户在某物上已具有的行为数")
    long hasBehave(ISecuritySession securitySession,
                   @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                   @CjOpenportParameter(usage = "内容物标识", name = "item") String item,
                   @CjOpenportParameter(usage = "行为，仅支持：like,comment,recommend", name = "behave") String behave
    ) throws CircuitException;

    @CjOpenport(usage = "用户做出某种行为")
    void doBehave(ISecuritySession securitySession,
                  @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                  @CjOpenportParameter(usage = "内容物标识", name = "item") String item,
                  @CjOpenportParameter(usage = "行为，仅支持：like,comment", name = "behave") String behave,
                  @CjOpenportParameter(usage = "行为附件，如like为空，comment为评论内容", name = "attachment") String attachment
    ) throws CircuitException;

    @CjOpenport(usage = "用户取消某种行为")
    void undoBehave(ISecuritySession securitySession,
                    @CjOpenportParameter(usage = "流量池标识", name = "pool") String pool,
                    @CjOpenportParameter(usage = "内容物标识", name = "item") String item,
                    @CjOpenportParameter(usage = "行为，仅支持：like,comment", name = "behave") String behave
    ) throws CircuitException;
}
