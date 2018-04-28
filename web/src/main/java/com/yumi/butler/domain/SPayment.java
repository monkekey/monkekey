package com.yumi.butler.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "s_payment", schema = "Bee", catalog = "")
public class SPayment {
    private long id;
    private String paymentNumber;
    private String openid;
    private BigDecimal paymentPrice;
    private String paymentCreateby;
    private Date paymentCreatetime;
    private Integer paymentStatus;
    private Integer flag;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "payment_number", nullable = true, length = 200)
    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    @Basic
    @Column(name = "payment_price", nullable = true, precision = 2)
    public BigDecimal getPaymentPrice() {
        return paymentPrice;
    }

    public void setPaymentPrice(BigDecimal paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    @Basic
    @Column(name = "payment_createby", nullable = true, length = 50)
    public String getPaymentCreateby() {
        return paymentCreateby;
    }

    public void setPaymentCreateby(String paymentCreateby) {
        this.paymentCreateby = paymentCreateby;
    }

    @Basic
    @Column(name = "payment_createtime", nullable = true)
    public Date getPaymentCreatetime() {
        return paymentCreatetime;
    }

    public void setPaymentCreatetime(Date paymentCreatetime) {
        this.paymentCreatetime = paymentCreatetime;
    }

    @Basic
    @Column(name = "payment_status", nullable = true)
    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
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
    @Column(name = "payment_openId", nullable = true)
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
