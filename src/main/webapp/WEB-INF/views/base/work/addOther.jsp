<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div>
    <form id="mainform3" action="${ctx}/base/work/addOther/${id}" onkeydown="if(event.keyCode==13)return false;"  method="post">
        <table class="formTable">
            <tr>
                <td>理货人员</td>
                <td>
                    <select id="addLh" name="lhPerson" class="easyui-combobox"
                            data-options="width: 180">
                    </select>
                </td>
            </tr>
            <tr>
                <td>叉车人员</td>
                <td>
                    <select id="addCc" name="ccPerson" class="easyui-combobox"
                            data-options="width: 180">
                    </select>
                </td>
            </tr>
          </table>
    </form>
</div>

<script type="text/javascript">

$(function () {
	  // 理货人员
        $('#addLh').combobox({
		  	method: "GET",
	        url: "${ctx}/system/user/getUserAll",
	        valueField: 'name',
	        textField: 'name',
	        mode: 'remote' 
	   	});
	   	
	   	// 叉车人员
        $('#addCc').combobox({
		  	method: "GET",
	        url: "${ctx}/system/user/getUserAll",
	        valueField: 'name',
	        textField: 'name',
	        mode: 'remote' 
	   	});
})



    //提交表单
    $('#mainform3').form({
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