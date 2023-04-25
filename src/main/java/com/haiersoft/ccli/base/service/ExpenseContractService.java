package com.haiersoft.ccli.base.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.base.dao.ExpenseContractDao;
import com.haiersoft.ccli.base.dao.ExpenseContractInfoDao;
import com.haiersoft.ccli.base.entity.ExpenseContract;
import com.haiersoft.ccli.base.entity.ExpenseContractInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseContractService
 * @Description: 费用合同Service
 * @date 2016年2月24日 下午3:52:37
 */
@Service
@Transactional(readOnly = true)
public class ExpenseContractService extends BaseService<ExpenseContract, String> {
	
	@Autowired
	private ExpenseContractDao expenseContractDao;
	@Autowired
	private ExpenseContractInfoDao expenseContractInfoDao;
	
	@Override
	public HibernateDao<ExpenseContract, String> getEntityDao() {
		return expenseContractDao;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得合同号
	 * @date 2016年2月25日 下午12:08:24
	 * @return
	 * @throws
	 */
	public String getContractIdToString() {
		int num = expenseContractDao.getSequenceId("SEQ_CONTRACT");
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
		String number = "C" + userCode + StringUtils.lpadInt(6, num);
		return number;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 复制合同
	 * @date 2016年2月27日 下午1:41:04 
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public ExpenseContract copyContract(String id) {
		User user = UserUtil.getCurrentUser();
		
		ExpenseContract contract = expenseContractDao.find(id);
		ExpenseContract newContract = new ExpenseContract();

		BeanUtils.copyProperties(contract, newContract);//复制对象属性
		String contractNum = this.getContractIdToString();//新合同号
		
		newContract.setContractNum(contractNum);
		newContract.setContractState("0");
		newContract.setOperatorPerson(user.getName());
		newContract.setOperateTime(new Date());
		
		expenseContractDao.save(newContract);
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>(); 
		
		PropertyFilter filter = new PropertyFilter("EQS_contractNum", contract.getContractNum());
		filters.add(filter);
		
		List<ExpenseContractInfo> contractInfos = expenseContractInfoDao.find(filters);
		for (ExpenseContractInfo contractInfo : contractInfos){
			ExpenseContractInfo info = new ExpenseContractInfo();
			BeanUtils.copyProperties(contractInfo, info);
			
			int num = expenseContractInfoDao.getSequenceId("SEQ_CONTRACT_INFO");
			String infoId = contractNum + StringUtils.lpadInt(4, num);
			
			info.setId(infoId);
			info.setContractNum(contractNum);
			info.setOperatePerson(user.getName());
			info.setOperateTime(new Date());
			expenseContractInfoDao.save(info);
		}
		return newContract;
	}

	public List<ExpenseContract> findList(String lanhuo) {
		return expenseContractDao.findBy("canvassionPerson", lanhuo);
	}

	//获取明细中价目未维护的条数
	public Integer getNoPrice(String contractNum) {
		return expenseContractDao.getNoPrice(contractNum);
	}
	
}
