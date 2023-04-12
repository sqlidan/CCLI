package com.haiersoft.ccli.wms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.DismantleTrayDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.BisDismantleTray;
import com.haiersoft.ccli.wms.entity.TrayInfo;

/**
 * 
 * @author Connor.M
 * @ClassName: DismantleTrayService
 * @Description: 拆托Service
 * @date 2016年3月17日 下午4:12:08
 */
@Service
@Transactional(readOnly = true)
public class DismantleTrayService extends BaseService<BisDismantleTray, Integer>{

	@Autowired
	private DismantleTrayDao dismantleTrayDao;
	@Autowired
	private TrayInfoDao trayInfoDao;
	
	@Override
    public HibernateDao<BisDismantleTray, Integer> getEntityDao() {
	    return dismantleTrayDao;
    }
	
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: web 拆托 确认
	 * @date 2016年3月18日 下午2:25:54 
	 * @param trayInfo
	 * @param newCode
	 * @param num
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
    public String dismantleTrayWebConfirm(TrayInfo trayInfo, String newCode, String num){
		String res = "success"; 
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", newCode));
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		
		if(null != trayInfos && trayInfos.size() > 0){
			res = "error";
		}else{
			Date now = new Date();//时间统一
			User user = UserUtil.getCurrentUser();//获得用户信息

			TrayInfo info = new TrayInfo();
			BeanUtils.copyProperties(trayInfo, info);//复制
			
			//修改  原托盘库存数据
			trayInfo.setNowPiece(trayInfo.getNowPiece() - Integer.parseInt(num));
			trayInfo.setRemovePiece(Integer.parseInt(num)+trayInfo.getRemovePiece());
			trayInfo.setNetWeight(BigDecimalUtil.mul(trayInfo.getNetSingle() == null ? 0.0 : trayInfo.getNetSingle(), Double.parseDouble(trayInfo.getNowPiece().toString())));
			trayInfo.setGrossWeight(BigDecimalUtil.mul(trayInfo.getGrossSingle() == null ? 0.0 : trayInfo.getGrossSingle(), Double.parseDouble(trayInfo.getNowPiece().toString())));
			trayInfo.setUpdateTime(now);
			trayInfoDao.save(trayInfo);
			
			//添加  新托盘库存数据
			info.setId(null);
			info.setOriginalPiece(Integer.parseInt(num));
			info.setRemovePiece(0);
			info.setTrayId(newCode);
			info.setNowPiece(Integer.parseInt(num));
			info.setNetWeight(BigDecimalUtil.mul(info.getNetSingle() == null ? 0.0 : info.getNetSingle(), Double.parseDouble(num)));
			info.setGrossWeight(BigDecimalUtil.mul(info.getGrossSingle() == null ? 0.0 : info.getGrossSingle(), Double.parseDouble(num)));
			info.setUpdateTime(now);
			trayInfoDao.save(info);
			
			//保存拆托数据
			BisDismantleTray dismantleTray = new BisDismantleTray();
			dismantleTray.setOldTrayCode(trayInfo.getTrayId());
			dismantleTray.setNewTrayCode(newCode);
			dismantleTray.setNum(Integer.parseInt(num));
			dismantleTray.setDismantleType("1");
			dismantleTray.setDismantleUser(user.getName());
			dismantleTray.setDismantleTime(now);
			dismantleTrayDao.save(dismantleTray);
		}
		return res;
	}


	public List<BisDismantleTray> findBy(String column, String value) {
		return dismantleTrayDao.findBy(column, value);
	}
	
	
}
