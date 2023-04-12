package com.haiersoft.ccli.base.service;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.base.dao.FeeCodeDao;
import com.haiersoft.ccli.base.entity.FeeCode;
/**
 * 费目service
 * @author ty
 * @date 2015年1月13日
 */
@Service
@Transactional(readOnly = true)
public class FeeCodeService extends BaseService<FeeCode, Integer> {
	
	/**加密方法*/
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;

	@Autowired
	private FeeCodeDao feeCodeDao;

	@Override
	public HibernateDao<FeeCode, Integer> getEntityDao() {
		return feeCodeDao;
	}
	
	
	public Page<FeeCode> seachSql(Page<FeeCode> page,Map<String, Object> params){
		return feeCodeDao.seachSql(page, params);
	}

	/**
	 * 保存费目代码
	 * @param feecode
	 */
	@Transactional(readOnly=false)
	public void save(FeeCode feeCode) {
		feeCodeDao.save(feeCode);
	}

	/**
	 * 删除用户
	 * @param id
	 */
	@Transactional(readOnly=false)
	public void delete(Integer id){
	    feeCodeDao.delete(id);
	}
	
	/**
	 * 按code查询
	 * @param feecode
	 * @return 费目代码
	 */
	public FeeCode getFeeCode(String code) {
		return feeCodeDao.findUniqueBy("code", code);
	}
	
	/**
	 * 获取买方承担的费目
	 */
	public List<FeeCode> getBuyFee() {
		return feeCodeDao.find(Restrictions.and(Restrictions.eq("buyBill",1),Restrictions.ne("code","cc")));
	}
	
	/**
	 * 获取卖方承担的费目
	 */
	public List<FeeCode> getSellFee() {
		return feeCodeDao.find(Restrictions.and(Restrictions.eq("sellBill",1),Restrictions.ne("code","cc")));
	}
	
	/**
	 * 根据费目代码获得费目名称
	 */
	public String getName(String code) {
		List<FeeCode> feeCodeList =  feeCodeDao.findBy("code", code);
		FeeCode feeCode = feeCodeList.get(0);
		String nameC = feeCode.getNameC();
		return nameC;
	}

	public List<FeeCode> findByType(Integer type) {
		return feeCodeDao.findBy("feeType", type);
	}

	public List<Map<String, Object>> findFeeCode(String param) {
		return feeCodeDao.findFeeCode(param);
	}

	public List<FeeCode> findList(Map<String, Object> param) {
		return feeCodeDao.findBy(param);
	}
	
	public List<Map<String,Object>> findFeeCodeByName(String name){
		return feeCodeDao.findFeeCodeByName(name);
	}

}
