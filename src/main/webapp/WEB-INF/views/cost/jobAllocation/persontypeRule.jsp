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
			<!-- <select class="easyui-combobox" id="filter_LIKES_bigType" name="filter_LIKES_bigType" data-options="width:150,prompt:'作业大类'">
					<option value=""></option>
			</select> -->
			<!-- <select id="person" name="person" class="easyui-combobox"
	                            data-options="width: 150,prompt:'人员'">
	        </select>
	        <span class="toolbar-item dialog-tool-separator"></span> -->
			<select class="easyui-combobox" id="personTypeName" name="personTypeName" data-options="width:150,prompt:'人员类型'">
					<option value=""></option>
			</select>
			<span class="toolbar-item dialog-tool-separator"></span>
			<!--  <input type="text" id="workTimeS" name="workTimeS" class="easyui-datebox" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '作业日期开始'"/>
            - <input type="text" id="workTimeE" name="workTimeE" class="easyui-datebox" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '作业日期结束'"/> -->
			
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
            url: '${ctx}/cost/piecework/persontypeRuleList',
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
                {field: 'personTypeName', title: '人员类型',width: 100},
                {field: 'ratio', title: '比例', sortable: true, width: 150},
                {field: 'remark', title: '备注', sortable: true, width: 150}
                /* {field: 'workDate', title: '作业日期', sortable: true,width: 150,
                	formatter: function(value,row,index){
                		if( value != null){
                			
                    		var datetime = new Date();
                    	    datetime.setTime(value);
                    	    var year = datetime.getFullYear();
                    	    var month = datetime.getMonth() + 1 < 10 ? "0"
                    	            + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
                    	    var date = datetime.getDate() < 10 ? "0" + datetime.getDate()
                    	            : datetime.getDate();
                    	    return year + "-" + month + "-" + date;
                		}
                		
                	}	
                },
                {field: 'workDuration', title: '作业时长（小时）', sortable: true, width: 150} */
               
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
        
        
     /* selPersonType(); */
	 /* selBigType(); */
	//人员 
/*     var manAssCC2="${person}";
    $('#person').combobox({
        method: "GET",
        url: "${ctx}/cost/piecework/getAllUsers?setid=${person}",
        valueField: 'person',
        textField: 'person',
        mode: 'remote',
        onLoadSuccess: function () {
           // if (man != null && man != "") {
            	
                $('#person').combobox("select", manAssCC2);
                manAssCC2 = "";
          //  }
        }
    }); */
	 
  	 selJobType("personType");
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
					$('#personTypeName').combobox({
						data : date.rows,
						valueField : 'value',
						textField : 'value',
						editable : true 
					});
			},
		});
	}else{
		$('#personTypeName').combobox({
						data : "",
						valueField : "",
						textField : "",
						editable : false 
					});
	}
}

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
			title="人员类型计件规则添加";
			href='${ctx}/cost/piecework/createPersonTypeRule';
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
                		 $("#mainform").submit();
                		 d.panel('close');
                		 /*   $.ajax({
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
                        }); */
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
        var href = '${ctx}/cost/piecework/updatePersonTypeRule/' + row.id;
        add("人员类型计件规则管理", href,"update");
    }

    //查询
    function cx() {
        //var obj = $("#searchFrom").serializeObject();
        /* var workerName = $("#person").combobox("getValue");
        var workerTypeName = $("#filter_LIKES_jobType").combobox("getValue");
        var workDateS = $('#workTimeS').datebox('getValue');
        var workDateE = $('#workTimeE').datebox('getValue'); */
        var personTypeName = $("#personTypeName").combobox("getValue");
        
        dg.datagrid('load',{
        	/* workerName:workerName,
        	workerTypeName:workerTypeName,
        	workDateS:workDateS,
        	workDateE:workDateE */
        	personTypeName:personTypeName
        });
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
                    url: "${ctx}/cost/piecework/deletePersonTypeRule/" + ids,
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