<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
	<div>
		<form id="mainform" action="${ctx}/bis/trayinfo/${action}"
			method="post">
			<table class="formTable">
				<tr>
					<td>原托盘号：</td>
					<td><input id=trayId name="trayId" class="easyui-validatebox" 
					data-options="width: 150,required:'required'" value="${trayInfo.trayId}" 
					maxlength="100" 
					<c:if test="${action =='update'}"> readonly style="background:#eee"</c:if>></td>
				</tr>
				<tr>
					<td>件数：</td>
					<td><input id=nowPiece name="nowPiece" class="easyui-validatebox" 
					data-options="width: 150,required:'required'" value="${trayInfo.nowPiece}" 
					maxlength="100" 
					<c:if test="${action =='update'}"> readonly style="background:#eee"</c:if>></td>
				</tr>
				<tr>
					<td>是否保税 </td><td><span class="easyui-checkbox"> <input
							id="isBonded" name="isBonded" type="checkbox"
							data-options="width: 180" value="1"
							<c:choose>
		                       <c:when test="${trayInfo.isBonded == 1}">checked</c:when>
		                    <c:otherwise></c:otherwise>
		                </c:choose> />
					</span>
					</td>	 		
				</tr>
				<tr>
					<td>托盘号：</td>
					<td><input id="trayNewCode" name="trayNewCode" class="easyui-validatebox" 
					data-options="width: 150,required:'required'" 
					maxlength="100" value="${trayInfo.trayNewCode} "></td>
				</tr>
			   <tr>
                <td>转出件数：</td>
                <td>
<%--                     <input id="qty" name="qty" class="easyui-validatebox"
                           data-options="width:150, required:'required'" value="${trayInfo.qty}"
                           onkeyup="value=value.replace(/[^\d]/g,'')"/> --%>
                 <input id="qty" name="qty" class="easyui-validatebox" 
					data-options="width: 150,required:'required'" 
					maxlength="100" value="${trayInfo.qty} "></td> 
                </td>
            </tr>
<%-- 				<tr>
					<td>件数：</td>
					<td><input class="easyui-numberspinner" style="width:80px;" 
					data-options="onChange: function(value){$('#vv').text(value); }"  value="${trayInfo.nowPiece}"></input></td>
				</tr> --%>
				

			</table>
		</form>
	</div>

	<script type="text/javascript">
var action="${action}";
$(function(){  
	if( action =="update"){ 
		jiaoyan();
	}
});	

function jiaoyan(){
	$('#cargoName').validatebox({    
	   	 	required: true, 
	    	validType:{
	    	remote:["${ctx}/bis/trayinfo/checktraynum","trayNewCode"]
	    }
	}); 
}


//提交表单
$('#mainform').form({
	onSubmit : function() {
		jiaoyan();
		var isValid = $(this).form('validate');
		return isValid; // 返回false终止表单提交
	},
	success : function(data) {
		parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight" });
	}
});


</script>
</body>
</html>