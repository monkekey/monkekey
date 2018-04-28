package com.yumi.butler.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "s_order", schema = "Bee", catalog = "")
public class SOrder {
    private long id;
    private String orderNumber;
    private long orderCountNumber;
    private BigDecimal orderSumPrice;
    private String orderPaymentNumber;
    private String orderHotel;
    private String orderRoom;
    private String orderCreateby;
    private Date orderCreatetime;
    private String orderOpenid;
    private String orderDeliveryby;
    private Date orderDeliverytime;
    private String orderRemark;
    private Integer orderStatus;
    private Integer flag;

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
    @Column(name = "order_sum_price", nullable = true, precision = 2)
    public BigDecimal getOrderSumPrice() {
        return orderSumPrice;
    }

    public void setOrderSumPrice(BigDecimal orderSumPrice) {
        this.orderSumPrice = orderSumPrice;
    }

    @Basic
    @Column(name = "order_payment_number", nullable = true, length = 200)
    public String getOrderPaymentNumber() {
        return orderPaymentNumber;
    }

    public void setOrderPaymentNumber(String orderPaymentNumber) {
        this.orderPaymentNumber = orderPaymentNumber;
    }

    @Basic
    @Column(name = "order_hotel", nullable = true, length = 50)
    public String getOrderHotel() {
        return orderHotel;
    }

    public void setOrderHotel(String orderHotel) {
        this.orderHotel = orderHotel;
    }

    @Basic
    @Column(name = "order_room", nullable = true, length = 50)
    public String getOrderRoom() {
        return orderRoom;
    }

    public void setOrderRoom(String orderRoom) {
        this.orderRoom = orderRoom;
    }

    @Basic
    @Column(name = "order_createby", nullable = true, length = 50)
    public String getOrderCreateby() {
        return orderCreateby;
    }

    public void setOrderCreateby(String orderCreateby) {
        this.orderCreateby = orderCreateby;
    }

    @Basic
    @Column(name = "order_createtime", nullable = true)
    public Date getOrderCreatetime() {
        return orderCreatetime;
    }

    public void setOrderCreatetime(Date orderCreatetime) {
        this.orderCreatetime = orderCreatetime;
    }

    @Basic
    @Column(name = "order_openid", nullable = true)
    public String getOrderOpenid() {
        return orderOpenid;
    }

    public void setOrderOpenid(String orderOpenid) {
        this.orderOpenid = orderOpenid;
    }

    @Basic
    @Column(name = "order_deliveryby", nullable = true, length = 50)
    public String getOrderDeliveryby() {
        return orderDeliveryby;
    }

    public void setOrderDeliveryby(String orderDeliveryby) {
        this.orderDeliveryby = orderDeliveryby;
    }

    @Basic
    @Column(name = "order_deliverytime", nullable = true)
    public Date getOrderDeliverytime() {
        return orderDeliverytime;
    }

    public void setOrderDeliverytime(Date orderDeliverytime) {
        this.orderDeliverytime = orderDeliverytime;
    }

    @Basic
    @Column(name = "order_status", nullable = true)
    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Basic
    @Column(name = "flag", nullable = true)
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Basic
    @Column(name = "order_remark", nullable = true)
    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    @Basic
    @Column(name = "order_count_number", nullable = true)
    public long getOrderCountNumber() {
        return orderCountNumber;
    }

    public void setOrderCountNumber(long orderCountNumber) {
        this.orderCountNumber = orderCountNumber;
    }

    private String hotelName;

    //不关联数据库
    @Transient
    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
}
