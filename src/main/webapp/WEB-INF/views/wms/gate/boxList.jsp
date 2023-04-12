<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<div id="tb">

    <div style="padding:5px;height:auto" class="datagrid-toolbar">

        <form id="searchFrom" action="">

            <input type="text" name="filter_LIKES_ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>
	        <input type="text" name="filter_LIKES_carNum" class="easyui-validatebox" data-options="width:120,prompt: '车牌号'"/>
	        <input type="text" name="filter_GED_createDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '入场时间'"/>
	      - <input type="text" name="filter_LED_createDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '入场时间'"/>
	         <input type="text" name="filter_GED_outDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '离场时间'"/>
	      - <input type="text" name="filter_LED_outDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '离场时间'"/> 
	        <input type="text" name="filter_LIKES_platform" class="easyui-validatebox" data-options="width:120,prompt: '月台口'"/>
	         <select id="statesearch" name="filter_EQS_state" class="easyui-combobox" data-options="width:100,prompt: '作业类型'">
	                <option></option>
	                <option value='1'>入场</option>
	                <option value='2'>在场</option>
	            </select>
			 <select id="statesearch" id="ifH" name="ifH" class="easyui-combobox" data-options="width:100,prompt: '是否在场'">
	                <option value='0' seleted ="true">未离场</option>
	                <option value='1'>已离场</option>
	            </select>
        </form>

	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
    </div>

</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">

    var dg;
    var dlg;


    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }

    $(document).ready(function () {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/gate/boxJson',
            fit: true,
            fitColumns: true,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
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
                {field: 'clientName', title: '客户名', sortable: true, width: 100},
                {field: 'state', title: '箱状态', sortable: true, width: 100,
	  	        	formatter : function(value, row, index) {
	 	       			if(value==1){
	 	       				return "已入场";
	 	       			}else if(value == 2){
	 	       				return "已在场";
	 	       			}else{
	 	       				return "已离场";
	 	       			}
	 	        	}
 	        	},
                {field: 'asn', title: 'ASN', sortable: true, width: 100},
                {field: 'billNum', title: '提单号', sortable: true, width: 100},
                {field: 'enterNum', title: '计划入库件数', sortable: true, width: 50},
                {field: 'loadingNum', title: '装车单号', sortable: true, width: 100},
                {field: 'outNum', title: '计划出库件数', sortable: true, width: 100},
                {field: 'platform', title: '月台口', sortable: true, width: 100},
                {field: 'createDate', title: '入场时间', hidden: true, width: 100},
                {field: 'outDate', title: '离场时间', hidden: true, width: 100},
                {field: 'createUser', title: '创建人', sortable: true, width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });

    });

</script>

</body>

</html>