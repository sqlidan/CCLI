package com.haiersoft.ccli.base.dao;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.ExpenseContract;
import com.haiersoft.ccli.common.persistence.HibernateDao;
/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseContractDao
 * @Description: 费用合同DAO
 * @date 2016年2月24日 下午3:52:06
 */
@Repository
public class ExpenseContractDao extends HibernateDao<ExpenseContract, String>{
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 查询SEQ
	 * @date 2016年2月25日 上午10:38:10 
	 * @return
	 * @throws
	 */
	public int getContractId() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SEQ_CONTRACT.nextval AS NUM FROM DUAL ");
	    int maxId = (Integer)(this.getSession().createSQLQuery(sb.toString()).addScalar("NUM", StandardBasicTypes.INTEGER)).uniqueResult();
		return maxId;
	}

	//获取明细中价目未维护的条数
	public Integer getNoPrice(String contractNum) {
		StringBuffer sb = new StringBuffer(""
							+ " select * " 
							+ " from BASE_EXPENSE_CONTRACT_INFO "
							+ " where CONTRACT_NUM =:contractNum and price is null ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("contractNum", contractNum);
		Integer a = super.getCountBySql(sb.toString(),params);
		return a;
	}
	
}
