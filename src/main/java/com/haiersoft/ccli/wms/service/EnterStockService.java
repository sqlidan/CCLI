package com.haiersoft.ccli.wms.service;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.EnterStockDao;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 
 * @author pyl
 * @ClassName: EnterStockService
 * @Description: 入库联系单Service
 * @date 2016年2月24日 下午3:52:37
 */
@Service
@Transactional(readOnly = true)
public class EnterStockService extends BaseService<BisEnterStock, String> {
	
	@Autowired
	private EnterStockDao enterStockDao;
	
	@Override
	public HibernateDao<BisEnterStock, String> getEntityDao() {
		return enterStockDao;
	}
	
	public Page<BisEnterStock> getEnterStocks(Page<BisEnterStock> page,BisEnterStock obj) {
		 return enterStockDao.getEnterStocks(page,obj);
	}
	public BisEnterStock exportCheckFeeData(String linkId) {
		return enterStockDao.exportCheckFeeData(linkId);
	}
	/**
	 * 按条件查询入库联系单和货转联系单集合
	 * @param page
	 * @param billNum //提单id
	 * @param transNum//装车单号
	 * @param ctnNum //厢号
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public  List<Map<String,Object>>  findList(Page page,String linkNum,String billNum,String transNum,String ctnNum){
		return enterStockDao.findList(page,linkNum, billNum, transNum, ctnNum);
	}
	
	@SuppressWarnings("rawtypes")
	public  List<Map<String,Object>>  findList(Page page,String value){
		return enterStockDao.findList(page,value);
	}
	
	public Page<BisEnterStock> getStocks(Page<BisEnterStock> page,BisEnterStock obj) {
		return enterStockDao.getStocks(page, obj);
	}
	/**
	 * 
	 * @author pyl
	 * @Description: 获得入库联系单ID
	 * @date 2016年3月5日 下午12:08:24
	 * @return
	 * @throws
	 */
	public String getLinkIdToString() {
		User user = UserUtil.getCurrentUser();
		String userCode = user.getUserCode();
		//判断用户码是否为空
		if (StringUtils.isNull(user.getUserCode())) {
			userCode = "YZH";
		}else{//判断用户码 的长度
			if (userCode.length() > 3) {
				userCode = userCode.substring(0, 3);
			}else if(userCode.length() < 3){
				userCode = StringUtils.lpadStringLeft(3, userCode);
			}
		}
		String linkId = "E" + userCode + StringUtils.timeToString();
		return linkId;
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 根据提单号获得对应的入库联系单号
	 * @date 2016年3月5日 下午12:08:24
	 * @return
	 * @throws
	 */
	public List<BisEnterStock> getEnterStockByBillNum(String billNum) {
		return enterStockDao.find(Restrictions.eq("delFlag","0"),Restrictions.like("itemNum", billNum,MatchMode.ANYWHERE));
	}
	
	/**
	 * 入库报告书--普通客户
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 *  @param sku sku
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	public List<Map<String,Object>> findRepot(Integer ntype,String itemNum,String cunNum,String stockIn,String linkId,String sku,String strTime,String endTime,String isBonded){
		List<Map<String,Object>> getlist=null;
		if(ntype!=null){
			if(1==ntype){
				getlist=enterStockDao.findRepotPT(itemNum, cunNum, stockIn, linkId,sku,strTime, endTime,isBonded);
			}else if(2==ntype){
				getlist=enterStockDao.findRepotJP(itemNum, cunNum, stockIn, linkId,sku,strTime, endTime,isBonded);
			}else{
				getlist=enterStockDao.findRepotOTE(itemNum, cunNum, stockIn, linkId,sku,strTime, endTime,isBonded);
			}
		}
		return getlist;
	}


	/**
	 * 入库报告书（接口专用）--普通客户
	 * @param itemNum 提单号
	 * @param ctnNum 厢号
	 * @param realClientName 客户id
	 * @return
	 */
	public List<Map<String,Object>> enterReportInformation(String itemNum, String ctnNum, String realClientName){
		List<Map<String,Object>> getList = enterStockDao.enterReportInformation(itemNum, ctnNum, realClientName);
		return getList;
	}

	/**
	 * 按提单号查询
	 * @param itemNum
	 */
	public List<BisEnterStock> findEnterList(Map<String,Object> params) {
		return enterStockDao.findBy(params);
	}
	
	public Date getFirstTime(String rkTime) throws ParseException {
    	Date date=null;
		if(!StringUtils.isNull(rkTime)){
			String dateS;
			if(rkTime.contains(",")){
			   dateS = (rkTime.split(","))[0];
			}else{
			   dateS =rkTime;
			}
			date = DateUtils.stringToDate(dateS);
		}
		return date;
	}

	public BisEnterStock getEnterStock(String billNum){
		return enterStockDao.getEnterStock(billNum);
	}
	
	//获得数据库连接
	public Connection getConnect() throws SQLException{
		return enterStockDao.getConnection();
	}

	public List<HashMap> getlisths(String chineseName, String voyageNum) {
		// TODO Auto-generated method stub
		List<HashMap> reString=enterStockDao.getlisths(voyageNum,chineseName);
		return reString;
	}


}
