package com.haiersoft.ccli.wms.entity.passPort;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 核放单表体
 */
@Entity
@Table(name = "BIS_PASSPORT_INFO")
public class BisPassPortInfo implements java.io.Serializable {

	private static final long serialVersionUID = -4326979420246878235L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PASSPORT_INFO")
	@SequenceGenerator(name="SEQ_PASSPORT_INFO", sequenceName="SEQ_PASSPORT_INFO", allocationSize = 1)
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
	@Column(name = "PASSPORT_SEQNO")
	private String passportSeqno;
	@Column(name = "GDS_MTNO")
	private String gdsMtno;
	@Column(name = "GDECD")
	private String gdecd;
	@Column(name = "GDS_NM")
	private String gdsNm;
	@Column(name = "GROSS_WT")
	private String grossWt;
	@Column(name = "NET_WT")
	private String netWt;
	@Column(name = "RLT_GDS_SEQNO")
	private String rltGdsSeqno;
	@Column(name = "DCL_UNITCD")
	private String dclUnitcd;
	@Column(name = "DCL_QTY")
	private String dclQty;
	@Column(name = "REMARK")
	private String remark;
	@Column(name = "COL1")
	private String col1;
	@Column(name = "COL2")
	private String col2;
	@Column(name = "COL3")
	private String col3;
	@Column(name = "COL4")
	private String col4;

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

	public String getPassportSeqno() {
		return passportSeqno;
	}

	public void setPassportSeqno(String passportSeqno) {
		this.passportSeqno = passportSeqno;
	}

	public String getGdsMtno() {
		return gdsMtno;
	}

	public void setGdsMtno(String gdsMtno) {
		this.gdsMtno = gdsMtno;
	}

	public String getGdecd() {
		return gdecd;
	}

	public void setGdecd(String gdecd) {
		this.gdecd = gdecd;
	}

	public String getGdsNm() {
		return gdsNm;
	}

	public void setGdsNm(String gdsNm) {
		this.gdsNm = gdsNm;
	}

	public String getGrossWt() {
		return grossWt;
	}

	public void setGrossWt(String grossWt) {
		this.grossWt = grossWt;
	}

	public String getNetWt() {
		return netWt;
	}

	public void setNetWt(String netWt) {
		this.netWt = netWt;
	}

	public String getRltGdsSeqno() {
		return rltGdsSeqno;
	}

	public void setRltGdsSeqno(String rltGdsSeqno) {
		this.rltGdsSeqno = rltGdsSeqno;
	}

	public String getDclUnitcd() {
		return dclUnitcd;
	}

	public void setDclUnitcd(String dclUnitcd) {
		this.dclUnitcd = dclUnitcd;
	}

	public String getDclQty() {
		return dclQty;
	}

	public void setDclQty(String dclQty) {
		this.dclQty = dclQty;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	public String getCol3() {
		return col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}

	public String getCol4() {
		return col4;
	}

	public void setCol4(String col4) {
		this.col4 = col4;
	}
}