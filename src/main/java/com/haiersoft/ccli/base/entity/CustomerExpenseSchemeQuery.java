package com.haiersoft.ccli.base.entity;

/**
 * 客户费用方案明细页面的查询条件。
 */
public class CustomerExpenseSchemeQuery {

    private String schemeNum;
    private String schemeName;
    private String contractNum;
    private String customerName;
    private String ifGet;
    private String programState;
    private String contractState;

    public String getSchemeNum() {
        return schemeNum;
    }

    public void setSchemeNum(String schemeNum) {
        this.schemeNum = schemeNum;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIfGet() {
        return ifGet;
    }

    public void setIfGet(String ifGet) {
        this.ifGet = ifGet;
    }

    public String getProgramState() {
        return programState;
    }

    public void setProgramState(String programState) {
        this.programState = programState;
    }

    public String getContractState() {
        return contractState;
    }

    public void setContractState(String contractState) {
        this.contractState = contractState;
    }
}
