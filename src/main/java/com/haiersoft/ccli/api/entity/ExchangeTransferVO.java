package com.haiersoft.ccli.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 换货实体传输类
 * @author 吕术翰
 *
 */

public class ExchangeTransferVO {

	String traceId;
	String channel;
	String operationUserId;
	String requestTime;
	String chanType;
	String trendId;
	String order;
	String sourceTrendId;
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
	public String getOperationUserId() {
		return operationUserId;
	}
	public void setOperationUserId(String operationUserId) {
		this.operationUserId = operationUserId;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
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
	public String getSourceTrendId() {
		return sourceTrendId;
	}
	public void setSourceTrendId(String sourceTrendId) {
		this.sourceTrendId = sourceTrendId;
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
	
	
}
