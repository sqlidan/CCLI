package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 出门证集装箱
 * @author pyl
 * @date 2016年6月28日
 */
@Entity
@Table(name = "BASE_GO_CARD_CONTAINER")
@DynamicUpdate 
@DynamicInsert
public class BaseGoCardContainer implements Serializable {

    
	// Fields
   
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GO_CARD_CONTAINER")
//	@SequenceGenerator(name="SEQ_GO_CARD_CONTAINER", sequenceName="SEQ_GO_CARD_CONTAINER", allocationSize = 1)  
//	@Column(name = "ID", unique = true, nullable = false)
//	private Integer id; //主键id

	/**
	 * 
	 */
	private static final long serialVersionUID = 8873633686138049500L;

	@Id
    @Column(name = "GO_CARD")
	private String goCard;	//出门证号
    
    @Column(name = "CAR_NUM")
	private String carNum;//卡车牌号
    
    @Column(name = "CTN_NUM_ONE")
	private String ctnNumOne;//集装箱号1
	
	@Column(name = "CTN_NUM_TWO")
	private String ctnNumTwo;//集装箱号2
	
	@Column(name = "IF_EMPTY")
	private String ifEmpty;//空重（1空箱2重箱）

	@Column(name = "THE_SIZE")
	private String theSize;//尺寸（20，30,40）
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "APPEAR_TIME")
	private Date appearTime;//出场时间
	
	@Column(name = "CTN_AMOUNT")
	private Integer ctnAmount;//箱量
	
	@Column(name = "TYPE")
	private String type;//类型
	
	@Column(name = "BILL_NUM")
	private String billNum;//提单号
	
	@Column(name = "SHIP_NUM")
	private String shipNum;//船名/船次
	
	@Column(name = "INPUT_MAN")
	private String inputMan;//录入员

	//出门证状态 1:已发送
	@Column(name = "GO_CARD_STATUS")
	private String goCardStatus;

	//出门证返回onlyMark
	@Column(name = "ONLY_MARK")
	private String onlyMark;


	public String getGoCard() {
		return goCard;
	}

	public void setGoCard(String goCard) {
		this.goCard = goCard;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getCtnNumOne() {
		return ctnNumOne;
	}

	public void setCtnNumOne(String ctnNumOne) {
		this.ctnNumOne = ctnNumOne;
	}

	public String getCtnNumTwo() {
		return ctnNumTwo;
	}

	public void setCtnNumTwo(String ctnNumTwo) {
		this.ctnNumTwo = ctnNumTwo;
	}

	public String getIfEmpty() {
		return ifEmpty;
	}

	public void setIfEmpty(String ifEmpty) {
		this.ifEmpty = ifEmpty;
	}

	public String getTheSize() {
		return theSize;
	}

	public void setTheSize(String theSize) {
		this.theSize = theSize;
	}

	public Date getAppearTime() {
		return appearTime;
	}

	public void setAppearTime(Date appearTime) {
		this.appearTime = appearTime;
	}

	public Integer getCtnAmount() {
		return ctnAmount;
	}

	public void setCtnAmount(Integer ctnAmount) {
		this.ctnAmount = ctnAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getShipNum() {
		return shipNum;
	}

	public void setShipNum(String shipNum) {
		this.shipNum = shipNum;
	}

	public String getInputMan() {
		return inputMan;
	}

	public void setInputMan(String inputMan) {
		this.inputMan = inputMan;
	}


	public String getGoCardStatus() {
		return goCardStatus;
	}

	public void setGoCardStatus(String goCardStatus) {
		this.goCardStatus = goCardStatus;
	}

	public String getOnlyMark() {
		return onlyMark;
	}

	public void setOnlyMark(String onlyMark) {
		this.onlyMark = onlyMark;
	}
}