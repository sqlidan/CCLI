package com.haiersoft.ccli.base.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.base.dao.ExpenseContractInfoDao;
import com.haiersoft.ccli.base.dao.ExpenseSchemeInfoDao;
import com.haiersoft.ccli.base.entity.ExpenseContractInfo;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseSchemeInfoService
 * @Description: 费用方案明细Service
 * @date 2016年2月28日 下午3:27:05
 */
@Service
@Transactional(readOnly = true)
public class ExpenseSchemeInfoService extends BaseService<ExpenseSchemeInfo, String> {

	@Autowired
	private ExpenseSchemeInfoDao expenseSchemeInfoDao;
	@Autowired
	private ExpenseContractInfoDao expenseContractInfoDao;
	
	@Override
    public HibernateDao<ExpenseSchemeInfo, String> getEntityDao() {
	    return expenseSchemeInfoDao;
    }
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得8位SEQ 的id
	 * @date 2016年2月29日 下午5:27:10 
	 * @return
	 * @throws
	 */
	public String getSchemeInfoSeq() {
		int num = expenseSchemeInfoDao.getSequenceId("SEQ_SCHEME_INFO");
		return StringUtils.lpadInt(8, num);
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description:返回动态显示内容
	 * @date 2018年12月10日 下午5:28:58 
	 * @param schemeNum
	 * @param ids
	 * @throws
	 */
	public Page<ExpenseSchemeInfo> seachSql(Page<ExpenseSchemeInfo> page,String contractNum){
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT                                                                    ");
		sb.append("	schemeInfo.Id,                                                           ");
		sb.append("	schemeInfo.billing,                                                      ");
		sb.append("	unit.label AS unitName,                                                  ");
		sb.append("	schemeInfo.currency,                                                     ");
		sb.append("	currency.Label AS currencyName,                                          ");
		sb.append("	schemeInfo.fee_code,                                                     ");
		sb.append("	schemeInfo.fee_name AS feeName,                                          ");
		sb.append("	schemeInfo.fee_type,                                                     ");
		sb.append("	feetype.label AS feeTypeName,                                            ");
		sb.append("	schemeInfo.gear_code AS gearCode,                                        ");
		sb.append("	schemeInfo.gear_exp AS gearExp,                                          ");
		sb.append("	schemeInfo.if_pay AS ifPay,                                              ");
		sb.append("	schemeInfo.max_price AS maxPrice,                                        ");
		sb.append("	schemeInfo.min_price AS minPrice,                                        ");
		sb.append("	schemeInfo.remark,                                                       ");
		sb.append("	schemeInfo.scheme_num,                                                   ");
		sb.append("	schemeInfo.term_attribute AS termAttribute,                              ");
		sb.append("	termAttribute.Label AS termattributeName,                                ");
		sb.append("	schemeInfo.unit                                                          ");
		sb.append("FROM                                                                      ");
		sb.append("	base_expense_scheme_info schemeInfo                                      ");
		sb.append("LEFT JOIN (                                                               ");
		sb.append("	SELECT                                                                   ");
		sb.append("		currency.label,                                                      ");
		sb.append("		currency.                                                            ");
		sb.append("	VALUE                                                                    ");
		sb.append("                                                                          ");
		sb.append("	FROM                                                                     ");
		sb.append("		dict currency                                                        ");
		sb.append("	WHERE                                                                    ");
		sb.append("		currency.type = 'currencyType'                                       ");
		sb.append(") currency ON schemeInfo.currency = currency.                             ");
		sb.append("VALUE                                                                     ");
		sb.append("                                                                          ");
		sb.append("LEFT JOIN (                                                               ");
		sb.append("	SELECT                                                                   ");
		sb.append("		unit.label,                                                          ");
		sb.append("		unit.                                                                ");
		sb.append("	VALUE                                                                    ");
		sb.append("                                                                          ");
		sb.append("	FROM                                                                     ");
		sb.append("		dict unit                                                            ");
		sb.append("	WHERE                                                                    ");
		sb.append("		unit.type = 'units'                                                  ");
		sb.append(") unit ON schemeInfo.billing = unit.                                      ");
		sb.append("VALUE                                                                     ");
		sb.append("                                                                          ");
		sb.append("LEFT JOIN (                                                               ");
		sb.append("	SELECT                                                                   ");
		sb.append("		termAttribute.label,                                                 ");
		sb.append("		termAttribute.                                                       ");
		sb.append("	VALUE                                                                    ");
		sb.append("                                                                          ");
		sb.append("	FROM                                                                     ");
		sb.append("		dict termAttribute                                                   ");
		sb.append("	WHERE                                                                    ");
		sb.append("		termAttribute.type = 'termAttribute'                                 ");
		sb.append(") termAttribute ON schemeInfo.Term_Attribute = termAttribute.             ");
		sb.append("VALUE                                                                     ");
		sb.append("                                                                          ");
		sb.append("LEFT JOIN (                                                               ");
		sb.append("	SELECT                                                                   ");
		sb.append("		feetype.label,                                                       ");
		sb.append("		feetype.                                                             ");
		sb.append("	VALUE                                                                    ");
		sb.append("                                                                          ");
		sb.append("	FROM                                                                     ");
		sb.append("		dict feetype                                                         ");
		sb.append("	WHERE                                                                    ");
		sb.append("		feetype.type = 'feeType'                                             ");
		sb.append(") feetype ON schemeInfo.Fee_Type = feetype.                               ");
		sb.append("VALUE                                                                     ");
		sb.append("                                                                          ");
		sb.append("WHERE                                                                     ");
		sb.append("    1=1                                                                   ");
		if(null!=contractNum&&!"".equals(contractNum)){
			sb.append(" and schemeInfo.Scheme_Num='"+contractNum+"'");
		}
		Map<String, Object> paramType = new HashMap<>();
        paramType.put("id", String.class);
        paramType.put("feeName", String.class);
        paramType.put("unit", Double.class);
        paramType.put("currency", String.class);
        paramType.put("currencyName", String.class);
        paramType.put("billing", String.class);
        paramType.put("unitName", String.class);
        paramType.put("minPrice", Double.class);
        paramType.put("maxPrice", Double.class);
        paramType.put("termAttribute", String.class);
        paramType.put("termattributeName", String.class);
        paramType.put("ifPay", Integer.class);
        paramType.put("gearCode", String.class);
        paramType.put("gearExp", String.class);
		return expenseSchemeInfoDao.findPageSql(page,sb.toString(), paramType,null);
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description: 批量添加明细
	 * @date 2016年2月29日 下午5:28:58 
	 * @param schemeNum
	 * @param ids
	 * @throws
	 */
	@Transactional(readOnly = false)
	public void createSchemeInfoBatch(String schemeNum, List<String> ids) {
		for(String id : ids){
			String num = getSchemeInfoSeq();
			ExpenseContractInfo contractInfo = expenseContractInfoDao.find(id);
			
			ExpenseSchemeInfo schemeInfo = new ExpenseSchemeInfo();
			schemeInfo.setId(num);
			schemeInfo.setSchemeNum(schemeNum);
			schemeInfo.setFeeCode(contractInfo.getExpenseCode());
			schemeInfo.setFeeName(contractInfo.getCargoName());
			schemeInfo.setCurrency(contractInfo.getCurrencyType());
			schemeInfo.setBilling(contractInfo.getBillUnit());
			
			Double sale = contractInfo.getOnSale() == null ? 1D : contractInfo.getOnSale();
			if(null != contractInfo.getPrice()){
				schemeInfo.setUnit(BigDecimalUtil.mul(contractInfo.getPrice(), sale));
			}
			schemeInfo.setFeeType(contractInfo.getFeeType());
			
			schemeInfo.setTermAttribute(contractInfo.getTermAttribute());
			schemeInfo.setMinPrice(contractInfo.getMinPrice());
			schemeInfo.setMaxPrice(contractInfo.getMaxPrice());
			schemeInfo.setGearCode(contractInfo.getGearCode());
			schemeInfo.setGearExp(contractInfo.getGearExp());
			schemeInfo.setIfPay(0);
			schemeInfo.setRemark(contractInfo.getRemark());
			
			expenseSchemeInfoDao.save(schemeInfo);
		}
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 复制方案明细
	 * @date 2016年2月29日 下午6:37:34 
	 * @param id
	 * @throws
	 */
	@Transactional(readOnly = false)
	public void createCopySchemeInfo(String id) {
		ExpenseSchemeInfo schemeInfo = expenseSchemeInfoDao.find(id);
		ExpenseSchemeInfo newSchemeInfo = new ExpenseSchemeInfo();

		BeanUtils.copyProperties(schemeInfo, newSchemeInfo);
		
		String num = getSchemeInfoSeq();//明细id
		newSchemeInfo.setId(num);
		
		expenseSchemeInfoDao.save(newSchemeInfo);
	}
	/**
	 * 
	 * @author PYL
	 * @Description: 根据多个条件获取的费用方案明细
	 * @param  params
	 */
	public List<ExpenseSchemeInfo> getFeeByName(Map<String,Object> params) {
			return expenseSchemeInfoDao.findBy(params);
	}

	public List<Map<String,Object>> getFeeCodeByBill(String billNum) {
		return expenseSchemeInfoDao.getFeeCodeByBill(billNum);
	}
	/**
	 * 获取费用方案下面的名称
	 * @param schemeNum
	 * @return
	 */
	public List<Map<String,Object>> getFeeCodeByNum(String schemeNum) {
		return expenseSchemeInfoDao.getFeeCodeByNum(schemeNum);
	}

	//更新垫付状态
	public void updatePay(String a) {
		expenseSchemeInfoDao.updatePay(a);
	}
	
	//更新取消垫付状态
	public void updateNoPay(String a) {
		expenseSchemeInfoDao.updateNoPay(a);
	}
}
