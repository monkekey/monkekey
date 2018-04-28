package com.yumi.butler.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zy on 2017/10/9.
 */
@Entity
@Table(name = "room", uniqueConstraints={
        @UniqueConstraint(columnNames={"innCode", "roomNo", "flag"})
})
public class Room {
    private long id;
    private String innCode;
    private String roomNo;
    private String roomTypeCode;
    private String roomTypeName;
    private String wifiName;
    private String wifiPassword;
    private Integer flag;
    private String createBy;
    private Date createTime;
    private String lastModifyBy;
    private Date lastModifyTime;

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
    @Column(name = "RoomNo", nullable = true, length = 20)
    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Basic
    @Column(name = "RoomTypeCode", nullable = true, length = 20)
    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        this.roomTypeCode = roomTypeCode;
    }

    @Basic
    @Column(name = "WifiName", nullable = true, length = 100)
    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    @Basic
    @Column(name = "WifiPassword", nullable = true, length = 100)
    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    @Basic
    @Column(name = "RoomTypeName", nullable = true, length = 100)
    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
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

        Room room = (Room) o;

        if (id != room.id) return false;
        if (innCode != null ? !innCode.equals(room.innCode) : room.innCode != null) return false;
        if (roomNo != null ? !roomNo.equals(room.roomNo) : room.roomNo != null) return false;
        if (roomTypeCode != null ? !roomTypeCode.equals(room.roomTypeCode) : room.roomTypeCode != null) return false;
        if (roomTypeName != null ? !roomTypeName.equals(room.roomTypeName) : room.roomTypeName != null) return false;
        if (wifiName != null ? !wifiName.equals(room.wifiName) : room.wifiName != null) return false;
        if (wifiPassword != null ? !wifiPassword.equals(room.wifiPassword) : room.wifiPassword != null) return false;
        if (flag != null ? !flag.equals(room.flag) : room.flag != null) return false;
        if (createBy != null ? !createBy.equals(room.createBy) : room.createBy != null) return false;
        if (createTime != null ? !createTime.equals(room.createTime) : room.createTime != null) return false;
        if (lastModifyBy != null ? !lastModifyBy.equals(room.lastModifyBy) : room.lastModifyBy != null) return false;
        if (lastModifyTime != null ? !lastModifyTime.equals(room.lastModifyTime) : room.lastModifyTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (innCode != null ? innCode.hashCode() : 0);
        result = 31 * result + (roomNo != null ? roomNo.hashCode() : 0);
        result = 31 * result + (roomTypeCode != null ? roomTypeCode.hashCode() : 0);
        result = 31 * result + (roomTypeName != null ? roomTypeName.hashCode() : 0);
        result = 31 * result + (wifiName != null ? wifiName.hashCode() : 0);
        result = 31 * result + (wifiPassword != null ? wifiPassword.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastModifyBy != null ? lastModifyBy.hashCode() : 0);
        result = 31 * result + (lastModifyTime != null ? lastModifyTime.hashCode() : 0);
        return result;
    }
}
