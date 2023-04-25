package com.haiersoft.ccli.wms.entity;

import javax.persistence.*;

/**
 * Created by galaxy on 2017/6/8.
 */
@Entity
@Table(name = "BASE_PLAN_TIME")
public class BasePlanTime {

    private Integer id;
    private String planDate;
    private Integer largeNo;
    private String description;
    private Integer state;

    private Integer timeHour;
    private Integer timeMin;

    @Transient
    public Integer getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(Integer timeHour) {
        this.timeHour = timeHour;
    }
    @Transient
    public Integer getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(Integer timeMin) {
        this.timeMin = timeMin;
    }

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CIQ_INFO")
//    @SequenceGenerator(name = "SEQ_CIQ_INFO", sequenceName = "SEQ_CIQ_INFO", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PLAN_DATE")
    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    @Basic
    @Column(name = "LARGE_NO")
    public Integer getLargeNo() {
        return largeNo;
    }

    public void setLargeNo(Integer largeNo) {
        this.largeNo = largeNo;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "STATE")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasePlanTime that = (BasePlanTime) o;

        if (id != that.id) return false;
        if (planDate != null ? !planDate.equals(that.planDate) : that.planDate != null) return false;
        if (largeNo != null ? !largeNo.equals(that.largeNo) : that.largeNo != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (planDate != null ? planDate.hashCode() : 0);
        result = 31 * result + (largeNo != null ? largeNo.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
