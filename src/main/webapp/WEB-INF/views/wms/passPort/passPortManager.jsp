<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<div class="easyui-tabs">
	<div title="表头" >
		<div data-options="region:'center'">
			<form id="mainForm">
			<div style="height:auto" class="datagrid-toolbar">
				<shiro:hasPermission name="wms:passPort:add">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
					<span class="toolbar-item dialog-tool-separator"></span>
				</shiro:hasPermission>
				<shiro:hasPermission name="wms:passPort:add">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="clearIt()">清空</a>
					<span class="toolbar-item dialog-tool-separator"></span>
				</shiro:hasPermission>
			</div>
			<table class="formTable" >
				<tr>
					<td style="text-align:right;">预录入统一编号</td>
					<td>
						<input type="hidden" id="id" name="id"  class="easyui-validatebox" value="${passPort.id}">
						<input type="text" id="seqNo" name="seqNo"  class="easyui-validatebox" value="${passPort.seqNo}" data-options="width:180">
					</td>
					<td style="text-align:right;">核放单编号</td>
					<td>
						<input type="text" id="passportNo" name="passportNo"  class="easyui-validatebox" value="${passPort.passportNo}" data-options="width:180">
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>核放单类型</td>
					<td>
						<select id="passportTypecd" name="passportTypecd" class="easyui-combobox" data-options="width:180, required:'required'" value="${passPort.passportTypecd}">
						</select>
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>进出标志</td>
					<td>
						<select id="ioTypecd" name="ioTypecd" class="easyui-combobox" data-options="width:180, required:'required'" value="${passPort.ioTypecd}">
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;"><span style="color: red">*</span>绑定类型</td>
					<td>
						<select id="bindTypecd" name="bindTypecd" class="easyui-combobox" data-options="width:180, required:'required'" value="${passPort.bindTypecd}">
						</select>
					</td>
					<td style="text-align:right;">关联单证类型</td>
					<td>
						<select id="rltTbTypecd" name="rltTbTypecd" class="easyui-combobox" data-options="width:180" value="${passPort.rltTbTypecd}">
						</select>
					</td>
					<td style="text-align:right;">关联单证编号</td>
					<td>
						<input type="text" id="rltNo" name="rltNo"  class="easyui-validatebox" value="${passPort.rltNo}" data-options="width:180, required:'required'"  >
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>主管关区</td>
					<td>
						<select id="masterCuscd" name="masterCuscd" class="easyui-combobox" data-options="width:180, required:'required'" value="${passPort.masterCuscd}">
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">区内账册号</td>
					<td>
						<input type="text" id="areainOriactNo" name="areainOriactNo"  class="easyui-validatebox" value="${passPort.areainOriactNo}" data-options="width:180"  >
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>区内企业编码</td>
					<td>
						<input type="text" id="areainEtpsno" name="areainEtpsno"  class="easyui-validatebox" value="${passPort.areainEtpsno}" data-options="width:180, required:'required'">
					</td>
					<td style="text-align:right;">区内企业社会信用代码</td>
					<td>
						<input type="text" id="areainEtpsSccd" name="areainEtpsSccd"  class="easyui-validatebox" value="${passPort.areainEtpsSccd}" data-options="width:180"  >
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>区内企业名称</td>
					<td>
						<input type="text" id="areainEtpsNm" name="areainEtpsNm"  class="easyui-validatebox" value="${passPort.areainEtpsNm}" data-options="width:180, required:'required'">
					</td>
				</td>
				</tr>
				<tr>
					<td style="text-align:right;">申报单位编码</td>
					<td>
						<input type="text" id="dclEtpsno" name="dclEtpsno"  class="easyui-validatebox" value="${passPort.dclEtpsno}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">申报单位社会信用代码</td>
					<td>
						<input type="text" id="dclEtpsSccd" name="dclEtpsSccd"  class="easyui-validatebox" value="${passPort.dclEtpsSccd}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">申报单位名称</td>
					<td>
						<input type="text" id="dclEtpsNm" name="dclEtpsNm"  class="easyui-validatebox" value="${passPort.dclEtpsNm}" data-options="width:180"  >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">企业内部编号</td>
					<td>
						<input type="text" id="etpsPreentNo" name="etpsPreentNo"  class="easyui-validatebox" value="${passPort.etpsPreentNo}" data-options="width:180" >
					</td>
					<td style="text-align:right;">承运车牌号</td>
					<td>
						<input type="text" id="vehicleNo" name="vehicleNo"  class="easyui-validatebox" value="${passPort.vehicleNo}" data-options="width:180" >
					</td>
					<td style="text-align:right;">IC卡号(电子车牌)</td>
					<td>
						<input type="text" id="vehicleIcNo" name="vehicleIcNo"  class="easyui-validatebox" value="${passPort.vehicleIcNo}" data-options="width:180">
					</td>
				</tr>
				<tr>
					<td style="text-align:right;"><span style="color: red">*</span>车自重</td>
					<td>
						<input type="text" id="vehicleWt" name="vehicleWt"  class="easyui-validatebox" value="${passPort.vehicleWt}" data-options="width:180, required:'required'" >
					</td>
					<td style="text-align:right;">车架号</td>
					<td>
						<input type="text" id="vehicleFrameNo" name="vehicleFrameNo"  class="easyui-validatebox" value="${passPort.vehicleFrameNo}" data-options="width:180" >
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>车架重</td>
					<td>
						<input type="text" id="vehicleFrameWt" name="vehicleFrameWt"  class="easyui-validatebox" value="${passPort.vehicleFrameWt}" data-options="width:180, required:'required'">
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">集装箱号</td>
					<td>
						<input type="text" id="containerNo" name="containerNo"  class="easyui-validatebox" value="${passPort.containerNo}" data-options="width:180" >
					</td>
					<td style="text-align:right;">集装箱箱型</td>
					<td>
						<input type="text" id="containerType" name="containerType"  class="easyui-validatebox" value="${passPort.containerType}" data-options="width:180" >
					</td>
					<td style="text-align:right;">集装箱重</td>
					<td>
						<input type="text" id="containerWt" name="containerWt"  class="easyui-validatebox" value="${passPort.containerWt}" data-options="width:180">
					</td>
				</tr>
				<tr>
					<td style="text-align:right;"><span style="color: red">*</span>货物总毛重</td>
					<td>
						<input type="text" id="totalGrossWt" name="totalGrossWt"  class="easyui-validatebox" value="${passPort.totalGrossWt}" data-options="width:180, required:'required'" >
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>货物总净重</td>
					<td>
						<input type="text" id="totalNetWt" name="totalNetWt"  class="easyui-validatebox" value="${passPort.totalNetWt}" data-options="width:180, required:'required'" >
					</td>
					<td style="text-align:right;">总重量</td>
					<td>
						<input type="text" id="totalWt" name="totalWt"  class="easyui-validatebox" value="${passPort.totalWt}" data-options="width:180">
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">过卡状态</td>
					<td>
						<select id="lockage" name="lockage" class="easyui-combobox" data-options="width:180" value="${passPort.lockage}">
						</select>
					</td>
					<td style="text-align:right;">过卡时间1</td>
					<td>
						<input id="lockageTime1" name="lockageTime1" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${lockageTime1}" pattern="yyyy-MM-dd HH:mm:ss" />" />
					</td>
					<td style="text-align:right;">过卡时间2</td>
					<td>
						<input id="lockageTime2" name="lockageTime2" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${lockageTime2}" pattern="yyyy-MM-dd HH:mm:ss" />" />
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">录入单位编码</td>
					<td>
						<input type="text" id="inputCode" name="inputCode"  class="easyui-validatebox" value="${passPort.inputCode}" data-options="width:180" >
					</td>
					<td style="text-align:right;">录入单位社会信用代码</td>
					<td>
						<input type="text" id="inputSccd" name="inputSccd"  class="easyui-validatebox" value="${passPort.inputSccd}" data-options="width:180" >
					</td>
					<td style="text-align:right;">录入单位名称</td>
					<td>
						<input type="text" id="inputName" name="inputName"  class="easyui-validatebox" value="${passPort.inputName}" data-options="width:180" >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">录入日期</td>
					<td>
						<input id="createTime" name="createTime" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
					</td>
					<td style="text-align:right;">申报类型</td>
					<td>
						<select id="dclTypecd" name="dclTypecd" class="easyui-combobox" data-options="width:180" value="${passPort.dclTypecd}">
						</select>
					</td>
					<td style="text-align:right;">申报人</td>
					<td>
						<input type="text" id="dclBy" name="dclBy"  class="easyui-validatebox" value="${passPort.dclBy}" data-options="width:180" >
					</td>
					<td style="text-align:right;">申报日期</td>
					<td>
						<input id="dclTime" name="dclTime" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${dclTime}" pattern="yyyy-MM-dd HH:mm:ss" />" />
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">到货确认</td>
					<td>
						<select id="col1" name="col1" class="easyui-combobox" data-options="width:180" value="${passPort.col1}">
						</select>
					</td>
					<td style="text-align:right;">备注</td>
					<td>
						<input type="text" id="rmk" name="rmk"  class="easyui-validatebox" value="${passPort.rmk}" data-options="width:180" >
					</td>
				</tr>
			</table>
		</form>
		</div>
		<div data-options="region:'south',split:true,border:false" title="关联单证" style="height:500px">
			<div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
				<div>
					<shiro:hasPermission name="wms:passPort:add">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addDJInfo()">添加关联</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
					<shiro:hasPermission name="wms:passPort:add">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delDJInfo()">删除</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
<%--					<shiro:hasPermission name="wms:passPort:add">--%>
<%--						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="submit()">提交</a>--%>
<%--						<span class="toolbar-item dialog-tool-separator"></span>--%>
<%--					</shiro:hasPermission>--%>
				</div>
			</div>
			<table id="dg" ></table>
			<div id="dlg"></div>
		</div>
	</div>
	<div title="表体" >
		<div data-options="region:'center'">
			<form id="mainForm2"  >
				<div style="height:auto" class="datagrid-toolbar">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="clearIt2()">清空</a>
						<span class="toolbar-item dialog-tool-separator"></span>
				</div>
				<table class="formTable" >
					<tr>
						<td style="text-align:right;">序号</td>
						<td>
							<input type="text" id="passportSeqno" name="passportSeqno"  class="easyui-validatebox" data-options="width:180" value="${passPortInfo.passportSeqno}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>关联商品序号</td>
						<td>
							<input type="text" id="rltGdsSeqno" name="rltGdsSeqno" onblur="getGdsInfo()"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${passPortInfo.rltGdsSeqno}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>商品料号</td>
						<td>
							<input type="text" id="gdsMtno" name="gdsMtno"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${passPortInfo.gdsMtno}" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>商品编号</td>
						<td>
							<input type="text" id="gdecd" name="gdecd"  class="easyui-validatebox" data-options="width:180, required:'required'"  value="${passPortInfo.gdecd}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>商品名称</td>
						<td>
							<input type="text" id="gdsNm" name="gdsNm"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${passPortInfo.gdsNm}">
						</td>
						<td style="text-align:right;">申报计量单位</td>
						<td>
							<select id="dclUnitcd" name="dclUnitcd" class="easyui-combobox" data-options="width:180" value="${passPortInfo.dclUnitcd}">
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>申报数量</td>
						<td>
							<input type="text" id="dclQty" name="dclQty"  class="easyui-validatebox" data-options="width:180, required:'required'"  value="${passPortInfo.dclQty}">
						</td>
						<td style="text-align:right;">货物毛重</td>
						<td>
							<input type="text" id="grossWt" name="grossWt"  class="easyui-validatebox" data-options="width:180" value="${passPortInfo.grossWt}">
						</td>
						<td style="text-align:right;">货物净重</td>
						<td>
							<input type="text" id="netWt" name="netWt"  class="easyui-validatebox" data-options="width:180" value="${passPortInfo.netWt}">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">备注</td>
						<td>
							<input type="text" id="remark" name="remark"  class="easyui-validatebox" data-options="width:180" value="${passPortInfo.remark}"  >
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'south',split:true,border:false" title="列表" style="height:600px">
			<div id="tb2" style="padding:5px;height:auto" class="datagrid-toolbar">
				<div>
					<shiro:hasPermission name="wms:passPort:add">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
					<shiro:hasPermission name="wms:passPort:add">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
					<shiro:hasPermission name="wms:passPort:add">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="copyInfo()">复制</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
				</div>
			</div>
			<table id="dg2" ></table>
		</div>
	</div>
</div>


<script type="text/javascript">
var dg;
var dg2;
var sfdj;
var d;
var passPortId = "${passPort.id}";
var b=0;
var dhs;
var dt;


var dclUnitcdAry;//计量单位

//初始化
$(function(){
	gridDG(passPortId);
	gridDG2(passPortId);
	selectAjax();
});
function selectAjax(){
	//表头
	//核放单类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_PASSPORT_TYPECD",
		success : function(date) {
			$('#passportTypecd').combobox({
				data : date.rows,
				value : '${passPort.passportTypecd}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//进出标志
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_IO_TYPECD",
		success : function(date) {
			$('#ioTypecd').combobox({
				data : date.rows,
				value : '${passPort.ioTypecd}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//绑定类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_BIND_TYPECD",
		success : function(date) {
			$('#bindTypecd').combobox({
				data : date.rows,
				value : '${passPort.bindTypecd}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//关联单证类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_RLT_TB_TYPECD",
		success : function(date) {
			$('#rltTbTypecd').combobox({
				data : date.rows,
				value : '${passPort.rltTbTypecd}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//主管关区
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DCL_PLC_CUSCD",
		success : function(date) {
			$('#masterCuscd').combobox({
				data : date.rows,
				value : '${passPort.masterCuscd}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//过卡状态
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/passPortInfo/dictData/lockage",
		success : function(date) {
			$('#lockage').combobox({
				data : date.rows,
				value : '${passPort.lockage}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//申报类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DCLTYPECD",
		success : function(date) {
			$('#dclTypecd').combobox({
				data : date.rows,
				value : '${passPort.dclTypecd}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//到货确认
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/passPortInfo/dictData/col1",
		success : function(date) {
			$('#col1').combobox({
				data : date.rows,
				value : '${passPort.col1}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});

	//表体
	//计量单位
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=unitOfWeight",
		dataType : "json",
		success : function(date) {
			dclUnitcdAry = date.rows;
			$('#dclUnitcd').combobox({
				data : date.rows,
				value : '${passPortInfo.dclUnitcd}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
}
//================================================================================================================================
//查询单据列表
function gridDG(passPortId){
   dg =$('#dg').datagrid({
		method: "get",
	    url:'${ctx}/wms/passPortInfo/jsonDJ?passPortId='+passPortId,
	    fit : true,
		fitColumns : true,
		border : false,
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 20, 50, 100],
		singleSelect:false,
	    columns:[[
	    	{field:'id',hidden:true},
			{field:'rtlNo',title:'关联单证编号',sortable:true},
 	        {field:'createTime',title:'添加时间',sortable:true},
 	        {field:'remark',title:'备注',sortable:true}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}
//查询预录入表体
function gridDG2(passPortId){
	dg2 =$('#dg2').datagrid({
		method: "get",
		url:'${ctx}/wms/passPortInfo/json?passPortId='+passPortId,
		fit : true,
		fitColumns : true,
		border : false,
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 20, 50, 100],
		singleSelect:false,
		columns:[[
			{field:'id',hidden:true},
			{field:'passportSeqno',title:'序号',sortable:true},
			{field:'rltGdsSeqno',title:'关联商品序号',sortable:true},
			{field:'gdsMtno',title:'商品料号',sortable:true},
			{field:'gdecd',title:'商品编码',sortable:true},
			{field:'gdsNm',title:'商品名称',sortable:true},
			{field:'dclQty',title:'申报数量',sortable:true},
			{field:'dclUnitcd',title:'计量单位',sortable:true,
				formatter : function(value, row, index) {
					var lbelStr;
					for (let i = 0; i < dclUnitcdAry.length; i++) {
						let row = dclUnitcdAry[i];
						if(row.value == value){
							lbelStr = row.label;
							break;
						}
					}
					return lbelStr;
				}},
		]],
		enableHeaderClickMenu: true,
		enableHeaderContextMenu: true,
		enableRowContextMenu: false,
		toolbar:'#tb2'
	});
}
//================================================================================================================================
//清空
function clearIt(){
	$('#mainForm').form('clear');
}
//表头保存
function submitForm(){
	if($("#mainForm").form('validate')){
		var passportTypecd = $("#passportTypecd").combobox("getValue");
		var areainOriactNo = $("#areainOriactNo").val();
		if(passportTypecd !== undefined && passportTypecd !== null && passportTypecd.toString().trim().length > 0){
			if(parseInt(passportTypecd) == 6){
				// console.log("空车进出区")
			}else{
				if(areainOriactNo !== undefined && areainOriactNo !== null && areainOriactNo.toString().trim().length > 0){
					// console.log("区内账册号有值")
				}else{
					parent.$.messager.show({title: "提示", msg: "核放单类型不为空车进出区时请填写区内账册号！", position: "bottomRight" });
					return;
				}
			}
		}else{
			parent.$.messager.show({title: "提示", msg: "请选择核放单类型！", position: "bottomRight" });
			return;
		}
		if(passPortId){
			type = "update";
			//用ajax提交form
			$.ajax({
				async: false,
				type: 'POST',
				url: "${ctx}/wms/passPortInfo/update",
				data: $('#mainForm').serialize(),
				dataType: "text",
				success: function(msg){
					if(msg == "success"){
						parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
					}
				}
			});
		}else{
			//用ajax提交form
			$.ajax({
				async: false,
				type: 'POST',
				url: "${ctx}/wms/passPortInfo/insert",
				data: $('#mainForm').serialize(),
				dataType: "text",
				success: function(resStr){
					var res = JSON.parse(resStr);
					if(res.msg == "success"){
						parent.$.messager.show({ title : "提示",msg: "操作成功！ID:"+res.id, position: "bottomRight" });
						passPortId = res.id;
						$("#id").val(passPortId);
					}
				}
			});
		}

 	}
}
//================================================================================================================================
//依据关联商品序号获取商品信息
function getGdsInfo(){
	var rltGdsSeqno = $("#rltGdsSeqno").val();
	if(rltGdsSeqno !== undefined && rltGdsSeqno !== null && rltGdsSeqno.toString().trim().length > 0){
		//用ajax提交form
		$.ajax({
			async: false,
			type: 'GET',
			url: "${ctx}/wms/passPortInfo/getGdsInfo?rltGdsSeqno="+rltGdsSeqno,
			dataType: "text",
			success: function(resStr){
				var res = JSON.parse(resStr);
				if(res.msg == "success"){
					var data = res.data;
					// $("#gdsMtno").val(data.gdsMtno);
					$("#gdecd").val(data.hsCode);
					$("#gdsNm").val(data.hsItemname);
					$('#dclUnitcd').combobox('select',data.dclUnitcd);
					$("#dclQty").val(data.piece);
					$("#grossWt").val(data.hsQty);
					$("#netWt").val(data.netWeight);
				}
			}
		});
	}else{
		parent.$.messager.show({title: "提示", msg: "请先输入关联商品序号！", position: "bottomRight" });
	}
}
//================================================================================================================================
//清空
function clearIt2(){
	$('#mainForm2').form('clear');
}
//表体添加
function addInfo(){
	if($("#mainForm2").form('validate')){
		if(!passPortId){
			parent.$.messager.show({title: "提示", msg: "请先保存表头信息再添加表体信息！", position: "bottomRight" });
		}else{
			//用ajax提交form
			$.ajax({
				async: false,
				type: 'POST',
				url: "${ctx}/wms/passPortInfo/create?passPortId="+passPortId,
				data: $('#mainForm2').serialize(),
				dataType: "text",
				success: function(msg){
					if(msg == "success"){
						$('#mainForm2').form('clear');
						parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
						window.setTimeout(function(){gridDG2(passPortId)},100);
					}else{
						parent.$.messager.show({ title : "提示",msg: msg, position: "bottomRight" });
					}
				}
			});
		}
	}
}
//表体删除
function delInfo(){
	var rows = dg2.datagrid('getSelections');
	var del = dg2.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].id);
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/wms/passPortInfo/deleteinfo/" + ids,
				success: function(data){
					dg2.datagrid('clearSelections');
					successTip(data, dg2);
				}
			});
		}
	});
}
//表体复制
function copyInfo(){
	var copy = dg2.datagrid('getSelected');
	if(copy == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var rows = dg2.datagrid('getSelections');
	if(rows.length > 1){
		parent.$.messager.show({title: "提示", msg: "请选择一行数据进行复制！", position: "bottomRight" });
		return;
	}
	$("#passportSeqno").val(copy.passportSeqno);
	$("#rltGdsSeqno").val(copy.rltGdsSeqno);
	$("#gdsMtno").val(copy.gdsMtno);
	$("#gdecd").val(copy.gdecd);
	$("#gdsNm").val(copy.gdsNm);
	$('#dclUnitcd').combobox('select',copy.dclUnitcd);
	$("#dclQty").val(copy.dclQty);
	$("#grossWt").val(copy.grossWt);
	$("#netWt").val(copy.netWt);
	$("#remark").val(copy.remark);
}

//================================================================================================================================
//添加关联单证
function addDJInfo(){
	if(!passPortId){
		parent.$.messager.show({title: "提示", msg: "请先保存表头信息再添加关联单证信息！", position: "bottomRight" });
	}else{
		dt=$("#dlg").dialog({
			title: '添加关联单证',
			width: 500,
			height: 300,
			href:'${ctx}/wms/passPortInfo/createDJ?passPortId='+passPortId,
			maximizable:true,
			modal:true,
			buttons:[{
				text:'确认',
				handler:function(){
					if($("#mainform").form('validate')){
						$("#mainform").submit();
						dt.panel('close');
					}
				}
			},{
				text:'取消',
				handler:function(){
					dt.panel('close');
				}
			}],
			onClose: function (){
				window.setTimeout(function(){gridDG(passPortId)},100);
			}
		});
	}
}
//关联单证删除
function delDJInfo(){
	var rows = dg.datagrid('getSelections');
	var del = dg.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].id);
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/wms/passPortInfo/deleteDJinfo/" + ids,
				success: function(data){
					dg.datagrid('clearSelections');
					successTip(data, dg);
				}
			});
		}
	});
}
//关联单证提交
function submit(){
	if(!passPortId){
		parent.$.messager.show({title: "提示", msg: "请先保存表头信息再提交关联单证！", position: "bottomRight" });
	}else{
		parent.$.messager.confirm('提示', '您确定要申报提交吗？', function(data){
			if (data){
				$.ajax({
					type: 'get',
					url: "${ctx}/wms/passPortInfo/submitDJinfo?passPortId=" + passPortId,
					success: function(data){
						dg.datagrid('clearSelections');
						successTip(data, dg);
					}
				});
			}
		});
	}
}
</script>
</body>
</html>