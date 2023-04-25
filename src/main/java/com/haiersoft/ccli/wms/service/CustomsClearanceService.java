package com.haiersoft.ccli.wms.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.CustomsDeclarationDao;
import com.haiersoft.ccli.wms.dao.CustomsClearanceDao;
import com.haiersoft.ccli.wms.entity.BisCustomsDeclaration;
import com.haiersoft.ccli.wms.entity.BisCustomsClearance;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
/**
 * 
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class CustomsClearanceService extends BaseService<BisCustomsClearance, String> {
	
	@Autowired
	private CustomsClearanceDao customsClearanceDao;
	
	@Override
	public HibernateDao<BisCustomsClearance, String> getEntityDao() {
		return customsClearanceDao;
	}
		
	
	/**
	 * 
	 * @author lsh
	 * @Description: 获得新的业务单ID
	 * @date 2016年3月5日 下午12:08:24
	 * @return
	 * @throws
	 */
	public String getLinkIdToString() {
//		int num = enterStockDao.getSequenceId("SEQ_ENTER_STOCK");
//		int num = expenseContractDao.getContractId();
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
//		String number = "E" + userCode + StringUtils.lpadInt(6, num);
		String linkId = "F" + userCode + StringUtils.timeToString();
		return linkId;
	}
	
	
	public Page<BisCustomsClearance> seachCustomsClearanceSql(Page<BisCustomsClearance> page,BisCustomsClearance customsClearance){
		return customsClearanceDao.seachCustomsClearanceSql(page, customsClearance);
	}
	
	
	public List<Map<String, String>> seachCustomsClearanceSql(BisCustomsClearance customsClearance){
		return customsClearanceDao.seachCustomsClearanceSql(customsClearance);

	}
	/**
	 * 
	 * @author 
	 * @throws
	 */
	public String getNewNumber() {
 		int num = customsClearanceDao.getSequenceId("SEQ_CUSTOMS");
 		String number = StringUtils.lpadInt(6, num);
		return number;
	}

 	//获得报关单集合（按param查询）
	public List<BisCustomsClearance> findList(Map<String, Object> params) {
		return customsClearanceDao.findBy(params);
	}
	
	//获取已入的数量和
	public String countNum(String cdNum) {
		return customsClearanceDao.countNum(cdNum);
	}
	
	//获取已入的数量和
	public String countMaxNum(String cdNum) {
		return customsClearanceDao.countMaxNum(cdNum);
	}
	
}
