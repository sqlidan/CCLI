<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<style>
	tr{
	height:30px;
	} 
</style>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="入闸" style="overflow-y:auto">
		<div   style="padding:5px;height:auto;background:#ffffff;" class="datagrid-toolbar">
		<table>
        <tr>
            <td>
                <a class="easyui-linkbutton" iconCls="icon-add"  onclick="searchBis()">业务关联</a>
            </td>
            <td>
                <a class="easyui-linkbutton" iconCls="icon-save"  onclick="saveBis()">入闸</a>
            </td>
            <td>
                <a class="easyui-linkbutton" iconCls="icon-save"  onclick="saveTsR()">特殊入闸</a>
            </td>
            <!-- <td>
                <a class="easyui-linkbutton" iconCls="icon-save"  onclick="saveTsC()">特殊出闸</a>
            </td> -->
        </tr>
    </table>

    <form id="mainForm" action="" method="post">

        <table>
        	<tr>
                <td>车牌号</td>
                <td><input type="text" name="carNum" id="carNum" class="easyui-validatebox"
                           data-options="width:240,prompt: '车牌号',required:'required'" />
               </td>
                <td align="right">司机姓名</td>
                <td><input type="text" name="driverName" id="driverName" class="easyui-validatebox"
                           data-options="width:240,prompt: '司机名称'" />
                </td>
            </tr>
			<tr>
            <td>集装箱号</td>
            <td><input type="text" id="ctnNum" name="ctnNum" class="easyui-validatebox" data-options="width:240,prompt: '集装箱号'"
                      />
            </td>
            <td align="right">箱型</td>
            <td><select type="text" name="ctnType" id="ctnType" class="easyui-combobox"
                           data-options="width:240" ></select>
            </td>
            </tr>
			<tr>
                <td>尺寸</td>
                <td><select type="text" name="ctnSize" id="ctnSize" class="easyui-combobox"
                           data-options="width:240" ></select>
                </td>
                <td align="right">业务类型</td>
                <td><input type="text" id="typeName" class="easyui-validatebox"
                           data-options="width:240 "  readonly="readonly"/>
                </td>
                <td><input type="hidden" name="bisType" id="bisType" class="easyui-validatebox"
                           data-options="width:240 "  readonly="readonly"/>
                </td>
                
            </tr>
            <tr>
                <td>ASN</td>
                <td><input type="text"  id="asn" name="asn" class="easyui-validatebox"  data-options="width:240,prompt: 'ASN' " readonly="readonly"/>
                </td>
                <td align="right">装车单号</td>
                <td><input type="text" name="loadingNum" id="loadingNum" class="easyui-validatebox"
                            data-options="width:240,prompt: '装车单号' " readonly="readonly"></input>
                </td>
            </tr>
            <tr>
                <td>提单号</td>
                <td><input type="text" name="billNum" id="billNum" class="easyui-validatebox"
                           data-options="width:240,prompt: '提单号'" readonly="readonly"/>
                </td>
                <td align="right">出库件数</td>
                <td><input type="text" name="outNum" id="outNum" class="easyui-validatebox"
                            data-options="width:240,prompt: '出库件数' "  readonly="readonly"></input>
                </td>
            </tr>
            <tr>
                <td>入库计划件数</td>
                <td><input type="text" name="enterNum" id="enterNum" class="easyui-validatebox"
                           data-options="width:240,prompt: '计划入库件数' " readonly="readonly"/>
                </td>
            </tr>

            <tr>
                <td>客户名称</td>
                <td><input type="text" name="stockName" id="stockName" class="easyui-validatebox"
                            data-options="width:240,prompt: '存货方名称' "  readonly="readonly"></input>
                    <input type="hidden" name="stockId" id="stockId" class="easyui-validatebox"/>
                </td>
            </tr>
            <tr height="100px"></tr>
        </table>
    </form>
</div>
<div  data-options="region:'south',split:true,border:false" title="入库预报单货物信息"  style="height:400px">
<form id="searchFrom" action="">
	<div  style="padding:5px;height:auto" class="datagrid-toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<input type="text" name="filter_LIKES_ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>
        <input type="text" name="filter_LIKES_carNum" class="easyui-validatebox" data-options="width:120,prompt: '车牌号'"/>
        <input type="text" name="filter_GED_createDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '入场时间'"/>
      - <input type="text" name="filter_LED_createDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '入场时间'"/>
        <input type="text" name="filter_LIKES_platform" class="easyui-validatebox" data-options="width:120,prompt: '月台口'"/>
         <select id="statesearch" name="filter_EQS_bisType" class="easyui-combobox" data-options="width:100,prompt: '业务类型'">
                <option></option>
                <option value='1'>入库</option>
                <option value='2'>出库</option>
                <option value='3'>无作业任务</option>
                <option value='4'>特殊作业任务</option>
            </select>
	</div>
</form>
	<div  style="padding:5px;height:auto" class="datagrid-toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="platform()">分配月台口</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="leave()">出闸</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del()">删除</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updateCarNo()">修改车牌号</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="fullScreen()">大屏展示</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="callCar()">呼叫车辆</a>
	</div> 
	
<table id="dgg"></table> 
</div>

<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg;

$(function(){   
	$(document).ready(function () {
        dgg = $('#dgg').datagrid({
            method: "get",
            url: '${ctx}/wms/gate/page',
            fit: false,
            fitColumns: true,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 10,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
            	{field: 'id', title: 'id', hidden:true},
            	{field: 'carNum', title: '车牌号', sortable: true, width: 100},
                {field: 'driverName', title: '司机名', sortable: true, width: 100},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 100},
                {field: 'ctnType', title: '箱型', sortable: true, width: 50},
                {field: 'ctnSize', title: '箱尺寸', sortable: true, width: 50},
                {field: 'platform', title: '月台口', sortable: true, width: 100},
                {field: 'platformtwo', title: '月台口2', sortable: true, width: 100},
                {field: 'stockName', title: '客户名', sortable: true, width: 100},
                {field: 'bisType', title: '业务类型', sortable: true, width: 100,
	  	        	formatter : function(value, row, index) {
	 	       			if(value==1){
	 	       				return "入库";
	 	       			}else if(value == 2){
	 	       				return "出库";
	 	       			}else if(value==3){
	 	       				return "无业务";
	 	       			}else{
	 	       			    return "特殊作业";
	 	       			}
	 	        	}
 	        	},
                {field: 'jobType', title: '作业类型', sortable: true, width: 100,
	  	        	formatter : function(value, row, index) {
	 	       			if(value==1){
	 	       				return "入闸";
	 	       			}else if(value == 2){
	 	       				return "出闸";
	 	       			}else if(value == 3){
	 	       				return "无作业";
	 	       			}else{
	 	       			    return "特殊作业";
	 	       			}
	 	        	}
	 	        },
                {field: 'asn', title: 'ASN', sortable: true, width: 100},
                {field: 'billNum', title: '提单号', sortable: true, width: 100},
                {field: 'enterNum', title: '计划入库件数', sortable: true, width: 50},
                {field: 'loadingNum', title: '装车单号', sortable: true, width: 100},
                {field: 'outNum', title: '计划出库件数', sortable: true, width: 100},
                {field: 'createDate', title: '入场日期', sortable: true, width: 100},
                {field: 'createUser', title: '创建人', sortable: true, width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false 
        });
    });
    
    
     //箱型
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=ctnType",
            dataType: "json",
            success: function (date) {
                $('#ctnType').combobox({
                    data: date.rows,
                    value: 'RF',
                    valueField: 'label',
                    textField: 'label',
                    editable: false
                });
            }
        });
        
        //箱尺寸
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=ctnSize",
            dataType: "json",
            success: function (date) {
                $('#ctnSize').combobox({
                    data: date.rows,
                    value: '40',
                    valueField: 'label',
                    textField: 'label',
                    editable: false
                });
            }
        });
});

function searchBis(){
	$("#billNum").val("");
	$("#asn").val("");
	$("#stockName").val("");
	$("#stockId").val("");
	$("#enterNum").val("");
	$("#bisType").val("");
	$("#typeName").val("");
	$("#loadingNum").val("");
	$("#outNum").val("");
	if($("#carNum").val()==""){
		toast("请填写车牌号!");
		return;
	}else{
		$.ajax({
	            async: false,
	            type: 'POST',
	            url: "${ctx}/wms/gate/searchBis",
	            data: {'ctnNum':$("#ctnNum").val(),'carNum':$("#carNum").val()},
	            dataType: "json",
	            success: function (date) {
	                if (date.str=="success") {
	                	if(date.type=="ent"){
	                		$("#billNum").val(date.billNum);
	                		$("#asn").val(date.asn);
	                		$("#stockName").val(date.clientName);
	                		$("#stockId").val(date.clientId);
	                		$("#enterNum").val(date.entNum);
	                		$("#bisType").val("1");
	                		$("#typeName").val("入库");
	                	}else{
	                		$("#loadingNum").val(date.loadingNum);
	                		$("#stockName").val(date.clientName);
	                		$("#stockId").val(date.clientId);
	                		$("#outNum").val(date.outNum);
	                		$("#bisType").val("2");
	                		$("#typeName").val("出库");
	                	}
	                    toast("关联成功!");
	                }else if(date.str=="empty"){
	                	$("#bisType").val("3");
	                	$("#typeName").val("无业务");
	                	toast("空车类型，无业务关联!");
	                }else{
	                    toast("保存失败!");
	                }
	
	            }
	        });
        }
}

//保存
function saveBis() {
	if($("#mainForm").form('validate')){
        $.ajax({
            async: false,
            type: 'POST',
            url: "${ctx}/wms/gate/submitBis",
            data: $('#mainForm').serialize(),
            dataType: "text",
            success: function (msg) {
                if (msg == "success") {
                    toast("保存成功!");
                    cx();
                } else if(msg=="overCar"){
                     toast("此车牌号已在场中!无法再次入闸！");
                }else if(msg=="overBox"){
                	 toast("此箱号已在场中！无法再次入场!");
                }else{
                	 toast("保存失败!");
                }
            }
        });
	}
}

//保存特殊入闸
function saveTsR() {
	if($("#mainForm").form('validate')){
        $.ajax({
            async: false,
            type: 'POST',
            url: "${ctx}/wms/gate/submitTsr",
            data: $('#mainForm').serialize(),
            dataType: "text",
            success: function (msg) {
                if (msg == "success") {
                    toast("保存成功!");
                    cx();
                } else if(msg=="overCar"){
                     toast("此车牌号已在场中!无法再次特殊入闸！");
                }else if(msg=="overBox"){
                	 toast("此箱号已在场中！无法再次特殊入场!");
                }else{
                	 toast("保存失败!");
                }
            }
        });
      }
   }
    
//分配月台口
function platform(){
	var row = dgg.datagrid('getSelected');

    if(rowIsNull(row)) return;
            
	d = $("#dlg").dialog({
            title: "月台口添加",
            width: 450,
            height: 450,
            href: '${ctx}/wms/gate/platform/'+row.id,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $("#mainform2").submit();
                     d.panel('close');
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
}


//出闸确认
function leave(){
	var row = dgg.datagrid('getSelected');
    if(rowIsNull(row)) return;
    parent.$.messager.confirm('提示', '确定进行出闸操作？', function (data) {
            if (data) {
            	$.ajax({
		            type: 'post',
		            data: {id: row.id},
		            url: "${ctx}/wms/gate/ifEnd",
		            success: function (date) {
		                if (date == "success") {
		                     outGate(row);
		                } else if(date=="ent"){
		                    parent.$.messager.confirm('提示', '此箱号对应的入库作业任务尚未上架，是否继续出闸？', function (data2) {
            					if (data2) {
            						outGate(row);
            					}
            				})
		                }else{
		                	 parent.$.messager.confirm('提示', '此车对应的出库作业任务尚未装车，是否继续出闸？', function (data2) {
            					if (data2) {
            						outGate(row);
            					}
            				})
		                }
		            }
		        });
            }
   });        
}

//出闸
function outGate(row){
	$.ajax({
            type: 'post',
            data: {id: row.id},
            url: "${ctx}/wms/gate/leave",
            success: function (data) {
                if (data == "success") {
                    toast("出闸成功");
                    cx();
                } else {
                    toast("出闸失败");
                }
            }
        });
}



	//修改车牌号
	function updateCarNo(){
	    var row = dgg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
		d = $("#dlg").dialog({
            title: "车牌号修改",
            width: 450,
            height: 450,
            href: '${ctx}/wms/gate/updateCarNo/'+row.id,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $("#mainform").submit();
                     d.panel('close');
                     cx();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
	}
	
	/**
     * 删除记录
     */
    function del() {
        var row = dgg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                delInfo(row);
            }
        });

    }

    function delInfo(row) {
        $.ajax({
            type: 'post',
            data: {id: row.id},
            url: "${ctx}/wms/gate/delete",
            success: function (data) {

                if (data == "success") {
                    toast("删除成功");
                    cx();
                } else if(data=="ld"){
                    toast("有落地计划，无法删除！");
                }else{
                	toast("删除失败");
                }

            }
        });
    }


//全屏
function fullScreen(){
	var url = '${fullip}';
	window.open("http://"+url+"wms/gate/fullScreen"); 
}


	/**
     * 呼叫安排车辆
     */
    function callCar() {
		var row = dgg.datagrid('getSelected');
        if (rowIsNull(row)) 
        	return;
        if (row.platform == null || row.platform=="") {
            toast("未安排停靠月台口");
            return;
        }
        voiceCar(row.carNum,row.platform,row.platformtwo);
    }

function voiceCar(carNum,platform,platformtwo){
	 $.ajax({
            type: 'post',
            data: {carNum:carNum,platform:platform,platformtwo:platformtwo},
            url: "${ctx}/wms/gate/callCar",
            success: function (data) {
                if (data == "success") {
                    toast("呼叫成功");
                }else{
                	toast("呼叫失败");
                }
            }
        });
}

	//查询
	function cx() {
        var obj = $("#searchFrom").serializeObject();
        dgg.datagrid('load', obj);
    }

function test(){
	$.post("http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&spd=2&text="+ "123");
}


function toast(msg) {
	$.post("http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&spd=2&text="+msg);
        //parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
} 
</script>
</body>
</html>