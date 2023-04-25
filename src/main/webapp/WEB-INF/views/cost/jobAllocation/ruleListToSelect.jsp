<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div style="padding:5px; height:auto" class="datagrid-toolbar">

    <form id="searchFrom" action="">
            <!-- <select class="easyui-combobox" id="filter_LIKES_personType" name="filter_LIKES_personType" data-options="width:150,prompt:'人员类型',hidden:true">
					<option value=""></option>
			</select> -->
			<select class="easyui-combobox" id="filter_LIKES_bigType" name="filter_LIKES_bigType" data-options="width:150,prompt:'作业大类'">
					<option value=""></option>
			</select>
			<select class="easyui-combobox" id="filter_LIKES_jobType" name="filter_LIKES_jobType" data-options="width:150,prompt:'作业类型'">
					<option value=""></option>
			</select>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
    </form>

</div>

<table id="ruledg"></table>

<script type="text/javascript">

    $(document).ready(function () {
        $('#ruledg').datagrid({
            method: "get",
            url: '${ctx}/cost/piecework/rule',
            fit: true,
            fitColumns: true,
            border: false,
            idField: 'id',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'id', title: 'id', hidden: true},
                {field: 'keyId', title: 'keyId', hidden: true},
                {field: 'personType', title: '人员类型', sortable: true, width: 100, hidden: true},
                {field: 'jobType', title: '作业类型', sortable: true,width: 100},
                {field: 'ratio', title: '比例', sortable: true, width: 50},
                {field: 'operateTime', title: '操作时间', sortable: true, width: 50},
                {field: 'operator', title: '操作人', sortable: true, width: 100},
                {field: 'remark', title: '备注', sortable: true, width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false
        });

	 //selPersonType();
	 selBigType();
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
					$('#filter_LIKES_jobType').combobox({
						data : date.rows,
						value : "${rule.personType}",
						valueField : 'value',
						textField : 'label',
						editable : false 
					});
			},
		});
	}else{
		$('#jobType').combobox({
						data : "",
						valueField : "",
						textField : "",
						editable : false 
					});
	}
}

//人员类型选择
/* function selPersonType(){
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=personType",
		dataType : "json",
		success : function(date) {
				$('#filter_LIKES_personType').combobox({
					data : date.rows,
					value : "${rule.personType}",
					valueField : 'label',
					textField : 'label',
					editable : false
				});
		}
	});
} */

//作业大类选择
function selBigType(){
	var bt='${rule.bigType}';
	if(null==bt || bt=="undefined"){
		bt=1
	}
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=bigType",
		dataType : "json",
		success : function(date) {
				$('#filter_LIKES_bigType').combobox({
					data : date.rows,
					value : bt,
					valueField : 'value',
					textField : 'label',
					editable : false,
					onChange: function (newVal, oldVal){
					   selJobType(newVal);
				   }
				});
		}
		
	});
}


    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        $('#ruledg').datagrid('load', obj);
    }

</script>

</body>
</html>