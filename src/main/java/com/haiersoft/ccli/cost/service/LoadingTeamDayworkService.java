package com.haiersoft.ccli.cost.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.cost.dao.LoadingTeamDayworkDao;
import com.haiersoft.ccli.cost.entity.LoadingTeamDaywork;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
 
/**
 * 装卸队日工统计Service
 * @author mzy
 * @date 2016年6月29日
 */

@Service
public class LoadingTeamDayworkService extends BaseService<LoadingTeamDaywork, String> {
	@Autowired
	private LoadingTeamDayworkDao loadingTeamDayworkDao;
	@Override
	public HibernateDao<LoadingTeamDaywork, String> getEntityDao() {
		return loadingTeamDayworkDao;
	}
	/**
	 * 生成日工ID：S+YYMMDD+SEQ(5位循环) 
	 * @param gClass 货物类型
	 * @return
	 */
	public String  getDayworkId() {
		StringBuffer retID=new StringBuffer("D");
		retID.append(DateUtils.getDateStr(new Date(),"YYMMdd"));
		int getNum=loadingTeamDayworkDao.getSequenceId("SEQ_LOADING_TEAM_DAYWORK");
		retID.append(StringUtils.lpadInt(3,getNum ));
		return retID.toString();
	}
	
	/**
	 * 按唯字段id查询对象信息
	 * @param queryStr 查询内容
	 * @return 客户对象
	 */
	public LoadingTeamDaywork getDayworkInfo(String id) {
		LoadingTeamDaywork getObj=null;
		if(!"".equals(id) && !"".equals(id)){
			getObj=loadingTeamDayworkDao.findUniqueBy("id", id);
		}
		return getObj;
	}
	
	/*
	 *  根据id查询信息
	 */
	public List<LoadingTeamDaywork> getDayworkById(String id) {
		return loadingTeamDayworkDao.findBy("id", id);
	}
	
	/*
	 *  回写台账ID
	 */
	public void updateTZID(String id,Integer standingNum) {
		loadingTeamDayworkDao.updateTZID(id,standingNum);
	}
	
	/*
	 *  更新完结状态
	 */
	public void updateState(String id) {
		loadingTeamDayworkDao.updateState(id);
	}
	
	/*
	 *  取消完结状态
	 */
	public void updateState2(String id) {
		loadingTeamDayworkDao.updateState2(id);
	}

}
