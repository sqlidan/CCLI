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
	private String createPreEntry;// 生成标识 0-未生成；1-已生成
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ORDER_TIME")
	private Date orderTime;
	@Column(name = "TD_NO")
	private String tdNo;

	public String getTdNo() {
		return tdNo;
	}

	public void setTdNo(String tdNo) {
		this.tdNo = tdNo;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getCreatePreEntry() {
		return createPreEntry;
	}

	public void setCreatePreEntry(String createPreEntry) {
		this.createPreEntry = createPreEntry;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
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
}