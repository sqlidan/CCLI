package com.haiersoft.ccli.supervision.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 分类监管 审核单头实体类
 * 
 * @author
 *
 */

@Entity
@Table(name = "FLJG_OP_MANI_HEAD")
@DynamicUpdate
@DynamicInsert
public class OpManiHead implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5570181153733811017L;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false)
	private String id; // 主键id

	/// 核放单编号 编辑必填
	@Column(name = "MANIFEST_ID")
	public String ManifestId;
	
	/// 申请单编号
	@Column(name = "APPR_ID ")
	public String ApprId;

	/// 经营单位编码 反填
	@Column(name = "TRADE_CODE")
	public String TradeCode;

	/// 经营企业名称反填
	@Column(name = "TRADE_NAME")
	public String TradeName;

	/// 主管海关反填
	@Column(name = "CUSTOMS_CODE")
	public String CustomsCode;

	/// 进出标志：I：进区，E：出区 保存必填
	@Column(name = "IE_FLAG")
	public String IeFlag;

	/// 空车标志：Y 空车 N 非空车
	@Column(name = "EMPTY_FLAG")
	public String EmptyFlag;
	
	/// 进出标志备注
	@Column(name = "IE_FLAG_NOTE")
	public String IeFlagNote;
	
	/// 货值
	@Column(name = "GOODS_VALUE")
	public String GoodsValue;

	/// 货物毛重保存必填
	@Column(name = "GROSS_WT")
	public String GrossWt;

	/// 车辆号码保存必填
	@Column(name = "VEHICLE_ID")
	public String VehicleId;

	/// 车辆自重保存必填
	@Column(name = "VEHICLE_WEIGHT")
	public String VehicleWeight;

	/// 状态 0-暂存，1-申报2-审批通过，3审批退回，4删除 反填
	@Column(name = "STATUS")
	public String Status;

	/// 0：未过闸；1：已过闸 2 到货确认 反填
	@Column(name = "PASS_STATUS")
	public String PassStatus;

	/// 录入员 反填
	@Column(name = "INPUTER")
	public String InputEr;

	/// 录入日期 反填
	@Column(name = "INPUT_DATE")
	public String InputDate;

	/// 申报日期 反填
	@Column(name = "D_DATE")
	public String DDate;

	/// 申请备注 保存选填
	@Column(name = "D_NOTE")
	public String DNote;

	/// 审批日期 反填
	@Column(name = "APPR_DATE")
	public String ApprDate;

	/// 审批意见 反填
	@Column(name = "APPR_NOTE")
	public String ApprNote;

	/// 组织机构代码 反填
	@Column(name = "ORG_CODE")
	public String OrgCode;

	/// 集装箱编号 保存选填
	@Column(name = "CONTA_ID")
	public String ContaId;

	/// 过闸时间 反填
	@Column(name = "PASSPORT_DATE")
	public String PassportDate;

	/// 申报类型1首次申报2变更3作废 反填
	@Column(name = "DECL_TYPE")
	public String DeclType;

	/// 海关序号 反填
	@Column(name = "SEQ_NO")
	public String SeqNo;

	/// 海关状态 0 未通过1发送成功2通过3删除 反填
	@Column(name = "CUS_STATUS")
	public String CusStatus;

	/// 海关状态描述 反填
	@Column(name = "CUSR_MK")
	public String CusRmk;

	@Column(name = "LOCAL_STATUS")
	public String LocalStatus;

	/// info 的ID
	@Column(name = "INFO_IDS")
	public String InfoIds;
	
	/// 箱型
	@Column(name = "CTN_TYPE_SIZE")
	public String CtnTypeSize;
	
	/// 核放单到货确认状态
	@Column(name = "Mani_Confirm_Status")
	public String ManiConfirmStatus;

	@JsonProperty(value = "CREATETIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "CREATE_TIME")
    private Date createTime;

	@Column(name = "APPR_ADD_TYPE")
	private String apprAddType;//手工申请单添加方式
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManifestId() {
		return ManifestId;
	}

	public void setManifestId(String manifestId) {
		ManifestId = manifestId;
	}

	public String getApprId() {
		return ApprId;
	}

	public void setApprId(String apprId) {
		ApprId = apprId;
	}

	public String getTradeCode() {
		return TradeCode;
	}

	public void setTradeCode(String tradeCode) {
		TradeCode = tradeCode;
	}

	public String getTradeName() {
		return TradeName;
	}

	public void setTradeName(String tradeName) {
		TradeName = tradeName;
	}

	public String getCustomsCode() {
		return CustomsCode;
	}

	public void setCustomsCode(String customsCode) {
		CustomsCode = customsCode;
	}

	public String getIeFlag() {
		return IeFlag;
	}

	public void setIeFlag(String ieFlag) {
		IeFlag = ieFlag;
	}

	public String getEmptyFlag() {
		return EmptyFlag;
	}

	public void setEmptyFlag(String emptyFlag) {
		EmptyFlag = emptyFlag;
	}

	public String getIeFlagNote() {
		return IeFlagNote;
	}

	public void setIeFlagNote(String ieFlagNote) {
		IeFlagNote = ieFlagNote;
	}

	public String getGoodsValue() {
		return GoodsValue;
	}

	public void setGoodsValue(String goodsValue) {
		GoodsValue = goodsValue;
	}

	public String getGrossWt() {
		return GrossWt;
	}

	public void setGrossWt(String grossWt) {
		GrossWt = grossWt;
	}

	public String getVehicleId() {
		return VehicleId;
	}

	public void setVehicleId(String vehicleId) {
		VehicleId = vehicleId;
	}

	public String getVehicleWeight() {
		return VehicleWeight;
	}

	public void setVehicleWeight(String vehicleWeight) {
		VehicleWeight = vehicleWeight;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getPassStatus() {
		return PassStatus;
	}

	public void setPassStatus(String passStatus) {
		PassStatus = passStatus;
	}

	public String getInputEr() {
		return InputEr;
	}

	public void setInputEr(String inputEr) {
		InputEr = inputEr;
	}

	public String getInputDate() {
		return InputDate;
	}

	public void setInputDate(String inputDate) {
		InputDate = inputDate;
	}

	public String getDDate() {
		return DDate;
	}

	public void setDDate(String dDate) {
		DDate = dDate;
	}

	public String getDNote() {
		return DNote;
	}

	public void setDNote(String dNote) {
		DNote = dNote;
	}

	public String getApprDate() {
		return ApprDate;
	}

	public void setApprDate(String apprDate) {
		ApprDate = apprDate;
	}

	public String getApprNote() {
		return ApprNote;
	}

	public void setApprNote(String apprNote) {
		ApprNote = apprNote;
	}

	public String getOrgCode() {
		return OrgCode;
	}

	public void setOrgCode(String orgCode) {
		OrgCode = orgCode;
	}

	public String getContaId() {
		return ContaId;
	}

	public void setContaId(String contaId) {
		ContaId = contaId;
	}

	public String getPassportDate() {
		return PassportDate;
	}

	public void setPassportDate(String passportDate) {
		PassportDate = passportDate;
	}

	public String getDeclType() {
		return DeclType;
	}

	public void setDeclType(String declType) {
		DeclType = declType;
	}

	public String getSeqNo() {
		return SeqNo;
	}

	public void setSeqNo(String seqNo) {
		SeqNo = seqNo;
	}

	public String getCusStatus() {
		return CusStatus;
	}

	public void setCusStatus(String cusStatus) {
		CusStatus = cusStatus;
	}

	public String getCusRmk() {
		return CusRmk;
	}

	public void setCusRmk(String cusRmk) {
		CusRmk = cusRmk;
	}

	public String getLocalStatus() {
		return LocalStatus;
	}

	public void setLocalStatus(String localStatus) {
		LocalStatus = localStatus;
	}

	public String getInfoIds() {
		return InfoIds;
	}

	public void setInfoIds(String infoIds) {
		InfoIds = infoIds;
	}

	public String getCtnTypeSize() {
		return CtnTypeSize;
	}

	public void setCtnTypeSize(String ctnTypeSize) {
		CtnTypeSize = ctnTypeSize;
	}

	public String getManiConfirmStatus() {
		return ManiConfirmStatus;
	}

	public void setManiConfirmStatus(String maniConfirmStatus) {
		ManiConfirmStatus = maniConfirmStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getApprAddType() {
		return apprAddType;
	}

	public void setApprAddType(String apprAddType) {
		this.apprAddType = apprAddType;
	}

}
