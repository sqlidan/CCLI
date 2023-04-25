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
            <input type="text" name="billNum" class="easyui-validatebox"
                   data-options="width:100,prompt: '提单号'"/>
             <input type="text" name="asnNum" class="easyui-validatebox"
                   data-options="width:100,prompt: 'asn号'"/>
             <input type="text" name="ctnNum" class="easyui-validatebox"
                   data-options="width:100,prompt: '箱号'"/>
            <select id="searchStock" name="clientName" class="easyui-combobox"
                    data-options="width:150,prompt: '客户'">
            </select>
            <select id="jobState" name="jobState" class="easyui-combobox"
                    data-options="width:80,prompt: '作业状态'">
                <option></option>
                <option value='0'>在途</option>
                <option value='2'>收货中</option>
                <option value='3'>已收货未上架</option>
                <option value='4'>上架中</option>
                <option value='5'>已上架</option>
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
            <input type="text" id="ccTimeS" name="ccTimeS" class="easyui-datebox" datefmt="yyyy-MM-dd"
                   data-options="width:150,prompt: '预计入库日期'"/>
            - <input type="text" id="ccTimeE" name="ccTimeE" class="easyui-datebox" datefmt="yyyy-MM-dd"
                     data-options="width:150,prompt: '预计入库日期'"/>
            <span class="toolbar-item dialog-tool-separator"></span>

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
            url: '${ctx}/wms/monitor/enterJson',
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
                    field: 'jobState', title: '作业状态', sortable: true, width: 50,
                    formatter: function (value, row, index) {
                        if (value == "0") {
                            return '在途';
                        }
                        else if (value == "1") {
                            return '车辆已入场';
                        }
                        else if (value == "2") {
                            return '收货中';
                        }
                        else if (value == "3") {
                            return '已收货未上架';
                        }
                        else if (value == "4") {
                            return '上架中';
                        }
                        else if (value == "5") {
                            return '已上架';
                        }
                        else {
                            return '无业务';
                        }
                    }
                },
                {field: 'clientName', title: '客户名称', sortable: true, width: 150},
                {field: 'asnNum', title: 'asn号', sortable: true, width: 150},
                {field: 'billNum', title: '提单号', sortable: true, width: 150},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 150},
                {field: 'cargoName', title: '货品名', sortable: true, width: 150},
                {field: 'planNum', title: '计划件数', sortable: true, width: 150},
                {field: 'planWeight', title: '计划吨数', sortable: true, width: 200,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}	
                },
                {field: 'allNum', title: '已收货件数', sortable: true, width: 150},
                {field: 'allWeight', title: '已收货吨数', sortable: true, width: 150, 
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}
                },
                {field: 'shNum', title: '收货件数', hidden:true},
                {field: 'shWeight', title: '收货吨数', hidden:true},
                {field: 'sjNum', title: '上架件数', sortable: true, width: 200},
                {field: 'sjWeight', title: '上架吨数', sortable: true, width: 200,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}	
                },
                {field: 'ccTime', title: '计划入库时间', sortable: true, width: 150},
                {field: 'strTime', title: '开始作业时间', sortable: true, width: 150},
                {field: 'endTime', title: '结束作业时间', sortable: true, width: 150},
                {field: 'operator1', title: '收货人员', sortable: true, width: 150},
                {field: 'operator2', title: '上架人员', sortable: true, width: 150},
                {field: 'linkCreater', title: '客服人员', sortable: true, width: 150}
            ]],
            queryParams: {
		    	ccTimeS:$("#ccTimeS").datebox("getValue"),
		    	ccTimeE:$("#ccTimeE").datebox("getValue")
			},
            onLoadSuccess: function () {
//            	hz1();//本页汇总
//           	hz2();//总体汇总
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