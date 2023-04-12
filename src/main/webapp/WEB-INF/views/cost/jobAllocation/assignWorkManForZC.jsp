<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>

</head>
<body>
	<div style="padding:5px;height:auto">
		<form id="mainform2" action="${ctx}/cost/piecework/assignL" method="post">
	        <table class="formTable">
	            <tr>
	                <td>订单号：</td>
	                <td>
	                    <input id="asnId" name="asnId" class="easyui-validatebox"
	                           style="width: 250, required:'required'" value="${loadingPlanNum}" readonly>
	                </td>
	                <td>
	                    <input type="hidden" id="remindId" name="remindId" class="easyui-validatebox"
	                           style="width: 180, required:'required'" value="${remindId}" readonly>
	                </td>
	                <td>
	                	<input type="hidden" id="loadingNum" name="loadingNum" class="easyui-validatebox"
	                           style="width: 180, required:'required'" value="${loadingTruckNum}" readonly>
	                </td>
	            </tr>
	            <tr>
	                <td>人员类型：</td>
	                <td>
	                    <select id="personType" name="personType" class="easyui-combobox"
	                            data-options="width: 180, required:'required'">
	                    </select>
	                </td>
	                <td>人员：</td>
	                <td>
	                    <select id="person" name="person" class="easyui-combobox"
	                            data-options="width: 180, required:'required'">
	                    </select>
	                </td>
	                <td>
	                	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" 
               				onclick="addWorkMan()">添加</a>
	                </td>
	            </tr>
	            
	          </table>
    	</form>
	</div>
<table id="dg_worker"></table>
<script type="text/javascript">
var dgWorker;
$(function () {
	
	dgWorker = $('#dg_worker').datagrid({ 
					
					method: "get",
			        url: '${ctx}/cost/piecework/loadingOrderWorkerList/${loadingPlanNum}',
			        /* fit: true, */
			        /* fitColumns: false, */
			        border: false,
			        striped: true,
			        pagination: true,
			        rownumbers: true,
			        pageNumber: 1,
			        pageSize: 10,
			        pageList: [10, 15, 20, 30],
			        singleSelect: true,
			       frozenColumns : [ [ 
			                           {
			                              title : '操作',
			                              field : 'caozuo',
			                              align:'center',
			                              width : 100,
			                              formatter :
			                           	   function(value, row, index) {
			                                      
			                                  var  str = "<a href='javascript:void(0)' title='删除' onclick=\"del('  "+row.id+","+row.workerName+" ')\" iconCls='icon-edit'>删除</a>";

			                                  return str;
			                              }
			                           }
			                           ] ], 
				    columns:[[    
				        {field:'id',title:'id',width:120,hidden:true},    
				        {field:'workerTypeName',title:'人员类型',width:230 ,align:'center'},    
				        {field:'workerName',title:'人员',width:230 , align:'center'},
				        {field:'ratio',title:'系数',width:230 , align:'center',hidden:true}
				    ]]
				});
	
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
	
	//人员 
    var manAssCC2="${person}";
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
    });
	
})

function addWorkMan(){
	var personTypeId = $("#personType").combobox("getValue");
	var personTypeName = $("#personType").combobox("getText");
	var linkId = $(" input[ name='asnId' ] ").val();
	var person = $("#person").combobox("getText");
	var loadingTruckNum = $(" input[ name='loadingNum' ] ").val();
	if(personTypeName==null || personTypeName==""){
		parent.$.messager.show({title: "提示", msg: "添加失败，人员类型为空！", position: "bottomRight"});
		return;
	}
	if(person==null || person==""){
		parent.$.messager.show({title: "提示", msg: "添加失败，人员为空！", position: "bottomRight"});
		return;
	}
	
	checkRatioTotal(personTypeId);
	if(ratioTotal<=2){
		$.ajax({
	        type: "POST",
	        url: "${ctx}/cost/piecework/saveBisLoadingOrderWorker",
	        data: {"personTypeId":personTypeId,"personTypeName":personTypeName,"linkId":linkId,"person":person,"loadingTruckNum":loadingTruckNum},
	        dataType: "Text",
	        success: function (date) {
	        	parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
	        	ratioTotal = 0;
	        	$('#dg_worker').datagrid('reload');
	        	$('#dg_worker').datagrid('reload');
	        }
	    });
	}else{
		parent.$.messager.show({title: "提示", msg: "添加失败，系数累计超过限制！", position: "bottomRight"});
		ratioTotal = 0;
		return;
	}
}

function del(id){
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
          if (data) {
              $.ajax({
                  type: 'get',
                  url: "${ctx}/cost/piecework/deleteLoadingOrderWorker/" + id,
                  success: function (data) {
                	  parent.$.messager.show({title: "提示", msg: "删除成功！", position: "bottomRight"});
                  	  $('#dg_worker').datagrid('reload');
                      
                  }
              });
          }
      });
}

//求系数和的方法
var ratioTotal = 0;
function checkRatioTotal(val){
	var personTypeId = val;
	$.ajax({
		async:false,
		type:"POST",
		url:"${ctx}/cost/piecework/checkRatioTotal",
		data:{"personTypeId":personTypeId},
		dataType:"Text",
		success:function(date){
			
			var rows = $('#dg_worker').datagrid('getRows');
		    for (var i = 0; i < rows.length; i++) {
		        ratioTotal += Number(rows[i]['ratio']);
		    }
	       	ratioTotal =  Number(date) + ratioTotal;
		}
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