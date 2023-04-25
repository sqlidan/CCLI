<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tt" class="easyui-tabs">   
    <div title="动态" data-options="refreshable: false">   
        <div id="tb" style="padding:5px;height:auto">
        	<label>客户</label>
        	<input type="hidden" id="clientId" >
        	<select class="easyui-combobox" id="clientP" name="client" class="easyui-validatebox" data-options="width:180" >
        	</select>
			<label>SKU</label>
			<input type="hidden" id="sku">
			<input type="text" id="skuP" name="sku" class="easyui-validatebox" data-options="width:300"/>
			<label>仓库</label>
			<input type="hidden" id="warehouseId">
			<input type="hidden" id="warehouse">
        	<select id="warehouseIdP" name="warehouseId" class="easyui-combobox" data-options="width: 180"  >
			</select>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</div>
		<div>
		 <tr>
		 <td>现已质押</td>
		 <td><input id="piece" name="piece" value="" readonly/>件</td>
		 </tr>
		 <tr>
		 <input type="hidden" id="single" value=""/>
		 <td>总净重</td>
		 <td><input id="netWeight" name="netWeight" value="" readonly/>KG</td>
		 </tr>
		<tr>
		<td>撤押件数</td>
		<td><input id="zyPiece" name="zyPiece" class="easyui-validatebox" value="" onkeyup="pieced()"/></td>
		</tr>
		<tr>
		<td>撤押重量</td>
		<td><input id="zyWeight" name="zyWeight" class="easyui-validatebox" value="" type="background:#eee" readonly/></td>
		<td><input id="zy" name="zy" value="撤押" class="easyui-linkbutton" onclick="zy()" /> </td>
		</tr>
		</div>  
    </div>   
    <div title="静态" data-options="refreshable: false">   
    	 <div id="tb" style="padding:5px;height:auto">
			<label>提单号</label>
			<input type="hidden" id="billNum">
			<input type="text" id="billNumP" name="billNum" class="easyui-validatebox" data-options="width:180"/>
			<label>箱号</label>
			<input type="hidden" id="ctnNum">
			<input type="text" id="ctnNumP" name="ctnNum" class="easyui-validatebox" data-options="width:180"/>
			<label>客户</label>
			<input type="hidden" id="clientIdj">
        	<select class="easyui-combobox" id="clientjP" name="clientj" class="easyui-validatebox" data-options="width:180">
        	</select>
			<label>SKU</label>
			<input type="hidden" id="skuj">
			<input type="text" id="skujP" name="skuj" class="easyui-validatebox" data-options="width:180"/>
			<label>仓库</label>
			<input type="hidden" id="warehouseIdj">
			<input type="hidden" id="warehousej">
        	<select id="warehouseIdjP" name="warehouseId" class="easyui-combobox" data-options="width: 180"  >
			</select>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cxj()">查询</a>
		</div>
		<div>
		 <tr>
		 <td>现已质押</td>
		 <td><input id="piecej" name="piecej" value="" readonly />件</td>
		 </tr>
		 <tr>
		 <input type="hidden" id="singlej" value=""/>
		 <td>总净重</td>
		 <td><input id="netWeightj" name="netWeightj" value="" readonly />KG</td>
		 </tr>
		 <tr>
		 <td>撤押件数</td>
		 <td><input id="zyPiecej" name="zyPiecej" class="easyui-validatebox" value="" onkeyup="piecedj()"/></td>
		 </tr>
		 <tr>
		 <td>撤押重量</td>
		 <td><input id="zyWeightj" name="zyWeightj" class="easyui-validatebox" value="" type="background:#eee" readonly></td>
		 <td><input id="zy" name="zy" value="撤押" class="easyui-linkbutton" onclick="zyj()" /></td>
		 </tr>
		</div>  
        <table id="dg_stay"></table>    
    </div>   
</div>  

<div id="dlg"></div>  

<script type="text/javascript">
var dg;
var dg_running;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){   
	$('#clientP').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
	   valueField: 'ids', 
	   textField: 'clientName',
	   mode:'remote'
   	});
   	
   	$('#clientjP').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
	   valueField: 'ids', 
	   textField: 'clientName',
	   mode:'remote'
   	});
   	
   	 //仓库
	 $.ajax({
	   type: "GET",
	   async : false,
	   url: "${ctx}/base/warehouse/getWarehouse",
	   data: "",
	   dataType: "json",
	   success: function(date){
			   $('#warehouseIdP').combobox({
				   data: date,
				   valueField: 'id',
				   textField: 'warehouseName',
				   editable : false
			   });
	   }
	});
	
	//仓库(静态质押的)
	 $.ajax({
	   type: "GET",
	   async : false,
	   url: "${ctx}/base/warehouse/getWarehouse",
	   data: "",
	   dataType: "json",
	   success: function(date){
			   $('#warehouseIdjP').combobox({
				   data: date,
				   valueField: 'id',
				   textField: 'warehouseName',
				   editable : false
			   });
	   }
	});
});

//动态撤押查询
function cx(){
	var sku = $("#skuP").val();
	var clientId = $('#clientP').combobox('getValue');
	var warehouseId=$('#warehouseIdP').combobox('getValue');
	if(sku == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入SKU！", position: "bottomRight" });
		return;
	}
	if(clientId == ""){
		parent.$.messager.show({ title : "提示",msg: "请选择客户！", position: "bottomRight" });
		return;
	}
	if(warehouseId == ""){
		parent.$.messager.show({ title : "提示",msg: "请选择仓库！", position: "bottomRight" });
		return;
	}
    $.ajax({
      type: 'POST',
      url: "${ctx}/wms/pledge/gettraybyskuc",
      data:{"sku":sku,"clientId":clientId,"warehouseId":warehouseId},
      dataType:'text',
      success: function(msg){
        var tt = new Array();
        tt = msg.split(",");
        $("#piece").val(tt[0]);
        $("#netWeight").val(tt[1]);
        $("#single").val(tt[2]);
        $("#sku").val(sku);
        $("#clientId").val(clientId);
        $("#warehouseId").val(warehouseId);
        $("#warehouse").val($('#warehouseIdP').combobox('getText'));
      }
   })
}


function pieced(){
	var num = $("#zyPiece").val();
	if (num) {
		if (!isNaN(num)) {
		  var single = $("#single").val();
 		  var zyWeight = single*num;
 		  $("#zyWeight").val(zyWeight);
		} else {
			alert('请输入数字！');
			$("#zyPiece").val("");
			myfm.isnum.select();
			return false;
		}
	}
	
}

//动态撤押
function zy(){
	var piece = $("#piece").val();
	var zyPiece = $("#zyPiece").val();
	var sku = $("#sku").val();
	var clientId = $('#clientId').val();
	var warehouseId=$('#warehouseId').val();
	var warehouse = $('#warehouse').val();
	if(sku == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入SKU！", position: "bottomRight" });
		return;
	}
	if(clientId == ""){
		parent.$.messager.show({ title : "提示",msg: "请选择客户！", position: "bottomRight" });
		return;
	}
	if(zyPiece == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入撤押数量！", position: "bottomRight" });
		return;
	}
	if(warehouseId == ""){
		parent.$.messager.show({ title : "提示",msg: "请选择仓库！", position: "bottomRight" });
		return;
	}
	if(parseInt(zyPiece) > parseInt(piece)){
	parent.$.messager.show({ title : "提示",msg: "撤押数量不得大于已质押数量！", position: "bottomRight" });
	return;
	}
	//后台校验件数
	$.ajax({
	  async: false,
	  type: 'POST',
	  url:"${ctx}/wms/pledge/ajaxcd",
	  data:{"sku":sku,"clientId":clientId,"piece":piece,"warehouseId":warehouseId},
	  dataType: 'text',
	  success: function(msg){
	    if(msg != "success"){
	    	parent.$.messager.show({ title : "提示",msg: "因质押数量发生变化，撤押失败！请重新查询后再尝试撤押！", position: "bottomRight" });
	    	return;
	    }else{
	    	//后台校验成功则进行撤押操作
	    	$.ajax({
	  			async: false,
      			type: 'POST',
      			url: "${ctx}/wms/pledge/zyc",
      			data:{"zyPiece":zyPiece,"sku":sku,"clientId":clientId,"warehouseId":warehouseId,"warehouse":warehouse},
      			dataType:'text',
      			success: function(msg){
      				parent.$.messager.show({ title : "提示",msg: "撤押成功!", position: "bottomRight" });
      				$("#piece").val(parseInt(piece) - parseInt(zyPiece));
      				return;
      			}
  			 })
	    }
	  }
	})
}

//静态撤押查询
function cxj(){
	var sku = $("#skujP").val();
	var clientId = $('#clientjP').combobox('getValue');
	var billNum = $('#billNumP').val();
	var ctnNum = $('#ctnNumP').val();
	var warehouseId=$('#warehouseIdjP').combobox('getValue');
	if(sku == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入SKU！", position: "bottomRight" });
		return;
	}
	if(clientId == ""){
		parent.$.messager.show({ title : "提示",msg: "请选择客户！", position: "bottomRight" });
		return;
	}
	if(billNum == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入提单号！", position: "bottomRight" });
		return;
	}
	if(ctnNum == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入箱号！", position: "bottomRight" });
		return;
	}
	if(warehouseId == ""){
		parent.$.messager.show({ title : "提示",msg: "请选择仓库！", position: "bottomRight" });
		return;
	}
    $.ajax({
      type: 'POST',
      url: "${ctx}/wms/pledge/gettraybystayc",
      data:{"sku":sku,"clientId":clientId,"billNum":billNum,"ctnNum":ctnNum,"warehouseId":warehouseId},
      dataType:'text',
      success: function(msg){
        var tt = new Array();
        tt = msg.split(",");
        $("#piecej").val(tt[0]);
        $("#netWeightj").val(tt[1]);
        $("#singlej").val(tt[2]);
        $("#billNum").val(billNum); 
        $("#ctnNum").val(ctnNum);
        $("#skuj").val(sku);
        $("#clientIdj").val(clientId);
        $("#warehouseIdj").val(warehouseId);
        $("#warehousej").val($('#warehouseIdjP').combobox('getText'));
      }
   })
}


function piecedj(){
	var numj = $("#zyPiecej").val();
	if (numj) {
		if (!isNaN(numj)) {
		  var singlej = $("#singlej").val();
 		  var zyWeightj = singlej*numj;
 		  $("#zyWeightj").val(zyWeightj);
		} else {
			alert('请输入数字！');
			$("#zyPiecej").val("");
			myfm.isnum.select();
			return false;
		}
	}
	
}


//静态撤押
function zyj(){
	var piece = $("#piecej").val();
	var zyPiece = $("#zyPiecej").val();
	var sku = $("#skuj").val();
	var clientId = $('#clientIdj').val();
	var billNum = $('#billNum').val();
	var ctnNum = $('#ctnNum').val();
	var warehouseId=$('#warehouseIdj').val();
	var warehouse=$('#warehousej').val();
	if(sku == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入SKU！", position: "bottomRight" });
		return;
	}
	if(clientId == ""){
		parent.$.messager.show({ title : "提示",msg: "请选择客户！", position: "bottomRight" });
		return;
	}
	if(zyPiece == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入撤押数量！", position: "bottomRight" });
		return;
	}
	if(billNum == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入提单号！", position: "bottomRight" });
		return;
	}
	if(ctnNum == ""){
		parent.$.messager.show({ title : "提示",msg: "请输入箱号！", position: "bottomRight" });
		return;
	}
	if(warehouseId == ""){
		parent.$.messager.show({ title : "提示",msg: "请选择仓库！", position: "bottomRight" });
		return;
	}
	if(parseInt(zyPiece) > parseInt(piece)){
	parent.$.messager.show({ title : "提示",msg: "撤押数量不得大于质押数量！", position: "bottomRight" });
	return;
	}
	
	//后台校验件数
	$.ajax({
	  async: false,
	  type: 'POST',
	  url:"${ctx}/wms/pledge/ajaxcj",
	  data:{"sku":sku,"clientId":clientId,"billNum":billNum,"ctnNum":ctnNum,"piece":piece,"warehouseId":warehouseId},
	  dataType: 'text',
	  success: function(msg){
	    if(msg != "success"){
	    	parent.$.messager.show({ title : "提示",msg: "因质押数量发生变化，撤押失败！请重新查询后再尝试撤押！", position: "bottomRight" });
	    	return;
	    }else{
	    	//后台校验成功则进行撤押操作
	    	$.ajax({
	  			async: false,
      			type: 'POST',
      			url: "${ctx}/wms/pledge/zyjc",
      			data:{"zyPiece":zyPiece,"sku":sku,"clientId":clientId,"billNum":billNum,"ctnNum":ctnNum,"warehouseId":warehouseId,"warehouse":warehouse},
      			dataType:'text',
      			success: function(msg){
					parent.$.messager.show({ title : "提示",msg: "撤押成功!", position: "bottomRight" });
					$("#piecej").val(parseInt(piece) - parseInt(zyPiece));
					return;
      			}
  			})
	    }
	  }
	})
	
}


</script>
</body>
</html>