package com.hpd.butler.domain;

import javax.persistence.*;

/**
 * Created by zy on 2017/10/9.
 */
@Entity
@Table(name = "wechataccount")
public class Wechataccount {
    private long id;
    private String appId;
    private String secret;
    private String platform;
    private Integer flag;
    private String mchId;
    private String paternerKey;

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
    @Column(name = "AppId", nullable = true, length = 50)
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Basic
    @Column(name = "Secret", nullable = true, length = 500)
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Basic
    @Column(name = "Platform", nullable = true, length = 50)
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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
    @Column(name = "mchId", nullable = true, length = 50)
    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    @Basic
    @Column(name = "paternerKey", nullable = true, length = 50)
    public String getPaternerKey() {
        return paternerKey;
    }

    public void setPaternerKey(String paternerKey) {
        this.paternerKey = paternerKey;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wechataccount that = (Wechataccount) o;

        if (id != that.id) return false;
        if (appId != null ? !appId.equals(that.appId) : that.appId != null) return false;
        if (secret != null ? !secret.equals(that.secret) : that.secret != null) return false;
        if (platform != null ? !platform.equals(that.platform) : that.platform != null) return false;
        if (flag != null ? !flag.equals(that.flag) : that.flag != null) return false;

        return true;
    }


}
