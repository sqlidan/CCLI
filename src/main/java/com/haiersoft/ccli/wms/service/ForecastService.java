package com.haiersoft.ccli.wms.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.wms.dao.ForecastDao;
import com.haiersoft.ccli.wms.entity.BisForecast;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 
 * @author pyl
 * @ClassName: ForecastService
 * @Description: 入库预报单Service
 * @date 2016年4月15日 下午3:52:37
 */
@Service
@Transactional(readOnly = true)
public class ForecastService extends BaseService<BisForecast, String> {
	
	@Autowired
	private ForecastDao forecastDao;
	//@Autowired
	//private ForecastInfoDao forecastInfoDao;
	
	@Override
	public HibernateDao<BisForecast, String> getEntityDao() {
		return forecastDao;
	}
	/**
	 * 
	 * @author pyl
	 * @Description: 获得新的预报单ID
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
	
}
