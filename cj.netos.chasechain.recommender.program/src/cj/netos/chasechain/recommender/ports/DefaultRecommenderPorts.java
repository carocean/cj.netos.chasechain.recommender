package cj.netos.chasechain.recommender.ports;

import cj.netos.chasechain.recommender.ContentItem;
import cj.netos.chasechain.recommender.IRecommenderService;
import cj.netos.chasechain.recommender.RecommenderConfig;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@CjService(name = "/recommender.ports")
public class DefaultRecommenderPorts implements IRecommenderPorts {
    @CjServiceRef(refByName = "defaultRecommenderService")
    IRecommenderService recommenderService;

    @Override
    public void configGlobalRecommender(ISecuritySession securitySession,
                                        int maxRecommendItemCount,
                                        BigDecimal countryRecommendWeight,
                                        BigDecimal normalRecommendWeight,
                                        BigDecimal provinceRecommendWeight,
                                        BigDecimal cityRecommendWeight,
                                        BigDecimal districtRecommendWeight,
                                        BigDecimal townRecommendWeight
    ) throws CircuitException {
        if (!securitySession.roleIn("platform:administrators")) {
            throw new CircuitException("801", "无权访问");
        }
        if (maxRecommendItemCount < 10) {
            throw new CircuitException("500", "maxRecommendItemCount<10");
        }
        if (countryRecommendWeight == null || countryRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "countryRecommendWeight参数为空或小于1");
        }
        if (normalRecommendWeight == null || normalRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "normalRecommendWeight参数为空或小于1");
        }
        if (provinceRecommendWeight == null || provinceRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "provinceRecommendWeight参数为空或小于1");
        }
        if (cityRecommendWeight == null || cityRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "cityRecommendWeight参数为空或小于1");
        }
        if (districtRecommendWeight == null || districtRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "districtRecommendWeight参数为空或小于1");
        }
        if (townRecommendWeight == null || townRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "townRecommendWeight参数为空或小于1");
        }

        RecommenderConfig config = new RecommenderConfig();

        config.setMaxRecommendItemCount(maxRecommendItemCount);

        config.setCountryRecommendWeight(countryRecommendWeight);
        config.setProvinceRecommendWeight(provinceRecommendWeight);
        config.setCityRecommendWeight(cityRecommendWeight);
        config.setDistrictRecommendWeight(districtRecommendWeight);
        config.setTownRecommendWeight(townRecommendWeight);

        config.setNormalRecommendWeight(normalRecommendWeight);

        BigDecimal total = countryRecommendWeight
                .add(provinceRecommendWeight)
                .add(cityRecommendWeight)
                .add(districtRecommendWeight)
                .add(townRecommendWeight)
                .add(normalRecommendWeight);
        BigDecimal weightCapacity = new BigDecimal(maxRecommendItemCount).divide(total, 4, RoundingMode.HALF_DOWN);
        config.setWeightCapacity(weightCapacity);
        CJSystem.logging().info(getClass(), String.format("每权容量:weightCapacity=%s", weightCapacity));
        recommenderService.configGlobal(config);
    }

    @Override
    public RecommenderConfig getGlobalRecommenderConfig(ISecuritySession securitySession) throws CircuitException {
        return recommenderService.getGlobalConfig();
    }

    @Override
    public RecommenderConfig getPersonRecommenderConfig(ISecuritySession securitySession) throws CircuitException {
        return recommenderService.getPersonConfig(securitySession.principal());
    }

    @Override
    public void configPersonRecommender(ISecuritySession securitySession, int maxRecommendItemCount, BigDecimal countryRecommendWeight, BigDecimal normalRecommendWeight, BigDecimal provinceRecommendWeight, BigDecimal cityRecommendWeight, BigDecimal districtRecommendWeight, BigDecimal townRecommendWeight) throws CircuitException {
        if (maxRecommendItemCount < 10) {
            throw new CircuitException("500", "maxRecommendItemCount<10");
        }
        if (countryRecommendWeight == null || countryRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "countryRecommendWeight参数为空或小于1");
        }
        if (normalRecommendWeight == null || normalRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "normalRecommendWeight参数为空或小于1");
        }
        if (provinceRecommendWeight == null || provinceRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "provinceRecommendWeight参数为空或小于1");
        }
        if (cityRecommendWeight == null || cityRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "cityRecommendWeight参数为空或小于1");
        }
        if (districtRecommendWeight == null || districtRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "districtRecommendWeight参数为空或小于1");
        }
        if (townRecommendWeight == null || townRecommendWeight.compareTo(new BigDecimal("0.0")) < -1) {
            throw new CircuitException("500", "townRecommendWeight参数为空或小于1");
        }

        RecommenderConfig config = new RecommenderConfig();

        config.setMaxRecommendItemCount(maxRecommendItemCount);

        config.setCountryRecommendWeight(countryRecommendWeight);
        config.setProvinceRecommendWeight(provinceRecommendWeight);
        config.setCityRecommendWeight(cityRecommendWeight);
        config.setDistrictRecommendWeight(districtRecommendWeight);
        config.setTownRecommendWeight(townRecommendWeight);

        config.setNormalRecommendWeight(normalRecommendWeight);

        BigDecimal total = countryRecommendWeight
                .add(provinceRecommendWeight)
                .add(cityRecommendWeight)
                .add(districtRecommendWeight)
                .add(townRecommendWeight)
                .add(normalRecommendWeight);
        BigDecimal weightCapacity = new BigDecimal(maxRecommendItemCount).divide(total, 4, RoundingMode.HALF_DOWN);
        config.setWeightCapacity(weightCapacity);
        recommenderService.configPerson(securitySession.principal(), config);
    }

    @Override
    public List<ContentItem> pullItem(ISecuritySession securitySession, String towncode) throws CircuitException {
        return recommenderService.pullItem(securitySession.principal(), towncode);
    }
}
