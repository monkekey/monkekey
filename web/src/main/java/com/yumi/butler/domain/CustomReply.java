package com.yumi.butler.domain;

import javax.persistence.*;

/**
 * Created by zy on 2017/12/20.
 */
@Entity
@Table(name = "custom_reply")
public class CustomReply {
    private long id;
    private String title;
    private String usercode;
    private String content;
    private int isDefault;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = true, length = 10)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "usercode", nullable = true, length = 50)
    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    @Basic
    @Column(name = "content", nullable = true, length = 100)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "is_default", nullable = false)
    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomReply that = (CustomReply) o;

        if (id != that.id) return false;
        if(usercode != null ? !usercode.equals(that.usercode) : that.usercode != null) return false;
        if (isDefault != that.isDefault) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (usercode != null ? usercode.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + isDefault;
        return result;
    }
}
