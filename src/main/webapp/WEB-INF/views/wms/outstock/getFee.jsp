<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<form id="outfee" method="post">
	<table id="sell" border="1"  style="float:left;width:50%" >
		<tr>
			<td colspan='2' align="center">存货方</td>
		</tr>
		<tr>
			<td align="center">费目<input type="checkbox" id="checkAllSell"/>全选</td>
			<td align="center">按比例收费<input type="checkbox"  id="checkAllSB"/>全选</td>
		</tr>
		<c:forEach items="${sellFeeList}" var="sellfee" varStatus="my">
			<tr>
				<td><input id="${my.count}" name="sell"  type="checkbox" value="${sellfee.feeCode}"/>${sellfee.feeName}</td>
				<td><input id="${my.count}" name="checkS" type="checkbox" />比例</td>
			</tr>
		</c:forEach>
	</table>
	<table id="buy" style="float:left;width:50%" border='1'    >
		<tr>
			<td colspan='2' align="center">收货方</td>
		</tr>
		<tr>
			<td align="center">费目<input type="checkbox" id="checkAllBuy"/>全选</td>
			<td align="center">按比例收费<input type="checkbox" id="checkAllBB"/>全选</td>
		</tr>
		<c:forEach items="${buyFeeList}" var="buyfee" varStatus="my">
			<tr>
				<td><input id="${my.count}" name="buy" type="checkbox" value="${buyfee.feeCode}"/><label>${buyfee.feeName}</label></td>
				<td><input id="${my.count}" name="checkB" type="checkbox"/>比例</td>
			</tr>
		</c:forEach>
	</table>
</form>
<script type="text/javascript">
	var buyNum="${buyNum}";
	var sellNum="${sellNum}";

	$(function(){   
	  var t ; 
	  var ids;
	  var sellId = $("#sellId").val();
	  var buyId = $("#buyId").val();
	  if(sellId != ""){
	  	var sellA = new Array();
	  	sellA = sellId.split(",");
	  	for(var i = 0;i<sellA.length;i++){
	  		t = sellA[i];
	   		$("input[name='sell'][value="+t+"]").attr("checked",true);
	   		i++;
	   		if(sellA[i] == 1){
	   			ids = $("input[name='sell'][value="+t+"]").attr("id");
	   			$("input[name='checkS'][id="+ids+"]").attr("checked",true);
	   		}
	  	}
	  }
	  if(buyId != ""){
	  	var buyA = new Array();
	  	buyA = buyId.split(",");
	  	for(var j = 0;j<buyA.length;j++){
	  		t = buyA[j];
	  		$("input[name='buy'][value="+t+"]").attr("checked",true);
	  		j++;
	  		if(buyA[j] == 1){
	   			ids = $("input[name='buy'][value="+t+"]").attr("id");
	   			$("input[name='checkB'][id="+ids+"]").attr("checked",true);
	   		}
	  	}
	  }
	});

$(":checkbox").click( function () { 
   var idt;
   var valuet;
   var idc= $(this).attr("id");
   var namec = $(this).attr("name");
   var valuec = $(this).attr("value");
   //点击卖方比例时
   if(namec == "checkS"){
   	  if( $(this).is(":checked") ){
   	  		$("input[name='sell'][id="+idc+"]").attr("checked",true);
   	  		valuet = $("input[name='sell'][id="+idc+"]").attr("value");
   	  		if ( $("input[name='buy'][value="+valuet+"]").is(":checked") ){
   	  			idt = $("input[name='buy'][value="+valuet+"]").attr("id");
   	  			$("input[name='checkB'][id="+idt+"]").attr("checked",true);
   	  		}
   	  }else{
   	  		valuet = $("input[name='sell'][id="+idc+"]").attr("value");
   	  		if( $("input[name='buy'][value="+valuet+"]").is(":checked") ){
   	  			$("input[name='sell'][id="+idc+"]").attr("checked",false);
   	  		}
   	  }
   }
   //点击买方比例时
   else if(namec == "checkB"){
   	  if( $(this).is(":checked") ){
   	  		$("input[name='buy'][id="+idc+"]").attr("checked",true);
   	  		valuet = $("input[name='buy'][id="+idc+"]").attr("value");
   	  		if ( $("input[name='sell'][value="+valuet+"]").is(":checked") ){
   	  			idt = $("input[name='sell'][value="+valuet+"]").attr("id");
   	  			$("input[name='checkS'][id="+idt+"]").attr("checked",true);
   	  		}
   	  }else{
   	  		valuet = $("input[name='buy'][id="+idc+"]").attr("value");
   	  		if( $("input[name='sell'][value="+valuet+"]").is(":checked") ){
   	  			$("input[name='buy'][id="+idc+"]").attr("checked",false);
   	  		}
   	  }
   }
    //点击卖方费目时
   else if(namec == "sell"){
  		if( $(this).is(":checked") ){
  			if( $("input[name='buy'][value="+valuec+"]").is(":checked") ){
  				$("input[name='checkS'][id="+idc+"]").attr("checked",true);
  				idt = $("input[name='buy'][value="+valuec+"]").attr("id");
  				$("input[name='checkB'][id="+idt+"]").attr("checked",true);
  			}
  		}else{
  			$("input[name='checkS'][id="+idc+"]").attr("checked",false);
  		}
  		
   }
    //点击买方买方费目时
   else if(namec == "buy"){
   		if( $(this).is(":checked") ){
  			if( $("input[name='sell'][value="+valuec+"]").is(":checked") ){
  				$("input[name='checkB'][id="+idc+"]").attr("checked",true);
  				idt = $("input[name='sell'][value="+valuec+"]").attr("id");
  				$("input[name='checkS'][id="+idt+"]").attr("checked",true);
  			}
  		}else{
  			$("input[name='checkB'][id="+idc+"]").attr("checked",false);
  		}
   }
   else{
   		return;
   }
});

//四个全选
$("#checkAllSell").click(function(){
 if( $("#checkAllSell").is(":checked") ){
 	for(var i = 1;i<parseInt(sellNum)+1;i++){
 		if( $("input[name='sell'][id="+i+"]").is(":checked") ){
 			
 		}else{
 			$("input[name='sell'][id="+i+"]").trigger("click");
 		}
 	}
 }else{
	for(var i = 1;i<parseInt(sellNum)+1;i++){
 		if( $("input[name='sell'][id="+i+"]").is(":checked") ){
 			$("input[name='sell'][id="+i+"]").trigger("click");
 		}else{
 			
 		}
 	}	
 }
})

$("#checkAllSB").click(function(){
	if( $("#checkAllSB").is(":checked") ){
 	for(var i = 1;i<parseInt(sellNum)+1;i++){
 		if( $("input[name='checkS'][id="+i+"]").is(":checked") ){
 			
 		}else{
 			$("input[name='checkS'][id="+i+"]").trigger("click");
 		}
 	}
 }else{
	for(var i = 1;i<parseInt(sellNum)+1;i++){
 		if( $("input[name='checkS'][id="+i+"]").is(":checked") ){
 			$("input[name='checkS'][id="+i+"]").trigger("click");
 		}else{
 			
 		}
 	}	
 }
})

$("#checkAllBuy").click(function(){
if( $("#checkAllBuy").is(":checked") ){
 	for(var i = 1;i<parseInt(buyNum)+1;i++){
 		if( $("input[name='buy'][id="+i+"]").is(":checked") ){
 			
 		}else{
 			$("input[name='buy'][id="+i+"]").trigger("click");
 		}
 	}
 }else{
	for(var i = 1;i<parseInt(buyNum)+1;i++){
 		if( $("input[name='buy'][id="+i+"]").is(":checked") ){
 			$("input[name='buy'][id="+i+"]").trigger("click");
 		}else{
 			
 		}
 	}	
 }
})

$("#checkAllBB").click(function(){
if( $("#checkAllBB").is(":checked") ){
 	for(var i = 1;i<parseInt(buyNum)+1;i++){
 		if( $("input[name='checkB'][id="+i+"]").is(":checked") ){
 			
 		}else{
 			$("input[name='checkB'][id="+i+"]").trigger("click");
 		}
 	}
 }else{
	for(var i = 1;i<parseInt(buyNum)+1;i++){
 		if( $("input[name='checkB'][id="+i+"]").is(":checked") ){
 			$("input[name='checkB'][id="+i+"]").trigger("click");
 		}else{
 			
 		}
 	}	
 }
})

</script>
</body>
</html>
