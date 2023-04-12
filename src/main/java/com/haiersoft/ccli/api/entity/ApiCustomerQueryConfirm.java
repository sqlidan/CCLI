package com.haiersoft.ccli.api.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 
 * @author
 * @ClassName:
 * @Description:
 * @date
 */
@Entity
@Table(name = "API_CUSTOMER_QUERY_CONFIRM")
@DynamicUpdate
@DynamicInsert
public class ApiCustomerQueryConfirm implements Serializable {


    // 主键id
	@Id
    @GeneratedValue(generator = "apiCustomerQueryConfirm")
    @GenericGenerator(name = "apiCustomerQueryConfirm", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false)
	private String id;

    //
    @Column(name = "APPLY_ID")
    private String applyId;

    //
    @Column(name = "CLIENT_ID")
    private Integer clientId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}
