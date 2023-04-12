<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>outNotPaidList</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
        <div>
        	<form id="searchFrom" action="">
				<input type="text" name="filter_LIKES_CONTAINER_NO" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
				<input type="text" name="filter_LIKES_PLAT_NO" class="easyui-validatebox" data-options="width:150,prompt: '车号'"/>
				<input type="text" name="filter_LIKES_T4.NAME" class="easyui-validatebox" data-options="width:150,prompt: '楼上叉车'"/>
				<input type="text" name="filter_LIKES_T5.NAME" class="easyui-validatebox" data-options="width:150,prompt: '现场叉车'"/>
				<input type="text" name="filter_LIKES_STEVEDORE_NAME" class="easyui-validatebox" data-options="width:150,prompt: '装卸工姓名'"/>
       	        <input type="text" name="filter_LIKES_ASN_TRANS_NUM" class="easyui-validatebox" data-options="width:150,prompt: 'ASN'"/>
				<input type="text" name="filter_LIKES_CLIENT_NAME" class="easyui-validatebox" data-options="width:150,prompt: '装卸队'"/>
		        <input type="text" name="filter_GTD_T1.CREATED_TIME" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '开始日期'"/>
		        - <input type="text" name="filter_LTD_T1.CREATED_TIME" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '结束日期'"/>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

				<%--<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="generateCost()">生成费用</a>--%>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="add()">生成费用</a>
<!--
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
					   onclick="exportExcel()">导出EXCEL</a>
					<span class="toolbar-item dialog-tool-separator"></span>
-->
<%--				<br/>
				出库重量:<input type="text" id="outWeight" readonly/>--%>
			</form>
        </div> 
        
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;

$(document).keypress(function(e) {if(e.which == 13) {cx();}});

$(function(){   
	dg=$('#dg').datagrid({    
	method: "get",
    url:'${ctx}/cost/outNotPaid/page',
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
        {field:'id',title:'ids',hidden:true},
		{field:'asnTransNum',title:'装车号',sortable:true,width:100},

		{field:'containerNo',title:'箱号',sortable:false,width:100},
        {field:'platNo',title:'车号',sortable:true,width:100},
		{field:'outWeight',title:'重量',sortable:true,width:100},

		{field:'inOutBoundFlag',title:'出入库标志',sortable:true,width:100,
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
		{field:'tallyName',title:'理货',sortable:true,width:100},
		{field:'forkliftSceneName',title:'现场叉车',sortable:true,width:100},

		{field:'forkliftUpName',title:'楼上叉车',sortable:true,width:100},
		{field:'clientName',title:'装卸队',sortable:true,width:200},

		{field:'stevedoreName',title:'装卸工姓名',sortable:true,width:100},
		{field:'numPlus',title:'参考重量系数',sortable:true,width:100},
        {field:'createdTime',title:'创建时间',sortable:true}
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
	//getOutWeight();
});

function getOutWeight(){
	$.ajax({
		async: false,
		type: "POST",
		url: "${ctx}/cost/platformWorkTicket/outWeight",
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
			$("#outWeight").val(date.outWeight);
		}
	});
}

//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj);
	//getOutWeight();
}

//生成费用  和修改传参一致
function generateCost(){
	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	window.parent.mainpage.mainTabs.addModule('出库装卸管理','cost/outstevedoring/update/' + row.asnTransNum);
	/* 	window.parent.mainpage.mainTabs.addModule('入库装卸管理','cost/outstevedoring/list/'); */
}


function add(){


	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var outWeight=row.outWeight;

	if(outWeight==0){
		parent.$.messager.show({title: "提示", msg: "重量为0，暂不能生成费用！", position: "bottomRight" });
		return;
	}
	var asnNum=row.asnTransNum;
	if(asnNum==''||asnNum==null){
		parent.$.messager.show({title: "提示", msg: "装车单为空！", position: "bottomRight" });
		return;
	}


//判断是否有相应的费用方案
	d=$("#dlg").dialog({
		title: '新增出库装卸',
		width: 480,
		height: 480,
		href:'${ctx}/cost/outNotPaid/add/'+row.id,
		maximizable:true,
		modal:true,
		buttons:[{
			text:'确认',
			handler:function(){
				var sortingNum = $("#sortingNuma").val();
				var manNum = $("#manNuma").val();
				var wrapNum = $("#wrapNuma").val();
				var packNum = $("#packNuma").val();
				var feeId = $('#feeId').combobox("getValue");
				if( manNum == "" || wrapNum == "" || packNum == "" || sortingNum == "" ){
					parent.$.messager.show({title: "提示", msg: "数量不可为空！", position: "bottomRight" });
					return;
				}
				if( manNum ==0 && wrapNum==0 && packNum ==0 && sortingNum ==0){
					parent.$.messager.show({title: "提示", msg: "数量不可全为0！", position: "bottomRight" });
					return;
				}
				if(feeId=="" || $('#clientId').combobox("getValue")==""){
					parent.$.messager.show({title: "提示", msg: "费用方案及装卸队不可为空！", position: "bottomRight" });
					return;
				}
				$.ajax({
					async: false,
					type : "GET",
					url : "${ctx}/cost/outstevedoring/judgefee" ,
					data : {"sortingNum":sortingNum , "manNum":manNum , "wrapNum":wrapNum , "packNum":packNum,"feeId":feeId},
					dataType : "text",
					success : function(msg) {
						if(msg != "success"){
							parent.$.messager.confirm('提示', msg, function(data){
								if(data){
									goonadd();
								}
							})
						}else{
							goonadd();
						}
					}
				});


			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}

//判断是否有相应的费用方案
function judgeFee(){
	var sortingNum = $("#sortingNuma").val();
	var manNum = $("#manNuma").val();
	var wrapNum = $("#wrapNuma").val();
	var packNum = $("#packNuma").val();
	var feeId = $('#feeId').combobox("getValue");
	$.ajax({
		type : "GET",
		url : "${ctx}/cost/enterstevedoring/judgefee" ,
		data : {"sortingNum":sortingNum , "manNum":manNum , "wrapNum":wrapNum , "packNum":packNum,"feeId":feeId},
		dataType : "text",
		success : function(msg) {
			if(msg != "success"){
				parent.$.messager.confirm('提示', msg, function(data){
					if(!data){
						return msg;
					}
				})
			}
		}
	});
}

function goonadd(){
	var clientName = $('#clientId').combobox("getText");
	$("#client").val(clientName);
	var feePlan = $('#feeId').combobox("getText");
	$("#feePlan").val(feePlan);
	$("#mainform").submit();
}

</script>
</body>
</html>