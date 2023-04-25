<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<style type="text/css">
.titdiv{
font-weight:700;padding-left: 10px;height: 35px;padding-top: 5px;
}
#mainform .panel{
	margin-left: 3px;
}
#plist{
margin-left: 1px;
}
#pone div:first-line{
	font-weight:700;
}
#pone div div{
	float: left;width: 150px;text-align: center;
}	
#ptwo div:first-line{
	font-weight:700;
}
#ptwo div div{
	float: left;width: 150px;text-align: center;
}
.radioSpan {
      position: relative;
      border: 1px solid #95B8E7;
      background-color: #fff;
      vertical-align: middle;
      display: inline-block;
      overflow: hidden;
      white-space: nowrap;
      margin: 0;
      padding: 0;
      -moz-border-radius: 5px 5px 5px 5px;
      -webkit-border-radius: 5px 5px 5px 5px;
      border-radius: 5px 5px 5px 5px;
      display:block;
      font-size:14px;
    }
 .radioSpan input{
 	height: 20px;
 	width: 14px;
 } 	
</style>
</head>
<body>
<div  class="easyui-layout" data-options="fit:true">
	<div class="datagrid-toolbar" data-options="region:'north',split:false,border:false" style="height:auto">
		<shiro:hasPermission name="base:stategy:save">
       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="save();">保存</a>
       		<span class="toolbar-item dialog-tool-separator"></span>
       	</shiro:hasPermission>
        <shiro:hasPermission name="base:stategy:delete">
           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
      		<span class="toolbar-item dialog-tool-separator"></span>
       </shiro:hasPermission>
       <shiro:hasPermission name="base:stategy:add">
           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" data-options="disabled:false" onclick="newAdd()">新建</a>
      		<span class="toolbar-item dialog-tool-separator"></span>
       </shiro:hasPermission>
	</div>
	<div id="plist" data-options="region:'west',split:false,border:false" style="width:200px;margin-right: 10px;">
		<div style="width:180px;height: 100%">
			<div id="cllist" class="easyui-panel" title="策略列表"  data-options="fit:true,border:false" >
				<c:forEach items="${list}" var="item">
						<span class="radioSpan">
						<input  id="${item.STRATEGYCODE}" name="ifWrap" type="radio"  onclick="onCheck(this.value)"  value="${item.STRATEGYCODE}">
						 <lab>${item.NAME}</lab>
						</span>
		  		</c:forEach>
	  		</div> 
		</div>
	</div>
	<div data-options="region:'center',split:false,border:false"  >
		<form id="mainform" action="${ctx }/base/stategy/${action}" method="post">
			  <div class="titdiv">
			  	<input type="hidden" id="strategyCode" name="strategyCode">
			  	策略名称：<input type="text" id="strategyName" name="strategyName" class="easyui-validatebox" data-options="width:150,required:'required',validType:'length[1,25]'" style="height:22px;" />
			  </div>
			  <div id="pone" class="easyui-panel" title="条件"  data-options="" style="height:280px;">
			  		<div>
			  			<div>属性</div>
			  			<div>表达式</div>
			  			<div>值</div>
			  			<div>条件连接</div>
			  			<div>操作</div>
			  		</div>
			  		<div style="clear:both;height:5px"></div>
			  		<div id="inhtmlo">
			  			<div>
				  			<input id="ifsx" name="ifsx" value="">
			  			</div>
			  			<div>
			  				<input id="ifbds" name="ifbds" value="">
			  			</div>
			  			<div>
			  				<input type="text" id="ifval" name="ifval" class="easyui-validatebox" style="height:22px;" />
			  			</div>
			  			<div>
							<input id="iflink" name="iflink" value="">  			
			  			</div>
			  			<div id="inhtmlif">
			  				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"  title="添加" onclick="addIf();"></a>
			  				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" title="删除"   onclick="delIf(this)"></a>
			  			</div>
			  		</div>
			  </div>
			  <div style="height:10px;"></div>
			  <div id="ptwo" class="easyui-panel" title="排序"  data-options="" style="height:120px;">
			  		<div>
			  			<div>属性</div>
			  			<div>排序</div>
			  			<div>操作</div>
			  		</div>
			  		<div style="clear:both;height:5px"></div>
			  		<div id="inhtmlot">
			  			<div>
				  			<input id="ordsx" name="ordsx" value="">
			  			</div>
			  			<div>
			  				 <input id="ordpx" name="ordpx" value="">
			  			</div>
			  			<div id="inhtmlord">
			  				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"  title="添加" onclick="addOrder();"></a>
			  				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" title="删除"   onclick="delIf(this)"></a>
			  			</div>
			  		</div>
			  </div> 
		</form>
	</div>
</div>

<script type="text/javascript">
var strategy01,strategy02,strategy03,strategy04;
$(document).ready(function(){
	$.ajax({
		   type: "GET",
		   url: "${ctx}/system/dict/json",
		   data: "filter_LIKES_type=strategy01",
		   dataType: "json",
		   success: function(date){
			   strategy01=date;
			   if(date!=null && date.rows!=null && date.rows.length>0){
				   intoSelBox("ifsx",date,"");
				   intoSelBox("ordsx",date,"");
			   }
		   }
	});
	$.ajax({
		   type: "GET",
		   url: "${ctx}/system/dict/json",
		   data: "filter_LIKES_type=strategy02",
		   dataType: "json",
		   success: function(date){
			   strategy02=date;
			   if(date!=null && date.rows!=null && date.rows.length>0){
				   intoSelBox("ifbds",date,"");
			   }
		   }
	});
	$.ajax({
		   type: "GET",
		   url: "${ctx}/system/dict/json",
		   data: "filter_LIKES_type=strategy03",
		   dataType: "json",
		   success: function(date){
			   strategy03=date;
			   if(date!=null && date.rows!=null && date.rows.length>0){
				   intoSelBox("iflink",date,"and"); 
			   }
		   }
	});
	$.ajax({
		   type: "GET",
		   url: "${ctx}/system/dict/json",
		   data: "filter_LIKES_type=strategy04",
		   dataType: "json",
		   success: function(date){
			   strategy04=date;
			   if(date!=null && date.rows!=null && date.rows.length>0){
				   intoSelBox("ordpx",date,""); 
			   }
		   }
	});
	//表单提交校验
	$('#mainform').form({    
	    onSubmit: function(){
	    	//var isValid = $(this).form('validate');
			return true;	// 返回false终止表单提交
	    },    
	    success:function(data){
	    	 if(data!=null){
	    		 data=eval("("+data+")");
	    		 if("success"==data.retStr){
	    			 //如果是新添加，列表处追加记录
	    			 if(""==$("#strategyCode").val()){
	    				 $("#strategyCode").val(data.id);
	    				 $("#cllist").append("<span class=\"radioSpan\"><input id=\""+data.id+"\" name=\"ifWrap\" type=\"radio\" checked=\"checked\"  onclick=\"onCheck(this.value)\"  value=\""+data.id+"\"><lab>"+$("#strategyName").val()+"</lab></span>");
	    			 }else{
	    				 var getObj= $("#"+data.id);
	    				 getObj.next().remove();
	    				 getObj.parent().append("<lab>"+$("#strategyName").val()+"</lab>");
	    			 }
	    			 parent.$.easyui.messager.show({title: "操作提示", msg: "保存成功！", position: "bottomRight"});
	    		 }else{
	    			 parent.$.easyui.messager.alert("保存失败！"); 
	    		 }
	    	 }else{
	    		 parent.$.easyui.messager.alert("保存失败！");
	    	 }
	    }    
	});
});

/**
 * 加载下列列表
 *id 下拉控件主键id
 *date 下拉填充值 json
 *val 下列选中值
 */
function  intoSelBox(id,date,val){
	$('#'+id).combobox({
		   data : date.rows,
		   valueField:'value',
		   textField:'label',
		   value:val
   });
}
var num=1;
//条件添加行
function addIf(ifsx,ifbds,ifval,iflink){
	if(typeof(ifval)=="undefined" || ifval==null){
		ifval="";
	}
	$("#pone").append("<div style=\"clear:both;height:5px\"></div>");
	var inButton=$("#inhtmlif").html();
	$("#pone").append("<div class=\"ifdiv\"><div><input id=\"ifsx"+num+"\"  name=\"ifsx\" value=\"\"/></div>"+
						"<div><input id=\"ifbds"+num+"\"  name=\"ifbds\" value=\"\"/></div>"+
						"<div><input type=\"text\" name=\"ifval\" class=\"easyui-validatebox validatebox-text validatebox-f textbox\" value=\""+ifval+"\" style=\"height:22px;\" data-options=\"width:150\"></div>"+
						"<div><input id=\"iflink"+num+"\"  name=\"iflink\" value=\"\"/></div>"+
						"<div>"+inButton+"</div>"+
						"</div>");
	if(typeof(ifsx)=="undefined" || ifsx==null){
		ifsx="";
	}
	intoSelBox("ifsx"+num,strategy01,ifsx);
	if(typeof(ifbds)=="undefined" || ifbds==null){
		ifbds="";
	}
	intoSelBox("ifbds"+num,strategy02,ifbds);
	if(typeof(iflink)=="undefined" || iflink==null){
		iflink="and";
	}
	intoSelBox("iflink"+num,strategy03,iflink);
	num++ ;	
}
//排序添加行
function addOrder(ordsx,ordpx){
	$("#ptwo").append("<div style=\"clear:both;height:5px\"></div>");
	var inButton=$("#inhtmlord").html();
	$("#ptwo").append("<div class=\"orddiv\"><div><input id=\"ordsx"+num+"\"  name=\"ordsx\" value=\"\"/></div>"+
						"<div><input id=\"ordpx"+num+"\"  name=\"ordpx\" value=\"\"/></div>"+
						"<div>"+inButton+"</div>"+
						 "</div>");
	if(typeof(ordsx)=="undefined" || ordsx==null){
		ordsx="";
	}
	intoSelBox("ordsx"+num,strategy01,ordsx);
	if(typeof(ordpx)=="undefined" || ordpx==null){
		ordpx="";
	}
	intoSelBox("ordpx"+num,strategy04,ordpx);
	num++ ;	
}
//删除行
function delIf(obj){
	var pDiv=$(obj).parent().parent();
	if("inhtmlo"!=$(pDiv).attr("id") && "inhtmlot"!=$(pDiv).attr("id")){
		$(pDiv).prev().remove();
		$(pDiv).remove();
	}
}
//保存操作
function save(){
	$("#mainform").submit();
}
//点击某一拣货策略显示详情
function onCheck(val){
	var ifnum=1,ordnum=1;
	$.ajax({
		   type: "post",
		   url: "${ctx }/base/stategy/jsonlist",
		   data: "strategynum="+val,
		   dataType: "json",
		   success: function(date){
			   if(date!=null &&  date.length>0){
				   var obj;clearIfAndOrd(false);
				   for(var i=0;i<date.length;i++){
					   obj=date[i];
					   if(null!=obj.ifField && ""!=obj.ifField){
							if(1==ifnum){
								   $("#strategyName").val(obj.strategyName);
								   $("#strategyCode").val(obj.strategyCode);
								   intoSelBox("ifsx",strategy01,obj.ifField);
								   intoSelBox("ifbds",strategy02,obj.ifType);
								   $("#ifval").val(obj.ifVal);
								   intoSelBox("iflink",strategy03,obj.ifLink);
								   ifnum++;
							 }else{
								 addIf(obj.ifField,obj.ifType,obj.ifVal,obj.ifLink); 
							 } 
					   }else{
						   if(1==ordnum){
							   intoSelBox("ordsx",strategy01,obj.ordField);
							   intoSelBox("ordpx",strategy04,obj.ordSort); 
							   ordnum++;
						   }else{
							   addOrder(obj.ordField,obj.ordSort); 
						   }
					   }
				   }
			   } 
		   }
	});
}
//清空条件和排序框  isall==true 清空所有，==false 第一列不清
function clearIfAndOrd(isall){
	$("#strategyName").val("");
	$(".ifdiv").each(function (i){
		$(this).prev().remove();	
		$(this).remove();
	});
	$(".orddiv").each(function (i){
		$(this).prev().remove();	
		$(this).remove();
	});
	if(true==isall){
		$(".radioSpan input").each(function (i){
			if($(this).attr("checked")=="checked"){
				$(this).removeAttr("checked");	
			}	
		});
		$("#strategyCode").val("");
		intoSelBox("ifsx",strategy01,"");
	   	intoSelBox("ifbds",strategy02,"");
	   	$("#ifval").val("");
	   	intoSelBox("iflink",strategy03,"");
	    intoSelBox("ordsx",strategy01,"");
		intoSelBox("ordpx",strategy04,""); 
	}
}
//新建
function newAdd(){
	clearIfAndOrd(true);
}
function del(){
	var getVal=$("input[name='ifWrap']:checked").val();
	if(typeof(getVal)=="undefined"){
		 parent.$.easyui.messager.alert("请在策略列表中选中一条！");
	}else{
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
			if (data){
				$.ajax({
					type:'get',
					url:"${ctx}/base/stategy/delete/"+getVal,
					dataType:"json",
					success: function(date){
						if(date!=null){
				    		 if("success"==date.retStr){
				    			 $("#"+date.id).parent().remove();
				    			 clearIfAndOrd(true);
				    			 parent.$.easyui.messager.show({title: "操作提示", msg: "删除成功！", position: "bottomRight"});
				    		 }else{
				    			 parent.$.easyui.messager.alert("删除失败！"); 
				    		 }
				    	}else{
			    			 parent.$.easyui.messager.alert("删除失败！"); 
			    		}
					}
				});	
			}
		});
	}
}
</script>
</body>
</html>