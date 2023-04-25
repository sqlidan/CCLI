package com.haiersoft.ccli.cost.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 装卸队日工统计实体
 * @author mzy
 * @date 2016年6月29日
 */
@Entity
@Table(name = "BIS_LOADING_TEAM_DAYWORK")
public class LoadingTeamDaywork implements java.io.Serializable {
    
	private static final long serialVersionUID = 7701615065461868779L;
	
	@Id
    @Column(name = "ID", unique = true)
	private String id;//id
    
    @Column(name = "BILL_NUM")
	private String billNum;//提单号
    
    @Column(name = "CLIENT_ID")
	private String clientId;//装卸队ID
    
    @Column(name = "CLIENT")
	private String client;//装卸队
    
    @Column(name = "LOADING_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date loadingDate;//装卸日期
    
    @Column(name = "SCHEME_ID")
	private String schemeId;//装卸方案ID
    
    @Column(name = "SCHEME_NAME")
	private String schemeName;//装卸方案名称
    
    @Column(name = "DAYWORK_NUM")
	private Double dayworkNum;//日工数

    @Column(name = "DAYWORK_REMARK")
	private String dayworkRemark;//日工备注
    
    @Column(name = "ELEVATOR_NUM")
	private Integer evevatorNum;//电梯工数
    
    @Column(name = "ELEVATOR_REMARK")
	private String evevatorRemark;//电梯工备注
    
    @Column(name = "OVERTIME")
	private Double overtime;//加班小时
    
    @Column(name = "OVERTIME_REMARK")
	private String overtimeRemark;//加班备注
    
    @Column(name = "BILL_DATE")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+08:00")
	private Date billDate;//账单年月
    
    @Column(name = "REMARK")
	private String remark;//备注
    
    @Column(name = "STANDING_NUM")
	private String standingNum;//台账ID
    
    @Column(name = "CREATER")
	private String creater;//创建人
    
    @Column(name = "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime;//创建时间
    
    @Column(name = "UPDATE_USER")
	private String updateUser;//修改人
    
    @Column(name = "UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateTime;//修改时间
    
    @Column(name = "DELETE_USER")
	private String deleteUser;//删除人
    
    @Column(name = "DELETE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date deleteTime;//删除时间
    
    @Column(name = "DEL_FLAG")
	private Integer delFlag;//删除标记 0正常，1删除
    
    @Column(name = "STATE")
	private Integer state=0;//状态

	// Constructors

	

	/** default constructor */
	public LoadingTeamDaywork() {
	}

	/** full constructor */
	public LoadingTeamDaywork(String id, String billNum,
			String clientId, String client, Date loadingDate,
			String schemeId, String schemeName, Double dayworkNum,
			String dayworkRemark, Integer evevatorNum, String evevatorRemark, Double overtime,
			String overtimeRemark, Date billDate, String remark,
			String standingNum, String creater,Date createTime, String updateUser,
			Date updateTime, String deleteUser,Date deleteTime,Integer delFlag,Integer state) {
		this.id = id;
		this.billNum = billNum;
		this.clientId = clientId;
		this.client = client;
		this.loadingDate = loadingDate;
		this.schemeId = schemeId;
		this.schemeName = schemeName;
		this.dayworkNum = dayworkNum;
		this.dayworkRemark = dayworkRemark;
		this.evevatorNum = evevatorNum;
		this.evevatorRemark = evevatorRemark;
		this.overtime = overtime;
		this.overtimeRemark = overtimeRemark;
		this.billDate = billDate;
		this.remark = remark;
		this.standingNum = standingNum;
		this.creater = creater;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.deleteUser = deleteUser;
		this.deleteTime = deleteTime;
		this.delFlag = delFlag;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Date getLoadingDate() {
		return loadingDate;
	}

	public void setLoadingDate(Date loadingDate) {
		this.loadingDate = loadingDate;
	}

	public String getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public Double getDayworkNum() {
		return dayworkNum;
	}

	public void setDayworkNum(Double dayworkNum) {
		this.dayworkNum = dayworkNum;
	}

	public String getDayworkRemark() {
		return dayworkRemark;
	}

	public void setDayworkRemark(String dayworkRemark) {
		this.dayworkRemark = dayworkRemark;
	}

	public Integer getEvevatorNum() {
		return evevatorNum;
	}

	public void setEvevatorNum(Integer evevatorNum) {
		this.evevatorNum = evevatorNum;
	}

	public String getEvevatorRemark() {
		return evevatorRemark;
	}

	public void setEvevatorRemark(String evevatorRemark) {
		this.evevatorRemark = evevatorRemark;
	}

	public Double getOvertime() {
		return overtime;
	}

	public void setOvertime(Double overtime) {
		this.overtime = overtime;
	}

	public String getOvertimeRemark() {
		return overtimeRemark;
	}

	public void setOvertimeRemark(String overtimeRemark) {
		this.overtimeRemark = overtimeRemark;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStandingNum() {
		return standingNum;
	}

	public void setStandingNum(String standingNum) {
		this.standingNum = standingNum;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	
	
}