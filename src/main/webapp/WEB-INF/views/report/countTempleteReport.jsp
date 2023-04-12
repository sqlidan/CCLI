<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>

<body>

<div id="tb" style="padding:5px;height:auto">

    <div>

        <form id="searchFrom" action="" method="post">

            <input type="hidden" name="ntype" id="ntype"/>

            <input type="text" id="BILL_NUM" name="BILL_NUM" class="easyui-validatebox"
                   data-options="width:150,prompt: '提单号'"/>

            <input type="text" name="CTN_NUM" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>

            <select id="STOCK_NAME" name="STOCK_NAME" class="easyui-combobox" data-options="width:150,prompt: '存货方'">
            </select>

            <input type="text" name="starTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,prompt: '日期起'"/>

            <input type="text" name="endTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,prompt: '日期止'"/>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true"
               onclick="cx()">查询</a>

        </form>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
           onclick="exportExcel()">导出EXCEL</a>

        <span class="toolbar-item dialog-tool-separator"></span>

    </div>

</div>

<table id="dg"></table>

<div id="dlg"></div>

<script type="text/javascript">

    //客户
    $('#STOCK_NAME').combobox({
        method: "GET",
        url: "${ctx}/base/client/getClientAll",
        valueField: 'clientName',
        textField: 'clientName',
        mode: 'remote'
    });

    var dg;

    var d;

    $(function () {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/report/countTemplete/json',
            fit: true,
            fitColumns: false,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'rukuhuozhu', title: '入库货主', sortable: true, width: 200},
                {field: 'tidanhao', title: '入/出库提单号', sortable: true, width: 150},
                {field: 'jizhuangxianghao', title: 'MR/集装箱号', sortable: true, width: 150},
                {field: 'sku', title: 'sku', sortable: true, width: 150},
                {field: 'huowumingcheng', title: '货物名称', sortable: true, width: 200},
                {field: 'zhonglei', title: '种类', sortable: true, width: 50},
                {field: 'churu', title: '入/出', sortable: true, width: 50},
                {field: 'yuefen', title: '月份', sortable: true, width: 100},
                {field: 'shijian', title: '入/出库日期', sortable: true, width: 150},
                {field: 'shuliang', title: '数量', sortable: true, width: 60},
                {field: 'danjing', title: '单件净重', sortable: true, width: 80},
                {field: 'danmao', title: '单件毛重', sortable: true, width: 80},
                {field: 'zongjing', title: '总净重', sortable: true, width: 90},
                {field: 'zongmao', title: '总毛重', sortable: true, width: 90},
                {field: 'shouhuokehu', title: '收货客户', sortable: true, width: 200}

            ]], onLoadSuccess: function () {
                /*var getDate = $("#dg").datagrid('getData');
                 $("#dg").datagrid('mergeCells', {index:0,field:'Money',rowspan:getDate.,colspan:1});
                 if(nhh>0){

                 }*/
            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //导出excle
    function exportExcel() {

        var isValid = $("#searchFrom").form('validate');

        if (isValid) {
            $("#searchFrom").attr("action", "${ctx}/report/countTemplete/outport");
            $("#searchFrom").submit();
        }

    }

    //创建查询对象并查询
    function cx() {
        var isValid = $("#searchFrom").form('validate');
        if (isValid) {
            var obj = $("#searchFrom").serializeObject();
            dg.datagrid('load', obj);
        }
    }

</script>
</body>

</html>