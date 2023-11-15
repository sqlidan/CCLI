package com.haiersoft.ccli.wms.entity.customsDeclaration;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 报关单货物明细
 */
@Entity
@Table(name = "BS_CUSTOMS_DECLARATION_INFO")
public class BsCustomsDeclarationInfo implements java.io.Serializable {

	private static final long serialVersionUID = -4298965739518170761L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUSTOMS_DECLARATION_INFO")
	@SequenceGenerator(name="SEQ_CUSTOMS_DECLARATION_INFO", sequenceName="SEQ_CUSTOMS_DECLARATION_INFO", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id; //主键id
	
	@Column(name = "FOR_ID")
	private String forId;  //报关单ID

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
	@Column(name = "ACCOUNT_BOOK")
	private String accountBook;//账册商品序号
	@Column(name = "SPBH")
	private String spbh;
	@Column(name = "SPMC")
	private String spmc;
	@Column(name = "GGXH")
	private String ggxh;
	@Column(name = "SBSL")
	private Double sbsl;
	@Column(name = "SBJLDW")
	private String sbjldw;
	@Column(name = "QYSBDJ")
	private Double qysbdj;
	@Column(name = "MYTJZJE")
	private Double mytjzje;
	@Column(name = "BZT")
	private String bzt;
	@Column(name = "YCG")
	private String ycg;
	@Column(name = "ZZMDG")
	private String zzmdg;
	@Column(name = "JNMDD")
	private String jnmdd;
	@Column(name = "ZMFS")
	private String zmfs;
	@Column(name = "REMARK")
	private String remark;

	public String getAccountBook() {
		return accountBook;
	}

	public void setAccountBook(String accountBook) {
		this.accountBook = accountBook;
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

	public String getSpbh() {
		return spbh;
	}

	public void setSpbh(String spbh) {
		this.spbh = spbh;
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

	public Double getSbsl() {
		return sbsl;
	}

	public void setSbsl(Double sbsl) {
		this.sbsl = sbsl;
	}

	public String getSbjldw() {
		return sbjldw;
	}

	public void setSbjldw(String sbjldw) {
		this.sbjldw = sbjldw;
	}

	public Double getQysbdj() {
		return qysbdj;
	}

	public void setQysbdj(Double qysbdj) {
		this.qysbdj = qysbdj;
	}

	public Double getMytjzje() {
		return mytjzje;
	}

	public void setMytjzje(Double mytjzje) {
		this.mytjzje = mytjzje;
	}

	public String getBzt() {
		return bzt;
	}

	public void setBzt(String bzt) {
		this.bzt = bzt;
	}

	public String getYcg() {
		return ycg;
	}

	public void setYcg(String ycg) {
		this.ycg = ycg;
	}

	public String getZzmdg() {
		return zzmdg;
	}

	public void setZzmdg(String zzmdg) {
		this.zzmdg = zzmdg;
	}

	public String getJnmdd() {
		return jnmdd;
	}

	public void setJnmdd(String jnmdd) {
		this.jnmdd = jnmdd;
	}

	public String getZmfs() {
		return zmfs;
	}

	public void setZmfs(String zmfs) {
		this.zmfs = zmfs;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}