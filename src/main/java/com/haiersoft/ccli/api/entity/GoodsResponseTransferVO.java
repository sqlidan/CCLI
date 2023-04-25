package com.haiersoft.ccli.api.entity;

import java.util.List;
import java.util.Map;

/**
 * 换货实体传输类
 * 
 * @author 吕术翰
 *
 */

public class GoodsResponseTransferVO {

	String traceId;
	String requestTime;
	String operationUserId;
	String channel;
	List<Map<String, String>> coldchainGoodsType;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public List<Map<String, String>> getColdchainGoodsType() {
		return coldchainGoodsType;
	}

	public void setColdchainGoodsType(List<Map<String, String>> coldchainGoodsType) {
		this.coldchainGoodsType = coldchainGoodsType;
	}

}
