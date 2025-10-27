package com.haiersoft.ccli.cost.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.wms.web.PreEntryInvtQuery.PreEntryInvtQueryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.utils.CreatPDFUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.MyFileUtils;
import com.haiersoft.ccli.common.utils.MyPDFUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.BisCheckingBook;
import com.haiersoft.ccli.cost.service.BisCheckingBookService;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.itextpdf.text.PageSize;
/**
 * 应收对账
 * @author PYL
 * @date 2016年3月23日
 */
@Controller
@RequestMapping("cost/checkingbook")
public class CheckingBookContorller extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(CheckingBookContorller.class);
		 @Autowired
		private BisCheckingBookService  bisCheckingBookService;//对账单
		//excel导出对账单明细sql统一处理
		@RequestMapping(value = "down")
		@ResponseBody
		public void down(@Valid @ModelAttribute @RequestBody BisCheckingBook obj,HttpServletRequest request, HttpServletResponse response) throws Exception{
			BisCheckingBook bisCheckingBook=bisCheckingBookService.find("codeNum",obj.getCodeNum());
			logger.info("bisCheckingBook1:"+ JSON.toJSONString(bisCheckingBook));
			List<Map<String,Object>> gethead=bisCheckingBookService.getRepCheckingBookHead11(obj.getCodeNum());
			logger.info("gethead1:"+ JSON.toJSONString(gethead));
    		List<Map<String,Object>> getlist=bisCheckingBookService.getCheckingBookInfos(obj.getCodeNum(),gethead,obj.getnType()+"",obj.getType());
			logger.info("getlist1:"+ JSON.toJSONString(getlist));
    		Map<String,Object> hsMap=new HashMap<String,Object>();
    		if(1==obj.getnType()||3==obj.getnType()){
    			hsMap=bisCheckingBookService.getBookRows(obj.getCodeNum(),gethead,obj.getnType()+"",obj.getType());
				logger.info("hsMap1:"+ JSON.toJSONString(hsMap));
    		}
			if(null==getlist||getlist.size()==0){
				return;
			}
			//2025-10-24 注释
//    		String formatFileName = URLEncoder.encode("应收对账单" +".xls","UTF-8");
			//2025-10-24 新增
//			String name = bisCheckingBook.getCustom()+bisCheckingBook.getYearMonth().split("-")[1];
			String name = bisCheckingBook.getCustom()+bisCheckingBook.getYearMonth()+"月对账单";
    		String formatFileName = URLEncoder.encode(name +".xls","UTF-8");
        	ExcelUtil excelUtil=new ExcelUtil();
        	String  filePath=PropertiesUtil.getPropertiesByName("filepath", "application");
        	String srcPath =null; 
        	String desPath=null;
        	if(filePath==null || "".equals(filePath)){
        		filePath = request.getSession().getServletContext().getRealPath("/");
            	if(1==obj.getnType() || 2==obj.getnType()){
            		srcPath = filePath+"WEB-INF\\classes\\exceltemplate\\igrouprmb.xls";
            	}else if(3==obj.getnType() || 4==obj.getnType()){
            		srcPath = filePath+"WEB-INF\\classes\\exceltemplate\\igrouprmbe.xls";
            	} 
            	desPath = filePath+"WEB-INF\\classes\\excelpost\\igrouprmb.xls";
        	}else{
        		if(1==obj.getnType() || 2==obj.getnType()){
            		srcPath = filePath+"exceltemplate\\igrouprmb.xls";
            	}else if(3==obj.getnType() || 4==obj.getnType()){
            		srcPath = filePath+"exceltemplate\\igrouprmbe.xls";
            	} 
            	desPath =  filePath+"excelpost\\igrouprmb.xls";
        	}
        	excelUtil.setSrcPath(srcPath);
        	excelUtil.setDesPath(desPath);
        	excelUtil.setSheetName("Sheet1");
            int cols=(null!=obj.getType()&&!"".equals(obj.getType()))?(10+gethead.size()+1):(9+gethead.size()+1);
        	excelUtil.getSheet(cols);//总列数
        	int nLrow=9;
        	//判断是不是有箱号跟lot号
        	if(null!=obj.getType()&&!"".equals(obj.getType())){
        		if(1==obj.getnType() || 2==obj.getnType()){
        		  excelUtil.setCellStrValue(2,1,("1".equals(obj.getType()))?"柜号":"Lot");
        		  excelUtil.setCellStrValue(2,2,"入/出");
          		  excelUtil.setCellStrValue(2,3,"计费起始日期");
          		  excelUtil.setCellStrValue(2,4,"品名");
          		  excelUtil.setCellStrValue(2,5,"件数");
          		  excelUtil.setCellStrValue(2,6,"净重");
          		  excelUtil.setCellStrValue(2,7,"毛重");
          		  excelUtil.setCellStrValue(2,8,"在库天数");
          		  excelUtil.setCellStrValue(2,9,"截止日期");
        		}else{
        			excelUtil.setCellStrValue(2,1,("1".equals(obj.getType()))?"CtnNum":"Lot");
	        		excelUtil.setCellStrValue(2,2,"in/out");
	      		    excelUtil.setCellStrValue(2,3,"Date in");
	      		    excelUtil.setCellStrValue(2,4,"name");
	      		    excelUtil.setCellStrValue(2,5,"pcs");
	      		    excelUtil.setCellStrValue(2,6,"Net KG");
	      		    excelUtil.setCellStrValue(2,7,"Gross KG");
	      		    excelUtil.setCellStrValue(2,8,"storage days");
	      		    excelUtil.setCellStrValue(2,9,"Date out");
        		}
        		nLrow=10;
        	}
        	//循环费目（所谓的标题列）
        	if(gethead!=null && gethead.size()>0){
        		for(Map<String,Object> getMap:gethead){
        			//中文
        			if(1==obj.getnType() || 2==obj.getnType()){
        				if(!"1".equals(getMap.get("FEE_TYPE").toString())){
        					excelUtil.setCellStrValue(2,nLrow, (getMap.get("NAME_C")!=null?getMap.get("NAME_C").toString():"费目名称")+"("+(getMap.get("PRICE")!=null?getMap.get("PRICE").toString():"0")+")");
        				}else{
        					excelUtil.setCellStrValue(2,nLrow, (getMap.get("NAME_C")!=null?getMap.get("NAME_C").toString():"费目名称"));
        				}
        			}else{//英文
        				if(!"1".equals(getMap.get("FEE_TYPE").toString())){
        					excelUtil.setCellStrValue(2,nLrow, (getMap.get("NAME_E")!=null?getMap.get("NAME_E").toString():"fee name")+"("+(getMap.get("PRICE")!=null?getMap.get("PRICE").toString():"0")+")");
        				}else{
        					excelUtil.setCellStrValue(2,nLrow, (getMap.get("NAME_E")!=null?getMap.get("NAME_E").toString():"fee name"));
        				}
        			}
        			nLrow++;
        		}
        		if(1==obj.getnType() || 2==obj.getnType()){
        			excelUtil.setCellStrValue(2,nLrow,"总计RMB");
        		}else{
        			excelUtil.setCellStrValue(2,nLrow,"TotalRMB");
        		}
        	}
        	excelUtil.setCellStrValue2(0,2,bisCheckingBook.getYearMonth()+"对账单明细");
			excelUtil.setCellStrValue2(0,4,"对账客户："+bisCheckingBook.getCustom());
			int row=3;
			int hjrow=0;
        	//对应往里面放入值
			int col=9;
			int xjcols=9;
			double sumnum=0;//统计在库数量
			double sumnet=0;//统计在库净重
			double sumwet=0;//统计在库毛重 
			String bill_num="";
        	for (int i = 0; i < getlist.size(); i++) {
        		Map<String,Object> detailMap=getlist.get(i);
        		
        		//小计统计
        		if(row==hjrow){
					row++;
        		}
        		excelUtil.setCellStrValue(row,0,(detailMap.get("BILL_NUM")!=null?detailMap.get("BILL_NUM").toString():""));
        		if(null!=obj.getType()&&!"".equals(obj.getType())){
            		if("1".equals(obj.getType())){//箱号
    					excelUtil.setCellStrValue(row,1,(detailMap.get("CTN_NUM")!=null?detailMap.get("CTN_NUM").toString():""));
            		}else{//柜号
            			excelUtil.setCellStrValue(row,1,(detailMap.get("LOT_NUM")!=null?detailMap.get("LOT_NUM").toString():""));
            		}
            		excelUtil.setCellStrValue(row,2,(detailMap.get("CRK_SIGN")!=null?detailMap.get("CRK_SIGN").toString():""));
    				excelUtil.setCellStrValue(row,3,(detailMap.get("CHARGE_START_DATE")!=null?detailMap.get("CHARGE_START_DATE").toString():""));
    				excelUtil.setCellStrValue(row,4,(detailMap.get("CARGO_NAME")!=null?detailMap.get("CARGO_NAME").toString():""));
    			    excelUtil.setCellDoubleValue(row,5,Double.valueOf((detailMap.get("PIECE")!=null?detailMap.get("PIECE").toString():"0")));
    				excelUtil.setCellDoubleValue(row,6,Double.valueOf((detailMap.get("NET_WEIGHT")!=null?detailMap.get("NET_WEIGHT").toString():"0")));
    				excelUtil.setCellDoubleValue(row,7,Double.valueOf((detailMap.get("GROSS_WEIGHT")!=null?detailMap.get("GROSS_WEIGHT").toString():"0")));
    				excelUtil.setCellDoubleValue(row,8,Double.valueOf((detailMap.get("CHARGE_DAY")!=null?detailMap.get("CHARGE_DAY").toString():"0")));
    				excelUtil.setCellStrValue(row,9,(detailMap.get("CHARGE_END_DATE")!=null?detailMap.get("CHARGE_END_DATE").toString():""));
    				col=10;
            	}else{
					excelUtil.setCellStrValue(row,1,(detailMap.get("CRK_SIGN")!=null?detailMap.get("CRK_SIGN").toString():""));
    				excelUtil.setCellStrValue(row,2,(detailMap.get("CHARGE_START_DATE")!=null?detailMap.get("CHARGE_START_DATE").toString():""));
    				excelUtil.setCellStrValue(row,3,(detailMap.get("CARGO_NAME")!=null?detailMap.get("CARGO_NAME").toString():""));
    			    excelUtil.setCellDoubleValue(row,4,Double.valueOf((detailMap.get("PIECE")!=null?detailMap.get("PIECE").toString():"0")));
        		    excelUtil.setCellDoubleValue(row,5,Double.valueOf((detailMap.get("NET_WEIGHT")!=null?detailMap.get("NET_WEIGHT").toString():"0")));
        			excelUtil.setCellDoubleValue(row,6,Double.valueOf((detailMap.get("GROSS_WEIGHT")!=null?detailMap.get("GROSS_WEIGHT").toString():"0")));
    				excelUtil.setCellDoubleValue(row,7,Double.valueOf((detailMap.get("CHARGE_DAY")!=null?detailMap.get("CHARGE_DAY").toString():"0")));
    				excelUtil.setCellStrValue(row,8,(detailMap.get("CHARGE_END_DATE")!=null?detailMap.get("CHARGE_END_DATE").toString():""));
    				col=9;
            	}
            	if("在库".equals(detailMap.get("CRK_SIGN"))){
					sumnum+=Double.valueOf(detailMap.get("PIECE").toString());
					sumnet+=Double.valueOf(detailMap.get("NET_WEIGHT").toString());
					sumwet+=Double.valueOf(detailMap.get("GROSS_WEIGHT").toString());
				}

            	int sumCellindex = col;
            	for (int j = 0; j <gethead.size(); j++) {
					excelUtil.setCellDoubleValue(row,col,Double.valueOf((detailMap.get("KURMB"+j)!=null?detailMap.get("KURMB"+j).toString():"0")));
					col++;
				}
            	//2025-10-25 徐峥 注释
//				excelUtil.setCellDoubleValue(row,col,Double.valueOf((detailMap.get("SUMRMB")!=null?detailMap.get("SUMRMB").toString():"0")));
				//2025-10-25 徐峥 新增
				excelUtil.setCellSumValue(row,col,getCellName((row+1),(sumCellindex-1)),getCellName((row+1),(col-1)));
				if((1==obj.getnType()||3==obj.getnType())){
					int count=Integer.parseInt(hsMap.get((detailMap.get("BILL_NUM")!=null?detailMap.get("BILL_NUM").toString():"")).toString());
					if(!bill_num.equals((detailMap.get("BILL_NUM")!=null?detailMap.get("BILL_NUM").toString():""))){
						hjrow=(row+count);
						excelUtil.setCellStrValue(hjrow,0,(1 == obj.getnType() || 2 == obj.getnType()) ? "小计 ：" : "SubTotal：");
						if (null != obj.getType() && !"".equals(obj.getType())) {
							excelUtil.setCellStrValue(hjrow,1," ");
							excelUtil.setCellStrValue(hjrow,2," ");
							excelUtil.setCellStrValue(hjrow,3," ");
							excelUtil.setCellStrValue(hjrow,4," ");
							excelUtil.setCellSumIfValue(hjrow,5,(row+1)+"",hjrow+"",getCellName((row+1),5),getCellName(hjrow,5),obj.getnType().toString(),obj.getType());
							excelUtil.setCellSumIfValue(hjrow,6,(row+1)+"",hjrow+"",getCellName((row+1),6),getCellName(hjrow,6),obj.getnType().toString(),obj.getType());
							excelUtil.setCellSumIfValue(hjrow,7,(row+1)+"",hjrow+"",getCellName((row+1),7),getCellName(hjrow,7),obj.getnType().toString(),obj.getType());
							excelUtil.setCellStrValue(hjrow,8," ");
							excelUtil.setCellStrValue(hjrow,9," ");
							xjcols = 10;
						} else {
							excelUtil.setCellStrValue(hjrow,1," ");
							excelUtil.setCellStrValue(hjrow,2," ");
							excelUtil.setCellStrValue(hjrow,3," ");
							excelUtil.setCellSumIfValue(hjrow,4,(row+1)+"",hjrow+"",getCellName((row+1),4),getCellName(hjrow,4),obj.getnType().toString(),obj.getType());
							excelUtil.setCellSumIfValue(hjrow,5,(row+1)+"",hjrow+"",getCellName((row+1),5),getCellName(hjrow,5),obj.getnType().toString(),obj.getType());
							excelUtil.setCellSumIfValue(hjrow,6,(row+1)+"",hjrow+"",getCellName((row+1),6),getCellName(hjrow,6),obj.getnType().toString(),obj.getType());
							excelUtil.setCellStrValue(hjrow,7," ");
							excelUtil.setCellStrValue(hjrow,8," ");
							xjcols = 9;
						}
						for (int j = 0; j < gethead.size(); j++) {
							excelUtil.setCellSumValue(hjrow,xjcols,getCellName((row+1),xjcols),getCellName(hjrow,xjcols));
							xjcols++;
						}
						excelUtil.setCellSumValue(hjrow,xjcols,getCellName((row+1),xjcols),getCellName(hjrow,xjcols));
	        		}
				}
				bill_num=(detailMap.get("BILL_NUM")!=null?detailMap.get("BILL_NUM").toString():"");
				row++;
        	}
        	if((1==obj.getnType()||3==obj.getnType())){
        	  row++;
        	}
	        ///////////////////////////////////////下面空白列//////////////////////////////////
        	col++;
        	for (int i = 0; i <col; i++) {
        		 excelUtil.setCellStrValue(row,i," ");
            	 excelUtil.setCellStrValue(row+1,i," ");
			}
        	//////////////////////////竖向合计////////////////////////////////////////
        	int sumcol=9;
    		excelUtil.setCellStrValue(row+2,0,(1==obj.getnType() || 2==obj.getnType())?"合计 ：":"sum:");
    		if(null!=obj.getType()&&!"".equals(obj.getType())){
    			excelUtil.setCellStrValue(row+2,1," ");
	    		excelUtil.setCellStrValue(row+2,2," ");
	    		excelUtil.setCellStrValue(row+2,3," ");
				excelUtil.setCellStrValue(row+2,4,(1==obj.getnType() || 2==obj.getnType())?"结余":"Balance");
				excelUtil.setCellDoubleValue(row+2,5,sumnum); 
				excelUtil.setCellDoubleValue(row+2,6,sumnet); 
				excelUtil.setCellDoubleValue(row+2,7,sumwet); 
				excelUtil.setCellStrValue(row+2,8," ");
				excelUtil.setCellStrValue(row+2,9," ");
				sumcol=10;
    		}else{
	    		excelUtil.setCellStrValue(row+2,1," ");
	    		excelUtil.setCellStrValue(row+2,2," ");
				excelUtil.setCellStrValue(row+2,3,(1==obj.getnType() || 2==obj.getnType())?"结余":"Balance");
				excelUtil.setCellDoubleValue(row+2,4,sumnum); 
				excelUtil.setCellDoubleValue(row+2,5,sumnet); 
				excelUtil.setCellDoubleValue(row+2,6,sumwet); 
				excelUtil.setCellStrValue(row+2,7," ");
				excelUtil.setCellStrValue(row+2,8," ");
				sumcol=9;
    		}
    		if((1==obj.getnType()||3==obj.getnType())){
				for (int j = 0; j <gethead.size(); j++) {
					excelUtil.setCellSumIfTotal(row+2,sumcol,4+"",(row+2)+"",getCellName(4,sumcol),getCellName((row+2),sumcol),obj.getnType().toString());
					sumcol++;
				}
				excelUtil.setCellSumIfTotal(row+2,sumcol,4+"",(row+2)+"",getCellName(4,sumcol),getCellName((row+2),sumcol),obj.getnType().toString());
    		}else{
    			for (int j = 0; j <gethead.size(); j++) {
    				excelUtil.setCellSumValue(row+2,sumcol,this.getCellName(4,sumcol),this.getCellName(row,sumcol));
    				sumcol++;
    			}
    		    excelUtil.setCellSumValue(row+2,sumcol,this.getCellName(4,sumcol),this.getCellName(row,sumcol));
    		}
			////////////////////////////////////////////////////////////////////////
    		if(1==obj.getnType() || 2==obj.getnType()){
	        	excelUtil.setCellStrValue2(row+5,0, "如无问题，请尽快安排付款。谢谢！");
	        	excelUtil.setCellStrValue2(row+6,0, "公司名称：青岛港怡之航冷链物流有限公司");
	        	StringBuffer sb=new StringBuffer();
	        	sb.append("制单人：");
	        	sb.append(UserUtil.getCurrentUser()!=null?UserUtil.getCurrentUser().getName():"");
	        	excelUtil.setCellStrValue2(row+6,3,sb.toString());
	        	excelUtil.setCellStrValue2(row+6,5, "审核人：");
	        	excelUtil.setCellStrValue2(row+7,0, "人民币账户：802790200002726");
	        	excelUtil.setCellStrValue2(row+8,0, "开户行名称：青岛银行");
	        	excelUtil.setCellStrValue2(row+9,0, "纳税人识别号：91370220395949850B");
	        	excelUtil.setCellStrValue2(row+10,0, "地  址：山东省青岛市保税区北京路41号203房间");
	        	excelUtil.setCellStrValue2(row+11,0, "电  话：0532-82987866");
    		}else{
    			StringBuffer sb=new StringBuffer();
	        	sb.append("SINGLE PERSON：");
	        	sb.append(UserUtil.getCurrentUser()!=null?UserUtil.getCurrentUser().getName():"");
	        	excelUtil.setCellStrValue2(row+5,0, "BANK INFORMATION:");
	        	excelUtil.setCellStrValue2(row+6,0, "BANK NAME：BANK Of QingDao");
	        	excelUtil.setCellStrValue2(row+6,3,sb.toString());
	        	excelUtil.setCellStrValue2(row+6,5, "AUDITOR：");
	        	excelUtil.setCellStrValue2(row+7,0, "BANK ADDRESS：NO.68 Xianggang Zhong Road,Qingdao,266071,CHINA");
	        	excelUtil.setCellStrValue2(row+8,0, "BENEFICIARY：QINGDAO PORT EIMSKIP COLDCHAIN LOGISTICS CO.,LTD");
	        	excelUtil.setCellStrValue2(row+9,0, "Account(USD)：802561200018887");
	        	excelUtil.setCellStrValue2(row+10,0, "SWIFT CODE：QCCBCNBQXXX");
    		}
        	excelUtil.exportToNewFile();
		    FileInputStream in = new FileInputStream(new File(desPath));
		    int len = 0;
		    byte[] buffer = new byte[1024];
		    response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
		    response.setContentType("application/msexcel");// 定义输出类型
		    OutputStream out = response.getOutputStream();
		    while((len = in.read(buffer)) > 0) {
		       out.write(buffer,0,len);
		    }
		    if(null != in){
		       in.close();
		    }
		    if(null != out){
		       out.close();
		    }	
	}
		 
		//excel导出对账单明细
		@RequestMapping(value = "report")
		@ResponseBody
		public void export(@Valid @ModelAttribute @RequestBody BisCheckingBook obj,HttpServletRequest request, HttpServletResponse response) throws Exception{
	    	if(obj.getCodeNum()!=null && !"".equals(obj.getCodeNum()) && obj.getnType()!=null && obj.getnType()>0){
	    		List<Map<String,Object>> getlist=bisCheckingBookService.getRepCheckingBookInfo(obj.getCodeNum(),(obj.getnType()==1||obj.getnType()==3)?"1":"2");
	    		List<Map<String,Object>> gethead=bisCheckingBookService.getRepCheckingBookHead11(obj.getCodeNum());
	    		//将非仓储费用的条目插入getlist当中，其中入库的排在在库后面，出库的按提单号跟随
	    		int chuku=-1;//记录第一个出库条目出现的位置
	    		List<Map<String,Object>> newList=bisCheckingBookService.getRepCheckingBookInfo11(obj.getCodeNum());
	    			if(!newList.isEmpty()&&getlist.isEmpty()){
	    				getlist.addAll(newList);
	    			}else if(!newList.isEmpty()){
	    				int size=getlist.size();
	    				for(int i=0;i<size;i++){
	    					Map<String,Object> getSql=getlist.get(i);
	    					//找到getList第一个不为在库的条目，将newList中入库部分插入到它的前面
	    					if(!getSql.get("CRK_SIGN").equals("在库")){
	    						chuku=i;
	    						//结束外部for循环
								i=size;
	    						int newSize=newList.size();
	    						for(int j=0;j<newSize;j++){
	    							if(newList.get(j).get("NTYPE").equals("2")){
	    								List<Map<String,Object>> inputList=new ArrayList<Map<String,Object>>();
	    								//将所有入库的条目放入inputList当中
	    								for(int k=0;k<j;k++){
	    									inputList.add(newList.get(k));
	    								}
	    								if(!inputList.isEmpty()){
	    									getlist.addAll(chuku,inputList);
	    								}
	    								if(j!=0){
	    									newList.subList(0, j).clear();
	    								}
	    								chuku += j;
	    								break;
	    							}else if(j==newSize-1){
	    								//newList中的条目均为入库条目
	    								getlist.addAll(chuku,newList);
	    								newList.clear();
	    								chuku += newSize;
	    								break;
	    							}
	    						}
	    					}//end if
	    				}//end for 
	    				//将剩余的出库的条目按提单号和出库联系单号跟随插入getlist中
	    				if(!newList.isEmpty()){
	    					//chuku为-1，则表明getlist中均为在库数据，此时出库数据直接插入到最后
	    					if(chuku<0){
	    						getlist.addAll(getlist.size(),newList);
	    					}else{
		    					int cc=0;
		    					for(Map<String,Object> chuMap:newList){
		    						int listSize=getlist.size();
		    						for(int m=chuku;m<listSize;m++){
		    							if(null!=chuMap.get("BILL_NUM")&&null!=getlist.get(m).get("BILL_NUM") && chuMap.get("BILL_NUM").equals(getlist.get(m).get("BILL_NUM")) && chuMap.get("LINK_ID").equals(getlist.get(m).get("LINK_ID"))){
		    								if(m!=listSize-1){
		    									cc=1;
		    								}else{
		    									getlist.add(chuMap);
		    									cc=0;
		    								}
		    							}else if(cc==1&&null!=chuMap.get("BILL_NUM")&&null!=getlist.get(m).get("BILL_NUM") && (!chuMap.get("BILL_NUM").equals(getlist.get(m).get("BILL_NUM")) || !chuMap.get("LINK_ID").equals(getlist.get(m).get("LINK_ID")))){
		    								getlist.add(m, chuMap);
		    								cc=0;
		    								break;
		    							}else if(m==listSize-1){
		    								getlist.add(chuMap);
		    								cc=0;
		    							}
		    						}//end for
		    					}//end for
	    					}
	    				}
	    			}

	    		String formatFileName = URLEncoder.encode("应收对账单" +".xls","UTF-8");
	        	ExcelUtil excelUtil=new ExcelUtil();
	        	String  filePath=PropertiesUtil.getPropertiesByName("filepath", "application");
	        	String srcPath =null; 
	        	String desPath=null;
	        	if(filePath==null || "".equals(filePath)){
	        		filePath = request.getSession().getServletContext().getRealPath("/");
	            	if(1==obj.getnType() || 2==obj.getnType()){
	            		srcPath = filePath+"WEB-INF\\classes\\exceltemplate\\igrouprmb.xls";
	            	}else if(3==obj.getnType() || 4==obj.getnType()){
	            		srcPath = filePath+"WEB-INF\\classes\\exceltemplate\\igrouprmbe.xls";
	            	} 
	            	desPath = filePath+"WEB-INF\\classes\\excelpost\\igrouprmb.xls";
	        	}else{
	        		if(1==obj.getnType() || 2==obj.getnType()){
	            		srcPath = filePath+"exceltemplate\\igrouprmb.xls";
	            	}else if(3==obj.getnType() || 4==obj.getnType()){
	            		srcPath = filePath+"exceltemplate\\igrouprmbe.xls";
	            	} 
	            	desPath =  filePath+"excelpost\\igrouprmb.xls";
	        	}
	        	excelUtil.setSrcPath(srcPath);
	        	excelUtil.setDesPath(desPath);
	        	excelUtil.setSheetName("Sheet1");
	        	excelUtil.getSheet(9+gethead.size()+1);//总列数
	        	int nLrow=9;
	        	//循环费目（所谓的标题列）
	        	if(gethead!=null && gethead.size()>0){
	        		for(Map<String,Object> getMap:gethead){
	        			//中文
	        			if(1==obj.getnType() || 2==obj.getnType()){
	        				if(!"1".equals(getMap.get("FEE_TYPE").toString())){
	        					excelUtil.setCellStrValue(2,nLrow, (getMap.get("NAME_C")!=null?getMap.get("NAME_C").toString():"费目名称")+"("+(getMap.get("PRICE")!=null?getMap.get("PRICE").toString():"0")+")");
	        				}else{
	        					excelUtil.setCellStrValue(2,nLrow, (getMap.get("NAME_C")!=null?getMap.get("NAME_C").toString():"费目名称"));
	        				}
	        			}else{//英文
	        				if(!"1".equals(getMap.get("FEE_TYPE").toString())){
	        					excelUtil.setCellStrValue(2,nLrow, (getMap.get("NAME_E")!=null?getMap.get("NAME_E").toString():"fee name")+"("+(getMap.get("PRICE")!=null?getMap.get("PRICE").toString():"0")+")");
	        				}else{
	        					excelUtil.setCellStrValue(2,nLrow, (getMap.get("NAME_E")!=null?getMap.get("NAME_E").toString():"fee name"));
	        				}
	        			}
	        			getMap.put("nlrow",nLrow);//添加该费目类在excel中是第几列
	        			getMap.put("hrowsum",0);//添加竖向金额计算和
	        			nLrow++;
	        		}
	        		if(1==obj.getnType() || 2==obj.getnType()){
	        			excelUtil.setCellStrValue(2,nLrow,"总计RMB");
	        		}else{
	        			excelUtil.setCellStrValue(2,nLrow,"TotalRMB");
	        		}
	        	}
	        	int hs=nLrow+1;
	        	excelUtil.setCellStrValue(2,hs, "是否货损");
	        	///////////////////////处理中间动态列赋值/////////////////////////////
	        	int myNum=0;//记录行数
	        	int addRow=0;
	        	int oldheadindex = -1;
	        	if(getlist!=null && getlist.size()>0){
	        		Map<String,Object> map=null;
	        		Double[] allInfo = new Double[gethead.size()];
	        		String billNum=null;
	        		Integer[] getRowNum=null;
	        		Map<String ,Object> keyMap=new HashMap<String ,Object>();//保存分组唯一表示和行数
	        		String keys=null;//记录分组唯一标示
	        		String oldKeys="";
	        		boolean isHave=false;
	        		Double sunRMB=0d;//记录横向总计RMB
	        		Double sunLRMB=0d;//记录竖向总计RMB
	         		Double allRMB=0d;
	         		List<String> objList = new ArrayList<String>();//最后记录所有需要总计sum的单元格
	         		Integer begin = 0; //开始单元格行数（放入objList中）
	         		Integer end = 0; //结束单元格行数（放入objList中）
	         		int nextCR=0;//记录上一条记录是否为入库1位入库0为不入库
	         		int repeat=0;//记录是否与上一条的key值是否想等0相等1是不等
	         		String repeatCargo="";//记录上一条的货物名称
	         		Double repeatPiece=0d;//记录上一条数量
	         		Double repeatNet=0d; //记录上一条净重
	         		Double repeatGross=0d; //记录上一条毛重
	         		Double repeatRmb=0d; //记录上一条费用
	         		//******************************************************************//
	        		for(int i=0;i<getlist.size();i++){
	        			repeat=0;
	        			map=getlist.get(i);
	        			if(0==i){
	        				String billDate=map.get("BILL_DATE")!=null?map.get("BILL_DATE").toString():"";
	        				String[] billDatelist=billDate.split("-");
	        				if(3==billDatelist.length){
	        					billDate=billDatelist[0]+"-"+billDatelist[1];
	        				}
	        				excelUtil.setCellStrValue2(0,2,billDate+"对账单明细");
	        				excelUtil.setCellStrValue2(0,4,"对账客户："+(map.get("UNAME")!=null?map.get("UNAME").toString():""));
							String name = (map.get("UNAME")!=null?map.get("UNAME").toString():"")+billDate+"月对账单";
							formatFileName = URLEncoder.encode(name +".xls","UTF-8");
	        			}
	        			//获取分组唯一标示
	        			keys=map.get("KEYS").toString();
	                    /*************************************/
	        			if(keyMap.get(keys)!=null || keys.split(":").length<3){
	        				if(oldKeys.split(":").length<3 && !keys.equals(oldKeys)){
		        				keyMap.put(keys,1);
			        			isHave=false;
			        			if(myNum>0){
			        				excelUtil.setCellSumValue(myNum+3+addRow-1,nLrow,this.getCellName(myNum+3+addRow,9),this.getCellName(myNum+3+addRow,nLrow-1));
			        			}
	        				}else if(oldKeys.split(":").length>3 ){
	        					if(oldKeys.equals(keys)){
	        						myNum--;
				        			isHave=true;
				        			repeat=1;
	        					}else if(map.get("NTYPE").toString().equals("1") || !(oldKeys.split(":"))[0].equals((keys.split(":"))[0]) ){
		        					keyMap.put(keys,1);
			        				isHave=false;
			        				if(myNum>0){
			        					excelUtil.setCellSumValue(myNum+3+addRow-1,nLrow,this.getCellName(myNum+3+addRow, 9),this.getCellName(myNum+3+addRow, nLrow-1));
			        				}
	        					}else{
	        						myNum--;
				        			isHave=true;
	        					}
	        				}else{
	        					if(nextCR==1 && !map.get("FEE_NAME").equals("仓储") && keys.split(":").length<3 && map.get("NTYPE").equals("1")&&!(oldKeys.split(":"))[0].equals((keys.split(":"))[0])){
	        						keyMap.put(keys,1);
			        				isHave=false;
			        				if(myNum>0){
			        					excelUtil.setCellSumValue(myNum+3+addRow-1,nLrow,this.getCellName(myNum+3+addRow, 9),this.getCellName(myNum+3+addRow, nLrow-1));
			        				}
	        					}else{
			        				myNum--;
				        			isHave=true;
	        					}
	        				}
	        			}else if(keyMap.get(keys)==null){
	        				keyMap.put(keys,1);
	        				isHave=false;
	        				if(myNum>0){
	        					excelUtil.setCellSumValue(myNum+3+addRow-1,nLrow,this.getCellName(myNum+3+addRow, 9),this.getCellName(myNum+3+addRow, nLrow-1));
	        				}
	        			}
	        			/*************************************/
	        			oldKeys = keys;
	        			//当上一条获取的提单号和当前获取提单号不一致时添加小计
	        			if((1==obj.getnType()||3==obj.getnType()) && billNum!=null && !billNum.equals(map.get("BILL_NUM")!=null?map.get("BILL_NUM").toString().trim():"") ){
	        				if(0==begin){
	        					begin=4;
		        			}
	        				end=myNum+3+addRow;
 	        				excelUtil.setCellStrValue(myNum+3+addRow,0,"小计 ：");
 	        				excelUtil.setCellStrValue(myNum+3+addRow,1," ");
 	        				excelUtil.setCellStrValue(myNum+3+addRow,2," ");
	        				excelUtil.setCellStrValue(myNum+3+addRow,3," ");
	        				excelUtil.setCellSumValue2(myNum+3+addRow,4,this.getCellName(begin, 4),this.getCellName(end, 4),begin.toString(),end.toString(),this.getCellName(begin, hs),this.getCellName(end, hs),obj.getnType().toString(),obj.getType()); 
	        				excelUtil.setCellSumValue2(myNum+3+addRow,5,this.getCellName(begin, 5),this.getCellName(end, 5),begin.toString(),end.toString(),this.getCellName(begin, hs),this.getCellName(end, hs),obj.getnType().toString(),obj.getType()); 
	        				excelUtil.setCellSumValue2(myNum+3+addRow,6,this.getCellName(begin, 6),this.getCellName(end, 6),begin.toString(),end.toString(),this.getCellName(begin, hs),this.getCellName(end, hs),obj.getnType().toString(),obj.getType()); 
		        			excelUtil.setCellStrValue(myNum+3+addRow,7," ");
		        			excelUtil.setCellStrValue(myNum+3+addRow,8," ");
		        			int j=0;
		        			for(Map<String,Object> gmap:gethead){
		        				excelUtil.setCellSumValue(myNum+3+addRow,Integer.valueOf(gmap.get("nlrow").toString()),this.getCellName(begin, Integer.valueOf(gmap.get("nlrow").toString())),this.getCellName(end, Integer.valueOf(gmap.get("nlrow").toString())));
		        				sunLRMB+=Double.valueOf(gmap.get("hrowsum").toString());
		        				if(allInfo[j]==null){
		        					allInfo[j] =Double.valueOf(gmap.get("hrowsum").toString());
		        				}else{
		        					allInfo[j] +=Double.valueOf(gmap.get("hrowsum").toString());
		        				}
		        				j++;
		        				gmap.put("hrowsum",0);//重置竖向金额计算和
		        			}
		        			//添加小计竖向计算总和
		        			allRMB += sunLRMB;
		        			excelUtil.setCellSumValue(myNum+3+addRow,nLrow,this.getCellName(begin, nLrow),this.getCellName(end, nLrow));
		        			objList.add(begin.toString());
		        			objList.add(end.toString());
		        			begin = end+2;
	        				addRow++;
	        				sunLRMB=0d;
	        			}
	        			//当行唯一标记在缓存map中不存在时添加一新行。如果存在则添加费目内容。
	        			if(false==isHave ){
		        			billNum=map.get("BILL_NUM")!=null?map.get("BILL_NUM").toString():"";
		        			excelUtil.setCellStrValue(myNum+3+addRow,0, map.get("BILL_NUM")!=null?map.get("BILL_NUM").toString():"");
		        			if(1==obj.getnType() || 2==obj.getnType()){
		        				excelUtil.setCellStrValue(myNum+3+addRow,1, map.get("CRK_SIGN")!=null?map.get("CRK_SIGN").toString():(map.get("NTYPE").toString().equals("1")?"入库":"出"));
		        				if(map.get("NTYPE").equals("1")){
		        					nextCR=1;
		        				}else{
		        					nextCR=0;
		        				}
		        			}else{
		        				excelUtil.setCellStrValue(myNum+3+addRow,1, getIOType( map.get("CRK_SIGN")!=null?map.get("CRK_SIGN").toString():(map.get("NTYPE").toString().equals("1")?"入库":"出") ));
		        				if(map.get("NTYPE").equals("1")){
		        					nextCR=1;
		        				}else{
		        					nextCR=0;
		        				}
		        			}
		        			excelUtil.setCellStrValue(myNum+3+addRow,2, map.get("CHARGE_START_DATE")!=null?map.get("CHARGE_START_DATE").toString():"");
		        			excelUtil.setCellStrValue(myNum+3+addRow,3, map.get("CARGO_NAME")!=null?map.get("CARGO_NAME").toString():"");
		        			repeatCargo=map.get("CARGO_NAME")!=null?map.get("CARGO_NAME").toString():"";
		        			excelUtil.setCellDoubleValue(myNum+3+addRow,4, Double.valueOf(map.get("PIECE")!=null?map.get("PIECE").toString():"0").longValue());
		        			repeatPiece=Double.valueOf(map.get("PIECE")!=null?map.get("PIECE").toString():"0");
		        			excelUtil.setCellDoubleValue(myNum+3+addRow,5, Double.valueOf(map.get("NET_WEIGHT")!=null?map.get("NET_WEIGHT").toString():"0.00"));
		        			repeatNet=Double.valueOf(map.get("NET_WEIGHT")!=null?map.get("NET_WEIGHT").toString():"0.00");
		        			excelUtil.setCellDoubleValue(myNum+3+addRow,6, Double.valueOf(map.get("GROSS_WEIGHT")!=null?map.get("GROSS_WEIGHT").toString():"0.00"));
		        			repeatGross=Double.valueOf(map.get("GROSS_WEIGHT")!=null?map.get("GROSS_WEIGHT").toString():"0.00");
		        			excelUtil.setCellDoubleValue(myNum+3+addRow,7, Double.valueOf(map.get("CHARGE_DAY")!=null?map.get("CHARGE_DAY").toString():"0"));
		        			excelUtil.setCellStrValue(myNum+3+addRow,8, map.get("CHARGE_END_DATE")!=null?map.get("CHARGE_END_DATE").toString():"");
		        			excelUtil.setCellDoubleValue(myNum+3+addRow,hs, map.get("HS")!=null?Integer.valueOf(map.get("HS").toString()):0);
		        			//费目明细金额填充0
		        			for(int n=9;n<nLrow;n++){
		        				excelUtil.setCellDoubleValue(myNum+3+addRow,n,0);
		        			}
		        			sunRMB=0d;
//		        			//只计算类型为在库的数量
//		        			if("3".equals(map.get("STORAGE_STATE")!=null?map.get("STORAGE_STATE").toString().trim():"")){
//		        				sumPiece=sumPiece+Double.valueOf(map.get("PIECE").toString());
//		        				sumNet=sumNet+Double.valueOf(map.get("NET_WEIGHT").toString());
//		        				sumSross=sumSross+Double.valueOf(map.get("GROSS_WEIGHT").toString());
//		        			}
	        			}
	        			if(repeat==1){
	        				int ifadd=0;
	        				for(String cargo:repeatCargo.split(",") ){
	        					if((map.get("CARGO_NAME")!=null?map.get("CARGO_NAME").toString():"").equals(cargo)){
	        						ifadd=1;
	        						break;
	        					}
	        				}
	        				if(ifadd==0){
	        					excelUtil.setCellStrValue(myNum+3+addRow,3, repeatCargo+","+map.get("CARGO_NAME")!=null?map.get("CARGO_NAME").toString():"");
	        					repeatCargo = repeatCargo+","+(map.get("CARGO_NAME")!=null?map.get("CARGO_NAME").toString():"");
	        				}
		        			excelUtil.setCellDoubleValue(myNum+3+addRow,4, repeatPiece+Double.valueOf(map.get("PIECE")!=null?map.get("PIECE").toString():"0").longValue());
		        			repeatPiece += Double.valueOf(map.get("PIECE")!=null?map.get("PIECE").toString():"0");
		        			excelUtil.setCellDoubleValue(myNum+3+addRow,5, repeatNet+Double.valueOf(map.get("NET_WEIGHT")!=null?map.get("NET_WEIGHT").toString():"0.00"));
		        			repeatNet += Double.valueOf(map.get("NET_WEIGHT")!=null?map.get("NET_WEIGHT").toString():"0.00");
		        			excelUtil.setCellDoubleValue(myNum+3+addRow,6, repeatGross+Double.valueOf(map.get("GROSS_WEIGHT")!=null?map.get("GROSS_WEIGHT").toString():"0.00"));
		        			repeatGross += Double.valueOf(map.get("GROSS_WEIGHT")!=null?map.get("GROSS_WEIGHT").toString():"0.00");
		        			excelUtil.setCellDoubleValue(myNum+3+addRow,getRowNum[0], repeatRmb+Double.valueOf(map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0.00"));
		        			repeatRmb +=Double.valueOf(map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0.00");
	        			}
	        			//填充费目金额
	        			getRowNum=getNum(gethead,map.get("LAB")!=null?map.get("LAB").toString():"");
	        			if(getRowNum==null){
	        				System.out.println("cccccccccccc");
	        				//continue;
	        			}
	        			int getheadindex = getRowNum[1];
	        			if(oldheadindex == -1){
	        				oldheadindex = getheadindex;
	        			}
	        			if(getRowNum!=null){
	        				/*判断本条明细数据 map(getlist集合的元素）和前一条明细map是否在gethead对应同一条费目。
	        				费目相同累加金额；费目不同 不能直接累加 需要先清空前费目的sunRMB值。*/
	        				if(oldheadindex == getheadindex){
	        					//添加横向计算总和
		        				sunRMB+=Double.valueOf(map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0");
		        				oldheadindex = getheadindex;
	        				}else{
	        					sunRMB = 0d;
	        					//添加横向计算总和
		        				sunRMB+=Double.valueOf(map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0");
		        				oldheadindex = getheadindex;
	        				}
	        				if(repeat!=1){
	        					//yhn20171107
	        					excelUtil.setCellDoubleValue(myNum+3+addRow,getRowNum[0], sunRMB);
	        					repeatRmb=Double.valueOf(map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0.00");
	        				}
	        				//添加竖向计算总和
	        				gethead.get(getRowNum[1]).put("hrowsum", Double.valueOf(gethead.get(getRowNum[1]).get("hrowsum").toString())+Double.valueOf(map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0"));
	        			}
	        			//如果是是最后记录添加小计
	        			if((1==obj.getnType()||3==obj.getnType()) && i==getlist.size()-1){
	        				if(0==begin){
	        					begin=4;
	        				}
	        				end=myNum+3+addRow+1;
	        				excelUtil.setCellStrValue(myNum+3+addRow+1,0,"小计 ：");
	        				excelUtil.setCellStrValue(myNum+3+addRow+1,1," ");
	        				excelUtil.setCellStrValue(myNum+3+addRow+1,2," ");
	        				excelUtil.setCellStrValue(myNum+3+addRow+1,3," ");
//	        				excelUtil.setCellDoubleValue(myNum+3+addRow+1,4, sumPiece.longValue());
//	        				excelUtil.setCellDoubleValue(myNum+3+addRow+1,5, sumNet);
//		        			excelUtil.setCellDoubleValue(myNum+3+addRow+1,6, sumSross);
		        			excelUtil.setCellSumValue2(myNum+3+addRow+1,4,this.getCellName(begin, 4),this.getCellName(end, 4),begin.toString(),end.toString(),this.getCellName(begin, hs),this.getCellName(end, hs),obj.getnType().toString(),obj.getType()); 
	        				excelUtil.setCellSumValue2(myNum+3+addRow+1,5,this.getCellName(begin, 5),this.getCellName(end, 5),begin.toString(),end.toString(),this.getCellName(begin, hs),this.getCellName(end, hs),obj.getnType().toString(),obj.getType()); 
	        				excelUtil.setCellSumValue2(myNum+3+addRow+1,6,this.getCellName(begin, 6),this.getCellName(end, 6),begin.toString(),end.toString(),this.getCellName(begin, hs),this.getCellName(end, hs),obj.getnType().toString(),obj.getType()); 
		        			excelUtil.setCellStrValue(myNum+3+addRow+1,7," ");
		        			excelUtil.setCellStrValue(myNum+3+addRow+1,8,"");
//		        			allPiece+=sumPiece;
//		        			allNet+=sumNet;
//		        			allGross+=sumSross;
//		        			excelUtil.setCellDoubleValue(myNum+3+addRow,nLrow,sunRMB);
		        			excelUtil.setCellSumValue(myNum+3+addRow,nLrow,this.getCellName(myNum+3+addRow+1, 9),this.getCellName(myNum+3+addRow+1, nLrow-1));
		        			int jj=0;
		        			for(Map<String,Object> gmap:gethead){
//		        				excelUtil.setCellDoubleValue(myNum+3+addRow+1,Integer.valueOf(gmap.get("nlrow").toString()),Double.valueOf(gmap.get("hrowsum").toString()));
		        				excelUtil.setCellSumValue(myNum+3+addRow+1,Integer.valueOf(gmap.get("nlrow").toString()),this.getCellName(begin, Integer.valueOf(gmap.get("nlrow").toString())),this.getCellName(end, Integer.valueOf(gmap.get("nlrow").toString())));
		        				sunLRMB+=Double.valueOf(gmap.get("hrowsum").toString());
		        				if(allInfo[jj]==null){
		        					allInfo[jj] =Double.valueOf(gmap.get("hrowsum").toString());
		        				}else{
		        					allInfo[jj] +=Double.valueOf(gmap.get("hrowsum").toString());
		        				}
		        				jj++;
		        			}
		        			allRMB += sunLRMB;
		        			//添加小计竖向计算总和
//		        			excelUtil.setCellDoubleValue(myNum+3+addRow+1,nLrow,sunLRMB);
		        			excelUtil.setCellSumValue(myNum+3+addRow+1,nLrow,this.getCellName(begin, nLrow),this.getCellName(end, nLrow));
		        			objList.add(begin.toString());
		        			objList.add(end.toString());
	        			}
	        			if((2==obj.getnType()||4==obj.getnType()) && i==getlist.size()-1){
//	        				excelUtil.setCellDoubleValue(myNum+3+addRow,nLrow,sunRMB);
	        				excelUtil.setCellSumValue(myNum+3+addRow,nLrow,this.getCellName(myNum+3+addRow+1, 9),this.getCellName(myNum+3+addRow+1, nLrow-1));
	        				end=myNum+3+addRow+1;
	        			}
	        			myNum++;
	        		}//end for
	        		//******************************************************************//
	        		if((1==obj.getnType()||3==obj.getnType())){
	        			for(int kong=0;kong<nLrow+1;kong++){
	        				excelUtil.setCellStrValue(myNum+3+addRow+1,kong," ");
	        			}
	        			excelUtil.setCellStrValue(myNum+3+addRow+2,0,"合计 ：");
	        			excelUtil.setCellStrValue(myNum+3+addRow+2,1," ");
	        			excelUtil.setCellStrValue(myNum+3+addRow+2,2," ");
	        			excelUtil.setCellStrValue(myNum+3+addRow+2,3, "结余");
// 	        			excelUtil.setCellDoubleValue(myNum+3+addRow+2,4, allPiece);
//        				excelUtil.setCellDoubleValue(myNum+3+addRow+2,5, allNet);
// 	        			excelUtil.setCellDoubleValue(myNum+3+addRow+2,6, allGross);
 	        			excelUtil.setCellSumValue2(myNum+3+addRow+2, 4, objList,hs,obj.getnType().toString()); 
	        			excelUtil.setCellSumValue2(myNum+3+addRow+2, 5, objList,hs,obj.getnType().toString()); 
	        			excelUtil.setCellSumValue2(myNum+3+addRow+2, 6, objList,hs,obj.getnType().toString()); 
	        			excelUtil.setCellStrValue(myNum+3+addRow+2,7," ");
	        			excelUtil.setCellStrValue(myNum+3+addRow+2,8," ");
//	        			int k=0;
		        		for(Map<String,Object> gmaps:gethead){
//	        				excelUtil.setCellDoubleValue(myNum+3+addRow+2,Integer.valueOf(gmaps.get("nlrow").toString()),allInfo[k]);
	        				excelUtil.setCellSumValue(myNum+3+addRow+2, Integer.valueOf(gmaps.get("nlrow").toString()), objList,obj.getnType().toString());
//	        				k++;
	        			}
//		        		excelUtil.setCellDoubleValue(myNum+3+addRow+2,nLrow,allRMB);
		        		excelUtil.setCellSumValue(myNum+3+addRow+2, nLrow, objList,obj.getnType().toString());
		        	}else{
		        		for(int kong=0;kong<nLrow+1;kong++){
		        			excelUtil.setCellStrValue(myNum+3+addRow+0,kong," ");
	        				excelUtil.setCellStrValue(myNum+3+addRow+1,kong," ");
	        			}
	        				//excelUtil.setCellStrValue(myNum+3+addRow,getRowNum[0], map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0");
	        				//gethead.get(getRowNum[1]).put("hrowsum", Double.valueOf(gethead.get(getRowNum[1]).get("hrowsum").toString())+Double.valueOf(map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0"));
		        		excelUtil.setCellStrValue(myNum+3+addRow+2,0,"合计 ：");
		        		excelUtil.setCellStrValue(myNum+3+addRow+2,1," ");
		        		excelUtil.setCellStrValue(myNum+3+addRow+2,2," ");
	        			excelUtil.setCellStrValue(myNum+3+addRow+2,3, "结余");
	        			//excelUtil.setCellDoubleValue(myNum+3+addRow+2,4, sumPiece);
        				//excelUtil.setCellDoubleValue(myNum+3+addRow+2,5, sumNet);
	        			//excelUtil.setCellDoubleValue(myNum+3+addRow+2,6, sumSross);
	        			excelUtil.setCellSumValue2(myNum+3+addRow+2,4,this.getCellName(4, 4),this.getCellName(end, 4),"4",end.toString(),this.getCellName(4, hs),this.getCellName(end, hs),obj.getnType().toString(),obj.getType()); 
	        			excelUtil.setCellSumValue2(myNum+3+addRow+2, 5, this.getCellName(4, 5),this.getCellName(end, 5),"4",end.toString(), this.getCellName(4, hs),this.getCellName(end, hs),obj.getnType().toString(),obj.getType()); 
	        			excelUtil.setCellSumValue2(myNum+3+addRow+2, 6, this.getCellName(4, 6),this.getCellName(end, 6),"4",end.toString(), this.getCellName(4, hs),this.getCellName(end, hs),obj.getnType().toString(),obj.getType()); 
	        			excelUtil.setCellStrValue(myNum+3+addRow+2,7," ");
	        			excelUtil.setCellStrValue(myNum+3+addRow+2,8," ");
	        			for(Map<String,Object> gmaps:gethead){
// 	        				excelUtil.setCellDoubleValue(myNum+3+addRow+2,Integer.valueOf(gmaps.get("nlrow").toString()),Double.valueOf(gmaps.get("hrowsum").toString()));
	        				excelUtil.setCellSumValue(myNum+3+addRow+2,Integer.valueOf(gmaps.get("nlrow").toString()),this.getCellName(4, Integer.valueOf(gmaps.get("nlrow").toString())),this.getCellName(end, Integer.valueOf(gmaps.get("nlrow").toString())));
	        				allRMB +=Double.valueOf(gmaps.get("hrowsum").toString());
	        			}
//		        		excelUtil.setCellDoubleValue(myNum+3+addRow+2,nLrow,allRMB);
	        			excelUtil.setCellSumValue(myNum+3+addRow+2,nLrow,this.getCellName(4, nLrow),this.getCellName(end,nLrow));
		        	}
	        	}
	        	////////////////////////////////////////////////////////////////////////
        		if(1==obj.getnType() || 2==obj.getnType()){
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+5,0, "如无问题，请尽快安排付款。谢谢！");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+6,0, "公司名称：青岛港怡之航冷链物流有限公司");
    	        	StringBuffer sb=new StringBuffer();
    	        	sb.append("制单人：");
    	        	sb.append(UserUtil.getCurrentUser()!=null?UserUtil.getCurrentUser().getName():"");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+6,3,sb.toString());
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+6,5, "审核人：");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+7,0, "人民币账户：802790200002726");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+8,0, "开户行名称：青岛银行");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+9,0, "纳税人识别号：91370220395949850B");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+10,0, "地  址：山东省青岛市保税区北京路41号203房间");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+11,0, "电  话：0532-82987866");
        		}else{
        			StringBuffer sb=new StringBuffer();
    	        	sb.append("SINGLE PERSON：");
    	        	sb.append(UserUtil.getCurrentUser()!=null?UserUtil.getCurrentUser().getName():"");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+5,0, "BANK INFORMATION:");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+6,0, "BANK NAME：BANK Of QingDao");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+6,3,sb.toString());
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+6,5, "AUDITOR：");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+7,0, "BANK ADDRESS：NO.68 Xianggang Zhong Road,Qingdao,266071,CHINA");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+8,0, "BENEFICIARY：QINGDAO PORT EIMSKIP COLDCHAIN LOGISTICS CO.,LTD");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+9,0, "Account(USD)：802561200018887");
    	        	excelUtil.setCellStrValue2(myNum+3+addRow+10,0, "SWIFT CODE：QCCBCNBQXXX");
        		}	
	        	excelUtil.exportToNewFile();
	        	FileInputStream in = new FileInputStream(new File(desPath));
	        	int len = 0;
	        	byte[] buffer = new byte[1024];
	        	response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
	        	response.setContentType("application/msexcel");// 定义输出类型
	        	OutputStream out = response.getOutputStream();
	        	while((len = in.read(buffer)) > 0) {
	        		out.write(buffer,0,len);
	        	}
	        	if(null != in){
	        		in.close();
	        	}
	        	if(null != out){
	        		out.close();
	        	}
	    	}
		}
		/**
		 * 从费目头部中获取费目所在行，和在list中的index
		 * @param list 
		 * @param lab 费目唯一标记
		 * @return
		 */
		private Integer[] getNum(List<Map<String,Object>> list,String lab){
			Integer[] retNum=null;
			if(list!=null && list.size()>0 && lab!=null){
				Map<String,Object> map=null;
				for(int i=0;i<list.size();i++){
					map=list.get(i);
					if(lab.equals(map.get("LAB").toString())){
						retNum=new Integer[2];
						retNum[0]= Integer.valueOf(map.get("nlrow")!=null?map.get("nlrow").toString():"0");
						retNum[1]=i;
						break;
					}
				}
			}
			return retNum;
		}
		////////////////////////////////////////////////////
		private String getIOType(String strpe) {
			if ("在库".equals(strpe)) {
				return "pre_in";
			} else if ("出".equals(strpe)) {
				return "out";
			} else if ("入库".equals(strpe)) {
				return "in";
			} else {
				return " ";
			}
		}
		/**
		 * 将单元格转换为字母+数字的名称
		 * @param rowIndex--行值
		 * @param cellnum--列值（需要转换为字母）
		 */
		private String getCellName(int rowIndex, int cellnum) {
			int zero = cellnum / 26;
			if (zero != 0) {
				char c = (char) (zero + 64);
				char d = (char) ((cellnum - zero * 26) + 65);
				return String.valueOf(c) + String.valueOf(d) + String.valueOf(rowIndex);
			} else {
				char c = (char) (cellnum + 65);
				return String.valueOf(c) + String.valueOf(rowIndex);
			}
		}
		//PDF导出对账单明细
		@RequestMapping(value = "reportpdf")
		@ResponseBody
		public void exportPDF(@Valid @ModelAttribute @RequestBody BisCheckingBook obj,HttpServletRequest request, HttpServletResponse response) throws Exception{
	    	if(obj.getCodeNum()!=null && !"".equals(obj.getCodeNum()) && obj.getnType()!=null && obj.getnType()>0){
	    		List<Map<String,Object>> getlist=bisCheckingBookService.getRepCheckingBookInfo(obj.getCodeNum(),(obj.getnType()==1||obj.getnType()==3)?"1":"2");
				logger.info("getlist2:"+ JSON.toJSONString(getlist));
	    		List<Map<String,Object>> gethead=bisCheckingBookService.getRepCheckingBookHead(obj.getCodeNum());
				logger.info("gethead2:"+ JSON.toJSONString(getlist));
	    		if(obj.getnType()==2||obj.getnType()==4){
	    			List<Map<String,Object>> newList=bisCheckingBookService.getRepCheckingBookInfo2(obj.getCodeNum());
	    			if(!newList.isEmpty()&&getlist.isEmpty()){
	    				getlist.addAll(newList);
	    			}else if(!newList.isEmpty()){
	    				int bbc=0;
	    				for(Map<String,Object> newMap:newList){
	    					bbc=0;
	    					int size=getlist.size();
	    					for(int i=0;i<size;i++){
	    						Map<String,Object> getSql=getlist.get(i);
	    						if(newMap.get("BILL_NUM").equals(getSql.get("BILL_NUM"))){
	    							bbc=1;
	    						}else{
	    							if(bbc==1){
	    								 getlist.add(i, newMap);
	    								 break;
	    							}
	    						}//end if
	    					}//end for
	    				}//end for 
	    			}
	    		}
				logger.info("getlist3:"+ JSON.toJSONString(getlist));
	    		String[] headCN={"入/出库提单号","入/出","计费起始日期","品名","件数","净重","毛重","在库天数","截止日期"};
	       		String[] headEN={"B/L","in/out","Date in","name","pcs","Net KG","Gross KG","storage days","Date out"};
	       		String pdfTitle = "对账单明细";
	       		if(1==obj.getnType() || 2==obj.getnType()){
	       			pdfTitle="对账单明细";
	       		}else{
	       			pdfTitle="Statement of Account";
	       		}
	            StringBuffer pdfCN=new StringBuffer();
	            if (1==obj.getnType()||2==obj.getnType()) {
	            	pdfCN.append(""
	            				+"入/出库提单号"+"                 "
	            				+"入/出"+"                                     "
	            				+"计费起始日期"+"             	               "
	            				+"品名"+"                                        "
	            				+"件数"+"                                            "
	            				+"净重"+"                                            "
	            				+"毛重"+"                                           "
	            				+"在库天数"+"                                "
	            				+"截止日期"+"                  "
	            				);
	            }
	            if (3==obj.getnType()||4==obj.getnType()) {
	            	pdfCN.append(""
            				+"B/L"+"                                       "
            				+"in/out"+"                                        "
            				+"Date in"+"                                       "
            				+"name"+"                                           "
            				+"pcs"+"                                               "
            				+"Net KG"+"                                     "
            				+"Gross KG"+"                                      "
            				+"storage days"+"                           "
            				+"Date out"+"                  "
	        				);
	            }
	       		String path=request.getSession().getServletContext().getRealPath("/");//获取web项目的路径"d://exceltemplate//syshtm.html";
	       		String pathHtml=path+"//igrouprmbtm.html";
	       		String pathPdf=path+"//igrouprmbpdf.pdf";
	       		StringBuffer sbHtml=new StringBuffer();
	       		 
	       		sbHtml.append("<div  style=\"height:5px;\"></div>");
	       		sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;margin:none; text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
	       		//填充标题头
	       		sbHtml.append("<tr style=\"height:30px; \">");
	       		if(1==obj.getnType() || 2==obj.getnType()){
		       		for(String lab:headCN){
		       			sbHtml.append("<td class=\"htd\">").append(lab).append("</td>"); 
		       		}
	       		}else{
	       			for(String lab:headEN){
	       				sbHtml.append("<td class=\"htd\">").append(lab).append("</td>"); 
		       		}
	       		}
	       		if(gethead!=null && gethead.size()>0){
	        		for(Map<String,Object> getMap:gethead){
	        			if(1==obj.getnType() || 2==obj.getnType()){
	        				sbHtml.append("<td class=\"htd\">").append(getMap.get("NAME_C")!=null?getMap.get("NAME_C").toString():"费目名称").append("</td>"); 
	        			}else{
	        				sbHtml.append("<td class=\"htd\">").append(getMap.get("NAME_E")!=null?getMap.get("NAME_E").toString():"fee name").append("</td>"); 
	        			}
	        			getMap.put("hrowsum",0);//添加竖向金额计算和
	        		}
	        		if(1==obj.getnType() || 2==obj.getnType()){
	        			sbHtml.append("<td class=\"htd\">").append("总计RMB").append("</td>"); 
	        		}else{
	        			sbHtml.append("<td class=\"htd\">").append("TotalRMB").append("</td>"); 
	        		}
	        		
	        	}
	       		sbHtml.append("</tr>");
	       		String userName=null;// 客户名称
				String formatFileName = URLEncoder.encode("应收对账单明细" +".pdf","UTF-8");
	       		//填充内容
	       		if(getlist!=null && getlist.size()>0){
	        		Map<String,Object> map=null;
	        		String billNum=null;
	        		Double sumPiece=0d;
	        		Double sumNet=0d;
	        		Double sumSross=0d;
	        		Integer[] getRowNum=null;
	        		Map<String ,Object> keyMap=new HashMap<String ,Object>();//保存分组唯一表示和行数
	        		String keys=null;//记录分组唯一标示
	        		//String oldKeys="";
	        		boolean isHave=false;
	        		Double sunRMB=0d;//记录列总计RMB
	        		Double sunLRMB=0d;//记录竖向总计RMB
	        		//int nextCR=0;//记录上一条记录是否为入库
	        		for(int i=0;i<getlist.size();i++){
	        			map=getlist.get(i);
	        			if(null==userName){
	        				userName=map.get("UNAME")!=null?map.get("UNAME").toString():"";
	        			}
						if(0==i){
							String billDate=map.get("BILL_DATE")!=null?map.get("BILL_DATE").toString():"";
							String[] billDatelist=billDate.split("-");
							if(3==billDatelist.length){
								billDate=billDatelist[0]+"-"+billDatelist[1];
							}
							String name = (map.get("UNAME")!=null?map.get("UNAME").toString():"")+billDate+"月对账单明细";
							formatFileName = URLEncoder.encode(name +".xls","UTF-8");
						}
	        			//获取分组唯一标示
	        			keys=map.get("KEYS").toString();
	        			if(keyMap.get(keys)!=null || keys.split(":").length<3){
	        				isHave=true;
	        			}else{
	        				keyMap.put(keys,1);
	        				isHave=false;
	        				if(0<i){
		        				for(Map<String,Object> hmap:gethead){
		        					sunLRMB+=Double.valueOf(hmap.get("feemoney")!=null?hmap.get("feemoney").toString():"0");
		        					sbHtml.append("<td>").append(hmap.get("feemoney")!=null?hmap.get("feemoney").toString():"0").append("</td>");
		        					hmap.put("feemoney",0);
			        			}
		        				//添加小计竖向计算总和
			        			sbHtml.append("<td>").append(sunLRMB).append("</td>");
			        			sbHtml.append("</tr>");
			        			sunLRMB=0d;
	        				}
	        			}
	        			//当上一条获取的提单号和当前获取提单号不一致时添加小计
	        			if( billNum!=null && !billNum.equals(map.get("BILL_NUM")!=null?map.get("BILL_NUM").toString().trim():"") ){
		        			if(1==obj.getnType()||3==obj.getnType()){
	        					sbHtml.append("<tr style=\"font-size:10px;text-align:center;font-weight:700;\">");
	        					sbHtml.append("<td  colspan=\"4\">").append("小计 ：").append("</td>");
	        					sbHtml.append("<td>").append(String.valueOf(sumPiece.longValue())).append("</td>");
	        					sbHtml.append("<td>").append(String.valueOf(sumNet)).append("</td>");
	        					sbHtml.append("<td>").append(String.valueOf(sumSross)).append("</td>");
	        					sbHtml.append("<td>").append("").append("</td>");
	        					sbHtml.append("<td>").append("").append("</td>");
	        					for(Map<String,Object> hmap:gethead){
	        						sunRMB+=Double.valueOf(hmap.get("hrowsum")!=null?hmap.get("hrowsum").toString():"0");
	        						sbHtml.append("<td>").append(hmap.get("hrowsum")!=null?hmap.get("hrowsum").toString():"0").append("</td>");
	        						hmap.put("hrowsum",0);
	        					}
	        					sbHtml.append("<td>").append(String.valueOf(sunRMB)).append("</td>");
	        					sbHtml.append("</tr>");
	        					sunRMB=0d;
	        				}
	        				sumPiece=0d;sumNet=0d;sumSross=0d;
	        			} 
	        			//当行唯一标记在缓存map中不存在时添加一新行。如果存在则添加费目内容。
	        			if(false==isHave){
	        				sbHtml.append("<tr>");
	        				billNum=map.get("BILL_NUM")!=null?map.get("BILL_NUM").toString():"";
	        				sbHtml.append("<td>").append(billNum).append("</td>");
		        			if(1==obj.getnType() || 2==obj.getnType()){
		        				sbHtml.append("<td>").append(map.get("CRK_SIGN")!=null?map.get("CRK_SIGN").toString():"").append("</td>");
		        			}else{
		        				sbHtml.append("<td>").append(getIOType(map.get("STORAGE_STATE")!=null?map.get("STORAGE_STATE").toString():"")).append("</td>");
		        			}
		        			sbHtml.append("<td>").append(map.get("CHARGE_START_DATE")!=null?map.get("CHARGE_START_DATE").toString():"").append("</td>");
		        			sbHtml.append("<td>").append(map.get("CARGO_NAME")!=null?map.get("CARGO_NAME").toString():"").append("</td>");
		        			sbHtml.append("<td>").append(Double.valueOf(map.get("PIECE")!=null?map.get("PIECE").toString():"0").longValue()).append("</td>");
		        			sbHtml.append("<td>").append(map.get("NET_WEIGHT")!=null?map.get("NET_WEIGHT").toString():"").append("</td>");
		        			sbHtml.append("<td>").append(map.get("GROSS_WEIGHT")!=null?map.get("GROSS_WEIGHT").toString():"").append("</td>");
		        			sbHtml.append("<td>").append(map.get("CHARGE_DAY")!=null?map.get("CHARGE_DAY").toString():"").append("</td>");
		        			sbHtml.append("<td>").append(map.get("CHARGE_END_DATE")!=null?map.get("CHARGE_END_DATE").toString():"").append("</td>");
		        			//只计算在库数量
		        			if("3".equals(map.get("NTYPE")!=null?map.get("NTYPE").toString().trim():"")){
		        				sumPiece=sumPiece+Double.valueOf(map.get("PIECE").toString());
		        				sumNet=sumNet+Double.valueOf(map.get("NET_WEIGHT").toString());
		        				sumSross=sumSross+Double.valueOf(map.get("GROSS_WEIGHT").toString());
		        			}
	        			}
	        			//填充费目金额
	        			getRowNum=getNum(gethead,map.get("LAB")!=null?map.get("LAB").toString():"");
	        			if(getRowNum!=null){
	        				//添加横向计算总和
	        				gethead.get(getRowNum[1]).put("feemoney", map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0");
	        				//添加竖向计算总和
	        				gethead.get(getRowNum[1]).put("hrowsum", Double.valueOf(gethead.get(getRowNum[1]).get("hrowsum").toString())+Double.valueOf(map.get("SHOULD_RMB")!=null?map.get("SHOULD_RMB").toString():"0"));
	        			}
	        			//如果是是最后记录添加小计
	        			if(i==getlist.size()-1){
		        			for(Map<String,Object> hmap:gethead){
		        					sunLRMB+=Double.valueOf(hmap.get("hrowsum").toString());
		        					sbHtml.append("<td>").append(hmap.get("feemoney")!=null?hmap.get("feemoney").toString():"0").append("</td>");
		        			}
		        			//添加小计竖向计算总和
		        			
		        			sbHtml.append("<td>").append(sunLRMB).append("</td>");
		        			sbHtml.append("</tr>");
		        			if(1==obj.getnType()||3==obj.getnType()){
	        					sbHtml.append("<tr style=\"font-size:10px;text-align:center;font-weight:700;\">");
	        					sbHtml.append("<td colspan=\"4\">").append("小计 ：").append("</td>");
	        					sbHtml.append("<td>").append(String.valueOf(sumPiece.longValue())).append("</td>");
	        					sbHtml.append("<td>").append(String.valueOf(sumNet)).append("</td>");
	        					sbHtml.append("<td>").append(String.valueOf(sumSross)).append("</td>");
	        					sbHtml.append("<td>").append("").append("</td>");
	        					sbHtml.append("<td>").append("").append("</td>");
	        					for(Map<String,Object> hmap:gethead){
	        						sunRMB+=Double.valueOf(hmap.get("hrowsum")!=null?hmap.get("hrowsum").toString():"0");
	        						sbHtml.append("<td>").append(hmap.get("hrowsum")!=null?hmap.get("hrowsum").toString():"0").append("</td>");
	        					}
	        					sbHtml.append("<td>").append(String.valueOf(sunRMB)).append("</td>");
	        					sbHtml.append("</tr>");
	        				}
		        			
	        			}
	        		}//end for
        		}//end if
	       		sbHtml.append("</table>");
	       		sbHtml.append("<table style=\"border-spacing:0px; border-collapse:collapse;font-family:宋体;font-size:10px;width:100%\"><tr>");
	       		sbHtml.append("<td style=\"width:70%;\"></td><td style=\"text-align:left;margin-top:10px;\">");
	       		sbHtml.append("如无问题，请尽快安排付款。谢谢！<br/>公司名称：青岛港怡之航冷链物流有限公司<br/>人民币账户：802790200002726<br/>开户行名称：青岛银行<br/>纳税人识别号：91370220395949850B<br/>");
	       		sbHtml.append("地  址：山东省青岛市保税区北京路41号203房间<br/>电  话：0532-82987866");
	       		sbHtml.append("</td>");
	       		sbHtml.append("</tr></table>"); 
	       		if(null==userName){
	       			userName="";
	       		}
	       		MyFileUtils html=new MyFileUtils();
	       		html.setFilePath(pathHtml);
	       		if(1==obj.getnType() || 2==obj.getnType()){
	       			html.saveStrToFile(CreatPDFUtils.createPdfHtmlAddRight("对账单明细", obj.getCodeNum(),"对账客户：",userName,sbHtml.toString()));
	       		}else{
	       			html.saveStrToFile(CreatPDFUtils.createPdfHtmlAddRight("Statement of Account",obj.getCodeNum(),"Consumer：",userName,sbHtml.toString()));	
	       		}
	       		MyPDFUtils.setsDEST(pathPdf);
	       		MyPDFUtils.setsHTML(pathHtml);
	       		MyPDFUtils.createPdf(PageSize.A3,pdfCN.toString(),pdfTitle);
	       		
	       		
	       		//下载操作
	       		FileInputStream in = new FileInputStream(new File(pathPdf));
	        	int len = 0;
	        	byte[] buffer = new byte[1024];
	        	response.setHeader("Content-disposition", "attachment; filename=\"" +formatFileName+"\"");// 设定输出文件头
	        	response.setContentType("application/msexcel");// 定义输出类型
	        	OutputStream out = response.getOutputStream();
	        	while((len = in.read(buffer)) > 0) {
	        		out.write(buffer,0,len);
	        	}
	        	if(null != in){
	        		in.close();
	        	}
	        	if(null != out){
	        		out.close();
	        	}
	    		
	    	}
    	}
}
