package com.haiersoft.ccli.wms.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.GoCardBulkDao;
import com.haiersoft.ccli.wms.entity.BaseGoCardBulk;
 
@Service
public class GoCardBulkService extends BaseService<BaseGoCardBulk, String> {
	@Autowired
	private GoCardBulkDao goCardBulkDao;
	
	@Override
	public HibernateDao<BaseGoCardBulk,String> getEntityDao() {
		return goCardBulkDao;
}
	
	
	/**
	 * 
	 * @author pyl
	 * @return
	 * @throws
	 */
	public String getNewGoCard() {
 			int num = goCardBulkDao.getSequenceId("SEQ_GO_CARD_BULK");
			String goCard = "NO_" + DateUtils.getYear()+DateUtils.getMonth()+DateUtils.getDay() + "SH" + StringUtils.lpadStringLeft(4, String.valueOf(num));
			return goCard;
	}

}
