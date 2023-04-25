package com.haiersoft.ccli.remoting.hand.in.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.service.AsnActionLogService;
import com.haiersoft.ccli.wms.dao.ASNDao;
import com.haiersoft.ccli.wms.dao.ASNInfoDao;
import com.haiersoft.ccli.wms.dao.AsnActionDao;
import com.haiersoft.ccli.wms.dao.EnterStockDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.TrayInfo;

/**
 * 
 * @author Connor.M
 * @ClassName: WarehousingWebService
 * @Description: 入库
 * @date 2016年3月3日 下午5:29:22
 */
@WebService
@Service
public class WarehousingWebService{
	
	@Autowired
	private ASNInfoDao aSNInfoDao;
	@Autowired
	private AsnActionLogService asnActionLogService;
	@Autowired
	private ASNDao aSNDao;
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private AsnActionDao asnActionDao;
	@Autowired
	private EnterStockDao enterStockDao;
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 入库理货扫描
	 * @date 2016年3月3日 下午6:40:03 
	 * @param asn
	 * @param sku
	 * @return
	 * @throws
	 */
	public String getTallyWarehousing(String asn, String sku){
		Result<BisAsnInfo> result = new Result<BisAsnInfo>();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_asnId", asn));
		filters.add(new PropertyFilter("EQS_skuId", sku));
		List<BisAsnInfo> infos = aSNInfoDao.find(filters);
		if(null != infos && infos.size() == 1){
			BisAsnInfo asnInfo = infos.get(0);
			result.setCode(0);
			result.setMsg("操作成功！");
			result.setObj(asnInfo);
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该ASN下此SKU有误！");
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 入库理货确认
	 * @date 2016年3月4日 上午9:33:37 
	 * @param asn ASN
	 * @param sku SKU
	 * @param trayCode 托盘号
	 * @param state 状态0成品，1货损
	 * @param num 数量
	 * @param userName 用户名
	 * @return 
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String inStorageTally(String asn, String sku, String trayCode, String state, String num, String userName){
		Date now = new Date();//同一时间
		Result<TrayInfo> result = new Result<TrayInfo>();
		///获得ASN明细-SKU
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_asnId", asn));
		filters.add(new PropertyFilter("EQS_skuId", sku));
		List<BisAsnInfo> infos = aSNInfoDao.find(filters);
		if(null != infos && infos.size() == 1){
			//查询库存集合
			List<PropertyFilter> trayFilters = new ArrayList<PropertyFilter>();
			trayFilters.add(new PropertyFilter("EQS_trayId", trayCode));
			List<TrayInfo> trayInfos = trayInfoDao.find(trayFilters);
			//判断该托盘是否理货
			if(null != trayInfos && trayInfos.size() > 0){
				result.setCode(1);
				result.setMsg("该托盘已上架！该托盘的区位号为："+trayInfos.get(0).getAreaNum()+" ！请重新扫描！");
			}else{
				//获得ASN对象
				BisAsn bisAsn = aSNDao.find(asn);
				//修改计费区间表
				List<PropertyFilter> asnActionFilters = new ArrayList<PropertyFilter>();
				asnActionFilters.add(new PropertyFilter("EQS_asn", asn));
				asnActionFilters.add(new PropertyFilter("EQS_sku", sku));
				asnActionFilters.add(new PropertyFilter("EQS_clientId", bisAsn.getStockIn()));
				asnActionFilters.add(new PropertyFilter("NULLS_chargeEndDate", ""));//时间  为 null
				asnActionFilters.add(new PropertyFilter("EQS_status", "1"));
				List<AsnAction> asnActions = asnActionDao.find(asnActionFilters);//查询ASN区间表
				
				//判断ASN计费区间是否存在
				if(null != asnActions && asnActions.size() == 1){
					//获得ASN明细-SKU
					BisAsnInfo asnInfo = infos.get(0);
					//添加库存表
					TrayInfo trayInfo = new TrayInfo();
					trayInfo.setTrayId(trayCode);
					trayInfo.setCtnNum(bisAsn.getCtnNum());
					trayInfo.setBillNum(bisAsn.getBillNum());
					trayInfo.setContactType("1");
					trayInfo.setContactNum(bisAsn.getLinkId());
					trayInfo.setAsn(asn);
					trayInfo.setSkuId(sku);
					trayInfo.setStockIn(bisAsn.getStockIn());
					trayInfo.setStockName(bisAsn.getStockName());
					trayInfo.setBeforeStockIn(bisAsn.getStockIn());
					trayInfo.setBeforeStockName(bisAsn.getStockName());
					trayInfo.setOriginalPiece(Integer.valueOf(num));
					trayInfo.setNowPiece(Integer.valueOf(num));
					trayInfo.setRemovePiece(0);
					trayInfo.setCargoName(asnInfo.getCargoName());
					trayInfo.setCargoType(asnInfo.getCargoType());
					trayInfo.setIfTransfer("0");
					trayInfo.setIfSecondEnter("N");
					trayInfo.setNetWeight(BigDecimalUtil.mul(asnInfo.getNetSingle() == null ? 0 : asnInfo.getNetSingle(), Double.parseDouble(num)));//总净重
					trayInfo.setGrossWeight(BigDecimalUtil.mul(asnInfo.getGrossSingle() == null ? 0 : asnInfo.getGrossSingle(), Double.parseDouble(num)));//总毛重
					trayInfo.setUnits(asnInfo.getUnits());//单位 千克，吨
					trayInfo.setNetSingle(asnInfo.getNetSingle() == null ? 0 : asnInfo.getNetSingle());
					trayInfo.setGrossSingle(asnInfo.getGrossSingle() == null ? 0 : asnInfo.getGrossSingle());
					trayInfo.setWarehouseId(bisAsn.getWarehouseId());
					trayInfo.setWarehouse(bisAsn.getWarehouse());
					trayInfo.setEnterTallyTime(now);
					trayInfo.setEnterTallyOperson(userName);
					trayInfo.setEnterState(state);
					trayInfo.setCargoState("00");
					trayInfo.setUpdateTime(now);
					trayInfoDao.save(trayInfo);//保存库存表
					//判断 回写 ASN状态为 2入库中
					if("1".equals(bisAsn.getAsnState())){
						bisAsn.setAsnState("2");
						bisAsn.setTallyUser(userName);
						aSNDao.save(bisAsn);//修改ASN表
					}
					int count = 0;//总数量
					double netWeight = 0.0;//总净重
					double grossWeight = 0.0;//总毛重
					AsnAction asnAction = asnActions.get(0);//获得 ASN费用区间表 对象
					//数量累加
					asnAction.setNum(null==asnAction.getNum()?0:asnAction.getNum());
					count = asnAction.getNum() + Integer.parseInt(num);
					//获得库存统计数量     暂时以千克为基准   累计净重，毛重
					if(trayInfo.getUnits().equals("2")){//单位  吨
						netWeight = BigDecimalUtil.add(asnAction.getNetWeight() == null ? 0 : asnAction.getNetWeight(), BigDecimalUtil.mul(trayInfo.getNetWeight(), 1000));
						grossWeight = BigDecimalUtil.add(asnAction.getGrossWeight() == null ? 0 : asnAction.getGrossWeight(), BigDecimalUtil.mul(trayInfo.getGrossWeight(), 1000));
					}else if(trayInfo.getUnits().equals("3")){//单位  克
						netWeight = BigDecimalUtil.add(asnAction.getNetWeight() == null ? 0 : asnAction.getNetWeight(), BigDecimalUtil.div(trayInfo.getNetWeight(), 1000));
						grossWeight = BigDecimalUtil.add(asnAction.getGrossWeight() == null ? 0 : asnAction.getGrossWeight(), BigDecimalUtil.div(trayInfo.getGrossWeight(), 1000));
					}else{
						netWeight = BigDecimalUtil.add(asnAction.getNetWeight() == null ? 0 : asnAction.getNetWeight(), trayInfo.getNetWeight());
						grossWeight = BigDecimalUtil.add(asnAction.getGrossWeight() == null ? 0 :asnAction.getGrossWeight(), trayInfo.getGrossWeight());
					}
					asnAction.setNum(count);
					asnAction.setNetWeight(netWeight);
					asnAction.setGrossWeight(grossWeight);
				    asnAction.setChargeStaDate(null==asnAction.getChargeStaDate()?now:asnAction.getChargeStaDate());
					asnActionDao.save(asnAction);//修改 ASN费用区间表
					asnActionLogService.saveLog(asnAction, "2", count-Integer.parseInt(num), Integer.parseInt(num), "入库理货确认时对ASN区间表进行修改",userName);
					result.setCode(0);
					result.setMsg("操作成功！");
				}else{//不存在  暂不操作
					result.setCode(1);
					result.setMsg("数据错误，计费区间表有误！");
				}
			}
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该ASN下此SKU有误！");
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 根据托盘号展示信息
	 * @date 2016年3月4日 下午7:10:32 
	 * @param trayCode 托盘号
	 * @return
	 * @throws
	 */
	public String getMessageByTrayCode(String trayCode){
		Result<TrayInfo> result = new Result<TrayInfo>();
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		filters.add(new PropertyFilter("EQS_cargoState", "00"));
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		
		if(null != trayInfos && trayInfos.size() == 1){
			TrayInfo trayInfo = trayInfos.get(0);
			result.setCode(0);
			result.setMsg("操作成功！");
			result.setObj(trayInfo);
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘未理货完成状态！");
		}
		String res = JSON.toJSONString(result);
		return res;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 入库确认
	 * @date 2016年3月4日 下午7:11:25 
	 * @param trayCode 托盘号
	 * @param location 库位
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String inStorageConfirm(String trayCode, String location, String userName){
		Date now = new Date();
		Result<TrayInfo> result = new Result<TrayInfo>();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		filters.add(new PropertyFilter("EQS_cargoState", "00"));
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
		if(null != trayInfos && trayInfos.size() == 1){
			TrayInfo trayInfo = trayInfos.get(0);
			if(StringUtils.isNull(location)){
				location = "00-00-000";
			}
			String[] num = location.split("-");//库位号截取 注：库位号为三段式如：A1-01-18
			//判断是否为三段式   若非三段式补足两位
			if(num.length < 3){
				num = ArrayUtils.addAll(num, "00", "00");
			}
			String buildingNum = StringUtils.lpadStringLeft(2, num[0]);
			//修改库存
			trayInfo.setBuildingNum(buildingNum.substring(0, 1));//楼
			trayInfo.setFloorNum(buildingNum.substring(1, 2));//层
			trayInfo.setRoomNum(num[1]);
			//手持输入货位号格式判断，区位号、库位号分情况处理
			if(num.length == 3){				
				 //格式 1-02-030405
				if(num[2].length() == 6){
					trayInfo.setAreaNum(num[2]);
					trayInfo.setCargoLocation(num[0]+"-"+num[1]+"-"+num[2].substring(0,2)+"-"+num[2].substring(2,4)+"-"+num[2].substring(4));
				}//格式 1-02-03
				else if(num[2].length() == 2){
					trayInfo.setAreaNum(StringUtils.lpadStringLeft(2, num[2]));//三位补足
					trayInfo.setCargoLocation(location);
				}else{
					result.setCode(1);
					result.setMsg("货位号格式不正确！");
					return JSON.toJSONString(result);
				}
			}//格式 1-02-03-04-05
			else if(num.length == 5){
				//修改库存				
				trayInfo.setAreaNum(StringUtils.lpadStringLeft(2, num[2])+StringUtils.lpadStringLeft(2, num[3])+StringUtils.lpadStringLeft(2, num[4]));
				trayInfo.setCargoLocation(location);
			}else{
				result.setCode(1);
				result.setMsg("货位号格式不正确！");
				return JSON.toJSONString(result);
			}	
			trayInfo.setCargoState("01");
			trayInfo.setEnterOperson(userName);
			Date asnTime=null;
			//修改ASN 状态
			BisAsn bisAsn = aSNDao.find(trayInfo.getAsn());
			if(bisAsn.getAsnState().equals("2")){
				bisAsn.setAsnState("3");
				if(bisAsn.getInboundTime() == null){
					bisAsn.setIfPlanTime(0);
					bisAsn.setInboundTime(now);
				}else{
					bisAsn.setIfPlanTime(1);
				}
			}
			//20170927注释掉：计件报表实际计算，此处不单独记录数量
			/*if(!StringUtils.isNull(bisAsn.getRuleJobType())){
				List<BasePieceworkRule> ruleList=pieceworkRuleService.findby("jobType",bisAsn.getRuleJobType());
				if(!ruleList.isEmpty()){ //TODO 计算人员工作量改存
					for(BasePieceworkRule rule:ruleList){
						if("库管人员".equals(rule.getPersonType())){
							bisAsn.setKgNumber(BigDecimalUtil.add(bisAsn.getKgNumber()!=null?bisAsn.getKgNumber():0d, BigDecimalUtil.mul(rule.getRatio(), trayInfo.getGrossWeight())));
						}else if("理货人员".equals(rule.getPersonType())){
							bisAsn.setLhNumber(BigDecimalUtil.add(bisAsn.getLhNumber()!=null?bisAsn.getLhNumber():0d, BigDecimalUtil.mul(rule.getRatio(), trayInfo.getGrossWeight())));
						}else if("叉车人员".equals(rule.getPersonType())){
							bisAsn.setCcNumber(BigDecimalUtil.add(bisAsn.getCcNumber()!=null?bisAsn.getCcNumber():0d, BigDecimalUtil.mul(rule.getRatio(), trayInfo.getGrossWeight())));
						}
					}
				}
			}*/
			//yyyyyy
			bisAsn.setRealGrossWeight(BigDecimalUtil.add(bisAsn.getRealGrossWeight()!=null?bisAsn.getRealGrossWeight():0d, trayInfo.getGrossWeight()));
			aSNDao.save(bisAsn);
			if(bisAsn.getIfPlanTime().equals(1) || bisAsn.getIfPlanTime()==1){
				asnTime=bisAsn.getInboundTime();
			}else{
				asnTime=now;
			}
			trayInfo.setEnterStockTime(asnTime);
			trayInfo.setUpdateTime(now);
			trayInfoDao.save(trayInfo);
			//查询入库联系单数据    修改入库时间
			BisEnterStock enterStock = enterStockDao.find(bisAsn.getLinkId());
			if(StringUtils.isNull(enterStock.getRkTime())){
				enterStock.setRkTime(DateUtils.formatDateTime2(now));
			}else{
				//如果时间一样 就不拼接
				String[] rkList=enterStock.getRkTime().split(",");
				int size=rkList.length;
				int a=1;
				for(int i=size-1;i>=0;i--){
					if(DateUtils.formatDateTime2(now).equals(rkList[i])){
						a=0;
						break;
					}
				}
				if(a==1){
					 enterStock.setRkTime(enterStock.getRkTime()+","+DateUtils.formatDateTime2(now));
				}
			}
			enterStockDao.save(enterStock);
			result.setCode(0);
			result.setMsg("操作成功！");
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘状态未理货完成！");
		}
		return JSON.toJSONString(result);
	}
	
}
