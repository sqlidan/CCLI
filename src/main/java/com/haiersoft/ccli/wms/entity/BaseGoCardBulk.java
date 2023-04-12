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
 * 出门证散货
 * @author pyl
 * @date 2016年6月28日
 */
@Entity
@Table(name = "BASE_GO_CARD_BULK")
@DynamicUpdate 
@DynamicInsert
public class BaseGoCardBulk implements Serializable {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 698542031510811259L;

	// Fields
    
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GO_CARD_BULK")
//	@SequenceGenerator(name="SEQ_GO_CARD_BULK", sequenceName="SEQ_GO_CARD_BULK", allocationSize = 1)  
//	@Column(name = "ID", unique = true, nullable = false)
//	private Integer id; //主键id

	@Id
    @Column(name = "GO_CARD")
	private String goCard;	//出门证号
    
    @Column(name = "CAR_NUM")
	private String carNum;//卡车牌号
    
    @Column(name = "SHIP_NUM")
	private String shipNum;//船名/航次
	
	@Column(name = "TAKE_ID")
	private String takeId;//提货单位ID
	
	@Column(name = "TAKE_NAME")
	private String takeName;//提货单位名称

	@Column(name = "BILL_NUM")
	private String billNum;//提单号
	
	@Column(name = "TAKE_MAN")
	private String takeMan;//提货人
	
	@Column(name = "INPUT_MAN")
	private String inputMan;//录入员
	
	@Column(name = "REMARK")
	private String remark;//备注
	
	@Column(name = "PIECE_ONE")
	private Integer pieceOne;//件数1
	
	
	@Column(name = "CARGO_NAME_ONE")
	private String cargoNameOne;//货名1
	
	@Column(name = "PACK_TYPE_ONE")
	private String packTypeOne;//包装种类1
	
	@Column(name = "CARGO_WEIGHT_ONE")
	private Double cargoWeightOne;//货重1
	
	@Column(name = "PIECE_TWO")
	private Integer pieceTwo;//件数2
	
	
	@Column(name = "CARGO_NAME_TWO")
	private String cargoNameTwo;//货名2
	
	@Column(name = "PACK_TYPE_TWO")
	private String packTypeTwo;//包装种类2
	
	@Column(name = "CARGO_WEIGHT_TWO")
	private Double cargoWeightTwo;//货重2
	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "LEAVE_TIME")
	private Date leaveTime;//出港时间
	
	@Column(name = "TYPE")
	private String type;//类型

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

	public String getShipNum() {
		return shipNum;
	}

	public void setShipNum(String shipNum) {
		this.shipNum = shipNum;
	}

	public String getTakeId() {
		return takeId;
	}

	public void setTakeId(String takeId) {
		this.takeId = takeId;
	}

	public String getTakeName() {
		return takeName;
	}

	public void setTakeName(String takeName) {
		this.takeName = takeName;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getTakeMan() {
		return takeMan;
	}

	public void setTakeMan(String takeMan) {
		this.takeMan = takeMan;
	}

	public String getInputMan() {
		return inputMan;
	}

	public void setInputMan(String inputMan) {
		this.inputMan = inputMan;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPieceOne() {
		return pieceOne;
	}

	public void setPieceOne(Integer pieceOne) {
		this.pieceOne = pieceOne;
	}

	public String getCargoNameOne() {
		return cargoNameOne;
	}

	public void setCargoNameOne(String cargoNameOne) {
		this.cargoNameOne = cargoNameOne;
	}

	public String getPackTypeOne() {
		return packTypeOne;
	}

	public void setPackTypeOne(String packTypeOne) {
		this.packTypeOne = packTypeOne;
	}

	public Double getCargoWeightOne() {
		return cargoWeightOne;
	}

	public void setCargoWeightOne(Double cargoWeightOne) {
		this.cargoWeightOne = cargoWeightOne;
	}

	public Integer getPieceTwo() {
		return pieceTwo;
	}

	public void setPieceTwo(Integer pieceTwo) {
		this.pieceTwo = pieceTwo;
	}

	public String getCargoNameTwo() {
		return cargoNameTwo;
	}

	public void setCargoNameTwo(String cargoNameTwo) {
		this.cargoNameTwo = cargoNameTwo;
	}

	public String getPackTypeTwo() {
		return packTypeTwo;
	}

	public void setPackTypeTwo(String packTypeTwo) {
		this.packTypeTwo = packTypeTwo;
	}

	public Double getCargoWeightTwo() {
		return cargoWeightTwo;
	}

	public void setCargoWeightTwo(Double cargoWeightTwo) {
		this.cargoWeightTwo = cargoWeightTwo;
	}

	public Date getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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