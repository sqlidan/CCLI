package com.haiersoft.ccli.wms.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by galaxy on 2017/6/21.
 */
@Entity
@Table(name = "BIS_GATE_CAR")
public class BisGateCar {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GATE_CAR")
	@SequenceGenerator(name = "SEQ_GATE_CAR", sequenceName = "SEQ_GATE_CAR", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;  //id 
	@Column(name = "CAR_NUM")
    private String carNum;//车牌号
	@Column(name = "CTN_NUM")
	private String ctnNum;  //箱号
	@Column(name = "CTN_TYPE")
	private String ctnType;  //箱型
	@Column(name = "CTN_SIZE")
	private String ctnSize;  //箱尺寸
	@Column(name = "ASN")
	private String asn;  //ASN
	@Column(name = "BILL_NUM")
	private String billNum;  //提单号
	@Column(name = "STOCK_ID")
	private String stockId;   //客户ID
	@Column(name = "STOCK_NAME")
	private String stockName;  //客户名称
	@Column(name = "ENTER_NUM")
	private Double enterNum;   //计划入库件数
	@Column(name = "OUT_NUM")
	private Double outNum;    //计划出库件数
	@Column(name = "LOADING_NUM")
	private String loadingNum;  //装车单号
	@Column(name = "DRIVER_NAME")
	private String driverName;   //司机名
	@Column(name = "BIS_TYPE")
	private String bisType;   //业务类型  1入库 2出库3无作业任务4特殊作业
	@Column(name = "JOB_TYPE")
	private String jobType;  //作业类型 1入闸  2出闸3无作业4特殊作业
	@Column(name = "CREATE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createDate;    //创建日期
	@Column(name = "CREATE_USER")
	private String createUser;   //创建人
	@Column(name = "PLATFORM")
	private String platform;   //月台口
	@Column(name = "PLATFORMTWO")
	private String platformtwo; //月台口2
	
	@Column(name = "UPDATE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateDate;    //开闸日期
	@Column(name = "UPDATE_USER")
	private String updateUser;  //开闸更新操作者
	@Column(name = "LINK_ID")
	private String linkId;//关联出入库id

	//出闸时间
	@Column(name = "CAR_OUT_DATE")
	private Date carOutDate;

	//出闸时间
	@Column(name = "CAR_OUT_GATE")
	private String carOutGate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getCtnNum() {
		return ctnNum;
	}
	public void setCtnNum(String ctnNum) {
		this.ctnNum = ctnNum;
	}
	public String getCtnType() {
		return ctnType;
	}
	public void setCtnType(String ctnType) {
		this.ctnType = ctnType;
	}
	public String getAsn() {
		return asn;
	}
	public void setAsn(String asn) {
		this.asn = asn;
	}
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public Double getEnterNum() {
		return enterNum;
	}
	public void setEnterNum(Double enterNum) {
		this.enterNum = enterNum;
	}
	public Double getOutNum() {
		return outNum;
	}
	public void setOutNum(Double outNum) {
		this.outNum = outNum;
	}
	public String getLoadingNum() {
		return loadingNum;
	}
	public void setLoadingNum(String loadingNum) {
		this.loadingNum = loadingNum;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getBisType() {
		return bisType;
	}
	public void setBisType(String bisType) {
		this.bisType = bisType;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCtnSize() {
		return ctnSize;
	}
	public void setCtnSize(String ctnSize) {
		this.ctnSize = ctnSize;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public String getPlatformtwo() {
		return platformtwo;
	}
	public void setPlatformtwo(String platformtwo) {
		this.platformtwo = platformtwo;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public Date getCarOutDate() {
		return carOutDate;
	}

	public void setCarOutDate(Date carOutDate) {
		this.carOutDate = carOutDate;
	}

	public String getCarOutGate() {
		return carOutGate;
	}

	public void setCarOutGate(String carOutGate) {
		this.carOutGate = carOutGate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BisGateCar [id=");
		builder.append(id);
		builder.append(", carNum=");
		builder.append(carNum);
		builder.append(", ctnNum=");
		builder.append(ctnNum);
		builder.append(", ctnType=");
		builder.append(ctnType);
		builder.append(", asn=");
		builder.append(asn);
		builder.append(", billNum=");
		builder.append(billNum);
		builder.append(", stockId=");
		builder.append(stockId);
		builder.append(", stockName=");
		builder.append(stockName);
		builder.append(", enterNum=");
		builder.append(enterNum);
		builder.append(", outNum=");
		builder.append(outNum);
		builder.append(", loadingNum=");
		builder.append(loadingNum);
		builder.append(", driverName=");
		builder.append(driverName);
		builder.append(", bisType=");
		builder.append(bisType);
		builder.append(", jobType=");
		builder.append(jobType);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", createUser=");
		builder.append(createUser);
		builder.append(", updateDate=");
		builder.append(updateDate);
		builder.append(", updateUser=");
		builder.append(updateUser);
		builder.append(", linkId=");
		builder.append(linkId);
		builder.append("]");
		return builder.toString();
	}
	 
     
	
}
