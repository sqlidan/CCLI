package com.haiersoft.ccli.wms.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by galaxy on 2017/6/13.
 */
@Entity
@Table(name = "BIS_LUODI_INFO")
public class BisLuodiInfo {

    private Integer id;
    private String luodiCode;
    private Date luodiTime;
    private String startPosition;
    private String endPosition;
    private String ctnNum;
    private String ctnVersion;
    private String ctnSize;
    private String clientId;
    private String clientName;
    private String yfClientId;  //应付客户ID
    private String yfClientName; //应付客户名称
    private String billCode;
    private String state; // -1删除 0正常 1完成
    private Date startTime;
    private Date endTime;
    private Integer isElectricity = 0;
    private String createUser;
    private Date createTime;
    private String carNum;

    private String luodiType;
    private String isCompleteElectricity;
    private String isCd;
    private String cdState;

    @Transient
    public String getCdState() {
        return cdState;
    }

    public void setCdState(String cdState) {
        this.cdState = cdState;
    }

    @Transient
    public String getIsCd() {
        return isCd;
    }

    public void setIsCd(String isCd) {
        this.isCd = isCd;
    }

    @Basic
    @Column(name = "IS_COMPLETE_ELECTRICITY")
    public String getIsCompleteElectricity() {
        return isCompleteElectricity;
    }

    public void setIsCompleteElectricity(String isCompleteElectricity) {
        this.isCompleteElectricity = isCompleteElectricity;
    }

    @Basic
    @Column(name = "LUODI_TYPE")
    public String getLuodiType() {
        return luodiType;
    }

    public void setLuodiType(String luodiType) {
        this.luodiType = luodiType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TASK_REMIND")
    @SequenceGenerator(name = "SEQ_TASK_REMIND", sequenceName = "SEQ_TASK_REMIND", allocationSize = 1)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "LUODI_CODE")
    public String getLuodiCode() {
        return luodiCode;
    }

    public void setLuodiCode(String luodiCode) {
        this.luodiCode = luodiCode;
    }

    @Basic
    @Column(name = "LUODI_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getLuodiTime() {
        return luodiTime;
    }

    public void setLuodiTime(Date luodiTime) {
        this.luodiTime = luodiTime;
    }

    @Column(name = "CLIENT_ID")
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Basic
    @Column(name = "START_POSITION")
    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    @Basic
    @Column(name = "END_POSITION")
    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    @Basic
    @Column(name = "CTN_NUM")
    public String getCtnNum() {
        return ctnNum;
    }

    public void setCtnNum(String ctnNum) {
        this.ctnNum = ctnNum;
    }

    @Basic
    @Column(name = "CTN_VERSION")
    public String getCtnVersion() {
        return ctnVersion;
    }

    public void setCtnVersion(String ctnVersion) {
        this.ctnVersion = ctnVersion;
    }

    @Basic
    @Column(name = "CTN_SIZE")
    public String getCtnSize() {
        return ctnSize;
    }

    public void setCtnSize(String ctnSize) {
        this.ctnSize = ctnSize;
    }

    @Basic
    @Column(name = "CLIENT_NAME")
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Column(name = "YF_CLIENT_ID")
    public String getYfClientId() {
		return yfClientId;
	}

	public void setYfClientId(String yfClientId) {
		this.yfClientId = yfClientId;
	}

	@Column(name = "YF_CLIENT_NAME")
	public String getYfClientName() {
		return yfClientName;
	}

	public void setYfClientName(String yfClientName) {
		this.yfClientName = yfClientName;
	}
    
    @Basic
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    @Basic
    @Column(name = "STATE")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Basic
    @Column(name = "START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "IS_ELECTRICITY")
    public Integer getIsElectricity() {
        return isElectricity;
    }

    public void setIsElectricity(Integer isElectricity) {
        this.isElectricity = isElectricity;
    }
    

	@Basic
    @Column(name = "CREATE_USER")
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Basic
    @Column(name = "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "CAR_NUM")
    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BisLuodiInfo that = (BisLuodiInfo) o;

        if (id != that.id) return false;
        if (luodiCode != null ? !luodiCode.equals(that.luodiCode) : that.luodiCode != null) return false;
        if (luodiTime != null ? !luodiTime.equals(that.luodiTime) : that.luodiTime != null) return false;
        if (startPosition != null ? !startPosition.equals(that.startPosition) : that.startPosition != null)
            return false;
        if (endPosition != null ? !endPosition.equals(that.endPosition) : that.endPosition != null) return false;
        if (ctnNum != null ? !ctnNum.equals(that.ctnNum) : that.ctnNum != null) return false;
        if (ctnVersion != null ? !ctnVersion.equals(that.ctnVersion) : that.ctnVersion != null) return false;
        if (ctnSize != null ? !ctnSize.equals(that.ctnSize) : that.ctnSize != null) return false;
        if (clientName != null ? !clientName.equals(that.clientName) : that.clientName != null) return false;
        if (billCode != null ? !billCode.equals(that.billCode) : that.billCode != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (isElectricity != null ? !isElectricity.equals(that.isElectricity) : that.isElectricity != null)
            return false;
        if (createUser != null ? !createUser.equals(that.createUser) : that.createUser != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (carNum != null ? !carNum.equals(that.carNum) : that.carNum != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (luodiCode != null ? luodiCode.hashCode() : 0);
        result = 31 * result + (luodiTime != null ? luodiTime.hashCode() : 0);
        result = 31 * result + (startPosition != null ? startPosition.hashCode() : 0);
        result = 31 * result + (endPosition != null ? endPosition.hashCode() : 0);
        result = 31 * result + (ctnNum != null ? ctnNum.hashCode() : 0);
        result = 31 * result + (ctnVersion != null ? ctnVersion.hashCode() : 0);
        result = 31 * result + (ctnSize != null ? ctnSize.hashCode() : 0);
        result = 31 * result + (clientName != null ? clientName.hashCode() : 0);
        result = 31 * result + (billCode != null ? billCode.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (isElectricity != null ? isElectricity.hashCode() : 0);
        result = 31 * result + (createUser != null ? createUser.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (carNum != null ? carNum.hashCode() : 0);
        return result;
    }
}
