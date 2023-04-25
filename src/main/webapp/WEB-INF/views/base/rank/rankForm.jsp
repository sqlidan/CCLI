<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/base/rank/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>等级：</td>
				<td>
					<input id="rank" name="rank" class="easyui-validatebox" validType="rank[0,100]"  required="true"  value="${rank.rank }"> 
				</td>
			</tr>
			<tr> 
				<td>等级别名：</td>
				<td>
					<input id="nickName" name="nickName" class="easyui-validatebox"    value="${rank.nickName }"> 
				</td>
			</tr>
			<tr>
				<td>最小库存量（KG）：</td>
				<td>
					<input id="minNum" name="minNum" class="easyui-validatebox"   required="true" value="${rank.minNum}"> 
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td><textarea rows="3" cols="41" class="easyui-validatebox" data-options="validType:'length[0,50]'" name="remark" style="font-size: 12px;font-family: '微软雅黑'">${rank.remark}</textarea></td>
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
	    	rank: {  
		         validator: function(value,param){  
		             	 var flag=false;
		             	 var rules = $.fn.validatebox.defaults.rules; 
		             	 //更新时，值和原值一致返回true，否则进行校验
		             	 if("update"==action && value==$("#rank").val()){
		             		flag = true;
		             	 }else{
		             		//校验用户名是否唯一
			                 $.ajax({
			                         type: 'GET', 
			                         url: '${ctx}/base/rank/checkRank',
			                         data:'rank='+value,
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