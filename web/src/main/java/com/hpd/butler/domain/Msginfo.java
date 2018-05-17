package com.hpd.butler.domain;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by zy on 2017/10/12.
 */
@Entity
@Table(name = "msginfo")
public class Msginfo {
    private int id;
    private String openid;
    private String guestName;
    private String checkinInn;
    private String checkinRoom;
    private String butlerAccount;
    private String msgContent;
    private String fromGuest;
    private Integer isRead;
    private Date chatTime;

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
    @Column(name = "Openid", nullable = true, length = 100)
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Basic
    @Column(name = "GuestName", nullable = true, length = 100)
    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    @Basic
    @Column(name = "CheckinInn", nullable = true, length = 20)
    public String getCheckinInn() {
        return checkinInn;
    }

    public void setCheckinInn(String checkinInn) {
        this.checkinInn = checkinInn;
    }

    @Basic
    @Column(name = "CheckinRoom", nullable = true, length = 10)
    public String getCheckinRoom() {
        return checkinRoom;
    }

    public void setCheckinRoom(String checkinRoom) {
        this.checkinRoom = checkinRoom;
    }

    @Basic
    @Column(name = "ButlerAccount", nullable = true, length = 100)
    public String getButlerAccount() {
        return butlerAccount;
    }

    public void setButlerAccount(String butlerAccount) {
        this.butlerAccount = butlerAccount;
    }

    @Basic
    @Column(name = "MsgContent", nullable = true, length = 800)
    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    @Basic
    @Column(name = "FromGuest", nullable = true, length = 45)
    public String getFromGuest() {
        return fromGuest;
    }

    public void setFromGuest(String fromGuest) {
        this.fromGuest = fromGuest;
    }

    @Basic
    @Column(name = "IsRead", nullable = true)
    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    @Basic
    @Column(name = "ChatTime", nullable = true)
    public Date getChatTime() {
        return chatTime;
    }

    public void setChatTime(Date chatTime) {
        this.chatTime = chatTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Msginfo msginfo = (Msginfo) o;

        if (id != msginfo.id) return false;
        if (openid != null ? !openid.equals(msginfo.openid) : msginfo.openid != null) return false;
        if (checkinInn != null ? !checkinInn.equals(msginfo.checkinInn) : msginfo.checkinInn != null) return false;
        if (checkinRoom != null ? !checkinRoom.equals(msginfo.checkinRoom) : msginfo.checkinRoom != null) return false;
        if (butlerAccount != null ? !butlerAccount.equals(msginfo.butlerAccount) : msginfo.butlerAccount != null)
            return false;
        if (msgContent != null ? !msgContent.equals(msginfo.msgContent) : msginfo.msgContent != null) return false;
        if (fromGuest != null ? !fromGuest.equals(msginfo.fromGuest) : msginfo.fromGuest != null) return false;
        if (isRead != null ? !isRead.equals(msginfo.isRead) : msginfo.isRead != null) return false;
        if (chatTime != null ? !chatTime.equals(msginfo.chatTime) : msginfo.chatTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (openid != null ? openid.hashCode() : 0);
        result = 31 * result + (checkinInn != null ? checkinInn.hashCode() : 0);
        result = 31 * result + (checkinRoom != null ? checkinRoom.hashCode() : 0);
        result = 31 * result + (butlerAccount != null ? butlerAccount.hashCode() : 0);
        result = 31 * result + (msgContent != null ? msgContent.hashCode() : 0);
        result = 31 * result + (fromGuest != null ? fromGuest.hashCode() : 0);
        result = 31 * result + (isRead != null ? isRead.hashCode() : 0);
        result = 31 * result + (chatTime != null ? chatTime.hashCode() : 0);
        return result;
    }
}


