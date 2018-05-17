package com.hpd.butler.vo;

import com.hpd.butler.domain.SHotelCommodity;

import java.util.List;

public class HotelCommodityVO {

    private String hotelId;
    private String hotelCategoryList;
    private List<SHotelCommodity> hotelCommoditylist;

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelCategoryList() {
        return hotelCategoryList;
    }

    public void setHotelCategoryList(String hotelCategoryList) {
        this.hotelCategoryList = hotelCategoryList;
    }

    public List<SHotelCommodity> getHotelCommoditylist() {
        return hotelCommoditylist;
    }

    public void setHotelCommoditylist(List<SHotelCommodity> hotelCommoditylist) {
        this.hotelCommoditylist = hotelCommoditylist;
    }
}
