package com.haiersoft.ccli.wms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Connor.M
 * @ClassName: BisDismantleTray
 * @Description: 拆托信息表  实体类
 * @date 2016年3月17日 下午3:35:36
 */
@Entity
@Table(name = "BIS_DISMANTLE_TRAY")
@DynamicUpdate
@DynamicInsert
public class BisDismantleTray implements Serializable{

    private static final long serialVersionUID = 6164696883206685797L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DISMANTLE_TRAY")
	@SequenceGenerator(name = "SEQ_DISMANTLE_TRAY", sequenceName = "SEQ_DISMANTLE_TRAY", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
	private Integer id;//主键
	
    @Column(name = "OLD_TRAY_CODE")
	private String oldTrayCode;//老托盘号
	
    @Column(name = "NEW_TRAY_CODE")
	private String newTrayCode;//新托盘号
	
    @Column(name = "NUM")
	private Integer num;//拆托数量
	
    @Column(name = "DISMANTLE_TYPE")
	private String dismantleType;//拆托类型：1：web拆托，2：出库拣货拆托，3：货转拆托，4：装车回库拆托
	
    @Column(name = "DISMANTLE_USER")
	private String dismantleUser;//拆托人
    
	@Column(name = "DISMANTLE_TIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date dismantleTime;//拆托时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOldTrayCode() {
		return oldTrayCode;
	}

	public void setOldTrayCode(String oldTrayCode) {
		this.oldTrayCode = oldTrayCode;
	}

	public String getNewTrayCode() {
		return newTrayCode;
	}

	public void setNewTrayCode(String newTrayCode) {
		this.newTrayCode = newTrayCode;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getDismantleType() {
		return dismantleType;
	}

	public void setDismantleType(String dismantleType) {
		this.dismantleType = dismantleType;
	}

	public String getDismantleUser() {
		return dismantleUser;
	}

	public void setDismantleUser(String dismantleUser) {
		this.dismantleUser = dismantleUser;
	}

	public Date getDismantleTime() {
		return dismantleTime;
	}

	public void setDismantleTime(Date dismantleTime) {
		this.dismantleTime = dismantleTime;
	}
	
}
