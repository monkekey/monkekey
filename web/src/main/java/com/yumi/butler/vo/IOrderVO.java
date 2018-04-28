package com.yumi.butler.vo;

import com.yumi.butler.domain.SOrder;
import com.yumi.butler.domain.SOrderDetail;

import java.util.List;

public class IOrderVO extends SOrder {

    private List<SOrderDetail> sOrderDetails;

    public List<SOrderDetail> getsOrderDetails() {
        return sOrderDetails;
    }

    public void setsOrderDetails(List<SOrderDetail> sOrderDetails) {
        this.sOrderDetails = sOrderDetails;
    }
}
