<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.haiersoft.ccli.wms.entity.InspectStock"%>
<%@page import="java.util.List"%>
<%
	List<InspectStock> list = (List<InspectStock>)request.getAttribute("sampleInfo");
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<style type="text/css">
	tr{
		height: 80px;
		
	}
	
	td{
		font-size:20px;
	}

</style>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center',title:'取样计划单打印浏览'" style="width:794px;height:1123px;border:1px solid #000000;">
		<div style="padding:5px;height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印</a>
		</div>
<div id="home" class="tab-pane" >
	
	<center><h1>青港物流冷链中心取样计划单</h1></center>
	<center>
		<table cellpadding="6" cellspacing="3">
		<tr id="t0">
			<td></td>
			<td>客户名称：</td>
			<td>${stockName}</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr id="t1">
			<td></td>
			<td>取样时间：</td>
			<td>${sampleDate}</td>
			<td></td>
			<td>制单时间：</td>
			<td>${checkDate}</td>
			<td></td>
		</tr>
		<tr id="t4">
			<td></td>
			<td>取样总数：</td>
			<td>${inspectTotal}</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr id="t5">
			<td align="center">序号</td>
			<td align="center">提单号</td>
			<td align="center">箱号</td>
			<td align="center">SKU</td>
			<td align="center">产品名称</td>
			<td align="center">库位号</td>
			<td align="center">件数</td>
		</tr>
		<% 
			InspectStock is = null;
			for(int i=0;i<list.size();i++){
				is = list.get(i);
		%>
			<tr>
				<td width="50px" align="center"><%=i+1 %></td>
				<td width="200px" align="center"><%=is.getBillNum() %></td>
				<td width="200px" align="center"><%=is.getCtnNum() %></td>
				<td width="200px" align="center"><%=is.getSkuId() %></td>
				<td width="200px" align="center"><%=is.getCargoName() %></td>
				<td width="200px" align="center"><%=is.getCargoLocation()==null?"":is.getCargoLocation() %></td>
				<td width="50px" align="center"><%=is.getInspectTotal() %></td>
			</tr>
		<%		
			}
		%>
		<tr id="t6">
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr id="t7">
			<td></td>
			<td>取样人签字：</td>
			<td></td>
			<td></td>
			<td>客户签字：</td>
			<td></td>
			<td></td>
		</tr>

	</table>
	</center>
	
</div>
</div>

<script type="text/javascript">
function print(){
$("#home").jqprint();
}


/* var hkey_root,hkey_path,hkey_key
hkey_root="HKEY_CURRENT_USER"
hkey_path="//Software//Microsoft//Internet Explorer//PageSetup//"
$(document).ready(function() {

	try{
		var RegWsh = new ActiveXObject("WScript.Shell")
		hkey_key="header";
		RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
		hkey_key="footer";
		RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
		print();
		}catch(e){}
	
}); */
</script>
</body>
</html>
