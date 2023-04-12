<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>

</head>
<body>
<div>
    <form id="mainform2" action="${ctx}/cost/piecework/addRule/${loadingPlanNum}/${type}" method="post">
        <div id="tb" style="padding:5px;height:auto">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="openRuleList()">计件规则库导入</a>
        </div>
        <table class="formTable">
            <tr>
                <td>作业类型：</td>
                <td>
                	<input type="hidden" id="loadingNum" name="loadingNum" value="${loadingPlanNum}"/>
                    <input id="jobType"  class="easyui-validatebox"  name="ruleJobType"  data-options="width: 150, required:'required'" value="${jobType}" readonly>
                </td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">


    //数字校验
    function ischeckNum() {
        var num = document.getElementById('priceBase').value;
        if (num) {
            if (!isNaN(num)) {

            } else {
                parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight"});
                $("#priceBaseF").val("");
                myfm.isnum.select();
                return false;
            }
        }
    }


     
    // rule库添加
    
    function openRuleList() {
        drule = $("#ruledlg").dialog({
            title: '计件规则选择',
            width: 650,
            height: 500,
            href: '${ctx}/cost/piecework/rulelist',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    var rows = $('#ruledg').datagrid('getSelected');
                    $("#jobType").val(rows.jobType);
                    drule.panel('close');
                }
            }, {
                text: '取消',
                handler: function () {
                    drule.panel('close');
                }
            }]
        });

    }

    //提交表单
    $('#mainform2').form({
        onSubmit: function () {
            var isValid = $(this).form('validate');
            return isValid; // 返回false终止表单提交
        },
        success: function (data) {
            parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
        }
    });
</script>
</body>
</html>