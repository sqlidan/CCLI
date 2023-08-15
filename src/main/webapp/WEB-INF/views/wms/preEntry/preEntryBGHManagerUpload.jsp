<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<!-- 导入 jQuery -->
<script src="${ctx}/static/plugins/easyui/jquery/jquery-1.11.1.min.js"></script>
</head>
<body>
<%--<form id="myForm">--%>
<%--    <div>--%>
<%--        备注：<input type="text" id="remark" name="remark" />--%>
<%--        <br/>--%>
<%--        <input type="file" id="file" />--%>
<%--        <button id="btnUpload">上传</button>--%>
<%--    </div>--%>
<%--</form>--%>
<div>
    <form id="mainform" action="${ctx }/wms/preEntryInfo/createDJFile?forId=${forId}" method="post" enctype="multipart/form-data">
        <table class="formTable">
            <tr>
                <td>备注：<input name="remark" id="remark" type="text"></td>
            </tr>
            <tr>
                <td>文件上传：<input name="file" type="file"></td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">
var forId="${forId}";

$('#mainform').form({
    onSubmit: function(){
        return true;	// 返回false终止表单提交
    },
    success:function(data){
        successTip(data,dg,d);
    }
});

<%--$('#btnUpload').on('click', function() {--%>
<%--	// 1. 将 jQuery 对象转化为 DOM 对象，并获取选中的文件列表--%>
<%--	var files = $('#file')[0].files;--%>
<%--	// 2. 判断是否选择了文件--%>
<%--	if (files.length <= 0) {--%>
<%--		$.messager.alert("提示", "请选择文件后再上传");--%>
<%--		return;--%>
<%--	}else{--%>
<%--        //单文件上传--%>
<%--        var formData = new FormData()--%>
<%--		formData.append('filename', files[0]);--%>
<%--		$.ajax({--%>
<%--			url: '${ctx}/wms/preEntryInfo/createDJFile?forId='+forId+'&remark='+rem,--%>
<%--			type: 'POST',--%>
<%--			processData: false,--%>
<%--			contentType: false,--%>
<%--			data: formData,--%>
<%--			dataType : 'json',--%>
<%--			success: function (data) {--%>
<%--				$.messager.alert("提示", "操作成功");--%>
<%--			},--%>
<%--			error: function (xhr, status, error) {--%>
<%--				$.messager.alert("提示", "操作失败");--%>
<%--			}--%>
<%--		});--%>
<%--	}--%>
<%--})--%>

</script>
</body>
</html>