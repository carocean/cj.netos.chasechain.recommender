package cj.netos.chasechain.recommender.ports;

import cj.netos.chasechain.recommender.ContentItem;
import cj.netos.chasechain.recommender.RecommenderConfig;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.math.BigDecimal;
import java.util.List;

@CjOpenports(usage = "追链推荐服务")
public interface IRecommenderPorts extends IOpenportService {

    @CjOpenport(usage = "配置推荐级别。注：所有级别推荐权重默认是1，可以是大于等于1的任意值")
    void configGlobalRecommender(ISecuritySession securitySession,
                                 @CjOpenportParameter(usage = "最大每次推荐的内空数", name = "maxRecommendItemCount") int maxRecommendItemCount,
                                 @CjOpenportParameter(usage = "国家级别的推荐权重", name = "countryRecommendWeight") BigDecimal countryRecommendWeight,
                                 @CjOpenportParameter(usage = "常规级别的推荐权重", name = "normalRecommendWeight") BigDecimal normalRecommendWeight,
                                 @CjOpenportParameter(usage = "省级别的推荐权重", name = "provinceRecommendWeight") BigDecimal provinceRecommendWeight,
                                 @CjOpenportParameter(usage = "市级别的推荐权重", name = "cityRecommendWeight") BigDecimal cityRecommendWeight,
                                 @CjOpenportParameter(usage = "区县级别的推荐权重", name = "districtRecommendWeight") BigDecimal districtRecommendWeight,
                                 @CjOpenportParameter(usage = "乡镇级别的推荐权重", name = "townRecommendWeight") BigDecimal townRecommendWeight
    ) throws CircuitException;

    @CjOpenport(usage = "获取全局推荐配置")
    RecommenderConfig getGlobalRecommenderConfig(ISecuritySession securitySession) throws CircuitException;

    @CjOpenport(usage = "获取个人推荐配置")
    RecommenderConfig getPersonRecommenderConfig(ISecuritySession securitySession) throws CircuitException;

    @CjOpenport(usage = "重置个人推荐配置")
    void configPersonRecommender(ISecuritySession securitySession,
                                 @CjOpenportParameter(usage = "最大每次推荐的内空数", name = "maxRecommendItemCount") int maxRecommendItemCount,
                                 @CjOpenportParameter(usage = "国家级别的推荐权重", name = "countryRecommendWeight") BigDecimal countryRecommendWeight,
                                 @CjOpenportParameter(usage = "常规级别的推荐权重", name = "normalRecommendWeight") BigDecimal normalRecommendWeight,
                                 @CjOpenportParameter(usage = "省级别的推荐权重", name = "provinceRecommendWeight") BigDecimal provinceRecommendWeight,
                                 @CjOpenportParameter(usage = "市级别的推荐权重", name = "cityRecommendWeight") BigDecimal cityRecommendWeight,
                                 @CjOpenportParameter(usage = "区县级别的推荐权重", name = "districtRecommendWeight") BigDecimal districtRecommendWeight,
                                 @CjOpenportParameter(usage = "乡镇级别的推荐权重", name = "townRecommendWeight") BigDecimal townRecommendWeight
    ) throws CircuitException;

    @CjOpenport(usage = "拉取内容物")
    List<ContentItem> pullItem(ISecuritySession securitySession,
                               @CjOpenportParameter(usage = "乡镇地理编码", name = "towncode") String towncode) throws CircuitException;

}
