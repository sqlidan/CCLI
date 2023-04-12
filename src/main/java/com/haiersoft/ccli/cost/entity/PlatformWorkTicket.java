package com.haiersoft.ccli.cost.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.platform.entity.PlatformReservationInbound;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import com.haiersoft.ccli.system.entity.User;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "PLATFORM_WORK_TICKET")
public class PlatformWorkTicket {


    @Id
    private String id;
    private String yyid;
    private String tallyId;
    private String forkliftUpId;
    private String forkliftSceneId;
    private String clientId;
    private Date updatedTime;
    private String deletedFlag;
    private String stevedoreId;
    private String ifAllMan;

    @Excel(name = "箱号")
    @Transient
    private String containerNo;//箱号
    @Excel(name = "车号")
    @Transient
    private String platNo;//车号
    @Excel(name = "入库出库")
    @Column(name = "INOUT_BOUND_FLAG")
    private String inOutBoundFlag;//入库出库
    @Excel(name = "搬运工姓名")
    private String stevedoreName;//搬运工姓名
    @Excel(name = "ASN")
    private String asnTransNum;//ASN
    @Excel(name = "理货者姓名")
    @Transient
    private String tallyName;//理货者姓名
    @Excel(name = "楼上叉车姓名")
    @Transient
    private String forkliftUpName;//楼上叉车姓名
    @Excel(name = "现场叉车姓名")
    @Transient
    private String forkliftSceneName;//现场叉车姓名
    @Excel(name = "装卸队名称")
    @Transient
    private String clientName;//装卸队名称
    @Excel(name = "创建时间")
    private Date createdTime;//创建时间
    @Excel(name = "参考重量系数")
    private String numPlus;// 参考重量系数
    @Transient
    private Double inWeight;
    @Transient
    private Double outWeight;

    @Transient
    private String  inProductName;

    @Transient
    private String  outProductName;

    //private String feePaid;//是否缴费，出库缴费或者入库缴费

    //@Id
    //@Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //@Column(name = "YYID")
    public String getYyid() {
        return yyid;
    }

    public void setYyid(String yyid) {
        this.yyid = yyid;
    }
    //@Column(name = "TALLY_ID")
    public String getTallyId() {
        return tallyId;
    }

    public void setTallyId(String tallyId) {
        this.tallyId = tallyId;
    }
    //@Column(name = "FORKLIFT_UP_ID")
    public String getForkliftUpId() {
        return forkliftUpId;
    }

    public void setForkliftUpId(String forkliftUpId) {
        this.forkliftUpId = forkliftUpId;
    }
    //@Column(name = "FORKLIFT_SCENE_ID")
    public String getForkliftSceneId() {
        return forkliftSceneId;
    }

    public void setForkliftSceneId(String forkliftSceneId) {
        this.forkliftSceneId = forkliftSceneId;
    }
    //@Column(name = "CLIENT_ID")
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    //@Column(name = "UPDATED_TIME")
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    //@Column(name = "DELETED_FLAG")
    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
    //@Column(name = "STEVEDORE_ID")
    public String getStevedoreId() {
        return stevedoreId;
    }

    public void setStevedoreId(String stevedoreId) {
        this.stevedoreId = stevedoreId;
    }
    //@Transient
    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }
    //@Transient
    public String getPlatNo() {
        return platNo;
    }

    public void setPlatNo(String platNo) {
        this.platNo = platNo;
    }
    //@Column(name = "INOUT_BOUND_FLAG")
    public String getInOutBoundFlag() {
        return inOutBoundFlag;
    }

    public void setInOutBoundFlag(String inOutBoundFlag) {
        this.inOutBoundFlag = inOutBoundFlag;
    }
    //@Column(name = "STEVEDORE_NAME")
    public String getStevedoreName() {
        return stevedoreName;
    }

    public void setStevedoreName(String stevedoreName) {
        this.stevedoreName = stevedoreName;
    }
    //@Column(name = "ASN_TRANS_NUM")
    public String getAsnTransNum() {
        return asnTransNum;
    }

    public void setAsnTransNum(String asnTransNum) {
        this.asnTransNum = asnTransNum;
    }
    //@Transient
    public String getTallyName() {
        return tallyName;
    }

    public void setTallyName(String tallyName) {
        this.tallyName = tallyName;
    }
    //@Transient
    public String getForkliftUpName() {
        return forkliftUpName;
    }

    public void setForkliftUpName(String forkliftUpName) {
        this.forkliftUpName = forkliftUpName;
    }
    //@Transient
    public String getForkliftSceneName() {
        return forkliftSceneName;
    }

    public void setForkliftSceneName(String forkliftSceneName) {
        this.forkliftSceneName = forkliftSceneName;
    }
    //@Transient
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    //@Column(name = "CREATED_TIME")
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    //@Column(name = "NUM_PLUS")
    public String getNumPlus() {
        return numPlus;
    }

    public void setNumPlus(String numPlus) {
        this.numPlus = numPlus;
    }

//    public String getFeePaid() {
//        return feePaid;
//    }
//
//    public void setFeePaid(String feePaid) {
//        this.feePaid = feePaid;
//    }


    public Double getInWeight() {
        return inWeight;
    }

    public void setInWeight(Double inWeight) {
        this.inWeight = inWeight;
    }

    public Double getOutWeight() {
        return outWeight;
    }

    public void setOutWeight(Double outWeight) {
        this.outWeight = outWeight;
    }

    public String getIfAllMan() {
        return ifAllMan;
    }

    public void setIfAllMan(String ifAllMan) {
        this.ifAllMan = ifAllMan;
    }

    public String getInProductName() {
        return inProductName;
    }

    public void setInProductName(String inProductName) {
        this.inProductName = inProductName;
    }

    public String getOutProductName() {
        return outProductName;
    }

    public void setOutProductName(String outProductName) {
        this.outProductName = outProductName;
    }
}
