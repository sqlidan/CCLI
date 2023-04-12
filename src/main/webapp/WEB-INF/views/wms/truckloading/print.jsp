<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path=application.getRealPath(request.getRequestURI());
	int getlastNum=path.lastIndexOf("webapp");
	//path=path.substring(0,getlastNum)+"webapp\\static\\barcode_file\\";
	path=path.substring(0,getlastNum)+"webapps\\ccli\\static\\barcode_file\\";
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
<!-- <div class="my_hidden" style="margin-bottom: 5px;"> 
	<a href="javascript:void(0)" class="easyui-linkbutton"  id="print" iconCls="icon-print">打印</a>
</div>  -->
<!-- <div class="my_hidden" style="margin-bottom: 5px;"> 
	<a href="javascript:void(0)" class="easyui-linkbutton"  id="print" iconCls="icon-print">打印</a>
</div>  -->
    <div style="padding:5px;height:auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true"
           onclick="print()">打印</a>
    </div>
  <!--   <div id="home" class="tab-pane"> -->
<div class="my_show" id="printdiv">
	<table style="border-collapse:separate; border-spacing:0px;"> 
		<thead>
			 <tr><td colspan="10">装车报表</td></tr>
			 <tr>
				<td class="skudesc" colspan="2">客户名称：${user}</td>
				<td colspan="3"></td>
				<td colspan="3" >
					装车号：<img src="/ccli/static/barcode_file/<%=com.haiersoft.ccli.common.utils.BarcodeUtil.generateFile(tnum, path,true) %>"   width="300px" height="60px" />  
				</td>
				<td colspan="2"></td>
			 </tr>
			 <tr>
				<td class="skudesc" colspan="2">客户地址：${adress}</td>
				<td colspan="3"></td>
				<td colspan="3">
					理货员签字：
				</td>
				<td colspan="2"></td>
			 </tr>
			 <tr >
				 <td class="trlink" colspan="10"></td>
			 </tr>
			 <tr class="th">
			 	<td width='150px'>区位号</td>
				<td width='200px'>MR号</td> 
				<td width='230px'>SKU</td> 
				<td width＝'200px'>托盘号</td>
				<td width='150px'>拣货位</td> 
				<td width='125px'>数量</td> 
<!--			<td>任务号</td> 
				<td>波次号</td>  -->
				<td width='200px'>SKU描述</td>
				<td>货物状态</td>
				<td>备注</td>    
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${infolist}" var="tinfo">
				<tr class="tdb">
					<td>${tinfo.areaNum}</td>
					<td>${tinfo.ctnNum}</td> 
					<td>${tinfo.skuId}</td> 
					<td>${tinfo.trayId}</td>
					<td>${tinfo.cargoLocation}</td> 
					<td>${tinfo.piece}</td> 
<!--				<td>${tinfo.trayId}</td> 
					<td>${tinfo.trayId}</td> -->
					<td>${tinfo.cargoName} ${tinfo.cargoType}</td>
					<td>
						<c:choose>
				         	<c:when test="${tinfo.enterState==1}">
								货损
				         	</c:when>
				         	<c:when test="${tinfo.enterState==0}">
								成品
				         	</c:when>
						</c:choose>
				 	</td>
					<td>${tinfo.remark}</td>    
				</tr>
  			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				 <td colspan="10" style="height:15px"></td>
			</tr>
			<tr>
  				<td >净重合计：${sumnet}</td>
  				<td >毛重合计：${sumgross}</td>
  				<td >数量合计：${sumpiece}</td>
				<td colspan="2">库管员签字</td> 
				<td colspan="2">叉车司机签字</td>
				<td colspan="3" style="text-align:right;">第<font tdata="PageNO"   color="blue">##</font>页/共<font tdata="PageCount"  color="blue">##</font>页</td>  
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
	font-size:30px;font-weight:700;
	}
	.th td{
	font-size:20px;
	font-weight:700;
	
	border:0px;
	border-bottom: #000 1px solid;
	}
	.trlink{
		border-bottom: #000 2px solid;
	}
	tbody td{text-align:center;border-bottom: #000 1px solid;}
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
/* $(document).ready(function() { 
	$("#print").click(function(){ 
		//$(".my_show").jqprint(); 
		LODOP=getLodop();  
		LODOP.PRINT_INIT("装车单打印");
		LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
		LODOP.ADD_PRINT_TABLE("2%","1%","96%","98%",document.getElementById("printdiv").innerHTML);
		LODOP.SET_PREVIEW_WINDOW(0,0,0,800,600,"");
		LODOP.PREVIEW();	
	}) 
});  */
function print() {
    $("#printdiv").jqprint();
}
</script>
</body>
</html>