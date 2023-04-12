package com.haiersoft.ccli.base.web;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.entity.SkuIntoExcel;
import com.haiersoft.ccli.base.service.ProductClassService;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ASNInfoService;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
/**
 * SUKcontroller
 * @author lzg
 * @date 2016年2月24日
 */
@Controller
@RequestMapping("base/sku")
public class SukInfoContorller extends BaseController {
	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private EnterStockInfoService enterStockInfoService;//入库联系单
	@Autowired
	private ProductClassService productClassService;//货品类型
	@Autowired
	private TrayInfoService trayInfoService;//gzq 20160623 添加
	@Autowired
	private ASNInfoService asnInfoService;//ans明细 //gzq 20160623 添加
	@Autowired
	private StandingBookService standingBookService;
	
	/*跳转列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "base/skuList";
	}
	
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="listjson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseSkuBaseInfo> page = getPage(request);
		page.orderBy("operateTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQI_delFlag", String.valueOf(0));
		filters.add(filter);
		page = skuInfoService.search(page, filters);
		return getEasyUIData(page);
	}
	 
	/**
	 * 添加SKU跳转
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		BaseSkuBaseInfo newObj=new BaseSkuBaseInfo();
		newObj.setValidityTime(DateUtils.addYear(new Date(), 2));
		model.addAttribute("sku", newObj);
		model.addAttribute("action", "create");
		return "base/skuForm";
	}
	
	/**
	 * 添加SKU
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BaseSkuBaseInfo sku, Model model) {
		sku.setSkuId(skuInfoService.getSKUId(Integer.valueOf(sku.getCargoType())));//id
		sku.setProducingArea(sku.getCargoName()+" "+sku.getTypeSize());//属性
		User user = UserUtil.getCurrentUser();
		if(user!=null){
			sku.setOperator(user.getName());
		}
		sku.setOperateTime(new Date());
		skuInfoService.save(sku);
		return "success";
	}
	
	/**
	 * json 异步添加SKU
	 */
	@RequestMapping(value = "createjx", method = RequestMethod.POST)
	@ResponseBody
	public String createSKU(@Valid BaseSkuBaseInfo sku, Model model,HttpServletResponse response) {
		String toParm =""; 
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		sku.setSkuId(skuInfoService.getSKUId(Integer.valueOf(sku.getCargoType())));//id
		sku.setProducingArea(sku.getCargoName()+" "+sku.getTypeSize());//属性
		double getGrossSingle=sku.getGrossWeight()/sku.getPiece();
		double getNetSingle=sku.getNetWeight()/sku.getPiece();
		sku.setGrossSingle(Double.valueOf(BigDecimalUtil.round(getGrossSingle, 2)));//毛重
		sku.setNetSingle(Double.valueOf(BigDecimalUtil.round(getNetSingle, 2)));//单净
		User user = UserUtil.getCurrentUser();
		if(user!=null){
			sku.setOperator(user.getName());
		}
		sku.setOperateTime(new Date());
		skuInfoService.save(sku);
		toParm=JSON.toJSONString(sku);
		return toParm;
	}
	/**
	 * 修改SKU跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("sku", skuInfoService.getSKUInfo(id));
		model.addAttribute("action", "update");
		return "base/skuForm";
	}

	
	/**
	 * 修改SKU
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody BaseSkuBaseInfo sku,Model model,HttpServletRequest request) {
		 
		try
		{
			String stris = request.getParameter("isupdatesku")!=null?request.getParameter("isupdatesku").toString():"";
			String pgs = request.getParameter("hidgrossSingle")!=null?request.getParameter("hidgrossSingle").toString():"0";
			String pns = request.getParameter("hidnetSingle")!=null?request.getParameter("hidnetSingle").toString():"0";
			String pcn = request.getParameter("hidcargoName")!=null?request.getParameter("hidcargoName").toString():"";
			//原始  单毛  单净  
			sku.setProducingArea(sku.getCargoName()+" "+sku.getTypeSize());//属性
			User user = UserUtil.getCurrentUser();
			if(user!=null) 
				sku.setUpdateUser(user.getName()); 
			
			////修改SKU的同时修改入库联系单明细的SKU信息    by slh 注释掉 无需更新件数 单价等。。只需修改单毛单净 等   
			//enterStockInfoService.updateSku(sku);
			
			sku.setUpdateTime(new Date());
			skuInfoService.update(sku);
			String re="success";
			String se="success";
			if(stris.trim().equals("2")) {//更新 所有此sku 相关的库存 单净毛、总净毛 、品名
			   if(sku.getNetSingle()!= Double.valueOf(pns) || sku.getGrossSingle()!=Double.valueOf(pgs)){
				   re=	UpdNetGroBySku(sku.getSkuId(),sku.getNetSingle(),sku.getGrossSingle(),pns,pgs,(user!=null?user.getName():"修改sku用户"));
				   re = re.contains("1")?"success":"nosuccess";
			   }
			   if(!sku.getCargoName().equals(pcn)){
				   se=this.UpdCargoNameBySku(sku.getSkuId(), sku.getCargoName(), pcn, (user!=null?user.getName():"修改sku用户"));
				   se=se.contains("1")?"success":"nosuccess";
			   }
			}
			if(re.equals("success") && se.equals("success")){
				return "success";
			}else{
				return "nosuccess"; 
			}
		}catch(Exception e) {
		  return "nosuccess"; 
		}
	}
	/**
	 * 删除客户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") String id) {
		if(id!=null){
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("skuId", id);
			List<TrayInfo> trayInfoList = trayInfoService.findBySku(params);
			if(!trayInfoList.isEmpty()){
				//货存表中有此SKU禁止删除
				return "error";
			}
			List<BisAsnInfo> asnList =asnInfoService.getASNInfoListBySKU(id);
			if(!asnList.isEmpty()){
				//ASN明细表中有此SKU禁止删除
				return "error";
			}
			Map<String,Object> params2 =new HashMap<String,Object>();
			params2.put("sku", id);
			List<BisEnterStockInfo> infoList = enterStockInfoService.getListByMap(params2);
			int size = infoList.size();
			if (size != 0) {//入库联系单明细表中有此SKU禁止删除
				return "error";
			}

			BaseSkuBaseInfo  getObj=skuInfoService.getSKUInfo(id);
			getObj.setDelFlag(1);
			skuInfoService.update(getObj);
		}
		return "success";
	}
	/**
	 * SKU导入跳转
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "into", method = RequestMethod.GET)
	public String intoExcel( Model model) {
		model.addAttribute("action", "into");
		return "base/skuInto";
	}
	/**
	 * sku导入
	 */
	@RequestMapping(value = "into", method = RequestMethod.POST)
	@ResponseBody
	public String intoExcel(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, Model model) {
		try {
			ImportParams params = new ImportParams();  
			params.setTitleRows(1);
			List<SkuIntoExcel> list = ExcelImportUtil.importExcel(file.getInputStream(), SkuIntoExcel.class, params);
			System.out.println(list.size());
			BaseSkuBaseInfo newObj=null;
			User user = UserUtil.getCurrentUser();
			if(list!=null && list.size()>0){
				int getTypeId=0;
				int getClassId=0;
				for(SkuIntoExcel getObj:list){
					getTypeId=productClassService.getProductClassId(getObj.getCargoType(),true);
					getClassId=productClassService.getProductClassId2(getObj.getClassType(),getTypeId);
					if(getTypeId>0 && getClassId>0){
						newObj=new BaseSkuBaseInfo();
						newObj.setTypeName(getObj.getCargoType());
						newObj.setClassName(getObj.getClassType());
						if(getObj.getCargoState()!=null && !"".equals(getObj.getCargoState()) && user!=null){
							newObj.setCargoState(getObj.getCargoState());
							newObj.setCargoName(getObj.getCargoName());
							newObj.setCargoType(String.valueOf(getTypeId));
							newObj.setClassType(String.valueOf(getClassId));
							newObj.setValidityTime(getObj.getValidityTime());
							newObj.setTypeSize(getObj.getTypeSize());
							newObj.setNetSingle(getObj.getNetSingle());
							newObj.setGrossSingle(getObj.getGrossSingle());
							newObj.setUnits(getObj.getUnits());
							newObj.setMscNum(getObj.getMscNum());
							newObj.setRemark(getObj.getRemark());
							newObj.setRkdh(getObj.getRkdh());
							newObj.setShipNum(getObj.getShipNum());
							newObj.setAttribute1(getObj.getAttribute1());
							newObj.setAttribute2(getObj.getAttribute2());
							newObj.setAttribute3(getObj.getAttribute3());
							newObj.setSkuId(skuInfoService.getSKUId(getTypeId));
							newObj.setOperateTime(new Date());
							newObj.setOperator(user.getName());
							newObj.setProducingArea(getObj.getCargoName()+" "+getObj.getTypeSize());
							newObj.setLotNum(getObj.getLotNum());
							newObj.setProNum(getObj.getProNum());
							skuInfoService.save(newObj);
						}
					}//end if
				}
			}
		}   catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * 根据sku获取入库号
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "getrkdh/{skuId}")
	@ResponseBody
	public String getrkdh(@PathVariable("skuId") String skuId) {
		BaseSkuBaseInfo  getObj=new BaseSkuBaseInfo();
		if (null!=skuId && !skuId.equals("")) {
			getObj=skuInfoService.get(skuId);
		}
		return getObj.getRkdh();
	}

	/**
	 * 根据sku获取库存  //bis_tray_info
	 * @param skuId
	 * @return GetSKU/"+
	 */
	@RequestMapping(value = "GetSKU/{skuId}")
	@ResponseBody
	public String GetSKU(@PathVariable("skuId") String skuId) {
		try{ 
		  List<Map<String,Object>> list = trayInfoService.GetTrayInfoBySku(skuId); 
		   return (list!=null && list.size()>0 )?"HAVA":"NO"; 
		}
		catch (Exception ex2) { 
			 return "NO";
		}
	}
	
	///存储过程 根据
   public String UpdNetGroBySku(String skuid,double netsingle,double grosssingle,String  pnetsingle,String pgrosssingle,String username){    
		 String driver = PropertiesUtil.getPropertiesByName("jdbc.driver", "application"); 
		  Statement stmt = null;  
		  ResultSet rs = null;  
		  Connection conn = null; 
		  String restate="0";//0失败 1成功
		  try {  
		      Class.forName(driver);   
		      conn = standingBookService.getConnect();
		      CallableStatement proc = null;  
		      proc = conn.prepareCall("{ call Pro_UpdNetGroSinleBySku(?,?,?,?,?,?,?) }");  
		      proc.setString(1, skuid);  
		      proc.setDouble(2, netsingle);
		      proc.setDouble(3, grosssingle);
		      proc.setString(4, pnetsingle); 
		      proc.setString(5, pgrosssingle); 
		      proc.setString(6, username); 
		      proc.registerOutParameter(7, Types.INTEGER);  
		      proc.execute();  
		      restate = proc.getString(7);    
		   }  
		   catch (SQLException ex2) {
			   //ex2.printStackTrace();  
		   }  
		   catch (Exception ex2) {  
			  // ex2.printStackTrace();  
		   }  
		   finally{  
		      try {  
		        if(rs != null){  
		          rs.close();  
		          if(stmt!=null){  
		            stmt.close();  
		          }  
		          if(conn!=null){  
		            conn.close();  
		          }  
		        }  
		      }  
		      catch (SQLException ex1) {  
		      }  
		  }
		return restate;
	}   
   
   
 ///存储过程 修改SKU品名
   public String UpdCargoNameBySku(String skuid,String cargoname,String pcargoname,String username){    
		 String driver = PropertiesUtil.getPropertiesByName("jdbc.driver", "application"); 
		  Statement stmt = null;  
		  ResultSet rs = null;  
		  Connection conn = null; 
		  String restate="0";//0失败 1成功
		  try {  
		      Class.forName(driver);   
		      conn = standingBookService.getConnect();
		      CallableStatement proc = null;  
		      proc = conn.prepareCall("{ call Pro_UpdCargoNameBySku(?,?,?,?,?) }");  
		      proc.setString(1, skuid);  
		      proc.setString(2, cargoname);
		      proc.setString(3, pcargoname);
		      proc.setString(4, username); 
		      proc.registerOutParameter(5, Types.INTEGER);  
		      proc.execute();  
		      restate = proc.getString(5);    
		   }  
		   catch (SQLException ex2) {
			   //ex2.printStackTrace();  
		   }  
		   catch (Exception ex2) {  
			  // ex2.printStackTrace();  
		   }  
		   finally{  
		      try {  
		        if(rs != null){  
		          rs.close();  
		          if(stmt!=null){  
		            stmt.close();  
		          }  
		          if(conn!=null){  
		            conn.close();  
		          }  
		        }  
		      }  
		      catch (SQLException ex1) {  
		      }  
		  }
		return restate;
	}

}
