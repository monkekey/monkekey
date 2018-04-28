package com.yumi.butler.vo;

import com.yumi.butler.domain.SCommodity;
import com.yumi.butler.domain.SOrderDetail;

public class OrderDateilVOS extends SOrderDetail {

    public SCommodity sCommodity;

    public SCommodity getsCommodity() {
        return sCommodity;
    }

    public void setsCommodity(SCommodity sCommodity) {
        this.sCommodity = sCommodity;
    }
}
