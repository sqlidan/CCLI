<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<form id="mainform3" action="${ctx}/base/share/startCostShare/${linkId }/${stockId }" method="post" enctype="multipart/form-data">
<%-- <div data-options="region:'north', split:true, border:false" style="height:35px;">
	<div class="datagrid-toolbar" style="height:auto">
	  	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-sign-up" plain="true" data-options="disabled:false" onclick="startShare()">分摊</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-trash" plain="true" data-options="disabled:false" onclick="cancelShare()">取消分摊</a>
		<span class="toolbar-item dialog-tool-separator"></span>
 	</div>  	
	<input type="hidden" id="linkId" name="linkId" value="${linkId }"/>
	<input type="hidden" id="stockId" name="stockId" value="${stockId }"/>
</div>  --%>

<div data-options="region:'center'">
	<div class="easyui-accordion" data-options="multiple:true">
		<div id="openCost1" title="发货方费用" style="height: 270px; width:auto; " data-options="collapsed:false">
			<div id="tbCost1">
			</div>
			<table id="dgCost1"></table> 
			<div id="dlgCost1"></div>
		</div>
		<div id="openCost2" title="收货方费用" style="height: 270px; width: auto; " data-options="collapsed:false">
			<div id="tbCost2">
			</div>
			<table id="dgCost2"></table> 
			<div id="dlgCost2"></div> 
		</div>
	</div>
</div>
</form>
<script type="text/javascript">
var dgCost1;
var dCost1;

var dgCost2;
var dCost2;

//添加行设置
var editRow1 = undefined;
var editRow2 = undefined;

$(function(){   
	var linkIdCost =  $('#linkId').val();
	//发货方
	dgCost1 = $('#dgCost1').datagrid({    
		method: "get",
	    url: '${ctx}/base/share/getToCostData?linkId='+linkIdCost, 
	    fit: true,
		fitColumns: true,
		border: false,
		striped: true,
		pagination: true,
		rownumbers: true,
		pageNumber: 1,
		pageSize: 30,
		pageList: [ 10, 20, 30, 40, 50 ],
		singleSelect: false,
	    columns:[[
            {field:'ck',checkbox:true},
	        {field:'id', title:'id', width:0,hidden:true},      
	        {field:'examineSign', title:'审核',align:'center', width:40,
	        	formatter : function(value, row, index) {
	         		if(value =='0'){
	        	    	return "<font color='red'>未审核</font>";
	        	    }
	       			if(value =='1'){
	        	      	return "<font color='green'>已审核</font>";
	        	    }
	        	}
	        },
	        {field:'shareSign', title:'分摊',align:'center', width:40,
	        	formatter : function(value, row, index) {
	         		if(value =='0'){
	        	    	return "<font color='red'>未分摊</font>";
	        	    }
	       			if(value =='1'){
	        	      	return "<font color='green'>已分摊</font>";
	        	    }
	        	}
	        }, 
	        {field:'feeCode',title:'feeCode', width:0,hidden:true},
	        {field:'feeName',title:'费目', width:40},
	        {field:'customsId',title:'customsId', width:0, hidden:true},
	        {field:'customsName',title:'承担方', width:100},
	        {field:'receiveAmount',title:'原金额', width:40,
	            formatter:function(val,rowData,rowIndex){
			        if(val!=null){
			            return val.toFixed(2);
			        }
			   }
	        },
	        {field:'fentan',title:'分摊比例',width:60,editor:{type:'numberbox',options:{precision:2}}},
	        {field:'num',title:'数量', width:0,hidden:true},
	        {field:'price',title:'单价', width:0,hidden:true},
	        {field:'fentanAmount',title:'分摊金额', width:40,
	        	formatter:function(val,rowData,rowIndex){
			        if(val!=null){
			            return val.toFixed(2);
			        }
			   }
	        },
	        {field:'currency',title:'币种', width:40, 
	        	formatter : function(value, row, index) {
	         		if(value == 0){
	        	    	return '人民币';
	        	    }
	       			if(value == 1){
	        	      	return '美元';
	        	    }
	        	    if(value == 2){
	        	      	return '日元';
	        	    }
	        	    if(value == '201'){
	 	        		return "阿尔及利亚第纳尔";
	 	        	}
	        	}		
	        },
	        {field:'linkId',title:'联系单', width:100},
	        {field:'billNum',title:'提单号', width:80}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tbCost1',
	    onCancelEdit: function(rowIndex, rowData, changes){//取消
			editRow1 = undefined;
			dgCost1.datagrid('reload');
		},
	    onAfterEdit: function(rowIndex, rowData, changes){//确定
	    	sureUpd1(rowData);//1:发货方
	    }
	});
	
	//添加工具栏
    var pager1 = $('#dgCost1').datagrid().datagrid('getPager');
	pager1.pagination({
		buttons:[{
			iconCls:'icon-edit',
			handler:function(){
				var row = dgCost1.datagrid('getSelected');
				if(rowIsNull(row)) return;
				if(row.shareSign =='1'){
					parent.$.messager.show({title: "提示", msg: "该发货方费目已经分摊完毕！", position: "bottomRight"});
					dgCost1.datagrid('reload');
				}else{
					var rowIndex = dgCost1.datagrid('getRowIndex', row);
					dgCost1.datagrid('beginEdit', rowIndex);
				}
			}
		}]
	});	
	
	//收货方
	dgCost2 = $('#dgCost2').datagrid({    
		method: "get",
	    url: '${ctx}/base/share/getFromCostData?linkId='+linkIdCost, 
	    fit: true,
		fitColumns: true,
		border: false,
		striped: true,
		pagination: true,
		rownumbers: true,
		pageNumber: 1,
		pageSize: 30,
		pageList: [ 10, 20, 30, 40, 50 ],
		singleSelect: false,
	    columns:[[
            {field:'ck',checkbox:true},
	        {field:'id', title:'id', width:0,hidden:true},    
	        {field:'examineSign', title:'审核',align:'center', width:40,
	        	formatter : function(value, row, index) {
	         		if(value =='0'){
	        	    	return "<font color='red'>未审核</font>";
	        	    }
	       			if(value =='1'){
	        	      	return "<font color='green'>已审核</font>";
	        	    }
	        	}
	        },
	        {field:'shareSign', title:'分摊',align:'center', width:40,
	        	formatter : function(value, row, index) {
	         		if(value =='0'){
	        	    	return "<font color='red'>未分摊</font>";
	        	    }
	       			if(value =='1'){
	        	      	return "<font color='green'>已分摊</font>";
	        	    }
	        	}
	        }, 
	        {field:'feeCode',title:'feeCode', width:0,hidden:true},
	        {field:'feeName',title:'费目', width:40},
	        {field:'customsId',title:'customsId', width:0, hidden:true},
	        {field:'customsName',title:'承担方', width:100},
	        {field:'receiveAmount',title:'原金额', width:40,
	        	formatter:function(val,rowData,rowIndex){
			        if(val!=null){
			            return val.toFixed(2);
			        }
			   }
	        },
	        {field:'fentan',title:'分摊比例',width:40,editor:{type:'numberbox',options:{precision:2}}},
	        {field:'num',title:'数量', width:0,hidden:true},
	        {field:'price',title:'单价', width:0,hidden:true},
	        {field:'fentanAmount',title:'分摊金额', width:40,
	        	formatter:function(val,rowData,rowIndex){
			        if(val!=null){
			            return val.toFixed(2);
			        }
			   }
	        },
	        {field:'currency',title:'币种', width:40, 
	        	formatter : function(value, row, index) {
	         		if(value == 0){
	        	    	return '人民币';
	        	    }
	       			if(value == 1){
	        	      	return '美元';
	        	    }
	        	    if(value == 2){
	        	      	return '日元';
	        	    }
	        	    if(value == '201'){
	 	        		return "阿尔及利亚第纳尔";
	 	        	}
	        	}		
	        },
	        {field:'linkId',title:'联系单', width:100},
	        {field:'billNum',title:'提单号', width:80}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tbCost2',
	    onCancelEdit: function(rowIndex, rowData, changes){//取消
			editRow2 = undefined;
			dgCost2.datagrid('reload');
		},
	    onAfterEdit: function(rowIndex, rowData, changes){//确认
	    	sureUpd2(rowData);//2:收货方
	    }
	});
	
	//添加工具栏
    var pager2 = $('#dgCost2').datagrid().datagrid('getPager');
	pager2.pagination({
		buttons:[{
			iconCls:'icon-edit',
			handler:function(){
				var row = dgCost2.datagrid('getSelected');
				if(rowIsNull(row)) return;
				if(row.shareSign =='1'){
					parent.$.messager.show({title: "提示", msg: "该收货方费目已经分摊完毕！", position: "bottomRight"});
					dgCost2.datagrid('reload');
				}else{
					var rowIndex = dgCost2.datagrid('getRowIndex', row);
					dgCost2.datagrid('beginEdit', rowIndex);
				}
			}
		}]
	});	
});

//编辑  应收
function sureUpd1(rowData){
	editRow1 = undefined;
	//判断是否审核
	if(rowData.shareSign =='1'){
		parent.$.messager.show({title: "提示", msg: "该发货方费目已经分摊完毕！", position: "bottomRight"});
		dgCost1.datagrid('reload');
	}else{
		parent.$.messager.confirm('提示', '确定要调整分摊比例吗？', function(data){
			if (data){
				$.ajax({
					type: 'GET',
					async: false,
					url: "${ctx}/base/share/updateCostData/"+rowData.id,
					data:"fentan="+rowData.fentan+"&receiveAmount="+rowData.receiveAmount+"&num="+rowData.num,
					success: function(data){
						if(data == 'success'){
							parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
						} 
						dgCost1.datagrid('reload');
						dgCost2.datagrid('reload');
					}
				});
			}else{
				dgCost1.datagrid('reload');
				dgCost2.datagrid('reload');
			}
		});
	}
}

//编辑  应收
function sureUpd2(rowData){
	editRow2 = undefined;
	//判断是否审核
	if(rowData.shareSign =='1'){
		parent.$.messager.show({title: "提示", msg: "该收货方费目已经分摊完毕！", position: "bottomRight"});
		dgCost2.datagrid('reload');
	}else{
		parent.$.messager.confirm('提示', '确定要调整分摊比例吗？', function(data){
			if (data){
				$.ajax({
					type: 'GET',
					async: false,
					url: "${ctx}/base/share/updateCostData/"+rowData.id,
					data:"fentan="+rowData.fentan+"&receiveAmount="+rowData.receiveAmount+"&num="+rowData.num,
					success: function(data){
						if(data == 'success'){
							parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
						}
						dgCost2.datagrid('reload');
						dgCost1.datagrid('reload');
					}
				});
			}else{
				dgCost2.datagrid('reload');
				dgCost1.datagrid('reload');
			}
		});
	}
}
//提交表单
$('#mainform3').form({    
    onSubmit: function(){   
		return true;	// 返回false终止表单提交
    },    
    success:function(data){   
    	if(data == "success"){
    		parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
		}else{
    		parent.$.messager.show({ title : "提示",msg:data, position: "bottomRight" });
		}
    	d1.panel('close');
    	window.dg1.datagrid('reload');
		window.dg2.datagrid('reload');
    }    
});   
</script>
</body>
</html>