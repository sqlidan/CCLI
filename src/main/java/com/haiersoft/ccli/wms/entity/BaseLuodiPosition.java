package com.haiersoft.ccli.wms.entity;

import javax.persistence.*;

/**
 * Created by galaxy on 2017/6/14.
 */
@Entity
@Table(name = "BASE_LUODI_POSITION")
public class BaseLuodiPosition {

    private Integer id;
    private String positionName;
    private String positionFirst;
    private String positionLast;
    private Integer positionMax;
    private Integer nowNum;
    private Integer state;
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FORECAST_INFO")
    @SequenceGenerator(name = "SEQ_FORECAST_INFO", sequenceName = "SEQ_FORECAST_INFO", allocationSize = 1)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "POSITION_NAME")
    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    @Basic
    @Column(name = "POSITION_FIRST")
    public String getPositionFirst() {
        return positionFirst;
    }

    public void setPositionFirst(String positionFirst) {
        this.positionFirst = positionFirst;
    }

    @Basic
    @Column(name = "POSITION_LAST")
    public String getPositionLast() {
        return positionLast;
    }

    public void setPositionLast(String positionLast) {
        this.positionLast = positionLast;
    }

    @Basic
    @Column(name = "POSITION_MAX")
    public Integer getPositionMax() {
        return positionMax;
    }

    public void setPositionMax(Integer positionMax) {
        this.positionMax = positionMax;
    }

    @Basic
    @Column(name = "NOW_NUM")
    public Integer getNowNum() {
        return nowNum;
    }

    public void setNowNum(Integer nowNum) {
        this.nowNum = nowNum;
    }

    @Basic
    @Column(name = "STATE")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseLuodiPosition that = (BaseLuodiPosition) o;

        if (id != that.id) return false;
        if (positionFirst != null ? !positionFirst.equals(that.positionFirst) : that.positionFirst != null)
            return false;
        if (positionLast != null ? !positionLast.equals(that.positionLast) : that.positionLast != null) return false;
        if (positionMax != null ? !positionMax.equals(that.positionMax) : that.positionMax != null) return false;
        if (nowNum != null ? !nowNum.equals(that.nowNum) : that.nowNum != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (positionFirst != null ? positionFirst.hashCode() : 0);
        result = 31 * result + (positionLast != null ? positionLast.hashCode() : 0);
        result = 31 * result + (positionMax != null ? positionMax.hashCode() : 0);
        result = 31 * result + (nowNum != null ? nowNum.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
