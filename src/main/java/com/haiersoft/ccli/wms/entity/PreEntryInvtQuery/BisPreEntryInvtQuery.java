package com.haiersoft.ccli.wms.entity.PreEntryInvtQuery;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Clob;
import java.util.Date;

@Entity
@Table(name = "BIS_PREENTRY_INVT_QUERY")
public class BisPreEntryInvtQuery implements java.io.Serializable {

	private static final long serialVersionUID = -4326979420246878235L;

	@Id
	@GeneratedValue(generator = "baseBoundedGenerator")
	@GenericGenerator(name = "baseBoundedGenerator", strategy = "uuid")
	@Column(name = "ID",unique = true)
	private String id;
	@Column(name = "BOND_INVT_NO")
	private String bondInvtNo;// 保税清单编号
	@Column(name = "INVT_QUERY_LIST")
	private byte[] InvtQueryList;// 保税核注清单列表查询结果
	@Column(name = "INVT_DEC_HEAD_TYPE")
	private byte[] invtDecHeadType;// 核注清单报关单表头
	@Column(name = "INVT_DEC_LIST_TYPE")
	private byte[] invtDecListType;// 核注清单报关单商品表体
	@Column(name = "INVT_GOODS_TYPE")
	private byte[] invtGoodsType;// 简单加工、一纳成品内销成品明细[核注清单商品表体(保存集报清单料件信息)]
	@Column(name = "INVT_HEAD_TYPE")
	private byte[] invtHeadType;// 核注清单表头
	@Column(name = "INVT_LIST_TYPE")
	private byte[] invtListType;// 核注清单表体
	@Column(name = "INVT_WAREHOUSE_TYPE")
	private byte[] invtWarehouseType;// 核注清单出入库单集报表体
	@Column(name = "LIST_STAT")
	private String listStat;// 清单状态
	@Column(name = "OPER_CUS_REG_CODE")
	private String operCusRegCode;// 操作卡的海关十位
	@Column(name = "SYS_ID")
	private String sysId;// 子系统ID 95 加工贸易账册系统 B1 加工贸易手册系统 B2 加工贸易担保管理系统 B3 保税货物流转系统二期 Z7 海关特殊监管区域管理系统 Z8 保税物流管理系
	@Column(name = "CREATE_BY")
	private String createBy;// 创建人
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	private Date createTime;// 下单日期
	@Column(name = "UPDATE_BY")
	private String updateBy;// 修改人
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;// 修改日期
	@Column(name = "SYNCHRONIZATION")
	private String synchronization;// 同步标识 0-未同步；1-已同步
	@Column(name = "CREATE_PREENTRY")
	private String createPreEntry;// 生成预报单标识 0-未生成；1-已生成
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ORDER_TIME")
	private Date orderTime;
	@Column(name = "TD_NO")
	private String tdNo;
	@Column(name = "CREATE_CLEARANCE")
	private String createClearance;// 生成台账标识 0-未生成；1-已生成
	@Column(name = "CREATE_BGD")
	private String createBgd;// 生成报关单 0-未生成；1-已生成

	@Column(name = "JL_AUDIT")
	private String jlAudit; //初审人

	@Column(name = "JL_REJECT_REASON")
	private String jlRejectReason; //初审驳回原因

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "JL_AUDIT_TIME")
	private Date jlAuditTime;  //初审时间

	@Column(name = "ZG_AUDIT")
	private String zgAudit; //复审审核

	@Column(name = "ZG_REJECT_REASON")
	private String zgRejectReason; //复审驳回原因

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ZG_AUDIT_TIME")
	private Date zgAuditTime;  //复审时间

	@Column(name = "IN_LINK_ID")
	private String inLinkId; //入库联系单号

	@Column(name = "OUT_LINK_ID")
	private String outLinkId; //出库联系单号

	public String getInLinkId() {
		return inLinkId;
	}

	public void setInLinkId(String inLinkId) {
		this.inLinkId = inLinkId;
	}

	public String getOutLinkId() {
		return outLinkId;
	}

	public void setOutLinkId(String outLinkId) {
		this.outLinkId = outLinkId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBondInvtNo() {
		return bondInvtNo;
	}

	public void setBondInvtNo(String bondInvtNo) {
		this.bondInvtNo = bondInvtNo;
	}

	public byte[] getInvtQueryList() {
		return InvtQueryList;
	}

	public void setInvtQueryList(byte[] invtQueryList) {
		InvtQueryList = invtQueryList;
	}

	public byte[] getInvtDecHeadType() {
		return invtDecHeadType;
	}

	public void setInvtDecHeadType(byte[] invtDecHeadType) {
		this.invtDecHeadType = invtDecHeadType;
	}

	public byte[] getInvtDecListType() {
		return invtDecListType;
	}

	public void setInvtDecListType(byte[] invtDecListType) {
		this.invtDecListType = invtDecListType;
	}

	public byte[] getInvtGoodsType() {
		return invtGoodsType;
	}

	public void setInvtGoodsType(byte[] invtGoodsType) {
		this.invtGoodsType = invtGoodsType;
	}

	public byte[] getInvtHeadType() {
		return invtHeadType;
	}

	public void setInvtHeadType(byte[] invtHeadType) {
		this.invtHeadType = invtHeadType;
	}

	public byte[] getInvtListType() {
		return invtListType;
	}

	public void setInvtListType(byte[] invtListType) {
		this.invtListType = invtListType;
	}

	public byte[] getInvtWarehouseType() {
		return invtWarehouseType;
	}

	public void setInvtWarehouseType(byte[] invtWarehouseType) {
		this.invtWarehouseType = invtWarehouseType;
	}

	public String getListStat() {
		return listStat;
	}

	public void setListStat(String listStat) {
		this.listStat = listStat;
	}

	public String getOperCusRegCode() {
		return operCusRegCode;
	}

	public void setOperCusRegCode(String operCusRegCode) {
		this.operCusRegCode = operCusRegCode;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
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

	public String getSynchronization() {
		return synchronization;
	}

	public void setSynchronization(String synchronization) {
		this.synchronization = synchronization;
	}

	public String getCreatePreEntry() {
		return createPreEntry;
	}

	public void setCreatePreEntry(String createPreEntry) {
		this.createPreEntry = createPreEntry;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getTdNo() {
		return tdNo;
	}

	public void setTdNo(String tdNo) {
		this.tdNo = tdNo;
	}

	public String getCreateClearance() {
		return createClearance;
	}

	public void setCreateClearance(String createClearance) {
		this.createClearance = createClearance;
	}

	public String getCreateBgd() {
		return createBgd;
	}

	public void setCreateBgd(String createBgd) {
		this.createBgd = createBgd;
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

	public Date getJlAuditTime() {
		return jlAuditTime;
	}

	public void setJlAuditTime(Date jlAuditTime) {
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

	public Date getZgAuditTime() {
		return zgAuditTime;
	}

	public void setZgAuditTime(Date zgAuditTime) {
		this.zgAuditTime = zgAuditTime;
	}
}