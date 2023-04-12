<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>PlatformWorkTicket</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
        <div>
        	<form id="searchFrom" action="">
				<input type="text" id="filter_LIKES_CONTAINER_NO" name="filter_LIKES_CONTAINER_NO" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
				<input type="text" id="filter_LIKES_PLAT_NO" name="filter_LIKES_PLAT_NO" class="easyui-validatebox" data-options="width:150,prompt: '车号'"/>
				<select id="status" name="filter_EQS_t1.inOut_Bound_Flag" class="easyui-combobox"
						data-options="width:150,prompt: '出入库类型'">
					<option></option>
					<option value="1">入库</option>
					<option value="2">出库</option>

				</select>
				<input type="text" id="filter_LIKES_T4.NAME" name="filter_LIKES_T4.NAME" class="easyui-validatebox" data-options="width:150,prompt: '楼上叉车'"/>
				<input type="text" id="filter_LIKES_T5.NAME" name="filter_LIKES_T5.NAME" class="easyui-validatebox" data-options="width:150,prompt: '现场叉车'"/>
				<input type="text" id="filter_LIKES_STEVEDORE_NAME" name="filter_LIKES_STEVEDORE_NAME" class="easyui-validatebox" data-options="width:150,prompt: '装卸工姓名'"/>
       	        <input type="text" id="filter_LIKES_ASN_TRANS_NUM" name="filter_LIKES_ASN_TRANS_NUM" class="easyui-validatebox" data-options="width:150,prompt: 'ASN'"/>
				<input type="text" id="filter_LIKES_CLIENT_NAME" name="filter_LIKES_CLIENT_NAME" class="easyui-validatebox" data-options="width:150,prompt: '装卸队'"/>
				<br/>
		        <input type="text" id="filter_GTD_T1.CREATED_TIME" name="filter_GTD_T1.CREATED_TIME" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '开始日期'"/>
		      - <input type="text" id="filter_LTD_T1.CREATED_TIME" name="filter_LTD_T1.CREATED_TIME" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '结束日期'"/>

				<!--
				入库重量:<input type="text" id="enterWeight" readonly/>
				出库重量:<input type="text" id="outWeight" readonly/>
				-->
				<span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">工票信息人员修改</a>
				<span class="toolbar-item dialog-tool-separator"></span>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
					   onclick="exportExcel()">导出EXCEL</a>
					<span class="toolbar-item dialog-tool-separator"></span>

			</form>
        </div> 
        
  </div>
<table id="dg"></table> 
<div id="dlg"></div>
<table id="dgg"></table>
<script type="text/javascript">
var dg;
var d;

$(document).keypress(function(e) {if(e.which == 13) {cx();}});

$(function(){   
	dg=$('#dg').datagrid({    
	method: "get",
    url:'${ctx}/cost/platformWorkTicket/page',
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'ids',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:true,
    columns:[[    
        {field:'ID',title:'ids',hidden:true},
		{field:'ASNTRANSNUM',title:'ASN/装车号',sortable:true,width:100},

		{field:'CONTAINERNO',title:'箱号',sortable:false,width:100},
        {field:'PLATNO',title:'车号',sortable:true,width:100},
		{field:'theWeight',title:'重量',sortable:true,width:100,
			formatter : function(value, row, index) {
				if(1==row.INOUTBOUNDFLAG){
					return Math.floor(row.INWEIGHT/100)/10;
				}else if(2==row.INOUTBOUNDFLAG){
					return Math.floor(row.OUTWEIGHT/100)/10;
				}else{
					return "";
				}
			}
		},
		{field:'INOUTBOUNDFLAG',title:'出入库标志',sortable:true,width:100,
			formatter : function(value, row, index) {
				if(1==value){
					return "入库";
				}else if(2==value){
					return "出库";
				}else{
					return "";
				}
			}
		},

	{field:'TALLYNAME',title:'理货',sortable:true,width:100},
	{field:'FORKLIFTSCENENAME',title:'现场叉车',sortable:true,width:100},
	{field:'FORKLIFTUPNAME',title:'楼上叉车',sortable:true,width:100},
	{field:'CLIENTNAME',title:'装卸队',sortable:true,width:200},
	{field:'STEVEDORENAME',title:'装卸工姓名',sortable:true,width:100},
		{field:'IFALLMAN',title:'是否全人工',sortable:true,width:100,
			formatter : function(value, row, index) {
				if(1==value){
					return "是";
				}else if(0==value){
					return "否";
				}else{
					return "";
				}
			}
		},
	{field:'NUMPLUS',title:'参考重量系数',sortable:true,width:100},
    {field:'CREATEDTIME',title:'创建时间',sortable:true}
    ]],
    headerContextMenu: [
        {text: "冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", true).contains(field); },
         handler: function (e, field) { dg.datagrid("freezeColumn", field); }
        },
        {text: "取消冻结该列", disabled: function (e, field) { return dg.datagrid("getColumnFields", false).contains(field); },
         handler: function (e, field) { dg.datagrid("unfreezeColumn", field); }
        }
    ],
    enableHeaderClickMenu: true,
    enableHeaderContextMenu: true,
    enableRowContextMenu: false,
    toolbar:'#tb'
	});
	//getWeightInfo();
});



//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj);
	//getWeightInfo();
}

//修改
function update(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	d=$("#dlg").dialog({
		title: '工票信息人员修改',
		width: 500,
		height: 340,
		href:'${ctx}/cost/platformWorkTicket/update/'+row.ID,
		maximizable:true,
		modal:true,
		buttons:[{
			text:'修改',
			handler:function(){

				submitForm();
				if(result=="success"){
					d.panel('close');
					cx();
				}

				//cx();
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
	//window.parent.mainpage.mainTabs.addModule('操作员修改','platform/user/manage/update/' + row.id);
}
//导出excel
function exportExcel(){
	var obj=$("#searchFrom").serializeObject();

	var strTime= obj["filter_GTD_T1.CREATED_TIME"];
	var endTime= obj["filter_LTD_T1.CREATED_TIME"] ;

	console.log("strTime",strTime);
	console.log("endTime",endTime);
	if(!strTime || !endTime){
		parent.$.messager.show({ title : "提示",msg: "请选择时间起止范围", position: "bottomRight" });
		return;
	}
	if((new Date(endTime)-new Date(strTime))/(1000*60*60*24)>31){
		parent.$.messager.show({ title : "提示",msg: "时间范围不要大于31天", position: "bottomRight" });
		return;
	}

	var url = "${ctx}/cost/platformWorkTicket/exportExcel";
	$("#searchFrom").attr("action",url).submit();
	//window.location.href = url;
}

function getWeightInfo(){
	$.ajax({
		async: false,
		type: "POST",
		url: "${ctx}/cost/platformWorkTicket/enterAndOutWeight",
		data: {
			"filter_LIKES_CONTAINER_NO": $("#filter_LIKES_CONTAINER_NO").val(),
			"filter_LIKES_PLAT_NO": $("#filter_LIKES_PLAT_NO").val(),
			"filter_LIKES_T4.NAME": $("#filter_LIKES_T4.NAME").val(),
			"filter_LIKES_T5.NAME": $("#filter_LIKES_T5.NAME").val(),
			"filter_LIKES_STEVEDORE_NAME": $("#filter_LIKES_STEVEDORE_NAME").val(),
			"filter_LIKES_ASN_TRANS_NUM": $("#filter_LIKES_ASN_TRANS_NUM").val(),
			"filter_LIKES_CLIENT_NAME": $("#filter_LIKES_CLIENT_NAME").val(),
			"filter_GTD_T1.CREATED_TIME": $("#filter_GTD_T1.CREATED_TIME").val(),
			"filter_LTD_T1.CREATED_TIME": $("#filter_LTD_T1.CREATED_TIME").val()
		},
		dataType: "json",
		success: function (date) {
			$("#enterWeight").val(date.enterWeight);
			$("#outWeight").val(date.outWeight);
		}
	});
}


</script>
</body>
</html>