package com.candy.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_dianrui_parameter")
public class TbDianruiParameterEntity {
    private int id;
    private String username;
    private String token;
    private String userid;
    private String planid;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "userid")
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "planid")
    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbDianruiParameterEntity that = (TbDianruiParameterEntity) o;
        return id == that.id &&
                Objects.equals(username, that.username) &&
                Objects.equals(token, that.token) &&
                Objects.equals(userid, that.userid) &&
                Objects.equals(planid, that.planid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, token, userid, planid);
    }
}
