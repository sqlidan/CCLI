<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform2" action="${ctx}/wms/customsDeclarationInfo/${action}" method="post">
		<table class="formTable">
			<tr>
				<td style="text-align:right;"><span style="color: red">*</span>项号</td>
				<td>
					<input type="hidden" id="id" name="id" value="${bsCustomsDeclarationInfo.id}">
					<input type="hidden" id="forId" name="forId" value="${bsCustomsDeclarationInfo.forId}">
					<input type="text" id="xh" name="xh" class="easyui-validatebox"
						   data-options="width:180, required:'required'" value="${bsCustomsDeclarationInfo.xh}">
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>账册商品序号</td>
				<td>
					<input type="text" id="accountBook" name="accountBook" class="easyui-validatebox"
						   data-options="width:180, required:'required'" value="${bsCustomsDeclarationInfo.accountBook}">
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>商品编号</td>
				<td>
					<input type="text" id="spbh" name="spbh" class="easyui-validatebox"
						   data-options="width:180, required:'required'" value="${bsCustomsDeclarationInfo.spbh}">
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>商品名称</td>
				<td>
					<input type="text" id="spmc" name="spmc" class="easyui-validatebox"
						   data-options="width:180, required:'required'"
						   value="${bsCustomsDeclarationInfo.spmc}">
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>规格型号</td>
				<td>
					<input type="text" id="ggxh" name="ggxh" class="easyui-validatebox"
						   data-options="width:180, required:'required'"
						   value="${bsCustomsDeclarationInfo.ggxh}">
				</td>
			</tr>
			<tr>
				<td style="text-align:right;"><span style="color: red">*</span>申报数量</td>
				<td>
					<input type="text" id="sbsl" name="sbsl" class="easyui-validatebox"
						   data-options="width:180, required:'required'"
						   value="${bsCustomsDeclarationInfo.sbsl}">
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>计量单位</td>
				<td>
					<select id="sbjldw" name="sbjldw" class="easyui-combobox"
							data-options="width:180, required:'required'"
							value="${bsCustomsDeclarationInfo.sbjldw}">
					</select>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;"><span style="color: red">*</span>单价</td>
				<td>
					<input type="text" id="qysbdj" name="qysbdj" class="easyui-validatebox"
						   data-options="width:180, required:'required'"
						   value="${bsCustomsDeclarationInfo.qysbdj}">
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>总价</td>
				<td>
					<input type="text" id="mytjzje" name="mytjzje" class="easyui-validatebox"
						   data-options="width:180, required:'required'"
						   value="${bsCustomsDeclarationInfo.mytjzje}">
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>币制</td>
				<td>
					<select id="bzt" name="bzt" class="easyui-combobox"
							data-options="width:180, required:'required'"
							value="${bsCustomsDeclarationInfo.bzt}">
					</select>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;"><span style="color: red">*</span>原产国(地区)</td>
				<td>
					<select id="ycg" name="ycg" class="easyui-combobox"
							data-options="width:180, required:'required'" value="${bsCustomsDeclarationInfo.ycg}">
					</select>
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>最终目的国</td>
				<td>
					<select id="zzmdg" name="zzmdg" class="easyui-combobox"
							data-options="width:180, required:'required'"
							value="${bsCustomsDeclarationInfo.zzmdg}">
					</select>
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>境内目的地</td>
				<td>
					<input type="text" id="jnmdd" name="jnmdd" class="easyui-validatebox"
						   data-options="width:180, required:'required'"
						   value="${bsCustomsDeclarationInfo.jnmdd}">
				</td>
				<td style="text-align:right;"><span style="color: red">*</span>征免方式</td>
				<td>
					<select id="zmfs" name="zmfs" class="easyui-combobox"
							data-options="width:180, required:'required'"
							value="${bsCustomsDeclarationInfo.zmfs}">
					</select>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;">备注</td>
				<td>
					<input type="text" id="remark" name="remark" class="easyui-validatebox"
						   data-options="width:180" value="${bsCustomsDeclarationInfo.remark}">
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";

var bztAry;//币制
var ycgAry;//原产国
var jldwAry;//计量单位
var zmfsAry;//征免方式
var xgbzAry;//修改标志


$(function(){
	selectAjax();
});
function selectAjax() {

	//表体
	//原产国/最终目的国
	$.ajax({
		type: "GET",
		async: false,
		url: "${ctx}/wms/preEntry/dictData/CUS_STSHIP_TRSARV_NATCD",
		success: function (date) {
			ycgAry = date.rows;
			$('#ycg').combobox({
				data: date.rows,
				value: '${bsCustomsDeclarationInfo.ycg}',
				valueField: 'value',
				textField: 'label',
				editable: false
			});
			$('#zzmdg').combobox({
				data: date.rows,
				value: '${bsCustomsDeclarationInfo.zzmdg}',
				valueField: 'value',
				textField: 'label',
				editable: false
			});
		}
	});
	//币种
	$.ajax({
		type: "GET",
		async: false,
		url: "${ctx}/wms/preEntry/dictData/CUS_DCLCURRCD",
		success: function (date) {
			bztAry = date.rows;
			$('#bzt').combobox({
				data: date.rows,
				value: '${bsCustomsDeclarationInfo.bzt}',
				valueField: 'value',
				textField: 'label',
				editable: false
			});
		}
	});
	//征免方式
	$.ajax({
		type: "GET",
		async: false,
		url: "${ctx}/wms/preEntry/dictData/CUS_LVYRLFMODECD",
		success: function (date) {
			zmfsAry = date.rows;
			$('#zmfs').combobox({
				data: date.rows,
				value: '${bsCustomsDeclarationInfo.zmfs}',
				valueField: 'value',
				textField: 'label',
				editable: false
			});
		}
	});
	//申报计量单位/法定计量单位/第二法定计量单位
	$.ajax({
		type: "GET",
		async: false,
		url: "${ctx}/system/dict/json",
		data: "filter_LIKES_type=unitOfWeight",
		dataType: "json",
		success: function (date) {
			jldwAry = date.rows;
			$('#sbjldw').combobox({
				data: date.rows,
				value: '${bsCustomsDeclarationInfo.sbjldw}',
				valueField: 'value',
				textField: 'label',
				editable: false
			});
		}
	});
}

//提交表单
$('#mainform2').form({
	onSubmit : function() {
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