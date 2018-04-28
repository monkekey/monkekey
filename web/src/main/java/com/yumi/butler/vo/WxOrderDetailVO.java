package com.yumi.butler.vo;

import java.math.BigDecimal;

public class WxOrderDetailVO {

    private String categoryCode;
    private String categoryName;
    private String commodityCode;
    private BigDecimal commodityCost;
    private String commodityImg;
    private String commodityInventory;
    private String commodityName;
    private BigDecimal commodityPrice;
    private Long commoditySum;
    private String commoditySynopsis;
    private BigDecimal pirceSum;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public BigDecimal getCommodityCost() {
        return commodityCost;
    }

    public void setCommodityCost(BigDecimal commodityCost) {
        this.commodityCost = commodityCost;
    }

    public String getCommodityImg() {
        return commodityImg;
    }

    public void setCommodityImg(String commodityImg) {
        this.commodityImg = commodityImg;
    }

    public String getCommodityInventory() {
        return commodityInventory;
    }

    public void setCommodityInventory(String commodityInventory) {
        this.commodityInventory = commodityInventory;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public Long getCommoditySum() {
        return commoditySum;
    }

    public void setCommoditySum(Long commoditySum) {
        this.commoditySum = commoditySum;
    }

    public String getCommoditySynopsis() {
        return commoditySynopsis;
    }

    public void setCommoditySynopsis(String commoditySynopsis) {
        this.commoditySynopsis = commoditySynopsis;
    }

    public BigDecimal getPirceSum() {
        return pirceSum;
    }

    public void setPirceSum(BigDecimal pirceSum) {
        this.pirceSum = pirceSum;
    }
}
