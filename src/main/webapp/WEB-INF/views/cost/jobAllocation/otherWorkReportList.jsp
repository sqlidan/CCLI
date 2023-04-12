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
			 
		-->
			<select id="personName" name="personName" class="easyui-combobox"
                            data-options="width: 180,prompt:'人员'">
             </select>
             <select class="easyui-combobox" id="jobType" name="jobType" data-options="width:150,prompt:'作业类型'">
					<option value=""></option>
			</select>
            <!--  <select class="easyui-combobox" id="personType" name="personType" data-options="width:150,prompt:'人员类型'">
					<option value=""></option>
			</select> -->
            <!--  <select id="type" name="type" class="easyui-combobox" data-options="width:120,prompt: '库存状态'"/>
            	<option></option>
            	<option value='1'>入库作业</option>
            	<option value='2'>出库作业</option>
            </select> -->
            <input type="text" id="workTimeS" name="workTimeS" class="easyui-datebox" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '作业时间开始'"/>
            - <input type="text" id="workTimeE" name="workTimeE" class="easyui-datebox" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '作业时间结束'"/>
             <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        	
    </div>
    <div>
        	合计时长（小时）：<input id="workDuration" name="workDuration" readonly/><span>(点击‘查询’后根据条件统计总时长)</span>
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
    	
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/cost/piecework/otherWorkerReport',
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
                {field: 'personName', title: '人员', sortable: true, width: 100},
                {field: 'jobType', title: '作业类型', sortable: true, width: 100
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
                {field: 'workload', title: '作业时长（小时）', sortable: true, width: 130},
               
            ]],
            queryParams: {
    			first:"1"
			},
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
        
	 	//人员 
        var manAssCC2="${person}";
        $('#personName').combobox({
            method: "GET",
            url: "${ctx}/cost/piecework/getAllUsers?setid=${person}",
            valueField: 'person',
            textField: 'person',
            mode: 'remote',
            onLoadSuccess: function () {
               // if (man != null && man != "") {
                	
                    $('#personName').combobox("select", manAssCC2);
                    manAssCC2 = "";
              //  }
            }
        });
        
     //selPersonType();
//	 selBigType();
	 selJobType("jobType0");
     // personName();
     hejishijian();
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
					$('#jobType').combobox({
						data : date.rows,
						valueField : 'value',
						textField : 'label',
						editable : true 
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


    //创建查询对象并查询
    function cx() {
        //var obj = $("#searchFrom").serializeObject();
        //dg.datagrid('load', obj);
    	 var workerName = $("#personName").combobox("getValue");
         var workerTypeName = $("#jobType").combobox("getValue");
         var workDateS = $('#workTimeS').datebox('getValue');
         var workDateE = $('#workTimeE').datebox('getValue');
         
         dg.datagrid('load',{
         	workerName:workerName,
         	workerTypeName:workerTypeName,
         	workDateS:workDateS,
         	workDateE:workDateE
         });
         
         hejishijian();
    }
    
  //计算合计    
function hejishijian(){
	var workerName = $("#personName").combobox("getValue");
    var workerTypeName = $("#jobType").combobox("getValue");
    var workDateS = $('#workTimeS').datebox('getValue');
    var workDateE = $('#workTimeE').datebox('getValue');
 	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/cost/piecework/otherWorkerReportHeji",
		data : {
         	workerName:workerName,
         	workerTypeName:workerTypeName,
         	workDateS:workDateS,
         	workDateE:workDateE
         },
		dataType : "json",
		success : function(date) {
				var heji = date.rows;
				
				var h = heji[0];
				
			document.getElementById("workDuration").value=h.workload;
		}
		
	});
}
</script>
</body>
</html>