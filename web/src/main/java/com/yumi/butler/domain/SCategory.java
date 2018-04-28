package com.yumi.butler.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "s_category", schema = "Bee", catalog = "")
public class SCategory {
    private long id;
    private String categoryCode;
    private String categoryName;
    private Integer categoryIsFixed;
    private String categoryCrateby;
    private Date categoryCratetime;
    private String categoryLastby;
    private Date categoryLasttime;
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
    @Column(name = "category_code", nullable = true, length = 500)
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Basic
    @Column(name = "category_name", nullable = true, length = 50)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Basic
    @Column(name = "category_isFixed", nullable = true)
    public Integer getCategoryIsFixed() {
        return categoryIsFixed;
    }

    public void setCategoryIsFixed(Integer categoryIsFixed) {
        this.categoryIsFixed = categoryIsFixed;
    }

    @Basic
    @Column(name = "category_crateby", nullable = true, length = 50)
    public String getCategoryCrateby() {
        return categoryCrateby;
    }

    public void setCategoryCrateby(String categoryCrateby) {
        this.categoryCrateby = categoryCrateby;
    }

    @Basic
    @Column(name = "category_cratetime", nullable = true)
    public Date getCategoryCratetime() {
        return categoryCratetime;
    }

    public void setCategoryCratetime(Date categoryCratetime) {
        this.categoryCratetime = categoryCratetime;
    }

    @Basic
    @Column(name = "category_lastby", nullable = true, length = 50)
    public String getCategoryLastby() {
        return categoryLastby;
    }

    public void setCategoryLastby(String categoryLastby) {
        this.categoryLastby = categoryLastby;
    }

    @Basic
    @Column(name = "category_lasttime", nullable = true)
    public Date getCategoryLasttime() {
        return categoryLasttime;
    }

    public void setCategoryLasttime(Date categoryLasttime) {
        this.categoryLasttime = categoryLasttime;
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

        SCategory sCategory = (SCategory) o;

        if (id != sCategory.id) return false;
        if (categoryCode != null ? !categoryCode.equals(sCategory.categoryCode) : sCategory.categoryCode != null)
            return false;
        if (categoryName != null ? !categoryName.equals(sCategory.categoryName) : sCategory.categoryName != null)
            return false;
        if (categoryIsFixed != null ? !categoryIsFixed.equals(sCategory.categoryIsFixed) : sCategory.categoryIsFixed != null)
            return false;
        if (categoryCrateby != null ? !categoryCrateby.equals(sCategory.categoryCrateby) : sCategory.categoryCrateby != null)
            return false;
        if (categoryCratetime != null ? !categoryCratetime.equals(sCategory.categoryCratetime) : sCategory.categoryCratetime != null)
            return false;
        if (categoryLastby != null ? !categoryLastby.equals(sCategory.categoryLastby) : sCategory.categoryLastby != null)
            return false;
        if (categoryLasttime != null ? !categoryLasttime.equals(sCategory.categoryLasttime) : sCategory.categoryLasttime != null)
            return false;
        if (flag != null ? !flag.equals(sCategory.flag) : sCategory.flag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (categoryCode != null ? categoryCode.hashCode() : 0);
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        result = 31 * result + (categoryIsFixed != null ? categoryIsFixed.hashCode() : 0);
        result = 31 * result + (categoryCrateby != null ? categoryCrateby.hashCode() : 0);
        result = 31 * result + (categoryCratetime != null ? categoryCratetime.hashCode() : 0);
        result = 31 * result + (categoryLastby != null ? categoryLastby.hashCode() : 0);
        result = 31 * result + (categoryLasttime != null ? categoryLasttime.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        return result;
    }
}
