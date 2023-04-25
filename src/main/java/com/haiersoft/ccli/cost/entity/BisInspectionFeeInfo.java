package com.haiersoft.ccli.cost.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 * 对账单主表
 * @author LZG
 *
 */
@Entity
@Table(name = "BIS_INSPECTION_FEE_INFO")
public class BisInspectionFeeInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2003122603766807952L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INSPECTION_FEE_INFO")
	@SequenceGenerator(name="SEQ_INSPECTION_FEE_INFO", sequenceName="SEQ_INSPECTION_FEE_INFO", allocationSize = 1)  
    @Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "BILL_NUM", unique = true, nullable = false)
	private String billNum; //提单号
	
	@Column(name = "CTN_NUM")
	private String ctnNum; //箱号（mr)
	
	@Column(name = "CHECK_STANDARD")
	private Double checkStandard; //查验标准
	
	@Column(name = "IF_PLUG")
	private Integer ifPlug; //是否插电
	
	@Column(name = "PLUG_DAYS")
	private Integer plugDays; //插电天数
	
	@Column(name = "PLUG_UNIT")
	private Double plugUnit; //插电标准（单价）
	
	@Column(name = "PLUG_PRICE")
	private Double plugPrice; //插电费用
	
	@Column(name = "IF_HANG")
	private Integer ifHang; //是否吊箱
	
	@Column(name = "HANG_TIMES")
	private Integer hangTimes; //吊箱次数
	
	@Column(name = "HANG_UNIT")
	private Double hangUnit; //吊箱标准（单价）
	
	@Column(name = "HANG_PRICE")
	private Double hangPrice; //吊箱费用
	
	@Column(name = "IF_FIELD")
	private Integer ifField; //是否场地
	
	@Column(name = "FIELD_DAYS")
	private Integer fieldDays; //场地天数
	
	@Column(name = "FIELD_UNIT")
	private Double fieldUnit; //场地标准（单价）
	
	@Column(name = "FIELD_PRICE")
	private Double fieldPrice; //场地费用
	
	@Column(name = "IF_HANDING")
	private Integer ifHanding; //是否搬倒
	
	@Column(name = "HANDING_TIMES")
	private Integer handingTimes; //搬倒次数
	
	@Column(name = "HANDING_UNIT")
	private Double handingUnit; //搬倒标准（单价）
	
	@Column(name = "HANDING_PRICE")
	private Double handingPrice; //搬倒费用
	
	@Column(name = "REMARK")
	private String remark;//备注

	@Column(name = "FEE_ID")
	private String feeId;//主表ID
	
	@Column(name = "STANDING_NUM")
	private String standingNum;//台账主键
	
	@Column(name = "IF_JS")
	private String ifJs;//是否已结算0否1是
	
	
	

	public String getIfJs() {
		return ifJs;
	}

	public void setIfJs(String ifJs) {
		this.ifJs = ifJs;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getCtnNum() {
		return ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}

	public Double getCheckStandard() {
		return checkStandard;
	}

	public void setCheckStandard(Double checkStandard) {
		this.checkStandard = checkStandard;
	}

	public Integer getIfPlug() {
		return ifPlug;
	}

	public void setIfPlug(Integer ifPlug) {
		this.ifPlug = ifPlug;
	}

	public Integer getPlugDays() {
		return plugDays;
	}

	public void setPlugDays(Integer plugDays) {
		this.plugDays = plugDays;
	}

	public Double getPlugUnit() {
		return plugUnit;
	}

	public void setPlugUnit(Double plugUnit) {
		this.plugUnit = plugUnit;
	}

	public Double getPlugPrice() {
		return plugPrice;
	}

	public void setPlugPrice(Double plugPrice) {
		this.plugPrice = plugPrice;
	}

	public Integer getIfHang() {
		return ifHang;
	}

	public void setIfHang(Integer ifHang) {
		this.ifHang = ifHang;
	}

	public Integer getHangTimes() {
		return hangTimes;
	}

	public void setHangTimes(Integer hangTimes) {
		this.hangTimes = hangTimes;
	}

	public Double getHangUnit() {
		return hangUnit;
	}

	public void setHangUnit(Double hangUnit) {
		this.hangUnit = hangUnit;
	}

	public Double getHangPrice() {
		return hangPrice;
	}

	public void setHangPrice(Double hangPrice) {
		this.hangPrice = hangPrice;
	}

	public Integer getIfField() {
		return ifField;
	}

	public void setIfField(Integer ifField) {
		this.ifField = ifField;
	}

	public Integer getFieldDays() {
		return fieldDays;
	}

	public void setFieldDays(Integer fieldDays) {
		this.fieldDays = fieldDays;
	}

	public Double getFieldUnit() {
		return fieldUnit;
	}

	public void setFieldUnit(Double fieldUnit) {
		this.fieldUnit = fieldUnit;
	}

	public Double getFieldPrice() {
		return fieldPrice;
	}

	public void setFieldPrice(Double fieldPrice) {
		this.fieldPrice = fieldPrice;
	}

	public Integer getIfHanding() {
		return ifHanding;
	}

	public void setIfHanding(Integer ifHanding) {
		this.ifHanding = ifHanding;
	}

	public Integer getHandingTimes() {
		return handingTimes;
	}

	public void setHandingTimes(Integer handingTimes) {
		this.handingTimes = handingTimes;
	}

	public Double getHandingUnit() {
		return handingUnit;
	}

	public void setHandingUnit(Double handingUnit) {
		this.handingUnit = handingUnit;
	}

	public Double getHandingPrice() {
		return handingPrice;
	}

	public void setHandingPrice(Double handingPrice) {
		this.handingPrice = handingPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getStandingNum() {
		return standingNum;
	}

	public void setStandingNum(String standingNum) {
		this.standingNum = standingNum;
	}

		
}
