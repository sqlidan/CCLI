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
		parent.$.easyui.messager.alert("报损清单未查询到");
	</script>
</c:if>
<div class="my_hidden" style="margin-bottom: 5px;"> 
	<a href="javascript:void(0)" class="easyui-linkbutton"  id="print" iconCls="icon-print">打印</a>
</div> 
<div class="my_show" id="printdiv">
	<table>
		<thead>
			 <tr><td colspan="10">报损清单</td></tr>
			 <tr>
				<td colspan="3" style="text-align:left">
					报损单号1：<img src="/ccli/static/barcode_file/<%=com.haiersoft.ccli.common.utils.BarcodeUtil.generateFile(tnum, path,true) %>"   width="300px" height="60px" />  
				</td>
				<td colspan="7"></td>
			 </tr>
			 <tr>
				<td  colspan="3"  style="text-align:left">
				   理货员签字：
				</td>
				<td colspan="7"></td>
			 </tr>
			 <tr >
				 <td class="trlink" colspan="10"></td>
			 </tr>
			 <tr class="th">
				<td>托盘号</td>
				<td>提单号</td> 
				<td>SKU</td> 
				<td>报损类型</td> 
				<td>客户名称</td> 
				<td>产品名称</td>
				<td>库位号</td> 
				<td>数量</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${infolist}" var="tinfo">
				<tr class="tdb">
					<td>${tinfo.trayCode}</td>
					<td>${tinfo.billNum}</td> 
					<td>${tinfo.sku}</td> 
					<td>
						<c:choose>
				         	<c:when test="${tinfo.scrapType==1}">
								普通报损
				         	</c:when>
				         	<c:when test="${tinfo.scrapType==2}">
								库内分拣报损
				         	</c:when>
						</c:choose>
				 	</td>
					<td>${tinfo.clientName}</td> 
					<td>${tinfo.cargoName}</td>
					<td>${tinfo.cargoLocation}</td> 
					<td>${tinfo.num}</td> 
				</tr>
  			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				 <td colspan="10" style="height:15px"></td>
			</tr>
			<tr>
  				<td></td>
				<td colspan="3">库管员签字</td> 
				<td colspan="3"></td>
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