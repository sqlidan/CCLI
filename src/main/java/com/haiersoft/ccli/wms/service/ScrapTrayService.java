package com.haiersoft.ccli.wms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.AsnActionDao;
import com.haiersoft.ccli.wms.dao.ScrapTrayDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisScrapTray;
import com.haiersoft.ccli.wms.entity.TrayInfo;

/**
 * 
 * @author Connor.M
 * @ClassName: ScrapTrayService
 * @Description: 报损Service
 * @date 2016年3月21日 下午4:03:10
 */
@Service
public class ScrapTrayService extends BaseService<BisScrapTray, Integer> {

	@Autowired
	private ScrapTrayDao scrapTrayDao;
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private AsnActionDao asnActionDao;
	
	
	@Override
    public HibernateDao<BisScrapTray, Integer> getEntityDao() {
	    return scrapTrayDao;
    }
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得报损单号
	 * @date 2016年3月21日 下午5:04:09 
	 * @return
	 * @throws
	 */
	public String getTrayCode(){
		int num = scrapTrayDao.getSequenceId("SEQ_SCRAP_TRAY_CODE");
		String number =  StringUtils.dateToString() + StringUtils.lpadInt(6, num);
		return number;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 货损确认
	 * @date 2016年3月21日 下午7:07:27 
	 * @param scrapCode
	 * @param scrapType 报损类型：1，普通报损，2,库内分拣报损
	 * @param ids
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String scrapTrayConfirm(String scrapCode, String scrapType, List<Integer> ids){
		StringBuffer res = new StringBuffer();
		int error = 0;//问题条数
		for(Integer id : ids){
			//获得托盘对象
			TrayInfo trayInfo = trayInfoDao.find(id);
			//判断状态 是否是  上架
			if("01".equals(trayInfo.getCargoState())){
				List<PropertyFilter> scrapFilters = new ArrayList<PropertyFilter>();
				scrapFilters.add(new PropertyFilter("EQS_trayCode", trayInfo.getTrayId()));
				List<BisScrapTray> scrapTrays = scrapTrayDao.find(scrapFilters);
				if(null != scrapTrays && scrapTrays.size() > 0){
					error++;
					res.append("托盘号：").append(trayInfo.getTrayId()).append("，已货损操作！</br>");
				}else{
					List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
					if(trayInfo.getAsn()==null||"".equals(trayInfo.getAsn())){
						filters.add(new PropertyFilter("NULLS_asn", "")); //ASN
					}else{
						filters.add(new PropertyFilter("EQS_asn", trayInfo.getAsn()));
					}
					filters.add(new PropertyFilter("EQS_clientId", trayInfo.getStockIn()));
					filters.add(new PropertyFilter("EQS_status", "1"));
					filters.add(new PropertyFilter("NULLS_chargeEndDate", ""));
					List<AsnAction> asnActions = asnActionDao.find(filters);
					
					if(null != asnActions && asnActions.size() > 0){
						User user = UserUtil.getCurrentUser();//获得用户信息
						//保存 托盘报损数据
						BisScrapTray scrapTray = new BisScrapTray();
						scrapTray.setScrapCode(scrapCode);
						scrapTray.setScrapState("0");
						scrapTray.setTrayCode(trayInfo.getTrayId());
						scrapTray.setBillNum(trayInfo.getBillNum());
						scrapTray.setScrapType(scrapType);
						scrapTray.setAsn(trayInfo.getAsn());
						scrapTray.setSku(trayInfo.getSkuId());
						scrapTray.setClientName(trayInfo.getStockName());
						scrapTray.setClientId(trayInfo.getStockIn());
						scrapTray.setCargoName(trayInfo.getCargoName());
						scrapTray.setCargoType(trayInfo.getCargoType());
						scrapTray.setCargoLocation(trayInfo.getCargoLocation());
						scrapTray.setNum(trayInfo.getNowPiece());
						scrapTray.setNetWeight(trayInfo.getNetWeight());
						scrapTray.setGrossWeight(trayInfo.getGrossWeight());
						scrapTray.setUnits(trayInfo.getUnits());
						scrapTray.setScrapPerson(user.getName());
						scrapTray.setScrapDate(new Date());
						scrapTrayDao.save(scrapTray);
					}else{
						error++;
						res.append("托盘号：").append(trayInfo.getTrayId()).append("，无费用区间数据，不能操作！</br>");
					}
				}
			}else{
				error++;
				res.append("托盘号：").append(trayInfo.getTrayId()).append("，非上架状态，不能操作！</br>");
			}
		}
		
		String s = ""; 
		if(StringUtils.isNull(res.toString())){
			s = "操作成功！";
		}else{
			ids.size();
			s = "</br>共操作：" + ids.size() + "条，其中存在问题：" + error + "条</br></br>问题托盘号：</br>" + res.toString();
		}
		return s;
	}

	public List<BisScrapTray> findListByScrapCode(String scrapCode) {
		return scrapTrayDao.findBy("scrapCode", scrapCode);
	}
	
}
