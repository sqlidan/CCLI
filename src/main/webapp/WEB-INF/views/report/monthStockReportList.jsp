<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>

    <style> 
        /* 表头色彩样式 */
        .datagrid-header-row td{
            border-top: 5px solid #1956A3;
            background-color: #D0ECFA;
            color: #1956A3}
        /* 全部字体 */
        .datagrid-cell {
            /* 表内文字大小 */
            font-size: 18px;
            font-family:'微软雅黑';
        }
        /* 表头字体 */
        .datagrid-header .datagrid-cell span{
            /* 表头文字大小 */
            font-size: 25px;
            /* 表头行高 */
            line-height:40px;
            /* 表头是否加粗 */
            font-weight:bold;
        }
        /* 表体行高 */
        .datagrid-row {
            height: 45px;
        }
    </style>
    <script>$(".datagrid-header-row td div span").each(function(i,th){
		var val = $(th).text();
		 $(th).html("<label style='font-weight: bolder;'>"+val+"</label>");
	});</script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="searchFrom" action="" method="post">
            <select id="months" name="months" class="easyui-validatebox"
                    data-options="width:150,prompt: '时间'">
            </select>
             <select id="customer" name="customer" class="easyui-combobox"
                    data-options="width:150,prompt: '客户名称'">
            </select>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="qk()">清空</a>
        </form>
    </div>
</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var obj = $("#searchFrom").serializeObject();
    $(function () {
        
         $('#customer').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });
        
        $('#months').combobox({
            method: "GET",
            url: "${ctx}/report/monthStock/getAllMonth",
            valueField: 'MONTHS',
            textField: 'MONTHS',
            mode: 'remote'
        });

        //加载页面数据表格
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/report/monthStock/json',//
            fit: true,
            fitColumns: false,
            border: false,
            sortOrder: 'desc',
            queryParams:obj,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'NAMES', title: '客户名称', sortable: true, width: 440, align: 'center'},
                {field: 'NUM', title: '件数', sortable: true, width: 150, align: 'center'},
                {field: 'NET_WEIGHT', title: '净重', sortable: true, width: 270, align: 'center'},
                {field: 'GROSS_WEIGHT', title: '毛重', sortable: true, width: 150, align: 'center'},
                {field: 'MONTHS', title: '月份', sortable: true, width: 100, align: 'center'},          
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar:'#tb'
        });
    });
    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        console.log(obj);
        dg.datagrid('load', obj);
    }
    //清空搜索条件
    function qk() {
        $('#months').combobox('clear'); //清除时间数据
        $('#customer').combobox('clear'); //清除客户名称数据
    }
</script>
</body>
</html>