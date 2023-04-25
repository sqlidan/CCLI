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
        <input type="text" name="filter_LIKES_skuId" class="easyui-validatebox" data-options="width:150,prompt: 'sku'"/>
        <input type="text" name="filter_LIKES_cargoName" class="easyui-validatebox"
               data-options="width:150,prompt: '商品名称'"/>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
    </form>

</div>

<table id="skudg"></table>

<script type="text/javascript">

    $(document).ready(function () {
        $('#skudg').datagrid({
            method: "get",
            url: '${ctx}/base/sku/listjson',
            fit: true,
            fitColumns: true,
            border: false,
            idField: 'skuId',
            striped: true,
            singleSelect: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 15,
            pageList: [15, 30, 45, 60, 75, 90],
            singleSelect: false,
            columns: [[
                {field: 'skuId', title: 'SKU_ID', sortable: true, width: 100},
                {field: 'producingArea', title: 'SKU描述', sortable: true, width: 70},
                {
                    field: 'cargoState', title: '库存类型', sortable: true,
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '成品'
                        } else if (value == 2) {
                            return '箱损'
                        } else {
                            return '货损'
                        }
                    }
                },
                {field: 'validityTime', title: '有效日期', sortable: true, width: 50},
                {field: 'cargoName', title: '品名', sortable: true, width: 50},
                {field: 'netSingle', title: '单净', sortable: true, width: 50},
                {field: 'grossSingle', title: '单毛', sortable: true, width: 50},
                {field: 'mscNum', title: 'msc', sortable: true, width: 50},
                {field: 'typeSize', title: '规格', sortable: true, width: 50},
                {field: 'remark', title: '备注', hidden: true}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false
        });

    });

    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        $('#skudg').datagrid('load', obj);
    }

</script>

</body>
</html>