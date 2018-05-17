package com.hpd.butler.vo;

import com.hpd.butler.domain.SOrder;

import java.util.List;

public class OrderInfoVO extends SOrder{

    public List<OrderDateilVOS> orderDateilVOSList;

    public List<OrderDateilVOS> getOrderDateilVOSList() {
        return orderDateilVOSList;
    }

    public void setOrderDateilVOSList(List<OrderDateilVOS> orderDateilVOSList) {
        this.orderDateilVOSList = orderDateilVOSList;
    }
}
