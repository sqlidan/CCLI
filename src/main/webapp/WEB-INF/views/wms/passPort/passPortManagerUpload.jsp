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
<div>
    <form id="mainform" action="${ctx }/wms/passPortInfo/addDJ?passPortId=${passPortId}" method="post" enctype="multipart/form-data">
        <table class="formTable">
            <tr>
                <td>关联单证类型：
                    <select id="rltTbTypecd" name="rltTbTypecd" class="easyui-combobox" data-options="width:180">
                        <option value=""></option>
                        <option value="1">核注清单</option>
                        <option value="2">出入库单</option>
                        <option value="3">提运单</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><span style="color: red">*</span>关联单证编号：<input name="rtlNo" id="rtlNo" type="text" data-options="width:180, required:'required'"></td>
            </tr>
            <tr>
                <td>备注：<input name="remark" id="remark" type="text" data-options="width:180"></td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">
var passPortId="${passPortId}";
$('#mainform').form({
    onSubmit: function(){
        return true;	// 返回false终止表单提交
    },
    success:function(data){
        successTip(data,dg,d);
    }
});

</script>
</body>
</html>