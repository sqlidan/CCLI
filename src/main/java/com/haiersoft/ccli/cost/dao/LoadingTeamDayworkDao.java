package com.haiersoft.ccli.cost.dao;

import java.util.HashMap;

import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.cost.entity.LoadingTeamDaywork;
import com.haiersoft.ccli.common.persistence.HibernateDao;

/**
 * 装卸队日工统计DAO
 * @author mzy
 * @date 2016年6月29日
 */

@Repository
public class LoadingTeamDayworkDao  extends HibernateDao<LoadingTeamDaywork, String> {
	
	/*
	 * 更新 是否完成 状态
	 */
	public void updateState(String id) {
		if(id!=null && !"".equals(id)){
			String sql="update bis_loading_team_daywork  set state = '1' where id = :id";
			Map<String,Object> parme=new HashMap<String,Object>();
			parme.put("id", id);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}
	
	/*
	 * 台账ID回写
	 */
	public void updateTZID(String id,Integer standing_num) {
		if(id!=null && !"".equals(id)){
			String sql="update bis_loading_team_daywork  set standing_num = :standing_num where id = :id";
			Map<String,Object> parme=new HashMap<String,Object>();
			parme.put("id", id);
			parme.put("standing_num", standing_num);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}
	
	/*
	 * 取消 完成 状态
	 */
	public void updateState2(String id) {
		if(id!=null && !"".equals(id)){
			String sql="update bis_loading_team_daywork  set state = '0',STANDING_NUM='' where id = :id";
			Map<String,Object> parme=new HashMap<String,Object>();
			parme.put("id", id);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}
	
}
