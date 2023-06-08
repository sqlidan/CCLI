package com.haiersoft.ccli.supervision.service;


import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.supervision.dao.BaseAccountDao;
import com.haiersoft.ccli.supervision.entity.BaseAccount;
import com.haiersoft.ccli.supervision.entity.BaseAccountExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseAccountService extends BaseService<BaseAccount, String> {
	
	@Autowired
	BaseAccountDao baseAccountDao;

	@Override
	public HibernateDao<BaseAccount, String> getEntityDao() {
		// TODO Auto-generated method stub
		return baseAccountDao;
	}

	/**
	 * 查询底账明细页面
	 * @param params
	 * @return
	 */
	public Page<BaseAccount> seachSql(Page<BaseAccount> page, Map<String, Object> params){
		return baseAccountDao.seachSql(page, params);
	}

	/**
	 * 导出明细页面
	 * @param params
	 * @return
	 */
	public List<BaseAccount> seachSql(Map<String, Object> params){
		return baseAccountDao.seachSql(params);
	}

	/**
	 * 查询底账汇总页面
	 * @param params
	 * @return
	 */
	public Page<BaseAccount> seachStatisticsSql(Page<BaseAccount> page, Map<String, Object> params){
		return baseAccountDao.seachStatisticsSql(page, params);
	}

	/**
	 * 导出底账汇总
	 * @param params
	 * @return
	 */
	public List<BaseAccountExcel> seachStatisticsSql(Map<String, Object> params){
		return baseAccountDao.seachStatisticsSql(params);
	}

	/**
	 * 通过id获取底账明细
	 * @param id
	 * @return
	 */
	public BaseAccount queryBaseAccountInfo(Integer id){
		return baseAccountDao.queryBaseAccountInfo(id);
	}

	/**
	 * 通过id修改底账明细
	 */
	public void  updateById(BaseAccount baseAccount){
		baseAccountDao.updateById(baseAccount);
	}


	/**
	 * 通过id查询底账明细
	 */
	public BaseAccount  selectByBillNoAndCno(String billNo, String containerNo) {
		return baseAccountDao.selectByBillNoAndCno(billNo, containerNo);
	}
    @Transactional(readOnly = false)
	public void  getEntityAndInsert(PlatformReservationOutbound outbound) {
		BaseAccount baseAccount = new BaseAccount();
		//箱号
		baseAccount.setContainerNo(outbound.getContainerNo());
		//提单号
		baseAccount.setBillNo(outbound.getBillNo());
		//货类
		baseAccount.setProductType(outbound.getProductType());
		//货物名称(品名)
		baseAccount.setProductName(outbound.getProductName());
		//件数
		baseAccount.setNum(outbound.getNum());
		//重量
		baseAccount.setWeight(outbound.getWeight().toString());
		if (outbound.getId() != null && outbound.getId().length() >= 10) {
			//预约出库/入库 id
			baseAccount.setStockId(outbound.getId().toString());
		}
		//  0:入库，1：出库
		baseAccount.setStockType("1");
		//创建时间
		baseAccount.setCreatedTime(new Date());
		//更新时间
		baseAccount.setUpdatedTime(new Date());
		//备注
		baseAccount.setRemark("写入底账拆分明细");
		this.save(baseAccount);
	}

	public void  updateOutBoundIsSplit(String id){
		baseAccountDao.updateOutBoundIsSplit(id);
	}



	/**
	 * 查询底账明细页面
	 * @param params
	 * @return
	 */
	public Page<BaseAccount> seachData(Page<BaseAccount> page, Map<String, Object> params){
		return baseAccountDao.seachData(page, params);
	}
	public Boolean flagInsert(List<PlatformReservationOutbound> info) {
		Boolean  flag = false;
		for (int i = 0; i < info.size(); i++) {
			//判断库存中的剩余数量是否大于出库数量
			Map<String, Object> params = new HashMap<>();
			params.put("billNo",info.get(i).getBillNo());
			params.put("containerNo",info.get(i).getContainerNo());
			List<BaseAccountExcel> 	baseAccountExcel = this.seachStatisticsSql(params);
			//查询有没有底账信息
			if (baseAccountExcel == null) {
				break;
			}
			if (Double.valueOf(baseAccountExcel.get(0).getSurplusNum()) < Double.valueOf(info.get(i).getNum())
					|| Double.valueOf(baseAccountExcel.get(0).getSurplusWeight()) < Double.valueOf(info.get(i).getWeight())) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}
