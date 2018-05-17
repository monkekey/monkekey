package com.hpd.butler.domain;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "comment")
public class Comment {
    private long id;
    private String roomNo;
    private String innCode;
    private String customerServices;
    private Integer starLavel;
    private String remark;
    private Date creatTime;

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
    @Column(name = "roomNo", nullable = true, length = 50)
    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Basic
    @Column(name = "innCode", nullable = true, length = 50)
    public String getInnCode() {
        return innCode;
    }

    public void setInnCode(String innCode) {
        this.innCode = innCode;
    }

    @Basic
    @Column(name = "customerServices", nullable = true, length = 50)
    public String getCustomerServices() {
        return customerServices;
    }

    public void setCustomerServices(String customerServices) {
        this.customerServices = customerServices;
    }

    @Basic
    @Column(name = "starLavel", nullable = true)
    public Integer getStarLavel() {
        return starLavel;
    }

    public void setStarLavel(Integer starLavel) {
        this.starLavel = starLavel;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 500)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "creatTime", nullable = true)
    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != comment.id) return false;
        if (roomNo != null ? !roomNo.equals(comment.roomNo) : comment.roomNo != null) return false;
        if (innCode != null ? !innCode.equals(comment.innCode) : comment.innCode != null) return false;
        if (customerServices != null ? !customerServices.equals(comment.customerServices) : comment.customerServices != null)
            return false;
        if (starLavel != null ? !starLavel.equals(comment.starLavel) : comment.starLavel != null) return false;
        if (remark != null ? !remark.equals(comment.remark) : comment.remark != null) return false;
        if (creatTime != null ? !creatTime.equals(comment.creatTime) : comment.creatTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (roomNo != null ? roomNo.hashCode() : 0);
        result = 31 * result + (innCode != null ? innCode.hashCode() : 0);
        result = 31 * result + (customerServices != null ? customerServices.hashCode() : 0);
        result = 31 * result + (starLavel != null ? starLavel.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (creatTime != null ? creatTime.hashCode() : 0);
        return result;
    }
}
