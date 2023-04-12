package com.haiersoft.ccli.outsidequery.entity;

import java.util.List;

/**
 * 在库信息返回,增加总件数
 * @author 
 *
 */
//@Entity
public class OutsideInvQuery {


	private Integer totalPieces; //总件数
	
	private List<OutsideQuery> data;

	public Integer getTotalPieces() {
		return totalPieces;
	}

	public void setTotalPieces(Integer totalPieces) {
		this.totalPieces = totalPieces;
	}

	public List<OutsideQuery> getData() {
		return data;
	}

	public void setData(List<OutsideQuery> data) {
		this.data = data;
	}


}
