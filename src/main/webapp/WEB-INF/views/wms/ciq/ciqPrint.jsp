<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center',title:'入库报检单打印浏览'">
		<div style="padding:5px;height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印</a>
		</div>
<div id="home" class="tab-pane" >
	<table id="test" class="formTable" border="1" width="700" >
		<tr>
			<td>报检号</td>
			<td>提单号</td>
			<td>检验检疫证明日期</td>
			<td>贸易方式</td>
			<td>收货人</td>
			<td>发货人</td>
			<td>申报日期</td>
			<td>船名</td>
			<td>航次</td>
			<td>件数</td>
			<td>净重</td>
			<td>备注</td>
		</tr>
		<c:forEach items="${bisCiq}" var="ciq" varStatus="my">
		<tr>
		<td>${ciq.ciqCode}</td>
		<td>${ciq.billNum}</td>
		<td>${ciq.certificateTime}</td>
		<td>${ciq.tradeType}</td>
		<td>${ciq.consignee}</td>
		<td>${ciq.consignor}</td>
		<td>${ciq.declareTime}</td>
		<td>${ciq.vesselName}</td>
		<td>${ciq.voyageNum}</td>
		<td>${ciq.piece}</td>
		<td>${ciq.netWeight}</td>
		<td>${ciq.remark1}</td>
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
