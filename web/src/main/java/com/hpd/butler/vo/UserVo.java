package com.hpd.butler.vo;

import com.hpd.butler.domain.Sysrole;
import com.hpd.butler.domain.Sysuser;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Created by zy on 2017/10/9.
 */
public class UserVo extends Sysuser {
    private long uid;
    private String innCode;
    private String userName;
    private String faces;
    private String sex;
    private Date birthDay;
    private String nature;
    private String specialty;
    private BigDecimal grade;
    private int roleId;
    private Integer favoriteQty;
    private Integer favorited;
    private List<Sysrole> sysroleList;

    public UserVo(){

    }

    public UserVo(String innCode, String account, Long favoriteQty){
        this.innCode = innCode;
        this.setAccount(account);
        this.favoriteQty = favoriteQty.intValue();
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getInnCode() {
        return innCode;
    }

    public void setInnCode(String innCode) {
        this.innCode = innCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFaces() {
        return faces;
    }

    public void setFaces(String faces) {
        this.faces = faces;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public List<Sysrole> getSysroleList() {
        return sysroleList;
    }

    public Integer getFavoriteQty(){ return  favoriteQty; }

    public void setFavoriteQty(Integer favoriteQty) { this.favoriteQty = favoriteQty;}

    public Integer getFavorited(){ return  favorited; }

    public void setFavorited(Integer favorited) { this.favorited = favorited;}

    public void setSysroleList(List<Sysrole> sysroleList) {
        this.sysroleList = sysroleList;
    }
}
