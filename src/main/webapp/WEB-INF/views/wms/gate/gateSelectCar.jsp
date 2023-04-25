<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>

<table id="carList"></table>

<script type="text/javascript">

    var carList;

    $(function () {
        initSelect();
    });

    function initSelect() {

        carList = $('#carList').datagrid({
            method: "get",
            url: '${ctx}/wms/gate/car/list',
            fit: true,
            fitColumns: true,
            border: false,
            striped: true,
            singleSelect: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 10,
            pageList: [10],
            columns: [[
                {field: 'CARNUM', title: '车牌号', sortable: true, width: 100},
                {field: 'DRIVERNAME', title: '司机', sortable: true, width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false
        });

    }

</script>
</body>
</html>