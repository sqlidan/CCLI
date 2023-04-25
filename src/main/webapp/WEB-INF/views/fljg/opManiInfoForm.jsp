<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>

</head>
<body>
<div>
    <form id="mainform" action="${ctx}/supervision/opManiInfo/save" method="post">
        <table class="formTable">
			<tr>
				<td><input id="headId"  type="hidden" name="headId" value=""/></td>
				<%--				<td><input id="headId" type="hidden" name="headId" value=""/></td>--%>
			</tr>

			<tr>
				<td>申请单号：</td>
				<td><input id="ApprId" name="ApprId"
						   class="easyui-validatebox"
						   data-options="width:150"/>
				</td>
				<td>申请单商品序号：</td>
				<td><input id="ApprGNo" name="ApprGNo"
						   class="easyui-validatebox"
						   data-options="width:150"/>
				</td>
			</tr>
            <tr>
				<td>商品编码：</td>
				<td><input id="CodeTs" name="CodeTs"
						   class="easyui-validatebox"
						   data-options="width:150,required:'required'"/>
				</td>
				<td>商品名称：</td>
				<td><input id="GName" name="GName"
						   class="easyui-validatebox"
						   data-options="width:150,required:'required'"/></td>
            </tr>
			<tr>
				<td>底账商品项号：</td>
				<td><input id="GNo" name="GNo" class="easyui-validatebox"
						   class="easyui-validatebox" data-options="width: 150,required:'required'"/></td>
			</tr>
			<tr>
				<td>数量：</td>
				<td><input id="GQty" name="GQty" class="easyui-validatebox"
						   data-options=" width: 150, required:'required'"/></td>
				<td>重量：</td>
				<td><input id="GrossWt" name="GrossWt"
						   class="easyui-validatebox" data-options="width: 150,required:'required'"/>
				</td>
			</tr>
			<tr>
				<td>单位：</td>
				<td><input id=" GUnit" name="GUnit"
						   class="easyui-validatebox"
						   data-options="width: 150, required:'required'" value="035" />
				</td>
			</tr>
        </table>
    </form>
</div>

<script type="text/javascript">
    $('#headId').val(recordId);
    //	console.log(list);
    $(function () {
        resetForm();
    });

    function resetForm() {

        $("#ApprType").combobox({
            data: [{
                'value': '0',
                'text': '非保税货物申请'
            }, {
                'value': '1',
                'text': '非保税转保税货物申请'
            }, {
                'value': '2',
                'text': '保税转非保税货物申请'
            }, {
                'value': '3',
                'text': '内通道出入区'
            }],
            valueField: 'value',
            textField: 'text',
            editable: false
        });
        $("#IoType").combobox({
            data: [{
                'value': '0',
                'text': '无'
            }, {
                'value': '1',
                'text': '入区'
            }, {
                'value': '2',
                'text': '出区'
            }],
            valueField: 'value',
            textField: 'text',
            editable: false
        });
        $("#CustomsCode").val('4258');
        $("#EmsNo").val("NH4230210001");
        $("#BondInvtNo").val("");
        $("#GNo").val("");
        $("#DNote").val("");

    }

    //提交表单
    $('#mainform').form({
        onSubmit: function () {
            var isValid = $(this).form('validate');
            return isValid;	// 返回false终止表单提交
        },
        success: function (data) {
            successTip(data, dg, d);

        }
    });

</script>
</body>
</html>