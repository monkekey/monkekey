package com.hpd.butler.vo;

import java.math.BigDecimal;

/**
 * Created by zy on 2017/10/30.
 */
public class InnVo implements Comparable<InnVo>{
    private String code;
    private String name;
    private int distance;
    private BigDecimal longitude;
    private BigDecimal latitude;

    public InnVo(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public InnVo(String code, String name, BigDecimal longitude, BigDecimal latitude) {
        this.code = code;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public int compareTo(InnVo o) {
        if (this.distance > o.distance) {
            return 1;
        } else if (this.distance < o.distance) {
            return -1;
        }else {
            return 0;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}
