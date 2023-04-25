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
      	    <input type="hidden" name="id" class="easyui-validatebox" data-options="width:150,prompt: '换货表主键id'"/>
      	    <input type="text" name="accountId" class="easyui-validatebox" data-options="width:150,prompt: '质押平台id'"/>
     	    </select>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form> 
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="confirm()">确认</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="refuse()">驳回</a>		      
	</div> 
</div>

<table id="dg"></table> 
<div id="dlg"></div>  

<script type="text/javascript">
var dg;
var d;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
   	
	dg=$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/exchange/comfirm/json', 
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
		singleSelect:true,
	    columns:[[   
 	        {field:'ID',title:'换货表id',sortable:true,width:100,hidden:'true'},
 	        {field:'accountId',title:'质押平台id',sortable:true,width:100},
 	        {field:'originalPledgeId',title:'原押品在库主键',sortable:true,width:100},
 	      	{field:'releasedCustomerName',title:'货主名称',sortable:true,width:100},
 	      	{field:'releasedPName',title:'品名',sortable:true,width:100},
 	      	{field:'releasedCtnNum',title:'箱号',sortable:true,width:100},
 	      	{field:'releasedBillNum',title:'提单号',sortable:true,width:100},
 	        {field:'relieveNumber',title:'仍被质押监管数量',sortable:true,width:100},
 	        {field:'relieveWeight',title:'仍被质押监管重量',sortable:true,width:100},
 	        
 	        {field:'newPledgeId',title:'新押品在库主键',sortable:true,width:100},
 	      	{field:'pledgedCustomerName',title:'货主名称',sortable:true,width:100},
 	      	{field:'pledgedPName',title:'品名',sortable:true,width:100},
 	      	{field:'pledgedCtnNum',title:'箱号',sortable:true,width:100},
 	      	{field:'pledgedBillNum',title:'提单号',sortable:true,width:100},
 	        {field:'pledgeNumber',title:'质押监管的数量',sortable:true,width:100},
 	        {field:'pledgeWeight',title:'质押监管的重量',sortable:true,width:100},
 	        
 	        {field:'sourceTrendId',title:'解除质押标识',sortable:true,width:100},
 	        {field:'relatedTrendId',title:'原质押标识',sortable:true,width:100},
 	        {field:'trendId',title:'新质押标识',sortable:true,width:100},
 	        
 	        {field:'createDate',title:'创建时间',sortable:true,width:100},
 	        {field:'confirmDate',title:'确认时间',sortable:true,width:100,},
   	        {field:'status',title:'生效与否',sortable:true,
  	        	formatter : function(value, row, index) {
  	        		if(row.status == 1){
  	        			return "已生效";
  	        		}else{
  	        			return "未生效";
  	        		}
 	       			/* return value == "1" ? '已生效':'未生效'; */
 	        	}
  	        },
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});



//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj); 
}

//导出
function exportExcel() {
    var url = "${ctx}/wms/pledge/export";
    $("#searchFrom").attr("action", url).submit();
}

//确认
function confirm() {
    var row = dg.datagrid('getSelected');
    var d = JSON.stringify(row);
    console.log(d);
/*     var source_trend_id = row.SOURCE_TREND_ID;
    var related_id = row.RELATED_ID;
    var trend_id = row.TREND_ID;
    var id = row.ID;
    var d = {"source_trend_id" : source_trend_id,
    		 "related_id" : related_id,
    		 "trend_id" : trend_id
    		 "id" : id}; */
    if (rowIsNull(row)) return;
    parent.$.messager.confirm('提示', '确认执行换货操作吗？', function (data) {
    	if(row.status != 0){
    		parent.$.messager.confirm('提示','该记录无法操作');
    		return;
    	}
    	if (data) {
    	     $.ajax({
    	         type: 'post',
    	         async:'false',
    	         url: "${ctx}/exchange/comfirm/exchange/confirm/" + row.id,
    	        // dataType:'json',
    	        // contentType:'application/json',
    	         //data:d,
    	         success: function (data) {
    	             successTip(data, dg);
    	         }
    	     });
    	}
    });
}
//驳回
function refuse() {
    var row = dg.datagrid('getSelected');
    if (rowIsNull(row)) return;
    parent.$.messager.confirm('提示', '确定要驳回吗？', function (data) {
    	if(row.status != 0){
    		parent.$.messager.confirm('提示', '该记录无法驳回');
    		return
    	}else{
    		if (data) {
                $.ajax({
                    type: 'post',
                    url: "${ctx}/exchange/comfirm/exchange/refuse/" + row.id,
                    success: function (data) {
                    	successTip(data, dg);
                    },
                });
            }
    	}
    })
}

</script>
</body>
</html>