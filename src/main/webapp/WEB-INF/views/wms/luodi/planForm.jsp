<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>

    <form id="mainform" action="${ctx}/wms/luodi/form/${action}" method="post">

        <table>
            <tr>
                <td>箱号</td>
                <td>
                    <input type="text" id="ctnNum" name="ctnNum" class="easyui-validatebox"
                           data-options="width:150,prompt: '箱号', required:'required'" value="${luodi.ctnNum}">
                </td>
            </tr>

            <tr>
                <td>类型</td>
                <td>
                    <select id="luodiType" name="luodiType" class="easyui-combobox"
                            data-options="width:150,prompt: '类型',required:'required'">
                        <option value="F">落地</option>
                        <option value="L">装车</option>
                        <option value="M">场内移动</option>
                    </select>
                </td>
            </tr>

            <%--<tr>--%>
            <%--<td>箱型</td>--%>
            <%--<td>--%>
            <%--<input type="text" id="ctnVersion" name="ctnVersion" class="easyui-validatebox"--%>
            <%--data-options="width:150,prompt: '箱型', required:'required'" value="${luodi.ctnVersion}">--%>
            <%--</td>--%>
            <%--</tr>--%>
            <tr>
                <td>插电</td>
                <td>
                    <input type="checkbox" id="isElectricity" name="isElectricity" data-options="width: 150" value="1">
                </td>
            </tr>
            <tr>
                <td>落地时间</td>
                <td>
                    <input type="text" id="luodiTime" name="luodiTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                           data-options="width:150,prompt: '落地时间', required:'required'" value="${luodi.luodiTime}"/>
                </td>
            </tr>
            <tr id="startPositionDiv" hidden>
                <td>开始位置</td>
                <td>
                    <select id="startPosition" name="startPosition" class="easyui-validatebox"
                            data-options="width:150, required:'required',disable:true">
                    </select>

                </td>
            </tr>
            <tr>
                <td>结束位置</td>
                <td>
                    <select id="endPosition" name="endPosition" class="easyui-validatebox"
                            data-options="width:150, required:'required'">
                    </select>
                </td>
            </tr>

            <tr>
                <td>提单号</td>
                <td>
                    <input type="text" id="billCode" name="billCode" class="easyui-validatebox"
                           data-options="width:150,prompt: '提单号', required:'required'" value="${luodi.billCode}">
                </td>
            </tr>
            <tr>
                <td>客户名称</td>
                <td>
                    <input type="hidden" id="clientId" name="clientId">
                    <input type="hidden" id="clientName" name="clientName">
                    <input id="clientNameV" name="clientNameV" class="easyui-validatebox"
                           data-options="width:150,disabled:true">
                    <a class="easyui-linkbutton" iconCls="icon-add" onclick="getClient()">获取</a>
                </td>
            </tr>
            <tr>
                <td>车牌号</td>
                <td>
                    <input type="text" id="carNum" name="carNum" class="easyui-validatebox"
                           data-options="width:150,prompt: '车牌号', required:'required'" value="${luodi.carNum}">
                </td>
            </tr>
            <tr>
                <td>应付客户名称</td>
                <td>
                    <input type="hidden" id="yfClientName" name="yfClientName" class="easyui-validatebox">
                    <select id="yfClientId" name="yfClientId" class="easyui-validatebox"
                            data-options="width:150,prompt: '应付客户名称', required:'required'">
                    </select>
                </td>
            </tr>
        </table>

    </form>

</div>

<script type="text/javascript">

    $(function () {

        $("#luodiType").combobox({
            onSelect: function (data) {

                console.log(data.value);

                if (data.value == "M") {
                    $("#startPositionDiv").show();
                    $("#startPosition").combobox({
                        disabled: false
                    });
                } else {
                    $("#startPositionDiv").hide();

                    $("#startPosition").combobox({
                        disabled: true
                    });
                }
            }
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