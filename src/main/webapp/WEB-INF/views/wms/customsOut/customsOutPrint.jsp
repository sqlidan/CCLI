<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center',title:'出库报关单打印浏览'">
		<div style="padding:5px;height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印</a>
		</div>
<div id="home" class="tab-pane" >
	<table id="test" class="formTable" border="1" width="700" >
		<tr>
			<td>报关单号</td>
			<td>提单号</td>
			<td>审批号</td>
			<td>贸易方式</td>
			<td>经营单位</td>
			<td>收货单位</td>
			<td>进口日期</td>
			<td>申报日期</td>
			<td>船名</td>
			<td>航次</td>
			<td>放行日期</td>
			<td>件数</td>
			<td>净重</td>
		</tr>
		<c:forEach items="${bisCustoms}" var="customs" varStatus="my">
		<tr>
		<td>${customs.cdNum}</td>
		<td>${customs.billNum}</td>
		<td>${customs.examNum}</td>
		<td>${customs.tradeType}</td>
		<td>${customs.managementUnit}</td>
		<td>${customs.consignee}</td>
		<td>${customs.importTime}</td>
		<td>${customs.declareTime}</td>
		<td>${customs.vesselName}</td>
		<td>${customs.voyageNum}</td>
		<td>${customs.releaseTime}</td>
		<td>${customs.piece}</td>
		<td>${customs.netWeight}</td>
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
