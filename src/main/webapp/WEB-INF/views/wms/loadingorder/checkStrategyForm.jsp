<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div  class="easyui-layout" data-options="fit:true">
	 <div class="datagrid-toolbar" data-options="region:'north',split:false,border:false" style="height:auto">
       <shiro:hasPermission name="base:stategy:add">
           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" data-options="disabled:false" onclick="newAdd()">新建</a>
       </shiro:hasPermission>
	</div>
	<div id="plist" data-options="region:'west',split:false,border:false" style="width:200px;margin-right: 10px;">
		<div style="width:180px;height: 100%">
			<div id="cllist" class="easyui-panel" title="策略列表"  data-options="fit:true,border:false" >
				<span class="radioSpan">
					<input  id="0" name="ifWrap" type="radio"  onclick="onCheck(this.value)" checked="checked"  value="0">
				 	<lab>默认拣货策略</lab>
				</span>
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
			  	<input type="hidden" id="strategyCode" name="strategyCode" value="-1">
			  	<input type="hidden" name="orderNum" id="orderNum" value="${ordnum}">
			  	策略名称：<input type="text" id="strategyName" name="strategyName" class="easyui-validatebox" data-options="width:150,required:'required',validType:'length[1,25]'" style="height:22px;" />
			  </div>
			  <div id="pone" class="easyui-panel" title="条件"  data-options="" style="height:200px;">
			  		<div>
			  			<div>属性</div>
			  			<div>表达式</div>
			  			<div>值</div>
			  			<div>条件连接</div>
			  			<div style="width: 100px;">操作</div>
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
			  			<div id="inhtmlif" style="width: 100px;">
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
			return true;	// 返回false终止表单提交
	    },    
	    success:function(data){
	    	 var getJosn=eval("("+data+")");
	    	 var nModle=getModle();
	    	 if("success"==getJosn.endStr){
	    		 splitJson=getJosn.splitTray;
	    		 d.panel('close');
	    		 if(getJosn.splitTray!=null && typeof(getJosn.splitTray)!="undefined" && splitJson.length>0){
	    			 tids=getJosn.trayIds;
	    			 openSplitT('${ordnum}');
	    		 }else{
	    			 if(0==nModle){
	    			 	 var orderNum = '${ordnum}';
		    		 	 $.ajax({
		  				  	async:false,
							type : "POST",
							url : "${ctx}/bis/truck/remark/"+orderNum+"/"+getJosn.tNum,
							data : "",
							dataType : "text",
							success : function(date) {
								
							}
						 });
	    				 parent.$.easyui.messager.alert("生成装车单成功！装车单号："+getJosn.tNum);
	    			 }else{
	    				 parent.$.easyui.messager.alert("货转成功");
	    				 //货转成功后加载托盘货转明细
	        			 intoTTaryTabl();
	        			 //删除拆托按钮
	        			 rmtga();
	    			 }
	    		 }
	    	 }else  if("error"==getJosn.endStr){
	    		 parent.$.easyui.messager.alert("提单号："+getJosn.billNum +" 箱号："+getJosn.ctnNum+" SKU:"+getJosn.skuNum+" 入库类型："+("0"==getJosn.state?"成品":"货损")+" 库存不足无法生成！");
	    	 }else{
	    		 parent.$.easyui.messager.alert("分拣失败，请重新选择分拣策略分拣！");
	    	 }
	    }//end success    
	});
});
/**
 * 根据订单号判断订单类型 1货转，0出库订单
 */
function getModle(){
	var getOrdNum=$("#orderNum").val();
	if(getOrdNum!=null && getOrdNum!=""){
		if("T"==getOrdNum.substring(0,1)){
			return 1;
		}else{
			return 0;
		}
	}
	return 0;
}

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
						"<div style=\"width: 100px;\">"+inButton+"</div>"+
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
//点击某一拣货策略显示详情
function onCheck(val){
	var ifnum=1,ordnum=1;
	if(0==val){
		clearIfAndOrd(true);
		$("#strategyCode").val(-1);
		$("#0").attr("checked","checked");
		return false;
	}
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
</script>
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
</body>
</html>