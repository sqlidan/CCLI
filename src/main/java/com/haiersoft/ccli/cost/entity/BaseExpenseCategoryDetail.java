package com.haiersoft.ccli.cost.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 中台费目
 *  @author MyEclipse Persistence Tools
 *
 */

@Entity
@Table(name = "BASE_EXPENSE_CATEGORY_DETAIL")
public class BaseExpenseCategoryDetail {
    @Column(name = "DETAIL_ID")
    private  String  detailId;
    @Column(name = "DETAIL_CODE")
    private String detailCode;
    @Column(name = "DETAIL_CODE_NAME")
    private String detailCodeName;
    @Column(name = "FEE_ID")
    private String feeId;
    @Id
    @Column(name = "DETAIL_ID", unique = true, nullable = false)
    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(String detailCode) {
        this.detailCode = detailCode;
    }

    public String getDetailCodeName() {
        return detailCodeName;
    }

    public void setDetailCodeName(String detailCodeName) {
        this.detailCodeName = detailCodeName;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public BaseExpenseCategoryDetail() {
    }

    public BaseExpenseCategoryDetail(String detailId, String detailCode, String detailCodeName, String feeId) {
        this.detailId = detailId;
        this.detailCode = detailCode;
        this.detailCodeName = detailCodeName;
        this.feeId = feeId;
    }

    @Override
    public String toString() {
        return "BaseExpenseCategoryDetail{" +
                "detailId='" + detailId + '\'' +
                ", detailCode='" + detailCode + '\'' +
                ", detailCodeName='" + detailCodeName + '\'' +
                ", feeId='" + feeId + '\'' +
                '}';
    }
}
