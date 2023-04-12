package com.haiersoft.ccli.cost.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.BisVerifiBookInfoDao;
import com.haiersoft.ccli.cost.entity.BisVerifiBookInfo;

/**
 * 核销明细
 * @author LZG
 *
 */
@Service
public class BisVerifiBookInfoService extends BaseService<BisVerifiBookInfo, String> {

	@Autowired
	private  BisVerifiBookInfoDao bisVerifiBookInfoDao;//核销
	 
	
	@Override
	public HibernateDao<BisVerifiBookInfo, String> getEntityDao() {
		return bisVerifiBookInfoDao;
	}
	/***
	 * 根据核销编号获取核销明细对象
	 * @param num
	 * @return
	 */
	public List<BisVerifiBookInfo> getVerifiBookInfoList(String num){
		return bisVerifiBookInfoDao.getVerifiBookInfoList(num);
	} 
	
	/**
	 * 核销明细
	 * @param num 核销单编号
	 * @return
	 */
	public  Map<String, Object> getBisVerifiBookInfoList(String num,int nPage,int nPageSize){
		return bisVerifiBookInfoDao.getBisVerifiBookInfoList(num, nPage, nPageSize);
	}
}
