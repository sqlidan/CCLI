<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform3" action="${ctx}/bis/asninfo/${action}/${asn}" method="post" enctype="multipart/form-data">
		<table class="formTable">
			<tr>
				<td><input name="file" type="file" extend="*.xls;*.xlsx"></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
//提交表单
$('#mainform3').form({    
    onSubmit: function(){  
		return true;	// 返回false终止表单提交
    },    
    success:function(data){   
    	if(data == "success"){
    		parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
    		gridDG();
			d.panel('close');
    	}else{
    		var mess = "EXCEL中第"+data+"条数据的大类小类不存在，或大小类不是对应关系，无法导入!";
    		alert(mess);
//    		parent.$.messager.show({ title : "提示",msg: mess, position: "bottomRight" });
			gridDG();
			d.panel('close');
//    		successTip(data,dg,d);
		}
    }    
});    
</script>
</body>
</html>