package com.haiersoft.ccli.report.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.report.dao.ItemNumReportDao;
/**
 * 
 * @author Mazy
 * @ClassName: CustomsReportService
 * @Description: 海关报表Service
 * @date 2016年6月25日 下午2:41:09
 */
@Service
public class ItemNumReportService {
	
	@Autowired
	private ItemNumReportDao itemNumReportDao;
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getItemNumReportInfo(String starTime,String endTime,int npage,int pageSize,String itemNum){
		Map<String, Object> getMap=null;
		getMap=  itemNumReportDao.getItemNumReportInfo(starTime, endTime, npage, pageSize,itemNum);
		if(getMap!=null && getMap.size()>0){
			List<Map<String, Object>> getList=(List<Map<String, Object>>) getMap.get("rows");
			if(getList!=null && getList.size()>0){
				Map<String, Object> rowMap=null;
 
				Double getTotalWeight=0d;
				Double getInWeight=0d;
				Double getOutWeight=0d;
			
				Double getKCWeight=0d;
				Double getCKWeight=0d;//出库净重
				String keys=null;
				
				for(int i=0;i<getList.size();i++){
					rowMap=getList.get(i);
					
					
					getInWeight=Double.valueOf(rowMap.get("INTOTALWEIGHT").toString());
					
					if(null!=rowMap.get("OUTTOTALWEIGHT")){
						getOutWeight=Double.valueOf(rowMap.get("OUTTOTALWEIGHT").toString());
					}else{
						getOutWeight=0.00;
					}
					getTotalWeight=getInWeight-getOutWeight;
					rowMap.put("KCTOTALWEIGHT", getTotalWeight);
					
					if(keys==null){

						getKCWeight=Double.valueOf(rowMap.get("NET_WEIGHT").toString());
						if(null!=rowMap.get("OUTNETWEIGHT")){
							getCKWeight=Double.valueOf(rowMap.get("OUTNETWEIGHT").toString());
						}else{
							getCKWeight=0.00;
						}
						getKCWeight=getKCWeight-getCKWeight;
						rowMap.put("WEIGHT", getKCWeight);
									
						
					}else{
						if(keys.equals(rowMap.get("BILL_NUM").toString()+rowMap.get("ITEM_NUM").toString())){

							
							if(null!=rowMap.get("OUTNETWEIGHT")){
								getCKWeight=Double.valueOf(rowMap.get("OUTNETWEIGHT").toString());
							}else{
								getCKWeight=0.00;
							}
							getKCWeight=getKCWeight-getCKWeight;
							rowMap.put("WEIGHT", getKCWeight);
							
					
							
						}else{

							
							getKCWeight=Double.valueOf(rowMap.get("NET_WEIGHT").toString());
							if(null!=rowMap.get("OUTNETWEIGHT")){
								getCKWeight=Double.valueOf(rowMap.get("OUTNETWEIGHT").toString());
							}else{
								getCKWeight=0.00;
							}
							getKCWeight=getKCWeight-getCKWeight;
							rowMap.put("WEIGHT", getKCWeight);
							
							
							
						}
					}
					getList.set(i, rowMap);
					keys=rowMap.get("BILL_NUM").toString()+rowMap.get("ITEM_NUM").toString();
				}//end for
				getMap.put("rows", getList);
			}
		}
		return getMap;
	}

}
