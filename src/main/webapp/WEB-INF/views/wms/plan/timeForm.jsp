<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>

    <form id="mainform" action="${ctx}/wms/plan/time/${action}" method="post">

        <input type="hidden" id="id" name="id" value="${id}">
        <input type="hidden" id="state" name="state" value="${state}">

        <table class="formTable">
            <tr>
                <td>时间：</td>
                <td>
                    <select id="timeHour" name="timeHour" class="easyui-combobox" data-options="width: 50">
                    </select>时

                    <select id="timeMin" name="timeMin" class="easyui-combobox" data-options="width: 50">
                    </select>分
                </td>
            </tr>
            <tr>
                <td>最大车次：</td>
                <td>
                    <input type="text" class="easyui-validatebox" id="largeNo" name="largeNo"
                           required="true" value="${largeNo}">
                </td>
            </tr>

        </table>
    </form>
</div>

<script type="text/javascript">

    var action = '${action}';

    $(function () {

        var timeHour = $("#timeHour");
        var timeMin = $("#timeMin");

        for (var i = 1; i <= 23; i++) {
            timeHour.append("<option value=" + i + ">" + i + "</option>");
        }

        for (var j = 0; j <= 5; j++) {
            timeMin.append("<option value=" + (j * 10) + ">" + (j * 10) + "</option>");
        }

    });


</script>
</body>
</html>