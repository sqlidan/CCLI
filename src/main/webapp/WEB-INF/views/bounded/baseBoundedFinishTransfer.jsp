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
            <input  type="hidden" id="id" name="id" value="${baseBoundedToNonBounded.id}">
            <tr>
                <td>库位号：</td>
                <td>
                    <input id="nonCargoLocation" name="nonCargoLocation" class="easyui-validatebox" data-options="width:150, required:'required'">
                </td>
            </tr>
            <tr>
                <td>库区：</td>
                <td>
                    <input id="nonCargoArea" name="nonCargoArea" class="easyui-validatebox" data-options="width:150, required:'required'" >
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
</script>
</body>
</html>