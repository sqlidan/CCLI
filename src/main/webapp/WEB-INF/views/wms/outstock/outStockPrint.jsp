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
		  	<span class="toolbar-item dialog-tool-separator"></span>
		  	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="addRow()">测试</a>
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
			<td>分拣要求</td>
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
		<tr> 
		<td colspan="7" align="center">合计：</td>
		<td colspan='1'><input id="allPiece" value="" readonly></td>
		<td colspan='1'><input id="allNet" value="" readonly></td>
		<td colspan='1'><input id="allGross" value="" readonly></td>
		</tr>
		
		<tr>
		<td rowspan="5">客户要求</td>
		<td colspan="9">分拣要求:${bisEnterStock.sortingAsk1}&nbsp${bisEnterStock.sortingAsk2}&nbsp${bisEnterStock.sortingAsk3}&nbsp${bisEnterStock.sortingAsk4}&nbsp${bisEnterStock.sortingAsk5}&nbsp${bisEnterStock.sortingAsk6}</td>
		</tr>
		<tr>
		<td colspan="9">预计出库时间:${bisEnterStock.etdWarehouse}&nbsp&nbsp&nbsp
		&nbsp<input type="checkbox" id="ifBonded" name="ifBonded" value="Y" readonly="readonly"/><label>是否保税</label>
		&nbsp<input type="checkbox" id="ifMacAdmit" name="ifMacAdmit" value="Y" readonly="readonly"/><label>是否MSC认证</label>
		</td>
		</tr>
		<tr>
		<td colspan="9">其他要求：
		存储温度：${bisEnterStock.temperature}
		</td>
		</tr>
		<tr>
		<td colspan="9">
		<input type="checkbox" id="ifWeigh" name="ifWeigh" value="Y" readonly/><label>称重;</label>
		<input type="checkbox" id="receptacle" name="receptacle" value="Y" readonly/><label>物流容器：</label>
		<input type="checkbox" id="ifWithWooden" name="ifWithWooden" value="Y" readonly/><label>自带木托；</label>
		<input type="checkbox" id="receptacle1" name="receptacle" value="1" readonly/><label>a.木托；</label>
		<input type="checkbox" id="receptacle2" name="receptacle" value="2" readonly/><label>b.铁架；</label>
		<input type="checkbox" id="receptacle3" name="receptacle" value="3" readonly/><label>c.木铁组合；</label>
		</td>
		</tr>
		<tr>
		<td colspan="9">
		<input type="checkbox" id="ifBagging" name="ifBagging" value="Y" readonly/><label>是否套袋；</label>
		<input type="checkbox" id="ifWrap" name="ifWrap" value="Y" readonly/><label>是否缠膜；</label>
		</td>
		</tr>
		<tr>
		<td>随附单据</td>
		<td colspan="9">
		<input type="checkbox" id="ifAttachBill" name="ifAttachBill" value="Y" readonly/><label>提单；</label>
		<input type="checkbox" id="ifAttachCtnInfo" name="ifAttachCtnInfo" value="Y" readonly/><label>箱单；</label>
		<input type="checkbox" id="ifAttachDetial" name="ifAttachDetial" value="Y" readonly/><label>入库货物明细；</label>
		<input type="checkbox" id="else" name="else" value="Y" readonly/><label>其它：</label>
		</td>
		</tr>
		<tr>
		<td>特殊要求</td>
		<td colspan="9">${bisEnterStock.sortingSpecialAsk1}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp${bisEnterStock.sortingSpecialAsk1}</td>
		</tr>
		<tr>
		<td>备注</td>
		<td colspan="9">${bisEnterStock.remark}</td>
		</tr>
		<tr>
		<td rowspan="1">财务</td>
		<td rowspan="1" colspan="10">
		收费类型
		<input type="checkbox"  value="Y" readonly/><label>标准收费：</label>
		<input type="checkbox"  value="Y" readonly/><label>合同收费：</label>
		<input type="checkbox"  value="Y" readonly/><label>临时收费：</label>
		<input type="checkbox"  value="Y" readonly/><label>其它：</label>
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
		<input type="checkbox"   value="Y" readonly/><label>客户付费使用 </label>
		<input type="checkbox"   value="Y" readonly/><label align="right">客户不付费 </label>
		</td>
		<td>现场确认</td>
		<td colspan="3">
		<input type="checkbox"   value="Y" readonly/><label>顶部加固 </label>
		<input type="checkbox"   value="Y" readonly/><label align="right">全部加固 </label>
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
    var i = 0;
    for(i=0;i<infoSize;i++){
      var info = "bisEnterStockInfo" + i ;
   	  var ttt=i+5;
    	  addRow(ttt,info);
    }
    var fin = infoSize + 14;
    tb.rows[1].cells[0].rowSpan = fin;
    
	var allPiece = ${bisEnterStockInfo1.piece};
	var allNet   = ${bisEnterStockInfo1.netWeight};
	var allGross = ${bisEnterStockInfo1.grossWeight};
	$("#allPiece").val(allPiece);
	$("#allNet").val(allNet);
	$("#allGross").val(allGross);
	$("input[name='ifMacAdmit'][value=${bisEnterStock.ifMacAdmit}]").attr("checked",true);
	$("input[name='ifBonded'][value=${bisEnterStock.ifBonded}]").attr("checked",true);
	$("input[name='ifWeigh'][value=${bisEnterStock.ifWeigh}]").attr("checked",true);
	$("input[name='receptacle'][value=${bisEnterStock.receptacle}]").attr("checked",true);
	$("input[name='receptacle'][value=Y]").attr("checked",true);
	$("input[name='ifWithWooden'][value=${bisEnterStock.ifWithWooden}]").attr("checked",true);
	$("input[name='ifBagging'][value=${bisEnterStock.ifBagging}]").attr("checked",true);
	$("input[name='ifWrap'][value=${bisEnterStock.ifWrap}]").attr("checked",true);
	$("input[name='ifAttachBill'][value=${bisEnterStock.ifAttachBill}]").attr("checked",true);
	$("input[name='ifAttachCtnInfo'][value=${bisEnterStock.ifAttachCtnInfo}]").attr("checked",true);
	$("input[name='ifAttachDetial'][value=${bisEnterStock.ifAttachDetial}]").attr("checked",true);
	if($("#ifSorting").val() == "Y"){
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
