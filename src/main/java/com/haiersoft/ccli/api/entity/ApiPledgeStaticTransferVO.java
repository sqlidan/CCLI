package com.haiersoft.ccli.api.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author 吕术翰
 * @Description: 质押情况实体类
 */

public class ApiPledgeStaticTransferVO implements Serializable {

	private static final long serialVersionUID = 705593496752901715L;

	String traceId;
	String channel;
	String requestTime;
	String operationUserId;
	String chanType;
	String trendId;
	String userNo;
	String userName;
	Integer watchAmount;
	Double watchWeight;
	String order;
	String result;
	String reason;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getOperationUserId() {
		return operationUserId;
	}

	public void setOperationUserId(String operationUserId) {
		this.operationUserId = operationUserId;
	}

	public String getChanType() {
		return chanType;
	}

	public void setChanType(String chanType) {
		this.chanType = chanType;
	}

	public String getTrendId() {
		return trendId;
	}

	public void setTrendId(String trendId) {
		this.trendId = trendId;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getWatchAmount() {
		return watchAmount;
	}

	public void setWatchAmount(Integer watchAmount) {
		this.watchAmount = watchAmount;
	}

	public Double getWatchWeight() {
		return watchWeight;
	}

	public void setWatchWeight(Double watchWeight) {
		this.watchWeight = watchWeight;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "{ userNo=" + userNo + ", userName=" + userName + ", watchAmount=" + watchAmount + ", watchWeight="
				+ watchWeight + ", order=" + order + ", result=" + result + ", reason=" + reason + "}";
	}

}
