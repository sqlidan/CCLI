package com.haiersoft.ccli.wms.entity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
/**
 * 
 * @ClassName: BIS_CALL_CAR
 */
@Entity
@Table(name = "BIS_CALL_CAR")
@DynamicUpdate
@DynamicInsert
public class CallCar implements Serializable {

    private static final long serialVersionUID = 4151326454264563385L;
    
    @Id
    @Column(name = "CAR_NUM", unique = true, nullable = false)
    private String carNum;//车牌号
    
    @Column(name = "PLATFORM")
    private String platform;//月台口
    
    @Column(name = "TIME")
    private Integer time;//呼叫剩余次数

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
    
    public CallCar(){
    
    }
    
    public CallCar(String carNum,String platform,Integer time){
    	this.carNum=carNum;
    	this.platform=platform;
    	this.time=time;
    }
    
    
}
