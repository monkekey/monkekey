package com.hpd.butler.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zy on 2018/2/6.
 */
@Entity
public class Goods {
    private Long id;
    private String goodsName;
    private BigDecimal originalPrice;
    private String pic;
    private String category;
    private String innCode;
    private Integer flag;
    private String createBy;
    private Date createTime;
    private String lastModifyBy;
    private Date lastModifyTime;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "GoodsName", nullable = true, length = 20)
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Basic
    @Column(name = "OriginalPrice", nullable = true, precision = 2)
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    @Basic
    @Column(name = "Pic", nullable = true, length = 100)
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Basic
    @Column(name = "Category", nullable = true, length = 20)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

        Goods goods = (Goods) o;

        if (id != null ? !id.equals(goods.id) : goods.id != null) return false;
        if (goodsName != null ? !goodsName.equals(goods.goodsName) : goods.goodsName != null) return false;
        if (originalPrice != null ? !originalPrice.equals(goods.originalPrice) : goods.originalPrice != null)
            return false;
        if (pic != null ? !pic.equals(goods.pic) : goods.pic != null) return false;
        if (category != null ? !category.equals(goods.category) : goods.category != null) return false;
        if (innCode != null ? !innCode.equals(goods.innCode) : goods.innCode != null) return false;
        if (flag != null ? !flag.equals(goods.flag) : goods.flag != null) return false;
        if (createBy != null ? !createBy.equals(goods.createBy) : goods.createBy != null) return false;
        if (createTime != null ? !createTime.equals(goods.createTime) : goods.createTime != null) return false;
        if (lastModifyBy != null ? !lastModifyBy.equals(goods.lastModifyBy) : goods.lastModifyBy != null) return false;
        if (lastModifyTime != null ? !lastModifyTime.equals(goods.lastModifyTime) : goods.lastModifyTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (goodsName != null ? goodsName.hashCode() : 0);
        result = 31 * result + (originalPrice != null ? originalPrice.hashCode() : 0);
        result = 31 * result + (pic != null ? pic.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (innCode != null ? innCode.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastModifyBy != null ? lastModifyBy.hashCode() : 0);
        result = 31 * result + (lastModifyTime != null ? lastModifyTime.hashCode() : 0);
        return result;
    }

    private String categoryName;

    @Transient
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
