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
	  	<shiro:hasPermission name="gocard:bulkma:save">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
		   	<span class="toolbar-item dialog-tool-separator"></span>
	   	</shiro:hasPermission>
	   	<shiro:hasPermission name="gocard:bulkma:delete">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteIt()">删除</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="gocard:bulk:print">
      		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="printIt()">打印</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
		</shiro:hasPermission>
	</div>
	<table class="formTable" >
		<tr>
			<td>出门证号</td>
			<td>
				<input  id="goCard" name="goCard" class="easyui-validatebox"  data-options="width: 180,required:'required'" value="${goCard}" readonly style="background:#eee">
			</td>
			<td>卡车牌号</td>
			<td>
				<input id="carNum" name="carNum" class="easyui-validatebox"  data-options="width: 180"  value="${obj.carNum}" >
			</td>
			<td>船名/航次</td>
			<td>
				<input id="shipNum" name="shipNum" class="easyui-validatebox" data-options="width: 180"  value="${obj.shipNum}"  > 
			</td>
			<td>提货单位</td>
			<td>
				<input id="takeName" name="takeName" class="easyui-validatebox" data-options="width: 180"   value="${obj.takeName}"> 
			</td>
		</tr>
		<tr>
			<td>提单号</td>
			<td>
				<input id="billNum" name="billNum" class="easyui-validatebox" data-options="width: 180"   value="${obj.billNum}"  > 
			</td>
			<td>提货人</td>
			<td>
				<input  id="takeMan" name="takeMan" class="easyui-validatebox"  data-options="width: 180" value="${obj.takeMan}"/>
			</td>
			<td>录入员</td>
			<td>
				<c:choose>
					<c:when test="${action == 'create'}">
						<input id="inputMan" name="inputMan" class="easyui-validatebox"  data-options="width: 180"  value="${user}" readonly style="background:#eee" />
					</c:when>
					<c:otherwise>
						<input id="inputMan" name="inputMan" class="easyui-validatebox"  data-options="width: 180"  value="${obj.inputMan}" readonly style="background:#eee">
					</c:otherwise>
				</c:choose>
			</td>
			<td>备注</td>
			<td>
				<input id="remark" name="remark" class="easyui-validatebox" data-options="width: 180" value="${obj.remark}"> 
			</td>
		</tr>
		<tr>
			<td>件数</td>
			<td>
				<input id="pieceOne" name="pieceOne" class="easyui-validatebox" data-options="width: 180"   value="${obj.pieceOne}"  onkeyup="value=value.replace(/[^\d]/g,'')"> 
			</td>
			<td>货名</td>
			<td>
				<input  id="cargoNameOne" name="cargoNameOne" class="easyui-validatebox"  data-options="width: 180" value="${obj.cargoNameOne}"/>
			</td>
			<td>包装种类</td>
			<td>
				<input id="packTypeOne" name="packTypeOne" class="easyui-validatebox"  data-options="width: 180"  value="${obj.packTypeOne}" >
			</td>
			<td>货重</td>
			<td>
				<input id="cargoWeightOne" name="cargoWeightOne" class="easyui-validatebox" data-options="width: 180" value="${obj.cargoWeightOne}"  onkeyup="value=value.replace(/[^\d+(\.\d{2})?$]/g,'')"> 
			</td>
		</tr>
		<tr>
			<td>件数</td>
			<td>
				<input id="pieceTwo" name="pieceTwo" class="easyui-validatebox" data-options="width: 180"   value="${obj.pieceTwo}"    onkeyup="value=value.replace(/[^\d]/g,'')"> 
			</td>
			<td>货名</td>
			<td>
				<input  id="cargoNameTwo" name="cargoNameTwo" class="easyui-validatebox"  data-options="width: 180" value="${obj.cargoNameTwo}"/>
			</td>
			<td>包装种类</td>
			<td>
				<input id="packTypeTwo" name="packTypeTwo" class="easyui-validatebox"  data-options="width: 180"  value="${obj.packTypeTwo}"  >
			</td>
			<td>货重</td>
			<td>
				<input id="cargoWeightTwo" name="cargoWeightTwo" class="easyui-validatebox" data-options="width: 180"  value="${obj.cargoWeightTwo}"   onkeyup="value=value.replace(/[^\d+(\.\d{2})?$]/g,'')"> 
			</td>
		</tr>
		<tr>
			<td>出港时间</td>
			<td>
				<input id="leaveTime" name="leaveTime" class="easyui-my97" data-options="width: 180,required:'required'"  datefmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${obj.leaveTime}" pattern="yyyy-MM-dd HH:mm:ss" />" readonly style="background:#eee"> 
			</td>
			<td>类型</td>
			<td>
				<input   name="type" class="easyui-validatebox"  data-options="width: 180" value="散货" readonly style="background:#eee">
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

$(function(){   
});




//保存
function submitForm(){
	if($("#mainForm").form('validate')){
			//用ajax提交form
	 		$.ajax({
	 	  		async: false,
	 	  		type: 'POST',
	 	  		url: "${ctx}/gocard/bulk/create",
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
				url: "${ctx}/gocard/bulk/ifsave/" + gocard,
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
									url: "${ctx}/gocard/bulk/delete/" + gocard,
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
				url: "${ctx}/gocard/bulk/ifsave/" + gocard,
				dataType:"text",
				success: function(data){
					if(data=="false"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "未保存，无法进行打印操作!", position: "bottomRight"});
						return;
					}else{
						window.parent.mainpage.mainTabs.addModule('散杂货出门证打印','gocard/bulk/print/' + gocard);
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