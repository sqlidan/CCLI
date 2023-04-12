<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/base/platform/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>月台口：</td>
				<td>
					<input id="platform" name="platform" class="easyui-validatebox" validType="platform[0,100]"   required="true"  value="${platform.platform}"> 
				</td>
				<td>
					<input id="id" name="id" class="easyui-validatebox"  type="hidden"   value="${platform.id}"> 
				</td>
			</tr>
		
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";
$(document).ready(function(){
	var messagestr="请修改输入值";
	//自定义validatebox的验证方法
    $.extend($.fn.validatebox.defaults.rules, {  
	    	platform: {  
		         validator: function(value,param){  
		             	 var flag=false;
		             	 var rules = $.fn.validatebox.defaults.rules; 
		             	 //更新时，值和原值一致返回true，否则进行校验
		             	 if("update"==action && value==$("#platform").val()){
		             		flag = true;
		             	 }else{
		             		//校验用户名是否唯一
			                 $.ajax({
			                         type: 'GET', 
			                         url: '${ctx}/base/platform/checkPlatform',
			                         data:'platform='+value,
			                         dataType:'text',
			                         async:false,
			                         success: function(data) {
			                             if(data=='true') {
			                                 flag = true;
			                             }else{
		                            		 flag =  false;
			                             }
			                         }
			                  });
		             	 }//end if
		             	
		             	if(!rules.length.validator(value,param)){ 
		                    rules.rangelength.message =  "请输入一个长度介于 {0} 和 {1} 之间的字符串"; 
		                    return false; 
		                } 
		             	 return flag;
		         },
		        message: '输入的客户名已存在！' 
	     	} 
	 });  
});
//提交表单
$('#mainform').form({    
    onSubmit: function(){ 
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){   
    	successTip(data,dg,d);
    }    
});    
</script>
</body>
</html>