package com.haiersoft.ccli.report.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Connor.M
 * @ClassName: Stock
 * @Description: 库存信息表
 * @date 2016年3月9日 下午3:44:43
 */
public class Stock implements Serializable {

    private static final long serialVersionUID = 2799111007612445959L;

    private String trayCode; //托盘号
    private String billCode; //提单号
    private String asn; //ASN
    private String sku; //SKU
    private String contactCode; //联系单号
    private String clientId; //客户id
    private String clientName; //客户名称
    private String shf;//收货方
    private String locationCode; //库位号
    private String warehouse; //仓库名 gzq 20160627 添加
    private String warehouseId; //仓库ID gzq 20160627 添加
    private String cargoName; //产品名称
    private String cargoType; //产品类型
    private Integer nowNum; //现有数量
    private Integer allpiece;//总数量
    private Double netWeight;//净重
    private Double grossWeight;//毛重
    private Double allnet;//总净重
    private Double allgross;//总毛重
    private String units;//单位
    private String state;//默认为00 00已收货（理货确认）01已上架（库管确认）10出库中（装车单）11出库理货（库管确认）12已出库（理货确认）20待回库 21回库收货（理货确认）  99报损货物状态

    private String ctnNum;//箱号

    private String enterPerson;//入库理货人员

    private String enterOp;//入库操作人员

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date enterTime;//入库理货时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date inTime;//入库时间

    private Date searchStrartTime;//开始时间

    private Date searchEndTime;//结束时间

    private String strartTime;//开始时间

    private String endTime;//结束时间

    private String locationType;//是否存在库位  1有，2没有

    private String reportType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date backupTime;

    private String floorNum;

    private String roomNum;

    private String areaNum;
    
    private String isBonded;//保税非保税
    
    private String bgdh;//报关单号
    private String ycg;//原产国
    private String bgdhdate;//申报日期
    private String rkTime;//实际入库时间
    private String cz;//箱型尺寸
    private String createUser;//操作人
    private String bigName;//大类名称
    private String bigType;//大类id
    private String simName;//小类名称
    private String simType;//小类id
    
    private String companyId;//报关企业id
    private String companyName;//报关企业名称
    private Integer companyNum;//报关数量
    
    private String ciqId;//报检企业id
    private String ciqName;//报检企业名称
    private Integer ciqNum;//报检数量
    
    private String orgId;//结算单位id
    private String orgName;//结算单位名称

    private Integer day;//距离生产日期之间日差数
    private Integer days;//距离报关之间月差数
    private Integer spNum;//审批数量
    private String remark;//备注
    

	private String hsCode;//hs编码


	private String hsItemname;//海关商品名称
	

	private String accountBook;//账册商品序号
    private String uploader;//保税转一般贸易人员
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date uploadDate;//修改时间

    private String xz;//货位号

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date makeTimes;//生产开始日期

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date makeTimee;//生产截至日期
    
    ////////////////////////////////////////////////////////////


    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Date getMakeTimes() {
        return makeTimes;
    }

    public void setMakeTimes(Date makeTimes) {
        this.makeTimes = makeTimes;
    }

    public Date getMakeTimee() {
        return makeTimee;
    }

    public void setMakeTimee(Date makeTimee) {
        this.makeTimee = makeTimee;
    }

    public String getHsCode() {
		return hsCode;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getHsItemname() {
		return hsItemname;
	}

	public void setHsItemname(String hsItemname) {
		this.hsItemname = hsItemname;
	}

	public String getAccountBook() {
		return accountBook;
	}

	public void setAccountBook(String accountBook) {
		this.accountBook = accountBook;
	}

	public String getShf() {
		return shf;
	}

	public Integer getSpNum() {
		return spNum;
	}

	public void setSpNum(Integer spNum) {
		this.spNum = spNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(Integer companyNum) {
		this.companyNum = companyNum;
	}

	public String getCiqId() {
		return ciqId;
	}

	public void setCiqId(String ciqId) {
		this.ciqId = ciqId;
	}

	public String getCiqName() {
		return ciqName;
	}

	public void setCiqName(String ciqName) {
		this.ciqName = ciqName;
	}

	public Integer getCiqNum() {
		return ciqNum;
	}

	public void setCiqNum(Integer ciqNum) {
		this.ciqNum = ciqNum;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRkTime() {
		return rkTime;
	}

	public void setRkTime(String rkTime) {
		this.rkTime = rkTime;
	}

	public String getBgdh() {
		return bgdh;
	}

	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}

	public String getYcg() {
		return ycg;
	}

	public void setYcg(String ycg) {
		this.ycg = ycg;
	}

	public String getBgdhdate() {
		return bgdhdate;
	}

	public void setBgdhdate(String bgdhdate) {
		this.bgdhdate = bgdhdate;
	}

	public String getCz() {
		return cz;
	}

	public void setCz(String cz) {
		this.cz = cz;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getBigName() {
		return bigName;
	}

	public void setBigName(String bigName) {
		this.bigName = bigName;
	}

	public String getBigType() {
		return bigType;
	}

	public void setBigType(String bigType) {
		this.bigType = bigType;
	}

	public String getSimName() {
		return simName;
	}

	public void setSimName(String simName) {
		this.simName = simName;
	}

	public String getSimType() {
		return simType;
	}

	public void setSimType(String simType) {
		this.simType = simType;
	}

	public void setShf(String shf) {
		this.shf = shf;
	}

	public String getIsBonded() {
		return isBonded;
	}

	public void setIsBonded(String isBonded) {
		this.isBonded = isBonded;
	}

	public String getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public Date getBackupTime() {
        return backupTime;
    }

    public void setBackupTime(Date backupTime) {
        this.backupTime = backupTime;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getStrartTime() {
        return strartTime;
    }

    public void setStrartTime(String strartTime) {
        this.strartTime = strartTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCtnNum() {
        return ctnNum;
    }

    public void setCtnNum(String ctnNum) {
        this.ctnNum = ctnNum;
    }

    public Date getSearchStrartTime() {
        return searchStrartTime;
    }

    public void setSearchStrartTime(Date searchStrartTime) {
        this.searchStrartTime = searchStrartTime;
    }

    public Date getSearchEndTime() {
        return searchEndTime;
    }

    public void setSearchEndTime(Date searchEndTime) {
        this.searchEndTime = searchEndTime;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getContactCode() {
        return contactCode;
    }

    public void setContactCode(String contactCode) {
        this.contactCode = contactCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public Integer getNowNum() {
        return nowNum;
    }

    public void setNowNum(Integer nowNum) {
        this.nowNum = nowNum;
    }

    public Integer getAllpiece() {
        return allpiece;
    }

    public void setAllpiece(Integer allpiece) {
        this.allpiece = allpiece;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEnterPerson() {
        return enterPerson;
    }

    public void setEnterPerson(String enterPerson) {
        this.enterPerson = enterPerson;
    }

    public String getEnterOp() {
        return enterOp;
    }

    public void setEnterOp(String enterOp) {
        this.enterOp = enterOp;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Double getAllnet() {
        return allnet;
    }

    public void setAllnet(Double allnet) {
        this.allnet = allnet;
    }

    public Double getAllgross() {
        return allgross;
    }

    public void setAllgross(Double allgross) {
        this.allgross = allgross;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getXz() {
        return xz;
    }

    public void setXz(String xz) {
        this.xz = xz;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "trayCode='" + trayCode + '\'' +
                ", billCode='" + billCode + '\'' +
                ", asn='" + asn + '\'' +
                ", sku='" + sku + '\'' +
                ", contactCode='" + contactCode + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", locationCode='" + locationCode + '\'' +
                ", warehouse='" + warehouse + '\'' +
                ", warehouseId='" + warehouseId + '\'' +
                ", cargoName='" + cargoName + '\'' +
                ", cargoType='" + cargoType + '\'' +
                ", nowNum=" + nowNum +
                ", allpiece=" + allpiece +
                ", netWeight=" + netWeight +
                ", grossWeight=" + grossWeight +
                ", allnet=" + allnet +
                ", allgross=" + allgross +
                ", units='" + units + '\'' +
                ", state='" + state + '\'' +
                ", ctnNum='" + ctnNum + '\'' +
                ", enterPerson='" + enterPerson + '\'' +
                ", enterOp='" + enterOp + '\'' +
                ", enterTime=" + enterTime +
                ", inTime=" + inTime +
                ", searchStrartTime=" + searchStrartTime +
                ", searchEndTime=" + searchEndTime +
                ", strartTime='" + strartTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", locationType='" + locationType + '\'' +
                ", reportType='" + reportType + '\'' +
                ", backupTime='" + backupTime + '\'' +
                '}';
    }
}
