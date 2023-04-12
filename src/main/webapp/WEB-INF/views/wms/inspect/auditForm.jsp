<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>

    <form id="mainform">

        <table>
            <input type="hidden" id="inspectId" name="inspectId" value="${inspectId}" class="easyui-validatebox"
                   data-options="required:'required'">
            <tr>
                <td>
                    托盘号:
                </td>
                <td>
                    <input type="text" id="trayId" name="trayId" class="easyui-validatebox"
                           data-options="width:150,prompt: '托盘号', required:'required'">
                </td>
            </tr>
        </table>

    </form>

</div>

<script type="text/javascript">

</script>
</body>
</html>