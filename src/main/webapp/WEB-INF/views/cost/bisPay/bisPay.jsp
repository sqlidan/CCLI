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
			<span class="toolbar-item dialog-tool-separator"></span>
      	    <select id="clientName" name="filter_LIKES_clientName" class="easyui-combobox" data-options="width:150,prompt: '收款客户'" >
      	    </select>
      	    <span class="toolbar-item dialog-tool-separator"></span>
      	    <input type="text" name="filter_LIKES_askMan" class="easyui-validatebox" data-options="width:150,prompt: '申请人'"/>
      	    <span class="toolbar-item dialog-tool-separator"></span>
      	    <input type="text" name="filter_INAS_payId" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
      	    <span class="toolbar-item dialog-tool-separator"></span>
      	    <input type="text" name="filter_GEN_price" class="easyui-validatebox" data-options="width:150,prompt: '付款金额'"/>
      	  -  <input type="text" name="filter_LEN_price" class="easyui-validatebox" data-options="width:150,prompt: '付款金额'"/>
      	    <span class="toolbar-item dialog-tool-separator"></span>
      	    <input type="text" name="filter_LIKES_payId" class="easyui-validatebox" data-options="width:150,prompt: '业务单号'"/>
      	    <span class="toolbar-item dialog-tool-separator"></span>
	        <input type="text" name="filter_GED_askDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '申请开始日期'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <input type="text" name="filter_LED_askDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '申请结束日期'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<shiro:hasPermission name="cost:bisPay:add">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('业务付款单管理','cost/bispay/manager')">添加</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="cost:bisPay:update">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
	      	<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="cost:bisPay:delete">
      		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="cost:bisPay:print">
	      	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印业务付款单</a>
	       	<span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="wms:bisPay:check">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
               onclick="check()">审核</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:bisPay:unCheck">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-delete" plain="true"
               onclick="unCheck()">取消审核</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>

		<%--应收对账单上传  later--%>
		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="uploadFileToAline()">上传生成结算单</a>
		<%--应收对账单上传  later--%>
		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="uploadFileToNotAline()">上传不生成结算单</a>
		<%--应收对账单撤回  later--%>
		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="returnUpload()">撤回</a>
	</div>
</div>

<table id="dg"></table>

<script type="text/javascript">
var dg;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

$(function(){
	//客户
	   $('#clientName').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});

	dg=$('#dg').datagrid({
		method: "get",
	    url:'${ctx}/cost/bispay/json',
	    fit : true,
		fitColumns : true,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[
	        {field:'payId',title:'业务单号',sortable:true},
	        {field:'askMan',title:'申请人',sortable:true},
 	        {field:'clientName',title:'收款客户',sortable:true},
// 	        {field:'account',title:'业务单号',sortable:true},
 	        {field:'askDate',title:'申请日期',sortable:true},
 	        {field:'state',title:'审核状态',sortable:true,
  	            formatter : function(value, row, index) {
           			 if(value=="0"){
          			   return '未审核';
          			 }
          			 else if(value=="1"){
          			   return '已审核';
          			 }
   	        	 }
   	         },
 	        {field:'payWay',title:'支付方式',sortable:true,
 	           formatter : function(value, row, index) {
         			if(value=="1"){
         			  return '电汇';
         			}
         			else if(value=="2"){
         			 return '支票';
         			}
         			else if(value=="3"){
         			 return '现金';
         			}
         			else if(value=="4"){
         			 return '其他';
         			}
  	        	}
  	        },
 	        {field:'payCycle',title:'付款周期',sortable:true,
 	           formatter : function(value, row, index) {
         			if(value=="1"){
         			  return '当日付款';
         			}
         			else if(value=="2"){
         			 return '2日付款';
         			}
         			else if(value=="3"){
         			 return '3日付款';
         			}
         			else if(value=="4"){
         			 return '一周付款';
         			}
         			else if(value=="5"){
         			 return '合同';
         			}
  	        	}
  	        },
 	        {field:'teamType',title:'合作类型',sortable:true,
 	           formatter : function(value, row, index) {
         			if(value=="1"){
         			  return '合同收费';
         			}
         			else if(value=="2"){
         			 return '公司确认';
         			}
         			else if(value=="3"){
         			 return '其他';
         			}
  	        	}
  	        },
 	        {field:'price',title:'金额合计',sortable:true,
				    formatter:function(val,rowData,rowIndex){
				        if(val!=null){
				            return val.toFixed(2);
				        }
				   }
			},
 	        {field:'drawMoney',title:'领款人',sortable:true},
 	        {field:'financeMan',title:'财务确认人',sortable:true},
 	        {field:'billManager',title:'财务主管',sortable:true},
 	        {field:'approver',title:'批准人',sortable:true},
			{field:'midGroupStatic',title:'上传状态',sortable:false,width: 50,
				formatter:function (value,row,index){
					if(value == null || value == '未上传'){
						return '未上传';
					}else{
						return '已上传';
					}
				}}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
});

//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	$.ajax({
		async:false ,
		type:'get',
		url:"${ctx}/cost/bispay/ifInfo/"+row.payId,
		success: function(data){
			if(data != "success"){
				parent.$.messager.show({title: "提示", msg: "此业务付款单已有明细添加，无法删除！", position: "bottomRight" });
			}else{
				parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
				if (data){
					$.ajax({
						type:'get',
						url:"${ctx}/cost/bispay/delete/"+row.payId,
						success: function(data){
							successTip(data,dg);
						}
					});
				}
				});
			}
		}
	});

}



//创建查询对象并查询
function cx(){
	var obj=$("#searchFrom").serializeObject();
	dg.datagrid('load',obj);
}

//修改
function update(){
	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
    window.parent.mainpage.mainTabs.addModule('业务付款单修改','cost/bispay/update/' + row.payId);
}

//打印
function print(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	window.parent.mainpage.mainTabs.addModule('业务付款单打印','cost/bispay/print/' + row.payId);
}

//审核
function check() {
    var row = dg.datagrid('getSelected');
    if (rowIsNull(row)) return;
    if (row.state != "0") {
        parent.$.messager.show({title: "提示", msg: "只能审核状态为未审核的数据！", position: "bottomRight"});
        return;
    }
    parent.$.messager.confirm('提示', '您确定审核此业务付款单？', function (data) {
        if (data) {
            $.ajax({
                type: 'get',
                url: "${ctx}/cost/bispay/passBisPay/" + row.payId,
                success: function (data) {
                	if(data=="success"){
                		successTip(data, dg);
                        parent.$.messager.show({title: "提示", msg: "审核成功！", position: "bottomRight"});
                	}else{
                       parent.$.messager.show({title: "提示", msg:data, position: "bottomRight"});
                    }
                }
            });
        }
    });
}

//取消审核
function unCheck() {
    var row = dg.datagrid('getSelected');
    if (rowIsNull(row)) return;
    if (row.state != "1") {
        parent.$.messager.show({title: "提示", msg: "只能取消状态为已审核的数据！", position: "bottomRight"});
        return;
    }

    parent.$.messager.confirm('提示', '您确定取消此业务付款单的审核？', function (data) {
        if (data) {
            $.ajax({
                type: 'get',
                url: "${ctx}/cost/bispay/unPassBisPay/" + row.payId,
                success: function (data) {
                    if("success"==data){
                    	successTip(data, dg);
                        parent.$.messager.show({title: "提示", msg: "取消审核成功！", position: "bottomRight"});
                     }else{
                        parent.$.messager.show({title: "提示", msg:data, position: "bottomRight"});
                     }
                }
            });
        }
    });
}



/*
* 上传不生成 结算单
* */
function uploadFileToNotAline() {
	var rows = dg.datagrid('getSelections');
	if(rowIsNull(rows)) return;
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].payId);
	}
	console.log(ids)
	var  flag;
	parent.$.messager.confirm('提示', '是否要导出数据到中台', function (data) {
		if (data){
			$.ajax({
				type:'post',
				url:"${ctx}/cost/bispaymid/uploadFileToNotAline/"+ids,
				success: function(data){
					if("success"==data){
						console.log("应付返回信息为:"+data)
						successTip(data, dg);
						parent.$.messager.show({title: "提示", msg: "上传成功！", position: "bottomRight"});
						cx()
					}else{
						alert(data)
						parent.$.messager.show({title: "提示", msg:data, position: "bottomRight"});
						cx()
					}
				}
			});
		}
	});
}

/*
* 上传生成结算单
* */
function uploadFileToAline() {
	var rows = dg.datagrid('getSelections');
	if(rowIsNull(rows)) return;
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].payId);
	}
	console.log(ids)
	var flag;
	parent.$.messager.confirm('提示', '导出数据到中台是否要生成结算单', function (data) {
		if (data == true){
			$.ajax({
				type:'post',
				url:"${ctx}/cost/bispaymid/uploadFileToAline/"+ids,
				data:{true:flag},
				success: function(data){
					if("success"==data){
						console.log("应付返回信息为:"+data)
						successTip(data, dg);
						parent.$.messager.show({title: "提示", msg: "上传成功！", position: "bottomRight"});
						cx()
					}else{
						alert(data)
						parent.$.messager.show({title: "提示", msg:data, position: "bottomRight"});
						cx()
					}
				}
			});
		}
	});
}

/*
* 上传到中台的数据 撤回
* */
function returnUpload(){
	var rows = dg.datagrid('getSelections');
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].payId);
	}
	console.log(ids)
	parent.$.messager.confirm('提示', '是否要从中台数据中撤回选中数据', function (data) {
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/cost/bispaymid/returnUpload/"+ids,
				success: function(data){
					if("success"==data){
						alert(data)
						successTip(data, dg);
						parent.$.messager.show({title: "提示", msg: "撤回成功！", position: "bottomRight"});
						cx()
					}else{
						alert(data)
						parent.$.messager.show({title: "提示", msg:data, position: "bottomRight"});
						cx()
					}
				}
			});
		}
	});
}

</script>
</body>
</html>
