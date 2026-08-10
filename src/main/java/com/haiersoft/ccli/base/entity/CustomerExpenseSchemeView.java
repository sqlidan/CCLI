package com.haiersoft.ccli.base.entity;

import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 客户费用方案明细列表的费用方案和合同主表整合数据。
 */
public class CustomerExpenseSchemeView {

    @Excel(name = "方案编号")
    private String schemeNum;
    @Excel(name = "方案名称")
    private String schemeName;
    @Excel(name = "方案客户")
    private String customsName;
    @Excel(name = "合同号")
    private String contractNum;
    @Excel(name = "合同客户")
    private String contractClientName;
    @Excel(name = "合同状态", replace = {"未审核_0", "已审核_1"})
    private String contractState;
    @Excel(name = "签订时间", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date signTime;
    @Excel(name = "到期时间", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date expirationTime;
    @Excel(name = "揽货人")
    private String canvassionPerson;
    @Excel(name = "收付类型", replace = {"应收_1", "应付_2"})
    private String ifGet;
    @Excel(name = "方案状态", replace = {"未审核_0", "已审核_1"})
    private String programState;
    @Excel(name = "创建人")
    private String operatorPerson;
    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date operateTime;
    @Excel(name = "方案备注")
    private String schemeRemark;
    @Excel(name = "合同备注")
    private String contractRemark;

    public String getSchemeNum() { return schemeNum; }
    public void setSchemeNum(String schemeNum) { this.schemeNum = schemeNum; }
    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }
    public String getCustomsName() { return customsName; }
    public void setCustomsName(String customsName) { this.customsName = customsName; }
    public String getContractNum() { return contractNum; }
    public void setContractNum(String contractNum) { this.contractNum = contractNum; }
    public String getContractClientName() { return contractClientName; }
    public void setContractClientName(String contractClientName) { this.contractClientName = contractClientName; }
    public String getContractState() { return contractState; }
    public void setContractState(String contractState) { this.contractState = contractState; }
    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }
    public Date getExpirationTime() { return expirationTime; }
    public void setExpirationTime(Date expirationTime) { this.expirationTime = expirationTime; }
    public String getCanvassionPerson() { return canvassionPerson; }
    public void setCanvassionPerson(String canvassionPerson) { this.canvassionPerson = canvassionPerson; }
    public String getIfGet() { return ifGet; }
    public void setIfGet(String ifGet) { this.ifGet = ifGet; }
    public String getProgramState() { return programState; }
    public void setProgramState(String programState) { this.programState = programState; }
    public String getOperatorPerson() { return operatorPerson; }
    public void setOperatorPerson(String operatorPerson) { this.operatorPerson = operatorPerson; }
    public Date getOperateTime() { return operateTime; }
    public void setOperateTime(Date operateTime) { this.operateTime = operateTime; }
    public String getSchemeRemark() { return schemeRemark; }
    public void setSchemeRemark(String schemeRemark) { this.schemeRemark = schemeRemark; }
    public String getContractRemark() { return contractRemark; }
    public void setContractRemark(String contractRemark) { this.contractRemark = contractRemark; }
}
