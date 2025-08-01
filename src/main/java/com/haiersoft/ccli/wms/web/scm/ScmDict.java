package com.haiersoft.ccli.wms.web.scm;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 同步平台字段数据 实体类
 */
@Entity
@Table(name = "BASE_SCM_DICT")
public class ScmDict implements Serializable {

	private static final long serialVersionUID = 8873633686138049500L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SCM_DICT")
	@SequenceGenerator(name="SEQ_SCM_DICT", sequenceName="SEQ_SCM_DICT", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	@Column(name = "DICT_TYPE")
    private String dictType;//字典类型编码

	@Column(name = "DICT_TYPE_NAME")
    private String dictName;//字典类型名称

	@Column(name = "DICT_LABEL")
    private String dictLabel;//字典项标签

	@Column(name = "DICT_VALUE")
    private String dictValue;//字典项编码

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictTypeName) {
		this.dictName = dictTypeName;
	}

	public String getDictLabel() {
		return dictLabel;
	}

	public void setDictLabel(String dictLabel) {
		this.dictLabel = dictLabel;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}
}
