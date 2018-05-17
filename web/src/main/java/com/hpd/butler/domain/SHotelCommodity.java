package com.hpd.butler.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "s_hotel_commodity", schema = "Bee", catalog = "")
public class SHotelCommodity {
    private long id;
    private String hotelId;
    private String categoryCode;
    private String commodityCode;
    private Long commodityInventory;
    private String createBy;
    private Date createTime;
    private String lastBy;
    private Date lastTime;
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
    @Column(name = "hotel_id", nullable = true)
    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    @Basic
    @Column(name = "category_code", nullable = true, length = 100)
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Basic
    @Column(name = "commodity_code", nullable = true, length = 100)
    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    @Basic
    @Column(name = "commodity_inventory", nullable = true)
    public Long getCommodityInventory() {
        return commodityInventory;
    }

    public void setCommodityInventory(Long commodityInventory) {
        this.commodityInventory = commodityInventory;
    }

    @Basic
    @Column(name = "create_by", nullable = true, length = 100)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "last_by", nullable = true, length = 100)
    public String getLastBy() {
        return lastBy;
    }

    public void setLastBy(String lastBy) {
        this.lastBy = lastBy;
    }

    @Basic
    @Column(name = "last_time", nullable = true)
    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    @Basic
    @Column(name = "flag", nullable = true)
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SHotelCommodity that = (SHotelCommodity) o;

        if (id != that.id) return false;
        if (hotelId != null ? !hotelId.equals(that.hotelId) : that.hotelId != null) return false;
        if (categoryCode != null ? !categoryCode.equals(that.categoryCode) : that.categoryCode != null) return false;
        if (commodityCode != null ? !commodityCode.equals(that.commodityCode) : that.commodityCode != null)
            return false;
        if (commodityInventory != null ? !commodityInventory.equals(that.commodityInventory) : that.commodityInventory != null)
            return false;
        if (createBy != null ? !createBy.equals(that.createBy) : that.createBy != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (lastBy != null ? !lastBy.equals(that.lastBy) : that.lastBy != null) return false;
        if (lastTime != null ? !lastTime.equals(that.lastTime) : that.lastTime != null) return false;
        if (flag != null ? !flag.equals(that.flag) : that.flag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (hotelId != null ? hotelId.hashCode() : 0);
        result = 31 * result + (categoryCode != null ? categoryCode.hashCode() : 0);
        result = 31 * result + (commodityCode != null ? commodityCode.hashCode() : 0);
        result = 31 * result + (commodityInventory != null ? commodityInventory.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastBy != null ? lastBy.hashCode() : 0);
        result = 31 * result + (lastTime != null ? lastTime.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        return result;
    }
}
