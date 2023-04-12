package com.haiersoft.ccli.supervision.entity;

import java.awt.print.Book;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.helpers.AttributesImpl;

import antlr.collections.List;

public class CopBaseInfo implements Serializable {
	
    private static final long serialVersionUID = 2799111007612445959L;

	private String EmsNo;//账册号 非保不填，保税必填
	private String EmsSeqNo;//账册商品序号 保税必填
	private String WmsMtsNo;//企业库存物料号
	private String GoodsMtsNo;///料号
	private String CodeTs;//编码
	private String GoodsName;//品名
	private String GoodsModelDesc;//规格型号
	private String WmsDclUnit;//申报单位
	private String WmsDclQty;//申报数量
	private String WmsLawUnit;//法定单位
	private String WmsLawQty;//法定数量
	private String PlaceIds;//库区号多个用 | 分割
	private String LocationIds;//库位号 多个用 | 分割
	private String StockStatus;//出入库状态 0 预出入库 1在库
	private String GoodsType;//货物类型 0 非保 1 保税 

	public String getEmsNo() {
		return EmsNo;
	}
	public void setEmsNo(String emsNo) {
		EmsNo = emsNo;
	}
	public String getEmsSeqNo() {
		return EmsSeqNo;
	}
	public void setEmsSeqNo(String emsSeqNo) {
		EmsSeqNo = emsSeqNo;
	}
	public String getWmsMtsNo() {
		return WmsMtsNo;
	}
	public void setWmsMtsNo(String wmsMtsNo) {
		WmsMtsNo = wmsMtsNo;
	}
	public String getGoodsMtsNo() {
		return GoodsMtsNo;
	}
	public void setGoodsMtsNo(String goodsMtsNo) {
		GoodsMtsNo = goodsMtsNo;
	}
	public String getCodeTs() {
		return CodeTs;
	}
	public void setCodeTs(String codeTs) {
		CodeTs = codeTs;
	}
	public String getGoodsName() {
		return GoodsName;
	}
	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}
	public String getGoodsModelDesc() {
		return GoodsModelDesc;
	}
	public void setGoodsModelDesc(String goodsModelDesc) {
		GoodsModelDesc = goodsModelDesc;
	}
	public String getWmsDclUnit() {
		return WmsDclUnit;
	}
	public void setWmsDclUnit(String wmsDclUnit) {
		WmsDclUnit = wmsDclUnit;
	}
	public String getWmsDclQty() {
		return WmsDclQty;
	}
	public void setWmsDclQty(String wmsDclQty) {
		WmsDclQty = wmsDclQty;
	}
	public String getWmsLawUnit() {
		return WmsLawUnit;
	}
	public void setWmsLawUnit(String wmsLawUnit) {
		WmsLawUnit = wmsLawUnit;
	}
	public String getWmsLawQty() {
		return WmsLawQty;
	}
	public void setWmsLawQty(String wmsLawQty) {
		WmsLawQty = wmsLawQty;
	}
	public String getPlaceIds() {
		return PlaceIds;
	}
	public void setPlaceIds(String placeIds) {
		PlaceIds = placeIds;
	}
	public String getLocationIds() {
		return LocationIds;
	}
	public void setLocationIds(String locationIds) {
		LocationIds = locationIds;
	}
	public String getStockStatus() {
		return StockStatus;
	}
	public void setStockStatus(String stockStatus) {
		StockStatus = stockStatus;
	}
	public String getGoodsType() {
		return GoodsType;
	}
	public void setGoodsType(String goodsType) {
		GoodsType = goodsType;
	}

	
	
	
	
	
	
	
}
