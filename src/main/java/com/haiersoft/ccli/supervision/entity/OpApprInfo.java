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
 * 分类监管 手工申请单明细实体类
 * @author
 *
 */

@Entity
@Table(name = "FLJG_OP_APPR_INFO")
@DynamicUpdate 
@DynamicInsert
public class OpApprInfo implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2610941222053023380L;

	@Id
    @GeneratedValue(generator ="paymentableGenerator")
    @GenericGenerator(name ="paymentableGenerator", strategy ="uuid")
	@Column(name = "ID", unique = true, nullable = false)
	private String id; //主键id
	
	@JsonProperty(value = "HeadId")
    @Column(name = "HEAD_ID")
    private String headId;
	
	//保存申请单时对应info行上的ID，对于之后生成核放单时
	//如果没有申请单，不能保存核放单
	@JsonProperty(value = "BisInfoId")
    @Column(name = "BIS_INFO_ID")
	private String bisInfoId;
    
	@JsonProperty(value = "ApprId")
    @Column(name = "APPR_ID")
    private String apprId;

	@JsonProperty(value = "LinkId")
    @Column(name = "LINK_ID")
    private String linkId;
    
	@JsonProperty(value = "ApprGNo")
    @Column(name = "APPR_GNO")
    private Integer apprGNo;
    
	//底账商品项号
	@JsonProperty(value = "GNo")
    @Column(name = "G_NO")
    private Integer gNo;    

	//海关编码 商品编码
	@JsonProperty(value = "CodeTs")
    @Column(name = "CODE_TS")
    private String codeTs;   

	//商品名称
	@JsonProperty(value = "GName")
    @Column(name = "G_NAME")
    private String gName;
    
	@JsonProperty(value = "GModel")
    @Column(name = "G_MODEL")
    private String gModel;

	@JsonProperty(value = "GUnit")
    @Column(name = "G_UNIT")
    private String gUnit;
    
	//申报数量
	@JsonProperty(value = "GQty")
    @Column(name = "G_QTY")
    private String gQty;

	@JsonProperty(value = "Unit1")
    @Column(name = "UNIT1")
    private String unit1;
    
	@JsonProperty(value = "Qty1")
    @Column(name = "QTY1")
    private String qty1;
    
	@JsonProperty(value = "TotalSum")
    @Column(name = "TOTAL_SUM")
    private String totalSum;
    
	@JsonProperty(value = "Curr")
    @Column(name = "CURR")
    private String curr;
    
	@JsonProperty(value = "GrossWt")
    @Column(name = "GROSS_WT")
    private String grossWt;
    
	@JsonProperty(value = "CREATETIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "CREATE_TIME")
    private Date createTime;

	@Column(name = "IO_TYPE")
	private String ioType;

	@Column(name = "CTN_NUM")
	private String ctnNum;

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

	public String getBisInfoId() {
		return bisInfoId;
	}

	public void setBisInfoId(String bisInfoId) {
		this.bisInfoId = bisInfoId;
	}

	public String getApprId() {
		return apprId;
	}

	public void setApprId(String apprId) {
		this.apprId = apprId;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public Integer getApprGNo() {
		return apprGNo;
	}

	public void setApprGNo(Integer apprGNo) {
		this.apprGNo = apprGNo;
	}

	public Integer getgNo() {
		return gNo;
	}

	public void setgNo(Integer gNo) {
		this.gNo = gNo;
	}

	public String getCodeTs() {
		return codeTs;
	}

	public void setCodeTs(String codeTs) {
		this.codeTs = codeTs;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}

	public String getgModel() {
		return gModel;
	}

	public void setgModel(String gModel) {
		this.gModel = gModel;
	}

	public String getgUnit() {
		return gUnit;
	}

	public void setgUnit(String gUnit) {
		this.gUnit = gUnit;
	}

	public String getgQty() {
		return gQty;
	}

	public void setgQty(String gQty) {
		this.gQty = gQty;
	}

	public String getUnit1() {
		return unit1;
	}

	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}

	public String getQty1() {
		return qty1;
	}

	public void setQty1(String qty1) {
		this.qty1 = qty1;
	}

	public String getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(String totalSum) {
		this.totalSum = totalSum;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public String getGrossWt() {
		return grossWt;
	}

	public void setGrossWt(String grossWt) {
		this.grossWt = grossWt;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}

	public String getCtnNum() {
		return ctnNum;
	}

	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}
}
