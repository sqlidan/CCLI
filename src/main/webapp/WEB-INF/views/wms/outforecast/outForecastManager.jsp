<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center'">
	<form id="mainForm"   method="post">
	<div style="height:auto" class="datagrid-toolbar">
		<!--<shiro:hasPermission name="wms:customsm:add">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="clearIt()">添加</a>
	  	<span class="toolbar-item dialog-tool-separator"></span>
	  	</shiro:hasPermission>-->
	  	<shiro:hasPermission name="wms:outforecastma:save">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
		   	<span class="toolbar-item dialog-tool-separator"></span>
	   	</shiro:hasPermission>
	   	<shiro:hasPermission name="wms:outforecastma:delete">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteIt()">删除</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="wms:outforecastma:submitCD">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="submitCd()">生成出库报关单</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outforecastma:submitCIQ">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="submitCiq()">生成出库报检单</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
<!--	<shiro:hasPermission name="wms:customsm:export">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-application-go" plain="true" onclick="submitIt()">导出</a>
	    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="wms:customsm:print">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="okPass()">打印</a>
	    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="wms:customsm:wc">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-delete" plain="true" onclick="noPass()">完成</a>
	    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission> -->
	</div>
	<table class="formTable" >
		<tr>
			<td>出库预报单ID</td>
			<td>
				<input type="text"  id="forId"  name="forId"  class="easyui-validatebox"  data-options="width:180"  value="${bisOutForecast.forId}"  readonly  style="background:#eee">
			</td>
			<td>客户名称：</td>
				<td>
					<input type="hidden" id="clientName" name="clientName">
					<select id="clientId" name="clientId" class="easyui-combobox" data-options="width:180, required:'required'" >
					</select>
				</td>
			<td>报关公司：</td>
				<td>
				<input type="hidden" id="declarationUnit" name="declarationUnit" >
				<select id="declarationUnitId" name="declarationUnitId" class="easyui-combobox" data-options="width:180, required:'required'"  maxlength="30" >
					</select>
				</td>
			<td>报检公司：</td>
				<td>
				<input type="hidden" id="ciqDeclarationUnit" name="ciqDeclarationUnit" >
				<select id="ciqDeclarationUnitId" name="ciqDeclarationUnitId" class="easyui-combobox" data-options="width:180, required:'required'"  maxlength="30" >
				</select> 
			</td>
		</tr>
		<tr>
			<td>提单号：</td>
				<td>
					<input id="billNum" name="billNum" type="text" class="easyui-validatebox"  data-options="width:180, required:'required'" maxlength="17"  value="${bisOutForecast.billNum}">
				</td>
			<td>箱量：</td>
				<td>
				<input type="text" id="ctnCont" name="ctnCont" class="easyui-validatebox"  data-options="width:180" maxlength="10" onkeyup="ischeckNum(this)"  value="${bisOutForecast.ctnCont}">
				</td>
			<td>贸易方式：</td>
				<td>
				<select id="tradeMode" name="tradeMode" class="easyui-combobox" data-options="width:180">
				</select>
				</td>
			<td>报关单号：</td>
				<td>
				<input type="text" id="cdNum" name="cdNum" class="easyui-validatebox"  data-options="width:180" maxlength="25"   value="${bisOutForecast.cdNum}">
			</td>
		</tr>
		<tr>
			<td>客服：</td>
				<td>
				<input id="customerService" name="customerService" class="easyui-validatebox"  data-options="width:180"  value="${bisOutForecast.customerService}" style="background:#eee"  readonly>
				</td>
			<td>修改日期</td>
			<td>
				<input id="updateTime" name="updateTime" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${bisOutForecast.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/> 
			</td>
			<td>创建日期</td>
			<c:choose>
				<c:when test="${action == 'create'}">
				<td>
					<input id="createTime" name="createTime" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/> 
				</td>
				</c:when>
				<c:otherwise>
				<td>
					<input id="createTime" name="createTime" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${bisOutForecast.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/> 
				</td>
				</c:otherwise>
			</c:choose>
			<td>报检单号：</td>
				<td>
				<input type="text" id="ciqNum" name="ciqNum" class="easyui-validatebox"  data-options="width:180" maxlength="25"   value="${bisOutForecast.ciqNum}">
			</td>
		</tr>
		<tr>
				<td>备注：</td>
				<td>
				<input type="text" id="remark" name="remark" class="easyui-validatebox"  data-options="width:180" maxlength="25"   value="${bisForecast.remark}">
				</td>
		</tr>
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false" title="出库预报单货物信息"  style="height:200px">
		<div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
			<div>
			<shiro:hasPermission name="wms:outforecastInfo:add">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:outforecastInfo:update">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:outforecastInfo:delete">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:outforecastInfo:copy">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="copyInfo()">复制</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
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
var dhs;
$(function(){   
if($("#forId").val() == "" ){
  //生成新的出库预报单ID
  $.ajax({
		type : "POST",
		url : "${ctx}/wms/outforecast/getNewForId",
		dataType : "text",
		success : function(date) {
		  $("#forId").val(date);
		}
	});
}
var forId = $("#forId").val()
if(action =="create"){
	$("#customerService").val("${user}");
	forId = 0;
}
	gridDG(forId);
	selectAjax();
});

function selectAjax(){
	//报关公司
	  	var getorg='${bisOutForecast.declarationUnitId}';
	   $('#declarationUnitId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?setid=${bisOutForecast.declarationUnitId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onLoadSuccess:function(){
			if(getorg!=null && getorg!=""){
				 $('#declarationUnitId').combobox("select",getorg);
				 getorg="";
			}
	   }
   	});
   	
   	//报检公司
	  	var getorgb='${bisOutForecast.ciqDeclarationUnitId}';
	   $('#ciqDeclarationUnitId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?setid=${bisOutForecast.ciqDeclarationUnitId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onLoadSuccess:function(){
			if(getorgb!=null && getorgb!=""){
				 $('#ciqDeclarationUnitId').combobox("select",getorgb);
				 getorgb="";
			}
	   }
   	});
   	
   	//物流容器
   		$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=tradeType",
		dataType : "json",
		success : function(date) {
				$('#tradeMode').combobox({
					data : date.rows,
					value : '${bisOutForecast.tradeMode}',
					valueField : 'label',
					textField : 'label',
					editable : false
				});
		}
	});
   	
   	//客户
 	var getstockId='${bisOutForecast.clientId}';
	   $('#clientId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bisOutForecast.clientId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onLoadSuccess:function(){
			if(getstockId!=null && getstockId!=""){
				 $('#clientId').combobox("select",getstockId);
				 getstockId="";
			}
	   }
   	});
}

//清空
function clearIt(){
	window.parent.mainpage.mainTabs.addModule('出库预报单管理','wms/outforecast/create');
}

//查询出库预报单明细
function gridDG(forId){
   dg =$('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/wms/outforecastinfo/json/'+forId, 
	    fit : true,
		fitColumns : true,
		border : false,
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[   
	    	{field:'id',hidden:true},  
	    	{field:'forId',hidden:true},
	        {field:'billNum',title:'提单号',sortable:true,width:100},    
 	        {field:'hscode',title:'海关编码',sortable:true,width:100},
 	        {field:'cargoName',title:'品名',sortable:true,width:100},
 	        {field:'space',title:'规格',sortable:true},
 	        {field:'piece',title:'件数',sortable:true},
 	        {field:'netWeight',title:'净重',sortable:true},
 	        {field:'clearStore',title:'清库/清品名',sortable:true},
 	        {field:'trayNum',title:'木托数量',sortable:true,width:100},
 	        {field:'remark',title:'备注',sortable:true}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
} 


//保存
function submitForm(){
	$("#clientName").val( $("#clientId").combobox("getText") );
	$("#declarationUnit").val( $("#declarationUnitId").combobox("getText") );
	$("#ciqDeclarationUnit").val( $("#ciqDeclarationUnitId").combobox("getText") );
	if($("#mainForm").form('validate')){
		//用ajax提交form
 		$.ajax({
 	  		async: false,
 	  		type: 'POST',
 	  		url: "${ctx}/wms/outforecast/create",
 	  		data: $('#mainForm').serialize(),
 	  		dataType: "text",  
 	  		success: function(msg){
 	  			if(msg == "success"){
 	  				parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
 	  			}
 	  		}
 	  	});
 	}
}

//删除此入库预报单
function deleteIt(){
		var forId = $("#forId").val();
		$.ajax({
				async:false,
				type: 'get',
				url: "${ctx}/wms/outforecast/ifsave/" + forId,
				success: function(data){
					if(data != "success"){
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
					url: "${ctx}/wms/outforecast/delete/" + forId,
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



//增加明细时，先判断是否已保存
function addInfo(){
var forId = $("#forId").val();
$.ajax({
		type:'get',
		url:"${ctx}/wms/outforecast/ifsave/"+forId,
		dataType:"text",
		success: function(data){
			if(data != "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存入库预报单！", position: "bottomRight"});
				return;
			}else{
			addInfoT();
			}
		}
	});	
}

//添加货物明细
function addInfoT(){
	var forId = $("#forId").val();
	d=$("#dlg").dialog({   
    	title: '货物信息添加',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/wms/outforecastinfo/create/'+forId,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
			  if($("#mainform3").form('validate')){
			     $("#mainform3").submit(); 
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
			window.setTimeout(function(){gridDG(forId)},300);
		}
	});
}

//修改
function editInfo(){
	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	d=$("#dlg").dialog({   
    	title: '修改货物信息',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/wms/outforecastinfo/update/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
			  if($("#mainform3").form('validate')){
			  	 $("#mainform3").submit(); 
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
			window.setTimeout(function(){gridDG(row.forId)},300);
		}
	});
	
}

//删除明细
function delInfo(){
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
				url: "${ctx}/wms/outforecastinfo/deleteinfo/" + ids,
				success: function(data){
					dg.datagrid('clearSelections');
					successTip(data, dg);
				}
			});
		} 
	});
}

//复制弹框
function copyInfo(){
var row = dg.datagrid('getSelected');
if(rowIsNull(row)) return;
d=$("#dlg").dialog({   
	    title: '新增入库明细',    
	    width: 380,    
	    height: 340,    
	    href:'${ctx}/wms/outforecastinfo/copy/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'增加',
			handler:function(){
			if($("#mainform3").form('validate')){
				$('#mainform3').submit(); 
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
			window.setTimeout(function(){gridDG(row.forId)},100);
		}
	});
}

//生成报关单
function submitCd(){
	var forId = $("#forId").val();
	if($("#cdNum").val() ==""){
		parent.$.messager.show({title: "提示", msg: "没有报关单号，无法生成报关单！", position: "bottomRight" });
		return;
	}
	$.ajax({
		async:false,
		type:'get',
		url:"${ctx}/wms/outforecast/ifsave/"+forId,
		dataType:"text",
		success: function(data){
			if(data != "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存出库预报单！", position: "bottomRight"});
				return;
			}else{
				$.ajax({
					type: 'get',
					url: "${ctx}/wms/outforecast/customs/" + forId,
					success: function(data){
						if(data == "success"){
							successTip(data, dg);
						}else{
							parent.$.messager.show({title: "提示", msg: "出库报关单中存在此提单号数据！生成报关单失败！", position: "bottomRight" });
							return;
						}
					}
				});
			}
		}
	});	
	
	
}

//生成报检单
function submitCiq(){
	var forId = $("#forId").val();
	if($("#ciqNum").val() ==""){
		parent.$.messager.show({title: "提示", msg: "没有报检单号，无法生成报检单！", position: "bottomRight" });
		return;
	}
	$.ajax({
		async:false,
		type:'get',
		url:"${ctx}/wms/outforecast/ifsave/"+forId,
		dataType:"text",
		success: function(data){
			if(data != "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存出库预报单！", position: "bottomRight"});
				return;
			}else{
				$.ajax({
					type: 'get',
					url: "${ctx}/wms/outforecast/ciq/" + forId,
					success: function(data){
						if(data == "success"){
							successTip(data, dg);
						}else{
							parent.$.messager.show({title: "提示", msg: "出库报检单中存在此提单号数据！生成报检单失败！", position: "bottomRight" });
							return;
						}
					}
				});
			}
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