<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
	<div>
		<form id="searchFrom" action="">
      	    <input type="text" name="filter_LIKES_scrapCode" class="easyui-validatebox" data-options="width:150,prompt: '报损单号'"/>
      	    <input type="text" name="filter_LIKES_sku" class="easyui-validatebox" data-options="width:150,prompt: 'SKU'"/>
	        <input type="text" name="filter_GED_scrapDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '报损开始日期'"/>
	      - <input type="text" name="filter_LED_scrapDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '报损结束日期'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cxdg()">查询</a>
		</form>
		<shiro:hasPermission name="wms:scrap:baosun">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-trash" plain="true" onclick="add()">报损</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="wms:scrap:print">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-trash" plain="true" onclick="printT()">报损打印</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
	</div> 
</div>

<table id="dg"></table> 
<div id="dlg"></div>  

<script type="text/javascript">
var dg;
var d;

document.onkeydown = function () {if(event.keyCode == 13){cxdg();}};

$(function(){   
	dg = $('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/scrap/json', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'id',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field:'id',title:'id',hidden:true},    
	        {field:'scrapCode',title:'报损单号',sortable:true,width:100},    
	        {field:'trayCode',title:'托盘号',sortable:true,width:100},   
	        {field:'billNum',title:'提单号',sortable:true,width:100},
	        {field:'asn',title:'ASN',sortable:true,width:100},
	        {field:'sku',title:'SKU',sortable:true,width:100},
	        {field:'scrapType',title:'报损类型',sortable:true,width:100,
	        	formatter: function(value, row, index) {
					if (value == 1) {
						return '普通报损';
					}
					if (value == 2) {
						return '库内分拣报损';
					}
				}	
	        },
	        {field:'scrapState',title:'报损状态',sortable:true,width:100,
	        	formatter: function(value, row, index) {
					if (value == 0) {
						return '待确认';
					}
					if (value == 1) {
						return '已确认';
					}
				}	
	        },
	        {field:'clientName',title:'客户',sortable:true,width:100},
	        {field:'num',title:'数量',sortable:true,width:100},
	        {field:'scrapPerson',title:'报损人',sortable:true,width:100},
	        {field:'scrapDate',title:'报损时间',sortable:true,width:100}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});

//创建查询对象并查询
function cxdg(){
	var obj = $("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

//弹窗增加
function add() {
	d = $("#dlg").dialog({   
	    title: '报损操作',    
	    width: 1000,    
	    height: 440,    
	    href:'${ctx}/wms/scrap/scrapTrayForm',
	    maximizable:true,
	    modal:true
// 	    buttons:[{
// 			text:'确认',
// 			handler:function(){
// 				$("#mainform").submit(); 
// 			}
// 		},{
// 			text:'取消',
// 			handler:function(){
// 				d.panel('close');
// 			}
// 		}]
	});
}

function printT(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.open("${ctx}/wms/scrap/print/"+row.scrapCode);
}

</script>
</body>
</html>