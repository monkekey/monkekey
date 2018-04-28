package com.yumi.butler.vo;

import java.math.BigDecimal;

/**
 * Created by zy on 2018/2/27.
 */
public class OrderDetailVo {
    private String orderNo;
    private Long goodsId;
    private String goodsName;
    private Integer qty;
    private BigDecimal salePrice;

    public OrderDetailVo(String orderNo, Long goodsId, String goodsName, Integer qty, BigDecimal salePrice) {
        this.orderNo = orderNo;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.qty = qty;
        this.salePrice = salePrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
}
