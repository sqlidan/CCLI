package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
/**
 * 
 */

public class CountTemplete implements Serializable {
	
    private static final long serialVersionUID =1L;
    
    private String BILL_NUM;
    private String STOCK_NAME;
    private String CTN_NUM;
    private String starTime;
    private String endTime;
    private String date;///////
    private String rukuhuozhu;
    private String tidanhao;
    private String jizhuangxianghao;
    private String sku;
    private String huowumingcheng;
    private String zhonglei;
    private String churu;
    private String yuefen;
    private String shijian;
    private String shuliang;
    private String danjing;
    private String danmao;
    private String zongjing;
    private String zongmao ;
    private String shouhuokehu;
	public String getBILL_NUM() {
		return BILL_NUM;
	}
	public void setBILL_NUM(String bILL_NUM) {
		BILL_NUM = bILL_NUM;
	}
	public String getSTOCK_NAME() {
		return STOCK_NAME;
	}
	public void setSTOCK_NAME(String sTOCK_NAME) {
		STOCK_NAME = sTOCK_NAME;
	}
	public String getCTN_NUM() {
		return CTN_NUM;
	}
	public void setCTN_NUM(String cTN_NUM) {
		CTN_NUM = cTN_NUM;
	}
	public String getStarTime() {
		return starTime;
	}
	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRukuhuozhu() {
		return rukuhuozhu;
	}
	public void setRukuhuozhu(String rukuhuozhu) {
		this.rukuhuozhu = rukuhuozhu;
	}
	public String getTidanhao() {
		return tidanhao;
	}
	public void setTidanhao(String tidanhao) {
		this.tidanhao = tidanhao;
	}
	public String getJizhuangxianghao() {
		return jizhuangxianghao;
	}
	public void setJizhuangxianghao(String jizhuangxianghao) {
		this.jizhuangxianghao = jizhuangxianghao;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getHuowumingcheng() {
		return huowumingcheng;
	}
	public void setHuowumingcheng(String huowumingcheng) {
		this.huowumingcheng = huowumingcheng;
	}
	public String getZhonglei() {
		return zhonglei;
	}
	public void setZhonglei(String zhonglei) {
		this.zhonglei = zhonglei;
	}
	public String getChuru() {
		return churu;
	}
	public void setChuru(String churu) {
		this.churu = churu;
	}
	public String getYuefen() {
		return yuefen;
	}
	public void setYuefen(String yuefen) {
		this.yuefen = yuefen;
	}
	public String getShijian() {
		return shijian;
	}
	public void setShijian(String shijian) {
		this.shijian = shijian;
	}
	public String getShuliang() {
		return shuliang;
	}
	public void setShuliang(String shuliang) {
		this.shuliang = shuliang;
	}
	public String getDanjing() {
		return danjing;
	}
	public void setDanjing(String danjing) {
		this.danjing = danjing;
	}
	public String getDanmao() {
		return danmao;
	}
	public void setDanmao(String danmao) {
		this.danmao = danmao;
	}
	public String getZongjing() {
		return zongjing;
	}
	public void setZongjing(String zongjing) {
		this.zongjing = zongjing;
	}
	public String getZongmao() {
		return zongmao;
	}
	public void setZongmao(String zongmao) {
		this.zongmao = zongmao;
	}
	public String getShouhuokehu() {
		return shouhuokehu;
	}
	public void setShouhuokehu(String shouhuokehu) {
		this.shouhuokehu = shouhuokehu;
	}
	@Override
	public String toString() {
		return "CountTemplete [BILL_NUM=" + BILL_NUM + ", STOCK_NAME=" + STOCK_NAME + ", CTN_NUM=" + CTN_NUM
				+ ", starTime=" + starTime + ", endTime=" + endTime + ", date=" + date + ", rukuhuozhu=" + rukuhuozhu
				+ ", tidanhao=" + tidanhao + ", jizhuangxianghao=" + jizhuangxianghao + ", sku=" + sku
				+ ", huowumingcheng=" + huowumingcheng + ", zhonglei=" + zhonglei + ", churu=" + churu + ", yuefen="
				+ yuefen + ", shijian=" + shijian + ", shuliang=" + shuliang + ", danjing=" + danjing + ", danmao="
				+ danmao + ", zongjing=" + zongjing + ", zongmao=" + zongmao + ", shouhuokehu=" + shouhuokehu + "]";
	}
	
}