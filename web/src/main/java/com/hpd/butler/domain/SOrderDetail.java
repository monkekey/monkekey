package com.hpd.butler.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "s_order_detail", schema = "Bee", catalog = "")
public class SOrderDetail {
    private long id;
    private String orderNumber;
    private String categoryCode;
    private String categoryName;
    private String commodityName;
    private String commodityImg;
    private String commodityCode;
    private BigDecimal commodityPrice;
    private BigDecimal commoditySumPrice;
    private Long commodityAmount;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "order_number", nullable = true, length = 200)
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Basic
    @Column(name = "category_id", nullable = true)
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Basic
    @Column(name = "commodity_id", nullable = true)
    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    @Basic
    @Column(name = "commodity_price", nullable = true, precision = 2)
    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    @Basic
    @Column(name = "commodity_sum_price", nullable = true, precision = 2)
    public BigDecimal getCommoditySumPrice() {
        return commoditySumPrice;
    }

    public void setCommoditySumPrice(BigDecimal commoditySumPrice) {
        this.commoditySumPrice = commoditySumPrice;
    }

    @Basic
    @Column(name = "commodity_amount", nullable = true)
    public Long getCommodityAmount() {
        return commodityAmount;
    }

    public void setCommodityAmount(Long commodityAmount) {
        this.commodityAmount = commodityAmount;
    }

    @Basic
    @Column(name = "category_name", nullable = true)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Basic
    @Column(name = "commodity_name", nullable = true)
    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    @Basic
    @Column(name = "commodity_img", nullable = true)
    public String getCommodityImg() {
        return commodityImg;
    }

    public void setCommodityImg(String commodityImg) {
        this.commodityImg = commodityImg;
    }
}
