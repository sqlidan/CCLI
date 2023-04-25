<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'north',split:true,border:false" style="height:140px" >
				<div style="padding:5px; height:auto" class="datagrid-toolbar">
		       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="finish()">完成</a>
		       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-lock-delete" plain="true" onclick="unendd()">取消完成</a>
		       	</div>
					<table>
					<tr>
					<td>ASN</td>
					<td><input id="asn" class="easyui-validatebox"  data-options="width:180" value="" /></td>
					<td><input id="cx" onclick="cx()"  value="查询" type="button"/></td>
					<td><input id="sq" onclick="sq()"  value="授权通过" type="button"/></td>
					</tr>
					<tr>
					<td>状态</td>
					<td><input id="state" class="easyui-validatebox"  data-options="width:180"  value="" readonly style="background:#eee"/></td>
					<td>ASN状态</td>
					<td><input id="asnState" class="easyui-validatebox"  data-options="width:180"  value="" readonly style="background:#eee"/></td>
					<td>ASN号</td>
					<td><input id="asnNum" class="easyui-validatebox"  data-options="width:180"  value="" readonly style="background:#eee"/></td>
					<td>提单号</td>
					<td><input id="billNum" class="easyui-validatebox"  data-options="width:180"  value="" readonly style="background:#eee"/></td>
					</tr>
					<tr>
					<td>箱号</td>
					<td><input id="ctnNum" class="easyui-validatebox"  data-options="width:180" value="" readonly style="background:#eee"/></td>
					<td>收货方</td>
					<td><input id="stockIn" class="easyui-validatebox"  data-options="width:180" value="" readonly style="background:#eee"/>
					<input type="hidden" id="stockInId" name="stockInId" value=""/> 
					<input type="hidden" id="enterStockTime" name="enterStockTime" value="" datefmt="yyyy-MM-dd HH:mm:ss" />   
					</td>
					<td>件数</td>
					<td><input id="piece" class="easyui-validatebox"  data-options="width:180" value="" readonly style="background:#eee"/></td>
					<td>净重</td>
					<td><input id="netWeight" class="easyui-validatebox"  data-options="width:180" value="" readonly style="background:#eee"/>吨</td>
					</tr>
					</table>
</div>
<div id="tt" data-options="region:'center',split:true,border:false" class="easyui-tabs" style="width:400px;border-top:1px solid #95b8e7;">
  	<div title="ASN入库货物明细">
  		<table id="dg"></table>
  	</div>
  	<div title="ASN装卸数量统计信息">	
  		<div style="padding:5px;height:auto" class="datagrid-toolbar">
			<shiro:hasPermission name="cost:enterstevedoring:add">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add()">添加</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="cost:enterstevedoring:delete">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-delete" plain="true" onclick="deleteIt()">删除</a>
			</shiro:hasPermission>
		</div>
		<table id="dgg"></table>
		<div id="dlg"></div>
  	</div>
</div>

<script type="text/javascript">
var dg;
var dgg;
var d;
var asn;
var action = "${action}";

document.onkeydown = function () {if(event.keyCode == 13){cx();}};


$(function(){   
  asn = 0;
  asnInfo(asn);
if(action=='update'){
	cx();
}
});

$("#tt").tabs({
	fit:true,
	border:false,
	onSelect:function(title,index){
		if(index == 1){
			if(asn == ""){
				asn = 0;
			}
				zx(asn);
		}
	}
})

function cx(){
	if(action=='update'){
		asn="${asnId}";
		$("#asn").val(asn);}else{
		asn = $("#asn").val();}
if(asn == ""){
    parent.$.easyui.messager.show({title: "操作提示", msg: "请输入ASN号！", position: "bottomRight"});
    return;
}
	$.ajax({
 	  		type: 'POST',
 	  		url: "${ctx}/cost/enterstevedoring/cxasn/"+asn,
 	  		dataType: "json",  
 	  		success: function(msg){
 	  			if(msg != null && msg!=""){
// 	  				var tt = new Array();
//      			tt = msg.split(",");
 	  				$("#asnState").val(msg[0]);
 	  				$("#asnNum").val(msg[1]);
 	  				$("#billNum").val(msg[2]);
 	  				$("#ctnNum").val(msg[3]);
 	  				$("#stockIn").val(msg[4]);
 	  				$("#piece").val(msg[5]);
 	  				$("#netWeight").val(Number(msg[6])/1000);
 	  				$("#stockInId").val(msg[7]);//收货方id
 	  				$("#enterStockTime").val(msg[8]);//入库日期
 	  			}else{
 	  			    parent.$.easyui.messager.show({title: "操作提示", msg: "未查到此条ASN信息！", position: "bottomRight"});
 	  			    return;
 	  			}
 	  		}
})
asnInfo(asn); //入库货物明细
zx(asn); //装载
}

//授权通过
function sq(){
	var asn = $("#asn").val();
	if(asn==""){
		parent.$.easyui.messager.show({title: "操作提示", msg: "请输入ASN号再进行授权！", position: "bottomRight"});
	}else{
		$.ajax({
		type : "GET",
		url : "${ctx}/cost/enterstevedoring/sqAsn" ,
		data : {"asn":asn},
		dataType : "text",
		success : function(msg) {
 				if(msg != "success"){
 				    parent.$.easyui.messager.show({title: "操作提示", msg: "未查到此ASN！", position: "bottomRight"});
 				}else{
 					parent.$.easyui.messager.show({title: "操作提示", msg: "授权成功！", position: "bottomRight"});
 				}
			}
		});
	}
}

//ASN入库货物明细表
function asnInfo(asn){
		dg =$('#dg').datagrid({    
			method: "get",
		    url:'${ctx}/cost/enterstevedoring/json/'+asn, 
		    fit : true,
			fitColumns :false,//水平滚动
			border : false,
			idField : 'skuId',
			striped:true,
			pagination:false,
			rownumbers:true,
			pageNumber:1,
			pageSize :1000,
			pageList : [1000],
			singleSelect:true,
		    columns:[[    
				{field:'SKU_ID',title:'SKU',sortable:false,width:150},
				{field:'ORIGINAL_PIECE',title:'初始件数',sortable:false,width:150},
				{field:'RKNUM',title:'入库件数',sortable:false,width:150},
				{field:'NOW_PIECE',title:'现有件数',sortable:false,width:100},
				{field:'REMOVE_PIECE',title:'拆托件数',sortable:false,width:100},
				{field:'NET_WEIGHT',title:'净重',sortable:false,width:100},
				{field:'GROSS_WEIGHT',title:'毛重',sortable:false,width:100},
				{field:'CARGONAME',title:'品名',sortable:false,width:100},
				{field:'TYPESIZE',title:'规格',sortable:false,width:100},
				{field:'ENTER_STOCK_TIME',title:'入库日期',sortable:false,width:150}
		    ]],
		    enableHeaderClickMenu: true,
		    enableHeaderContextMenu: true,
		    enableRowContextMenu: false,
		    toolbar:'#tb'
		});
}

//ASN装卸货物数量统计信息
function zx(asn){
		dgg=$('#dgg').datagrid({    
			method: "get",
		    url:'${ctx}/cost/enterstevedoring/zxjson/'+asn, 
		    fit : true,
			fitColumns :false,//水平滚动
			border : false,
			idField : 'skuId',
			striped:true,
			pagination:false,
			rownumbers:true,
			pageNumber:1,
			pageSize :1000,
			pageList : [1000],
			singleSelect:false,
		    columns:[[    
		   		{field:'ID',title:'ID',hidden:true},
				{field:'CLIENT',title:'装卸队',sortable:false,width:150},
				{field:'FEE_PLAN',title:'装卸队方案',sortable:false,width:100},
				{field:'SORTING_NUM',title:'应付分拣数量',sortable:false,width:100},
				{field:'MAN_NUM',title:'人工装卸数量',sortable:false,width:100},
				{field:'WRAP_NUM',title:'缠膜数量',sortable:false,width:100},
				{field:'PACK_NUM',title:'打包数量',sortable:false,width:100},
				{field:'IF_OK',title:'是否已完成',hidden:true},
				{field:'IF_ALL_MAN',title:'是否全人工',sortable:false,width:150,
				   	formatter : function(value, row, index) {
	       				return value == 0 ? '否':'是';
	        		}
				},
				{field:'NUMPLUS',title:'重量系数',sortable:false,width:100},
		    ]],
 		    onLoadSuccess:function(data){
 		    	 if(data.total==0){
					$("#state").val("未完成");
					if($("#asnNum").val() == ""){
						$("#state").val("");
					}
				 }else{
 		   		 	var row = dgg.datagrid('getData').rows[0];
 		   		 	if(row.IF_OK == 0){
 		   		 		$("#state").val("未完成");
 		   			 }else{
 		   		  	  $("#state").val("已完成");
 		   		 	}
 		   		 }
 		    },
		    enableHeaderClickMenu: true,
		    enableHeaderContextMenu: true,
		    enableRowContextMenu: false,
		    toolbar:'#tb'
		});
}

//添加装卸明细
function add(){
	if( $("#asnNum").val() == ""){
		parent.$.messager.show({title: "提示", msg: "请先进行查询操作！", position: "bottomRight" });
		return;
	}
	if( $("#state").val() == "已完成"){
     	parent.$.messager.show({title: "提示", msg: "已完成状态无法进行添加操作！", position: "bottomRight" });
     	return;
    }
	//判断是否有相应的费用方案
  d=$("#dlg").dialog({   
    	title: '新增入库装卸',    
	    width: 480,    
	    height: 480,    
	    href:'${ctx}/cost/enterstevedoring/add',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
 				var sortingNum = $("#sortingNuma").val();
   				var manNum = $("#manNuma").val();
  			    var wrapNum = $("#wrapNuma").val();
    			var packNum = $("#packNuma").val();
    			var feeId = $('#feeId').combobox("getValue");
    			if( manNum == "" || wrapNum == "" || packNum == "" || sortingNum == "" ){
    				parent.$.messager.show({title: "提示", msg: "数量不可为空！", position: "bottomRight" });
    				return;
    			}
    			if( manNum ==0 && wrapNum==0 && packNum ==0 && sortingNum ==0){
    				parent.$.messager.show({title: "提示", msg: "数量不可全为0！", position: "bottomRight" });
    				return;
    			}
    			if(feeId=="" || $('#clientId').combobox("getValue") == ""){
    				parent.$.messager.show({title: "提示", msg: "费用方案及装卸队不可为空！", position: "bottomRight" });
    				return;
    			}
    			$.ajax({
    				async: false,
					type : "GET",
					url : "${ctx}/cost/enterstevedoring/judgefee" ,
					data : {"sortingNum":sortingNum , "manNum":manNum , "wrapNum":wrapNum , "packNum":packNum,"feeId":feeId},
					dataType : "text",
					success : function(msg) {
 						if(msg != "success"){
 			   				parent.$.messager.confirm('提示', msg, function(data){
 			     				if(data){
 			    				 goonadd();
 			    				}
 			   				})
 						}else{
 							goonadd();
 						}
					}
				});
 				
 				
				}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}

//判断是否有相应的费用方案
function judgeFee(){
    var sortingNum = $("#sortingNuma").val();
    var manNum = $("#manNuma").val();
    var wrapNum = $("#wrapNuma").val();
    var packNum = $("#packNuma").val();
    var feeId = $('#feeId').combobox("getValue");
    $.ajax({
		type : "GET",
		url : "${ctx}/cost/enterstevedoring/judgefee" ,
		data : {"sortingNum":sortingNum , "manNum":manNum , "wrapNum":wrapNum , "packNum":packNum,"feeId":feeId},
		dataType : "text",
		success : function(msg) {
 			if(msg != "success"){
 			   parent.$.messager.confirm('提示', msg, function(data){
 			     if(!data){
 			    	 return msg;
 			     }
 			   })
 			}
		}
	});
}

function goonadd(){
 		var clientName = $('#clientId').combobox("getText");
 		$("#client").val(clientName);
 		var feePlan = $('#feeId').combobox("getText");
 		$("#feePlan").val(feePlan);
  		$("#mainform").submit(); 
}




//删除
function deleteIt(){
    if( $("#state").val() == "已完成"){
     	parent.$.messager.show({title: "提示", msg: "已完成状态无法进行删除操作！", position: "bottomRight" });
     	return;
    }
    var rows = dgg.datagrid('getSelections');
    var del = dgg.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].ID);
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/cost/enterstevedoring/delete/" + ids,
				success: function(data){
					dgg.datagrid('clearSelections');
					successTip(data, dgg);
				}
			});
		} 
	});
}



//完成
function finish(){
var asn = $("#asnNum").val();
if( $("#asnNum").val() == ""){
		parent.$.messager.show({title: "提示", msg: "请先进行查询操作！", position: "bottomRight" });
		return;
	}
	if( $("#state").val == "已完成"){
     	parent.$.messager.show({title: "提示", msg: "已完成状态无法再次完成！", position: "bottomRight" });
     	return;
    }
 $.ajax({
		type : "get",
		url : "${ctx}/cost/enterstevedoring/addstandbook/" + asn,
		dataType : "text",
		success : function(msg) {
 			if(msg == "success"){
 				parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight" });
 				$("#state").val("已完成");
 			}
		}
	});

}


//取消完成
function unendd() {

	var asn = $("#asnNum").val();
	if( $("#asnNum").val() == ""){
			parent.$.messager.show({title: "提示", msg: "请先进行查询操作！", position: "bottomRight" });
			return;
	}else if( $("#state").val() == "" || $("#state").val() == "未完成"){
     	parent.$.messager.show({title: "提示", msg: "未完成状态无法取消完成！", position: "bottomRight" });
     	return;
    }else{
		parent.$.messager.confirm('提示', '取消完成后将删除装卸队费用，确定要取消？', function (data) {
			if (data) {
			 $.ajax({
					type : "get",
					url : "${ctx}/cost/enterstevedoring/concel/" + asn,
					dataType : "text",
					success : function(msg) {
			 			if(msg == "success"){
			 				parent.$.messager.show({title: "提示", msg: "取消成功！", position: "bottomRight" });
			 				$("#state").val("");
			 			}
					}
				});
			}
		});
    }
}
</script>
</body>
</html>