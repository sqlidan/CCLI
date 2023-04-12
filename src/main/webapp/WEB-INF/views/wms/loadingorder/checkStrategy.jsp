<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body >
		<div style="margin-left: 5px;margin-top:10px;">
			<form id="clform" action="${ctx }/bis/truck/${action}" method="post">   
				<div id="orddiv" style="margin-bottom: 5px;display: none;">
					<input type="text"  name="ordid"  class="easyui-searchbox"    data-options="prompt:'请选择出库订单',required:'required',width:150,searcher:doSearchOrder,validType:'length[4,20]'" value="${ordId}" >
				</div>
				<div>
					<input id="cl" name="clcode" value="">
					<shiro:hasPermission name="base:stategy:add">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addcl()">添加策略</a>
					</shiro:hasPermission>
				</div>
			</form>
			<div id="hiddiv" style="display: none;">
			</div>
		</div>
 
	
<script type="text/javascript">
var retList=${retList};

$(document).ready(function(){
	$('#cl').combobox({
		   data : retList,
		   valueField:'STRATEGYCODE',
		   textField:'NAME',
		   value:0
	});
	if("00000000"==$("#ordid").val()){
		
		 
	}
	 
}); 
//提交表单
function addcl(){
	 var href='base/stategy/create';
	 window.parent.mainpage.mainTabs.addModule('拣货策略',href,'icon-hamburg-customers');
	 d.panel('close');
}
$('#clform').form({    
    onSubmit: function(){
		return true;	// 返回false终止表单提交
    },    
    success:function(data){
    	var getJosn=eval("("+data+")");
    	 if("success"==getJosn.endStr){
    		 splitJson=getJosn.splitTray;
    		 d.panel('close');
    		 console.log("1");
    		 if(splitJson.length>0){
    			 tids=getJosn.trayIds;
    			 openSplitT('${ordId}');
    		 } 
    	 }else  if("error"==getJosn.endStr){
    		 parent.$.easyui.messager.alert("提单号："+getJosn.billNum +" 箱号："+getJosn.ctnNum+" SKU:"+getJosn.skuNum+" 入库类型："+("0"==getJosn.state?"成品":"货损")+" 库存不足无法生成装车单！");
    	 }else{
    		 parent.$.easyui.messager.alert("分拣失败，请重新选择分拣策略分拣！");
    	 }
    }    
});
function doSearchOrder(){
	
}
</script>
</body>
</html>