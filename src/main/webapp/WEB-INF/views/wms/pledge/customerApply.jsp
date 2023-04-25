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
      	    <input type="text" name="filter_LIKES_accountId" class="easyui-validatebox" data-options="width:150,prompt: '账户ID'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
               data-options="disabled:false" onclick="comfirm()">通过</a>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               data-options="disabled:false" onclick="refuse()">驳回</a>
            <span class="toolbar-item dialog-tool-separator"></span>
         <span class="toolbar-item dialog-tool-separator"></span>
	</div> 
</div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
document.onkeydown = function () {if(event.keyCode == 13){cx();}};
$(function(){   
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/pledge/customerApply/json',
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'codeNum',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
	    	{field: 'id', title: 'id', hidden: true},
 	        {field:'accountId',title:'账户ID',sortable:true,width:100},
 	       	{field:'customerNumber',title:'货主代码',sortable:true,width:120},
 	      	{field:'customerName',title:'货主名称',sortable:true,width:120},
            {field:'remark',title:'备注',sortable:true,width:300},
			{field:'fileUrl',title:'文件url',sortable:true,width:300},
            {field:'createTime',title:'申请时间',sortable:true,width:120},
            {field: 'confirmStatus', title: '申请状态', sortable: true, width: 100,
                formatter: function (value, row, index) {
            		if(value == "0"){
						value = "未处理"
					}
                    else if (value == "1") {
                        value = "通过"
                    }
					else if (value == "2") {
						value = "驳回"
					}
                    return value
                }
            },
 	    	{field:'comfirmTime',title:'确认时间',sortable:true,width:100}
	    ]],
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

//确认 弹窗
function comfirm() {
    var row = dg.datagrid('getSelected');
    if (rowIsNull(row)) return;
    <%--parent.$.messager.confirm('提示', '确定要通过吗？', function (data) {--%>
	<%--	if (data) {--%>
	<%--		$.ajax({--%>
	<%--			type: 'get',--%>
	<%--			url: "${ctx}/pledge/customerApply/comfirm/" + row.id,--%>
	<%--			success: function (data) {--%>
	<%--				successTip(data, dg);--%>
	<%--				dg.datagrid('load');--%>

	<%--			},--%>

	<%--		});--%>
	<%--	}--%>
    <%--});--%>

	d=$("#dlg").dialog({
		title: '客户管理',
		width: 880,
		height: 550,
		href:'${ctx}/pledge/customerApply/'+row.id+'/pass',
		maximizable:true,
		modal:true,
		buttons:[{
			text:'确认',
			handler:function(){
				saveCustomerInfo();
				d.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}

//驳回
function refuse() {
    var row = dg.datagrid('getSelected');
    if (rowIsNull(row)) return;
    parent.$.messager.confirm('提示', '确定要驳回吗？', function (data) {
		if (data) {
			$.ajax({
				type: 'get',
				url: "${ctx}/pledge/customerApply/refuse/" + row.id,
				success: function (data) {
					successTip(data, dg);
				},
			});
		}
    });
}
</script>
</body>
</html>