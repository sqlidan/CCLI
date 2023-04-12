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
 * @Description: 质押动态情况实体类
 */

public class ApiPledgeDynamicTransferVO implements Serializable{

    private static final long serialVersionUID = 705593496752901715L;
	
    String  traceId;
    String channel;
    String  requestTime;
    String  operationUserId;
    String chanType;
    String trendId;
    String lowAmount;
    String lowWeight;
    String order;
    String result;
    String reason;
    
    

	public String getLowAmount() {
		return lowAmount;
	}
	public void setLowAmount(String lowAmount) {
		this.lowAmount = lowAmount;
	}
	public String getLowWeight() {
		return lowWeight;
	}
	public void setLowWeight(String lowWeight) {
		this.lowWeight = lowWeight;
	}
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
		return "{  order=" + order + ", result="
				+ result + ", reason=" + reason + "}";
	}
	


    
	
}
