package com.yumi.butler.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zy on 2017/10/9.
 */
@Entity
@Table(name = "inn", uniqueConstraints={
        @UniqueConstraint(columnNames={"innCode", "flag"})
})
public class Inn {
    private long id;
    private String innCode;
    private String innName;
    private String innDesc;
    private String address;
    private String phone;
    private Integer flag;
    private String createBy;
    private Date createTime;
    private String lastModifyBy;
    private Date lastModifyTime;
    private BigDecimal longitude;
    private BigDecimal latitude;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "InnCode", nullable = true, length = 50)
    public String getInnCode() {
        return innCode;
    }

    public void setInnCode(String innCode) {
        this.innCode = innCode;
    }

    @Basic
    @Column(name = "InnName", nullable = true, length = 100)
    public String getInnName() {
        return innName;
    }

    public void setInnName(String innName) {
        this.innName = innName;
    }

    @Basic
    @Column(name = "InnDesc", nullable = true, length = 500)
    public String getInnDesc() {
        return innDesc;
    }

    public void setInnDesc(String innDesc) {
        this.innDesc = innDesc;
    }

    @Basic
    @Column(name = "Address", nullable = true, length = 500)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "Phone", nullable = true, length = 20)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "Flag", nullable = true)
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Basic
    @Column(name = "CreateBy", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Basic
    @Column(name = "CreateTime", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "LastModifyBy", nullable = true, length = 50)
    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    @Basic
    @Column(name = "LastModifyTime", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inn inn = (Inn) o;

        if (id != inn.id) return false;
        if (innCode != null ? !innCode.equals(inn.innCode) : inn.innCode != null) return false;
        if (innName != null ? !innName.equals(inn.innName) : inn.innName != null) return false;
        if (innDesc != null ? !innDesc.equals(inn.innDesc) : inn.innDesc != null) return false;
        if (address != null ? !address.equals(inn.address) : inn.address != null) return false;
        if (phone != null ? !phone.equals(inn.phone) : inn.phone != null) return false;
        if (flag != null ? !flag.equals(inn.flag) : inn.flag != null) return false;
        if (createBy != null ? !createBy.equals(inn.createBy) : inn.createBy != null) return false;
        if (createTime != null ? !createTime.equals(inn.createTime) : inn.createTime != null) return false;
        if (lastModifyBy != null ? !lastModifyBy.equals(inn.lastModifyBy) : inn.lastModifyBy != null) return false;
        if (lastModifyTime != null ? !lastModifyTime.equals(inn.lastModifyTime) : inn.lastModifyTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (innCode != null ? innCode.hashCode() : 0);
        result = 31 * result + (innName != null ? innName.hashCode() : 0);
        result = 31 * result + (innDesc != null ? innDesc.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastModifyBy != null ? lastModifyBy.hashCode() : 0);
        result = 31 * result + (lastModifyTime != null ? lastModifyTime.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "longitude", nullable = true, precision = 6)
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "latitude", nullable = true, precision = 6)
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}
