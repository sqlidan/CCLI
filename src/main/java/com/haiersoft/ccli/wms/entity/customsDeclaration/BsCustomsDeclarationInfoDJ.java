package com.haiersoft.ccli.wms.entity.customsDeclaration;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 报关单货物随附单据
 */
@Entity
@Table(name = "BS_CUSTOMS_DECLARATION_INFO_DJ")
public class BsCustomsDeclarationInfoDJ implements java.io.Serializable {

	private static final long serialVersionUID = -4298965739518170761L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUSTOMS_DECLARATION_DJ")
	@SequenceGenerator(name="SEQ_CUSTOMS_DECLARATION_DJ", sequenceName="SEQ_CUSTOMS_DECLARATION_DJ", allocationSize = 1)
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

	@Column(name = "REMARK")
	private String remark; //备注

	@Column(name = "FILENAME")
	private String fileName;

	@Column(name = "FILESIZE")
	private String fileSize;

	@Column(name = "FILECONTENT")
	private byte[] fileContent;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
}