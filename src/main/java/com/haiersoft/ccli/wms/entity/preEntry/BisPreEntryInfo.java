package com.haiersoft.ccli.wms.entity.preEntry;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 预报单货物明细
 */
@Entity
@Table(name = "BIS_PREENTRY_INFO")
public class BisPreEntryInfo implements java.io.Serializable {

	private static final long serialVersionUID = -4298965739518170761L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PREENTRY_INFO")
	@SequenceGenerator(name="SEQ_PREENTRY_INFO", sequenceName="SEQ_PREENTRY_INFO", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //主键id
	
	@Column(name = "FOR_ID")
	private String forId;  //预报单ID

	@Column(name = "CREATE_BY")
	private String createBy; //创建人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATE_TIME")
	private Date createTime;  //创建日期

	@Column(name = "UPDATE_BY")
	private String updateBy; //修改人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;  //修改日期

	@Column(name = "XH")
	private String xh;
	@Column(name = "BAXH")
	private String baxh;
	@Column(name = "SPLH")
	private String splh;
	@Column(name = "BGDSPXH")
	private String bgdspxh;
	@Column(name = "LZSBBXH")
	private String lzsbbxh;
	@Column(name = "SPXH")
	private String spxh;
	@Column(name = "SPMC")
	private String spmc;
	@Column(name = "GGXH")
	private String ggxh;
	@Column(name = "SBJLDW")
	private String sbjldw;
	@Column(name = "FDJLDW")
	private String fdjldw;
	@Column(name = "FDDEJLDW")
	private String fddejldw;
	@Column(name = "SBSL")
	private Double sbsl;
	@Column(name = "FDSL")
	private Double fdsl;
	@Column(name = "DEFDSL")
	private String defdsl;
	@Column(name = "QYSBDJ")
	private Double qysbdj;
	@Column(name = "QYSBZJ")
	private Double qysbzj;
	@Column(name = "MYTJZJE")
	private Double mytjzje;
	@Column(name = "YCG")
	private String ycg;
	@Column(name = "ZLBLYZ")
	private String zlblyz;
	@Column(name = "DYBLYZ")
	private String dyblyz;
	@Column(name = "DEBLYZ")
	private String deblyz;
	@Column(name = "MZ")
	private Double mz;
	@Column(name = "JZ")
	private Double jz;
	@Column(name = "YTDM")
	private String ytdm;
	@Column(name = "ZMFS")
	private String zmfs;
	@Column(name = "DHBBH")
	private String dhbbh;
	@Column(name = "ZZMDG")
	private String zzmdg;
	@Column(name = "XGBZ")
	private String xgbz;
	@Column(name = "WHPBZ")
	private String whpbz;
	@Column(name = "REMARK")
	private String remark;
	@Column(name = "BZT")
	private String bzt;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getForId() {
		return forId;
	}

	public void setForId(String forId) {
		this.forId = forId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getBaxh() {
		return baxh;
	}

	public void setBaxh(String baxh) {
		this.baxh = baxh;
	}

	public String getSplh() {
		return splh;
	}

	public void setSplh(String splh) {
		this.splh = splh;
	}

	public String getBgdspxh() {
		return bgdspxh;
	}

	public void setBgdspxh(String bgdspxh) {
		this.bgdspxh = bgdspxh;
	}

	public String getLzsbbxh() {
		return lzsbbxh;
	}

	public void setLzsbbxh(String lzsbbxh) {
		this.lzsbbxh = lzsbbxh;
	}

	public String getSpxh() {
		return spxh;
	}

	public void setSpxh(String spxh) {
		this.spxh = spxh;
	}

	public String getSpmc() {
		return spmc;
	}

	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}

	public String getGgxh() {
		return ggxh;
	}

	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}

	public String getSbjldw() {
		return sbjldw;
	}

	public void setSbjldw(String sbjldw) {
		this.sbjldw = sbjldw;
	}

	public String getFdjldw() {
		return fdjldw;
	}

	public void setFdjldw(String fdjldw) {
		this.fdjldw = fdjldw;
	}

	public String getFddejldw() {
		return fddejldw;
	}

	public void setFddejldw(String fddejldw) {
		this.fddejldw = fddejldw;
	}

	public Double getSbsl() {
		return sbsl;
	}

	public void setSbsl(Double sbsl) {
		this.sbsl = sbsl;
	}

	public Double getFdsl() {
		return fdsl;
	}

	public void setFdsl(Double fdsl) {
		this.fdsl = fdsl;
	}

	public String getDefdsl() {
		return defdsl;
	}

	public void setDefdsl(String defdsl) {
		this.defdsl = defdsl;
	}

	public Double getQysbdj() {
		return qysbdj;
	}

	public void setQysbdj(Double qysbdj) {
		this.qysbdj = qysbdj;
	}

	public Double getQysbzj() {
		return qysbzj;
	}

	public void setQysbzj(Double qysbzj) {
		this.qysbzj = qysbzj;
	}

	public Double getMytjzje() {
		return mytjzje;
	}

	public void setMytjzje(Double mytjzje) {
		this.mytjzje = mytjzje;
	}

	public String getYcg() {
		return ycg;
	}

	public void setYcg(String ycg) {
		this.ycg = ycg;
	}

	public String getZlblyz() {
		return zlblyz;
	}

	public void setZlblyz(String zlblyz) {
		this.zlblyz = zlblyz;
	}

	public String getDyblyz() {
		return dyblyz;
	}

	public void setDyblyz(String dyblyz) {
		this.dyblyz = dyblyz;
	}

	public String getDeblyz() {
		return deblyz;
	}

	public void setDeblyz(String deblyz) {
		this.deblyz = deblyz;
	}

	public Double getMz() {
		return mz;
	}

	public void setMz(Double mz) {
		this.mz = mz;
	}

	public Double getJz() {
		return jz;
	}

	public void setJz(Double jz) {
		this.jz = jz;
	}

	public String getYtdm() {
		return ytdm;
	}

	public void setYtdm(String ytdm) {
		this.ytdm = ytdm;
	}

	public String getZmfs() {
		return zmfs;
	}

	public void setZmfs(String zmfs) {
		this.zmfs = zmfs;
	}

	public String getDhbbh() {
		return dhbbh;
	}

	public void setDhbbh(String dhbbh) {
		this.dhbbh = dhbbh;
	}

	public String getZzmdg() {
		return zzmdg;
	}

	public void setZzmdg(String zzmdg) {
		this.zzmdg = zzmdg;
	}

	public String getXgbz() {
		return xgbz;
	}

	public void setXgbz(String xgbz) {
		this.xgbz = xgbz;
	}

	public String getWhpbz() {
		return whpbz;
	}

	public void setWhpbz(String whpbz) {
		this.whpbz = whpbz;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBzt() {
		return bzt;
	}

	public void setBzt(String bzt) {
		this.bzt = bzt;
	}
}