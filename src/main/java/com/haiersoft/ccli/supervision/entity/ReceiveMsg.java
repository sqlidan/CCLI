package com.haiersoft.ccli.supervision.entity;

import java.io.Serializable;

public class ReceiveMsg implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4944305063391497416L;
	private String state;
	private String message;
	private String data;
	private String CheckInfos;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getCheckInfos() {
		return CheckInfos;
	}
	public void setCheckInfos(String checkInfos) {
		CheckInfos = checkInfos;
	}

}
