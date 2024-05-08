<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>
    <table>
        <tr>
            <th style="text-align: center;width:85px">
                货物ID
            </th>
            <th style="text-align: center;width:85px">
                操作重量
            </th>
            <th style="text-align: center;width:85px">
                操作件数
            </th>
            <th style="text-align: center;width:85px">
                箱号
            </th>
            <th style="text-align: center;width:85px">
                提单号
            </th>
        </tr>

        <c:forEach items="${bulk}" var="item">
        <tr style="height:25px">
            <td style="text-align: center;width:85px">
                    ${item.TRAY_ID}
            </td>
            <td style="text-align: center;width:85px">
                ${item.OPER_WEIGHT}
            </td>
            <td style="text-align: center;width:85px">
                    ${item.OPER_QTY}
            </td>
            <td style="text-align: center;width:85px">
                ${item.CTN_NUM}
            </td>
            <td style="text-align: center;width:85px">
                ${item.BILL_NUM}
            </td>
        </tr>
        </c:forEach>
    </table>
</div>

<table id="dg"></table>

<script type="text/javascript">

    $(function () {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/bulk/page',
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
                {field: 'customerCode', title: '客户编号', hidden: true},
                {field: 'customerName', title: '客户名称', sortable: true, width: 100},
                {field: 'endPosition', title: '场位', sortable: true, width: 80},
                {
                    field: 'operCode', title: '操作编码', sortable: true, width: 100,
                },
                {
                    field: 'operType', title: '操作类型', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == "ZYCheck") {
                            return "质押校验";
                        } else if (value == "JYCheck") {
                            return "解押校验";
                        }
                    }
                },
                {field: 'mkDd', title: '申请时间', sortable: true, width: 100},
                {field: 'execDd', title: '执行时间', sortable: true, width: 100},
                {
                    field: 'operState', title: '操作状态', sortable: true, width: 150,
                    formatter: function (value, row, index) {
                        if (value == "0") {
                            return "失败";
                        } else if (value == "0") {
                            return "成功";
                        }
                    }
                }
            ]],
            onLoadSuccess: function (data) {

            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
        $('#startPosition').combobox({
            method: "GET",
            url: "${ctx}/wms/luodi/position/list",
            valueField: 'ID',
            textField: 'POSITIONNAME',
            mode: 'remote'
        });

        $('#endPosition').combobox({
            method: "GET",
            url: "${ctx}/wms/luodi/position/list",
            valueField: 'ID',
            textField: 'POSITIONNAME',
            mode: 'remote'
        });

        $('#yfClientId').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });

    });

    /**
     * 获取客户信息
     */
    function getClient() {

        var billCode = $("#billCode").val();

        if (billCode == null || billCode == "") {
            return;
        }

        var url = "${ctx}/wms/luodi/client?billCode=" + billCode;

        $.get(url, function (data) {
            $("#clientId").val(data.stockId);
            $("#clientName").val(data.stockIn);
            $("#clientNameV").val(data.stockIn);
        })
    }

</script>
</body>
</html>