<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center',title:'入库报检单（带货物信息）打印浏览'">
		<div style="padding:5px;height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印</a>
		</div>
<div id="home2" class="tab-pane" >
	<table id="test" class="formTable" border="1" width="700" >
		<tr>
			<td>报检号</td>
			<td colspan='5'>${bisCiq.ciqCode}</td>
			<td>提单号</td>
			<td colspan='5'>${bisCiq.billNum}</td>
		</tr>
		<tr>
			<td>检验检疫证明日期</td>
			<td colspan='5'>${bisCiq.certificateTime}</td>
			<td>贸易方式</td>
			<td colspan='5'>${bisCiq.tradeType}</td>
		</tr>
		<tr>
			<td>收货人</td>
			<td colspan='5'>${bisCiq.consignee}</td>
			<td>发货人</td>
			<td colspan='5'>${bisCiq.consignor}</td>
		</tr>
		<tr>
			<td>申报日期</td>
			<td colspan='5'>${bisCiq.declareTime}</td>
			<td>船名</td>
			<td colspan='5'>${bisCiq.vesselName}</td>
		</tr>
		<tr>
			<td>航次</td>
			<td colspan='5'>${bisCiq.voyageNum}</td>
			<td>件数</td>
			<td colspan='5'>${bisCiq.piece}</td>
		</tr>
		<tr>
			<td>净重</td>
			<td colspan='5'>${bisCiq.netWeight}</td>
			<td>备注</td>
			<td colspan='5'>${bisCiq.remark1}</td>
		</tr>
		<tr>
			<td>HS编码</td>
			<td>商品名称</td>
			<td>件数</td>
			<td>净重</td>
			<td>包装种类</td>
			<td>货值</td>
			<td>录入员</td>
			<td>录入日期</td>
			<td>备注</td>
		</tr>
		<c:forEach items="${ciqInfo}" var="ciq" varStatus="my">
		<tr>
		<td>${ciq.ciqNum}</td>
		<td>${ciq.cargoName}</td>
		<td>${ciq.scalar}</td>
		<td>${ciq.netWeight}</td>
		<td>${ciq.bagType}</td>
		<td>${ciq.price}</td>
		<td>${ciq.recordMan}</td>
		<td>${ciq.recordTime}</td>
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
$("#home2").jqprint();
}
</script>
</body>
</html>
