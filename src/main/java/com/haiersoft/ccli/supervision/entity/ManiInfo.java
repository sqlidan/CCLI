package com.haiersoft.ccli.supervision.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 分类监管 审核单头实体类
 * 
 * @author
 *
 */

@Entity
@Table(name = "FLJG_MANI_INFO")
@DynamicUpdate
@DynamicInsert
public class ManiInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7141444241077053560L;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false)
	private String id; // 主键id
	
	@JsonProperty(value = "HeadId")
    @Column(name = "HEAD_ID")
    private String headId;

	/// 核放单编号 编辑必填
	@Column(name = "MANIFEST_ID")
	public String manifestId;

	/// 核放单序号
	@Column(name = "G_NO")
	public Integer gNo;

	/// 申请单编号 保存必填
	@Column(name = "APPR_ID ")
	public String ApprId;

	/// 申请单商品序号 保存必填
	@Column(name = "APPR_GNO ")
	public Integer ApprGNo;

	/// 商品编码 保存必填
	@Column(name = "CODE_TS ")
	public String CodeTs;

	/// 商品名称保存必填
	@Column(name = "G_NAME")
	public String GName;

	/// 规格型号 保存选填
	@Column(name = "G_MODEL")
	public String GModel;

	/// 申报计量单位 保存必填
	@Column(name = "G_UNIT")
	public String GUnit;

	/// 申报数量 保存必填
	@Column(name = "G_QTY")
	public String GQty;
	
	/// 
	@Column(name = "G_UNIT1")
	public String GUnit1;

	/// 
	@Column(name = "G_QTY1")
	public String GQty1;

	/// 毛重 保存选填
	@Column(name = "GROSS_WT")
	public String GrossWt;

	/// 到货确认数 到货确认必填
	@Column(name = "CONFIRM_QTY")
	public String ConfirmQty;

	/// 底账项号 反填
	@Column(name = "EMS_GNO")
	public String EmsGNo;

	/// 商品料号保存选填
	@Column(name = "GDS_MTNO")
	public String GdsMtno;
	
	@JsonProperty(value = "CREATETIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "CREATE_TIME")
    private Date createTime;

	/// 箱号
	@Column(name = "CONTA_ID")
	public String ContaId;
	
	/// 箱号
	@Column(name = "CONTA_TYPE")
	public String ContaType;
	
	/// 箱号
	@Column(name = "CONTA_WEIGHT")
	public String ContaWeight;
	
	
	/// 原入库联系单信息行的ID
	@Column(name = "BIS_INFO_ID")
	public String BisInfoId;
	
	/// 原入库联系单信息行联系单号
	@Column(name = "BIS_INFO_LINK_ID")
	public String BisInfoLinkId;	
	/// 原入库联系单信息行提单号
	@Column(name = "BIS_ITEM_NUM")
	public String BisItemNum;	
	/// 原入库联系单信息行SKU
	@Column(name = "BIS_SKU")
	public String BisSku;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public String getManifestId() {
		return manifestId;
	}
	public void setManifestId(String manifestId) {
		this.manifestId = manifestId;
	}
	public Integer getgNo() {
		return gNo;
	}
	public void setgNo(Integer gNo) {
		this.gNo = gNo;
	}
	public String getApprId() {
		return ApprId;
	}
	public void setApprId(String apprId) {
		ApprId = apprId;
	}
	public Integer getApprGNo() {
		return ApprGNo;
	}
	public void setApprGNo(Integer apprGNo) {
		ApprGNo = apprGNo;
	}
	public String getCodeTs() {
		return CodeTs;
	}
	public void setCodeTs(String codeTs) {
		CodeTs = codeTs;
	}
	public String getGName() {
		return GName;
	}
	public void setGName(String gName) {
		GName = gName;
	}
	public String getGModel() {
		return GModel;
	}
	public void setGModel(String gModel) {
		GModel = gModel;
	}
	public String getGUnit() {
		return GUnit;
	}
	public void setGUnit(String gUnit) {
		GUnit = gUnit;
	}
	public String getGQty() {
		return GQty;
	}
	public void setGQty(String gQty) {
		GQty = gQty;
	}
	public String getGUnit1() {
		return GUnit1;
	}
	public void setGUnit1(String gUnit1) {
		GUnit1 = gUnit1;
	}
	public String getGQty1() {
		return GQty1;
	}
	public void setGQty1(String gQty1) {
		GQty1 = gQty1;
	}
	public String getGrossWt() {
		return GrossWt;
	}
	public void setGrossWt(String grossWt) {
		GrossWt = grossWt;
	}
	public String getConfirmQty() {
		return ConfirmQty;
	}
	public void setConfirmQty(String confirmQty) {
		ConfirmQty = confirmQty;
	}
	public String getEmsGNo() {
		return EmsGNo;
	}
	public void setEmsGNo(String emsGNo) {
		EmsGNo = emsGNo;
	}
	public String getGdsMtno() {
		return GdsMtno;
	}
	public void setGdsMtno(String gdsMtno) {
		GdsMtno = gdsMtno;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getContaId() {
		return ContaId;
	}
	public void setContaId(String contaId) {
		ContaId = contaId;
	}
	public String getContaType() {
		return ContaType;
	}
	public void setContaType(String contaType) {
		ContaType = contaType;
	}
	public String getContaWeight() {
		return ContaWeight;
	}
	public void setContaWeight(String contaWeight) {
		ContaWeight = contaWeight;
	}
	public String getBisInfoId() {
		return BisInfoId;
	}
	public void setBisInfoId(String bisInfoId) {
		BisInfoId = bisInfoId;
	}
	public String getBisInfoLinkId() {
		return BisInfoLinkId;
	}
	public void setBisInfoLinkId(String bisInfoLinkId) {
		BisInfoLinkId = bisInfoLinkId;
	}
	public String getBisItemNum() {
		return BisItemNum;
	}
	public void setBisItemNum(String bisItemNum) {
		BisItemNum = bisItemNum;
	}
	public String getBisSku() {
		return BisSku;
	}
	public void setBisSku(String bisSku) {
		BisSku = bisSku;
	}

}
