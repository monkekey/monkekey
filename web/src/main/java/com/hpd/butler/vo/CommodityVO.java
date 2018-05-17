package com.hpd.butler.vo;

import java.math.BigDecimal;

public class CommodityVO {

    private Long id;
    private String categoryName;
    private String commodityName;
    private String commodityImg;
    private String commoditySynopsis;
    private BigDecimal commodityPrice;
    private BigDecimal commodityCost;
    private Integer commodityInventory;
    private Integer totalCount;
    private String commodityCode;
    private String categoryCode;
    private String commodityUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityImg() {
        return commodityImg;
    }

    public void setCommodityImg(String commodityImg) {
        this.commodityImg = commodityImg;
    }

    public String getCommoditySynopsis() {
        return commoditySynopsis;
    }

    public void setCommoditySynopsis(String commoditySynopsis) {
        this.commoditySynopsis = commoditySynopsis;
    }

    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public BigDecimal getCommodityCost() {
        return commodityCost;
    }

    public void setCommodityCost(BigDecimal commodityCost) {
        this.commodityCost = commodityCost;
    }

    public Integer getCommodityInventory() {
        return commodityInventory;
    }

    public void setCommodityInventory(Integer commodityInventory) {
        this.commodityInventory = commodityInventory;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCommodityUnit() {
        return commodityUnit;
    }

    public void setCommodityUnit(String commodityUnit) {
        this.commodityUnit = commodityUnit;
    }
}
