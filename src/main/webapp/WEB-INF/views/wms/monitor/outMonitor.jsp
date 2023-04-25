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
        <form id="infoForm" method="post">
        </form>
        <form id="searchFrom" action="">
        	<input type="text" name="loadingNum" class="easyui-validatebox"
                   data-options="width:100,prompt: '装车单号'"/>
             <input type="text" name="asnNum" class="easyui-validatebox"
                   data-options="width:100,prompt: 'asn号'"/>
            <select id="searchStock" name="clientName" class="easyui-combobox"
                    data-options="width:150,prompt: '客户'">
            </select>
             <input type="text" name="platformNum" class="easyui-validatebox"
                   data-options="width:100,prompt: '月台口'"/>
             <input type="text" name="carNo" class="easyui-validatebox"
                   data-options="width:100,prompt: '车牌号'"/>
            <select id="jobState" name="jobState" class="easyui-combobox"
                    data-options="width:120,prompt: '作业状态'">
                <option></option>
                <option value='6'>在途</option>
                <option value='8'>拣货中</option>
                <option value='9'>完成拣货</option>
                <option value='10'>装车中</option>
                <option value='11'>已完成装车</option>
                <option value='00'>无业务</option>
            </select>
            <input type="text" name="strTimeS" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '作业开始日期'"/>
            - <input type="text" name="strTimeE" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '作业开始日期'"/>   
            <input type="text" name="endTimeS" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '作业结束日期'"/>
            - <input type="text" name="endTimeE" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '作业结束日期'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
 			<input type="text" id="ccTimeS" name="ccTimeS" class="easyui-datebox" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '出库预计日期'"/>
             - <input type="text" id="ccTimeE" name="ccTimeE" class="easyui-datebox" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '出库预计日期'"/>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

        </form>
    </div>
</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var d;

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
        }
    };

    $(function () {
    	var curr_time = new Date();
   		var strDate = curr_time.getFullYear()+"-";
   		strDate += curr_time.getMonth()+1+"-";
   		strDate += curr_time.getDate();
    	$("#ccTimeS").datebox("setValue",strDate);
    	$("#ccTimeE").datebox("setValue",strDate);
        //客户
        $('#searchStock').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'clientName',
            textField: 'clientName',
            mode: 'remote'
        });

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/monitor/outJson',
            fit: true,
            fitColumns: false,
            border: false,
            sortOrder: 'desc',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
            	{
                    field: 'jobState', title: '作业状态', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == "6") {
                            return '在途';
                        }
                        else if (value == "7") {
                            return '车辆已入场';
                        }
                        else if (value == "8") {
                            return '拣货中';
                        }
                        else if (value == "9") {
                            return '完成拣货';
                        }
                        else if (value == "10") {
                            return '装车中';
                        }
                        else if (value == "11") {
                            return '已完成装车';
                        }
                        else {
                            return '无业务';
                        }
                    }
                },
 //          	{field: 'asnNum', title: 'ASN', sortable: true, width: 150},
                {field: 'clientName', title: '客户名称', sortable: true, width: 150},
                {field: 'loadingNum', title: '装车单号', sortable: true, width: 150},
                {field: 'platformNum', title: '月台号', sortable: true, width: 150},
                {field: 'carNo', title: '车牌号', sortable: true, width: 150},
                {field: 'cargoName', title: '货品名', sortable: true, width: 150},
                {field: 'planNum', title: '计划件数', sortable: true, width: 150},
                {field: 'planWeight', title: '计划吨数', sortable: true, width: 150,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}		
                },
                {field: 'allNum', title: '已拣货件数', sortable: true, width: 150},
                {field: 'allWeight', title: '已拣货吨数', sortable: true, width: 150,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}		
                },
                {field: 'jhNum', title: '拣货件数', hidden:true},
                {field: 'jhWeight', title: '拣货吨数', hidden:true,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}	
                },
                {field: 'zcNum', title: '装车件数', sortable: true, width: 150},
                {field: 'zcWeight', title: '装车吨数', sortable: true, width: 150,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}		
                },
                {field: 'ccTime', title: '计划出库时间', sortable: true, width: 150,
                	formatter: function(value,row,index){
                		if( value != null){
                			
                    		return value.substring(0,11);	
                		}
                		
                	}	
                },
                {field: 'strTime', title: '开始作业时间', sortable: true, width: 150},
                {field: 'endTime', title: '结束作业时间', sortable: true, width: 150},
                {field: 'operator1', title: '拣货人员', sortable: true, width: 150},
                {field: 'operator2', title: '装车人员', sortable: true, width: 150},
                {field: 'linkCreater', title: '客服人员', sortable: true, width: 150}
            ]],
            queryParams: {
		    	ccTimeS:$("#ccTimeS").datebox("getValue"),
		    	ccTimeE:$("#ccTimeE").datebox("getValue")
			},
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });



    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }



</script>
</body>
</html>