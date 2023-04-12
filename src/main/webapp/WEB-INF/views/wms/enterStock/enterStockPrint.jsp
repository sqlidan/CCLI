<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center',title:'入库联系单打印浏览'">
		<div style="padding:5px;height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印</a>
		</div>
<div id="home" class="tab-pane" >
	<table id="test" class="formTable" border="1" width="700" >
		<tr>
		    <td id="abc" rowspan="14">客服</td>
			<td>存货方</td>
			<td colspan='5'>${bisEnterStock.stockIn}</td>
			<td>报关单号</td>
			<td colspan='3'>&nbsp</td>
		</tr>
		<tr>
			<td>ETA</td>
			<td colspan='5'>${bisEnterStock.etaShip}</td>
			<td>是否分拣</td>
			<td colspan='3'>
			<input id="ifSorting" type="hidden" value="${bisEnterStock.ifSorting}" />
			<input id="ifSortingV" value="" type="text" readonly>
			</td>
		</tr>
		<tr>
			<td>航名/航次</td>
			<td colspan='5'></td>
			<td>入库提单号</td>
			<td colspan='3'>${bisEnterStock.itemNum}</td>
		</tr>
		<tr>
			<td>箱型箱量</td>
			<td colspan='5'>${bisEnterStock.ctnTypeSize}</td>
			<td>入库号</td>
			<td colspan='3'>${bisEnterStock.rkNum}</td>
		</tr>
		<tr>
			<td colspan='3'>货物名称</td>
			<td colspan='2'>入库MR号</td>
			<td colspan='2'>规格</td>
			<td colspan='1'>件数</td>
			<td colspan='1'>净重</td>
			<td colspan='1'>毛重</td>
		</tr>
		<c:forEach items="${enterStockInfoList}" var="enter" varStatus="my">
		<tr>
		<td colspan='3'>${enter.cargoName}</td>
		<td colspan='2'>${enter.ctnNum}</td>
		<td colspan='2'>${enter.typeSize}</td>
		<td>${enter.piece}</td>
		<td>${enter.netWeight}</td>
		<td>${enter.grossWeight}</td>
		</tr>
		</c:forEach>
		<tr> 
		<td colspan="7" align="center">合计：</td>
		<td colspan='1'>${allPiece}</td>
		<td colspan='1'>${allNet}</td>
		<td colspan='1'>${allGross}</td>
		</tr>
		
		<tr>
		<td rowspan="5">客户要求</td>
		<td colspan="9">分拣要求:${bisEnterStock.sortingAsk1}&nbsp${bisEnterStock.sortingAsk2}&nbsp${bisEnterStock.sortingAsk3}&nbsp${bisEnterStock.sortingAsk4}&nbsp${bisEnterStock.sortingAsk5}&nbsp${bisEnterStock.sortingAsk6}</td>
		</tr>
		<tr>
		<td colspan="9">预计出库时间:${bisEnterStock.etdWarehouse}&nbsp&nbsp&nbsp
		&nbsp<input type="checkbox" id="ifBonded" name="ifBonded" value="1" readonly="readonly"/><label>是否保税</label>
		&nbsp<input type="checkbox" id="ifMacAdmit" name="ifMacAdmit" value="1" readonly="readonly"/><label>是否MSC认证</label>
		</td>
		</tr>
		<tr>
		<td colspan="9">其他要求：
		存储温度：${bisEnterStock.temperature}
		</td>
		</tr>
		<tr>
		<td colspan="9">
		<input type="checkbox" id="ifWeigh" name="ifWeigh" value="1" disabled/><label>称重;</label>
		<input type="checkbox" id="receptacle" name="receptacle"  disabled/><label>物流容器：</label>
		<input type="checkbox" id="ifWithWooden" name="ifWithWooden" value="1" disabled/><label>自带木托；</label>
		<c:forEach items="${dict}" var="dict" varStatus="my">
		<input type="checkbox" id="${my.count}" name="receptacle" value="${dict.label}" readonly disabled/><label>${my.count}.${dict.label}；</label>
		</c:forEach>
		</td>
		</tr>
		<tr>
		<td colspan="9">
		<input type="checkbox" id="ifBagging" name="ifBagging" value="1" readonly/><label>是否套袋；</label>
		<input type="checkbox" id="ifWrap" name="ifWrap" value="1" readonly/><label>是否缠膜；</label>
		</td>
		</tr>
		<tr>
		<td>特殊要求</td>
		<td colspan="9">
		${bisEnterStock.sortingSpecial}
		</td>
		</tr>
		<tr>
		<td>特殊分拣</td>
		<td colspan="9">${bisEnterStock.sortingSpecialAsk}</td>
		</tr>
		<tr>
		<td>备注</td>
		<td colspan="9">${bisEnterStock.remark}</td>
		</tr>
		<tr>
		<td rowspan="1">财务</td>
		<td rowspan="1" colspan="10">
		收费类型
		<input type="checkbox"  value="1" readonly/><label>标准收费：</label>
		<input type="checkbox"  value="1" readonly/><label>合同收费：</label>
		<input type="checkbox"  value="1" readonly/><label>临时收费：</label>
		<input type="checkbox"  value="1" readonly/><label>其它：</label>
		财务确认：
		</td>
		</tr>
		<tr>
		<td rowspan="2">UI</td>
		<td>存放区位</td>
		<td colspan="5"></td>
		<td>注意事项</td>
		<td colspan="3"></td>
		</tr>
		<tr>
		<td colspan="5">收费接受计划时间：</td>
		<td colspan="4">UI设置完成时间</td>
		<td colspan="1"> UI确认：</td>
		</tr>
		<tr>
		<td rowspan="4">计划</td>
		<td>SELAING确认</td>
		<td colspan="5"></td>
		<td>运输车队</td>
		<td colspan="3"></td>
		</tr>
		<tr>
		<td>吊车使用</td>
		<td colspan="5">
		<input type="checkbox"   value="1" readonly/><label>客户付费使用 </label>
		<input type="checkbox"   value="1" readonly/><label align="right">客户不付费 </label>
		</td>
		<td>现场确认</td>
		<td colspan="3">
		<input type="checkbox"   value="1" readonly/><label>顶部加固 </label>
		<input type="checkbox"   value="1" readonly/><label align="right">全部加固 </label>
		</td>
		</tr>
		<tr>
		<td>预计卸货时间</td>
		<td colspan="5"></td>
		<td>其他</td>
		<td colspan="3"></td>
		</tr>
		<tr>
		<td colspan="4">批准</td>
		<td colspan="4">计划</td>
		<td colspan="2">客服消息传递时间</td>
		</tr>
		<tr>
		<td rowspan="5">仓库</td>
		<td rowspan="3">班组实际执行：</td>
		<td colspan="9">使用托盘数</td>
		</tr>
		<tr>
		<td colspan="9">拍照序号记录：</td>
		</tr>
		<tr>
		<td colspan="5">入库测温记录：</td>
		<td colspan="4">计划确认：</td>
		</tr>
		<tr>
		<td colspan="3">收到ASN条码时间</td>
		<td colspan="3">作业开始时间</td>
		<td colspan="2">完成时间</td>
		<td colspan="2">仓库主管</td>
		</tr>
		<tr>
		<td rowspan="1" colspan="10">备注</td>
		</tr>
	</table>
</div>
</div>

<script type="text/javascript">
$(function(){   
    var infoSize = "${infoSize}";
    // addRow(ttt,info);
    var fin = parseFloat("${infoSize}") + 14;
    var tb=document.getElementById("test");
 	tb.rows[0].cells[0].rowSpan = fin;
	$("input[name='ifMacAdmit'][value=${bisEnterStock.ifMacAdmit}]").attr("checked",true);
	$("input[name='ifBonded'][value=${bisEnterStock.ifBonded}]").attr("checked",true);
	$("input[name='ifWeigh'][value=${bisEnterStock.ifWeigh}]").attr("checked",true);
	if("${bisEnterStock.receptacle}" != "无"){
		$("#receptacle").attr("checked",true);
	}
	$("input[name='receptacle'][value=${bisEnterStock.receptacle}]").attr("checked",true);
	$("input[name='ifWithWooden'][value=${bisEnterStock.ifWithWooden}]").attr("checked",true);
	$("input[name='ifBagging'][value=${bisEnterStock.ifBagging}]").attr("checked",true);
	$("input[name='ifWrap'][value=${bisEnterStock.ifWrap}]").attr("checked",true);
	if($("#ifSorting").val() == "1"){
		$("#ifSortingV").val("是");
	}else{
	 $("#ifSortingV").val("否");
	}
});


function addRow(i,info){
    var tb=document.getElementById("test");
　　　var row=document.getElementById("test").insertRow(i);
	row.insertCell(0).innerHTML="<td>${bisEnterStockInfo0.piece}</td>";
	row.insertCell(1).innerHTML="<td>11</td>";
	row.insertCell(2).innerHTML="<td>11</td>";
	row.insertCell(3).innerHTML="<td>11</td>";
	row.insertCell(4).innerHTML="<td>11</td>";
	row.insertCell(5).innerHTML="<td>11</td>";
    tb.rows[i].cells[0].colSpan = 3;
    tb.rows[i].cells[1].colSpan = 2;
    tb.rows[i].cells[2].colSpan = 2;
}

function print(){
$("#home").jqprint();
}
</script>
</body>
</html>
