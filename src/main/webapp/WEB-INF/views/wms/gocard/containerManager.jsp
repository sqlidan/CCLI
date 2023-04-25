<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center'">
	<form id="mainForm"   method="post">
	<div style="height:auto" class="datagrid-toolbar">
	  	<shiro:hasPermission name="gocard:containerma:save">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
		   	<span class="toolbar-item dialog-tool-separator"></span>
	   	</shiro:hasPermission>
	   	<shiro:hasPermission name="gocard:containerma:delete">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteIt()">删除</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="gocard:container:print">
      		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="printIt()">打印</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
	</div>
	<table class="formTable" >
		<tr>
			<td>出门证号</td>
			<td>
				<input  id="goCard" name="goCard" class="easyui-validatebox"  data-options="width: 180,required:'required'" value="${goCard}" readonly style="background:#eee"/>
			</td>
			<td>卡车牌号</td>
			<td>
				<input id="carNum" name="carNum" class="easyui-validatebox"  data-options="width: 180"  value="${obj.carNum}" >
			</td>
			<td>集装箱号</td>
			<td>
				<input id="ctnNumOne" name="ctnNumOne" class="easyui-validatebox" data-options="width: 180,required:'required'"  value="${obj.ctnNumOne}"  > 
			</td>
			<td>集装箱号2</td>
			<td>
				<input id="ctnNumTwo" name="ctnNumTwo" class="easyui-validatebox" data-options="width: 180"  value="${obj.ctnNumTwo}"  > 
			</td>
		</tr>
		<tr>
			<td>空重</td>
			<td>
				<select id="ifEmpty" name="ifEmpty" class="easyui-combobox" data-options="width: 180"  readonly > 
					<option value="1">空箱</option>
					<option value="2">重箱</option>
				</select>
			</td>
			<td>尺寸</td>
			<td>
				<select id="theSize" name="theSize" class="easyui-combobox"  data-options="width: 180" readonly >
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
				</select>
			</td>
			<td>出场时间</td>
			<td>
				<input id="appearTime" name="appearTime" class="easyui-my97" data-options="width: 180,required:'required'"  datefmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${obj.appearTime}" pattern="yyyy-MM-dd HH:mm:ss" />" > 
			</td>
			<td>箱数</td>
			<td>
				<input id="ctnAmount" name="ctnAmount" class="easyui-validatebox" data-options="width: 180" value="${obj.ctnAmount}"   onkeyup="value=value.replace(/[^\d]/g,'')"> 
			</td>
		</tr>
		<tr>
			<td>类型</td>
			<td>
				<input id="type" name="type" class="easyui-validatebox" data-options="width: 180"   value="集装箱" readonly style="background:#eee"> 
			</td>
			<td>提单号</td>
			<td>
				<input  id="billNum" name="billNum" class="easyui-validatebox"  data-options="width: 180" value="${obj.billNum}"/>
			</td>
			<td>船名/航次</td>
			<td>
				<input id="shipNum" name="shipNum" class="easyui-validatebox"  data-options="width: 180"  value="${obj.shipNum}" >
			</td>
			<td>录入员</td>
			<td>
				<c:choose>
					<c:when test="${action == 'create'}">
						<input id="inputMan" name="inputMan" class="easyui-validatebox"  data-options="width: 180"  value="${user}" readonly style="background:#eee">
					</c:when>
					<c:otherwise>
						<input id="inputMan" name="inputMan" class="easyui-validatebox"  data-options="width: 180"  value="${obj.inputMan}" readonly style="background:#eee">
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
</form>
</div>

<script type="text/javascript">
var dg;
var d;
var action = "${action}";

$(function(){  
	if(action=="update"){
		var a="${obj.ifEmpty}";
		var b="${obj.theSize}";
		
		$("#ifEmpty").combobox("setValue",a);
		$("#theSize").combobox("setValue",b);
	}
})


//保存
function submitForm(){
	if($("#mainForm").form('validate')){
			//用ajax提交form
	 		$.ajax({
	 	  		async: false,
	 	  		type: 'POST',
	 	  		url: "${ctx}/gocard/container/create",
	 	  		data: $('#mainForm').serialize(),
	 	  		dataType: "text",  
	 	  		success: function(msg){
	 	  			if(msg == "success"){
	 	  				parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
	 	  			}
	 	  		}
	 	  	});
	 	}
}

//删除
function deleteIt(){
		var gocard = $("#goCard").val();
		$.ajax({
				async:false,
				type: 'get',
				url: "${ctx}/gocard/container/ifsave/" + gocard,
				dataType:"text",
				success: function(data){
					if(data=="false"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "未保存，无法进行删除操作!", position: "bottomRight"});
						return;
					}else{
						parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
							if(data){
								$.ajax({
									async:false,
									type: 'get',
									url: "${ctx}/gocard/container/delete/" + gocard,
									success: function(data){
										if(data=="success"){
											parent.$.easyui.messager.show({title: "操作提示", msg: "删除成功！", position: "bottomRight"});
											window.parent.mainpage.mainTabs.closeCurrentTab();//关闭TAB
										}
									}
						  		});
							}
						});//end confirm
					}
				}
		 });
}


//打印
function printIt(){
	var gocard = $("#goCard").val();
	$.ajax({
				async:false,
				type: 'get',
				url: "${ctx}/gocard/container/ifsave/" + gocard,
				dataType:"text",
				success: function(data){
					if(data=="false"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "未保存，无法进行打印操作!", position: "bottomRight"});
						return;
					}else{
						window.parent.mainpage.mainTabs.addModule('集装箱出门证打印','gocard/container/print/' + gocard);
					}
				}
			});
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