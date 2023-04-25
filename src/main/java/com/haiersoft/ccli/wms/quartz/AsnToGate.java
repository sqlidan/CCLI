package com.haiersoft.ccli.wms.quartz;
import java.util.ArrayList;
import java.util.List;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.BisGateCar;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
import com.haiersoft.ccli.wms.entity.BisPresenceBox;
import com.haiersoft.ccli.wms.service.ASNInfoService;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.GateCarService;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.PresenceBoxService;

/**
 *  
 *  
 *  
 */
//@Service
@DisallowConcurrentExecution 
public class AsnToGate  implements Job {
	//@Autowired
	//private InOutService inOutService;
	@Autowired
	private GateCarService gateCarService;
	@Autowired
	private PresenceBoxService presenceBoxService;
	@Autowired
	private ASNService asnService;
	@Autowired
	private ASNInfoService asnInfoService;
	@Autowired
    private LoadingInfoService loadingInfoService;
    @Autowired
    private LoadingOrderService loadingOrderService;
    @Autowired
    private LoadingOrderInfoService loadingOrderInfoService;
    
	//private static Logger logger = LoggerFactory.getLogger(AsnToGate.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("NNULLS_asn", ""));
		List<BisPresenceBox> boxList = presenceBoxService.search(filters);
		//更新在场箱记录，并更新相关车辆信息记录
		if(!boxList.isEmpty()){
			int size=boxList.size();
			for(int i=0;i<size;i++){
				BisPresenceBox box=new BisPresenceBox();
				box=boxList.get(i);
				String mr=box.getCtnNum();
				
				List<PropertyFilter> asnF = new ArrayList<PropertyFilter>();
				asnF.add(new PropertyFilter("INAS_asnState", new String[] {"1","2"}));
				asnF.add(new PropertyFilter("EQS_ctnNum", mr));
				List<BisAsn> asnList = asnService.search(asnF);
				if(!asnList.isEmpty()){
					BisAsn asn=asnList.get(0);
					List<BisAsnInfo> infoList = asnInfoService.getASNInfoList(asn.getAsn());
		        	Double entNum=0d;
		        	for(BisAsnInfo info:infoList){
		        		entNum+=info.getPiece();
		        	}
					box.setAsn(asn.getAsn());
					box.setBillNum(asn.getBillNum());
					box.setStockId(asn.getStockIn());
					box.setStockName(asn.getStockName());
					box.setEnterNum(entNum);
					if(!StringUtils.isNull(box.getCarNum())){
						BisGateCar car = gateCarService.find("carNum", box.getCarNum());
						car.setAsn(asn.getAsn());
						car.setBillNum(asn.getBillNum());
						car.setStockId(asn.getStockIn());
						car.setStockName(asn.getStockName());
						car.setEnterNum(entNum);
						car.setBisType("1");
						gateCarService.update(car);
					}
					presenceBoxService.update(box);
				}
			}
		}
		
		//更新无箱号无状态的车辆记录
		List<PropertyFilter> filterCar = new ArrayList<PropertyFilter>();
		filterCar.add(new PropertyFilter("NULLS_ctnNum", ""));
		filterCar.add(new PropertyFilter("EQS_bisType", "3"));
		List<BisGateCar> carList = gateCarService.search(filterCar);
		if(!carList.isEmpty()){
			int sizeC =carList.size();
			for(int j=0;j<sizeC;j++){
				BisGateCar car = carList.get(j);
				List<PropertyFilter> orderF = new ArrayList<PropertyFilter>();
	        	orderF.add(new PropertyFilter("EQS_carNum", car.getCarNum()));
	        	orderF.add(new PropertyFilter("INAS_orderState", new String[] {"1", "2", "3"}));
	    		List<BisLoadingOrder> orderList = loadingOrderService.search(orderF);
	    		if(!orderList.isEmpty()){
	    			BisLoadingOrder order = orderList.get(0);
	    			List<BisLoadingOrderInfo> infoList=loadingOrderInfoService.getInfoList(order.getOrderNum());
	    			Double outNum=0d;
	    			for(BisLoadingOrderInfo info:infoList){
	    				outNum += info.getPiece();
	    			}
	    			car.setBisType("2");
	    			car.setOutNum(outNum);
	    			car.setStockId(order.getStockIn());
	    			car.setStockName(order.getStockName());
	    			List<BisLoadingInfo> loadingList = loadingInfoService.getLoadingByOrder(order.getOrderNum());
	    			if(!loadingList.isEmpty()){
	    				BisLoadingInfo loading = loadingList.get(0);
	    				car.setLoadingNum(loading.getLoadingTruckNum());
	    			}
	    		}
			}
		}
		
		//更新出库作业，未生成装车单的车辆信息的装车单号
		List<PropertyFilter> filterCar2 = new ArrayList<PropertyFilter>();
		filterCar2.add(new PropertyFilter("NULLS_ctnNum", ""));
		filterCar2.add(new PropertyFilter("NULLS_loadingNum", ""));
		filterCar2.add(new PropertyFilter("EQS_bisType", "2"));
		List<BisGateCar> car2List = gateCarService.search(filterCar);
		if(!car2List.isEmpty()){
			int sizeD =car2List.size();
			for(int k=0;k<sizeD;k++){
				BisGateCar car = car2List.get(k);
				List<PropertyFilter> orderL = new ArrayList<PropertyFilter>();
				orderL.add(new PropertyFilter("EQS_carNum", car.getCarNum()));
				orderL.add(new PropertyFilter("INAS_orderState", new String[] {"2", "3"}));
	    		List<BisLoadingOrder> orderList2 = loadingOrderService.search(orderL);
	    		if(!orderList2.isEmpty()){
	    			BisLoadingOrder order2 = orderList2.get(0);
	    			List<BisLoadingInfo> loadingList = loadingInfoService.getLoadingByOrder(order2.getOrderNum());
	    			if(!loadingList.isEmpty()){
	    				BisLoadingInfo loading = loadingList.get(0);
	    				car.setLoadingNum(loading.getLoadingTruckNum());
	    			}
	    		}
			}
		}
    }
}
