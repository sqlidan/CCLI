package com.haiersoft.ccli.base.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.dao.LoadingStrategyDao;
import com.haiersoft.ccli.base.entity.BaseLoadingSQL;
import com.haiersoft.ccli.base.entity.BaseLoadingStrategy;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;

@Service
public class LoadingStrategyService extends BaseService<BaseLoadingStrategy, Integer> {
	@Autowired
	private LoadingStrategyDao loadingStrategyDao;
	@Autowired
	private LoadingSQLService loadingSQLService;
	
	@Override
	public HibernateDao<BaseLoadingStrategy, Integer> getEntityDao() {
		return loadingStrategyDao;
	}
	
	
	
	/**
	 * 获取分拣策略列表
	 * @return
	 */
	public List<Map<String,Object>> findList(){
		return loadingStrategyDao.findList();
	}
	/**
	 * 根据策略编号获取策略详情
	 * @param strategyCode
	 * @return
	 */
	public List<BaseLoadingStrategy> findStrategyObjList(Integer strategyCode){
		return loadingStrategyDao.findStrategyObjList(strategyCode);
	}
    /**
     * 保存拣货策略
     * @param strategy
     * @return
     */
	public Map<String,Object> saveStrategy(BaseLoadingStrategy strategy){
		Map<String,Object> retMap=new HashMap<String,Object>();
		retMap.put("retStr", "error");
		if(strategy!=null){
			StringBuffer sqlIf=new StringBuffer(" ");
			StringBuffer sqlOdr=new StringBuffer(" ");
			 int intoID=0;
			if(strategy.getStrategyCode()!=null && strategy.getStrategyCode()>0 ){
				//执行修改操作,删除之前详情，重新添加
				intoID=strategy.getStrategyCode();
				loadingStrategyDao.deleteStrategy(intoID);
				loadingSQLService.deleteStrategy(intoID);
			}else if(strategy.getStrategyCode()!=null && strategy.getStrategyCode()<=0){
				retMap.put("retStr", "success");
				retMap.put("id", 0);
				return retMap;
			}else{
				 intoID=loadingStrategyDao.getSequenceId("SEQ_LOADING_STRATEGY");
			} 
			//执行添加操作
			 String[] ifsx=strategy.getIfsx() ;//属性
			 String[] ifbds=strategy.getIfbds();//表达式
			 String[] ifval=strategy.getIfval();//值
			 String[] iflink=strategy.getIflink();//条件连接
			 String[] ordsx=strategy.getOrdsx();//排序属性
			 String[] ordpx=strategy.getOrdpx();//排序
			 BaseLoadingStrategy newObj=null;
			
			 if(ifsx!=null && ifsx.length>0){
				 for(int i=0;i<ifsx.length;i++){
					 if(!"".equals(ifsx[i]) && !"".equals(ifbds[i]) && !"".equals(ifval[i])){
						 newObj=new BaseLoadingStrategy();
						 newObj.setStrategyName(strategy.getStrategyName());
						 newObj.setStrategyCode(intoID);
						 newObj.setIfField(ifsx[i]);
						 
						 if("AREA_NUM".equals(ifsx[i])){
							 sqlIf.append("substr(AREA_NUM,1,2)").append(" ");
							 newObj.setIfType(ifbds[i]);
							 sqlIf.append(changVal(ifbds[i])).append(" ");
							 newObj.setIfVal(ifval[i]);
							 sqlIf.append("'").append(StringUtils.lpadStringLeft(2, ifval[i])).append("' ");
							 
						 }else if("AREA_NUM2".equals(ifsx[i])){
							 sqlIf.append("substr(AREA_NUM,3,2)").append(" ");
							 newObj.setIfType(ifbds[i]);
							 sqlIf.append(changVal(ifbds[i])).append(" ");
							 newObj.setIfVal(ifval[i]);
							 sqlIf.append("'").append(StringUtils.lpadStringLeft(2, ifval[i])).append("' ");
							 
							 
						 }else if("AREA_NUM3".equals(ifsx[i])){
							 sqlIf.append("substr(AREA_NUM,5,2)").append(" ");
							 newObj.setIfType(ifbds[i]);
							 sqlIf.append(changVal(ifbds[i])).append(" ");
							 newObj.setIfVal(ifval[i]);
							 sqlIf.append("'").append(StringUtils.lpadStringLeft(2, ifval[i])).append("' ");
							 
							 
						 }else{
							 sqlIf.append(ifsx[i]).append(" ");
							 newObj.setIfType(ifbds[i]);
							 sqlIf.append(changVal(ifbds[i])).append(" ");
							 newObj.setIfVal(ifval[i]);
							 sqlIf.append("'").append(ifval[i]).append("' ");
							 
						 }
						 
						 /*sqlIf.append(ifsx[i]).append(" ");
						 newObj.setIfType(ifbds[i]);
						 sqlIf.append(changVal(ifbds[i])).append(" ");
						 newObj.setIfVal(ifval[i]);
						 if("AREA_NUM".equals(ifsx[i])){
							 sqlIf.append("'").append(StringUtils.lpadStringLeft(2, ifval[i])).append("' ");
						 }else{
							 sqlIf.append("'").append(ifval[i]).append("' "); 
						 }*/
						 
						 if(i< (ifsx.length-1)){
							 newObj.setIfLink(iflink[i]);
							 sqlIf.append(iflink[i]).append(" ");
						 }
						 this.save(newObj);
					 }
					 
				 }//end if for
				 
				 if(ordsx!=null && ordsx.length>0){
					 for(int i=0;i<ordsx.length;i++){
						 if(!"".equals(ordsx[i]) && !"".equals(ordpx[i])){
							 newObj=new BaseLoadingStrategy();
							 newObj.setStrategyName(strategy.getStrategyName());
							 newObj.setStrategyCode(intoID);
							 
							 if("AREA_NUM".equals(ordsx[i])){
								 newObj.setOrdField("substr(AREA_NUM,1,2)");
								 sqlOdr.append("substr(AREA_NUM,1,2)").append(" ");
								 
							 }else if("AREA_NUM2".equals(ordsx[i])){
								 newObj.setOrdField("substr(AREA_NUM,3,2)");
								 sqlOdr.append("substr(AREA_NUM,1,2)").append(" ");
								 
							 }else if("AREA_NUM3".equals(ordsx[i])){
								 newObj.setOrdField("substr(AREA_NUM,5,2)");
								 sqlOdr.append("substr(AREA_NUM,1,2)").append(" ");
								 
							 }else{
								 newObj.setOrdField(ordsx[i]);
								 sqlOdr.append(ordsx[i]).append(" ");
							 }
							 
							
							 
							 newObj.setOrdSort(ordpx[i]);
							 sqlOdr.append(ordpx[i]);
							 if(i< (ordsx.length-1)){
								 sqlOdr.append(",");
							 }
							 this.save(newObj);
						 }
					 }
				 }//end order
			 }//end if
			 if(intoID>0){
				 BaseLoadingSQL loadingSQL=new BaseLoadingSQL();
				 loadingSQL.setId(intoID);
				 loadingSQL.setIfStr(sqlIf.toString());
				 loadingSQL.setOrdStr(sqlOdr.toString());
				 loadingSQLService.save(loadingSQL);
			 }
			 retMap.put("retStr", "success");
			 retMap.put("id", intoID);
		}// end 添加
		return retMap;
	}
	private String changVal(String bds){
		if("&lt;".equals(bds)){
			return "<";
		}else if("&gt;".equals(bds)){
			return ">";
		}else if("&lt;=".equals(bds)){
			return "<=";
		}else if("&gt;=".equals(bds)){
			return ">=";
		} 
		return bds;
	}
	
	/**
	 * 根据策略编码删除策略详情
	 * @param strategyCode 策略编码
	 * @return
	 */
	public Map<String,Object> deleteStrategy(Integer strategyCode){
		Map<String,Object> retMap=new HashMap<String,Object>();
		retMap.put("retStr", "error");
		if(strategyCode!=null && strategyCode>0){
			loadingStrategyDao.deleteStrategy(strategyCode);
			loadingSQLService.deleteStrategy(strategyCode);
			retMap.put("retStr", "success");
			retMap.put("id", strategyCode);
		}
		return retMap;
	}
}
