<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="searchFrom" action="">
           <input type="hidden" name="first" value="2"></input>
        <!-- <select class="easyui-combobox" id="bigType" name="bigType" data-options="width:150,prompt:'作业大类'">
					<option value=""></option>
			</select>
			<select class="easyui-combobox" id="jobType" name="jobType" data-options="width:150,prompt:'作业类型'">
					<option value=""></option>
			</select> 
		-->
            <select class="easyui-combobox" id="personType" name="personType" data-options="width:150,prompt:'人员类型'">
					<option value=""></option>
			</select>
			<select id="personName" name="personName" class="easyui-combobox"
                            data-options="width: 180,prompt:'作业人员'">
             </select>
             <select id="type" name="type" class="easyui-combobox" data-options="width:120,prompt: '库存状态'"/>
            	<option></option>
            	<option value='1'>入库作业</option>
            	<option value='2'>出库作业</option>
            </select>
            <input type="text" id="workTimeS" name="workTimeS" class="easyui-datebox" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '作业时间'"/>
            - <input type="text" id="workTimeE" name="workTimeE" class="easyui-datebox" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '作业时间'"/>
             <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
               onclick="exportExcel()">计件规则报表导出</a>
        </form>
    </div>
    <div>
         
    </div>
</div>
<table id="dg"></table>
<div id="dlg"></div>
<div id="clg"></div>
<script type="text/javascript">
    var dg;
    var d;
    var cd;

    
    $(function () {
    	var curr_time = new Date();
   		var strDate = curr_time.getFullYear()+"-";
   		strDate += curr_time.getMonth()+1+"-";
   		strDate += curr_time.getDate()+" ";
    	$("#workTimeS").datebox("setValue",strDate);
    	$("#workTimeE").datebox("setValue",strDate);
    	
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/cost/piecework/report',
            fit: true,
            fitColumns: true,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'man', title: '作业人员', sortable: true, width: 100},
                {field: 'personType', title: '人员类别', sortable: true, width: 100
                   /*  formatter: function (value, row, index) {
                        if (value == '1') {
                            return '库管人员';
                        }
                        if (value == '2') {
                            return '理货人员';
                        }
                        if (value == '3') {
                            return '叉车人员';
                        }
                    } */
                },
                {field: 'workload', title: '计件量（吨）', sortable: true, width: 130,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'ruleJobType', title: '作业类型', sortable: true, width: 130}
                /* {field: 'ratio', title: '系数', sortable: true, width: 130},
                {field: 'realGrossWeight', title: '出入库吨数', sortable: true, width: 130,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }
                } */
            ]],
            queryParams: {
    			first:"1"
			},
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
        
     selPersonType();
//	 selBigType();
//  	 selJobType("");
     personName();
    });


//作业人员
function personName(){
$('#personName').combobox({
            method: "GET",
	        url: "${ctx}/system/user/getUserAll",
	        valueField: 'name',
	        textField: 'name',
	        mode: 'remote'
        });
}


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
					$('#jobType').combobox({
						data : date.rows,
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
function selPersonType(){
    $.ajax({
        type: "GET",
        url: "${ctx}/cost/piecework/getAllPersonType",
        data: "",
        dataType: "json",
        success: function (date) {
            if (date != null && date.rows != null && date.rows.length > 0) {
                $('#personType').combobox({
                    data: date.rows,
                    valueField: 'personTypeId',
                    textField: 'personTypeName'
                });
            }
        }
    });
	
	/* 	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=personType",
		dataType : "json",
		success : function(date) {
				$('#personType').combobox({
					data : date.rows,
					valueField : 'label',
					textField : 'label',
					editable : false
				});
		}
	}); */
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
				$('#bigType').combobox({
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

//导出
function exportExcel() {
    var url = "${ctx}/cost/piecework/export";
    $("#searchFrom").attr("action", url).submit();
}
    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }

</script>
</body>
</html>