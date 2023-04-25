<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>

<form id="mainForm" action="" method="post">

    <table>
        <tr>
            </td>
            <td>车牌号</td>
            <td><input type="text" name="carNum" id="carNum" class="easyui-validatebox"
                       data-options="width:240,prompt: '车牌号',required:'required'"/></td>
        </tr>

        <tr>
            </td>
            <td>司机</td>
            <td><input type="text" name="driverName" id="driverName" class="easyui-validatebox"
                       data-options="width:240,prompt: '司机'"/></td>
        </tr>

    </table>
</form>

<script type="text/javascript">

</script>
</body>
</html>