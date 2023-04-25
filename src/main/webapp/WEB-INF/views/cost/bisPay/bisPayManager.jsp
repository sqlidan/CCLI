<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center'">
	<form id="downForm" method="post">
    </form>
	<form id="mainForm"   method="post">
	<div style="height:auto" class="datagrid-toolbar">
	  	<shiro:hasPermission name="wms:paymanager:save">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
		   	<span class="toolbar-item dialog-tool-separator"></span>
	   	</shiro:hasPermission>
	   	<shiro:hasPermission name="wms:paymanager:delete">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteIt()">删除</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	</div>
	<table class="formTable" >
		<tr>
			<td >申请人</td>
				<c:choose>
				<c:when test="${action == 'create'}">
				<td>
					<input id="askMan" name="askMan" class="easyui-validatebox" data-options="width:180"  value="${user}"  readonly/> 
				</td>
				</c:when>
				<c:otherwise>
				<td>
					<input id="askMan" name="askMan" class="easyui-validatebox" data-options="width:180"  value="${bisPay.askMan}" readonly /> 
				</td>
				</c:otherwise>
				</c:choose>
			<td>申请日期</td>
			<c:choose>
				<c:when test="${action == 'create'}">
				<td>
					<input type="hidden" id="payId" name="payId" value=""/>
					<input id="askDate" name="askDate" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/> 
				</td>
				</c:when>
				<c:otherwise>
				<td>
					<input type="hidden" id="payId" name="payId" value="${bisPay.payId}" />
					<input id="askDate" name="askDate" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${bisPay.askDate}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/> 
				</td>
				</c:otherwise>
			</c:choose>
			<td>财务确认人</td>
			<td>
				<input id="financeMan" name="financeMan" class="easyui-validatebox" data-options="width: 180" value="${bisPay.financeMan}"/>
			</td>
			<td>收款客户</td>
			<td>
				<input type="hidden" id="clientName" name="clientName" ></input>
				<select class="easyui-combobox" id="clientId" name="clientId" data-options="width:180, required:'required'" >
				</select>
			</td>
		</tr>
		<tr>
			<td>开户银行</td>
			<td>
				<input id="bank" name="bank" class="easyui-validatebox" data-options="width: 180" value="${bisPay.bank}"  readonly/> 
			</td>
			<td>账号</td>
			<td>
				<input id="account" name="account" class="easyui-validatebox"" data-options="width: 180" value="${bisPay.account}"  readonly/>
			</td>
			<td>发票到达日期</td>
			<td>
				<input id="receipt" name="receipt" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${bisPay.receipt}" pattern="yyyy-MM-dd HH:mm:ss" />" />
			</td>
			<td>支付方式</td>
			<td>
				<select id="payWay" name="payWay" class="easyui-combobox"  data-options="width: 180"  >
				</select>
			</td>
		</tr>
		<tr>
			
			<td>付款周期</td>
			<td>
				<select id="payCycle" name="payCycle" class="easyui-combobox"  data-options="width: 180"  >
				</select>
			</td>
			<td>合作类型</td>
			<td>
				<select id="teamType" name="teamType" class="easyui-combobox"  data-options="width: 180"  >
				</select>
			</td>
			<td>财务主管</td>
			<td>
				<input id="billManager" name="billManager" class="easyui-validatebox" data-options="width: 180" value="${bisPay.billManager}"/>
			</td>
			<td>批准人</td>
			<td>
				<input id="approver" name="approver" class="easyui-validatebox" data-options="width: 180" value="${bisPay.approver}"/>
			</td>
		</tr>
		<tr>
			<td>情况说明</td>
			<td colspan='3'>
				<input id="situation" name="situation" class="easyui-validatebox" data-options="width: 480" value="${bisPay.situation}"/>
			</td>
			<td>领款人</td>
			<td>
				<input id="drawMoney" name="drawMoney" class="easyui-validatebox" data-options="width: 180" value="${bisPay.drawMoney}"/>
			</td>
			<td>单据（张）</td>
			<td>
				<input id="piece" name="piece" class="easyui-validatebox" data-options="width: 180" value="${bisPay.piece}" onkeyup="ischeckNum(this)" />
			</td>
		</tr>
		<tr>
			<td>全额合计</td>
			<td>
				<input id="price" name="price" class="easyui-validatebox" data-options="width: 180" value="${bisPay.price}" readonly />
				元人民币
			</td>
			<td></td>
			<td></td>
			<td>当前状态</td>
			<td>
				<input id="state" name="state" class="easyui-validatebox"" data-options="width: 180" value="${bisPay.state}"  readonly/>
			</td>
		</tr>
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false" title="业务付款明细"  style="height:300px">
		<div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
			<div>
			<shiro:hasPermission name="wms:paymanager:addInfo">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:paymanager:updateInfo">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:paymanager:deleteInfo">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
	 			<span class="toolbar-item dialog-tool-separator"></span>
 			</shiro:hasPermission>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="down()">下载导入模板</a>
                <span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="into()">EXCEL导入</a>
                <span class="toolbar-item dialog-tool-separator"></span>
<!--			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="testa()">测试</a> -->
	    	</div>
		</div>
       <table id="dg" ></table> 
		<div id="dlg" ></div> 
</div>

<script type="text/javascript">
var dg;
var dlg;
var action = "${action}";
var state = "${bisPay.state}";
//a为保存时或联动时判断客户是否为下拉框选择的最终标示符，b为判断上次change是否为下拉框选择的标示符
var a;
var b=0;

$(function(){ 
 	var payId = $("#payId").val();
    if(payId == ""){
      $.ajax({
  		async:false,
		type : "POST",
		url : "${ctx}/cost/bispay/getNewPayId",
		data : "",
		dataType : "text",
		success : function(date) {
		  $("#payId").val(date);
		}
	  });
    }
	gridDG();
    selectAJAX();
    if(action == "create"){
    	a=0;
    }else{
    	a=1
    }
 
    if(state == "1"){
    	$("#state").val("已审核");
    }else{
    	$("#state").val("未审核");
    }
    
});

//清空
function clearIt(){
	window.parent.mainpage.mainTabs.addModule('业务付款单管理','cost/bispay/manager');
}

//查询业务付款单明细
function gridDG(){
    var payId = $("#payId").val();
	dg=$('#dg').datagrid({    
		method: "GET",
	    url:'${ctx}/cost/bispayinfo/json/'+payId, 
	    fit : false,
		fitColumns : true,
		border : false,
		striped:true,
		pagination:false,
		rownumbers:true,
		pageNumber:1,
		pageSize : 2000,
		pageList : [2000 ],
		singleSelect:false,
	    columns:[[    
			{field:'id',title:'id',sortable:true}, 
			{field:'payId',title:'payId',hidden:true}, 
	        {field:'checkSign',title:'是否审核',sortable:true,
	        	formatter : function(value, row, index) {
 	       			return value == "0" ? '否':'是';
 	        	}
 	        },    
	       // {field:'sellMan',title:'销售',sortable:true},
	        {field:'clientName',title:'客户名称',sortable:true},  
	        {field:'billNum',title:'提单号',sortable:true},
	        {field:'feeName',title:'费目名称',sortable:true},  
	        {field:'unitPrice',title:'单价',sortable:true},  
	        {field:'num',title:'数量',sortable:true},  
	        {field:'totelPrice',title:'金额',sortable:true},  
	        {field:'taxRate',title:'税率',sortable:true},  
	       // {field:'ctnAmount',title:'箱量',sortable:true},  
 	        {field:'helpPay',title:'是否垫付',sortable:true,
 	        	formatter : function(value, row, index) {
  	       			return value == "0" ? '否':'是';	
  	        	}
  	        },
  	      {field:'ifReverse',title:'是否冲账',sortable:true,
 	        	formatter : function(value, row, index) {
  	       			return value == "1" ? '是':'否';	
  	        	}
  	        },
	        {field:'shareState',title:'分配状态',sortable:true,
	        	formatter : function(value, row, index) {
 	       			if(value=="1"){
 	       				return "<font color='green'>分配成功</font>";
 	       			}else if(value=="2"){
 	       				return "<font color='red'>分配失败(应付费目已存在)</font>";
 	       			}else if(value=="3"){
 	       				return "<font color='red'>分配失败（费用已完成）</font>";
 	       			}
 	        	}
 	        },  
	        {field:'remark',title:'备注',sortable:true}
	    ]],
	    onLoadSuccess:function(data){
	    	 if(data.total !=0){
		    	 var allPrice = 0;
		    	 var rows = dg.datagrid("getRows");
				 for(var i=0;i<rows.length;i++){
					allPrice = Number(allPrice) + Number(rows[i].totelPrice);
				 }
				 $("#price").val(allPrice);
				 var priceT=$("#price").val();
    			 $("#price").val(Number(priceT).toFixed(2));
				 //异步更新主表合计金额
				 $.ajax({
	 	  			async: false,
	 	  			type: 'GET',
	 	  			url: "${ctx}/cost/bispay/setprice",
	 	  			data: {"payId":payId,"price":allPrice},
	 	  			dataType: "text",  
	 	  			success: function(msg){
	 	  				
	 	  			}
	 	  		});
 	  		}
	    },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false//,
	    //toolbar:'#tb2'
	});
} 


//保存
function submitForm(){
	$("#clientName").val($('#clientId').combobox("getText"));
	if($("#mainForm").form('validate')){
		if(a == 0){
			parent.$.messager.show({ title : "提示",msg: "收款客户请选择下拉框中的数据!", position: "bottomRight" });
			return;
		}else{
		//用ajax提交form
 			$.ajax({
 	  			async: false,
 	  			type: 'POST',
 	  			url: "${ctx}/cost/bispay/create",
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

//删除此业务付款单前先判断明细
function deleteIt(){
var payId = $("#payId").val();
	$.ajax({
		type:'get',
		url:"${ctx}/cost/bispay/ifsave/"+payId,
		success: function(data){
			if(data != "success"){
				parent.$.messager.show({title: "提示", msg: "未保存！", position: "bottomRight" });
				return;
			}else{
				deleteItGo(payId);
			}
		}
	})
}

//删除此业务付款单
function deleteItGo(payId){
		$.ajax({
				type: 'get',
				url: "${ctx}/cost/bispay/ifInfo/" + payId,
				success: function(data){
					if(data!="success"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "此业务付款单已有明细添加，无法删除！", position: "bottomRight"});
						return;
					}else{
						parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
							if(data){
								$.ajax({
									type: 'get',
									url: "${ctx}/cost/bispay/delete/" + payId,
									success: function(data){
										parent.$.easyui.messager.show({title: "操作提示", msg: "删除成功！", position: "bottomRight"});
										window.parent.mainpage.mainTabs.closeCurrentTab();//关闭TAB
									}
		  						});
							}
						})
					}
				}
		  	});
}

//增加明细时，先判断是否已保存
function addInfo(){
    if ($("#state").val() == "已审核") {
        parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
        return;
    }
	var payId = $("#payId").val();
	$.ajax({
		type:'get',
		url:"${ctx}/cost/bispay/ifsave/"+payId,
		success: function(data){
			if(data != "success"){
				parent.$.messager.show({title: "提示", msg: "未保存！", position: "bottomRight" });
				return;
			}else{
				addInfoT(payId);
			}
		}
	})
}

//业务付款明细弹窗增加
function addInfoT(payId) {
	dlg=$("#dlg").dialog({   
    	title: '新增业务付款单明细',    
	    width: 480,    
	    height: 480,    
	    href:'${ctx}/cost/bispayinfo/create/'+payId,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				var clientId=$("#clientIda").val();
				var stockId=$("#stockId").val();//付款单的结算单位
				var feeCode=$("#feeCode").combobox("getValue");
				$("#feeName").val($("#feeCode").combobox("getText"));
				$.ajax({
					type:'get',
					url:"${ctx}/cost/bispayinfo/checkbillnum",
					data : {
						"billNum":$("#billNum").searchbox("getValue"),
						"clientId":clientId,
						"feeCode":feeCode,
						"stockId":stockId
					},
					success: function(data){
						if(data == "success"){
							if($("#mainform2").form('validate')){
			  			   		$("#mainform2").submit(); 
			 			   		dlg.panel('close');
		 			   	    }
						}else{
						  parent.$.messager.show({title: "提示", msg:data, position: "bottomRight" });
						  return;
						}
					}
				});
				
			}
		},{
			text:'取消',
			handler:function(){
				dlg.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){gridDG()},300);
		}
	});
	
}

//修改业务付款单明细
function editInfo() {
	
	if ($("#state").val() == "已审核") {
        parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
        return;
    }
 	var row = dg.datagrid('getSelected');
	if(row== null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	if(row.checkSign == '1'){
		parent.$.messager.show({title: "提示", msg: "此明细已审核，无法进行修改！", position: "bottomRight" });
		return;
	}
	dlg=$("#dlg").dialog({   
    	title: '修改业务付款单明细',    
	    width: 480,    
	    height: 480,    
	    href:'${ctx}/cost/bispayinfo/update/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				var clientId=$("#clientIda").val();
				var feeCode=$("#feeCode").combobox("getValue");
				var stockId=$("#stockId").val();//付款单的结算单位
				$("#feeName").val($("#feeCode").combobox("getText"));
				$.ajax({
					type:'get',
					url:"${ctx}/cost/bispayinfo/checkbillnum",
					data : {
						"billNum":$("#billNum").searchbox("getValue"),
						"clientId":clientId,
						"feeCode":feeCode,
						"id":row.id,
						"stockId":stockId
					},
					success: function(data){
						if(data == "success"){
							if($("#mainform2").form('validate')){
			  			   		$("#mainform2").submit(); 
			 			   		dlg.panel('close');
		 			   	    }
						}else{
						  parent.$.messager.show({title: "提示", msg:data, position: "bottomRight" });
						  return;
						}
					}
				});
		
			}
		},{
			text:'取消',
			handler:function(){
				dlg.panel('close');
			}
		}],
		onClose: function (){
			dg.datagrid('clearSelections');
			window.setTimeout(function(){gridDG()},100);
		}
	});
}


//删除业务付款单明细
function delInfo(){
	if ($("#state").val() == "已审核") {
        parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
        return;
    }
	var row = dg.datagrid('getSelected');
	if(row== null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	if(row.checkSign == '1'){
		parent.$.messager.show({title: "提示", msg: "此明细已审核，无法删除！", position: "bottomRight" });
		return;
	}
	var rows = dg.datagrid('getSelections');
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].id);
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/cost/bispayinfo/delete/" + ids,
				success: function(data){
					dg.datagrid('clearSelections');
					successTip(data, dg);
				}
			});
		} 
	});
}


//下拉框
function selectAJAX(){
    //收款客户
 	var getclient='${bisPay.clientId}';
	$('#clientId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?setid=${bisPay.clientId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onChange:function(newVal){
	   	  if(b==1){
	   		a=1;
	   		b=0;
	   	   }else{
	   	   	a=0;
	   	   }
	   	   window.setTimeout(function(){getClientInfo()},1000);
	   },
	   onSelect:function(){
	   		a=1;
	   		b=1;
	   },
	   onLoadSuccess:function(){
			if(getclient!=null && getclient!=""){
				 $('#clientId').combobox("select",getclient);
				 $('#clientId').val(getclient);
				 getclient="";
			}
	   }
   	});
   	
//支付方式
   	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=payWay",
		dataType : "json",
		success : function(date) {
				$('#payWay').combobox({
					data : date.rows,
					value : '${bisPay.payWay}',
					valueField : 'value',
					textField : 'label',
					editable : false
				});
		}
	});   	
	
	//付款周期
   	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=payCycle",
		dataType : "json",
		success : function(date) {
				$('#payCycle').combobox({
					data : date.rows,
					value : '${bisPay.payCycle}',
					valueField : 'value',
					textField : 'label',
					editable : false
				});
		}
	});   	
	
	//合作类型
   	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=teamType",
		dataType : "json",
		success : function(date) {
				$('#teamType').combobox({
					data : date.rows,
					value : '${bisPay.teamType}',
					valueField : 'value',
					textField : 'label',
					editable : false
				});
		}
	});   	
}

//获取客户信息
function getClientInfo(){
	var clientId = $("#clientId").combobox("getValue");
	if(clientId != ""){
		if(a==1){
			$.ajax({
				type : "GET",
				async : false,
				url : "${ctx}/cost/bispay/getClientInfo/"+clientId,
				data : "",
				dataType : "json",
				success : function(date) {
					$("#bank").val(date[0]);
					$("#account").val(date[1]);
				}
			});  
		}
	} 	
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

//下载
    function down() {
	
    	if ($("#state").val() == "已审核") {
            parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
            return;
        }
        var url = "${ctx}/cost/bispayinfo/download";
        $("#downForm").attr("action", url).submit();
    }

    //导入
    function into() {
    	if ($("#state").val() == "已审核") {
            parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
            return;
        }
        var payId = $("#payId").val();
        $.ajax({
            type: 'get',
            url:"${ctx}/cost/bispay/ifsave/"+payId,
            dataType: "text",
            success: function (data) {
                if (data != "success") {
                    parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存业务付款单！", position: "bottomRight"});
                    return;
                } else {
                    intoc(payId);
                }
            }
        });
    }


	function intoc(payId) {
        d = $("#dlg").dialog({
            title: "业务付款单明细导入",
            width: 450,
            height: 450,
            href: '${ctx}/cost/bispayinfo/into/' + payId,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $("#mainform3").submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }

function testa(){
	
}
</script>
</body>
</html>