<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="分类监管核放列表" style="overflow-y:auto">
<div   style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">
      	    <input type="text" name="filter_LIKES_ManifestId" class="easyui-validatebox" data-options="width:150,prompt: '核放单编号'"/>
      	    <input type="text" name="filter_LIKES_TradeCode" class="easyui-validatebox" data-options="width:150,prompt: '经营单位编码'"/>
      	    <input type="text" name="filter_LIKES_VehicleId" class="easyui-validatebox" data-options="width:150,prompt: '车辆编号'"/>
      	    <input type="text" name="filter_LIKES_ContaId" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
      	    <select type="text" id="filter_LIKES_IeFlag" name="filter_LIKES_IeFlag"
                   class="easyui-combobox" data-options="width:150,prompt: '进出标志'">
                <option></option>
                <option value='I'>进区</option>
                <option value='E'>出区</option>
            </select>	   
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>

	      	<a id="submit" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="submit()">申报</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>

	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteMani()">删除</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>


      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" data-options="disabled:false" onclick="cancelMani()">作废</a>
       		<span class="toolbar-item dialog-tool-separator"></span>


        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="mani()">新增空车核放单</a>
        	<span class="toolbar-item dialog-tool-separator"></span>

        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="empsubmit()">空车申报</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        	
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="cancelEmpMani()">空车作废</a>
        	<span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="applyDetail()">更新核放单详情</a>
            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="maniConfirm()">入库到货确认（测试用）</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </div>
	<table id="dg"></table> 
</div>
<div data-options="region:'south',split:true,border:false" title="分类监管核放单信息"  style="height:200px">
<!--	<div  style="padding:5px;height:auto" class="datagrid-toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="copyInfo()">复制</a>
	</div> -->
<table id="dgg"></table> 
</div>

<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;
var dgg;
document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){
	gridDG();
});
//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

function gridDG(){	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/mani/json', 
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
	    	{field:'ManifestId',title:'核放单编号',width:140},  
	        {field:'TradeCode',title:'经营单位编码',width:100},    
 	        {field:'TradeName',title:'经营企业名称',width:200},
 	        {field:'ContaId',title:'集装箱编号',width:200}, 	        
 	        {field:'CustomsCode',title:'主管海关',width:80},
 	        {
 	        	field:'IeFlag',title:'进出标志',
                formatter: function (value, row, index) {
                    return value == "I" ? '进区' : '出区';
                }
 	        	},
 	        {field:'EmptyFlag',title:'是否空车核放',width:100},
 	        {field:'IeFlagNote',title:'备注',width:100},
 	        {field:'GoodsValue',title:'货值',width:100},
 	        {field:'GrossWt',title:'货物毛重',width:100},
 	        {field:'VehicleId',title:'车辆号码',width:100},
 	        {field:'VehicleWeight',title:'车辆自重',width:100},
 	        {field:'Status',title:'核放单状态',width:100,
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
                }

        },
            {field:'LocalStatus',title:'状态',width:100,
                formatter: function (value, row, index) {
                            if (value == "1")
                                return '保存';
                            else if (value == "2")
                                return '作废';
                            else if (value == "3")
                                return '已提交';                   
                }
        },
 	        {field:'PassStatus',title:'过闸状态',width:200,
                formatter: function (value, row, index) {
                            if (value == "0")
                                return '未过闸';
                            else if (value == "1")
                                return '已过闸';
                            else if (value == "2")
                                return '到货确认';                   
                }
        },
 	        {field:'InputEr',title:'录入员',width:200},
 	        {field:'InputDate',title:'录入日期',width:200},
 	        {field:'DDate',title:'申报日期',width:200},
 	        {field:'DNote',title:'申请备注',width:200},
 	        {field:'ApprDate',title:'审批日期',width:200},
 	        {field:'ApprNote',title:'审批意见',width:200},
 	        {field:'OrgCode',title:'组织机构代码',width:200},
 	        {field:'PassportDate',title:'过闸时间',width:200},
 	        {field:'DeclType',title:'申报类型',width:200,
                formatter: function (value, row, index) {
                            if (value == "1")
                                return '首次申报';
                            else if (value == "2")
                                return '变更';
                            else if (value == "3")
                                return '作废';                   
                }

        },
 	        {field:'SeqNo',title:'海关序号',width:200},
 	        {field:'CusStatus',title:'海关状态',width:200,
                 formatter: function (value, row, index) {
                            if (value == "0")
                                return '未通过';
                            else if (value == "1")
                                return '发送成功';
                            else if (value == "2")
                                return '通过';  
                            else if (value == "3")
                                return '删除';                     
                }
        },
 	        {field:'CusRmk',title:'海关状态描述',width:200}
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



//核放单信息
function info(id){
	dgg=$('#dgg').datagrid({    
		method: "get",
	    url:'${ctx}/supervision/maniinfo/json/'+id, 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'linkId',
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[
	    	{field:'manifestId',title:'核放单编号',width:100},   
	        {field:'gNo',title:'核放单序号',width:100},    
 	        {field:'ApprId',title:'申请单编号',width:50},
 	        {field:'ApprGNo',title:'申请单商品序号',width:100},
 	        {field:'codeTs',title:'商品编码',sortable:true},
 	        {field:'GName',title:'商品名称',sortable:true},
 	        {field:'gModel',title:'商品规格型号',sortable:true},
 	        {field:'GUnit',title:'申报计量单位',sortable:true},
 	        {field:'GQty',title:'申报数量',sortable:true},
 	        {field:'unit1',title:'法定计量单位',sortable:true},
 	        {field:'qty1',title:'法定数量',sortable:true},
 	        {field:'GrossWt',title:'毛重',sortable:true},
 	        {field:'ConfirmQty',title:'到货确认数',sortable:true},
 	        {field:'EmsGNo',title:'底账项号',sortable:true},
 	        {field:'GdsMtno',title:'商品料号',sortable:true},
 	        {field:'CREATETIME',title:'创建时间',sortable:true},
            {field:'ContaId',title:'箱号',sortable:true},
            {field:'ContaType',title:'箱型',sortable:true},
            {field:'ContaWeight',title:'箱重',sortable:true}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}

//核放单申报
function submit(){
    var row = dg.datagrid('getSelected');
    if(rowIsNull(row)) return;
	$('#submit').linkbutton({disabled:true}); 
    if(row.EmptyFlag=="Y"){
        parent.$.messager.alert("空车核放单请点击'空车申报'");
        return;
    }    
    $.get('${ctx}/supervision/mani/apply/'+row.id,      
            function(data){
                if(data == "success"){
                        successTip("申报成功");
                        dg.datagrid('load');
                        $('#submit').linkbutton({disabled:false});
                }else{
                    parent.$.messager.alert(data);
                    $('#submit').linkbutton({disabled:false});
                }

            });
}
//核放单删除
function deleteMani() {
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
                    type: 'post',
                    url: "${ctx}/supervision/mani/del/" + ids,
                    success: function (data) {
                    	if(data == "success"){
                        dg.datagrid('clearSelections');
                        successTip(data, dg, d);
                        }else{
                            parent.$.messager.alert(data);
                        }
                    }
                });
            }
        });   	
}
//核放单作废
function cancelMani() {
	    var rows = dg.datagrid('getSelections');
    	//如果没有选择行记录
    	if(rows.length==0) return;
    	var ids= [];
    	for(var i=0; i<rows.length; i++){
    		ids.push(rows[i].id);
    	}

        parent.$.messager.confirm('提示', '是否确定要作废？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/supervision/mani/cancel/" + ids,
                    success: function (data) {
                    	if(data == "success"){
                        dg.datagrid('clearSelections');
                        successTip(data, dg, d);
                        }else{
                            parent.$.messager.alert(data);
                        }
                    }
                });
            }
        });   	
}

   //分类监管 空车核放单 弹窗
    function mani() {
    	
    	d=$("#dlg").dialog({   
    	    title: '空车核放单申请',    
    	    width: 560,    
    	    height: 380,    
    	    href:'${ctx}/supervision/mani/createEmp',
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

//更新详情
function applyDetail(){
    var row = dg.datagrid('getSelected');
    if(rowIsNull(row)) return; 
    $.get('${ctx}/supervision/mani/applyDetail/'+row.id,        
            function(data){
                if(data == "success"){
                        successTip(" 更新成功");
                        dg.datagrid('load');
                }else{
                    parent.$.messager.alert(data);
                }

            });
}

//入库到货确认
function maniConfirm(){
    var row = dg.datagrid('getSelected');
    if(rowIsNull(row)) return; 
    $.get('${ctx}/supervision/maniconfirm/request/'+row.id,     
            function(data){
                if(data == "success"){
                        successTip(" 确认成功");
                        dg.datagrid('load');
                }else{
                    parent.$.messager.alert(data);
                }

            });
}

//空车核放单申报
function empsubmit(){
    var row = dg.datagrid('getSelected');
    if(rowIsNull(row)) return;
    if(row.EmptyFlag=="N"){
        parent.$.messager.alert("不是空车记录");
        return;
    }
    $.get('${ctx}/supervision/mani/empty/apply/'+row.id,      
            function(data){
                if(data == "success"){
                        successTip("申报成功");
                        dg.datagrid('load');
                }else{
                    parent.$.messager.alert(data);
                }

            });
}

//空车核放单作废
function cancelEmpMani() {
        var rows = dg.datagrid('getSelections');
        //如果没有选择行记录
        if(rows.length==0) return;

        var ids= [];
        for(var i=0; i<rows.length; i++){
              if(rows[i].EmptyFlag=="N"){
                parent.$.messager.alert("不是空车记录");
                return;
                }
            ids.push(rows[i].id);
        }

        parent.$.messager.confirm('提示', '是否确定要作废？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/supervision/mani/empty/cancel/" + ids,
                    success: function (data) {
                        if(data == "success"){
                        dg.datagrid('clearSelections');
                        successTip(data, dg, d);
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