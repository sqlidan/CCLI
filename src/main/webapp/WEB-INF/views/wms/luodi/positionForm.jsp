<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>

    <form id="mainform" action="${ctx}/wms/luodi/position/${action}" method="post">

        <input type="hidden" id="id" name="id" value="${id}">
        <table class="formTable">

            <tr>
                <td>场位：</td>
                <td>
                    <input type="text" class="easyui-validatebox" id="positionFirst" name="positionFirst"
                           required="true" value="${positionFirst}">
                </td>
            </tr>

            <tr>
                <td>区号：</td>
                <td>
                    <input type="text" class="easyui-validatebox" id="positionLast" name="positionLast"
                           required="true" value="${positionLast}">
                </td>
            </tr>

        </table>
    </form>
</div>

<script type="text/javascript">

    var action = '${action}';

    $(function () {

    });


</script>
</body>
</html>