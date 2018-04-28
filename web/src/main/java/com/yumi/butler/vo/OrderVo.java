package com.yumi.butler.vo;

import com.yumi.butler.domain.Order;

import java.util.List;

/**
 * Created by zy on 2018/2/27.
 */
public class OrderVo extends Order {
    private List<OrderDetailVo> orderDetails;

    public List<OrderDetailVo> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailVo> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
