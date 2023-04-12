<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
        <div>
        	<form id="searchFrom" action="">
        		<select class="easyui-combobox" id="customID" name="filter_EQS_customID" data-options="width:150,prompt: '账单客户'">
					<option value=""></option>
				</select>
				<input type="text" id="codeNum" name="filter_LIKES_codeNum"  class="easyui-validatebox"     data-options="width:150,validType:'length[1,20]',prompt: '账单编号'"  /></td>
		        <input id="yearMonth" name="filter_EQS_yearMonth" type="text"   class="easyui-my97" datefmt="yyyy-MM" data-options="width: 150,prompt: '账单年月'"   />
       	       <select class="easyui-combobox" id="isTrue" name="filter_EQS_isTrue" data-options="width:150,prompt: '是否确认'">
					<option value=""></option>
					<option value="1">是</option>
					<option value="2">否</option>
				</select>
				<span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

			</form>

	       	<shiro:hasPermission name="bis:checkbook:add">
	       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add();">添加</a>
	       		<span class="toolbar-item dialog-tool-separator"></span>
	       	</shiro:hasPermission>

	        <shiro:hasPermission name="bis:checkbook:update">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">修改</a>
	            <span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	         <shiro:hasPermission name="bis:checkbook:del">
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="bis:checkbook:report">
		        <select class="easyui-combobox" id="nType" data-options="width:100,prompt: '导出类型选择'">
						<option value=""></option>
						<option value="1">中文(小计)</option>
						<option value="2">中文</option>
						<option value="3">英文(小计)</option>
						<option value="4">英文</option>
				</select>
				<select class="easyui-combobox" id="type" name="type" data-options="width:100,prompt: '导出选项'">
						<option value=""></option>
						<option value="1">箱号</option>
						<option value="2">Lot</option>
				</select>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="report()">导出对账单</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="reportpdf()">导出对账单</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="uploadfp()">月结上传发票</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editfp()">调整发票</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deletefp()">撤销发票</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-check" plain="true" onclick="openfp()">现结开票</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-print" plain="true" onclick="printfp()">发票打印</a>
	        	<span class="toolbar-item dialog-tool-separator"></span>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-lock-delete" plain="true" onclick="cancelfp()">发票作废</a>
	        </shiro:hasPermission>
			<%--应收对账单上传  later--%>
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="uploadFileToAline()">上传生成结算单</a>
			<%--应收对账单上传  later--%>
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="uploadFileToNotAline()">上传不生成结算单</a>

			<%--应收对账单撤回  later--%>
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="returnUpload()">撤回</a>
        </div>
  </div>
<table id="dg"></table>
<div id="dlg"></div>
<form id="reportFrom" action="#" method="post">
  <input type="hidden" id="repcodeNum" name="codeNum"/>
  <input type="hidden" id="repnType" name="nType"/>
  <input type="hidden" id="reptype" name="type"/>
</form>
<script type="text/javascript">
var dg;
var d;
document.onkeydown = function () {if(event.keyCode == 13){cx();}};
$(document).ready(function(){
	dg=$('#dg').datagrid({
		method: "get",
	    url:'${ctx}/cost/standingBook/listjson',
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'codeNum',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		/*
		* singleSelect:false,  为true 的话 不支持 多选
		* */
		singleSelect:true,
	    columns:[[
			{field:'result',title:'是否开票',sortable:false,width:50,
				formatter : function(value, row, index) {
				    if(value=="success"){
				    	return "<font color='green'>已开票</font>"
				    }else if(value=="upload"){
				    	return "<font color='blue'>已上传</font>"
				    }else if(value=="print"){
				    	return "已打印"
				    }else if(value=="cancel"){
				    	return "<font color='red'>已作废</font>"
				    }else if(value=="remove"){
				    	return "<font color='purple'>已撤销</font>"
				    }else{
				    	return "<font color='grey'>未开票</font>"
				    }
			    }
			},
	        {field:'isTrue',title:'是否确定',sortable:false,width:50,
	        	formatter : function(value, row, index) {
        		    if(value==1){return '是'}else{return '否'}
        	    }
	        },
	        {field:'jsfs',title:'结算方式',sortable:false,width:50,
	        	formatter : function(value, row, index) {
        		    if(value=='Y'){
        		    	return "<font color='red'>月结</font>"
        		    }else if(value=='X'){
        		    	return "<font color='green'>现结</font>"
        		    }else{
        		    	return ""
        		    }
        	    }
	        },
	        {field:'codeNum',title:'账单编号',sortable:false,width:80},
	        {field:'custom',title:'账单客户',sortable:false,width:80},
	        {field:'yearMonth',title:'账单年月',sortable:false,width:80},
	        {field:'operator',title:'制单人',sortable:false,width:50},
	        {field:'operateTime',title:'制单日期',sortable:false,width:80},
	        {field:'remark',title:'备注',sortable:false,width:100},
			{field:'midGroupStatic',title:'上传状态',sortable:false,width: 50,
			formatter:function (value,row,index){
				if(value == null || value == '未上传'){
					return "<font>未上传</font>"
				}else{
					return "<font>已上传</font>"
				}
			}}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
	$('#customID').combobox({
		   method:"GET",
		   url:"${ctx}/base/client/getClientAll?tim=1",
		   valueField: 'ids',
		   textField: 'clientName',
		   mode:'remote'
	});


});


function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj);
}
function add(){
	var href='cost/standingBook/query';
	window.parent.mainpage.mainTabs.addModule('应收对账单管理',href,'icon-hamburg-cv');
}
function upd(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	var href='cost/standingBook/upquery/'+row["codeNum"];
	window.parent.mainpage.mainTabs.addModule('应收对账单管理',href,'icon-hamburg-cv');
}
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'post',
				url:"${ctx}/cost/standingBook/delquery/"+row["codeNum"],
				success: function(data){
					if("error"==data){
						parent.$.easyui.messager.alert("删除操作失败！");
					}else{
						successTip(data,dg);
					}
				}
			});
		}
	});
}
function report(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	var getType=$("#nType").combobox("getValue");
	var type=$("#type").combobox("getValue");
	if(getType==null || ""==getType){
		parent.$.easyui.messager.alert("请选择导出类型！");return;
	}
	$("#repcodeNum").val(row["codeNum"]);
	$("#repnType").val(getType);
	$("#reptype").val(type);
/* 	 $("#reportFrom").attr("action", "/ccli/cost/checkingbook/report"); */
	 $("#reportFrom").attr("action","${ctx}/cost/checkingbook/down");
	$("#reportFrom").submit();
}
function reportpdf(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	var getType=$("#nType").combobox("getValue");
	var type=$("#type").combobox("getValue");
	if(getType==null || ""==getType){
		parent.$.easyui.messager.alert("请选择导出类型！");return;
	}
	$("#repcodeNum").val(row["codeNum"]);
	$("#repnType").val(getType);
	$("#reptype").val(type);
	$("#reportFrom").attr("action","${ctx}/cost/checkingbook/reportpdf");
	$("#reportFrom").submit();
}
//上传发票
function uploadfp(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if("success"==row["result"]||"print"==row["result"]){
		parent.$.easyui.messager.alert("账单已经开票无法进行其他操作！");
		return;
	}
	d=$("#dlg").dialog({
    	title: '上传发票',
	    width: 380,
	    height: 380,
	    href:'${ctx}/cost/standingBook/upload/'+row["codeNum"],
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				if($("#mainForm").form('validate')){
					$("#mainform").submit();
				}
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}
//撤销发票
function deletefp(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if("success"==row["result"]||"print"==row["result"]){
		parent.$.easyui.messager.alert("账单已经开票无法进行其他操作！");
		return;
	}
	parent.$.messager.confirm('提示', '确定要撤销发票信息吗？', function(data){
		if (data){
			$.ajax({
				type:'post',
					url:"${ctx}/cost/standingBook/delData/"+row["codeNum"],
				success: function(data){
					if("error"==data){
						parent.$.easyui.messager.alert("调用接口失败！");
					}else{
						successTip(data,dg);
					}
				}
			});
		}
	});
}

function editfp(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if("success"==row["result"]||"print"==row["result"]){
		parent.$.easyui.messager.alert("账单已经开票无法进行其他操作！");
		return;
	}
	d=$("#dlg").dialog({
    	title: '调整发票',
	    width: 380,
	    height: 380,
	    href:'${ctx}/cost/standingBook/editUpload/'+row["codeNum"],
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				if($("#mainForm").form('validate')){
					$("#mainform").submit();
				}
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}
//开发票
function openfp(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if("success"==row["result"]||"print"==row["result"]){
		parent.$.easyui.messager.alert("账单已经开票无法进行此操作！");
		return;
	}
    parent.$.messager.confirm('提示', '是否要进行开票操作？', function (data) {
        if (data) {
        	d=$("#dlg").dialog({
            	title: '开通发票',
        	    width: 380,
        	    height: 380,
        	    href:'${ctx}/cost/standingBook/open/'+row["codeNum"],
        	    maximizable:true,
        	    modal:true,
        	    buttons:[{
        			text:'确认',
        			handler:function(){
        				if($("#mainForm").form('validate')){
        					$("#mainform").submit();
        				}
        			}
        		},{
        			text:'取消',
        			handler:function(){
        				d.panel('close');
        			}
        		}]
        	});
        }
    });
}
//发票打印
function printfp(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if("success"==row["result"]){
		 parent.$.messager.confirm('提示', '是否要进行发票打印操作？', function (data) {
		    if (data) {
		    	d=$("#dlg").dialog({
	            	title: '发票打印',
	        	    width: 380,
	        	    height: 380,
	        	    href:'${ctx}/cost/standingBook/print/'+row["codeNum"],
	        	    maximizable:true,
	        	    modal:true,
	        	    buttons:[{
	        			text:'打印',
	        			handler:function(){
	        				if($("#mainForm").form('validate')){
	        					$("#mainform").submit();
	        				}
	        			}
	        		},{
	        			text:'取消',
	        			handler:function(){
	        				d.panel('close');
	        			}
	        		}]
	        	});
		    }
		 });
	}else{
		parent.$.easyui.messager.alert("账单未开票无法进行此操作！");
		return;
	}
}

//发票作废
function cancelfp(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	if("success"==row["result"]||"print"==row["result"]){
		 parent.$.messager.confirm('提示', '是否要进行发票作废操作？', function (data) {
		    if (data) {
		    	d=$("#dlg").dialog({
	            	title: '发票作废',
	        	    width: 380,
	        	    height: 380,
	        	    href:'${ctx}/cost/standingBook/cancel/'+row["codeNum"],
	        	    maximizable:true,
	        	    modal:true,
	        	    buttons:[{
	        			text:'作废',
	        			handler:function(){
	        				if($("#mainForm").form('validate')){
	        					$("#mainform").submit();
	        				}
	        			}
	        		},{
	        			text:'取消',
	        			handler:function(){
	        				d.panel('close');
	        			}
	        		}]
	        	});
		    }
		 });
	}else{
		parent.$.easyui.messager.alert("账单不满足发票作废的要求！");
		return;
	}

}
/*
* 上传到中台
* */
function uploadFileToAline(){
	var rows = dg.datagrid('getSelections');
	if(rowIsNull(rows)) return;
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].codeNum);
	}
	console.log(ids)
	parent.$.messager.confirm('提示', '是否要导出数据到中台', function (data) {
		if (data){
			$.ajax({
				type:'post',
				url:"${ctx}/cost/standingbookmid/uploadFileToAline/"+ids,
				success: function(data){
					alert(data)
					if("success"==data){
						successTip(data, dg);
						parent.$.messager.show({title: "提示", msg: "上传成功！", position: "bottomRight"});
						cx()
					}else{
						parent.$.messager.show({title: "提示", msg:data, position: "bottomRight"});
						cx()
					}
				}
			});
		}
	});
}
/*
* 上传到中台
* */
function uploadFileToNotAline(){
	var rows = dg.datagrid('getSelections');
	if(rowIsNull(rows)) return;
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].codeNum);
	}
	console.log(ids)
	parent.$.messager.confirm('提示', '是否要导出数据到中台', function (data) {
		if (data){
			$.ajax({
				type:'post',
				url:"${ctx}/cost/standingbookmid/uploadFileToNotAline/"+ids,
				success: function(data){
					alert(data)
					if("success"==data){
						successTip(data, dg);
						parent.$.messager.show({title: "提示", msg: "上传成功！", position: "bottomRight"});
						cx()
					}else{
						parent.$.messager.show({title: "提示", msg:data, position: "bottomRight"});
						cx()
					}
				}
			});
		}
	});
}



/*
* 上传到中台的数据 撤回
* */
function returnUpload(){
	var rows = dg.datagrid('getSelections');
	if(rowIsNull(rows)) return;
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].codeNum);
	}
	console.log(ids)
	parent.$.messager.confirm('提示', '是否要从中台数据中撤回选中数据', function (data) {
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/cost/standingbookmid/returnUpload/"+ids,
				success: function(data){
					if("success"==data){
						alert(data)
						successTip(data, dg);
						parent.$.messager.show({title: "提示", msg: "撤回成功！", position: "bottomRight"});
						cx()
					}else{
						alert(data)
						parent.$.messager.show({title: "提示", msg:data, position: "bottomRight"});
						cx()
					}
				}
			});
		}
	});
}

</script>
</body>
</html>
