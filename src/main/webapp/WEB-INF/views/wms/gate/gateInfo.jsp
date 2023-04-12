<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<div data-options="region:'center'">

    <div style="height:auto" class="datagrid-toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true"
           data-options="disabled:false" onclick="submitMainForm()">保存</a>
    </div>

    <table>
        <tr>
            <td>集装箱号</td>
            <td><input type="text" id="ctn" class="easyui-validatebox" data-options="width:240,prompt: '集装箱号'"
                       value="${obj.ctnNum}"/></td>
            <td>
                <a class="easyui-linkbutton" iconCls="icon-add" id="ctnSearchBtn" onclick="searchCtnNum()">添加</a>
            </td>
        </tr>
    </table>

    <form id="mainForm" action="" method="post">

        <table>

            <input type="hidden" name="ctnNum" id="ctnNum" class="easyui-validatebox" value="${obj.ctnNum}"/>

            <tr>
                <td>箱型</td>
                <td><input type="text" name="ctnVersion" id="ctnVersion" class="easyui-validatebox"
                           data-options="width:240,prompt: '箱型'" value="${obj.ctnVersion}"/>
            </tr>

            <tr>
                <td>尺寸</td>
                <td><input type="text" name="ctnSize" id="ctnSize" class="easyui-validatebox"
                           data-options="width:240,prompt: '尺寸'" value="${obj.ctnSize}"/>
            </tr>

            <tr>
                <td>提单号</td>
                <td><input type="text" name="billNum" id="billNum" class="easyui-validatebox"
                           data-options="width:240,prompt: '提单号'" value="${obj.billNum}"/>
            </tr>

            <tr>
                <td>ASN</td>
                <td><input type="text" name="asn" id="asn" class="easyui-validatebox"
                           data-options="width:240,prompt: 'ASN'" value="${obj.asn}"/>
                </td>
            </tr>

            <tr>
                <td>场位</td>
                <td><select type="text" name="positionId" id="positionId" class="easyui-validatebox"
                            data-options="width:240,prompt: '场位'" value="${obj.positionId}"></select>
                    <input type="hidden" name="position" id="position" class="easyui-validatebox"
                           value="${obj.position}"/>
                </td>
            </tr>

            <tr>
                <td>装卸单位</td>
                <td><select type="text" name="clientId" id="clientId" class="easyui-validatebox"
                            data-options="width:240,prompt: '装卸单位'" value="${obj.clientId}"></select>
                    <input type="hidden" name="clientName" id="clientName" class="easyui-validatebox"
                           value="${obj.clientName}"/>
                </td>
            </tr>

            <tr>
                </td>
                <td>车牌号</td>
                <td><input type="text" name="carNum" id="carNum" class="easyui-validatebox"
                           data-options="width:240,prompt: '车牌号'" value="${obj.carNum}"/></td>
            </tr>

            <tr>
                </td>
                <td>司机</td>
                <td><input type="text" name="driverName" id="driverName" class="easyui-validatebox"
                           data-options="width:240,prompt: '司机'" value="${obj.driverName}"/></td>
            </tr>

        </table>
    </form>

</div>

<script type="text/javascript">

    var action = "${action}";
    var clientId = '${obj.clientId}';
    var positionId = '${obj.positionId}';

    $(function () {

        if (action == "update") {
            $("#ctn").attr("disabled", true);
            $("#ctnSearchBtn").attr("disabled", true);
            $("#billNum").attr("readonly", "readonly");
            $("#asn").attr("readonly", "readonly");
        } else {
            $("#mainForm").hide();
        }

        $('#positionId').combobox({
            method: "GET",
            url: "${ctx}/wms/luodi/position/list",
            valueField: 'ID',
            textField: 'POSITIONNAME',
            mode: 'remote',
            onLoadSuccess: function () {

                if (positionId != null && positionId != "") {
                    $('#positionId').combobox("select", positionId);
                    positionId = "";
                }

            }
        });

        $('#clientId').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll?setid=${obj.clientId}",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote',
            onLoadSuccess: function () {

                if (clientId != null && clientId != "") {
                    $('#clientId').combobox("select", clientId);
                    clientId = "";
                }

            }
        });

    });

    function searchCtnNum() {

        var ctnNum = $("#ctn").val();

        if (ctnNum == null || ctnNum == "") {
            toast("请输入箱号");
            return;
        }

        $.get(
            "${ctx}/wms/gate/info/searchAsnAndBill?ctnNum=" + ctnNum,
            function (data) {

                console.log(data);

                if (data === null || data === "") {
                    toast("箱号使用中");
                    return;
                }

                if (data.length === 0) {
                    toast("箱号无效");
                    return;
                }

                var row = data[0];

                $("#mainForm").show();

                $("#ctn").attr("disabled", true);
                $("#ctnSearchBtn").attr("disabled", true);
                $("#billNum").attr("readonly", "readonly");
                $("#asn").attr("readonly", "readonly");

                $("#ctnNum").val($("#ctn").val());

                $("#billNum").val(row.BILLNUM);
                $("#asn").val(row.ASN);

            }
        );

    }

    function submitMainForm() {

        $("#clientName").val($("#clientId").combobox("getText"));
        $("#position").val($("#positionId").combobox("getText"));

        $.ajax({
            async: false,
            type: 'POST',
            url: "${ctx}/wms/gate/info/merge",
            data: $('#mainForm').serialize(),
            dataType: "text",
            success: function (msg) {

                if (msg == "success") {
                    toast("保存成功!");
                } else {
                    toast("保存失败!");
                }

            }
        });

    }

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }

</script>
</body>
</html>