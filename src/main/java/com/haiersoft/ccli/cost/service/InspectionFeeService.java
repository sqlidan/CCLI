package com.haiersoft.ccli.cost.service;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.dao.InspectionFeeDao;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.cost.entity.BisInspectionFee;
 
@Service
public class InspectionFeeService extends BaseService<BisInspectionFee, String> {
	@Autowired
	private InspectionFeeDao inspectionFeeDao;
	//@Autowired
	//private ClientDao clientDao;
	//@Autowired
	//private AsnActionDao asnActionDao;
	//@Autowired
	//private ASNInfoService asnInfoService;//ans明细
	//@Autowired
	//private SkuInfoService skuInfoService;//sku
	//@Autowired
	//private AsnActionService asnActionService;//ASN计费区间
	//@Autowired
	//private EnterStockService enterStockService;//入库联系单
	//@Autowired
	//private ClientService clientService;//客户
	
	@Override
	public HibernateDao<BisInspectionFee,String> getEntityDao() {
		return inspectionFeeDao;
}

	/**
	 * 
	 * @author pyl
	 * @Description: 获得新的查验费用主键ID
	 * @date 2016年6月24日 下午2:46:24
	 * @return
	 * @throws
	 */
	public String getNewFeeId() {
 		String num = this.getSequenceId().toString();
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
		String feeId = "CY" + userCode + StringUtils.dateToString() + StringUtils.lpadStringLeft(4,num);
		return feeId;
	}
	
	public Integer getSequenceId() {
		return inspectionFeeDao.getSequenceId("SEQ_INSPECTION_FEE");
	}

	//查验费用报表查询
	public List<Map<String, Object>> findReportList(String clientId,Date startTime, Date endTime,String ifLx,String sort,String order) {
		return inspectionFeeDao.findReportList(clientId,startTime,endTime,ifLx,sort,order);
	}
}
