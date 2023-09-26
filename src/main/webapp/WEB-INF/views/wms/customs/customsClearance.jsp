<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center'">
<form id="searchFrom3" action="">
</form>
	<form id="mainForm"   method="post">
	<div style="height:auto" class="datagrid-toolbar">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
	   	<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-application-go" plain="true" onclick="submitIt()">提交</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-red" plain="true" onclick="reject()">驳回</a>	   	
		<span class="toolbar-item dialog-tool-separator"></span>
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="okpass()">审核</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-delete" plain="true" onclick="nopass()">取消审核</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton"
		   iconCls="icon-standard-page-excel" plain="true"
		   onclick="exportInfo()">导出EXCEL</a>
			<span class="toolbar-item dialog-tool-separator"></span>						   
	</div>
	<table class="formTable" >
		<tr>
		    <td >审核状态</td>
			<td>
				<input type="hidden"  id="auditingState" name="auditingState" value="${bisCustoms.auditingState}" />
				<input id="auditingStateC" class="easyui-validatebox" data-options="width: 180" readonly style="background:#eee" />
			</td>
			<td >业务单号</td>
			<td>
				<input id="cdNum"  name="cdNum" class="easyui-validatebox" data-options="width: 180"  value="${bisCustoms.cdNum}" readonly style="background:#eee" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="20"/>
			</td>
			<td>服务项目</td>
			<td>
				<select id="serviceProject" name="serviceProject" class="easyui-combobox" data-options="width: 180" editable="false"> 
					<option  value="0">报进</option>	
					<option  value="1">报出</option>	
				</select>
			</td>
		  <td>申报日期</td>
			<td>
				<input id="declareTime" name="declareTime" class="easyui-my97" data-options="width: 180" datefmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${bisCustoms.declareTime}" pattern="yyyy-MM-dd HH:mm:ss" />" /> 
			</td>
		  <td>创建人</td>
			<td>
				<input id="operator" name="operator" class="easyui-validatebox" data-options="width: 180" value="${bisCustoms.operator}" readonly style="background:#eee" />
			</td>							
		</tr>
		
			<td>客户</td>
			<td>
					<!-- <input type="hidden" id="clientName" name="clientName"> -->
					<input id="clientName" name="clientName" class="easyui-validatebox" value="${bisCustoms.clientName}"  data-options="width: 180, required:'required'" maxlength="100"/>
					<!-- <select id="clientId" name="clientId" class="easyui-combobox" data-options="width:180, required:'required'" >
					</select> -->
			</td>
			<td>收货人</td>
			<td>
				<input class="easyui-validatebox" id="consignee" name="consignee" value="${bisCustoms.consignee}" data-options="width:180"/>
			</td>
			<td>发货人</td>
			<td>
				<input class="easyui-validatebox" id="consignor" name="consignor" value="${bisCustoms.consignor}"  data-options="width:180"/>
			</td>						
		</tr>
		
		<tr>		
			<td >货权方</td>
			<td>
				<input id="cargoClientName" name="cargoClientName" class="easyui-validatebox" value="${bisCustoms.cargoClientName}"  data-options="width: 180, required:'required'" maxlength="100"/>
<%-- 				<select id="cargoClientId" name="cargoClientId" class="easyui-combobox"  value="${bisCustoms.cargoClientId}" data-options="width:180, required:'required'"  maxlength="30" >
					</select>
				</td> --%>
			<td >消费者使用单位</td>
			<td>
				<input id="useUnit"  name="useUnit" class="easyui-validatebox" value="${bisCustoms.useUnit}" data-options="width: 180"  value="" maxlength="20"/>
			</td>				
			<td>贸易方式</td>
            <td>
				<select id="modeTrade" name="modeTrade" class="easyui-combobox" data-options="width: 180" editable="false"> 
					<option  value="0">保税</option>	
					<option  value="1">来料加工</option>	
					<option  value="2">进料加工</option>
					<option  value="3">一般贸易</option>
				</select>
			</td>
		</tr>
		
		<tr>
			<td>存放地点</td>
			<td>
				<input id="storagePlace" name="storagePlace" class="easyui-validatebox" data-options="width: 180"  value="${bisCustoms.storagePlace}" maxlength="30"/>
			</td>
			<td>入境口岸</td>
			<td>
				<input id="portEntry" name="portEntry" class="easyui-validatebox" data-options="width: 180"  value="${bisCustoms.portEntry}"  maxlength="30"/>
			</td>			
			<td>启运国</td>
			<td>
				<input id="contryDeparture" name="contryDeparture" class="easyui-validatebox" data-options="width: 180"  value="${bisCustoms.contryDeparture}"  maxlength="30"/>
			</td>			
			<td>原产国</td>
			<td>
				<input id="contryOragin" name="contryOragin" class="easyui-validatebox" data-options="width: 180"  value="${bisCustoms.contryOragin}"  maxlength="30"/>
			</td>
		</tr>
		
		<tr>
			<td>提单号</td>
			<td>
				<input id="billNum" name="billNum" class="easyui-validatebox" data-options="width: 180"  value="${bisCustoms.billNum}" maxlength="30"/>
			</td>		
			<td >报关单号</td>
			<td>
				<input id="customsDeclarationNumber"  name="customsDeclarationNumber" class="easyui-validatebox" data-options="width: 180" value="${bisCustoms.customsDeclarationNumber}"  maxlength="20"/>
			</td>
			<td >原报入数量：</td>
			<td>
				<label id="inSumCounts"/>
			</td>
			<td>
				<input type="hidden"  id="operateTime" name="operateTime" value="${bisCustoms.operateTime}" />
			</td>
		</tr>
		
		<tr>



			<!-- <td>明细净重合计：</td>
		 <td><input id="netInfo" class="easyui-validatebox"
				data-options="width: 180" readonly style="background: #eee" /></td> -->
		</tr>
		
		<!-- <tr>
		 <td>明细毛重合计：</td>
		 <td><input id="grossInfo" class="easyui-validatebox"
			data-options="width: 180" readonly style="background: #eee" /></td>
		 <td>明细金额合计：</td>
		 <td><input id="moneyInfo" class="easyui-validatebox"
				data-options="width: 180" readonly style="background: #eee" /></td>
		</tr> -->
		<tr>
			<td >备注</td>
			<td>
				<input id="comments"  name="comments" class="easyui-validatebox" data-options="width: 180"  value="${bisCustoms.comments}" maxlength="200"/>
			</td>
		</tr>
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false" title=" "  style="height:400px">
		<div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
			<div>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
			<span class="toolbar-item dialog-tool-separator"></span>
	    	
	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" data-options="disabled:false" onclick="importIn()">导入报进记录</a>
			<span class="toolbar-item dialog-tool-separator"></span>	
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveInfo()">保存</a>
			<span class="toolbar-item dialog-tool-separator"></span>	
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-refresh" plain="true" data-options="disabled:false" onclick="clearIn()">刷新</a>
	    	</div>
		</div>
       <table id="dg" ></table> 
		<div id="dlg" ></div> 
		<div id="hsdlg"></div>
</div>

<script type="text/javascript">
var dg;
var d;
var action = "${action}";
var a;
var b=0;
var dhs;
var des;

$(function(){
$('#serviceProject').combobox({
	onSelect: function (record) {
		var out = $('#serviceProject').combobox("getValue");
		if(out ==1 ){
			$('#cargoClientName').validatebox({ required: false });
		}else{
			$('#cargoClientName').validatebox({ required: true });
		}
			
	}
})
//报出时检查原报入单中的数量
$('#customsDeclarationNumber').bind('blur',function(){ 
	//var audit = "${bisCustoms.auditingState}";	
	var out = $('#serviceProject').combobox("getValue");
	if(out ==1 ){
	  
	  $.ajax({
			type : "POST",
			url : "${ctx}/wms/customs/clearance/getSum",
			data:{dNumber:$('#customsDeclarationNumber').val()},
			dataType : "text",
			success : function(data) {
			 if(data == "error"){
				 $("#inSumCounts").css("color", "red").html("没有找到对应记录");
				 }
			 else{
				 $("#inSumCounts").css("color", "red").html(data);
				 }
			  
			}
		});
	}
})

if($("#cdNum").val() == "" ){
  //生成新的入库报关ID
  $.ajax({
		type : "POST",
		url : "${ctx}/wms/customs/clearance/getNumber",
		dataType : "text",
		success : function(date) {
		  $("#cdNum").val(date);
		  des = date;
		}
	});
}
if(action =="create"){
	$("#recordMan").val("${user}");
	a = 0;
}else{
	a=1;
}
	gridDG();
	selectAjax();
	/* window.setTimeout(function(){jiaoyan()},200); */
	//物流容器
	getState();
});

//判断当前入库联系单状态
function getState() {
    var stateN = $("#auditingState").val();
    if (stateN == 3) {
        $("#auditingStateC").val("已审核");
    }
    else if (stateN == 2) {
        $("#auditingStateC").val("已驳回");
    }
    else if (stateN == 1) {
        $("#auditingStateC").val("已提交");
    }
    else if (stateN == 0) {
        $("#auditingStateC").val("保存");
    }
    if (action == "create") {
        $("#auditingStateC").val("未保存");
    }
}

function selectAjax(){
     
	//客户
	   var getstockId='${bisCustoms.clientId}';
	   $('#clientId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?setid=${bisCustoms.clientId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onChange:function(){
	   	  if(b==1){
	   		a=1;
	   		b=0;
	   	   }else{
	   	   	a=0;
	   	   }
	   },
	   onSelect:function(){
	   		a=1;
	   		b=1;
	   },
	   onLoadSuccess:function(){
			if(getstockId!=null && getstockId!=""){
				 $('#clientId').combobox("select",getstockId);
				 getstockId="";
			}
	   }
	});
	
	
   	//货权公司
	  	var getorgb='${bisCustoms.cargoClientId}';
	   $('#cargoClientId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?setid=${bisCustoms.cargoClientId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onLoadSuccess:function(){
			if(getorgb!=null && getorgb!=""){
				 $('#cargoClientId').combobox("select",getorgb);
				 getorgb="";
			}
	   }
   	});
	//贸易方式
	$('#modeTrade').combobox("select",'${bisCustoms.modeTrade}');
	//服务项目
	$('#serviceProject').combobox("select",'${bisCustoms.serviceProject}');
	
	
}

//带出原报入单中的货物信息
function importIn(){
	var audit = "${bisCustoms.auditingState}";	
	var out = $('#serviceProject').combobox("getValue");
	var dNumber = $('#customsDeclarationNumber').val()
	if(audit ==3){
		parent.$.easyui.messager.alert("提示","已审核的记录不能修改");
		return;
		}
	if(out !=1){
		parent.$.easyui.messager.alert("提示","报出时可导入原货物信息");
		return;
		}
	if(dNumber == ''){
		parent.$.easyui.messager.alert("提示","原报关单号不能为空");
		return;
		}
	
	  $.ajax({
			type : "POST",
			url : "${ctx}/wms/customs/clearance/info/getRecordByDNumber",
			data:{dNumber},
			dataType : "text",
			success : function(data) {
			 if(data == "error"){
				 }
				
			 else{
				 var cdNum = $("#cdNum").val();
				 var data = JSON.parse(data);
				   console.log(data.length);
				   console.log(data[0]);
				   var json={};
				   for(var i=0;i<data.length;i++){
					   data[i].id='';
					   data[i].cusId = cdNum;
					   $('#dg').datagrid('appendRow', data[i]);
				   }
				   //JSON.stringify(json);
				  //console.log(json);
				  // $('#dg').datagrid('appendRow', data[0]);				 
			
			 }
			}
		});

}
//清除导入页面未保存的记录
function clearIn(){
/* 	var rows = dg.datagrid('getRows');
	alert(rows.length)
	console.log(rows)
	for(var i = 0; i<= rows.length; i++){
		console.log(rows[i].id)	
		if(rows[i].id == ""){			
	  		$('#dg').datagrid('deleteRow', $("#dg").datagrid('getRowIndex', rows[i]));
			}
	  		
						
	} */
	gridDG();
}

//提交
function submitIt(){
    if($("#auditingStateC").val() != "保存")
    {
        if($("#auditingStateC").val() != "已驳回"){
            parent.$.easyui.messager.show({title: "操作提示", msg: "此记录不是保存或已驳回状态，无法提交！", position: "bottomRight"});
            return;
        }
    }

	var cdNum = $("#cdNum").val();
	parent.$.messager.confirm('提示', '您确定要提交？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/customs/clearance/submitIt/" + cdNum,
				success: function(data){
					if(data == "success"){
						parent.$.messager.show({ title : "提示",msg: "提交成功！", position: "bottomRight" });
						$("#auditingStateC").val("已提交");
						$("#auditingState").val("1");
					}
				}
			});
		} 
	});
}

//驳回
function reject(){
    if($("#auditingStateC").val() != "已提交")
    {
      parent.$.easyui.messager.show({title: "操作提示", msg: "此记录未提交，无法驳回！", position: "bottomRight"});
      return;
    }

	 var cdNum = $("#cdNum").val();
	parent.$.messager.confirm('提示', '您确定要驳回？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/customs/clearance/reject/" + cdNum,
				success: function(data){
					if(data == "success"){
						parent.$.messager.show({ title : "提示",msg: "驳回成功！", position: "bottomRight" });
						$("#auditingStateC").val("已驳回");
						$("#auditingState").val("2");
					}
				}
			});
		} 
	});
}

//审核查验
function okpass(){
    if($("#auditingStateC").val() != "已提交")
    {
      parent.$.easyui.messager.show({title: "操作提示", msg: "此记录不是已提交状态，无法审核！", position: "bottomRight"});
      return;
    }
/*      if($("#customsDeclarationNumber").val() ==""){
 		parent.$.messager.alert("提示","报关单号不能为空！");
 		return;
 	}  */
	 var cdNum = $("#cdNum").val();
	parent.$.messager.confirm('提示', '您确定要审核？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/customs/clearance/okpass/" + cdNum,
				success: function(data){
					if(data == "success"){
						parent.$.messager.show({ title : "提示",msg: "审核成功！", position: "bottomRight" });
						$("#auditingStateC").val("已审核");
						$("#auditingState").val("3");
					}
					if(data == "error"){
						parent.$.messager.alert("提示","审核人不能为创建人");
					}
				}
			});
		} 
	});
}

//取消审核查验
function nopass(){
    if($("#auditingStateC").val() != "已审核")
    {
      parent.$.easyui.messager.show({title: "操作提示", msg: "已是未审核状态！", position: "bottomRight"});
      return;
    }
    var cdNum = $("#cdNum").val();
	parent.$.messager.confirm('提示', '您确定取消审核？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/customs/clearance/nopass/" + cdNum,
				success: function(data){
					if(data == "success"){
						parent.$.messager.show({ title : "提示",msg: "取消审核成功！", position: "bottomRight" });
						 $("#auditingStateC").val("未审核");
  						 $("#auditingState").val("0");
					} 
				}
			});
		} 
	});
}
//导出出库联系单明细
function exportInfo(type) {
	// var row = dg.datagrid('getSelected');
	// if (rowIsNull(row))
	// 	return;

	var cdNum = $("#cdNum").val();
	$.ajax({
		type:'get',
		url:"${ctx}/wms/customs/clearance/ifsave/"+cdNum,
		dataType:"text",
		success: function(data){
			if(data == "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存业务单！", position: "bottomRight"});
				return;
			}else{
				var url = "${ctx}/wms/customs/clearance/info/exportinfo/" + cdNum;
				$("#mainForm").attr("action", url).submit();
			}
		}
	});
} 

/* function jiaoyan(){
	$('#billNum').validatebox({    
	   	 	required: true, 
	    	validType:{
	    	length:[4,20],
	    	remote:["${ctx}/wms/customs/clearance/checkbillnum/"+$("#cid").val(),"billNum"]
	    }
	}); 
} */

 function serviceProject(){
$('#serviceProject').validatebox({    
   	 	required: true, 
    	validType:{
    	length:[4,20],
    	remote:["${ctx}/wms/customs/clearance/checkbillnum/"+$("#cid").val(),"billNum"]
    }
}); 
} 



//清空
function clearIt(){
	window.parent.mainpage.mainTabs.addModule('入库报关管理','wms/customs/clearance/add');
}

//查询明细
function gridDG(){
    var cdNum = $("#cdNum").val();
    if(cdNum == ""){
    cdNum = 0;
    }
	dg=$('#dg').datagrid({    
		method: "GET",
	    url:'${ctx}/wms/customs/clearance/info/json/'+cdNum, 
	    fit : true,
		fitColumns : true,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[    
			{field:'id',hidden:true}, 
	    	{field:'cusId',hidden:true}, 
	        {field:'commodityName',title:'商品名称',sortable:true,width:30},    
 	        {field:'latinName',title:'拉丁文名',sortable:true,width:30},
			{field:'accountBook',title:'账册商品序号',sortable:true,width:30},
			{field:'commodityCode',title:'商品编码',sortable:true,width:30},
 	        {field:'specification',title:'规格',sortable:true,width:30},
 	        {field:'num',title:'数量',sortable:true,width:30},
 	        {field:'netWeight',title:'净重',sortable:true,width:30},
 	        {field:'grossWeight',title:'毛重',sortable:true,width:30},
 	        {field:'money',title:'金额',sortable:true,width:30},
 	        {field:'currencyValue',title:'币制',sortable:true,width:30,
	        	formatter : function(value, row, index) {
	 	        	if(value == '0'){
	 	        		return "人民币";
	 	        	}
	 	        	if(value == '1'){
	 	        		return "美元";
	 	        	}
	 	        	if(value == '2'){
	 	        		return "日元";
	 	        	}
	 	        	if(value == '3'){
	 	        		return "欧元";
	 	        	}
	 	        	if(value == '4'){
	 	        		return "英镑";
	 	        	}
	        	}},
 	        {field:'firmName',title:'生产企业名称及注册号',sortable:true,width:30},
	        {field:'packagedForm',title:'包装形式',sortable:true,width:30},
	        {field:'ifWoodenPacking',title:'有无木质包装',sortable:true,width:30,

	        	formatter : function(value, row, index) {
	 	        	if(value == '0'){
	 	        		return "无";
	 	        	}
	 	        	if(value == '1'){
	 	        		return "有";
	 	        	}
		        	}},
			{field:'woodenNo',title:'木托编号',sortable:true,width:30},

		]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb2'
	});
} 


//保存
function submitForm(){
	 if($("#cdNum").val() ==""){
		parent.$.messager.show({ title : "提示",msg: "业务单号不能为空！", position: "bottomRight" });
		return;
	}
	 if($("#serviceProject").val() =="1"){
			parent.$.messager.show({ title : "提示",msg: "业务单号不能为空！", position: "bottomRight" });
			return;
		}
	 if($("#auditingState").val() =="3"){
			parent.$.messager.show({ title : "提示",msg: "已审核的记录不能修改！", position: "bottomRight" });
			return;
		} 	 
	//$("#clientName").val( $("#clientId").combobox("getText") );
	//$("#cargoClientName").val( $("#cargoClientId").combobox("getText") );
	if($("#mainForm").form('validate')){
		//用ajax提交form
 		$.ajax({
 	  		async: false,
 	  		type: 'POST',
 	  		url: "${ctx}/wms/customs/clearance/"+action,
 	  		data: $('#mainForm').serialize(),

 	  		success: function(msg){
 	  			if(msg == "success"){
 	  				parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
                    if ($("#auditingStateC").val() == "未保存") {
                        $("#auditingState").val("0");
                        $("#auditingStateC").val("保存");
                    }
 	  			}
 	  			if(msg == "duplicate"){
 	  				parent.$.messager.alert("提示","此记录已存在，请确认");
 	  			}
 	  			if(msg == "passed"){
 	  				parent.$.messager.show({ title : "提示",msg: "已审核的记录不能修改！", position: "bottomRight" });
 	  			}
 	  		}
 	  	});
 	}
}

//删除此入库报关单
function deleteIt(){
		var cdNum = $("#cdNum").val();
		$.ajax({
				async:false,
				type: 'get',
				url: "${ctx}/wms/customs/clearance/ifsave/" + cdNum,
				success: function(data){
					if(data=="success"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "未保存，无法进行删除操作!", position: "bottomRight"});
						return;
					}
				}
		 });
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
			if(data){
				$.ajax({
					async:false,
					type: 'get',
					url: "${ctx}/wms/customs/clearance/delete/" + cdNum,
					success: function(data){
						if(data=="success"){
							parent.$.easyui.messager.show({title: "操作提示", msg: "删除成功！", position: "bottomRight"});
							window.parent.mainpage.mainTabs.closeCurrentTab();//关闭TAB
						}
					}
		  		})
			}
		})
}


//保存明细
function saveInfo(){
	var audit = "${bisCustoms.auditingState}";	

	if(audit ==3){
		parent.$.easyui.messager.alert("提示","已审核的记录不能修改");
		return;
		}	
	var rows = $("#dg").datagrid("getRows");
	$.ajax({
		type:'post',
		url:"${ctx}/wms/customs/clearance/info/saveInfo",
		contentType: 'application/json;charset=UTF-8',
		data:JSON.stringify(rows),
		success: function(data){
			if(data=="success")	
				parent.$.easyui.messager.show({title: "操作提示", msg: "操作成功！", position: "bottomRight"});
				dg.datagrid('clearSelections');
				successTip(data, dg);
		}
	});

	
}

//增加明细时，先判断是否已保存
function addInfo(){
var cdNum = $("#cdNum").val();
$.ajax({
		type:'get',
		url:"${ctx}/wms/customs/clearance/ifsave/"+cdNum,
		dataType:"text",
		success: function(data){
			if(data == "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存业务单！", position: "bottomRight"});
				return;
			}else{
			addInfoT();
			}
		}
	});	
}

//添加货物明细
function addInfoT(){
	 if($("#auditingState").val() =="3"){
			parent.$.messager.show({ title : "提示",msg: "已审核的记录不能修改！", position: "bottomRight" });
			return;
		} 
	var cid = $("#cid").val();
	var cdNum = $("#cdNum").val();
	d=$("#dlg").dialog({   
    	title: '货物信息添加',    
	    width: 420,    
	    height: 450,    
	    href:'${ctx}/wms/customs/clearance/info/create/'+cdNum,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				if($("#mainform2").form('validate')){
					//if( $("#freeLavyC").is(":checked") ){
						//$("#freeLavy").val("1");
					//}
					
				   $("#mainform2").submit(); 
				   
				   //将mainform2中的各项保存到页面中，此时不调用后台保存方法
/* 				   var data=$("#mainform2").serializeArray();
				   console.log(data);
				   console.log(data[0]);
				   var json={};
				   for(var i=0;i<data.length;i++){
					   json[data[i]["name"]] = data[i]['value'];
				   }				   
				   	$('#dg').datagrid('appendRow', json); */
				   	
				  	d.panel('close');
					successTip(data, dg);				    
			    }
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}],
 		onClose: function (){
			window.setTimeout(function(){gridDG()},100);
		}
	});
}

//修改
function editInfo(){
	 if($("#auditingState").val() =="3"){
			parent.$.messager.show({ title : "提示",msg: "已审核的记录不能修改！", position: "bottomRight" });
			return;
		} 
	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	
	if(row.id == null ||row.id =='')
	{
		parent.$.messager.show({title: "提示", msg: "选中的记录没有保存，不能修改！", position: "bottomRight" });
		return;
	}

	d=$("#dlg").dialog({   
    	title: '修改货物信息',    
	    width: 380,    
	    height: 480,    
	    href:'${ctx}/wms/customs/clearance/info/update/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
			   if($("#mainform2").form('validate')){
			   	   if( $("#freeLavyC").is(":checked") ){
					   $("#freeLavy").val("1");
				   }
				   $("#mainform2").submit(); 				   
				   d.panel('close');
				   successTip(data, dg);
			   }
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){gridDG()},100);
		}
	});
	
}


//删除明细
function delInfo(){
	 if($("#auditingState").val() =="3"){
			parent.$.messager.show({ title : "提示",msg: "已审核的记录不能修改！", position: "bottomRight" });
			return;
		} 
	var rows = dg.datagrid('getSelections');
	var del = dg.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	

	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			var ids= [];
			for(var i=0; i<rows.length; i++){
				
				if(rows[i].id == "")
			  		 $('#dg').datagrid('deleteRow', $("#dg").datagrid('getRowIndex', rows[i]));
				else
					ids.push(rows[i].id)					
							
			}
			if(ids.length != 0)
				delInfoT(ids);
		} 
	});
}

function delInfoT(ids){
	$.ajax({
		type: 'get',
		url: "${ctx}/wms/customs/clearance/info/deleteinfo/" + ids,
		success: function(data){
			parent.$.messager.show({title: "提示", msg: "删除成功！", position: "bottomRight" });
			dg.datagrid('clearSelections');
			successTip(data, dg);
		}
	});
	
}

/* //导出
function exportm(){
	var cid = $("#cid").val();
	$.ajax({
		async:false,
		type:'get',
		url:"${ctx}/wms/customs/ifsave/"+cid,
		dataType:"text",
		success: function(data){
			if(data == "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存入库报关单！", position: "bottomRight"});
				return;
			}else{
					var url = "${ctx}/wms/customs/exportwith/"+cid;
 					$("#searchFrom3").attr("action",url).submit();
			}
		}
	});	

} */

//带有货物信息的打印
/* function printInfo(){
	var cid=$("#cid").val();
	$.ajax({
		async:false,
		type:'get',
		url:"${ctx}/wms/customs/ifsave/"+cid,
		dataType:"text",
		success: function(data){
			if(data == "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存入库报关单！", position: "bottomRight"});
				return;
			}else{
				window.parent.mainpage.mainTabs.addModule('入库报关单（带货物信息）打印','wms/customs/printInfo/' + cid);
			}
		}
	});	
	
} */

//数字校验
function ischeckNum(val) {
	if (val.value) {
		if (!isNaN(val.value)) {

		} else {
			parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
			$("#"+val.id).val("");
			myfm.isnum.select();
			return false;
		}
	}
}


</script>
</body>
</html>