package com.haiersoft.ccli.wms.entity.preEntry;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 预报单
 */
@Entity
@Table(name = "BIS_PREENTRY")
public class BisPreEntry implements java.io.Serializable {

	private static final long serialVersionUID = -4326979420246878235L;

	@Id
	@Column(name = "FOR_ID")
	private String forId;//预报单ID

	@Column(name = "SERVICE_PROJECT")
	private String serviceProject;  //服务项目 报进_0;报出_1

	/**
	 * 状态
	 * 0-待完善，状态为0
	 * 1-待除核，状态为1
	 * 2-待复审，状态为2
	 * 3-待申报，状态为3
	 * 4-申报核注清单中，状态为4
	 * 5-申报核注清单通过，状态为5
	 * 6-申报报关中，状态为6
	 * 7-报关通过，状态为7
	 */
	@Column(name = "STATE")
	private String state;  //状态

	@Column(name = "CREATE_BY")
	private String createBy; //创建人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	private Date createTime;  //创建日期

	@Column(name = "UPDATE_BY")
	private String updateBy; //修改人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;  //修改日期

	@Column(name = "JL_AUDIT")
	private String jlAudit; //初审人

	@Column(name = "JL_REJECT_REASON")
	private String jlRejectReason; //初审驳回原因

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "JL_AUDIT_TIME")
	private Date jlAuditTime;  //初审时间

	@Column(name = "ZG_AUDIT")
	private String zgAudit; //复审审核

	@Column(name = "ZG_REJECT_REASON")
	private String zgRejectReason; //复审驳回原因

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "ZG_AUDIT_TIME")
	private Date zgAuditTime;  //复审时间

	@Column(name = "CD_BY")
	private String cdBy; //报关人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CD_TIME")
	private Date cdTime;  //报关时间

	@Column(name = "UP_AND_DOWN")
	private String upAndDown; //上传/下载,0-未上传;1-已上传;2-已下载

	@Column(name = "UP_BY")
	private String upBy; //上传人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UP_TIME")
	private Date upTime;  //上传时间

	@Column(name = "UP_FILE_NAME")
	private String upFileName; //上传文件名

	@Column(name = "CHECK_LIST_NO")
	private String checkListNo; //核注清单号

	@Column(name = "CD_NUM")
	private String cdNum;  //报关单号

	@Column(name = "CLIENT_ID")
	private String clientId; //客户ID

	@Column(name = "CLIENT_NAME")
	private String clientName; //客户名称

	@Column(name = "DECLARATION_UNIT_ID")
	private String declarationUnitId; //报关公司ID

	@Column(name = "DECLARATION_UNIT")
	private String declarationUnit; //报关公司

	@Column(name = "BILL_NUM")
	private String billNum;  //提单号

	@Column(name = "CTN_CONT")
	private Integer ctnCont;  //箱量

	@Column(name = "TRADE_MODE")
	private String tradeMode;  //贸易方式

	@Column(name = "CD_SIGN")
	private Integer cdSign;    //(1已通关，0未通关)报关标志

	@Column(name = "REMARK")
	private String remark;  //备注

	@Column(name = "CUSTOMER_SERVICE")
	private String customerService;  //客服

	@Column(name = "PRODUCT_NAME")
	private String productName;  //品名

	@Column(name = "HS_NO")
	private String hsNo;  //HS编码

	@Column(name = "PRICE")
	private String price;  //件数

	@Column(name = "NET_WEIGHT")
	private Double netWeight;  //重量

	@Column(name = "CONSIGNEE")
	private String consignee;   // 收货人

	@Column(name = "CONSIGNOR")
	private String consignor;   // 发货人

	@Column(name = "CONTRY_ORAGIN")
	private String contryOragin; // 原产国

	@Column(name = "YLRTYBH")
	private String YLRTYBH;
	@Column(name = "QDBH")
	private String QDBH;
	@Column(name = "QDLX")
	private String QDLX;
	@Column(name = "ZCBH")
	private String ZCBH;
	@Column(name = "JYDWBM")
	private String JYDWBM;
	@Column(name = "JYDWSHXYDM")
	private String JYDWSHXYDM;
	@Column(name = "JYDWMC")
	private String JYDWMC;
	@Column(name = "JGDWBM")
	private String JGDWBM;
	@Column(name = "JGDWSHXYDM")
	private String JGDWSHXYDM;
	@Column(name = "JGDWMC")
	private String JGDWMC;
	@Column(name = "SBDWBM")
	private String SBDWBM;
	@Column(name = "SBDWSHXYDM")
	private String SBDWSHXYDM;
	@Column(name = "SBDWMC")
	private String SBDWMC;
	@Column(name = "LRDWBM")
	private String LRDWBM;
	@Column(name = "LRDWSHXYDM")
	private String LRDWSHXYDM;
	@Column(name = "LRDWMC")
	private String LRDWMC;
	@Column(name = "QYNBBH")
	private String QYNBBH;
	@Column(name = "SBLX")
	private String SBLX;
	@Column(name = "LRRQ")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date LRRQ;
	@Column(name = "QDSBRQ")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date QDSBRQ;
	@Column(name = "LJCPBZ")
	private String LJCPBZ;
	@Column(name = "JGFS")
	private String JGFS;
	@Column(name = "YSFS")
	private String YSFS;
	@Column(name = "JCJGB")
	private String JCJGB;
	@Column(name = "ZGHG")
	private String ZGHG;
	@Column(name = "HKBZ")
	private String HKBZ;
	@Column(name = "QYG")
	private String QYG;
	@Column(name = "QGJCKKZT")
	private String QGJCKKZT;
	@Column(name = "SBBBH")
	private String SBBBH;
	@Column(name = "LZLX")
	private String LZLX;
	@Column(name = "BGBZ")
	private String BGBZ;
	@Column(name = "SFXTSCBGD")
	private String SFXTSCBGD;
	@Column(name = "BGDLX")
	private String BGDLX;
	@Column(name = "BGLX")
	private String BGLX;
	@Column(name = "DYBGDBH")
	private String DYBGDBH;
	@Column(name = "DYBGDDWBM")
	private String DYBGDDWBM;
	@Column(name = "DYBGDDWSHXYDM")
	private String DYBGDDWSHXYDM;
	@Column(name = "DYBGDDWMC")
	private String DYBGDDWMC;
	@Column(name = "GLBGDBH")
	private String GLBGDBH;
	@Column(name = "GLQDBH")
	private String GLQDBH;
	@Column(name = "GLSCBAH")
	private String GLSCBAH;
	@Column(name = "BLBGDJNSFHRBM")
	private String BLBGDJNSFHRBM;
	@Column(name = "BLBGDJNSFHRSHXYDM")
	private String BLBGDJNSFHRSHXYDM;
	@Column(name = "BLBGDJNSFHRMC")
	private String BLBGDJNSFHRMC;
	@Column(name = "GLBGDSCXSDWBM")
	private String GLBGDSCXSDWBM;
	@Column(name = "GLBGDSCXSDWSHXYDM")
	private String GLBGDSCXSDWSHXYDM;
	@Column(name = "GLBGDSCXSDWMC")
	private String GLBGDSCXSDWMC;
	@Column(name = "BLBGDSBDWBM")
	private String BLBGDSBDWBM;
	@Column(name = "BLBGDSBDWSHXYDM")
	private String BLBGDSBDWSHXYDM;
	@Column(name = "BLBGDSBDWMC")
	private String BLBGDSBDWMC;
	@Column(name = "BGDSBRQ")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date BGDSBRQ;
	@Column(name = "BZ")
	private String BZ;
	@Column(name = "BGDTYBH")
	private String BGDTYBH;
	@Column(name = "BGDCGBZ")
	private String BGDCGBZ;
	@Column(name = "CZYKH")
	private String CZYKH;

//	@Column(name = "ETPS_INNER_INVT_NO")
//	private String etpsInnerInvtNo;

	@Column(name = "SEQ_NO")
	private String seqNo;

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

//	public String getEtpsInnerInvtNo() {
//		return etpsInnerInvtNo;
//	}
//
//	public void setEtpsInnerInvtNo(String etpsInnerInvtNo) {
//		this.etpsInnerInvtNo = etpsInnerInvtNo;
//	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getForId() {
		return forId;
	}

	public void setForId(String forId) {
		this.forId = forId;
	}

	public String getServiceProject() {
		return serviceProject;
	}

	public void setServiceProject(String serviceProject) {
		this.serviceProject = serviceProject;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getJlAudit() {
		return jlAudit;
	}

	public void setJlAudit(String jlAudit) {
		this.jlAudit = jlAudit;
	}

	public Date getJlAuditTime() {
		return jlAuditTime;
	}

	public void setJlAuditTime(Date jlAuditTime) {
		this.jlAuditTime = jlAuditTime;
	}

	public String getJlRejectReason() {
		return jlRejectReason;
	}

	public void setJlRejectReason(String jlRejectReason) {
		this.jlRejectReason = jlRejectReason;
	}

	public String getZgRejectReason() {
		return zgRejectReason;
	}

	public void setZgRejectReason(String zgRejectReason) {
		this.zgRejectReason = zgRejectReason;
	}

	public String getZgAudit() {
		return zgAudit;
	}

	public void setZgAudit(String zgAudit) {
		this.zgAudit = zgAudit;
	}

	public Date getZgAuditTime() {
		return zgAuditTime;
	}

	public void setZgAuditTime(Date zgAuditTime) {
		this.zgAuditTime = zgAuditTime;
	}

	public String getCdBy() {
		return cdBy;
	}

	public void setCdBy(String cdBy) {
		this.cdBy = cdBy;
	}

	public Date getCdTime() {
		return cdTime;
	}

	public void setCdTime(Date cdTime) {
		this.cdTime = cdTime;
	}

	public String getUpAndDown() {
		return upAndDown;
	}

	public void setUpAndDown(String upAndDown) {
		this.upAndDown = upAndDown;
	}

	public String getUpBy() {
		return upBy;
	}

	public void setUpBy(String upBy) {
		this.upBy = upBy;
	}

	public Date getUpTime() {
		return upTime;
	}

	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}

	public String getUpFileName() {
		return upFileName;
	}

	public void setUpFileName(String upFileName) {
		this.upFileName = upFileName;
	}

	public String getCheckListNo() {
		return checkListNo;
	}

	public void setCheckListNo(String checkListNo) {
		this.checkListNo = checkListNo;
	}

	public String getCdNum() {
		return cdNum;
	}

	public void setCdNum(String cdNum) {
		this.cdNum = cdNum;
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

	public String getDeclarationUnitId() {
		return declarationUnitId;
	}

	public void setDeclarationUnitId(String declarationUnitId) {
		this.declarationUnitId = declarationUnitId;
	}

	public String getDeclarationUnit() {
		return declarationUnit;
	}

	public void setDeclarationUnit(String declarationUnit) {
		this.declarationUnit = declarationUnit;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public Integer getCtnCont() {
		return ctnCont;
	}

	public void setCtnCont(Integer ctnCont) {
		this.ctnCont = ctnCont;
	}

	public String getTradeMode() {
		return tradeMode;
	}

	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}

	public Integer getCdSign() {
		return cdSign;
	}

	public void setCdSign(Integer cdSign) {
		this.cdSign = cdSign;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCustomerService() {
		return customerService;
	}

	public void setCustomerService(String customerService) {
		this.customerService = customerService;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getHsNo() {
		return hsNo;
	}

	public void setHsNo(String hsNo) {
		this.hsNo = hsNo;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsignor() {
		return consignor;
	}

	public void setConsignor(String consignor) {
		this.consignor = consignor;
	}

	public String getContryOragin() {
		return contryOragin;
	}

	public void setContryOragin(String contryOragin) {
		this.contryOragin = contryOragin;
	}

	public String getYLRTYBH() {
		return YLRTYBH;
	}

	public void setYLRTYBH(String YLRTYBH) {
		this.YLRTYBH = YLRTYBH;
	}

	public String getQDBH() {
		return QDBH;
	}

	public void setQDBH(String QDBH) {
		this.QDBH = QDBH;
	}

	public String getQDLX() {
		return QDLX;
	}

	public void setQDLX(String QDLX) {
		this.QDLX = QDLX;
	}

	public String getZCBH() {
		return ZCBH;
	}

	public void setZCBH(String ZCBH) {
		this.ZCBH = ZCBH;
	}

	public String getJYDWBM() {
		return JYDWBM;
	}

	public void setJYDWBM(String JYDWBM) {
		this.JYDWBM = JYDWBM;
	}

	public String getJYDWSHXYDM() {
		return JYDWSHXYDM;
	}

	public void setJYDWSHXYDM(String JYDWSHXYDM) {
		this.JYDWSHXYDM = JYDWSHXYDM;
	}

	public String getJYDWMC() {
		return JYDWMC;
	}

	public void setJYDWMC(String JYDWMC) {
		this.JYDWMC = JYDWMC;
	}

	public String getJGDWBM() {
		return JGDWBM;
	}

	public void setJGDWBM(String JGDWBM) {
		this.JGDWBM = JGDWBM;
	}

	public String getJGDWSHXYDM() {
		return JGDWSHXYDM;
	}

	public void setJGDWSHXYDM(String JGDWSHXYDM) {
		this.JGDWSHXYDM = JGDWSHXYDM;
	}

	public String getJGDWMC() {
		return JGDWMC;
	}

	public void setJGDWMC(String JGDWMC) {
		this.JGDWMC = JGDWMC;
	}

	public String getSBDWBM() {
		return SBDWBM;
	}

	public void setSBDWBM(String SBDWBM) {
		this.SBDWBM = SBDWBM;
	}

	public String getSBDWSHXYDM() {
		return SBDWSHXYDM;
	}

	public void setSBDWSHXYDM(String SBDWSHXYDM) {
		this.SBDWSHXYDM = SBDWSHXYDM;
	}

	public String getSBDWMC() {
		return SBDWMC;
	}

	public void setSBDWMC(String SBDWMC) {
		this.SBDWMC = SBDWMC;
	}

	public String getLRDWBM() {
		return LRDWBM;
	}

	public void setLRDWBM(String LRDWBM) {
		this.LRDWBM = LRDWBM;
	}

	public String getLRDWSHXYDM() {
		return LRDWSHXYDM;
	}

	public void setLRDWSHXYDM(String LRDWSHXYDM) {
		this.LRDWSHXYDM = LRDWSHXYDM;
	}

	public String getLRDWMC() {
		return LRDWMC;
	}

	public void setLRDWMC(String LRDWMC) {
		this.LRDWMC = LRDWMC;
	}

	public String getQYNBBH() {
		return QYNBBH;
	}

	public void setQYNBBH(String QYNBBH) {
		this.QYNBBH = QYNBBH;
	}

	public String getSBLX() {
		return SBLX;
	}

	public void setSBLX(String SBLX) {
		this.SBLX = SBLX;
	}

	public Date getLRRQ() {
		return LRRQ;
	}

	public void setLRRQ(Date LRRQ) {
		this.LRRQ = LRRQ;
	}

	public Date getQDSBRQ() {
		return QDSBRQ;
	}

	public void setQDSBRQ(Date QDSBRQ) {
		this.QDSBRQ = QDSBRQ;
	}

	public String getLJCPBZ() {
		return LJCPBZ;
	}

	public void setLJCPBZ(String LJCPBZ) {
		this.LJCPBZ = LJCPBZ;
	}

	public String getJGFS() {
		return JGFS;
	}

	public void setJGFS(String JGFS) {
		this.JGFS = JGFS;
	}

	public String getYSFS() {
		return YSFS;
	}

	public void setYSFS(String YSFS) {
		this.YSFS = YSFS;
	}

	public String getJCJGB() {
		return JCJGB;
	}

	public void setJCJGB(String JCJGB) {
		this.JCJGB = JCJGB;
	}

	public String getZGHG() {
		return ZGHG;
	}

	public void setZGHG(String ZGHG) {
		this.ZGHG = ZGHG;
	}

	public String getHKBZ() {
		return HKBZ;
	}

	public void setHKBZ(String HKBZ) {
		this.HKBZ = HKBZ;
	}

	public String getQYG() {
		return QYG;
	}

	public void setQYG(String QYG) {
		this.QYG = QYG;
	}

	public String getQGJCKKZT() {
		return QGJCKKZT;
	}

	public void setQGJCKKZT(String QGJCKKZT) {
		this.QGJCKKZT = QGJCKKZT;
	}

	public String getSBBBH() {
		return SBBBH;
	}

	public void setSBBBH(String SBBBH) {
		this.SBBBH = SBBBH;
	}

	public String getLZLX() {
		return LZLX;
	}

	public void setLZLX(String LZLX) {
		this.LZLX = LZLX;
	}

	public String getBGBZ() {
		return BGBZ;
	}

	public void setBGBZ(String BGBZ) {
		this.BGBZ = BGBZ;
	}

	public String getSFXTSCBGD() {
		return SFXTSCBGD;
	}

	public void setSFXTSCBGD(String SFXTSCBGD) {
		this.SFXTSCBGD = SFXTSCBGD;
	}

	public String getBGDLX() {
		return BGDLX;
	}

	public void setBGDLX(String BGDLX) {
		this.BGDLX = BGDLX;
	}

	public String getBGLX() {
		return BGLX;
	}

	public void setBGLX(String BGLX) {
		this.BGLX = BGLX;
	}

	public String getDYBGDBH() {
		return DYBGDBH;
	}

	public void setDYBGDBH(String DYBGDBH) {
		this.DYBGDBH = DYBGDBH;
	}

	public String getDYBGDDWBM() {
		return DYBGDDWBM;
	}

	public void setDYBGDDWBM(String DYBGDDWBM) {
		this.DYBGDDWBM = DYBGDDWBM;
	}

	public String getDYBGDDWSHXYDM() {
		return DYBGDDWSHXYDM;
	}

	public void setDYBGDDWSHXYDM(String DYBGDDWSHXYDM) {
		this.DYBGDDWSHXYDM = DYBGDDWSHXYDM;
	}

	public String getDYBGDDWMC() {
		return DYBGDDWMC;
	}

	public void setDYBGDDWMC(String DYBGDDWMC) {
		this.DYBGDDWMC = DYBGDDWMC;
	}

	public String getGLBGDBH() {
		return GLBGDBH;
	}

	public void setGLBGDBH(String GLBGDBH) {
		this.GLBGDBH = GLBGDBH;
	}

	public String getGLSCBAH() {
		return GLSCBAH;
	}

	public void setGLSCBAH(String GLSCBAH) {
		this.GLSCBAH = GLSCBAH;
	}

	public String getBLBGDJNSFHRBM() {
		return BLBGDJNSFHRBM;
	}

	public void setBLBGDJNSFHRBM(String BLBGDJNSFHRBM) {
		this.BLBGDJNSFHRBM = BLBGDJNSFHRBM;
	}

	public String getBLBGDJNSFHRSHXYDM() {
		return BLBGDJNSFHRSHXYDM;
	}

	public void setBLBGDJNSFHRSHXYDM(String BLBGDJNSFHRSHXYDM) {
		this.BLBGDJNSFHRSHXYDM = BLBGDJNSFHRSHXYDM;
	}

	public String getBLBGDJNSFHRMC() {
		return BLBGDJNSFHRMC;
	}

	public void setBLBGDJNSFHRMC(String BLBGDJNSFHRMC) {
		this.BLBGDJNSFHRMC = BLBGDJNSFHRMC;
	}

	public String getGLBGDSCXSDWBM() {
		return GLBGDSCXSDWBM;
	}

	public void setGLBGDSCXSDWBM(String GLBGDSCXSDWBM) {
		this.GLBGDSCXSDWBM = GLBGDSCXSDWBM;
	}

	public String getGLBGDSCXSDWSHXYDM() {
		return GLBGDSCXSDWSHXYDM;
	}

	public void setGLBGDSCXSDWSHXYDM(String GLBGDSCXSDWSHXYDM) {
		this.GLBGDSCXSDWSHXYDM = GLBGDSCXSDWSHXYDM;
	}

	public String getGLBGDSCXSDWMC() {
		return GLBGDSCXSDWMC;
	}

	public void setGLBGDSCXSDWMC(String GLBGDSCXSDWMC) {
		this.GLBGDSCXSDWMC = GLBGDSCXSDWMC;
	}

	public String getBLBGDSBDWBM() {
		return BLBGDSBDWBM;
	}

	public void setBLBGDSBDWBM(String BLBGDSBDWBM) {
		this.BLBGDSBDWBM = BLBGDSBDWBM;
	}

	public String getBLBGDSBDWSHXYDM() {
		return BLBGDSBDWSHXYDM;
	}

	public void setBLBGDSBDWSHXYDM(String BLBGDSBDWSHXYDM) {
		this.BLBGDSBDWSHXYDM = BLBGDSBDWSHXYDM;
	}

	public String getBLBGDSBDWMC() {
		return BLBGDSBDWMC;
	}

	public void setBLBGDSBDWMC(String BLBGDSBDWMC) {
		this.BLBGDSBDWMC = BLBGDSBDWMC;
	}

	public Date getBGDSBRQ() {
		return BGDSBRQ;
	}

	public void setBGDSBRQ(Date BGDSBRQ) {
		this.BGDSBRQ = BGDSBRQ;
	}

	public String getBZ() {
		return BZ;
	}

	public void setBZ(String BZ) {
		this.BZ = BZ;
	}

	public String getBGDTYBH() {
		return BGDTYBH;
	}

	public void setBGDTYBH(String BGDTYBH) {
		this.BGDTYBH = BGDTYBH;
	}

	public String getBGDCGBZ() {
		return BGDCGBZ;
	}

	public void setBGDCGBZ(String BGDCGBZ) {
		this.BGDCGBZ = BGDCGBZ;
	}

	public String getCZYKH() {
		return CZYKH;
	}

	public void setCZYKH(String CZYKH) {
		this.CZYKH = CZYKH;
	}

	public String getGLQDBH() {
		return GLQDBH;
	}

	public void setGLQDBH(String GLQDBH) {
		this.GLQDBH = GLQDBH;
	}
}