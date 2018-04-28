package com.yumi.butler.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zy on 2017/10/30.
 */
@Entity
@Table(name="services",  uniqueConstraints={
        @UniqueConstraint(columnNames={"serviceName", "flag"})
})
public class Services {
    private long id;
    private String serviceName;
    private String serviceDesc;
    private String serviceUrl;
    private Integer sort;
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
    @Column(name = "ServiceName", nullable = true, length = 20)
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Basic
    @Column(name = "ServiceDesc", nullable = true, length = 100)
    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    @Basic
    @Column(name = "ServiceUrl", nullable = true, length = 100)
    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Basic
    @Column(name = "Sort", nullable = true)
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
    @Column(name = "CreateBy", nullable = true, length = 255)
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
    @Column(name = "LastModifyBy", nullable = true, length = 255)
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

        Services services = (Services) o;

        if (id != services.id) return false;
        if (serviceName != null ? !serviceName.equals(services.serviceName) : services.serviceName != null)
            return false;
        if (serviceDesc != null ? !serviceDesc.equals(services.serviceDesc) : services.serviceDesc != null)
            return false;
        if (serviceUrl != null ? !serviceUrl.equals(services.serviceUrl) : services.serviceUrl != null) return false;
        if (sort != null ? !sort.equals(services.sort) : services.sort != null) return false;
        if (flag != null ? !flag.equals(services.flag) : services.flag != null) return false;
        if (createBy != null ? !createBy.equals(services.createBy) : services.createBy != null) return false;
        if (createTime != null ? !createTime.equals(services.createTime) : services.createTime != null) return false;
        if (lastModifyBy != null ? !lastModifyBy.equals(services.lastModifyBy) : services.lastModifyBy != null)
            return false;
        if (lastModifyTime != null ? !lastModifyTime.equals(services.lastModifyTime) : services.lastModifyTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
        result = 31 * result + (serviceDesc != null ? serviceDesc.hashCode() : 0);
        result = 31 * result + (serviceUrl != null ? serviceUrl.hashCode() : 0);
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastModifyBy != null ? lastModifyBy.hashCode() : 0);
        result = 31 * result + (lastModifyTime != null ? lastModifyTime.hashCode() : 0);
        return result;
    }
}
