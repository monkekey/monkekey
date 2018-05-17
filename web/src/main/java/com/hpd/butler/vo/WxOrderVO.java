package com.hpd.butler.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WxOrderVO {

    private List<WxOrderDetailVO> wxOrderDetailVOS;
    private Integer sum;
    private BigDecimal price;
    private String payNumber;
    private String hotelId;
    private String room;
    private String createBy;
    private Date createTime;
    private String openId;

    public List<WxOrderDetailVO> getWxOrderDetailVOS() {
        return wxOrderDetailVOS;
    }

    public void setWxOrderDetailVOS(List<WxOrderDetailVO> wxOrderDetailVOS) {
        this.wxOrderDetailVOS = wxOrderDetailVOS;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
