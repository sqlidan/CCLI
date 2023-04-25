<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div style="padding:5px; height:auto" class="datagrid-toolbar">

    <form id="searchFrom" action="">
           <input type="text" id="loadingPlanNum" name="loadingPlanNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '订单号'"/>
            <input type="text" id="loadingTruckNum" name="loadingTruckNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '装车单号'"/>
            <input type="text" id="outLinkId" name="outLinkId" class="easyui-validatebox"
                   data-options="width:150,prompt: '出库联系单号'"/>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
    </form>

</div>

<table id="loadingdg"></table>

<script type="text/javascript">

    $(document).ready(function () {
        $('#loadingdg').datagrid({
            method: "get",
            url: '${ctx}/bis/asn/getLoadingTime',
            fit: true,
            fitColumns: false,
            border: false,
            idField: 'id',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
            	{field: 'loadingTruckNum', title: '装车单号', sortable: false, width: 150},
            	{field: 'loadingTime', title: '重收时间', sortable: false, width: 150},
                {field: 'loadingPlanNum', title: '出库订单号', sortable: false, width: 150},
                {field: 'outLinkId', title: '出库联系单号', sortable: false, width: 180},
                {field: 'carNo', title: '车牌号', sortable: false, width: 100},
                {field: 'piece', title: '总件数', sortable: false, width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false 
        });
    });



    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        $('#loadingdg').datagrid('load', obj);
    }

</script>

</body>
</html>