package com.haiersoft.ccli.wms.entity.apiEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;


public class InvtHeadTypeVo extends InvtHeadType implements java.io.Serializable{
    private String id;// ID
    private String createBy;// 创建人
    private String createTime;// 下单日期
    private String updateBy;// 修改人
    private String updateTime;// 修改日期
    private String jlAudit; //初审人
    private String jlRejectReason; //初审驳回原因
    private String jlAuditTime;  //初审时间
    private String zgAudit; //复审审核
    private String zgRejectReason; //复审驳回原因
    private String zgAuditTime;  //复审时间
    private Integer days; //天数

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getJlAudit() {
        return jlAudit;
    }

    public void setJlAudit(String jlAudit) {
        this.jlAudit = jlAudit;
    }

    public String getJlRejectReason() {
        return jlRejectReason;
    }

    public void setJlRejectReason(String jlRejectReason) {
        this.jlRejectReason = jlRejectReason;
    }

    public String getJlAuditTime() {
        return jlAuditTime;
    }

    public void setJlAuditTime(String jlAuditTime) {
        this.jlAuditTime = jlAuditTime;
    }

    public String getZgAudit() {
        return zgAudit;
    }

    public void setZgAudit(String zgAudit) {
        this.zgAudit = zgAudit;
    }

    public String getZgRejectReason() {
        return zgRejectReason;
    }

    public void setZgRejectReason(String zgRejectReason) {
        this.zgRejectReason = zgRejectReason;
    }

    public String getZgAuditTime() {
        return zgAuditTime;
    }

    public void setZgAuditTime(String zgAuditTime) {
        this.zgAuditTime = zgAuditTime;
    }
}
