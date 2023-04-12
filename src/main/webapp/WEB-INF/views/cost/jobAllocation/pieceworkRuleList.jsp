<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">

    <div>

        <form id="searchFrom" action="">
        	<!-- <select class="easyui-combobox" id="filter_LIKES_personType" name="filter_LIKES_personType" data-options="width:150,prompt:'人员类型'">
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

        <shiro:hasPermission name="cost:rule:add">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="add();">添加</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>

        <shiro:hasPermission name="cost:rule:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="upd()">修改</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="cost:rule:delete">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
               data-options="disabled:false" onclick="del()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
    </div>

</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">

    var dg;
    var d;

    $(document).keypress(function (e) {
        // 回车键事件
        if (e.which == 13) {
            cx();
        }
    });

    $(document).ready(function () {
        dg = $('#dg').datagrid({
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
                {field: 'personType', title: '人员类型', sortable: true, hidden:true, width: 100},
                {field: 'jobType', title: '作业类型', sortable: true,width: 100},
                {field: 'ratio', title: '比例', sortable: true, width: 50},
                {field: 'operateTime', title: '操作时间', sortable: true, width: 50},
                {field: 'operator', title: '操作人', sortable: true, width: 100},
                {field: 'remark', title: '备注', sortable: true, width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
        
        
     /* selPersonType(); */
	 selBigType();
  	 selJobType("");
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
						valueField : 'value',
						textField : 'label',
						editable : false 
					});
			},
		});
	}else{
		$('#filter_LIKES_jobType').combobox({
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
					valueField : 'label',
					textField : 'label',
					editable : false
				});
		}
	});
} */

//作业大类选择
function selBigType(){
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=bigType",
		dataType : "json",
		success : function(date) {
				$('#filter_LIKES_bigType').combobox({
					data : date.rows,
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





    //添加
    function add(title, href,version) {
    	if(typeof(href) =='undefined'){
			title="计件规则添加";
			href='${ctx}/cost/piecework/create';
			version="create";
		}
        d = $("#dlg").dialog({
            title: title,
            width: 450,
            height: 450,
            href: href,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                	 if ($("#mainform").form('validate')) {
                         $.ajax({
                             type: 'POST',
                             url: "${ctx}/cost/piecework/checkKey",
                             //personType:$("#personType").combobox("getValue"),
                             data: {jobType:$("#jobType").combobox("getValue"),ratio:$("#ratio").val(),version:version,id:$("#id").val()},
                             dataType: "text",
                             success: function (msg) {
                             	if(msg=="success"){
                             		$("#mainform").submit();
	                   				d.panel('close');
                             	}else{
                             		parent.$.messager.show({title: "提示", msg: "此计件规则已存在！", position: "bottomRight"});
                             	}
                             }
                        });
                    }
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }

    //修改
    function upd() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        var href = '${ctx}/cost/piecework/update/' + row.id;
        add("计件规则修改", href,"update");
    }

    //查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }

    //删除
    function del() {
       var rows = dg.datagrid('getSelections');
        var del = dg.datagrid('getSelected');
        if (del == null) {
            parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight"});
            return;
        }
        var ids = [];
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i].id);
        }
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/cost/piecework/delete/" + ids,
                    success: function (data) {
                        dg.datagrid('clearSelections');
                        successTip(data, dg);
                    }
                });
            }
        });
    }

    //导出
    function exportExcel() {
        var url = "${ctx}/base/client/export";
        $("#searchFrom").attr("action", url).submit();
    }

</script>
</body>

</html>