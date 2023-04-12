package com.haiersoft.ccli.base.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 库位基础表
 * @author slh
 *
 */
@Entity
@Table(name = "Base_Tray")
public class BaseTray implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4368159080367328989L;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private String Id; //编号
	@Column(name = "GROUP_ID")
	private String GroupId; //库位号
	@Column(name = "WAREHOUSE_ID")
	private String WarehouseId; //仓库id
	@Column(name = "WAREHOUSE")
	private String Warhouse; //仓库名称
	@Column(name = "BUILDING_NUM")
	private String BuildingNum;  //楼号
	@Column(name = "FLOOR_NUM")
	private String FloorNum; //楼层号
	@Column(name = "ROOM_NUM")
	private String RoomNum;//房间号 
	@Column(name = "AREA_NUM")
	private Date AreaNum;//区位号 
	@Column(name = "STOREROOM_NUM")
	private String StoreRoomNum;//库房号
	@Column(name = "ISEFFECT")
	private Integer IsEffect;//库位是否有效（0有效1无效）
	@Column(name = "REMARK")
	private String Remark;// 备注 
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "CREATEDATE")
	private Date CreateDate;// 创建时间
	@Column(name = "CREATEPER")
	private String CreatePer;// 创建人
	@Column(name = "FIELD_1")
	private String Field1;//备用字段
	
	
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getGroupId() {
		return GroupId;
	}
	public void setGroupId(String groupId) {
		GroupId = groupId;
	}
	public String getWarehouseId() {
		return WarehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		WarehouseId = warehouseId;
	}
	public String getWarhouse() {
		return Warhouse;
	}
	public void setWarhouse(String warhouse) {
		Warhouse = warhouse;
	}
	public String getBuildingNum() {
		return BuildingNum;
	}
	public void setBuildingNum(String buildingNum) {
		BuildingNum = buildingNum;
	}
	public String getFloorNum() {
		return FloorNum;
	}
	public void setFloorNum(String floorNum) {
		FloorNum = floorNum;
	}
	public String getRoomNum() {
		return RoomNum;
	}
	public void setRoomNum(String roomNum) {
		RoomNum = roomNum;
	}
	public Date getAreaNum() {
		return AreaNum;
	}
	public void setAreaNum(Date areaNum) {
		AreaNum = areaNum;
	}
	public String getStoreRoomNum() {
		return StoreRoomNum;
	}
	public void setStoreRoomNum(String storeRoomNum) {
		StoreRoomNum = storeRoomNum;
	}
	public Integer getIsEffect() {
		return IsEffect;
	}
	public void setIsEffect(Integer isEffect) {
		IsEffect = isEffect;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public Date getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
	}
	public String getCreatePer() {
		return CreatePer;
	}
	public void setCreatePer(String createPer) {
		CreatePer = createPer;
	}
	public String getField1() {
		return Field1;
	}
	public void setField1(String field1) {
		Field1 = field1;
	}
 
}
