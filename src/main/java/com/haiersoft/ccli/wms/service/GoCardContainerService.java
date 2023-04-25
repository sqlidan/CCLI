package com.haiersoft.ccli.wms.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.GoCardContainerDao;
import com.haiersoft.ccli.wms.entity.BaseGoCardContainer;
 
@Service
public class GoCardContainerService extends BaseService<BaseGoCardContainer, String> {
	@Autowired
	private GoCardContainerDao goCardContainerDao;
	
	@Override
	public HibernateDao<BaseGoCardContainer,String> getEntityDao() {
		return goCardContainerDao;
}

	/**
	 * 
	 * @author pyl
	 * @return
	 * @throws
	 */
	public String getNewGoCard() {
 			int num = goCardContainerDao.getSequenceId("SEQ_GO_CARD_CONTAINER");
			String goCard = "NO_" + DateUtils.getYear()+DateUtils.getMonth()+DateUtils.getDay()  + "JZ" + StringUtils.lpadStringLeft(4, String.valueOf(num));
			return goCard;
	}
}
