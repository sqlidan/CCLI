<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div>
    <form id="mainform" action="${ctx}/stock/check/feedback/add" method="post">

        <table class="formTable">

            <c:if test="${action == 'trayId'}">
                <tr>
                    <td>托盘号：</td>
                    <td>
                        <input name="trayId" class="easyui-validatebox" data-options="width: 180" value="${trayCode}"
                               readonly="readonly">
                    </td>
                </tr>


                <tr>
                    <td>提单号：</td>
                    <td>
                        <input name="billNum" class="easyui-validatebox" data-options="width: 180" value="${billCode}"
                               readonly>
                    </td>
                </tr>
            </c:if>

            <tr>
                <td>箱号：</td>
                <td>
                    <input name="ctnNum" class="easyui-validatebox" data-options="width: 180" value="${ctnNum}"
                           readonly>
                </td>
            </tr>

            <c:if test="${action == 'trayId'}">

                <tr>
                    <td>SKU：</td>
                    <td>
                        <input name="skuId" class="easyui-validatebox" data-options="width: 180" value="${sku}"
                               readonly>
                    </td>
                </tr>
            </c:if>

            <tr>
                <td>现有产品名：</td>
                <td>
                    <input name="cargoName" class="easyui-validatebox" data-options="width: 180,required:'required'"
                           value="${cargoName}" readonly>
                </td>
            </tr>

            <tr>
                <td>现有数量：</td>
                <td>
                    <input name="nowPiece" class="easyui-validatebox" data-options="width: 180,required:'required'"
                           value="${nowNum}" readonly>
                    <%--净重--%>
                    <input type="hidden" class="easyui-validatebox" name="netWeight" value="${netWeight}">
                    <%--毛重--%>
                    <input type="hidden" class="easyui-validatebox" name="grossWeight" value="${grossWeight}">
                    <%--单位--%>
                    <input type="hidden" class="easyui-validatebox" name="units" value="${units}">
                </td>
            </tr>

            <tr>
                <td>实际产品名：</td>
                <td>
                    <input name="realCargoName" class="easyui-validatebox" data-options="width: 180,required:'required'"
                           value="${cargoName}">
                </td>
            </tr>

            <tr>
                <td>实际数量：</td>
                <td>
                    <input name="realPiece" class="easyui-validatebox" data-options="width: 180,required:'required'"
                           value="${nowNum}">
                </td>
            </tr>
			
			<tr>
                <td>盘点反馈人员：</td>
                <td>
                    <input name="checkName" class="easyui-validatebox" data-options="width: 180,required:'required'"
                           >
                </td>
            </tr>
            
            <tr>
                <td>盘点时间：</td>
                <td>
                    <input id="checkTime" name="checkTime" class="easyui-datetimebox" data-options="width: 180,required:'required'"
                           datefmt="yyyy-MM-dd HH:mm:ss">
                </td>
            </tr>
            
            <tr>
                <td>备注：</td>
                <td>
                    <textarea name="description" style="width:180px;height:100px"></textarea>
                </td>
            </tr>


        </table>
    </form>
</div>

<script type="text/javascript">
	

	
    //提交表单
    $('#mainform').form({
        onSubmit: function () {
            var isValid = $(this).form('validate');
            return isValid;	// 返回false终止表单提交
        },
        success: function (data) {
            successTip(data, dg, d);
        }
    });
/* 	//取当前时间
	$(function () {
    	var curr_time = new Date();
   		var strDate = curr_time.getFullYear()+"-";
   		strDate += curr_time.getMonth()+1+"-";
   		strDate += curr_time.getDate();
   		strDate += " "+ curr_time.getHours() + ":" + curr_time.getMinutes() +":"+curr_time.getSeconds();
    	$("#checkTime").datetimebox("setValue",strDate);
	}); */

</script>
</body>
</html>