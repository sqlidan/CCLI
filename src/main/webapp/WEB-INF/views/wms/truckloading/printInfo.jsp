<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path=application.getRealPath(request.getRequestURI());
	int getlastNum=path.lastIndexOf("webapp");
	//path=path.substring(0,getlastNum)+"webapp\\static\\barcode_file\\";
	path=path.substring(0,getlastNum)+"webapps\\ccli\\static\\barcode_file\\";
	String imgPath=request.getRequestURL().toString().split("ccli")[0];
	String tnum=(String)request.getAttribute("tnum");
	if(tnum == null){
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script >
 var path='<%=path %>';
</script>
<script src="${ctx}/static/plugins/print/LodopFuncs.js"></script>
</head>
<body>
<c:if test="${isok == 0}"> 
	<script type="text/javascript">
		parent.$.easyui.messager.alert("出库订单未查询到");
	</script>
</c:if>
<div class="my_hidden" style="margin-bottom: 5px;"> 
	<a href="javascript:void(0)" class="easyui-linkbutton"  id="print" iconCls="icon-print">打印</a>
</div> 
<div class="my_show" id="printdiv">
	<table>
		<thead>
			 <tr>
			 <td colspan="10" style="text-align: left;"><img src="<%=imgPath %>ccli/static/images/tpdf1.jpg" /><img src="<%=imgPath %>ccli/static/images/tpdf2.jpg" /></td>
			 </tr>
			 <tr>
			 <td colspan="3" rowspan="2" style="text-align: left;font-size: 13px">
			 	QINGDAO PORT EIMSKIPCOLDCHAIN LOGISTICS CO. LTD<br/> 
			 	 &nbsp;&nbsp;&nbsp;&nbsp;青岛港怡之航冷链物流有限公司.<br/>
			 	Weisan Road , Qianwan Port, Huangdao District<br/> 
			 	Qingdao ,Shandong 266500,China<br/>	
			 	www.eimskip.com&nbsp;&nbsp;&nbsp;www.eimskipcoldstore.com<br/>		
			 	Tel: +86 532 82987866   Fax: +86 532 86947977
			 	
			 </td>
			 <td colspan="3" align='center'>出&nbsp;&nbsp;库&nbsp;&nbsp;货&nbsp;&nbsp;物&nbsp;&nbsp;随&nbsp;&nbsp;车&nbsp;&nbsp;清&nbsp;&nbsp;单</td>
			 
			 <td  colspan="4" style="text-align: left;font-size: 14px" rowspan="2">Print Time: &nbsp;&nbsp; ${now1}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${now2}</td>
			 </tr>
			 <tr>
			 <td colspan="3" align='center'>Loading Report</td>
			 </tr>
			 <tr>
				<td class="skudesc" colspan="5" style="text-align: left;font-size: 17px;height:50px">&nbsp;&nbsp;客户名称：${user}</td>
				<td class="skudesc" colspan="5"  style="text-align: left;font-size: 17px">提货客户：${user1} </td>
			</tr>
						 <tr>
				<td class="skudesc" colspan="5" style="text-align: left;font-size: 17px;height:45px">&nbsp;&nbsp;入库提单号：${billNum}</td>
				<td class="skudesc" colspan="5" style="text-align: left;font-size: 17px;height:45px">出库日期：${loadingTime} </td>
			</tr>
			<tr>
			<td class="skudesc" colspan="5" style="text-align: left;font-size: 17px;height:45px">&nbsp;&nbsp;车牌号：${car} </td>
			</tr>
<%-- 			 <tr>
				<td class="skudesc" colspan="5" style="font-size: 14px">客户地址：${adress}</td>
			 </tr>
			<tr>
				<td colspan="5" style="font-size: 14px">
					装车号：<img src="/ccli/static/barcode_file/<%=com.haiersoft.ccli.common.utils.BarcodeUtil.generateFile(tnum, path,true) %>"   width="300px" height="60px" />  
				</td>
			 </tr> --%>
<!-- 			 <tr >
				 <td class="trlink" colspan="10"></td>
			 </tr> -->
			 <tr class="th1">
				<td colspan='2' style="font-size: 15px;height:30px">MR/入库  集装箱号</td>
				<td colspan='3' style="font-size: 15px;">货物描述</td>
				<td colspan='1' style="font-size: 15px;">件数</td>
				<td colspan='2' style="font-size: 15px;">总净重(KGS)）</td> 
				<td colspan='2' style="font-size: 15px;">总毛重(KGS)</td> 
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${infolist}" var="tinfo">
				<tr class="tdb">
					<td colspan='2'>${tinfo.ctnNum}</td>
					<td colspan='3'>${tinfo.producingArea}</td>
					<td colspan='1'>${tinfo.piece}</td>
					<td colspan='2'>${tinfo.netWeight}</td> 
					<td colspan='2'>${tinfo.grossWeight}</td> 
				</tr>
  			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
			<td style="height:20px" width="10%">&nbsp;</td><td width="10%">&nbsp;</td>
			<td width="10%">&nbsp;</td><td width="10%">&nbsp;</td>
			<td width="10%">&nbsp;</td><td width="15%">&nbsp;</td>
			<td width="8%">&nbsp;</td><td width="8%">&nbsp;</td>
			<td width="8%">&nbsp;</td><td>&nbsp;</td>
			</tr>
					
			<c:forEach items="${infolist}" var="tinfo">
						 <tr colspan='10' class="th"  >
						 <td colspan='2'>   </td>
				<td colspan='3'> 合计/Total:</td>
				<td colspan='1' style="text-align: left;">${tinfo.piece}</td>
				<td colspan='2' style="text-align: left;">${tinfo.netWeight}</td> 
				<td colspan='2' style="text-align: left;">${tinfo.grossWeight}</td> 
			</tr>
						</c:forEach>

			<td colspan="10"><br/><br/></td>
			<tr>
  				<td colspan="5" style="font-size: 17px;height:40px">
					现场理货员：
				</td>
				<td style="font-size: 17px">运输司机：</td> 
			</tr>
			<tr>
				<td colspan="5" style="font-size: 17px;height:40px">现场总监：</td> 
				<td style="font-size: 17px">铅封号：</td> 
			</tr> 
						<tr>
				<td colspan="5" style="font-size: 17px;height:40px">制表：</td> 
				<td style="font-size: 17px">集装箱号码：</td> 
			</tr> 
			<tr>
			<td colspan="9" style="height:35px"> 说明： 随车清单是曼哈顿仓储管理系统在现场装车结束后自动产生的当车次内的装车货物明细，需要提货人（委托人）进行复核确认。</td> 
			<td colspan="1" style="text-align:right;">page<font tdata="PageNO"   color="blue">##</font>of<font tdata="PageCount"  color="blue">##</font></td>  
			</tr>
		</tfoot>
	</table>
<style type="text/css">
	.my_show{
	width:1487px;overflow-y:false;
/* 	border-style:solid;border-width:1px;border-color:#000 */
	}
	.tdb{font-weight:700;}
	.special{font-weight:700;font-size:30px;}
	table{width:100%;font-size:16px;}
	thead tr td{
	text-align:center;
	font-size:25px;font-weight:700;
	}
	.th td{
	text-align:center;
	font-size:14px;
	font-weight:700;
	border:0px;
	border-bottom: #000 1px solid;
	}
		.th1 td{
	text-align:left;
	font-size:14px;
	font-weight:700;
	border:0px;
	}
	.trlink{
		border-bottom: #000 2px solid;
	}
	tbody td{text-align:left;}
	.skudesc{font-size:30px;font-weight:700;}
	.skuimgtr .skudesc{border-bottom: #000 1px solid;}
	tfoot tr{
		font-size:14px;
		font-weight:700;
	}
/* 	tr{border:1px solid #999999;} */
/* 	td{border:1px solid #999999;} */
</style>
</div>
		
		 
			
		  
		 
<script type="text/javascript">
$(document).ready(function() { 
	$("#print").click(function(){ 
		//$(".my_show").jqprint(); 
		LODOP=getLodop();  
		LODOP.PRINT_INIT("装车单打印");
		LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
		LODOP.ADD_PRINT_TABLE("2%","1%","96%","98%",document.getElementById("printdiv").innerHTML);
		LODOP.SET_PREVIEW_WINDOW(0,0,0,800,600,"");
		LODOP.PREVIEW();	
	}) 
}); 

</script>
</body>
</html>