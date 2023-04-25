<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform3" action="${ctx}/bis/transferinfo/${action}/${transferId}" method="post" enctype="multipart/form-data">
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
			dimp.panel('close');
		}else{
			var lis = new Array();
			lis = data.split(":");
			var mess = "";
			if( lis[0] != ""){
				mess = "第"+lis[0]+"条数据在明细中已存在！ ";
			}
			if( lis[1] !=""){
				mess = mess + "第"+lis[1]+"条数据的出库件数大于库存可出库件数！ " ;
			}
			if(lis[2] !=""){
				mess = mess +"第"+lis[2]+"条数据不存在于库存中！" ;
			}
			mess = mess + "上述数据导入失败！"
			dimp.panel('close');
		}
    }    
});    
</script>
</body>
</html>