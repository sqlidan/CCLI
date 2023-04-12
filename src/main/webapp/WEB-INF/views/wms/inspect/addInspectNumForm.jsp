<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>

    <form id="mainform_num">

        <table>
            <%-- <input type="hidden" id="inspectId" name="inspectId" value="${inspectId}" class="easyui-validatebox"
                   data-options="required:'required'"> --%>
            <tr>
                <td>
                取样件数:
                </td>
                <td>
                    <input type="text" id="inspectNum" name="inspectNum" class="easyui-validatebox"
                           data-options="width:150,prompt: '取样件数', required:'required'">
                </td>
            </tr>
            <tr>
            	<td>
              样品单位:     	
            	</td>
            	<td>
            		<select class="easyui-combobox" id="sampleUnit" name="sampleUnit" data-options="width:150,prompt:'样品单位'">
					<option value="件"></option>
					</select>
            	</td>
            </tr>
        </table>

    </form>

</div>

<script type="text/javascript">

$(document).ready(function () {
	selJobType("sampleUnit");
});
//作业类型选择
function selJobType(dataType){
	if(dataType!=""){
		$.ajax({
			type : "GET",
			async : false,
			url : "${ctx}/system/dict/json",
			data : "filter_LIKES_type="+dataType,
			dataType : "json",
			success : function(date) {
					$('#sampleUnit').combobox({
						data : date.rows,
						valueField : 'value',
						textField : 'value',
						editable : true 
					});
			},
		});
	}else{
		$('#sampleUnit').combobox({
						data : "",
						valueField : "",
						textField : "",
						editable : false 
					});
	}
}
</script>
</body>
</html>