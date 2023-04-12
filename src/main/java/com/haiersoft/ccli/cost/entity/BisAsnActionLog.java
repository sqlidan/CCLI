package com.haiersoft.ccli.cost.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author PYL
 * @ClassName: BisAsnActionLog
 * @Description: ASN区间表 日志表
 * @date 2016年9月14日 
 */
@Entity
@Table(name = "BIS_ASN_ACTION_LOG")
@DynamicUpdate 
@DynamicInsert
public class BisAsnActionLog implements java.io.Serializable {

	private static final long serialVersionUID = 5765160688826212769L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ASN_ACTION_LOG")
   	@SequenceGenerator(name="SEQ_ASN_ACTION_LOG", sequenceName="SEQ_ASN_ACTION_LOG", allocationSize = 1)  
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;//id
	
	@Column(name = "ASN_ACTION_ID")
	private Integer asnActionId; //asn区间表ID
	
	@Column(name = "TYPE") 
	private String type; //asn区间操作类型 1：asn明细添加 2：入库  3：出库  4：完结   5：清库   6：货转托盘   7：取消货转托盘  8：货损    

	@Column(name = "BILL_NUM")
	private String billNum; //提单号
	
	@Column(name = "LINK_ID")
	private String linkId;//联系单号
	
	@Column(name = "LINK_TYPE")
	private Integer linkType; //联系单类型  1：入库   2：出库  3：货转     
	
	@Column(name = "ASN")
	private String asn;  //asn号
	
	@Column(name = "OLD_PIECE")
	private Integer oldPiece;  //原有件数
	
	@Column(name = "CHANGE_PIECE")
	private Integer changePiece;  //修改件数
	
	@Column(name = "NOW_PIECE")
	private Integer nowPiece;  //现有件数
	
	@Column(name = "NET_WEIGHT")
	private Double netWeight;  //净重
	
	@Column(name = "GROSS_WEIGHT")
	private Double grossWeight; //毛重
	
	@Column(name = "OPERATE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date operateTime;   //操作时间
	
	@Column(name = "OPERATOR")
	private String operator;//操作人
	
	@Column(name = "REMARK")
	private String remark;  //备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAsnActionId() {
		return asnActionId;
	}

	public void setAsnActionId(Integer asnActionId) {
		this.asnActionId = asnActionId;
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

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public Integer getLinkType() {
		return linkType;
	}

	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
	}

	public String getAsn() {
		return asn;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}

	public Integer getOldPiece() {
		return oldPiece;
	}

	public void setOldPiece(Integer oldPiece) {
		this.oldPiece = oldPiece;
	}

	public Integer getChangePiece() {
		return changePiece;
	}

	public void setChangePiece(Integer changePiece) {
		this.changePiece = changePiece;
	}

	public Integer getNowPiece() {
		return nowPiece;
	}

	public void setNowPiece(Integer nowPiece) {
		this.nowPiece = nowPiece;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}