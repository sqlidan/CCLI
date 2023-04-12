<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center'">
<form id="searchFrom3" action="">
</form>
	<form id="mainForm"   method="post">
	<div style="height:auto" class="datagrid-toolbar">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
	   	<span class="toolbar-item dialog-tool-separator"></span>

	</div>
	<table class="formTable" >
		<tr>



			<td>
				<input id="id" name="id" class="easyui-validatebox"  type="hidden"   value="${groupManage.id}">
			</td>
			<input type="hidden" id="createdTime" name="createdTime"   value="<fmt:formatDate value="${groupManage.createdTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"/>

			<td>组名称</td>
			<td>
				<input id="groupName" name="groupName" class="easyui-validatebox" data-options="width: 180" value="${groupManage.groupName}"  />

			</td>

		  <td>创建人</td>
			<td>
				<input id="operator" name="operator" class="easyui-validatebox" data-options="width: 180" value="${groupManage.operator}" readonly style="background:#eee" />
			</td>							
		</tr>
		

	</table>
</form>
</div>


<script type="text/javascript">
var dg;
var d;
var action = "${action}";
var a;
var b=0;
var dhs;
var des;





console.log(action);


//保存
function submitForm(){


	if($("#mainForm").form('validate')){
		//用ajax提交form
 		$.ajax({
 	  		async: false,
 	  		type: 'POST',
 	  		url: "${ctx}/platform/group/manage/"+action,
 	  		data: $('#mainForm').serialize(),

 	  		success: function(msg){
 	  			if(msg == "success"){
 	  				parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });

 	  			}
 	  			if(msg == "duplicate"){
 	  				parent.$.messager.alert("提示","此记录已存在，请确认");
 	  			}

 	  		}
 	  	});
 	}
}

//删除此入库报关单
function deleteIt(){
		var cdNum = $("#cdNum").val();
		$.ajax({
				async:false,
				type: 'get',
				url: "${ctx}/platform/group/manage/ifsave/" + cdNum,
				success: function(data){
					if(data=="success"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "未保存，无法进行删除操作!", position: "bottomRight"});
						return;
					}
				}
		 });
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
			if(data){
				$.ajax({
					async:false,
					type: 'get',
					url: "${ctx}/platform/group/manage/delete/" + cdNum,
					success: function(data){
						if(data=="success"){
							parent.$.easyui.messager.show({title: "操作提示", msg: "删除成功！", position: "bottomRight"});
							window.parent.mainpage.mainTabs.closeCurrentTab();//关闭TAB
						}
					}
		  		})
			}
		})
}






//数字校验
function ischeckNum(val) {
	if (val.value) {
		if (!isNaN(val.value)) {

		} else {
			parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
			$("#"+val.id).val("");
			myfm.isnum.select();
			return false;
		}
	}
}


</script>
</body>
</html>