package com.haiersoft.ccli.cost.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * BisPay entity. @author MyEclipse Persistence Tools
 * 业务付款单
 */
@Entity
@Table(name = "BIS_PAY")
public class BisPay  implements java.io.Serializable {


	private static final long serialVersionUID = 4040788502963084205L;
	// Fields
	@Id
    @Column(name = "PAY_ID", unique = true, nullable = false)
	private String payId; //ID (年月日日期字符串加上六位sequence)

	@Column(name = "CLIENT_ID")
	private String clientId;  //收款客户ID

	@Column(name = "CLIENT_NAME")
	private String clientName;  //收款客户名称

	@Column(name = "ASK_MAN_ID")
	private String askManId;  //申请人ID

	@Column(name = "ASK_MAN")
	private String askMan;  //申请人

	@Column(name = "ASK_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date askDate; //申请日期

	@Column(name = "FINANCE_MAN")
	private String financeMan;  //财务确认人

	@Column(name = "BANK")
	private String bank;  //开户银行

	@Column(name = "ACCOUNT")
	private String account;  //账号

	@Column(name = "RECEIPT_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date receipt; //发票到达日期

	@Column(name = "PAY_WAY")
	private String payWay;  //支付方式 1:电汇 2:支票 3:现金 4:其他

	@Column(name = "PAY_CYCLE")
	private String payCycle;  //付款周期 1：当日付款   2:2日付款  3:3日付款   4：一周付款   5：合同

	@Column(name = "TEAM_TYPE")
	private String teamType;  //合作类型  1：合同收费  2：公司确认   3：其他

	@Column(name = "SITUATION")
	private String situation;  //情况说明

	@Column(name = "BILL_MANAGER")
	private String billManager;  //财务主管

	@Column(name = "APPROVER")
	private String approver;  //批准人

	@Column(name = "DRAW_MONEY")
	private String drawMoney;  //领款人

	@Column(name = "PRICE")
	private Double price;  //金额合计

	@Column(name = "PIECE")
	private Integer piece;  //单据张数

	@Column(name = "STATE")
	private Integer state;  //审核状态：0 未审核，1 已审核
	@Column(name = "MIDGROUPSTATIC")
	private String midGroupStatic;//上传状态

	@Column(name = "STATEMENT_NO")
	private String statementNo;//结算单号

	public String getStatementNo(){
		return statementNo;
	}
	public void setStatementNo(String statementNo){
		this.statementNo = statementNo;
	}

	public String getMidGroupStatic(){
		return midGroupStatic;
	}
	public void setMidGroupStatic(String midGroupStatic){
		this.midGroupStatic = midGroupStatic;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getAskManId() {
		return askManId;
	}

	public void setAskManId(String askManId) {
		this.askManId = askManId;
	}

	public String getAskMan() {
		return askMan;
	}

	public void setAskMan(String askMan) {
		this.askMan = askMan;
	}

	public Date getAskDate() {
		return askDate;
	}

	public void setAskDate(Date askDate) {
		this.askDate = askDate;
	}

	public String getFinanceMan() {
		return financeMan;
	}

	public void setFinanceMan(String financeMan) {
		this.financeMan = financeMan;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Date getReceipt() {
		return receipt;
	}

	public void setReceipt(Date receipt) {
		this.receipt = receipt;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getPayCycle() {
		return payCycle;
	}

	public void setPayCycle(String payCycle) {
		this.payCycle = payCycle;
	}

	public String getTeamType() {
		return teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public String getBillManager() {
		return billManager;
	}

	public void setBillManager(String billManager) {
		this.billManager = billManager;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getDrawMoney() {
		return drawMoney;
	}

	public void setDrawMoney(String drawMoney) {
		this.drawMoney = drawMoney;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getPiece() {
		return piece;
	}

	public void setPiece(Integer piece) {
		this.piece = piece;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}




}
