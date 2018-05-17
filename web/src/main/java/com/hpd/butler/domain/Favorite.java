package com.hpd.butler.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zy on 2017/10/13.
 */
@Entity
@Table(name = "favorite")
public class Favorite {
    private int id;
    private String innCode;
    private String account;
    private String openid;
    private Date createTime;
    private Integer flag;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "InnCode", nullable = true, length = 45)
    public String getInnCode() {
        return innCode;
    }

    public void setInnCode(String innCode) {
        this.innCode = innCode;
    }

    @Basic
    @Column(name = "Account", nullable = true, length = 20)
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "Openid", nullable = true, length = 100)
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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
    @Column(name = "Flag", nullable = true)
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

        Favorite favorite = (Favorite) o;

        if (id != favorite.id) return false;
        if (innCode != null ? !innCode.equals(favorite.innCode) : favorite.innCode != null) return false;
        if (account != null ? !account.equals(favorite.account) : favorite.account != null) return false;
        if (openid != null ? !openid.equals(favorite.openid) : favorite.openid != null) return false;
        if (createTime != null ? !createTime.equals(favorite.createTime) : favorite.createTime != null) return false;
        if (flag != null ? !flag.equals(favorite.flag) : favorite.flag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (innCode != null ? innCode.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (openid != null ? openid.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        return result;
    }
}

