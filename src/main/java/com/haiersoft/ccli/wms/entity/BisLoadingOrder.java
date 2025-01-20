package com.haiersoft.ccli.wms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 出库订单6
 * BisLoadingOrder entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BIS_LOADING_ORDER")
@DynamicUpdate
@DynamicInsert
public class BisLoadingOrder implements java.io.Serializable {

    private static final long serialVersionUID = 95655454047802915L;

    private String orderNum;//订单号  O+yymmdd+3seq
    private String outLinkId;//出库联系单号
    private String orderState;//1创建、2分配出库中（生成装车单以后）、3拣货中、4已出  出库订单状态
    private String receiverId;//收货方id
    private String receiverName;//收货方名称
    private String createPerson;//创建人
    private Date createTime;//创建时间
    private String updatePerson;//修改人
    private Date updateTime;//修改时间
    private Date planTime;//计划出库时间
    private String billNum;//提单号
    private String stockIn;//存货方id
    private String stockName;//存货方
    private String remark;//备注
    private String ifClearStore; //是否清库
    private String carNum;//车牌号
    private String warehouseId;//仓库ID 关联入库联系单获取
    private String warehouse;//仓库名称
    private Integer lastCar = 0; //是否是最后一车，0：否 1：是
    private Integer ifHasCleared = 0; //是否清库结算过 0：否 1：是
    private String mxjs;//明细件数
    private String islock;//是否进行控车 默认不控，1是控车
    //追加列表字段
    private Integer isEdite = 0;//标记是否可以修改数据 0可编辑，1不可编辑
    private Integer isEditeTwo=0;//标记计划出库日期是否可以修改 0可以 1不可以
    private Integer isClear = 0;//标记是否清库  1清库
    private String[] skuList;//sku集合
    private String[] billList;//提单
    private String[] ctnList;//厢号
    private String[] asnList;//ASN
    private String[] foodList;//品名
    private String[] intoList;//入库类型
    private Integer[] peacList;//出库件数
    private Double[] netList;//单净重
    private Double[] grossList;//单毛重
    private String[] rkList;//入库号
    private String[] lotList;//入库号
    private String[] typeSizeList;//入库号
    private String[] mscList;//入库号
    private String[] delList;//明细删除列
    private String[] remarkList;//明细备注列
    private Date planDateNoPay;//不计费计划出库日期
    
    private String ruleJobType;//作业类型
    // Constructors

    /**
     * default constructor
     */

    public BisLoadingOrder() {
    }

    private String ifBonded;   //是否保税
    @Transient
    public String getIfBonded() {
        return ifBonded;
    }
    public void setIfBonded(String ifBonded) {
        this.ifBonded = ifBonded;
    }

    @Id
    @Column(name = "ORDER_NUM")
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
    
    @Column(name = "ISLOCK")
    public String getIslock() {
		return islock;
	}

	public void setIslock(String islock) {
		this.islock = islock;
	}


	@Column(name = "OUT_LINK_ID")
    public String getOutLinkId() {
        return outLinkId;
    }
    
	public void setOutLinkId(String outLinkId) {
        this.outLinkId = outLinkId;
    }

    @Column(name = "ORDER_STATE")
    public String getOrderState() {
        return this.orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    @Column(name = "RECEIVER_ID")
    public String getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @Column(name = "RECEIVER_NAME")
    public String getReceiverName() {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    @Column(name = "CREATE_PERSON")
    public String getCreatePerson() {
        return this.createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_PERSON")
    public String getUpdatePerson() {
        return this.updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @Column(name = "PLAN_DATE")
    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    @Column(name = "BILL_NUM")
    public String getBillNum() {
        return this.billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    @Column(name = "CARNUM")
    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    @Column(name = "STOCK_ID")
    public String getStockIn() {
        return this.stockIn;
    }

    public void setStockIn(String stockIn) {
        this.stockIn = stockIn;
    }

    @Column(name = "STOCK_NAME")
    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "WAREHOUSE_ID")
    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Column(name = "WAREHOUSE")
    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }


    @Column(name = "IF_CLEAR_STORE")
    public String getIfClearStore() {
        return ifClearStore;
    }

    public void setIfClearStore(String ifClearStore) {
        this.ifClearStore = ifClearStore;
    }

    @Column(name = "LAST_CAR")
    public Integer getLastCar() {
        return lastCar;
    }

    public void setLastCar(Integer lastCar) {
        this.lastCar = lastCar;
    }

    @Transient
    public Integer getIsEdite() {
        return isEdite;
    }

    public void setIsEdite(Integer isEdite) {
        this.isEdite = isEdite;
    }

    @Transient
    public Integer getIsClear() {
        return isClear;
    }

    public void setIsClear(Integer isClear) {
        this.isClear = isClear;
    }

    @Transient
    public String[] getSkuList() {
        return skuList;
    }

    public void setSkuList(String[] skuList) {
        this.skuList = skuList;
    }

    @Transient
    public String[] getBillList() {
        return billList;
    }

    public void setBillList(String[] billList) {
        this.billList = billList;
    }

    @Transient
    public String[] getCtnList() {
        return ctnList;
    }

    public void setCtnList(String[] ctnList) {
        this.ctnList = ctnList;
    }

    @Transient
    public String[] getFoodList() {
        return foodList;
    }

    public void setFoodList(String[] foodList) {
        this.foodList = foodList;
    }

    @Transient
    public String[] getIntoList() {
        return intoList;
    }

    public void setIntoList(String[] intoList) {
        this.intoList = intoList;
    }

    @Transient
    public Integer[] getPeacList() {
        return peacList;
    }

    public void setPeacList(Integer[] peacList) {
        this.peacList = peacList;
    }

    @Transient
    public Double[] getNetList() {
        return netList;
    }

    public void setNetList(Double[] netList) {
        this.netList = netList;
    }

    @Transient
    public Double[] getGrossList() {
        return grossList;
    }

    public void setGrossList(Double[] grossList) {
        this.grossList = grossList;
    }

    @Transient
    public String[] getDelList() {
        return delList;
    }

    public void setDelList(String[] delList) {
        this.delList = delList;
    }

    @Transient
    public String[] getRkList() {
        return rkList;
    }

    public void setRkList(String[] rkList) {
        this.rkList = rkList;
    }

    @Transient
    public String[] getLotList() {
		return lotList;
	}

	public void setLotList(String[] lotList) {
		this.lotList = lotList;
	}
	
	@Transient
	public String[] getAsnList() {
		return asnList;
	}

	public void setAsnList(String[] asnList) {
		this.asnList = asnList;
	}

	@Transient
	public String[] getMscList() {
		return mscList;
	}

	public void setMscList(String[] mscList) {
		this.mscList = mscList;
	}

	@Transient
    public String[] getRemarkList() {
        return remarkList;
    }

    public void setRemarkList(String[] remarkList) {
        this.remarkList = remarkList;
    }

    @Column(name = "IF_HAS_CLEARED")
    public Integer getIfHasCleared() {
        return ifHasCleared;
    }

    public void setIfHasCleared(Integer ifHasCleared) {
        this.ifHasCleared = ifHasCleared;
    }

    @Transient
    public String getMxjs() {
        return mxjs;
    }

    public void setMxjs(String mxjs) {
        this.mxjs = mxjs;
    }
    @Transient
	public Integer getIsEditeTwo() {
		return isEditeTwo;
	}

	public void setIsEditeTwo(Integer isEditeTwo) {
		this.isEditeTwo = isEditeTwo;
	}
	@Transient
	public String[] getTypeSizeList() {
		return typeSizeList;
	}

	public void setTypeSizeList(String[] typeSizeList) {
		this.typeSizeList = typeSizeList;
	}
	 @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	 @Column(name = "PLAN_DATE_NOPAY")
	public Date getPlanDateNoPay() {
		return planDateNoPay;
	}

	public void setPlanDateNoPay(Date planDateNoPay) {
		this.planDateNoPay = planDateNoPay;
	}
	@Column(name = "RULE_JOB_TYPE")
	public String getRuleJobType() {
		return ruleJobType;
	}

	public void setRuleJobType(String ruleJobType) {
		this.ruleJobType = ruleJobType;
	}
    
	
	
}