package com.haiersoft.ccli.wms.service.tallying;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.wms.dao.DismantleTrayDao;
import com.haiersoft.ccli.wms.dao.LoadingInfoDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.dao.tallying.SplitTrayManageDao;
import com.haiersoft.ccli.wms.entity.BisDismantleTray;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 拆托
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class SplitTrayManageService extends BaseService<TrayInfo, Integer> {
	@Autowired
	private WarehouseManageService warehouseService;
	@Autowired
	private SplitTrayManageDao splitTrayDao;
	@Autowired
	private LoadingInfoDao loadingInfofDao;
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private DismantleTrayDao dismantleTrayDao;

	@Override
	public HibernateDao<TrayInfo, Integer> getEntityDao() {
		return splitTrayDao;
	}

	public Map<String, Object> getMaxXZ(String buildingNum, String floorNum, String roomNum, String areaNum, String actualStoreroomX) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		mapList = splitTrayDao.getMaxXZ(buildingNum,floorNum,roomNum,areaNum,actualStoreroomX);
		if (mapList!=null && mapList.size() > 0){
			return mapList.get(0);
		}
		return null;
	}

	/**
	 *
	 * @author Connor.M
	 * @Description: 出库 装车  拆托确认 操作
	 * @date 2016年3月21日 下午2:20:10
	 * @param oldTrayCode 老托盘号
	 * @param newTrayCode 新托盘号
	 * @param num 数量
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String dismantleTrayConfirm(String oldTrayCode, String newTrayCode, String num, String userName){
		Result<TrayInfo> result = new Result<TrayInfo>();

		List<PropertyFilter> pfs = new ArrayList<PropertyFilter>();
		pfs.add(new PropertyFilter("EQS_loadingState", "1"));
		pfs.add(new PropertyFilter("EQS_trayId", oldTrayCode));
		List<BisLoadingInfo> loadingInfos = loadingInfofDao.find(pfs);

		if(null != loadingInfos && loadingInfos.size() > 0){

			BisLoadingInfo loadingInfo = loadingInfos.get(0);

			//判断两个托盘数量
			if(loadingInfo.getPiece() > Integer.parseInt(num)){

				//查询库存 托盘数据
				List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
				filters.add(new PropertyFilter("EQS_trayId", oldTrayCode));
				filters.add(new PropertyFilter("EQS_cargoState", "11"));
				List<TrayInfo> trayInfos = trayInfoDao.find(filters);

				//判断托盘状态
				if(null != trayInfos && trayInfos.size() == 1){
					//查询新托盘数据
					List<PropertyFilter> trayfilters = new ArrayList<PropertyFilter>();
					PropertyFilter filter = new PropertyFilter("EQS_trayId", newTrayCode);
					trayfilters.add(filter);
					List<TrayInfo> infos = trayInfoDao.find(trayfilters);

					//判断 新托盘号是否存在
					if(null != infos && infos.size() > 0){
						result.setCode(1);
						result.setMsg("新托盘号已存在，请重新扫描！");
					}else{
						Date now = new Date();//时间统一

						TrayInfo trayInfo = trayInfos.get(0);//获得老托盘对象
						TrayInfo info = new TrayInfo();//新托盘对象

						BeanUtils.copyProperties(trayInfo, info);//复制

						//修改  原   托盘库存数据
						trayInfo.setNowPiece(trayInfo.getNowPiece() - Integer.parseInt(num));
						trayInfo.setRemovePiece(Integer.parseInt(num)+trayInfo.getRemovePiece());
						trayInfo.setNetWeight(BigDecimalUtil.mul(trayInfo.getNetSingle() == null ? 0.0 : trayInfo.getNetSingle(), Double.parseDouble(trayInfo.getNowPiece().toString())));
						trayInfo.setGrossWeight(BigDecimalUtil.mul(trayInfo.getGrossSingle() == null ? 0.0 : trayInfo.getGrossSingle(), Double.parseDouble(trayInfo.getNowPiece().toString())));
						trayInfo.setUpdateTime(now);
						trayInfoDao.save(trayInfo);

						//添加  新   托盘库存数据
						info.setId(null);
						info.setTrayId(newTrayCode);
						info.setRemovePiece(0);
						info.setOriginalPiece(Integer.parseInt(num));
						info.setNowPiece(Integer.parseInt(num));
						info.setNetWeight(BigDecimalUtil.mul(info.getNetSingle() == null ? 0.0 : info.getNetSingle(), Double.parseDouble(num)));
						info.setGrossWeight(BigDecimalUtil.mul(info.getGrossSingle() == null ? 0.0 : info.getGrossSingle(), Double.parseDouble(num)));
						info.setUpdateTime(now);
						//获取最大层高数
						List<Map<String,Object>> mapList = warehouseService.GetData(null,trayInfo.getBuildingNum(),trayInfo.getFloorNum(),trayInfo.getRoomNum()
								,trayInfo.getAreaNum(),null,null,trayInfo.getActualStoreroomX());
						if (mapList!=null && mapList.size() > 0) {
							Map<String, Object> maxXZ = mapList.get(0);
							if (maxXZ == null || maxXZ.get("MAXZ") == null) {
								return "未获取到原托盘层高数";
							}
							info.setActualStoreroomZ((Integer.parseInt(maxXZ.get("MAXZ").toString()) + 1) + "");
						}
						trayInfoDao.save(info);

						//修改  原   托盘装车单信息
						loadingInfo.setPiece(trayInfo.getNowPiece());
						loadingInfo.setNetWeight(trayInfo.getNetWeight());
						loadingInfo.setGrossWeight(trayInfo.getGrossWeight());
						loadingInfofDao.save(loadingInfo);

						//生成  新   托盘装车单信息
						BisLoadingInfo loadingInfo2 = new BisLoadingInfo();
						BeanUtils.copyProperties(loadingInfo, loadingInfo2);//复制

						loadingInfo2.setId(null);
						loadingInfo2.setTrayId(newTrayCode);
						loadingInfo2.setPiece(info.getNowPiece());
						loadingInfo2.setNetWeight(info.getNetWeight());
						loadingInfo2.setGrossWeight(info.getGrossWeight());
						loadingInfofDao.save(loadingInfo2);

						//保存拆托数据
						BisDismantleTray dismantleTray = new BisDismantleTray();
						dismantleTray.setOldTrayCode(trayInfo.getTrayId());
						dismantleTray.setNewTrayCode(newTrayCode);
						dismantleTray.setNum(Integer.parseInt(num));
						dismantleTray.setDismantleType("4");
						dismantleTray.setDismantleUser(userName);
						dismantleTray.setDismantleTime(now);
						dismantleTrayDao.save(dismantleTray);

						result.setCode(0);
						result.setMsg("操作成功！");
					}
				}else{
					result.setCode(1);
					result.setMsg("该托盘数据状态有误！");
				}
			}else{
				result.setCode(1);
				result.setMsg("新托盘数量不得大于原托盘数量，请重新输入！");
			}
		}else{
			result.setCode(1);
			result.setMsg("该托盘在装车单中状态错误！");
		}
		return JSON.toJSONString(result);
	}
}
