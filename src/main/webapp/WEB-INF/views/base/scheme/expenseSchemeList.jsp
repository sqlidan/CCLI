<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
        <div>
        	<form id="searchFrom" action="">
        		<input type="text" name="lanhuo" class="easyui-validatebox" data-options="width:150,prompt: '揽货人'"/>
       	        <input type="text" name="filter_LIKES_schemeName" class="easyui-validatebox" data-options="width:150,prompt: '方案名称'"/>
       	        <input type="text" name="filter_LIKES_contractId" class="easyui-validatebox" data-options="width:150,prompt: '合同号'"/>
       	        <input type="text" id="clientName" name="filter_EQS_customsId" class="easyui-validatebox" data-options="width:150,prompt: '客户名称'"/>
       	        <select class="easyui-combobox"  name="filter_EQS_ifGet" data-options="width:150,prompt:'应收&应付'">
					<option value="">应收&应付</option>
					<option value="1">应收</option>
					<option value="2">应付</option>
				</select>
				<select class="easyui-combobox"  name="filter_EQS_programState" data-options="width:150,prompt:'审核&未审核'">
					<option value="">审核&未审核</option>
					<option value="1">已审核</option>
					<option value="0">未审核</option>
				</select>
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			</form>
			<shiro:hasPermission name="base:scheme:add">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('方案录入','base/scheme/createSchemeForm')">添加</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
       		<shiro:hasPermission name="base:scheme:delete">
       			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
        		<span class="toolbar-item dialog-tool-separator"></span>
       		</shiro:hasPermission>
            <shiro:hasPermission name="base:scheme:update">
            	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
            	<span class="toolbar-item dialog-tool-separator"></span>
            </shiro:hasPermission>
            <shiro:hasPermission name="base:scheme:copy">
            	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="toCopy()">复制</a>
            	<span class="toolbar-item dialog-tool-separator"></span>
            </shiro:hasPermission>
            <shiro:hasPermission name="base:scheme:check">
            	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-zoom" plain="true" onclick="check()">查看</a>
            	<span class="toolbar-item dialog-tool-separator"></span>
            </shiro:hasPermission>
            <shiro:hasPermission name="base:scheme:pass">
            	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="isOK()">审核</a>
	    		<span class="toolbar-item dialog-tool-separator"></span>
            </shiro:hasPermission>
			<shiro:hasPermission name="base:scheme:pass">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="noOK()">取消审核</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">Excel导出</a>
	    	<span class="toolbar-item dialog-tool-separator"></span>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcelInfo()">Excel明细导出</a>
	    	<span class="toolbar-item dialog-tool-separator"></span>
	 <!--   	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="saveall()">saveall</a>
	    	<span class="toolbar-item dialog-tool-separator"></span> -->
	    	
<!-- 	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportPDF()">PDF导出</a> -->
<!-- 	    	<span class="toolbar-item dialog-tool-separator"></span> -->
        </div> 
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
$(document).keypress(function(e) {  
    // 回车键事件  
       if(e.which == 13) {  
   		cx(); 
       }  
   }); 
$(function(){   
	//客户下拉
	$('#clientName').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote' 
   	});
	
	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/base/scheme/json', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'schemeNum',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field:'schemeNum',title:'方案编号',sortable:true,width:100},  
	        {field:'schemeName',title:'方案名称',sortable:true,width:100},
	        {field:'contractId',title:'合同号',sortable:true,width:100},
//	        {field:'programType',title:'方案类型',sortable:true,width:100,
//	        	formatter: function(value, row, index) {
//					if (value == 1) {
//						return '1';
//					}
//					if (value == 2) {
//						return '2';
//					}
//					if (value == 3) {
//						return '3';
//					}
//					if (value == 4) {
//						return '4';
//					}
//				}
//	        },
	        {field: 'customsName', title:'客户名称', sortable:true, width:100},
//	        {field: 'bisType', title:'业务类型', sortable:true, width:100,
//	        	formatter: function(value, row, index) {
//					if (value == 1) {
//						return '1';
//					}
//					if (value == 2) {
//						return '2';
//					}
//					if (value == 3) {
//						return '3';
//					}
//					if (value == 4) {
//						return '4';
//					}
//				}	
//	        },
	        {field: 'ifGet', title:'收付类型', sortable:true, width:100,
	        	formatter: function(value, row, index) {
					if (value == 1) {
						return '应收';
					}
					if (value == 2) {
						return '应付';
					}
					
				}	
	        },
	        {field:'programState',title:'方案状态', sortable:true,width:100,
	        	formatter: function(value, row, index) {
	       			return value == 1 ? '已审核':'未审核';
	        	}
	        },
	        {field:'operatorPerson', title:'创建人', sortable:true, width:100},
	        {field:'operateTime', title:'创建时间', sortable:true, width:100}
	    ]],
	    rowStyler:function(rowIndex,rowData){  
             //已审核的变色  
             if(rowData.programState=="1" )  
             {  
                return 'background-color:#eee';  
             }  
        },  
        onSelect:function(rowIndex,rowData){
//          console.log( $("tr[datagrid-row-index="+rowIndex+"]").attr("style") );
//  		console.log( $("tr[style*='rgb(255, 228, 141)']").attr("datagrid-row-index") );
  			var tt=$("tr[style*='ffe48d']").attr("datagrid-row-index");
 			var row = $('#dg').datagrid('getData').rows[tt];
 			if(typeof(row) != 'undefined'){
	 			if( row.programState=="1" ){
					$("tr[style*='ffe48d").attr("style","height: 26px; background-color:#eee");
	 			}else{
	 				$("tr[style*='ffe48d").attr("style","height: 26px; background-color:#FFFFFF");
	 			}
			}
 			$("tr[datagrid-row-index="+rowIndex+"]").attr("style","height: 26px; background-color:#ffe48d");
 			
        },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});

//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();

	dg.datagrid('load',obj);
}

//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if(row.programState == 1){
		parent.$.messager.show({ title : "提示",msg: "该方案已审核不允许删除！", position: "bottomRight" });
	}else{
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
			if (data){
				$.ajax({
					type:'GET',
					url:"${ctx}/base/scheme/deleteScheme/"+ row.schemeNum,
					success: function(data){
						successTip(data,dg);
					}
				});
			} 
		});
	}
}

//审核
function isOK(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '您确定要审核通过？', function(data){
		if (data){
			$.ajax({
				type:'GET',
				url:"${ctx}/base/scheme/passOkScheme/"+ row.schemeNum,
				success: function(data){
					successTip(data,dg);
				}
			});
		} 
	});
}

// 取消审核
function noOK(){

	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '您确定要取消审核？', function(data){
		if (data){
			$.ajax({
				type:'GET',
				url:"${ctx}/base/scheme/passNoScheme/"+ row.schemeNum,
				success: function(data){
					successTip(data,dg);
				}
			});
		}
	});

}

//复制
function toCopy(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '您确定要复制该合同？', function(data){
		if (data){
			$.ajax({
				type:'GET',
				url:"${ctx}/base/scheme/copyScheme/"+ row.schemeNum,
				success: function(data){
					successTip(data,dg);
				}
			});
		} 
	});
}

//修改
function update(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if(row.programState == 1){
		parent.$.messager.show({ title : "提示",msg: "该方案已审核不允许修改！", position: "bottomRight" });
	}else{
		window.parent.mainpage.mainTabs.addModule('方案修改','base/scheme/updateSchemeForm/' + row.schemeNum);
	}
}

//导出excel
function exportExcel(){
	var url = "${ctx}/base/scheme/exportSchemeExcel";
	$("#searchFrom").attr("action",url).submit();
}
//导出PDF
function exportPDF(){
	var url = "${ctx}/base/scheme/exportSchemePDF";
	$("#searchFrom").attr("action",url).submit();
}

//导出excel 明细
function exportExcelInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	var url = "${ctx}/base/scheme/exportSchemeInfoExcel/"+ row.schemeNum;
	window.location.href = url;
}

//查看
function check(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('查看方案','base/scheme/checkSchemeForm/' + row.schemeNum);
}

function saveall(){
	$.ajax({
				type:'GET',
				url:"${ctx}/base/scheme/saveall",
				success: function(data){
					alert(data);
				}
			});
}
</script>
</body>
</html>