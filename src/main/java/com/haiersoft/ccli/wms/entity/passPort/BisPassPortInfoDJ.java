package com.haiersoft.ccli.wms.entity.passPort;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 核放单单据
 */
@Entity
@Table(name = "BIS_PASSPORT_INFO_DJ")
public class BisPassPortInfoDJ implements java.io.Serializable {

	private static final long serialVersionUID = -4326979420246878235L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PASSPORT_INFO_DJ")
	@SequenceGenerator(name="SEQ_PASSPORT_INFO_DJ", sequenceName="SEQ_PASSPORT_INFO_DJ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	@Column(name = "PASSPORT_ID")
	private String passPortId;
	@Column(name = "CREATE_BY")
	private String createBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	private Date createTime;
	@Column(name = "UPDATE_BY")
	private String updateBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	@Column(name = "SEQ_NO")
	private String seqNo;
	@Column(name = "PASSPORT_NO")
	private String passportNo;
	@Column(name = "REMARK")
	private String remark;
	@Column(name = "RTL_TB_TYPECD")
	private String rtlTbTypecd;
	@Column(name = "RTL_NO")
	private String rtlNo;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassPortId() {
		return passPortId;
	}

	public void setPassPortId(String passPortId) {
		this.passPortId = passPortId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRtlTbTypecd() {
		return rtlTbTypecd;
	}

	public void setRtlTbTypecd(String rtlTbTypecd) {
		this.rtlTbTypecd = rtlTbTypecd;
	}

	public String getRtlNo() {
		return rtlNo;
	}

	public void setRtlNo(String rtlNo) {
		this.rtlNo = rtlNo;
	}
}