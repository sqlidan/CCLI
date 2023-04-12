package com.haiersoft.ccli.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 库管理
 * @author
 *
 */
@Entity
@Table(name = "Base_Tray_Room")
public class BaseTrayRoom implements java.io.Serializable{
	/**
	 * 
	 */

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer Id; //编号
	@Column(name = "ROOM_NUM")
	private String RoomNum;//库号
	@Column(name = "SORT")
	private String sort;//排序


	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getRoomNum() {
		return RoomNum;
	}

	public void setRoomNum(String roomNum) {
		RoomNum = roomNum;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
