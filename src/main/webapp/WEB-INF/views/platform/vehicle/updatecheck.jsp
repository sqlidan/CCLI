<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="infoForm" method="post">
        </form>
        <form id="mainform" action="${ctx}/platform/vehicle/check/${action}" method="post" >
            <input type="hidden" name="id" value="${check.id }"/>
            <span>实际车号（不需更新车号可不填）：</span>
            <input type="text" name="actualVehicleNo"/>
        </form>

    </div>
</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var d;
    var action="${action}"
    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
        }
    };
    $('#mainform').form({
        onSubmit: function(){
            var isValid = $(this).form('validate');
            return isValid;	// 返回false终止表单提交
        },
        success:function(data){
            successTip(data,dg,d);
        }
    });

    $(function () {
        //客户
        $('#searchStockIn').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });


    });


</script>
</body>
</html>