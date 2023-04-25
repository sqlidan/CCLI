package com.haiersoft.ccli.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.base.dao.ExpenseContractInfoDao;
import com.haiersoft.ccli.base.dao.FeeCodeDao;
import com.haiersoft.ccli.base.entity.ExpenseContractInfo;
import com.haiersoft.ccli.base.entity.FeeCode;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseContractInfoService
 * @Description: 合同费目明细Service
 * @date 2016年2月25日 下午4:55:48
 */
@Service
@Transactional(readOnly = true)
public class ExpenseContractInfoService extends BaseService<ExpenseContractInfo, String> {
	
	@Autowired
	private ExpenseContractInfoDao expenseContractInfoDao;
	@Autowired
	private FeeCodeDao feeCodeDao;
	
	@Override
    public HibernateDao<ExpenseContractInfo, String> getEntityDao() {
	    return expenseContractInfoDao;
    }
	/**
	 * 关于合同列表动态展示数据方法的处理以及调整
	 * @param page
	 * @param contractNum
	 * @return
	 */
	public Page<ExpenseContractInfo> seachSql(Page<ExpenseContractInfo> page,String contractNum){
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT                                                         ");
		sb.append("   contractInfo.id AS id,                                       ");
		sb.append("   contractInfo.Expense_Code AS expenseCode,                    ");
		sb.append("   contractInfo.Cargo_Name AS cargoName,                        ");
		sb.append("   contractInfo.Price AS price,                                 ");
		sb.append("   contractInfo.On_Sale AS onSale,                              ");
		sb.append("   currency.label AS currencyType,                              ");
		sb.append("   contractInfo.Min_Price AS minPrice,                          ");
		sb.append("   contractInfo.Max_Price AS maxPrice,                          ");
		sb.append("   contractInfo.BILL_UNIT AS unit,                              ");
		sb.append("   unit.label AS billUnit,                                      ");
		sb.append("   termAttribute.Label AS termAttribute,                        ");
		sb.append("   contractInfo.TERM_ATTRIBUTE AS term,                         ");
		sb.append("   feetype.label AS feeType,                                    ");
		sb.append("   contractInfo.Space AS space,                                 ");
		sb.append("   contractInfo.Gear_Code AS gearCode,                          ");
		sb.append("   contractInfo.Gear_Exp AS gearExp,                            ");
		sb.append("   contractInfo.Remark AS remark                                ");
		sb.append(" FROM                                                           ");
		sb.append("   BASE_EXPENSE_CONTRACT_INFO contractInfo                      ");
		sb.append(" LEFT JOIN (                                                    ");
		sb.append("   SELECT                                                       ");
		sb.append("     currency.label,                                            ");
		sb.append("     currency.VALUE                                             ");
		sb.append("   FROM                                                         ");
		sb.append("     dict currency                                              ");
		sb.append("   WHERE                                                        ");
		sb.append("     currency.type = 'currencyType'                             ");
		sb.append(" ) currency ON contractInfo.currency_type = currency.VALUE      ");
		sb.append(" LEFT JOIN (                                                    ");
		sb.append("   SELECT                                                       ");
		sb.append("     unit.label,                                                ");
		sb.append("     unit.VALUE                                                 ");
		sb.append("   FROM                                                         ");
		sb.append("     dict unit                                                  ");
		sb.append("   WHERE                                                        ");
		sb.append("     unit.type = 'units'                                        ");
		sb.append(" ) unit ON contractInfo.Bill_Unit = unit.VALUE                  ");
		sb.append(" LEFT JOIN (                                                    ");
		sb.append("	  SELECT                                                       ");
		sb.append("	    termAttribute.label,                                       ");
		sb.append("		termAttribute.VALUE                                        ");
		sb.append("	  FROM                                                         ");
		sb.append("		dict termAttribute                                         ");
		sb.append("	  WHERE                                                        ");
		sb.append("		termAttribute.type = 'termAttribute'                       ");
		sb.append("	) termAttribute ON contractInfo.Term_Attribute = termAttribute.VALUE ");
		sb.append(" LEFT JOIN (                                                    ");
		sb.append("   SELECT                                                       ");
		sb.append("     feetype.label,                                             ");
		sb.append("     feetype.VALUE                                              ");
		sb.append("   FROM                                                         ");
		sb.append("     dict feetype                                               ");
		sb.append("   WHERE                                                        ");
		sb.append("     feetype.type = 'feeType'                                   ");
		sb.append(" ) feetype ON contractInfo.Fee_Type = feetype.VALUE             ");
		sb.append(" where 1=1            ");
		if(null!=contractNum&&!"".equals(contractNum)){
			sb.append(" and contractInfo.Contract_Num='"+contractNum+"'");
		}
		Map<String, Object> paramType = new HashMap<>();
        paramType.put("id", String.class);
        paramType.put("expenseCode", String.class);
        paramType.put("cargoName", String.class);
        paramType.put("price", Double.class);
        paramType.put("onSale", Double.class);
        paramType.put("currencyType", String.class);
        paramType.put("minPrice", Double.class);
        paramType.put("maxPrice", Double.class);
        paramType.put("billUnit", String.class);
        paramType.put("unit", String.class);
        paramType.put("termAttribute", String.class);
        paramType.put("term", String.class);
        paramType.put("feeType", String.class);
        paramType.put("space", String.class);
        paramType.put("gearCode", String.class);
        paramType.put("gearExp", String.class);
        paramType.put("remark", String.class);
		return expenseContractInfoDao.findPageSql(page,sb.toString(), paramType,null);
	}

	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得合同号费目明细
	 * @date 2016年2月25日 下午12:08:24
	 * @return
	 * @throws
	 */
	public String getContractIdToString(String contractNum) {
		int num = expenseContractInfoDao.getSequenceId("SEQ_CONTRACT_INFO");
		String number = contractNum + StringUtils.lpadInt(4, num);
		return number;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 批量添加明细 
	 * @date 2016年2月26日 下午2:05:21 
	 * @param contractNum
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String createContractInfoMore(String contractNum, List<Integer> ids) {
		User user = UserUtil.getCurrentUser();
		String back = "success";
		
		for (Integer codeId : ids){
			
			FeeCode feeCode = feeCodeDao.find(codeId);
			if(feeCode != null && feeCode.getId() != null){
				String id = this.getContractIdToString(contractNum);
				ExpenseContractInfo contractInfo = new ExpenseContractInfo();
				contractInfo.setId(id);
				contractInfo.setContractNum(contractNum);
				contractInfo.setExpenseCode(feeCode.getCode());
				contractInfo.setPrice(feeCode.getPriceBase());
				contractInfo.setCurrencyType(feeCode.getCurrencyType().toString());
				contractInfo.setCargoName(feeCode.getNameC());
				contractInfo.setBillUnit(StringUtils.isNull(String.valueOf(feeCode.getUnits())) ? "" : String.valueOf(feeCode.getUnits()));
				if(null!=feeCode.getMaxPrice()){
					contractInfo.setMaxPrice(feeCode.getMaxPrice());
				}
				if(null!=feeCode.getMinPrice()){
					contractInfo.setMinPrice(feeCode.getMinPrice());
				}
				contractInfo.setTermAttribute(feeCode.getTermAttribute());
				contractInfo.setFeeType(feeCode.getFeeType().toString());
				contractInfo.setOnSale(1D);
//				contractInfo.setRemark(feeCode.getRemark());
				contractInfo.setOperatePerson(user.getName());
				contractInfo.setOperateTime(new Date());
				
				expenseContractInfoDao.save(contractInfo);
			}else{
				back = "error";
			}
		}
		return back;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description:根据费用代码  插入明细表
	 * @date 2016年2月26日 下午4:32:50 
	 * @param contractNum
	 * @param ids
	 * @throws
	 */
	@Transactional(readOnly = false)
	public void createContractInfoByCode(String contractNum, String code) {
		User user = UserUtil.getCurrentUser();
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		
		PropertyFilter filter = new PropertyFilter("EQS_code", code);
		filters.add(filter);
		
		List<FeeCode> feeCodes = feeCodeDao.find(filters);
		if(feeCodes != null && feeCodes.size() > 0){
			for (FeeCode feeCode : feeCodes){
				String id = this.getContractIdToString(contractNum);
				
				ExpenseContractInfo contractInfo = new ExpenseContractInfo();
				contractInfo.setId(id);
				contractInfo.setContractNum(contractNum);
				contractInfo.setExpenseCode(feeCode.getCode());
				contractInfo.setPrice(feeCode.getPriceBase());
				contractInfo.setCurrencyType(feeCode.getCurrencyType().toString());
				contractInfo.setCargoName(feeCode.getNameC());
				contractInfo.setBillUnit(feeCode.getUnits().toString());
				contractInfo.setFeeType(feeCode.getFeeType().toString());
//				contractInfo.setRemark(feeCode.getRemark());
				contractInfo.setOperatePerson(user.getName());
				contractInfo.setOperateTime(new Date());
				
				expenseContractInfoDao.save(contractInfo);
			}
		}
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 复制
	 * @date 2016年2月26日 下午3:26:46 
	 * @param contractNum
	 * @param ids
	 * @throws
	 */
	@Transactional(readOnly = false)
	public void createCopyContractInfo(String id) {
		User user = UserUtil.getCurrentUser();
		
		ExpenseContractInfo contractInfo = expenseContractInfoDao.find(id);
		ExpenseContractInfo newContractInfo = new ExpenseContractInfo();

		BeanUtils.copyProperties(contractInfo, newContractInfo);
		String num = this.getContractIdToString(contractInfo.getContractNum());//合同号明细
		newContractInfo.setId(num);
		newContractInfo.setOperatePerson(user.getName());
		newContractInfo.setOperateTime(new Date());
		
		expenseContractInfoDao.save(newContractInfo);
	}

	public List<ExpenseContractInfo> findByNum(String contractNum) {
		return expenseContractInfoDao.findBy("contractNum", contractNum);
	}
	
}
