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
	  	<shiro:hasPermission name="cost:inspecionma:save">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
		   	<span class="toolbar-item dialog-tool-separator"></span>
	   	</shiro:hasPermission>
	   	<shiro:hasPermission name="cost:inspecionma:delete">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteIt()">删除</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="cost:inspecionma:okpass">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="okpass()">审核</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	     <shiro:hasPermission name="cost:inspecionma:nopass">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-delete" plain="true" onclick="nopass()">取消审核</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	</div>
	<table class="formTable" >
		<tr>
			<td >审核状态</td>
			<td>
				<input type="hidden"  id="ifPass" name="ifPass" value="${inspection.ifPass}" />
				<input id="thepass"   class="easyui-validatebox" data-options="width: 180"  value="<c:choose><c:when test="${inspection.ifPass==1}">已审核</c:when><c:otherwise>未审核</c:otherwise></c:choose>" readonly style="background:#eee" />
			</td>
			<td>查验单号</td>
			<td>
				<input id="feeId" name="feeId" class="easyui-validatebox" data-options="width: 180,required:'required'"  value="${inspection.feeId}" readonly style="background:#eee" />
			</td>
			<td>
			      零星
			</td>
			<td>
			  <input id="ifLx" name="ifLx" class="easyui-validatebox"  data-options="width: 180" type="checkbox" value="${inspection.ifLx}" <c:if test="${inspection.ifLx==1}">checked</c:if> />  
			</td>
		</tr>
		<tr>
			<td>收费客户</td>
			<td>  
				<input type="hidden" id="clientName" name="clientName" value="${inspection.clientName}"/>
			    <select id="clientId" name="clientId" class="easyui-combobox" data-options="width: 180,required:'required'" > 
			        <option value="${inspection.clientName}">${inspection.clientName}</option>
			    </select>
			</td>
			<td>提单号</td>
			<td>
				<input id="billNum" name="billNum" class="easyui-validatebox"  data-options="width: 180,required:'required'" value="${inspection.billNum}"/>
			</td>
			
			<td>箱号</td>
			<td>
				<input id="ctnNum" name="ctnNum" class="easyui-validatebox"  data-options="width: 180"  value="${inspection.ctnNum}" readonly style="background:#eee"/>
			</td>
		</tr>
		<tr>
			<td>查验日期</td>
			<td>
				<input id="checkDate" name="checkDate" class="easyui-my97" data-options="width: 180,required:'required'" datefmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${inspection.checkDate}" pattern="yyyy-MM-dd HH:mm:ss" />" /> 
			</td>
			<td>箱型</td>
			<td>
				<input id="ctnType" name="ctnType" class="easyui-validatebox" data-options="width: 180" value="${inspection.ctnType}" /> 
			</td>
			<td>箱量</td>
			<td>
				<input id="ctnAmount" name="ctnAmount" class="easyui-validatebox" data-options="width: 180"   value="${inspection.ctnAmount}" readonly style="background:#eee"/> 
			</td>
		</tr>
		<tr>
			<td>查验品类</td>
			<td>
				<select id="checkType" name="checkType" class="easyui-combobox" data-options="width: 180" > 
					<option  value="1">水产</option>	
					<option  value="2">冻肉</option>	
					<option  value="3">水果</option>
					<option  value="4">其他</option>
				</select>
			</td>
			<td>报检号</td>
			<td>
				<input id="inspectionNum" name="inspectionNum" class="easyui-validatebox"  data-options="width: 180"  value="${inspection.inspectionNum}"/>
			</td>
			<td>现结</td>
			<td> 
				<input id="balanceWay" name="balanceWay" class="easyui-validatebox" data-options="width: 180" type="checkbox" value="${inspection.balanceWay}" <c:if test="${inspection.balanceWay == 2}">checked</c:if> /> 
			</td>
		</tr>
		<tr>
		   <td>操作人员</td>
			<c:choose>
				<c:when test="${action == 'create'}">
					<td>
						<input id="operatePerson" name="operatePerson" class="easyui-validatebox" data-options="width: 180" value="${user}" readonly style="background:#eee"/> 
					</td>
				</c:when>
				<c:otherwise>
					<td>
						<input type="hidden" name="operatePerson" class="easyui-validatebox" data-options="width: 180" value="${user}"/> 
						<input id="operatePerson"  class="easyui-validatebox" data-options="width: 180" value="${inspection.operatePerson}" readonly style="background:#eee"/> 
					</td>
				</c:otherwise>
			</c:choose>
		    <td>操作时间</td>
			<c:choose>
				<c:when test="${action == 'create'}">
					<td>
						<input id="operateTime"   name="operateTime" class="easyui-my97" data-options="width: 180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />"   disabled/>
					</td>
				</c:when>
				<c:otherwise>
					<td>
						<input id="operateTime"  name="operateTime"  class="easyui-my97" data-options="width: 180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${inspection.operateTime}" pattern="yyyy-MM-dd HH:mm:ss" />"   disabled/>
					</td>
				</c:otherwise>
			</c:choose>
			<td>备注</td>
			<td>
				<input id="remark" name="remark"  class="easyui-validatebox"   data-options="width: 180" value="${inspection.remark}" maxlength="200"/>
			</td>
		</tr>
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false" title="查验明细"  style="height:200px">
		<div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
			<div>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
			<span class="toolbar-item dialog-tool-separator"></span>
	    	</div>
		</div>
       <table id="dg" ></table> 
		<div id="dlg" ></div> 
</div>

<script type="text/javascript">
var dg;
var d;
var action = "${action}";
var a;
var b=0;

$(function(){   
if(action =="create"){
	a = 0;
}else{
 	a=1;
	var types="${inspection.checkType}"; 
	if(parseInt(types) == 1){
		$("#checkType").combobox("setValue",1);
	}else if(parseInt(types) == 2){
		$("#checkType").combobox("setValue",2);
	}else if(parseInt(types) == 3){
		$("#checkType").combobox("setValue",3);
	}else{
		$("#checkType").combobox("setValue",4);
	}
}
	gridDG();
	   	//客户
 	   var getstockId='${inspection.clientId}';
	   $('#clientId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?setid=${inspection.clientId}",
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
});


//查询查验费用明细
function gridDG(){
    var feeId = "${inspection.feeId}";
	dg=$('#dg').datagrid({    
		method: "GET",
	    url:'${ctx}/cost/inspecioninfo/json/'+feeId, 
	    fit : true,
		fitColumns : true,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 1000,
		pageList : [ 1000, 2000, 3000],
		singleSelect:false,
	    columns:[[    
			{field:'id',hidden:true}, 
			{field:'feeId',hidden:true}, 
	    	{field:'billNum',title:'提单号',sortable:true,width:100}, 
	        {field:'ctnNum',title:'批号/MR',sortable:true,width:100},    
 	        {field:'checkStandard',title:'查验标准',sortable:true,width:100},
 	        {field:'ifPlug',title:'是否插电',sortable:true,width:100,
 	        	formatter : function(value, row, index) {
	 	        	if(value == '0'){
	 	        		return "否";
	 	        	}else{
	 	        		return "是";
	 	        	}
 	        	}
 	        },
 	        {field:'plugDays',title:'插电天数',sortable:true},
 	        {field:'plugUnit',title:'插电标准',sortable:true},
 	        {field:'plugPrice',title:'插电费用',sortable:true},
 	        {field:'ifHang',title:'是否吊箱',sortable:true,width:100,
 	        	formatter : function(value, row, index) {
	 	        	if(value == '0'){
	 	        		return "否";
	 	        	}else{
	 	        		return "是";
	 	        	}
 	        	}
 	         },
 	        {field:'hangTimes',title:'吊箱次数',sortable:true},
 	        {field:'hangUnit',title:'吊箱标准',sortable:true},
 	        {field:'hangPrice',title:'吊箱费用',sortable:true},
 	        {field:'ifField',title:'是否使用场地',sortable:true,width:100,
 	        	formatter : function(value, row, index) {
	 	        	if(value == '0'){
	 	        		return "否";
	 	        	}else{
	 	        		return "是";
	 	        	}
 	        	}
 	         },
 	        {field:'fieldDays',title:'场地天数',sortable:true},
 	        {field:'fieldUnit',title:'场地标准',sortable:true},
 	        {field:'fieldPrice',title:'场地费用',sortable:true},
 	        {field:'ifHanding',title:'是否搬倒',sortable:true,width:100,
 	        	formatter : function(value, row, index) {
	 	        	if(value == '0'){
	 	        		return "否";
	 	        	}else{
	 	        		return "是";
	 	        	}
 	        	}
 	         },
 	        {field:'handingUnit',title:'搬倒标准',sortable:true},
 	        {field:'handingPrice',title:'搬倒费用',sortable:true},
 	        {field:'remark1',title:'备注',sortable:true}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb2'
	});
} 


//保存
function submitForm(){
	if( $("#thepass").val() == "已审核"){
			parent.$.messager.show({title: "提示", msg: "已审核，无法操作！", position: "bottomRight" });
			return;	
	}else{
		if($("#mainForm").form('validate')){
			var billNum=$("#billNum").val();
			var clientId=$("#clientId").combobox("getValue");
			var ifLx="0";
			var balanceWay="1";
            if($("#ifLx").is(":checked")){
            	ifLx="1";
            }
            if($("#balanceWay").is(":checked")){
            	balanceWay="2";
            }
			$.ajax({
			 	  async: false,
			 	  type: 'POST',
			 	  url: "${ctx}/cost/inspecion/checkbill/"+billNum+"/"+clientId+"/"+ifLx,
			 	  dataType: "text",  
			 	  success: function(msg){
			 	  	if(msg == "none"){
			 	  		parent.$.messager.show({ title : "提示",msg: "此提单号不存在！", position: "bottomRight" });
			 	  		return;
			 	  	}else if(msg == "false"){
			 	  		parent.$.messager.show({ title : "提示",msg: "此提单号的结算单位与所选单位不一致！", position: "bottomRight" });
			 	  		return;
			 	  	}else{
			 	  		goon(ifLx,balanceWay);
			 	  	}
			 	  }
			 });
		 }
	}
}

function goon(ifLx,balanceWay){
	    var feeId = $('#feeId').val();
		$("#clientName").val($("#clientId").combobox("getText"));
		if(ifLx=="1"){
			$("input[name='clientId']").val("");
			$("#ifLx").val("1");
		}
		if(balanceWay=="2"){
			$("#balanceWay").val("2");
		}
		if(a == 0&&ifLx=="0"){
			parent.$.messager.show({ title : "提示",msg: "下拉框选项请选择下拉框中的数据!", position: "bottomRight" });
			return;
		}else{
			//用ajax提交form
	 		$.ajax({
	 	  		async: false,
	 	  		type: 'POST',
	 	  		url: "${ctx}/cost/inspecion/create",
	 	  		data: $('#mainForm').serialize(),
	 	  		dataType: "text",  
	 	  		success: function(msg){
	 	  			if(msg == "success"){
	 	  				window.parent.mainpage.mainTabs.addModule('查验修改','cost/inspecion/update/'+feeId);
	 	  				parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
	 	  			}
	 	  		}
	 	  	});
	 	}
}
//删除
function deleteIt(){
	if( $("#thepass").val() == "已审核"){
				parent.$.messager.show({title: "提示", msg: "已审核，无法操作！", position: "bottomRight" });
				return;	
	}else{
		var feeId = $("#feeId").val();
		$.ajax({
				async:false,
				type: 'get',
				url: "${ctx}/cost/inspecion/ifsave/" + feeId,
				dataType:"text",
				success: function(data){
					if(data=="false"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "未保存，无法进行删除操作!", position: "bottomRight"});
						return;
					}else if(data=="hasInfo"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "有明细存在，无法删除!", position: "bottomRight"});
						return;
					}else{
						parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
							if(data){
								$.ajax({
									async:false,
									type: 'get',
									url: "${ctx}/cost/inspecion/delete/" + feeId,
									success: function(data){
										if(data=="success"){
											parent.$.easyui.messager.show({title: "操作提示", msg: "删除成功！", position: "bottomRight"});
											window.parent.mainpage.mainTabs.closeCurrentTab();//关闭TAB
										}
									}
						  		});
							}
						});//end confirm
					}
				}
		 });
	}
}



//增加明细时，先判断是否已保存
function addInfo(){
    var feeId = $("#feeId").val();
	if( $("#thepass").val() == "已审核"){
		parent.$.messager.show({title: "提示", msg: "已审核，无法操作！", position: "bottomRight" });
		return;
	}else{
		$.ajax({
				type:'get',
				url:"${ctx}/cost/inspecion/ifsave/"+feeId,
				dataType:"text",
				success: function(data){
					if(data == "false"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存！", position: "bottomRight"});
						return;
					}else{
					    addInfoT();
					}
				}
			});	
	}
}

//添加查验费用明细
function addInfoT(){
	var feeId = $("#feeId").val();
	var billNum = $("#billNum").val();
	while(billNum.search("/")!=-1){
		billNum = billNum.replace("/","*");
	}
	
	d=$("#dlg").dialog({   
    	title: '查验费用明细添加',    
	    width: 530,    
	    height: 480,    
	    href:'${ctx}/cost/inspecioninfo/create/'+feeId+'/'+billNum,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				if(savejy()!="true"){
					parent.$.messager.show({title: "提示", msg: "选择了的费目下的费用信息必须填写！", position: "bottomRight" });
					return;
				}
				if($("#mainform2").form('validate')){
				    $("#mainform2").submit();
				    d.panel('close');
			    }
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){gridDG()},2000);
		}
	});
}

//修改明细
function editInfo(){
	var feeId = $("#feeId").val();
	if( $("#thepass").val() == "已审核"){
		parent.$.messager.show({title: "提示", msg: "已审核，无法操作！", position: "bottomRight" });
		return;	
	}else{
		var row = dg.datagrid('getSelected');
		if(row == null) {
			parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
			return;
		}
		d=$("#dlg").dialog({   
	    	title: '修改货物信息',    
		    width: 530,    
		    height: 380,    
		    href:'${ctx}/cost/inspecioninfo/update/'+row.id,
		    maximizable:true,
		    modal:true,
		    buttons:[{
				text:'确认',
				handler:function(){
					if(!savejy()){
						parent.$.messager.show({title: "提示", msg: "选择了的费目下的费用信息必须填写！", position: "bottomRight" });
						return;
					}
				   if($("#mainform2").form('validate')){
					   $("#mainform2").submit(); 
					   d.panel('close');
				   }
				}
			},{
				text:'取消',
				handler:function(){
					d.panel('close');
				}
			}],
			onClose: function (){
				window.setTimeout(function(){gridDG()},2000);
			}
		});
	}
}

//删除明细
function delInfo(){
	var feeId = $("#feeId").val();
	if( $("#thepass").val() == "已审核"){
		parent.$.messager.show({title: "提示", msg: "已审核，无法操作！", position: "bottomRight" });
		return;	
	}else{
		var rows = dg.datagrid('getSelections');
		var del = dg.datagrid('getSelected');
		if(del == null) {
			parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
			return;
		}
		var ids= [];
		for(var i=0; i<rows.length; i++){
			ids.push(rows[i].id);
		}
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
			if (data){
				$.ajax({
					type: 'get',
					url: "${ctx}/cost/inspecioninfo/deleteinfo/" + ids,
					success: function(data){
						location.reload();
						parent.$.messager.show({title: "提示", msg: "删除成功！", position: "bottomRight" });
						dg.datagrid('clearSelections');
						successTip(data, dg);
					}
				});
			} 
		});
	}
}



//审核查验
function okpass(){
    if($("#thepass").val() != "未审核")
    {
      parent.$.easyui.messager.show({title: "操作提示", msg: "已是审核状态！", position: "bottomRight"});
      return;
    }
	var feeId = $('#feeId').val();
	parent.$.messager.confirm('提示', '您确定要审核此查验单？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/cost/inspecion/okpass/" + feeId,
				success: function(data){
					if(data == "success"){
						$("#thepass").val("已审核");
						$("#ifPass").val("1");
					}
				}
			});
		} 
	});
}

//取消审核查验
function nopass(){
    if($("#thepass").val() != "已审核")
    {
      parent.$.easyui.messager.show({title: "操作提示", msg: "已是未审核状态！", position: "bottomRight"});
      return;
    }
	var feeId = $('#feeId').val();
	parent.$.messager.confirm('提示', '您确定取消此查验单的审核？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/cost/inspecion/nopass/" + feeId,
				success: function(data){
					if(data == "success"){
						 $("#thepass").val("未审核");
  						 $("#ifPass").val("0");
					} 
				}
			});
		} 
	});
}

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