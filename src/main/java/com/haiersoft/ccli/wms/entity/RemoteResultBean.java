package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;

/**
 * Created by galaxy on 2017/7/6.
 */
public class RemoteResultBean implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2174534087451715427L;
	private String state;
    private String msg;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "RemoteResultBean{" +
                "state='" + state + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

}
