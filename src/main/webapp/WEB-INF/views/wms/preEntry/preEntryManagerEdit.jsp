<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="预报单添加" style="overflow-y:auto">
	<form id="mainForm"   method="post">
		<div style="height:auto" class="datagrid-toolbar">
			<shiro:hasPermission name="wms:preEntry:edit">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
		</div>
		<table class="formTable" >
			<tr>
				<td>预报单ID：</td>
				<td>
					<input type="text" id="forId" name="forId"  class="easyui-validatebox" value="${preEntry.forId}" data-options="width:180, required:'required'"  readonly style="background:#eee">
				</td>
				<td>服务项目：</td>
				<td>
					<select id="serviceProject" name="serviceProject" class="easyui-combobox" data-options="width: 180, required:'required'">
						<option value="0">报进</option>
						<option value="1">报出</option>
					</select>
				</td>
				<td>客户名称：</td>
				<td>
					<input type="hidden" id="clientName" name="clientName">
					<select id="clientId" name="clientId" class="easyui-combobox" data-options="width:180, required:'required'" >
					</select>
				</td>
				<td>报关公司：</td>
				<td>
					<input type="hidden" id="declarationUnit" name="declarationUnit" >
					<select id="declarationUnitId" name="declarationUnitId" class="easyui-combobox" data-options="width:180, required:'required'"  maxlength="30" >
					</select>
				</td>
			</tr>
			<tr>
				<td>贸易方式：</td>
				<td>
					<select id="tradeMode" name="tradeMode" class="easyui-combobox" data-options="width:180, required:'required'">
					</select>
				</td>
				<td>提单号：</td>
				<td>
					<input id="billNum" name="billNum" type="text" class="easyui-validatebox"  data-options="width:180, required:'required'" maxlength="17"  value="${preEntry.billNum}">
				</td>
				<td>品名：</td>
				<td>
					<input id="productName" name="productName" type="text" class="easyui-validatebox"  data-options="width:180, required:'required'" maxlength="100"  value="${preEntry.productName}">
				</td>
				<td>HS编码：</td>
				<td>
					<input id="hsNo" name="hsNo" type="text" class="easyui-validatebox"  data-options="width:180, required:'required'" maxlength="100"  value="${preEntry.hsNo}">
				</td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
					<input type="text" id="price" name="price" class="easyui-validatebox"  data-options="width: 180, required:'required'" value="${preEntry.price}" maxlength="10" oninput="ischeckNum(this)" onpropertychange="ischeckNum(this)">
				</td>
				<td>重量：</td>
				<td>
					<input id="netWeight" name="netWeight" class="easyui-validatebox" data-options="width: 160, required:'required'"  value="${preEntry.netWeight}" maxlength="10" onkeyup="ischeckNum(this)">
					<span style="color: red">KG</span>
				</td>

				<td>收货人</td>
				<td>
					<input id="consignee" name="consignee" class="easyui-validatebox" data-options="width: 180, required:'required'"  value="${preEntry.consignee}" maxlength="50">
				</td>
				<td>发货人</td>
				<td>
					<input id="consignor" name="consignor" class="easyui-validatebox"  data-options="width: 180, required:'required'" value="${preEntry.consignor}" maxlength="50"/>
				</td>
			</tr>
			<tr>
				<td>原产国</td>
				<td>
					<input id="contryOragin" name="contryOragin" class="easyui-validatebox" data-options="width: 180, required:'required'"  value="${preEntry.contryOragin}"  maxlength="50"/>
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
		if($("#forId").val() == "" ){
			//生成新的预报单ID
			$.ajax({
				type : "POST",
				url : "${ctx}/wms/preEntry/getNewForId",
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
		// gridDG(forId);
		selectAjax();
	});

	function addInfo2(){
		alert(a);
	}

	function selectAjax(){
		//报关公司
		var getorg='${preEntry.declarationUnitId}';
		$('#declarationUnitId').combobox({
			method:"GET",
			url:"${ctx}/base/client/getClientAll?setid=${preEntry.declarationUnitId}",
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
					value : '${preEntry.tradeMode}',
					valueField : 'value',
					textField : 'label',
					editable : false
				});
			}
		});

		//客户
		var getstockId='${preEntry.clientId}';
		$('#clientId').combobox({
			method:"GET",
			url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${preEntry.clientId}",
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
		$('#serviceProject').combobox('select','${preEntry.serviceProject}');
	}

	//清空
	function clearIt(){
		window.parent.mainpage.mainTabs.addModule('预报单管理','wms/preEntry/manager');
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
					url: "${ctx}/wms/preEntry/update",
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