<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body >
	<div  style="width: 100%;height: 100%">
			<form id="ctform" action="${ctx }/bis/truck/createct" method="post"> 
				<input type="hidden" name="ordId" value="${ordId }">
				<input type="hidden" id="ids" name="ids" >   
			</form>
	</div>
	
<script type="text/javascript">
 if(typeof(splitJson)!="undefined" && splitJson!=null ){
	 var inHtml;
	 $("#ids").val(tids);
	 for(var i=0;i<splitJson.length;i++){
		 inHtml="<div style=\"margin:3px 5px\" >"+splitJson[i].tcode+"托盘需要拆托，请输入新托盘号：<input type=\"text\" id=\"newTCode\" name=\"newTCode\" class=\"easyui-validatebox\" data-options=\"required:'required',validType:'length[0,20]'\" >"+
		 " 拆托数量"+splitJson[i].splitnum+		
		 "<input type=\"hidden\" name=\"tId\" value=\""+splitJson[i].id+"\"><input type=\"hidden\" name=\"spNum\" value=\""+splitJson[i].splitnum+"\"></div>";
		 $("#ctform").append(inHtml);
	 }
 }
 /**
  * 根据订单号判断订单类型 1货转，0出库订单
  */
 function getModle(){
 	var getOrdNum="${ordId }";
 	if(getOrdNum!=null && getOrdNum!=""){
 		if("T"==getOrdNum.substring(0,1)){
 			return 1;
 		}else{
 			return 0;
 		}
 	}
 	return 0;
 }
 
//提交表单
$('#ctform').form({    
    onSubmit: function(){
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){
    	var getJosn=eval("("+data+")");
    	var nmodel=getModle();
    	 if("success"==getJosn.endStr){
    		 dt.panel('close');
    		 if(1==nmodel){
    			 parent.$.easyui.messager.alert("货转成功");
    			 //货转成功后加载托盘货转明细
    			 intoTTaryTabl();
    			 //删除拆托按钮
    			 rmtga();
    		 }else{
    			
    		 	var getOrdNum2="${ordId }";
    		 	 $.ajax({
  					async:false,
					type : "POST",
					url : "${ctx}/bis/truck/remark/"+getOrdNum2+"/"+getJosn.tNum,
					data : "",
					dataType : "text",
					success : function(date) {
						
					} 
				}); 
    			 parent.$.easyui.messager.alert("生成装车单成功！装车单号："+getJosn.tNum);
        		 successTip("success",dg);
    		 }
    	 }else{
    		 if(1==nmodel){
    			 parent.$.easyui.messager.alert("货转失败，请重新选择分拣策略！");
    		 }else{
    			 parent.$.easyui.messager.alert("生成装车单失败，请重新选择分拣策略！");
    		 }
    	 }
    }    
});
</script>
</body>
</html>