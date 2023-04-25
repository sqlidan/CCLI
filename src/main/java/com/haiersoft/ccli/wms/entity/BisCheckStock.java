package com.haiersoft.ccli.wms.entity;

import java.util.Date;

import javax.persistence.*;

/**
 * Created by galaxy on 2017/5/16.
 */

@Entity
@Table(name = "BIS_CHECK_STOCK")
public class BisCheckStock {

    private Integer id;
    private String trayId;
    private String ctnNum;
    private String billNum;
    private String skuId;
    private String cargoName;
    private Integer nowPiece;
    private Double netWeight;
    private Double grossWeight;
    private String units;
    private String realCargoName;
    private Integer realPiece;
    private String description;
    private String checkName;
    private Date checkTime;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHECK_STOCK")
    @SequenceGenerator(name = "SEQ_CHECK_STOCK", sequenceName = "SEQ_CHECK_STOCK", allocationSize = 1)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TRAY_ID")
    public String getTrayId() {
        return trayId;
    }

    public void setTrayId(String trayId) {
        this.trayId = trayId;
    }

    @Basic
    @Column(name = "CTN_NUM")
    public String getCtnNum() {
        return ctnNum;
    }

    public void setCtnNum(String ctnNum) {
        this.ctnNum = ctnNum;
    }

    @Basic
    @Column(name = "BILL_NUM")
    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    @Basic
    @Column(name = "SKU_ID")
    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    @Basic
    @Column(name = "CARGO_NAME")
    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    @Basic
    @Column(name = "NOW_PIECE")
    public Integer getNowPiece() {
        return nowPiece;
    }

    public void setNowPiece(Integer nowPiece) {
        this.nowPiece = nowPiece;
    }

    @Basic
    @Column(name = "NET_WEIGHT")
    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    @Basic
    @Column(name = "GROSS_WEIGHT")
    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    @Basic
    @Column(name = "UNITS")
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Basic
    @Column(name = "REAL_CARGO_NAME")
    public String getRealCargoName() {
        return realCargoName;
    }

    public void setRealCargoName(String realCargoName) {
        this.realCargoName = realCargoName;
    }

    @Basic
    @Column(name = "REAL_PIECE")
    public Integer getRealPiece() {
        return realPiece;
    }

    public void setRealPiece(Integer realPiece) {
        this.realPiece = realPiece;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    @Basic
    @Column(name = "CHECK_NAME")
    public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	@Basic
    @Column(name = "CHECK_TIME")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BisCheckStock that = (BisCheckStock) o;

        if (id != that.id) return false;
        if (trayId != null ? !trayId.equals(that.trayId) : that.trayId != null) return false;
        if (ctnNum != null ? !ctnNum.equals(that.ctnNum) : that.ctnNum != null) return false;
        if (billNum != null ? !billNum.equals(that.billNum) : that.billNum != null) return false;
        if (skuId != null ? !skuId.equals(that.skuId) : that.skuId != null) return false;
        if (cargoName != null ? !cargoName.equals(that.cargoName) : that.cargoName != null) return false;
        if (nowPiece != null ? !nowPiece.equals(that.nowPiece) : that.nowPiece != null) return false;
        if (netWeight != null ? !netWeight.equals(that.netWeight) : that.netWeight != null) return false;
        if (grossWeight != null ? !grossWeight.equals(that.grossWeight) : that.grossWeight != null) return false;
        if (units != null ? !units.equals(that.units) : that.units != null) return false;
        if (realCargoName != null ? !realCargoName.equals(that.realCargoName) : that.realCargoName != null)
            return false;
        if (realPiece != null ? !realPiece.equals(that.realPiece) : that.realPiece != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (trayId != null ? trayId.hashCode() : 0);
        result = 31 * result + (ctnNum != null ? ctnNum.hashCode() : 0);
        result = 31 * result + (billNum != null ? billNum.hashCode() : 0);
        result = 31 * result + (skuId != null ? skuId.hashCode() : 0);
        result = 31 * result + (cargoName != null ? cargoName.hashCode() : 0);
        result = 31 * result + (nowPiece != null ? nowPiece.hashCode() : 0);
        result = 31 * result + (netWeight != null ? netWeight.hashCode() : 0);
        result = 31 * result + (grossWeight != null ? grossWeight.hashCode() : 0);
        result = 31 * result + (units != null ? units.hashCode() : 0);
        result = 31 * result + (realCargoName != null ? realCargoName.hashCode() : 0);
        result = 31 * result + (realPiece != null ? realPiece.hashCode() : 0);
        return result;
    }
}
