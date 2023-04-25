package com.haiersoft.ccli.wms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.PresenceBoxDao;
import com.haiersoft.ccli.wms.entity.BisGateCar;
import com.haiersoft.ccli.wms.entity.BisPresenceBox;

/**
 * 
 * @ClassName: PresenceBoxService
 * @Description: 在场箱管理Service
 */
@Service
public class PresenceBoxService extends BaseService<BisPresenceBox, Integer> {

	@Autowired
	private PresenceBoxDao presenceBoxDao;
	@Autowired
	private GateCarService gateCarService;
	@Override
    public HibernateDao<BisPresenceBox, Integer> getEntityDao() {
	    return presenceBoxDao;
    }
	
	//根据箱号返回在场箱信息
	public BisPresenceBox getBoxInfo(String ctnNum){
		BisPresenceBox box = this.find("ctnNum", ctnNum);
		if(null==box){
			return null;
		}else{
			return box;
		}
	}
	
	//箱子从车上落地
	public String luodiFromCar(String ctnNum){
		BisPresenceBox box = this.find("ctnNum", ctnNum);
		if(null==box){
			return "noBox";//未找到箱信息
		}
		String carNum=box.getCarNum();
		if(StringUtils.isNull(carNum)){
			return "notIn";//不在车上
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("carNum", carNum);
		List<BisGateCar> cars = gateCarService.findBy(params);
		if(cars.isEmpty()){
			return "noCar";
		}
		BisGateCar car = cars.get(0);
		car.setCtnNum("");
		car.setCtnSize("");
		car.setCtnType("");
		car.setAsn("");
		car.setBillNum("");
		car.setBisType("3");
		car.setStockId("");
		car.setStockName("");
		car.setEnterNum(0D);
		gateCarService.update(car);
		box.setCarNum("");
		box.setState("2");
		box.setDriverName("");
		this.update(box);
		return "success";
	}
	
	//箱子上车 
	public String upCar(String ctnNum,String carNum){
		BisPresenceBox box = this.find("ctnNum", ctnNum);
		if(null==box){
			return "noBox";//未找到箱信息
		}
		if(!StringUtils.isNull(box.getCarNum())){
			return "inCar";//此箱子已在车上
		}
		BisGateCar car = gateCarService.find("carNum", carNum);
		if(null==car){
			return "noCar";//未找到此车信息
		}
		if(!StringUtils.isNull(car.getCtnNum())){
			return "hasBox";  //此车上已有箱子
		}
		if(!StringUtils.isNull(box.getAsn()) && car.getBisType().equals("2")){
			return "conflict";  //此车为出库作业车,箱子为入库作业箱
		}
		box.setCarNum(carNum);
		car.setCtnNum(ctnNum);
		if(!StringUtils.isNull(box.getAsn())){
			box.setDriverName(car.getDriverName());
			car.setAsn(box.getAsn());
			car.setBillNum(box.getBillNum());
			car.setEnterNum(box.getEnterNum());
			car.setStockId(box.getStockId());
			car.setStockName(box.getStockName());
			car.setBisType("1");
			car.setCtnSize(box.getCtnNum());
			car.setCtnType(box.getCtnType());
		}
		gateCarService.update(car);
		this.update(box);
		return "success";
	}
	
	/**
	 * 计划插电
	 */
	public String ifCd(String ctnNum){
		BisPresenceBox box = this.find("ctnNum", ctnNum);
		if(null==box){
			return "noBox";
		}
		presenceBoxDao.updateCd(ctnNum,null,"3");
		return "success";
	}
	
	/**
	 * 开始插电
	 * @param ctnNum
	 */
	public String startCd(String ctnNum,Date now){
		BisPresenceBox box = this.find("ctnNum", ctnNum);
		if(null==box){
			return "noBox";
		}
		if(!box.getCdState().equals("0")){
			return "noZ";
		}
		presenceBoxDao.updateCd(ctnNum,now,"1");
		return "success";
	}
	/**
	 * 结束插电
	 * @param ctnNum
	 * @param now
	 * @return
	 */
	public String endCd(String ctnNum,Date now){
		BisPresenceBox box = this.find("ctnNum", ctnNum);
		if(null==box){
			return "noBox";
		}
		if(!box.getCdState().equals("1")){
			return "noZ";
		}
		presenceBoxDao.updateCd(ctnNum,now,"2");
		return "success";
	}
	
	public int deleteList(String ctnNum){
    	return presenceBoxDao.getSession().createSQLQuery(" delete from BIS_PRESENCE_BOX where CTN_NUM='"+ctnNum+"'").executeUpdate();
    }
}
