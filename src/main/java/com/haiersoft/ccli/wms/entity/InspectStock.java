package com.haiersoft.ccli.wms.entity;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "BIS_INSPECT_STOCK")
public class InspectStock {

    @Id
    @Column(name = "INSPECT_ID")
    private String inspectId;

    @Basic
    @Column(name = "BILL_NUM")
    private String billNum;

    @Basic
    @Column(name = "CTN_NUM")
    private String ctnNum;

    @Basic
    @Column(name = "SKU_ID")
    private String skuId;

    @Basic
    @Column(name = "STOCK_NAME")
    private String stockName;

    @Basic
    @Column(name = "INSPECT_TOTAL")
    private Integer inspectTotal;

    @Basic
    @Column(name = "OPERATE_STATE")
    private Integer operateState;

    @Basic
    @Column(name = "OPERATE_DATE")
    private Date operateDate;

    @Basic
    @Column(name = "OPERATE_USER_NAME")
    private String operateUserName;

    @Basic
    @Column(name = "OPERATE_USER_ID")
    private String operateUserId;

    @Basic
    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Basic
    @Column(name = "CREATE_USER_NAME")
    private String createUserName;

    @Basic
    @Column(name = "CREATE_USER_ID")
    private String createUserId;

    @Basic
    @Column(name = "DESCRIPTION")
    private String description;

    @Basic
    @Column(name = "TRAY_ID")
    private String trayId;

    @Basic
    @Column(name = "WAREHOUSE")
    private String warehouse;

    @Basic
    @Column(name = "CARGO_NAME")
    private String cargoName;

    @Basic
    @Column(name = "CONTACT_NUM")
    private String contactNum;

    @Basic
    @Column(name = "ASN")
    private String asn;

    @Basic
    @Column(name = "STATE")
    private Integer state;
    
    @Basic
    @Column(name = "CHECK_DATE")
    private Date checkDate;
    
    @Basic
    @Column(name = "SAMPLE_DATE")
    private Date sampleDate;
    
    @Basic
    @Column(name = "INSPECT_MASTER_ID")
    private String inspectMasterId;
    
    @Basic
    @Column(name = "CARGO_LOCATION")
    private String cargoLocation;
    
    @Basic
    @Column(name = "SAMPLE_UNIT")
    private String sampleUnit;  //取样件数的计量单位
    
    
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getInspectId() {
        return inspectId;
    }

    public void setInspectId(String inspectId) {
        this.inspectId = inspectId;
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


    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }


    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }


    public Integer getInspectTotal() {
        return inspectTotal;
    }

    public void setInspectTotal(Integer inspectTotal) {
        this.inspectTotal = inspectTotal;
    }


    public Integer getOperateState() {
        return operateState;
    }

    public void setOperateState(Integer operateState) {
        this.operateState = operateState;
    }


    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }


    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }


    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTrayId() {
        return trayId;
    }

    public void setTrayId(String trayId) {
        this.trayId = trayId;
    }


    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }


    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }


    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }


    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getSampleDate() {
		return sampleDate;
	}

	public void setSampleDate(Date sampleDate) {
		this.sampleDate = sampleDate;
	}

	public String getInspectMasterId() {
		return inspectMasterId;
	}

	public void setInspectMasterId(String inspectMasterId) {
		this.inspectMasterId = inspectMasterId;
	}

	public String getCargoLocation() {
		return cargoLocation;
	}

	public void setCargoLocation(String cargoLocation) {
		this.cargoLocation = cargoLocation;
	}

	public String getSampleUnit() {
		return sampleUnit;
	}

	public void setSampleUnit(String sampleUnit) {
		this.sampleUnit = sampleUnit;
	}
    
	
    
}
