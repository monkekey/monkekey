package com.hpd.butler.vo;

import com.hpd.butler.domain.SOrder;
import com.hpd.butler.domain.SOrderDetail;

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
