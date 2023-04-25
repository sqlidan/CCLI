package com.haiersoft.ccli.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 
 * @author Connor.M
 * @ClassName: Warehouse
 * @Description: 仓库实体类
 * @date 2016年3月3日 下午3:37:22
 */
@Entity
@Table(name = "BASE_WAREHOUSE")
@DynamicUpdate
@DynamicInsert
public class Warehouse implements Serializable{

    private static final long serialVersionUID = 5923615904048549472L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WAREHOUSE")
	@SequenceGenerator(name = "SEQ_WAREHOUSE", sequenceName = "SEQ_WAREHOUSE", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false)
    private String id;//仓库Id
    
    @Column(name = "WAREHOUSE_CODE")
    private String warehouseCode;//仓库编号
    
    @Column(name = "WAREHOUSE_NAME")
    private String warehouseName;//仓库名称

    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
    
}
