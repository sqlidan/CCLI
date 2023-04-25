package com.haiersoft.ccli.base.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 费目分摊比例表
 * BaseExpenseSchemeScale entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_EXPENSE_SCHEME_SCALE")
@DynamicUpdate 
@DynamicInsert
public class BisExpenseSchemeScale implements java.io.Serializable {

	private static final long serialVersionUID = 4040788502963084322L;
	
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EXPENSE_SCHEME_SCALE")
	@SequenceGenerator(name="SEQ_EXPENSE_SCHEME_SCALE", sequenceName="SEQ_EXPENSE_SCHEME_SCALE", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //主键id
    
    @Column(name = "LINK_ID")
	private String linkId; //联系单号
    
    @Column(name = "CUSTOMS_ID")
	private String customsId; //客户ID
   
    @Column(name = "CUSTOMS_NAME")
    private String customsName;//客户名称
    
    @Column(name = "ENTRY_SIGN")
	private String entrySign;  //出入转标志1：入库 2：出库（联系单号为出库单号）3：货转
    
    @Column(name = "SCHEME_NAME")
	private String schemeName; //方案名称
    
    @Column(name = "FEE_CODE")
	private String feeCode;//费目代码
   
    @Column(name = "FEE_NAME")
    private String feeName;//费目名称
    
    @Column(name = "IF_RATIO")
	private String ifRatio;//是否按比例   （0：不按比例，1：按比例)
    
    @Column(name = "SHARE_SIGN")
	private String shareSign;//分摊标记（0：未分摊 1：已分摊）

    @Column(name = "BOS_SIGN")
 	private String bosSign;//买卖方标记  （0：存货方，1收货方)
    
    @Column(name = "REMARK")
 	private String remark;//备注
    
    @Column(name = "FENTAN")
    private Double fentan;//分摊比列
    
    @Column(name = "STANDING_NUM")
    private String standingNum;//台账Id
   
    @Column(name = "EXAMINE_SIGN")
    private String examineSign; //审核标记 0 未审核  1 已审核
    
    @Transient
    private String asn; //asn
    
    @Column(name = "NUM")
    private Double num; //数量
    
    @Transient
    private Double price;//单价
    
    @Transient
    private Double receiveAmount;//金额
    
    @Transient
    private String currency;//币种
    
    @Transient
    private Double exchangeRate;//汇率
    
    @Transient
    private Date billDate;//账单
    
    @Transient
    private String fillSign;//冲补标记
    
    @Column(name = "BILL_NUM")
    private String billNum;//提单号
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "OPERATE_TIME")
	private Date operateTime;//操作时间
    
    @Column(name = "FENTAN_AMOUNT")
    private Double fentanAmount;//分摊后金额
    
    //////////////////////////////////////////////////////////////////////////
    
    
	public String getStandingNum() {
		return standingNum;
	}
	public Double getFentanAmount() {
		return fentanAmount;
	}
	public void setFentanAmount(Double fentanAmount) {
		this.fentanAmount = fentanAmount;
	}
	public Double getFentan() {
		return fentan;
	}
	public void setFentan(Double fentan) {
		this.fentan = fentan;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public void setStandingNum(String standingNum) {
		this.standingNum = standingNum;
	}
	public String getExamineSign() {
		return examineSign;
	}
	public void setExamineSign(String examineSign) {
		this.examineSign = examineSign;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getCustomsId() {
		return customsId;
	}
	public void setCustomsId(String customsId) {
		this.customsId = customsId;
	}
	public String getCustomsName() {
		return customsName;
	}
	public void setCustomsName(String customsName) {
		this.customsName = customsName;
	}
	public String getEntrySign() {
		return entrySign;
	}
	public void setEntrySign(String entrySign) {
		this.entrySign = entrySign;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public String getIfRatio() {
		return ifRatio;
	}
	public void setIfRatio(String ifRatio) {
		this.ifRatio = ifRatio;
	}
	public String getShareSign() {
		return shareSign;
	}
	public void setShareSign(String shareSign) {
		this.shareSign = shareSign;
	}
	public String getBosSign() {
		return bosSign;
	}
	public void setBosSign(String bosSign) {
		this.bosSign = bosSign;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAsn() {
		return asn;
	}
	public void setAsn(String asn) {
		this.asn = asn;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getReceiveAmount() {
		return receiveAmount;
	}
	public void setReceiveAmount(Double receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public String getFillSign() {
		return fillSign;
	}
	public void setFillSign(String fillSign) {
		this.fillSign = fillSign;
	}
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

}