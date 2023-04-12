<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<form id="downForm" method="post" >
	</form>
<form id="asnform" action="${ctx }/bis/asn/${action}" method="post">   
<div data-options="region:'north',split:true,border:false" style="height:220px" >
         <div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false,border:false"   class="datagrid-toolbar easyui-layout" >
				<shiro:hasPermission name="bis:asnma:save">
		       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="save();">保存</a>
		       		<span class="toolbar-item dialog-tool-separator"></span>
		       	</shiro:hasPermission>
		       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="withLoading()">重收类型关联装车单</a>
		       		<span class="toolbar-item dialog-tool-separator"></span>
		       		<c:if test="${ifEnter == 1}">
		       		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="refresh()">制作下一个的ASN</a>
		       		<span class="toolbar-item dialog-tool-separator"></span>
		       		</c:if>
			</div>
			<div data-options="region:'south',split:false,border:false" style="height:10px;"></div>
			<div data-options="region:'west',split:false,border:false" style="width:10px;"></div>
			<div data-options="region:'east',split:false,border:false" style="width:10px;"></div>
			<div data-options="region:'center',split:false,border:false"  >
					<table>
						<tr>
							<input type="hidden" id="isBonded" name="isBonded" value="${asn.isBonded}" />
							<input type="hidden" id="asnState" name="asnState"  value="${asn.asnState}" />
							<td style="width:50px; ">ASN号：</td><td>
							<input type="text" id="asn"  name="asn"  class="easyui-validatebox"  readonly="readonly"  data-options="width:150,required:'required'" value="${asn.asn}" /></td>
							<td style="width:60px; ">联系单号：</td><td>
							<c:choose>
								<c:when test="${asn.isEdite == 1}">
									<input type="text" name="linkId" class="easyui-validatebox" readonly="readonly"   data-options="width:150" value="${asn.linkId}" />
								</c:when>
								<c:otherwise>
									<input type="text" id="linkId" name="linkId" class="easyui-searchbox"    data-options="prompt:'请选择联系单',required:'required',width:150,searcher:doSearch,validType:'length[16,20]'" value="${asn.linkId}" />
								</c:otherwise>
							</c:choose>
							</td>
							<td style="width:60px; ">提单号：</td><td>
							<input type="text" id="billNum" name="billNum" readonly="readonly" class="easyui-validatebox" <c:if test="${asn.isEdite == 1}"> readonly="readonly" </c:if> data-options="width:150,required:'required',validType:'length[4,100]'" value="${asn.billNum}"/></td>
							<td style="width:60px; ">入库类型：</td><td>
							<c:choose>
								<c:when test="${asn.isEdite == 1}">
									<input type="text" name="ifSecondEnter" class="easyui-validatebox" readonly="readonly"   data-options="width:150" <c:if test="${asn.ifSecondEnter == 1}"> value="正常" </c:if> <c:if test="${asn.ifSecondEnter == 3}"> value="分拣" </c:if> <c:if test="${asn.ifSecondEnter == 2}"> value="重收" </c:if>/>
								</c:when>
								<c:otherwise>
									<select class="easyui-combobox" id="ifSecondEnter" name="ifSecondEnter" data-options="width:150,required:'required'">
										<option value=""></option>
									</select>
								</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr style="height: 5px;"></tr>
						<tr>
							<td >箱号：</td><td>
							<select  id="ctnSelect" name="ctnNum" class="easyui-combobox" <c:if test="${asn.isEdite == 1}"> disabled </c:if> data-options="width:150,required:'required'" >
							</select>
							</td>
							<td>order号：</td><td>
							<input type="text" id="orderNum" name="orderNum" class="easyui-validatebox" <c:if test="${asn.isEdite == 1}"> readonly="readonly" </c:if> data-options="width:150,validType:'length[2,20]'" value="${asn.orderNum}"/></td>
							<td>入库时间：</td><td>
							<c:choose>
								<c:when test="${asn.isEdite == 1}">
									<input type="text" id="inboundTime" name="inboundTime" class="easyui-validatebox" readonly="readonly"   data-options="width:150" value="<fmt:formatDate value="${asn.inboundTime}"/>" >
								</c:when>
								<c:otherwise>
								 	<input id="inboundTime" name="inboundTime" type="text"   class="easyui-datebox" datefmt="yyyy-MM-dd" data-options="width: 150,prompt: '计费按此日期为准'"  value="<fmt:formatDate value="${asn.inboundTime}"/>" />
								</c:otherwise>
							</c:choose>
							</td>
							<td>存货方：</td><td>
							 	<input type="hidden" id="stockName" name="stockName" value="${asn.stockName}" />
								<c:choose>
								<c:when test="${asn.isEdite == 1}">
									<input type="text" name="stockIn"  class="easyui-validatebox" readonly="readonly"   data-options="width:150" value="${asn.stockName}" />
								</c:when>
								<c:otherwise>
									<select class="easyui-combobox" id="stockIn" name="stockIn" data-options="width:150,required:'required'">
										<option value=""></option>
									</select>
								</c:otherwise>
							</c:choose>
							</td>
						</tr>
					<!--	<tr>
							<td>导入箱号选择：</td>
							<td>
								<select id="ctnSelect"  class="easyui-combobox"  data-options="width:150">
								</select>
							</td>
						</tr> -->
						<tr style="height: 5px;">
							<td>备注：</td>
							<td colspan="7"><textarea rows="2" name="remark" cols="110" <c:if test="${asn.isEdite == 1}"> readonly="readonly" </c:if>  class="easyui-validatebox" data-options="validType:'length[1,50]'"   style="font-size: 12px;font-family: '微软雅黑'">${asn.remark}</textarea></td>
							<div id="skuList" style="display: none;"></div>
						</tr>
						<tr >
							<td>明细件数合计：</td>
							<td>
								<input id="pieceInfo"   class="easyui-validatebox" data-options="width: 180" readonly style="background:#eee"/> 
							</td>
							<td>明细净重合计：</td>
							<td>
								<input id="netInfo"   class="easyui-validatebox" data-options="width: 180"  readonly style="background:#eee"/> 
							</td>
							<td>明细毛重合计：</td>
							<td>
								<input id="grossInfo"   class="easyui-validatebox" data-options="width: 180"  readonly style="background:#eee"/> 
							</td>
						</tr>
					</table>
			</div>
        </div>
</div>
<div data-options="region:'center',split:true,border:false,title:'明细列表'" >
	<div id="tb" style="padding:5px;height:auto">
	  	<div>
	  	<c:if test="${asn.isEdite == 0}"> 
	  		<shiro:hasPermission name="bis:asnma:newsku">
		  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addSKUFrom()">新建下一个sku</a>
		  		<span class="toolbar-item dialog-tool-separator"></span>
	  		</shiro:hasPermission>
	  		<shiro:hasPermission name="bis:asnma:impsku">
		  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openSKUList()">sku库导入</a>
		  		<span class="toolbar-item dialog-tool-separator"></span>
	  		</shiro:hasPermission>
	  		<shiro:hasPermission name="bis:asnma:delete">
		  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
		   		<span class="toolbar-item dialog-tool-separator"></span>
	   		</shiro:hasPermission>
	   		<shiro:hasPermission name="bis:asnma:import">
		  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="importInfo()">导入入库联系单明细</a>
		  		<span class="toolbar-item dialog-tool-separator"></span>
	  		</shiro:hasPermission>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="down()">下载导入模板</a>
				<span class="toolbar-item dialog-tool-separator"></span>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="into()">EXCEL导入</a>
				<span class="toolbar-item dialog-tool-separator"></span>
	    </c:if> 
	    </div>
	</div>
  	<table id="dg"></table>
</div>
</form>
<div id="dlg"></div>
<div id="dlgloading"></div>
  
<script type="text/javascript">
var dg;
var d;
var dloading;
var model='${action}';
var isEdite='${asn.isEdite}';
var trNum=0;//用于记录行号
$(document).ready(function(){
	
	gridDG();
	ctnSelect();
	//入库类型
	var ifSecond = '${asn.ifSecondEnter}';
	if(ifSecond == ""){
		ifSecond = 1;
	}
	$.ajax({
	   type: "GET",
	   url: "${ctx}/system/dict/json",
	   data: "filter_LIKES_type=asnType",
	   dataType: "json",
	   success: function(date){
		   if(date!=null && date.rows!=null && date.rows.length>0){
			   $('#ifSecondEnter').combobox({
				   data : date.rows,
				   value: ifSecond,
				   valueField:'value',
				   textField:'label',
				   editable : false,
				   onChange: function (newVal, oldVal){
					   if(newVal=='2'){
					   	   $('#inboundTime').datebox("clear");
					   	   $('#inboundTime').datebox({ disabled: true });
					   }else if(oldVal=='2'){
					   	   $('#inboundTime').datebox({ disabled: false });
					   }
				   },
				   onLoadSuccess:function(){
						if($('#ifSecondEnter').combobox("getValue")=="2"){
							$('#inboundTime').datebox({ disabled: true });
						}
					}
			   });
		   }
	   }
	});
	//客户
	var getstockIn='${asn.stockIn}';
	$('#stockIn').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${asn.stockIn}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onLoadSuccess:function(){
				if(getstockIn!=null && getstockIn!=""){
					 $('#stockIn').combobox("select",getstockIn);
					 getstockIn="";
				}
		    
	   }
   	});
	
	$('#asnform').form({    
	    onSubmit: function(){
	    	if(0==isEdite){
	    		//仓库名称赋值
		    	var faild=$('#stockIn').combobox("getText");
		    	$('#stockName').val(faild);
	    	}
	    	var isValid = $(this).form('validate');
	    	//检验件数是否都填入值
	    	$("input[name='piece']").each(function(){ 
	    		if($(this).val()==""){
	    			parent.$.easyui.messager.alert("明细列表中请填写件数值！");
	    			isValid= false;
	    		}
	    	});
			return isValid;	// 返回false终止表单提交
	    },    
	    success:function(data){
	    	 if("success"==data){
	    		 parent.$.easyui.messager.show({title: "操作提示", msg: "保存成功！", position: "bottomRight"});
	    	 }else{
	    		 parent.$.easyui.messager.alert("保存失败！");
	    	 }
	    }    
	});
	$("input[name='linkId']").attr("readonly","readonly");
});

function ctnSelect(){
//箱号下拉
 if( $("input[name='linkId']").val() != "" ){
   	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/bis/asn/getAllMr/"+$("input[name='linkId']").val(),
		data : "",
		dataType : "json",
		success : function(date) {
			var ctn = '${asn.ctnNum}';
			if(ctn == "" && date.length !=0){
				ctn = date[0].ctnNum;
				$("#orderNum").val( date[0].orderNum );
			}
				$('#ctnSelect').combobox({
					data : date,
					value: ctn,
					valueField : 'ctnNum',
					textField : 'ctnNum',
					formatter:fmt,
					editable : false,
					onSelect:function(record){
						$("#orderNum").val(record.orderNum);
					}
				});
		}
	});
  }
}

function fmt(r) { 
 if(r.ifasn == "1"){
 return '<div style="background:#f00">'+r.ctnNum+'</div>' 
 }else{
 return  r.ctnNum ;
 }
}

function gridDG(){
	var getId='${asn.asn}';
	if(getId!=null && getId!=""){
		dg=$('#dg').datagrid({    
			method: "get",
		    url:'${ctx}/bis/asninfo/listjson', 
		    fit : true,
			fitColumns :false,//水平滚动
			border : false,
			idField : 'skuId',
			striped:true,
			pagination:false,
			rownumbers:true,
			pageNumber:1,
			pageSize :1000,
			pageList : [1000],
			singleSelect:true,
		    columns:[[    
				{field:'skuId',title:'SKU_ID',sortable:false,width:150},    
//				{field:'skuDescribe',title:'SKU描述',sortable:false,width:350},
				{field:'cargoName',title:'品名',sortable:false,width:350},//(2016-9-12修改)
				{field:'cargoState',title:'库存类型',sortable:false,width:80,
					formatter : function(value, row, index) {
						if(value==1){return '成品'}else if(value==2){return '箱损'}else{return '货损'}
					}
				},
				{field:'piece',title:'件数',sortable:false,width:100},
				{field:'netWeight',title:'总净重',sortable:false,width:100},
				{field:'grossWeight',title:'总毛重',sortable:false,width:100},
				{field:'salesNum',title:'SALES REF NO',sortable:false,width:100},
				{field:'rkNum',title:'入库号',sortable:false,width:100},
				{field:'proTime',title:'生产日期/YYYY-MM-DD',sortable:false,width:150},
				{field:'remark1',title:'备注',sortable:false,width:100},
				{field:'netSingle',title:'单净',sortable:false,width:60}, 
				{field:'grossSingle',title:'单毛',sortable:false,width:60},
				{field:'realPiece',title:'实际入库件数',sortable:false,width:60,
					formatter : function(value, row, index) {
						var real;
						$.ajax({
							async:false,
							type:'get',
							url: "${ctx}/bis/asn/realPiece/"+getId+"/"+row.skuId,
							dataType:"text",
							success: function(data){
									real=data;
							}
						});	
						return real;
					}
				},
				{field:'hsCode',title:'HS编码',sortable:false,width:60},
				{field:'hsItemname',title:'海关商品成名',sortable:false,width:60},
				{field:'accountBook',title:'账册商品序号',sortable:false,width:60},
				{field:'addid',title:'addid',hidden:true},
				{field:'num',title:'行号',hidden:true}
		    ]],
		    queryParams: {
		    	filter_EQS_asnId:'${asn.asn}'
			},
			onLoadSuccess:function(){

				var rows = $('#dg').datagrid('getRows');
				var getState=$("#asnState").val();
	            if (rows) {
	            	trNum=rows.length;
	            	var rowObj;
	                for (var i = 0; i<rows.length; i++) {
	                	rowObj=rows[i];
	                	if(model=="update" && getState>1){
	                		$('#dg').datagrid('updateRow',{
	                    		index:i,
	                    		row: {
	                    			piece: (rowObj['piece']!=null?rowObj['piece']:""),
	                    			netWeight: (rowObj['netWeight']!=null?rowObj['netWeight']:""),
	                    			grossWeight: (rowObj['grossWeight']!=null?rowObj['grossWeight']:""),
	                    			salesNum: (rowObj['salesNum']!=null?rowObj['salesNum']:""),
	                    			rkNum: '<input type=\"text\" id=\"rkNum'+i+'\" name=\"rkNum\" style=\"width:100%\" value=\"'+(rowObj['rkNum']!=null?rowObj['rkNum']:"") +'\">',
	                    			proTime: '<input type=\"text\" id=\"proTime'+i+'\" name=\"proTime\"  onchange="checkDate(this,this.value)"  style=\"width:100%\" value=\"'+(rowObj['proTime']!=null?rowObj['proTime']:"")+'\">',
	                    			remark1: (rowObj['remark1']!=null?rowObj['remark1']:""),
	                    			hsCode: (rowObj['hsCode']!=null?rowObj['hsCode']:""),
	                    			hsItemname: (rowObj['hsItemname']!=null?rowObj['hsItemname']:""),
	                    			accountBook: (rowObj['accountBook']!=null?rowObj['accountBook']:""),
	                    			addid:'<input type=\"text\" name=\"addid\" style=\"width:100%\" value=\"'+rowObj['skuId']+'\">',
	                    			num:i
	                    		}
	                    	});
	                	}else{
	                		$('#dg').datagrid('updateRow',{
	                    		index:i,
	                    		row: {
	                    			rkNum: '<input type=\"text\" id=\"rkNum'+i+'\" name=\"rkNum\" style=\"width:100%\" value=\"'+(rowObj['rkNum']!=null?rowObj['rkNum']:"") +'\">',
	                    			piece: '<input type=\"text\" id=\"piece'+i+'\" autocomplete=\"off\" name=\"piece\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d]/g,\'\')\"  value=\"'+(rowObj['piece']!=null?rowObj['piece']:"")+'\" onchange=\"changVal(\''+rowObj['skuId']+'\',this.value)\" >',
	                    			netWeight: '<input type=\"text\"  id=\"netWeight'+i+'\" name=\"netWeight\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d.]/g,\'\')\"  value=\"'+(rowObj['netWeight']!=null?rowObj['netWeight']:"")+'\">',
	                    			grossWeight: '<input type=\"text\" id=\"grossWeight'+i+'\" name=\"grossWeight\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d.]/g,\'\')\"  value=\"'+(rowObj['grossWeight']!=null?rowObj['grossWeight']:"")+'\">',
	                    			salesNum: '<input type=\"text\" id=\"salesNum'+i+'\" name=\"salesNum\" style=\"width:100%\" value=\"'+(rowObj['salesNum']!=null?rowObj['salesNum']:"")+'\">',
	                    			proTime: '<input type=\"text\" id=\"proTime'+i+'\" name=\"proTime\" style=\"width:100%\" onchange="checkDate(this,this.value)" value=\"'+(rowObj['proTime']!=null?rowObj['proTime']:"")+'\">',
	                    			remark1: '<input type=\"text\" id=\"remark1'+i+'\" name=\"remark1\" style=\"width:100%\" value=\"'+ (rowObj['remark1']!=null?rowObj['remark1']:"")+'\">',
	                    			hsCode: '<input type=\"text\" id=\"hsCode'+i+'\" name=\"hsCode\" style=\"width:100%\" value=\"'+ (rowObj['hsCode']!=null?rowObj['hsCode']:"")+'\">',
	                    			hsItemname: '<input type=\"text\" id=\"hsItemname'+i+'\" name=\"hsItemname\" style=\"width:100%\" value=\"'+ (rowObj['hsItemname']!=null?rowObj['hsItemname']:"")+'\">',
	                    			accountBook: '<input type=\"text\" id=\"accountBook'+i+'\" name=\"accountBook\" style=\"width:100%\" value=\"'+ (rowObj['accountBook']!=null?rowObj['accountBook']:"")+'\">',
	                    			
	                    			addid:'<input type=\"text\" name=\"addid\" style=\"width:100%\" value=\"'+rowObj['skuId']+'\">',
	                    			num:i
	                    		}
	                    	});
	                	}
	                }
	            }
	            insertSum();
			},
		    enableHeaderClickMenu: true,
		    enableHeaderContextMenu: true,
		    enableRowContextMenu: false,
		    toolbar:'#tb'
		});
	}//end if getId
}

function insertSum(){
		    var rows = $('#dg').datagrid('getRows');
	    	var pieces=0;
	    	var net=0;
	    	var gross=0;
	    	if($("#asnState").val()>1){
	    		for(var i=0;i<rows.length;i++){
	    			pieces += rows[i]['piece'];
	    			net += rows[i]['netWeight'];
	    			gross += rows[i]['grossWeight'];
	    		};
	    	}else{
	    		for(var i=0;i<rows.length;i++){
	    			pieces += parseInt($("#piece"+i).val());
	    			net +=Number($("#netWeight"+i).val());
	    			gross +=Number($("#grossWeight"+i).val());
	    		};
	    	}
	    	$("#pieceInfo").val(pieces);
	    	$("#netInfo").val(net);
	    	$("#grossInfo").val(gross);	
		
}

function checkDate(obj,sDate){
	/** var sDate=sDate.replace(/(^\s+|\s+$)/g,'');
	 //去两边空格; 
	 if(sDate==''){ 
		 return true; 
     } 
	 //如果格式满足YYYY-(/)MM-(/)DD或YYYY-(/)M-(/)DD或YYYY-(/)M-(/)D或YYYY-(/)MM-(/)D就替换为'' 
	 //数据库中，合法日期可以是:YYYY-MM/DD(2003-3/21),数据库会自动转换为YYYY-MM-DD格式 10. 
	  var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;     
      var r = sDate.replace(reg,''); 
	 if(r==''){//说明格式满足YYYY-MM-DD或YYYY-M-DD或YYYY-M-D或YYYY-MM-D 
		var t=new Date(sDate.replace(/\-/g,'/')); 
			 var ar=sDate.split(/[-/:]/);        
			 if(Number(ar[0])!=t.getFullYear()||Number(ar[1])!=t.getMonth()+1||Number(ar[2])!=t.getDate()){
				 //alert('错误的日期格式！格式为：YYYY-MM-DD或YYYY/MM/DD。注意闰年。');
	            return $(obj).val(""); 
			 } 
	 }else{
		 //alert('错误的日期格式！格式为：YYYY-MM-DD或YYYY/MM/DD。注意闰年。'); 
		 return $(obj).val("");  
	 } 
	 */
}
//改变件数修改总毛重，总净重
function changVal(id,val){
	var nIndex=getIndex(id); 
	var row = $('#dg').datagrid('getRows')[nIndex]; 
	if(nIndex<10000000 && row!=null){
		var netSingle=row["netSingle"];//单净
		var grossSingle=row["grossSingle"];//单毛
		var getNum=Number(row["num"]);
		$('#dg').datagrid('updateRow',{
			index:nIndex,
			row: {
 				piece:'<input type=\"text\" id=\"piece\" name=\"piece\" autocomplete=\"off\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d]/g,\'\')\" value=\"'+val+'\" onchange=\"changVal(\''+id+'\',this.value)\" >',
				netWeight:'<input type=\"text\" name=\"netWeight\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d.]/g,\'\')\" value=\"'+Number(netSingle)*Number(val) +'\">',
				grossWeight:'<input type=\"text\" name=\"grossWeight\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d.]/g,\'\')\" value=\"'+Number(grossSingle)*Number(val) +'\">',
				rkNum:'<input type=\"rkNum\" id=\"rkNum'+(getNum)+'\" name=\"rkNum\" style=\"width:100%\" value=\"'+$("#rkNum"+getNum).val()+'\">',
				salesNum:'<input type=\"text\" id=\"salesNum'+(getNum)+'\" name=\"salesNum\" style=\"width:100%\" value=\"'+$("#salesNum"+getNum).val()+'\">',
				proTime:'<input type=\"text\" id=\"proTime'+(getNum)+'\" name=\"proTime\" style=\"width:100%\"  onchange="checkDate(this,this.value)"  value=\"'+$("#proTime"+getNum).val()+'\">',
				remark1:'<input type=\"text\" id=\"remark1'+(getNum)+'\" name=\"remark1\" style=\"width:100%\" value=\"'+$("#remark1"+getNum).val()+'\">',
				hsCode:'<input type=\"text\" id=\"hsCode'+(getNum)+'\" name=\"hsCode\" style=\"width:100%\" value=\"'+$("#hsCode"+getNum).val()+'\">',
				hsItemname:'<input type=\"text\" id=\"hsItemname'+(getNum)+'\" name=\"hsItemname\" style=\"width:100%\" value=\"'+$("#hsItemname"+getNum).val()+'\">',
				accountBook:'<input type=\"text\" id=\"accountBook'+(getNum)+'\" name=\"accountBook\" style=\"width:100%\" value=\"'+$("#accountBook"+getNum).val()+'\">',
				
				addid:'<input type=\"text\" name=\"addid\" style=\"width:100%\" value=\"'+id+'\">'
			}
		});
	}
	
}
//遍历表单获取行号
function  getIndex(id){
	var nIndex='10000000';
	var rows = $('#dg').datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		if(id==rows[i]["skuId"]){
			nIndex = i;
			break;
		}
	}
	return nIndex;
}
//给表单添加信息
function addTBRow(sku,desc,cargoState,rkNum,piece,netWeight,grossWeight,nnetSingle,ngrossSingle,remark1,accountBook,hsCode,hsItemname){
	trNum+=1;
	var rows = $('#dg').datagrid('getRows');
	$('#dg').datagrid('insertRow',{
		index: rows.length+1,
		row: {
			skuId:sku,
			cargoName:desc,
			cargoState:cargoState,
			piece: '<input type=\"text\" id=\"piece\" autocomplete=\"off\" name=\"piece\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d]/g,\'\')\" value=\"'+piece+'\" onchange=\"changVal(\''+sku+'\',this.value)\">',
			netWeight: '<input type=\"text\" name=\"netWeight\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d.]/g,\'\')\" value=\"'+netWeight +'\">',
			grossWeight: '<input type=\"text\" name=\"grossWeight\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d.]/g,\'\')\" value=\"'+grossWeight +'\">',
			netSingle: nnetSingle,
			grossSingle:ngrossSingle,
			rkNum:'<input type=\"text\" id=\"rkNum'+trNum+'\" name=\"rkNum\" style=\"width:100%\">',
			salesNum: '<input type=\"text\" id=\"salesNum'+trNum+'\" name=\"salesNum\" style=\"width:100%\" value=\"'+""+'\">',
			accountBook: '<input type=\"text\" name=\"accountBook\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d.]/g,\'\')\" value=\"'+accountBook +'\">',
			hsCode: '<input type=\"text\" name=\"hsCode\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d.]/g,\'\')\" value=\"'+hsCode +'\">',
			hsItemname: '<input type=\"text\" name=\"hsItemname\" style=\"width:100%\" onkeyup=\"value=value.replace(\/[^\\-?\\d.]/g,\'\')\" value=\"'+hsItemname +'\">',		
			remark1: '<input type=\"text\" id=\"remark1'+trNum+'\" name=\"remark1\" style=\"width:100%\" value=\"'+(remark1!=null?remark1:"") +'\">',
			proTime:'<input id=\"proTime'+trNum+'\" name=\"proTime\"  onchange="checkDate(this,this.value)"  type=\"text\"  style=\"width:100%\" />',
			addid:'<input type=\"text\" name=\"addid\" style=\"width:100%\" value=\"'+sku+'\">',
			num:trNum
		}
	});
}
//校验SKU是否已经添加过
function  checkSKUIsHave(skuId){
	var rows = $('#dg').datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		if(skuId==rows[i]["skuId"]){
			return true;
		}
	}
	return false;
}
//SKU库添加
function openSKUList(){
 	d=$("#dlg").dialog({   
	    title: 'SKU选择',    
	    width: 650,    
	    height: 500,    
	    href:'${ctx}/bis/asn/skulist',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				var addrows = $('#skudg').datagrid('getSelections');
				var obj;
				for(var i=0;i<addrows.length;i++){
					obj=addrows[i];
					if(!checkSKUIsHave(obj['skuId'])){
						addTBRow(obj['skuId'],obj['cargoName'],obj['cargoState'],"","","","",obj['netSingle'],obj['grossSingle'],obj['remark']);
					}
				}
				d.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	}); 
} 
//新建下一个sku
function addSKUFrom(){
	d=$("#dlg").dialog({   
	    title: 'SKU添加',    
	    width: 450,    
	    height: 500,    
	    href:'${ctx}/bis/asn/skucreate',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$("#mainSKUform").submit();
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	}); 
}
//保存
function save(){
	var getDate;
	if(1==isEdite){
		getDate="";
	}else{
		getDate= $('#inboundTime').datebox('getValue');
	}
	if(1!=isEdite){
		if( $('#inboundTime').datebox('getValue')=="" &&  $('#ifSecondEnter').combobox("getValue")=="2" ){
			parent.$.easyui.messager.show({title: "操作提示", msg: "重收类型的ASN，请根据装车单号获取入库时间！", position: "bottomRight"});
			return;
		}
	}
	if(getDate!=null && getDate!=""){
		parent.$.messager.confirm('提示','入库日期已填，计费按照此日期起为准，是否保存？', function(data){
			if(true==data){
				$("#asnform").submit();
			}
		});
	}else{
		$('#asnform').submit();
	}
	
}
//删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '您确定要删除选中的行吗？', function(data){
		if(data){
			$("#skuList").append("<input type=\"hidden\"  name=\"delSkus\" value=\""+row["skuId"]+"\" >");
			$('#dg').datagrid('deleteRow',getIndex(row["skuId"])); 	
			insertSum();
		}
	});
}
//联系单查询
function doSearch(){
	d=$("#dlg").dialog({   
	    title: '联系单查询',    
	    width: 650,    
	    height:500,    
	    href:'${ctx}/bis/asn/enterlist',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				var row = dglx.datagrid('getSelected');
				if(rowIsNull(row)) return;
				$("#linkId").searchbox('setValue', row["CODENUM"]);
				$("#billNum").val(row["ITEMNUM"]);
				$("#stockIn").combobox("select",row["STOCKID"]);
				$("#isBonded").val(row["IF_BONDED"]);
				//客户
				var nstockIn=row["STOCKID"] ;
				$('#stockIn').combobox({
				   method:"GET",
				   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid="+nstockIn,
				   valueField: 'ids',
				   textField: 'clientName',
				   mode:'remote',
				   onLoadSuccess:function(){
							if(nstockIn!=null && nstockIn!=""){
								 $('#stockIn').combobox("select",nstockIn);
								 nstockIn="";
							}
							//选中入库联系单后，存货方不可更改
							$($("input[name='stockIn']").parent().find(".combo-text")).attr("readonly","readonly");
				   }
			   	});
			   	ctnSelect();
				d.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	}); 
}

//导入入库联系单明细
function importInfo(){
	var asnId = "${asn.asn}";
	var linkId = $("input[name='linkId']").val();
	var ctnNum = $("#ctnSelect").combobox("getValue");
	$.ajax({
	   type: "GET",
	   url: "${ctx}/bis/asn/ifsave/"+asnId,
	   dataType: "text",
	   success: function(date){
	   		if(date == "success"){
	   			parent.$.easyui.messager.show({title: "操作提示", msg: "ASN未保存！", position: "bottomRight"});
	   			return;
	   		}else{
					$.ajax({
	  					 type: "GET",
	  					 url: "${ctx}/bis/asn/importinfo",
	  					 data: {"asnId":asnId,"linkId":linkId,"ctnNum":ctnNum},
	   					 dataType: "text",
	   					 success: function(date){
	   						if(date=="success"){
	   							parent.$.easyui.messager.show({title: "操作提示", msg: "导入成功！", position: "bottomRight"});
	   							gridDG();
	   						}else if(date=="nostate"){
	   							parent.$.easyui.messager.show({title: "操作提示", msg: "只能对在架状态的ASN进行操作！", position: "bottomRight"});
	   						}else if(date=="exist"){
	   							parent.$.easyui.messager.show({title: "操作提示", msg: "此ASN已存在明细，无法进行导入操作！", position: "bottomRight"});
	   						}else{
								parent.$.easyui.messager.show({title: "操作提示", msg: "对应入库联系单无明细，导入失败！", position: "bottomRight"});	   		
	   						}
	 					 }
					});
	   		}
	   }
	});
}

//下载
function down(){
	var url = "${ctx}/bis/asninfo/download";
	$("#downForm").attr("action",url).submit();
}

//导入
function into(){
	var asnId = "${asn.asn}";
$.ajax({
		type:'get',
		url: "${ctx}/bis/asn/ifsave/"+asnId,
		dataType:"text",
		success: function(data){
			if(data == "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "ASN未保存！", position: "bottomRight"});
				return;
			}else{
				intoc(asnId);
			}
		}
	});	
}

function intoc(asnId){
	d=$("#dlg").dialog({   
	    title: "ASN明细导入",    
	    width: 450,    
	    height: 450,    
	    href:'${ctx}/bis/asninfo/into/'+asnId,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$("#mainform3").submit(); 
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	}); 
}

/*关联装车单取装车时间*/
function withLoading(){
	dloading = $("#dlgloading").dialog({
            title: '关联装车单选择',
            width: 650,
            height: 500,
            href: '${ctx}/bis/asn/loadingList',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    var rows = $('#loadingdg').datagrid('getSelected');
                    $('#inboundTime').datebox('setValue',rows.loadingTime);
                    dloading.panel('close');
                }
            }, {
                text: '取消',
                handler: function () {
                    dloading.panel('close');
                }
            }]
        });
}


function refresh(){
	window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
}
</script>
</body>
</html>