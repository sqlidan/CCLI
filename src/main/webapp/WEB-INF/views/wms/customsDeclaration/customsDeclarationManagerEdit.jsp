<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="报关单管理" style="overflow-y:auto">
	<form id="mainForm"   method="post">
		<div style="height:auto" class="datagrid-toolbar">
			<shiro:hasPermission name="wms:customsDeclaration:add">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
		</div>
		<table class="formTable" >
			<tr>
				<td>报关单ID：</td>
				<td>
					<input type="text" id="forId" name="forId"  class="easyui-validatebox" value="${bsCustomsDeclaration.forId}" data-options="width:180, required:'required'"  readonly style="background:#eee">
				</td>
				<td>核注清单号：</td>
				<td>
					<input type="text" id="checkListNo" name="checkListNo"  class="easyui-validatebox" value="${bsCustomsDeclaration.checkListNo}" data-options="width:180, required:'required'">
				</td>
				<td>报关单号：</td>
				<td>
					<input type="text" id="cdNum" name="cdNum"  class="easyui-validatebox" value="${bsCustomsDeclaration.cdNum}" data-options="width:180, required:'required'">
				</td>
				<td>申报日期：</td>
				<td>
					<input id="sbTime" name="sbTime" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${sbTime}" pattern="yyyy-MM-dd HH:mm:ss" />" />
				</td>
				<td>提运单号：</td>
				<td>
					<input id="billNum" name="billNum" type="text" class="easyui-validatebox"  data-options="width:180" maxlength="17"  value="${bsCustomsDeclaration.billNum}">
				</td>
			</tr>
			<tr>
				<td>服务项目：</td>
				<td>
					<select id="serviceProject" name="serviceProject" class="easyui-combobox" data-options="width: 180">
						<option value="0">报进</option>
						<option value="1">报出</option>
					</select>
				</td>
				<td>贸易方式：</td>
				<td>
					<select id="tradeMode" name="tradeMode" class="easyui-combobox" data-options="width:180">
					</select>
				</td>
				<td>消费使用单位：</td>
				<td>
					<input type="hidden" id="clientName" name="clientName">
					<select id="clientId" name="clientId" class="easyui-combobox" data-options="width:180" >
					</select>
				</td>
				<td>申报单位：</td>
				<td>
					<input type="hidden" id="declarationUnit" name="declarationUnit" >
					<select id="declarationUnitId" name="declarationUnitId" class="easyui-combobox" data-options="width:180"  maxlength="30" >
					</select>
				</td>
				<td>货物存放地点：</td>
				<td>
					<input id="storagePlace" name="storagePlace" type="text" class="easyui-validatebox"  data-options="width:180" maxlength="100"  value="${bsCustomsDeclaration.storagePlace}">
				</td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
					<input type="text" id="dty" name="dty" class="easyui-validatebox"  data-options="width: 180" value="${bsCustomsDeclaration.dty}" maxlength="10" oninput="ischeckNum(this)">
				</td>
				<td>毛重：</td>
				<td>
					<input type="text" id="grossWeight" name="grossWeight" class="easyui-validatebox" data-options="width: 160"  value="${bsCustomsDeclaration.grossWeight}" maxlength="10" onkeyup="ischeckNum(this)">
					<span style="color: red">KG</span>
				</td>
				<td>净重：</td>
				<td>
					<input type="text" id="netWeight" name="netWeight" class="easyui-validatebox" data-options="width: 160"  value="${bsCustomsDeclaration.netWeight}" maxlength="10" onkeyup="ischeckNum(this)">
					<span style="color: red">KG</span>
				</td>
			</tr>
			<tr>
				<td>收货人：</td>
				<td>
					<input type="text" id="consignee" name="consignee" class="easyui-validatebox" data-options="width: 180"  value="${bsCustomsDeclaration.consignee}" maxlength="50">
				</td>
				<td>发货人：</td>
				<td>
					<input type="text" id="consignor" name="consignor" class="easyui-validatebox"  data-options="width: 180" value="${bsCustomsDeclaration.consignor}" maxlength="50" >
				</td>
				<td>贸易国：</td>
				<td>
					<input type="text" id="myg" name="myg" class="easyui-validatebox" data-options="width: 180"  value="${bsCustomsDeclaration.myg}"  maxlength="50" >
				</td>
				<td>启运国：</td>
				<td>
					<input type="text" id="qyg" name="qyg" class="easyui-validatebox" data-options="width: 180"  value="${bsCustomsDeclaration.qyg}"  maxlength="50" >
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
	$(function(){
		var foridVal = $("#forId").val();
		if(foridVal== undefined || foridVal == null || foridVal.trim().length == 0){
			//生成新的报关单ID
			$.ajax({
				type : "POST",
				url : "${ctx}/wms/customsDeclaration/getNewForId",
				dataType : "text",
				success : function(date) {
					$("#forId").val(date);
				}
			});
		}
		var forId = $("#forId").val()
		if(action =="create"){
			$("#customerService").val("${user}");
			forId = 0;
			a = 0;
		}else{
			a=1;
		}
		selectAjax();
	});
	function addInfo2(){
		alert(a);
	}
	function selectAjax(){
		//报关公司
		var getorg='${bsCustomsDeclaration.declarationUnitId}';
		$('#declarationUnitId').combobox({
			method:"GET",
			url:"${ctx}/base/client/getClientAll?setid=${bsCustomsDeclaration.declarationUnitId}",
			valueField: 'ids',
			textField: 'clientName',
			mode:'remote',
			onLoadSuccess:function(){
				if(getorg!=null && getorg!=""){
					$('#declarationUnitId').combobox("select",getorg);
					getorg="";
				}
			}
		});

		//物流容器
		$.ajax({
			type : "GET",
			async : false,
			url : "${ctx}/system/dict/json",
			data : "filter_LIKES_type=tradeType",
			dataType : "json",
			success : function(date) {
				$('#tradeMode').combobox({
					data : date.rows,
					value : '${bsCustomsDeclaration.tradeMode}',
					valueField : 'value',
					textField : 'label',
					editable : false
				});
			}
		});

		//客户
		var getstockId='${bsCustomsDeclaration.clientId}';
		$('#clientId').combobox({
			method:"GET",
			url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bsCustomsDeclaration.clientId}",
			valueField: 'ids',
			textField: 'clientName',
			mode:'remote',
			onChange:function(){
				if(b==1){
					a=1;
					b=0;
				}else{
					a=0;
				}
			},
			onSelect:function(){
				a=1;
				b=1;
			},
			onLoadSuccess:function(){
				if(getstockId!=null && getstockId!=""){
					$('#clientId').combobox("select",getstockId);
					getstockId="";
				}
			}
		});
		$('#serviceProject').combobox('select','${bsCustomsDeclaration.serviceProject}');
	}
	//清空
	function clearIt(){
		window.parent.mainpage.mainTabs.addModule('报关单添加','wms/customsDeclaration/manager');
	}

	//保存
	function submitForm(){
		$("#clientName").val( $("#clientId").combobox("getText") );
		$("#declarationUnit").val( $("#declarationUnitId").combobox("getText") );
		if($("#mainForm").form('validate')){
			if(a == 0){
				parent.$.messager.show({ title : "提示",msg: "下拉框选项请选择下拉框中的数据!", position: "bottomRight" });
				return;
			}else{
				//用ajax提交form
				$.ajax({
					async: false,
					type: 'POST',
					url: "${ctx}/wms/customsDeclaration/update",
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