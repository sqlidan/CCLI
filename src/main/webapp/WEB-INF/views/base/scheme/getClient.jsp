<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<form id="shareForm" method="post">
	<table id="share" border="1"  style="float:left;width:50%" >
		<tr>
			<td colspan='1' align="center">可选择用户</td>
		</tr>
		<tr>
			<td align="center">客户名称<input type="checkbox" id="checkAll"/>全选</td>
		</tr>
		<c:forEach items="${clientList}" var="client" varStatus="my">
		<tr>
			<td><input id="${my.count}" name="client"  type="checkbox" value="${client.clientId}"/>${client.clientName}</td>
		</tr>
		</c:forEach>
	</table>
</form>
<script type="text/javascript">
var num="${num}";

$(function(){   
  var t ; 
  var ids;
  var clientIds = $("#clientIds").val();
  if(clientIds != ""){
  	var clientId = new Array();
  	clientId = clientIds.split(",");
  	for(var i = 0;i<clientId.length;i++){
  		t = clientId[i];
   		$("input[name='client'][value="+t+"]").attr("checked",true);
  	}
  }
});


//全选
$("#checkAll").click(function(){
 if( $("#checkAll").is(":checked") ){
 	for(var i = 1;i<parseInt(num)+1;i++){
 		if( $("input[name='client'][id="+i+"]").is(":checked") ){
 			
 		}else{
 			$("input[name='client'][id="+i+"]").attr("checked",true);
 		}
 	}
 }else{
	for(var i = 1;i<parseInt(num)+1;i++){
 		if( $("input[name='client'][id="+i+"]").is(":checked") ){
 			$("input[name='client'][id="+i+"]").attr("checked",false);
 		}else{
 			
 		}
 	}	
 }
})


</script>
</body>
</html>
