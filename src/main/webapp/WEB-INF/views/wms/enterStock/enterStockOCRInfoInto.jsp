<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>

<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>

<body>

<div>
    <form id="mainform3" action="${ctx}/wms/enterStock/OCRPDF" method="post"
          enctype="multipart/form-data">
        <table class="formTable">
            <tr>
                <td><input name="file" type="file" extend="*.pdf"></td>
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
            if (res.indexOf("success") >= 0) {
                d.panel('close');
                parent.$.messager.confirm('提示', '报关单识别成功,是否需要跳转至入库联系单管理画面？', function(data){
                    if (data){
                        window.parent.mainpage.mainTabs.addModule('入库联系单管理', 'wms/enterStock/addData/' + res);
                    }
                });
            } else {
                parent.$.easyui.messager.alert(res);
                d.panel('close');
            }
        }
    });
</script>
</body>
</html>