package com.yumi.butler.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zy on 2018/2/6.
 */
@Entity
@Table(name = "sys_dict", schema = "Bee", catalog = "")
public class SysDict {
    private Long id;
    private String typeCode;
    private String itemCode;
    private String itemName;
    private String itemValue;
    private Integer isFixed;
    private Integer flag;
    private Date createTime;
    private String createBy;
    private Date lastModifyTime;
    private String lastModifyBy;

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
    @Column(name = "TypeCode", nullable = true, length = 15)
    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Basic
    @Column(name = "ItemCode", nullable = true, length = 20)
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Basic
    @Column(name = "ItemName", nullable = true, length = 50)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Basic
    @Column(name = "ItemValue", nullable = true, length = 200)
    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    @Basic
    @Column(name = "IsFixed", nullable = true)
    public Integer getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Integer isFixed) {
        this.isFixed = isFixed;
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
    @Column(name = "CreateTime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "CreateBy", nullable = true, length = 20)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Basic
    @Column(name = "LastModifyTime", nullable = true)
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Basic
    @Column(name = "LastModifyBy", nullable = true, length = 20)
    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysDict sysDict = (SysDict) o;

        if (id != null ? !id.equals(sysDict.id) : sysDict.id != null) return false;
        if (typeCode != null ? !typeCode.equals(sysDict.typeCode) : sysDict.typeCode != null) return false;
        if (itemCode != null ? !itemCode.equals(sysDict.itemCode) : sysDict.itemCode != null) return false;
        if (itemName != null ? !itemName.equals(sysDict.itemName) : sysDict.itemName != null) return false;
        if (itemValue != null ? !itemValue.equals(sysDict.itemValue) : sysDict.itemValue != null) return false;
        if (isFixed != null ? !isFixed.equals(sysDict.isFixed) : sysDict.isFixed != null) return false;
        if (flag != null ? !flag.equals(sysDict.flag) : sysDict.flag != null) return false;
        if (createTime != null ? !createTime.equals(sysDict.createTime) : sysDict.createTime != null) return false;
        if (createBy != null ? !createBy.equals(sysDict.createBy) : sysDict.createBy != null) return false;
        if (lastModifyTime != null ? !lastModifyTime.equals(sysDict.lastModifyTime) : sysDict.lastModifyTime != null)
            return false;
        if (lastModifyBy != null ? !lastModifyBy.equals(sysDict.lastModifyBy) : sysDict.lastModifyBy != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (typeCode != null ? typeCode.hashCode() : 0);
        result = 31 * result + (itemCode != null ? itemCode.hashCode() : 0);
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (itemValue != null ? itemValue.hashCode() : 0);
        result = 31 * result + (isFixed != null ? isFixed.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (lastModifyTime != null ? lastModifyTime.hashCode() : 0);
        result = 31 * result + (lastModifyBy != null ? lastModifyBy.hashCode() : 0);
        return result;
    }

    private String typeName;

    //不关联数据库
    @Transient
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
