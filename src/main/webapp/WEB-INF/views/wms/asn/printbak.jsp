<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/print/LodopFuncs.js"></script>
</head>
<body>
<div class="my_hidden" style="margin-bottom: 5px;"> 
	<a href="javascript:void(0)" class="easyui-linkbutton"  id="print" iconCls="icon-print">打印</a>
</div> 
<div class="my_show" id="printdiv">
	<table>
		<thead>
			 <tr><td colspan="6">入库ASN</td></tr>
			 <tr>
				<td class="skudesc" colspan="1">ASN号码：${asn.asn}</td> <td></td>
				<td ></td><td></td>
				<td colspan="2" rowspan="2" style="height:100px;" id="asn">
					<img src="${ctx }/barcode?msg=${asn.asn}&BARCODE_TYPE=CODE128&fmt=png"   width="300px" height="100px" /> 
				</td>
			</tr>
			<tr>
				<td class="skudesc" colspan="1">提单号：${asn.billNum}</td> <td></td>
				<td></td><td></td>
			</tr>
			<tr >
				 <td class="trlink" colspan="6"></td>
			</tr>
		</thead>
		<tbody>
			<tr class="th">
				<td>商品编码</td>
				<td>MR/Container No.</td> 
				<td>有效时期</td> 
				<td>标准托盘数量</td> 
				<td>应收数量</td> 
				<td>上架类型</td>   
			</tr>
			<c:forEach items="${infolist}" var="asninfo">
				<tr class="tdb" style="page-break-after:auto;">
				<td>${asninfo.skuId }</td>
				<td>${asn.ctnNum }</td> 
				<td><fmt:formatDate value="${asninfo.validityTime }"/></td> 
				<td></td> 
				<td class="special">${asninfo.piece }箱</td> 
				<td></td>   
				</tr>
				<tr class="skuimgtr"  style="page-break-after:auto;">
				<td class="skuimg" colspan="2" style="height:120px;" id="${asninfo.skuId }">
					<img src="${ctx }/barcode?msg=${asninfo.skuId }&BARCODE_TYPE=CODE128&fmt=png&hrp=none" width="100%" msg="${asninfo.skuId }" height="100px" /> 
				</td> 
				<td class="skudesc" colspan="2"></td>
				<td class="skudesc" colspan="2">货物描述：${asninfo.skuDescribe }</td>   
				</tr>
  			</c:forEach>
		</tbody>
		<tfoot>
  			<tr >
				 <td colspan="6" style="height:15px"></td>
			</tr>
			<tr>
  				<td></td>
				<td>库管员签名</td><td></td>
				<td></td>
				<td>理货员签名</td><td></td>  
			</tr> 
		</tfoot>
	</table>
<style type="text/css">
	.my_show{
	width:1487px;overflow-y:false;
/* 	border-style:solid;border-width:1px;border-color:#000 */
	}
	.tdb{font-weight:700;}
	.special{font-weight:700;font-size:30px;}
	table{width:100%;font-size:16px;}
	thead tr td{
	text-align:center;
	font-size:30px;font-weight:700;
	}
	.th td{
	font-size:20px;
	font-weight:700;
	border:0px;
	border-bottom: #000 1px solid;
	}
	.trlink{
		border-bottom: #000 2px solid;
	}
	tbody .skuimg{text-align:center;border-bottom: #000 1px solid;}
	.skudesc{font-size:30px;font-weight:700;}
	.skuimgtr .skudesc{border-bottom: #000 1px solid;}
	tfoot tr{
		font-size:14px;
		font-weight:700;
	}
/* 	tr{border:1px solid #999999;} */
/* 	td{border:1px solid #999999;} */
</style>
</div>
		
		 
			
		  
		 
<script type="text/javascript">
$(document).ready(function() { 
	$("#print").click(function(){ 
		//$(".my_show").jqprint(); 
			$("#asn").html("");
			var skuList= new Array();
			$(".skuimg img").each(function (i){
				skuList[i]=$(this).attr("msg");
				$(this).parent().html("");
			});
			LODOP=getLodop();  
			LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_整页表格");
			LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
			LODOP.ADD_PRINT_TABLE("2%","1%","96%","98%",document.getElementById("printdiv").innerHTML);
			LODOP.ADD_PRINT_BARCODE(50,700,300,90,"128Auto","${asn.asn}");
			LODOP.SET_PRINT_STYLEA(0, "ShowBarText", 0);
			$(".skuimg").each(function (i){
				var x = $(this).offset().top; 
				var y= $(this).offset().left;
				LODOP.ADD_PRINT_BARCODE(x+Math.floor(i/3)*210,y,500,100,"128Auto",skuList[i]);
				LODOP.SET_PRINT_STYLEA(0, "ShowBarText", 0);
			});
			
			LODOP.SET_PREVIEW_WINDOW(0,0,0,800,600,"");
			LODOP.PREVIEW();				 
	}) 
}); 

</script>
</body>
</html>