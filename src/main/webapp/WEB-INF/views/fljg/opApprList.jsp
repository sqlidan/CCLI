<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="分类监管申请单列表" style="overflow-y:auto">
<div   style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">
      	    <input type="text" name="filter_LIKES_apprId" class="easyui-validatebox" data-options="width:150,prompt: '申请单号'"/>
      	    <input type="text" name="filter_LIKES_linkId" class="easyui-validatebox" data-options="width:150,prompt: '入库联系单号'"/>
      	     <select type="text" id="filter_EQS_ioType" name="filter_EQS_ioType"
                   class="easyui-combobox" data-options="width:150,prompt: '出入区类型'">
                <option></option>
                <option value='0'>无</option>
                <option value='1'>入区</option>
                <option value='2'>出区</option>
            </select>
            、      	<select type="text" id="filter_EQS_apprType" name="filter_EQS_apprType"
                   class="easyui-combobox" data-options="width:150,prompt: '申请单类型'">
                <option></option>
                <option value='0'>非保税货物申请</option>
                <option value='1'>非保税转保税货物申请</option>
                <option value='2'>保税转非保税货物申请</option>
                <option value='3'>内通道出入区</option>
            </select>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="add()">新增</a>

		</form>

	      	<a id="submit" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="submit()" >申报</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>

	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteappr()">删除</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>

        </div>
	<table id="dg"></table> 
</div>
<div data-options="region:'south',split:true,border:false" title="分类监管申请单信息"  style="height:400px">
	<div  style="padding:5px;height:auto" class="datagrid-toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加货物明细</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
	</div>
<table id="dgg"></table> 
</div>

<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg;
var recordId;
document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){
	gridDG();
});
//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}
//申请单列表
function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/opAppr/json',
	    fit : true,
		fitColumns : false,
		border : false,
		//idField : 'forId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[   
	    	{field:'ApprId',title:'申请单号',sortable:true,width:140},  
	        {field:'LinkId',title:'入库联系单号',sortable:true,width:140},    
 	        {field:'ApprType',title:'申请单类型',sortable:true,width:100,
 	         	        formatter: function (value, row, index) {
 	                    	if (value == "0")
 	                    		return '非保税货物申请';
 	                    	else if (value == "1")
 	                    		return '非保税转保税货物申请';
 	                    	else if (value == "2")
 	                    		return '保税转非保税货物申请';
 	                    	else if (value == "3")
 	                    		return '内通道出入区';
                   
                }
   
 	   			},
 	        {field:'IoType',title:'出入区类型',sortable:true,width:100,
 	                    formatter: function (value, row, index) {
 	                    	if (value == "0")
 	                    		return '无';
 	                    	else if (value == "1")
 	                    		return '入区';
 	                    	else if (value == "2")
 	                    		return '出区';
                   
                }
                },
 	        {field:'BondInvtNo',title:'核注清单编号',sortable:true,width:200},
 	        // {field:'PackNo',title:'件数',sortable:true,width:100},
 	        // {field:'GrossWt',title:'毛重',sortable:true,width:100},
 	        {field:'CustomsCode',title:'主管海关',sortable:true,width:100},
 	        {field:'EmsNo',title:'账册编号',sortable:true,width:100},
 	        {field:'GNo',title:'底账商品项号',sortable:true,width:100},
 	        {field:'Fnote',title:'申请备注',sortable:true,width:200},
 	        {field:'ItemNum',title:'提单号',sortable:true,width:200},
 	        {field:'InputEr',title:'录入员',sortable:true,width:100},
 	        {field:'InputDate',title:'录入日期',sortable:true,width:150},
 	        {field:'DDate',title:'申报日期',sortable:true,width:150},
 	        {field:'ApproveDate',title:'审批日期',sortable:true,width:150},
 	        {field:'ApproveNote',title:'审批意见',sortable:true,width:200},
 	        {field:'TradeCode',title:'经营单位编号',sortable:true,width:200},
 	        {field:'TradeName',title:'经营单位名称',sortable:true,width:200},
 	        {field:'OwnerCode',title:'货主单位代码',sortable:true,width:100},
 	        {field:'OwnerName',title:'货主单位名称',sortable:true,width:200},
 	        {field:'AgentCode',title:'申报单位代码',sortable:true,width:200},
 	        {field:'AgentName',title:'申报单位名称',sortable:true,width:200},
 	        {field:'LoadingFlag',title:'是否配载',sortable:true,width:80,
 	        	formatter: function (value, row, index) {
 	        		 	    if (value == "0")
 	                    		return '未配载';
 	                    	if (value == "1")
 	                    		return '部分配载';
 	                    	else if (value == "2")
 	                    		return '全部配载';
 	                    	else if (value == "9")
 	                    		return '不需配载';                   
                }
 	    },
 	        {field:'DeclType',title:'申报类型',sortable:true,width:80,
	         	formatter: function (value, row, index) {
 	                    	if (value == "1")
 	                    		return '首次申报';
 	                    	else if (value == "2")
 	                    		return '变更';
 	                    	else if (value == "3")
 	                    		return '作废';                   
                }
 	    },
 	        {field:'LocalStatus',title:'状态',sortable:true,width:50,
 	         	formatter: function (value, row, index) {
 	                    	if (value == "1")
 	                    		return '保存';
 	                    	else if (value == "2")
 	                    		return '作废';
 	                    	else if (value == "3")
 	                    		return '已提交';                   
                }

 	    },
 	        {field:'Status',title:'申请单状态',sortable:true,width:80,
 	     	         	formatter: function (value, row, index) {
 	                    	if (value == "0")
 	                    		return '暂存';
 	                    	else if (value == "1")
 	                    		return '申报';
 	                    	else if (value == "2")
 	                    		return '审批通过';
 	                    	else if (value == "3")
 	                    		return '审批退回'; 
 	                    	else if (value == "4")
 	                    		return '删除';                    
                }},
 	        {field:'PassStatus',title:'过闸状态',sortable:true,width:80,
 	        //0-为过闸；1-部分过闸；2-全部过闸；3-全部到货确认；4-不需过闸
 	     	         	formatter: function (value, row, index) {
 	                    	if (value == "0")
 	                    		return '过闸';
 	                    	else if (value == "1")
 	                    		return '部分过闸';
 	                    	else if (value == "2")
 	                    		return '全部过闸';
 	                    	else if (value == "3")
 	                    		return '全部到货确认'; 
 	                    	else if (value == "4")
 	                    		return '不需过闸';  
 	                    }
 	    }
	    ]],
	    onClickRow:function(rowIndex, rowData){
	    	info(rowData.id);
	    },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}



//申请单信息
function info(id){
	dgg=$('#dgg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/opApprInfo/json/'+id,
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'LinkId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[
	    	// {field:'ApprId',title:'申请单号',sortable:true,width:100},
	        // {field:'LinkId',title:'出/入库联系单号',sortable:true,width:100},
 	        {field:'ApprGNo',title:'商品序号'},
 	        {field:'GNo',title:'底账项号'},
 	        {field:'CodeTs',title:'商品编码'},
 	        {field:'GName',title:'商品名称'},
 	        {field:'GModel',title:'商品规格型号'},
 	        {field:'GUnit',title:'申报计量单位'},
 	        {field:'GQty',title:'申报数量'},
 	        {field:'Unit1',title:'法定计量单位'},
 	        {field:'Qty1',title:'法定数量'},
 	        {field:'TotalSum',title:'总价'},
 	        {field:'Curr',title:'币制'},
 	        {field:'GrossWt',title:'重量'},
 	        {field:'CREATETIME',title:'创建时间'}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}
function submit(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	$('#submit').linkbutton({disabled:true});
	$.get('${ctx}/supervision/opAppr/apply/'+row.id,
			function(data){
				if(data == "success"){
						successTip(row.LinkId+" 申报成功");
						dg.datagrid('load');
						$('#submit').linkbutton({disabled:false});
				}else{
					parent.$.messager.alert(data);
					$('#submit').linkbutton({disabled:false});
				}

			});
}
//删除记录
function deleteappr(){
	var rows = dg.datagrid('getSelections');
    //如果没有选择行记录
    if(rows.length==0) return;
	var ids= [];
    	for(var i=0; i<rows.length; i++){
    		ids.push(rows[i].id);
    	}
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/supervision/opAppr/del/" + ids,
                    success: function (data) {
                       	if(data == "success"){
                        dg.datagrid('clearSelections');
                        successTip(data, dg, d);
                        parent.$.messager.alert("删除成功");
                        }else{
                        	parent.$.messager.alert(data);
                        }
                    }
                });
            }
        });  
}

//新增申请单
function add() {
	d=$("#dlg").dialog({
		title: '申请单申请',
		width: 560,
		height: 380,
		href:'${ctx}/supervision/opAppr/create',
		modal:true,
		buttons:[{
			text:'确认',
			handler:function(){
				$("#mainform").submit();

			}
		},{
			text:'重置',
			handler:function(){
				resetForm();
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}

//新增申请单信息
function addInfo() {
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	recordId = row.id;
	d=$("#dlg").dialog({
		title: '申请单信息',
		width: 560,
		height: 380,
		href:'${ctx}/supervision/opApprInfo/create',
		modal:true,
		buttons:[{
			text:'确认',
			handler:function(){
				$("#mainform").submit();

			}
		},{
			text:'重置',
			handler:function(){
				resetForm();
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}

//删除申请单信息
function delInfo() {
	var row = dgg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
		if (data) {
			$.ajax({
				type: 'get',
				url: "${ctx}/supervision/opApprInfo/del/" + row.id,
				success: function (data) {
					if(data == "success"){
						dgg.datagrid('clearSelections');
						successTip(data, dgg, d);
						parent.$.messager.alert("删除成功");
					}else{
						parent.$.messager.alert(data);
					}
				}
			});
		}
	});

}
</script>
</body>
</html>