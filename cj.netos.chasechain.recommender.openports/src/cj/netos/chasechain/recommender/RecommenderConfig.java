package cj.netos.chasechain.recommender;

import java.math.BigDecimal;
//默认每权为1，总数除以1*6即为每权容量，那么如乡镇级就为：(1*每权容量)*总数
public class RecommenderConfig {
    public final static transient String _COL_NAME = "chasechain.config.recommender";
    //最大每次推荐的内空数
    int maxRecommendItemCount;
    BigDecimal countryRecommendWeight;//国家级别的推荐权重
    BigDecimal normalRecommendWeight;//常规级别的推荐权重
    BigDecimal provinceRecommendWeight;//省级别的推荐权重
    BigDecimal cityRecommendWeight;//市级别的推荐权重
    BigDecimal districtRecommendWeight;//区县级别的推荐权重
    BigDecimal townRecommendWeight;//乡镇级别的推荐权重
    BigDecimal weightCapacity;//每权可分配的内容数

    public BigDecimal getWeightCapacity() {
        return weightCapacity;
    }

    public void setWeightCapacity(BigDecimal weightCapacity) {
        this.weightCapacity = weightCapacity;
    }

    public int getMaxRecommendItemCount() {
        return maxRecommendItemCount;
    }

    public void setMaxRecommendItemCount(int maxRecommendItemCount) {
        this.maxRecommendItemCount = maxRecommendItemCount;
    }

    public BigDecimal getCountryRecommendWeight() {
        return countryRecommendWeight;
    }

    public void setCountryRecommendWeight(BigDecimal countryRecommendWeight) {
        this.countryRecommendWeight = countryRecommendWeight;
    }

    public BigDecimal getNormalRecommendWeight() {
        return normalRecommendWeight;
    }

    public void setNormalRecommendWeight(BigDecimal normalRecommendWeight) {
        this.normalRecommendWeight = normalRecommendWeight;
    }

    public BigDecimal getProvinceRecommendWeight() {
        return provinceRecommendWeight;
    }

    public void setProvinceRecommendWeight(BigDecimal provinceRecommendWeight) {
        this.provinceRecommendWeight = provinceRecommendWeight;
    }

    public BigDecimal getCityRecommendWeight() {
        return cityRecommendWeight;
    }

    public void setCityRecommendWeight(BigDecimal cityRecommendWeight) {
        this.cityRecommendWeight = cityRecommendWeight;
    }

    public BigDecimal getDistrictRecommendWeight() {
        return districtRecommendWeight;
    }

    public void setDistrictRecommendWeight(BigDecimal districtRecommendWeight) {
        this.districtRecommendWeight = districtRecommendWeight;
    }

    public BigDecimal getTownRecommendWeight() {
        return townRecommendWeight;
    }

    public void setTownRecommendWeight(BigDecimal townRecommendWeight) {
        this.townRecommendWeight = townRecommendWeight;
    }
}
