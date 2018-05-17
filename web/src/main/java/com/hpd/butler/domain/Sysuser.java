package com.hpd.butler.domain;

import javax.persistence.*;

/**
 * Created by zy on 2017/10/9.
 */
@Entity
@Table(name = "sysuser")
public class Sysuser {
    private long id;
    private String name;
    private String account;
    private String password;
    private String mobile;
    private String salt;
    private Integer flag;

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
    @Column(name = "Name", nullable = true, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "Password", nullable = true, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "Mobile", nullable = true, length = 12)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "Salt", nullable = true, length = 255)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

        Sysuser sysuser = (Sysuser) o;

        if (id != sysuser.id) return false;
        if (name != null ? !name.equals(sysuser.name) : sysuser.name != null) return false;
        if (account != null ? !account.equals(sysuser.account) : sysuser.account != null) return false;
        if (password != null ? !password.equals(sysuser.password) : sysuser.password != null) return false;
        if (mobile != null ? !mobile.equals(sysuser.mobile) : sysuser.mobile != null) return false;
        if (salt != null ? !salt.equals(sysuser.salt) : sysuser.salt != null) return false;
        if (flag != null ? !flag.equals(sysuser.flag) : sysuser.flag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        return result;
    }
}
