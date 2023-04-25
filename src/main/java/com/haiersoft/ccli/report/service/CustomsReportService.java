package com.haiersoft.ccli.report.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.report.dao.CustomsReportDao;
/**
 * 
 * @author Mazy
 * @ClassName: CustomsReportService
 * @Description: 海关报表Service
 * @date 2016年6月25日 下午2:41:09
 */
@Service
public class CustomsReportService {
	
	@Autowired
	private CustomsReportDao customsReportDao;
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCustomsReportInfo(String starTime,String endTime,int npage,int pageSize){
		Map<String, Object> getMap=null;
		getMap=  customsReportDao.getCustomsReportInfo(starTime, endTime, npage, pageSize);
		if(getMap!=null && getMap.size()>0){
			List<Map<String, Object>> getList=(List<Map<String, Object>>) getMap.get("rows");
			if(getList!=null && getList.size()>0){
				Map<String, Object> rowMap=null;
				Double getKCNum=0d;
				Double getCKNum=0d;//出库数量
				Double getKCWeight=0d;
				Double getCKWeight=0d;//出库净重
				Double getKCPrices=0d;
				Double getCKPrices=0d;//出库总价
				String keys=null;
				for(int i=0;i<getList.size();i++){
					rowMap=getList.get(i);
					if(keys==null){

						getKCNum=Double.valueOf(rowMap.get("SCALAR").toString());
						if(null!=rowMap.get("OUTSCALAR")){
							getCKNum=Double.valueOf(rowMap.get("OUTSCALAR").toString());
						}else{
							getCKNum=0.00;
						}
						getKCNum=getKCNum-getCKNum;
						rowMap.put("NUM", getKCNum);
						
						getKCWeight=Double.valueOf(rowMap.get("NET_WEIGHT").toString());
						if(null!=rowMap.get("OUTNETWEIGHT")){
							getCKWeight=Double.valueOf(rowMap.get("OUTNETWEIGHT").toString());
						}else{
							getCKWeight=0.00;
						}
						getKCWeight=getKCWeight-getCKWeight;
						rowMap.put("WEIGHT", getKCWeight);
									
						getKCPrices=Double.valueOf(rowMap.get("TOTAL_PRICES").toString());
						if(null!=rowMap.get("OUTTOTALPRICES")){
							getCKPrices=Double.valueOf(rowMap.get("OUTTOTALPRICES").toString());
						}else{
							getCKPrices=0.00;
						}
						getKCPrices=getKCPrices-getCKPrices;
						rowMap.put("PRICES", getKCPrices);
						
					}else{
						if(keys.equals(rowMap.get("BILL_NUM").toString()+rowMap.get("ITEM_NUM").toString())){
							if(null!=rowMap.get("OUTSCALAR")){
								getCKNum=Double.valueOf(rowMap.get("OUTSCALAR").toString());
							}else{
								getCKNum=0.00;
							}
							getKCNum=getKCNum-getCKNum;
							rowMap.put("NUM", getKCNum);
							

							if(null!=rowMap.get("OUTNETWEIGHT")){
								getCKWeight=Double.valueOf(rowMap.get("OUTNETWEIGHT").toString());
							}else{
								getCKWeight=0.00;
							}
							getKCWeight=getKCWeight-getCKWeight;
							rowMap.put("WEIGHT", getKCWeight);
							

							if(null!=rowMap.get("OUTTOTALPRICES")){
								getCKPrices=Double.valueOf(rowMap.get("OUTTOTALPRICES").toString());
							}else{
								getCKPrices=0.00;
							}
							getKCPrices=getKCPrices-getCKPrices;
							rowMap.put("PRICES", getKCPrices);						
							
						}else{
							getKCNum=Double.valueOf(rowMap.get("SCALAR").toString());
							if(null!=rowMap.get("OUTSCALAR")){
								getCKNum=Double.valueOf(rowMap.get("OUTSCALAR").toString());
							}else{
								getCKNum=0.00;
							}
							getKCNum=getKCNum-getCKNum;
							rowMap.put("NUM", getKCNum);
							
							getKCWeight=Double.valueOf(rowMap.get("NET_WEIGHT").toString());
							if(null!=rowMap.get("OUTNETWEIGHT")){
								getCKWeight=Double.valueOf(rowMap.get("OUTNETWEIGHT").toString());
							}else{
								getCKWeight=0.00;
							}
							getKCWeight=getKCWeight-getCKWeight;
							rowMap.put("WEIGHT", getKCWeight);
							
							getKCPrices=Double.valueOf(rowMap.get("TOTAL_PRICES").toString());
							if(null!=rowMap.get("OUTTOTALPRICES")){
								getCKPrices=Double.valueOf(rowMap.get("OUTTOTALPRICES").toString());
							}else{
								getCKPrices=0.00;
							}
							getKCPrices=getKCPrices-getCKPrices;
							rowMap.put("PRICES", getKCPrices);
							
							
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
