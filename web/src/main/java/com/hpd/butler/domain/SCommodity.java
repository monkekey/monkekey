package com.hpd.butler.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "s_commodity", schema = "Bee", catalog = "")
public class SCommodity {
    private long id;
    private String commodityCode;
    private String commodityName;
    private String commodityImg;
    private String commoditySynopsis;
    private Long commodityInventory;
    private String commodityUnit;
    private BigDecimal commodityPrice;
    private BigDecimal commodityCost;
    private String commodityCategoryCode;
    private String commodityCreateby;
    private Date commodityCreatetime;
    private String commodityLastby;
    private Date commodityLasttime;
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
    @Column(name = "commodity_code", nullable = true, length = 500)
    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    @Basic
    @Column(name = "commodity_name", nullable = true, length = 50)
    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    @Basic
    @Column(name = "commodity_img", nullable = true, length = 200)
    public String getCommodityImg() {
        return commodityImg;
    }

    public void setCommodityImg(String commodityImg) {
        this.commodityImg = commodityImg;
    }

    @Basic
    @Column(name = "commodity_synopsis", nullable = true, length = 200)
    public String getCommoditySynopsis() {
        return commoditySynopsis;
    }

    public void setCommoditySynopsis(String commoditySynopsis) {
        this.commoditySynopsis = commoditySynopsis;
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
    @Column(name = "commodity_unit", nullable = true, length = 20)
    public String getCommodityUnit() {
        return commodityUnit;
    }

    public void setCommodityUnit(String commodityUnit) {
        this.commodityUnit = commodityUnit;
    }

    @Basic
    @Column(name = "commodity_price", nullable = true, precision = 2)
    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    @Basic
    @Column(name = "commodity_cost", nullable = true, precision = 2)
    public BigDecimal getCommodityCost() {
        return commodityCost;
    }

    public void setCommodityCost(BigDecimal commodityCost) {
        this.commodityCost = commodityCost;
    }

    @Basic
    @Column(name = "commodity_category_code", nullable = true)
    public String getCommodityCategoryCode() {
        return commodityCategoryCode;
    }

    public void setCommodityCategoryCode(String commodityCategoryId) {
        this.commodityCategoryCode = commodityCategoryId;
    }

    @Basic
    @Column(name = "commodity_createby", nullable = true, length = 50)
    public String getCommodityCreateby() {
        return commodityCreateby;
    }

    public void setCommodityCreateby(String commodityCreateby) {
        this.commodityCreateby = commodityCreateby;
    }

    @Basic
    @Column(name = "commodity_createtime", nullable = true)
    public Date getCommodityCreatetime() {
        return commodityCreatetime;
    }

    public void setCommodityCreatetime(Date commodityCreatetime) {
        this.commodityCreatetime = commodityCreatetime;
    }

    @Basic
    @Column(name = "commodity_lastby", nullable = true, length = 50)
    public String getCommodityLastby() {
        return commodityLastby;
    }

    public void setCommodityLastby(String commodityLastby) {
        this.commodityLastby = commodityLastby;
    }

    @Basic
    @Column(name = "commodity_lasttime", nullable = true)
    public Date getCommodityLasttime() {
        return commodityLasttime;
    }

    public void setCommodityLasttime(Date commodityLasttime) {
        this.commodityLasttime = commodityLasttime;
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

        SCommodity that = (SCommodity) o;

        if (id != that.id) return false;
        if (commodityCode != null ? !commodityCode.equals(that.commodityCode) : that.commodityCode != null)
            return false;
        if (commodityName != null ? !commodityName.equals(that.commodityName) : that.commodityName != null)
            return false;
        if (commodityImg != null ? !commodityImg.equals(that.commodityImg) : that.commodityImg != null) return false;
        if (commoditySynopsis != null ? !commoditySynopsis.equals(that.commoditySynopsis) : that.commoditySynopsis != null)
            return false;
        if (commodityInventory != null ? !commodityInventory.equals(that.commodityInventory) : that.commodityInventory != null)
            return false;
        if (commodityUnit != null ? !commodityUnit.equals(that.commodityUnit) : that.commodityUnit != null)
            return false;
        if (commodityPrice != null ? !commodityPrice.equals(that.commodityPrice) : that.commodityPrice != null)
            return false;
        if (commodityCost != null ? !commodityCost.equals(that.commodityCost) : that.commodityCost != null)
            return false;
        if (commodityCategoryCode != null ? !commodityCategoryCode.equals(that.commodityCategoryCode) : that.commodityCategoryCode != null)
            return false;
        if (commodityCreateby != null ? !commodityCreateby.equals(that.commodityCreateby) : that.commodityCreateby != null)
            return false;
        if (commodityCreatetime != null ? !commodityCreatetime.equals(that.commodityCreatetime) : that.commodityCreatetime != null)
            return false;
        if (commodityLastby != null ? !commodityLastby.equals(that.commodityLastby) : that.commodityLastby != null)
            return false;
        if (commodityLasttime != null ? !commodityLasttime.equals(that.commodityLasttime) : that.commodityLasttime != null)
            return false;
        if (flag != null ? !flag.equals(that.flag) : that.flag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (commodityCode != null ? commodityCode.hashCode() : 0);
        result = 31 * result + (commodityName != null ? commodityName.hashCode() : 0);
        result = 31 * result + (commodityImg != null ? commodityImg.hashCode() : 0);
        result = 31 * result + (commoditySynopsis != null ? commoditySynopsis.hashCode() : 0);
        result = 31 * result + (commodityInventory != null ? commodityInventory.hashCode() : 0);
        result = 31 * result + (commodityUnit != null ? commodityUnit.hashCode() : 0);
        result = 31 * result + (commodityPrice != null ? commodityPrice.hashCode() : 0);
        result = 31 * result + (commodityCost != null ? commodityCost.hashCode() : 0);
        result = 31 * result + (commodityCategoryCode != null ? commodityCategoryCode.hashCode() : 0);
        result = 31 * result + (commodityCreateby != null ? commodityCreateby.hashCode() : 0);
        result = 31 * result + (commodityCreatetime != null ? commodityCreatetime.hashCode() : 0);
        result = 31 * result + (commodityLastby != null ? commodityLastby.hashCode() : 0);
        result = 31 * result + (commodityLasttime != null ? commodityLasttime.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        return result;
    }

    private String categoryName;

    //不关联数据库
    @Transient
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
