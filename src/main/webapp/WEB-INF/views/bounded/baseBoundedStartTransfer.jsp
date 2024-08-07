<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>

<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>

<body>

<div>
    <form id="mainform3" action="${ctx}/supervision/bonded/${action}" method="post"  enctype="multipart/form-data">
        <table class="formTable">
            <input  type="hidden" id="id" name="id" value="${baseBounded.id}">
            <input  type="hidden" id="piece" name="piece value="${baseBounded.piece}">
            <input  type="hidden" id="netWeight" name="netWeight" value="${baseBounded.netWeight}">
            <tr>
                <td>转出件数：</td>
                <td>
                    <input type="text" id="nonPiece" name="nonPiece" class="easyui-validatebox"  data-options="width: 150, required:'required'" value="${baseBounded.piece}" maxlength="10" oninput="ischeckNum(this)" onpropertychange="ischeckNum(this)">
                </td>
            </tr>
            <tr>
                <td>转出净重：</td>
                <td>
                    <input type="text" id="nonNetWeight" name="nonNetWeight" class="easyui-validatebox"  data-options="width: 150, required:'required'" value="${baseBounded.netWeight}" maxlength="10" oninput="ischeckNum(this)" onpropertychange="ischeckNum(this)">
                </td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">
    //提交表单
    $('#mainform3').form({
        onSubmit: function () {
            return true;	// 返回false终止表单提交
        },
        success: function (res) {
            if (res == "success") {
                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                gridDG();
                d.panel('close');
            } else {
                alert(res);
                d.panel('close');
            }
        }
    });
    //数字校验
    function ischeckNum(val) {
        if (val.value) {
            if (!isNaN(val.value)) {

            } else {
                parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
                $("#"+val.id).val("");
                return false;
            }
        }
    }
</script>
</body>
</html>