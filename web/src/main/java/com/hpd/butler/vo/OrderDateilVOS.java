package com.hpd.butler.vo;

import com.hpd.butler.domain.SCommodity;
import com.hpd.butler.domain.SOrderDetail;

public class OrderDateilVOS extends SOrderDetail {

    public SCommodity sCommodity;

    public SCommodity getsCommodity() {
        return sCommodity;
    }

    public void setsCommodity(SCommodity sCommodity) {
        this.sCommodity = sCommodity;
    }
}
