<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>

</head>
<body>
<div>
    <form id="mainform2" action="${ctx}/base/work/addKg" onkeydown="if(event.keyCode==13)return false;" method="post">
        <table class="formTable">
            <tr>
                <td>库管人员</td>
                <td>
                    <select id="addKg" name="kgPerson" class="easyui-combobox"
                            data-options="width: 180, required:'required'">
                    </select>
                </td>
            </tr>
          </table>
    </form>
</div>

<script type="text/javascript">

$(function () {
	  // 库管人员
        $('#addKg').combobox({
		  	method: "GET",
	        url: "${ctx}/system/user/getUserAll",
	        valueField: 'name',
	        textField: 'name',
	        mode: 'remote' 
	   	});
})



    //提交表单
    $('#mainform2').form({
        onSubmit: function () {
            var isValid = $(this).form('validate');
            return isValid; // 返回false终止表单提交
        },
        success: function (data) {
            parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
        }
    });
</script>
</body>
</html>