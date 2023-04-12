<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center',title:'入库预报单打印浏览'">
		<div style="padding:5px;height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印</a>
		</div>
<div id="home" class="tab-pane" >
	<table id="test" class="formTable" border="1" width="700" >
		<tr>
			<td>客户名称</td>
			<td colspan='4'>${bisForecast.clientName}</td>
			<td>提单号</td>
			<td colspan='4'>${bisForecast.billNum}</td>
		</tr>
		<tr>
			<td>箱量</td>
			<td colspan='4'>${bisForecast.ctnCont}</td>
			<td>贸易方式</td>
			<td colspan='4'>${bisForecast.tradeMode}</td>
		</tr>
		<tr>
			<td>报关公司</td>
			<td colspan='4'>${bisForecast.declarationUnit}</td>
			<td>报检公司</td>
			<td colspan='4'>${bisForecast.ciqDeclarationUnit}</td>
		</tr>
		<tr>
			<td>报关状态</td>
			<td colspan='4'><c:choose><c:when test="${bisForecast.cdSign == 1}">已报关</c:when><c:otherwise>未报关</c:otherwise></c:choose></td>
			<td>报检状态</td>
			<td colspan='4'><c:choose><c:when test="${bisForecast.ciqSign == 1}">已报检</c:when><c:otherwise>未报检</c:otherwise></c:choose></td>
		</tr>
		<tr>
			<td>创建日期</td>
			<td colspan='4'>${bisForecast.createTime}</td>
		</tr>
		<tr>
			<td colspan='8'>货物明细</td>
		</tr>
		<tr>
			<td>项号</td>
			<td>编码</td>
			<td>品名</td>
			<td>规格</td>
			<td>件数</td>
			<td>净重</td>
			<td>ETA</td>
			<td>木托数量</td>
		</tr>
		<c:forEach items="${forecastInfo}" var="fore" varStatus="my">
		<tr>
		<td>${fore.itemNum}</td>
		<td>${fore.hscode}</td>
		<td>${fore.cargoName}</td>
		<td>${fore.space}</td>
		<td>${fore.piece}</td>
		<td>${fore.netWeight}</td>
		<td>${fore.eta}</td>
		<td>${fore.trayNum}</td>
		</tr>
		</c:forEach>
	</table>
</div>
</div>

<script type="text/javascript">
$(function(){   
});

function print(){
$("#home").jqprint();
}
</script>
</body>
</html>
