package com.hpd.butler.domain;

import javax.persistence.*;

/**
 * Created by zy on 2017/10/9.
 */
@Entity
@Table(name = "sysrole")
public class Sysrole {
    private long id;
    private String description;
    private String role;

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
    @Column(name = "Description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "Role", nullable = true, length = 255)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sysrole sysrole = (Sysrole) o;

        if (id != sysrole.id) return false;
        if (description != null ? !description.equals(sysrole.description) : sysrole.description != null) return false;
        if (role != null ? !role.equals(sysrole.role) : sysrole.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
