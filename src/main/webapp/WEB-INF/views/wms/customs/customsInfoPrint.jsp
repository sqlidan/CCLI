<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center',title:'入库报关单（带货物信息）打印浏览'">
		<div style="padding:5px;height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印</a>
		</div>
<div id="home2" class="tab-pane" >
	<table id="test" class="formTable" border="1" width="700" >
		<tr>
			<td>报关单号</td>
			<td colspan='6'>${bisCustoms.cdNum}</td>
			<td>提运单号</td>
			<td colspan='6'>${bisCustoms.billNum}</td>
		</tr>
		<tr>
			<td>审批号</td>
			<td colspan='6'>${bisCustoms.examNum}</td>
			<td>贸易方式</td>
			<td colspan='6'>${bisCustoms.tradeType}</td>
		</tr>
		<tr>
			<td>进口口岸</td>
			<td colspan='6'>${bisCustoms.importPort}</td>
			<td>进出标致</td>
			<td colspan='6'>入库</td>
		</tr>
		<tr>
			<td>经营单位</td>
			<td colspan='6'>${bisCustoms.managementUnit}</td>
			<td>收货单位</td>
			<td colspan='6'>${bisCustoms.consignee}</td>
		</tr>
		<tr>
			<td>进口日期</td>
			<td colspan='6'>${bisCustoms.importTime}</td>
			<td>申报日期</td>
			<td colspan='6'>${bisCustoms.declareTime}</td>
		</tr>
		<tr>
			<td>船名</td>
			<td colspan='6'>${bisCustoms.vesselName}</td>
			<td>航次</td>
			<td colspan='6'>${bisCustoms.voyageNum}</td>
		</tr>
		<tr>
			<td>放行日期</td>
			<td colspan='6'>${bisCustoms.releaseTime}</td>
			<td>件数</td>
			<td colspan='6'>${bisCustoms.piece}</td>
		</tr>
		<tr>
			<td>净重</td>
			<td colspan='6'>${bisCustoms.netWeight}</td>
			<td>备注</td>
			<td colspan='6'>${bisCustoms.remark1}</td>
		</tr>
		<tr>
			<td>项号</td>
			<td>HS编码</td>
			<td>商品名称</td>
			<td>规格</td>
			<td>件数</td>
			<td>净重</td>
			<td>单位</td>
			<td>目的地</td>
			<td>单价</td>
			<td>总价</td>
			<td>币种</td>
			<td>免征</td>
			<td>关税</td>
			<td>备注</td>
		</tr>
		<c:forEach items="${customsInfo}" var="customs" varStatus="my">
		<tr>
		<td>${customs.itemNum}</td>
		<td>${customs.cdNum}</td>
		<td>${customs.cargoName}</td>
		<td>${customs.spec}</td>
		<td>${customs.scalar}</td>
		<td>${customs.netWeight}</td>
		<td>${customs.units}</td>
		<td>${customs.destination}</td>
		<td>${customs.unitPrice}</td>
		<td>${customs.totalPrices}</td>
		<td>${customs.currencyType}</td>
		<td><c:choose><c:when test="${customs.freeLavy == 1}">免征</c:when><c:otherwise>不免征</c:otherwise></c:choose>
		</td>
		<td>${customs.duty}</td>
		<td>${customs.remark1}</td>
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
