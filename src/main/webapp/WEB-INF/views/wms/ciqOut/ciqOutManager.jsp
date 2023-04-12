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
	<!--	<shiro:hasPermission name="wms:ciqoutm:add">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="clearIt()">添加</a>
	  	<span class="toolbar-item dialog-tool-separator"></span>
	  	</shiro:hasPermission> -->
	  	<shiro:hasPermission name="wms:ciqoutm:save">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
	   	<span class="toolbar-item dialog-tool-separator"></span>
	   	</shiro:hasPermission>
	   	<shiro:hasPermission name="wms:ciqoutm:delete">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteIt()">删除</a>
	    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
		<shiro:hasPermission name="wms:ciqoutm:export">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-application-go" plain="true" onclick="exportm()">导出</a>
	    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="wms:ciqoutm:print">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="printInfo()">打印</a>
	    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
<!--	    <shiro:hasPermission name="wms:ciqoutm:wc">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-delete" plain="true" onclick="noPass()">完成</a>
	    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission> -->
	</div>
	<table class="formTable" >
		<tr>
			<td >报检号</td>
			<td>
				<input type="hidden" id="cid" name="id" value="${bisCiq.id}">
				<input id="ciqCode"  name="ciqCode" class="easyui-validatebox" data-options="width: 180"  value="${bisCiq.ciqCode}" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="20"/>
			</td>
			<td>提单号</td>
			<td>
				<input id="billNum" name="billNum" class="easyui-validatebox" data-options="width: 180"  value="${bisCiq.billNum}" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="30"/>
			</td>
			<td>检验检疫证明日期</td>
			<td>
				<input id="certificateTime" name="certificateTime" class="easyui-my97"  data-options="width: 180" datefmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${bisCiq.certificateTime}" pattern="yyyy-MM-dd HH:mm:ss" />" >
			</td>
			<td>贸易方式</td>
			<td>
				<select class="easyui-combobox" id="tradeType" name="tradeType"  data-options="width:180" >
				</select>
			</td>
		</tr>
		<tr>
			<td>收货人</td>
			<td>
				<input id="consignee" name="consignee" class="easyui-validatebox" data-options="width: 180"  value="${bisCiq.consignee}" maxlength="20"> 
			</td>
			<td>进出标志</td>
			<td>
				<input type="hidden" id="inOutSign" name="inOutSign" value="2"/>
				<input value="出库" class="easyui-validatebox"  data-options="width: 180"  readonly>
			</td>
			<td>发货人</td>
			<td>
				<input id="consignor" name="consignor" class="easyui-validatebox"  data-options="width: 180" value="${bisCiq.consignor}" maxlength="20"/>
			</td>
			<td>申报日期</td>
			<td>
				<input id="declareTime" name="declareTime" class="easyui-my97"  data-options="width: 180" datefmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${bisCiq.declareTime}" pattern="yyyy-MM-dd HH:mm:ss" />" >
			</td>
		</tr>
		<tr>
			<td>放行日期</td>
			<td>
				<input id="releaseTime" name="releaseTime" class="easyui-my97"  data-options="width: 180" datefmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${bisCiq.releaseTime}" pattern="yyyy-MM-dd HH:mm:ss" />" >
			</td>
			<td>船名</td>
			<td>
				<input id="vesselName" name="vesselName" class="easyui-validatebox" data-options="width: 180" value="${bisCiq.vesselName}" maxlength="20"/> 
			</td>
			<td>航次</td>
			<td>
				<input id="voyageNum" name="voyageNum" class="easyui-validatebox" data-options="width: 180" value="${bisCiq.voyageNum}" maxlength="10"/> 
			</td>
			<td>件数</td>
			<td>
				<input id="piece" name="piece" class="easyui-validatebox" data-options="width: 180"  value="${bisCiq.piece}"   onkeyup="ischeckNum(this)" maxlength="8"/> 
			</td>
		</tr>
		<tr>
			<td>净重</td>
			<td>
				<input id="netWeight" name="netWeight" class="easyui-validatebox" data-options="width: 180" value="${bisCiq.netWeight}"   onkeyup="ischeckNum(this)" maxlength="8"/> 
			</td>
			<td>录入员</td>
			<td>
				<input id="recordMan" name="recordMan" class="easyui-validatebox" data-options="width: 180" value="${bisCiq.recordMan}" /> 
			</td>
			<td>录入日期</td>
			<c:choose>
				<c:when test="${action == 'create'}">
					<td>
						<input id="recordTime"   name="recordTime" class="easyui-my97" data-options="width: 180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />"   disabled>
					</td>
				</c:when>
				<c:otherwise>
					<td>
						<input id="recordTime"   name="recordTime" class="easyui-my97" data-options="width: 180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${bisCiq.recordTime}" pattern="yyyy-MM-dd HH:mm:ss" />"   disabled>
					</td>
				</c:otherwise>
			</c:choose>
			<td>修改日期</td>
			<td>
				<input id="updateTime"  name="updateTime" class="easyui-my97" data-options="width: 180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${bisCiq.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" />"  disabled> 
			</td>
		</tr>
		<tr>
			<td>备注一</td>
			<td>
				<input id="remark1" name="remark1"  class="easyui-validatebox"   data-options="width: 180" value="${bisCiq.remark1}" maxlength="25">
			</td>
			<td>备注二</td>
			<td>
				<input id="remark2" name="remark2" class="easyui-validatebox" data-options="width: 180" value="${bisCiq.remark2}" maxlength="25"/> 
			</td>
			<td>备注三</td>
			<td>
				<input id="remark3" name="remark3"  class="easyui-validatebox"   data-options="width: 180" value="${bisCiq.remark3}" maxlength="25">
			</td>
			<td>客户</td>
			<td>
				<input type="hidden" name="extensionPerson" value="${bisCiq.extensionPerson}">
				<input type="hidden" name="extensionTime" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${bisCiq.extensionTime}" pattern="yyyy-MM-dd HH:mm:ss" />">
				<input type="hidden" id="clientName" name="clientName" /> 
				<select id="clientId" name="clientId" class="easyui-validatebox" data-options="width: 180" > 
				</select>
			</td>
		</tr>
		<tr>
			<td>延期次数</td>
				<c:choose>
				<c:when test="${action == 'create'}">
					<td>
						<input id="extension"   name="extension" class="easyui-validatebox" data-options="width: 180"  value="0"  readonly>
					</td>
				</c:when>
				<c:otherwise>
					<td>
						<input id="extension"   name="extension" class="easyui-validatebox" data-options="width: 180"  value="${bisCiq.extension}"  readonly>
					</td>
				</c:otherwise>
				</c:choose>
		</tr>
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false" title="出库报检货物信息"  style="height:200px">
		<div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
			<div>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="copyInfo()">复制</a>
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

$(function(){   
if($("#cid").val() == "" ){
  //生成新的出库报检单ID
  $.ajax({
		type : "POST",
		url : "${ctx}/wms/ciqout/getNewNumber",
		dataType : "text",
		success : function(date) {
		  $("#cid").val(date);
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
	window.setTimeout(function(){jiaoyan()},200);
			//物流容器
   		$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=tradeType",
		dataType : "json",
		success : function(date) {
				$('#tradeType').combobox({
					data : date.rows,
					value : '${bisCiq.tradeType}',
					valueField : 'label',
					textField : 'label',
					editable : false
				});
		}
	});
	
	//客户
 		var getstockId='${bisCiq.clientId}';
	   $('#clientId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bisCiq.clientId}",
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

function jiaoyan(){
	$('#billNum').validatebox({    
	   	 	required: true, 
	    	validType:{
	    	length:[4,20],
	    	remote:["${ctx}/wms/ciqout/checkbillnum/"+$("#cid").val(),"billNum"]
	    }
	}); 
}

//清空
function clearIt(){
	window.parent.mainpage.mainTabs.addModule('出库报检管理','wms/ciqout/add');
}

//查询出库报关单明细
function gridDG(){
    var ciqId = $("#cid").val();
    if(ciqId == ""){
    ciqId = 0;
    }
	dg=$('#dg').datagrid({    
		method: "GET",
	    url:'${ctx}/wms/ciqoutinfo/json/'+ciqId, 
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
	    	{field:'ciqId',hidden:true}, 
 	         {field:'ciqNum',title:'HS编码',sortable:true,width:100},
 	        {field:'cargoName',title:'商品名称',sortable:true,width:100},
 	        {field:'scalar',title:'件数',sortable:true},
 	        {field:'netWeight',title:'净重',sortable:true},
 	        {field:'bagType',title:'包装种类',sortable:true},
 	        {field:'price',title:'货值',sortable:true},
 	        {field:'recordMan',title:'录入员',sortable:true},
 	        {field:'recordTime',title:'录入时间',sortable:true},
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
	if( $("#ciqCode").val() == ""){
		parent.$.messager.show({ title : "提示",msg: "报检号不能为空！", position: "bottomRight" });
		return;
	}
	$("#clientName").val( $("#clientId").combobox("getText") );
	if($("#mainForm").form('validate')){
		if(a == 0){
			parent.$.messager.show({ title : "提示",msg: "下拉框选项请选择下拉框中的数据!", position: "bottomRight" });
			return;
		}else{
			//用ajax提交form
	 		$.ajax({
	 	  		async: false,
	 	  		type: 'POST',
	 	  		url: "${ctx}/wms/ciqout/create",
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
}

//删除此出库报关单
function deleteIt(){
		var cid = $("#cid").val();
		$.ajax({
				async:false,
				type: 'get',
				url: "${ctx}/wms/ciqout/ifsave/" + cid,
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
					url: "${ctx}/wms/ciqout/delete/" + cid,
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
var cid = $("#cid").val();
$.ajax({
		type:'get',
		url:"${ctx}/wms/ciqout/ifsave/"+cid,
		dataType:"text",
		success: function(data){
			if(data == "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存出库报关单！", position: "bottomRight"});
				return;
			}else{
			addInfoT();
			}
		}
	});	
}

//添加货物明细
function addInfoT(){
	var cid = $("#cid").val();
	var ciqNum = $("#ciqCode").val();
	d=$("#dlg").dialog({   
    	title: '货物信息添加',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/wms/ciqoutinfo/create/'+cid+'/'+ciqNum,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
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
			window.setTimeout(function(){gridDG()},100);
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
	    href:'${ctx}/wms/ciqoutinfo/update/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
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
			window.setTimeout(function(){gridDG()},100);
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
				url: "${ctx}/wms/ciqoutinfo/deleteinfo/" + ids,
				success: function(data){
					parent.$.messager.show({title: "提示", msg: "删除成功！", position: "bottomRight" });
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
	    title: '货物信息添加',    
	    width: 380,    
	    height: 340,    
	    href:'${ctx}/wms/ciqoutinfo/copy/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'增加',
			handler:function(){
				if($("#mainform2").form('validate')){
					$('#mainform2').submit(); 
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
			window.setTimeout(function(){gridDG()},100);
		}
	});
}

//导出
function exportm(){
	var cid = $("#cid").val();
	$.ajax({
		async:false,
		type:'get',
		url:"${ctx}/wms/ciqout/ifsave/"+cid,
		dataType:"text",
		success: function(data){
			if(data == "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存出库报检单！", position: "bottomRight"});
				return;
			}else{
				var url = "${ctx}/wms/ciqout/exportwith/"+cid;
 				$("#searchFrom3").attr("action",url).submit();
			}
		}
	});	
	
}

//带有货物信息的打印
function printInfo(){
	var cid=$("#cid").val();
	$.ajax({
		async:false,
		type:'get',
		url:"${ctx}/wms/ciqout/ifsave/"+cid,
		dataType:"text",
		success: function(data){
			if(data == "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存出库报检单！", position: "bottomRight"});
				return;
			}else{
				window.parent.mainpage.mainTabs.addModule('出库报检单（带货物信息）打印','wms/ciqout/printInfo/' + cid);
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