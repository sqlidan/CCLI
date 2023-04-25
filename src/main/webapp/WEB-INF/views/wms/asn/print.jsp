<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.haiersoft.ccli.wms.entity.BisAsnInfo"%>
<%@page import="com.haiersoft.ccli.wms.entity.BisAsn"%>
<%@page import="java.util.List"%>
<%
	String path=application.getRealPath(request.getRequestURI());
	int getlastNum=path.lastIndexOf("webapp");
	//path=path.substring(0,getlastNum)+"webapp\\static\\barcode_file\\";
	path=path.substring(0,getlastNum)+"webapps\\ccli\\static\\barcode_file\\";
	List<BisAsnInfo> getList=(List<BisAsnInfo>)request.getAttribute("infolist");
	BisAsn getAsnObj=(BisAsn)request.getAttribute("asn");
	if(getAsnObj==null){
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script >
 var path='<%=path %>';
</script>
<script src="${ctx}/static/plugins/print/LodopFuncs.js"></script>
</head>
<body>
<!-- <div class="my_hidden" style="margin-bottom: 5px;"> 
	<a href="javascript:void(0)" class="easyui-linkbutton"  id="print" iconCls="icon-print">打印</a>
</div>  -->
    <div style="padding:5px;height:auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true"
           onclick="print()">打印</a>
    </div>
  <!--   <div id="home" class="tab-pane"> -->
<div class="tab-pane" id="printdiv">
	<table>
		<thead>
			 <tr><td colspan="6">入库ASN</td></tr>
			 <tr>
				<td class="skudesc" colspan="2">ASN号码：${asn.asn}</td>
				<td ></td><td></td>
				<td colspan="2" rowspan="2" style="height:60px;" id="asn"> 
					<img src="/ccli/static/barcode_file/<%=com.haiersoft.ccli.common.utils.BarcodeUtil.generateFile(getAsnObj.getAsn(), path,false) %>"   width="300px" height="60px" /> 
				</td>
			</tr>
			<tr>
				<td class="skudesc" colspan="2">提单号：${asn.billNum}</td> 
				<td></td><td></td>
			</tr>
			<tr>
				<td class="skudesc" colspan="2">是否保税：${isBonded}</td> 
				<td></td>
				<td class="skudesc" colspan="2">是否MSC：${ifmsc}</td> 
			</tr>
			<tr >
				 <td class="trlink" colspan="6"></td>
			</tr>
			<tr class="th">
				<td>商品编码</td>
				<td>MR/Container No.</td> 
				<td>有效期</td> 
				<td></td> 
				<td>应收数量</td> 
				<td>上架类型</td>   
			</tr>
		</thead>
		<tbody>
			<%
			BisAsnInfo asninfo=null;
			if(getList!=null && getList.size()>0){
				for(int i=0;i<getList.size();i++){ 
					asninfo=getList.get(i);
					String a = asninfo.getPiece().toString();
					asninfo.setPiecein(a.substring(0,a.length()-2));
			%>
			<tr class="tdb" style="page-break-after:auto;">
				<td><%=asninfo.getSkuId() %></td>
				<td>${asn.ctnNum }</td> 
				<td style="width:100px;"><fmt:formatDate value="<%=asninfo.getValidityTime() %>"/></td> 
				<td></td> 
				<td class="special" ><%=asninfo.getPiecein() %>箱</td> 
				<td></td>   
			</tr>
			<tr class="skuimgtr"  style="page-break-after:auto;">
				<td class="skuimg" colspan="2" style="height:60px;" id="${asninfo.skuId }"> 
					<img src="/ccli/static/barcode_file/<%=com.haiersoft.ccli.common.utils.BarcodeUtil.generateFile(asninfo.getSkuId(), path,false) %>" width="50%" msg="<%=asninfo.getSkuId() %>" height="60px" /> 
				</td> 
				<td class="skudesc" colspan="2"></td>
				<td class="skudesc" colspan="2">货物描述：<%=asninfo.getCargoName() %></td>   
			</tr>
			<%}} %>
		</tbody>
		<tfoot>
  			<tr >
				 <td colspan="6" style="height:10px"></td>
			</tr>
			<tr>
  				<td colspan="2">库管员签名</td>
				<td colspan="2">理货员签名</td>
				<td colspan="2" style="text-align:right;">
					第<font tdata="PageNO"   color="blue">##</font>页/共<font tdata="PageCount"  color="blue">##</font>页
				</td>  
			</tr>
		</tfoot>
	</table>
<style type="text/css">
	.my_show{
	width:1487px;overflow-y:false;
/* 	border-style:solid;border-width:1px;border-color:#000 */
	}
	.tdb{font-weight:700;}
	.special{font-weight:700;font-size:30px;text-align:center;}
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
/*   	tr{border:1px solid #999999;}   */
/*   	td{border:1px solid #999999;}   */
</style>
</div>
		
		 
			
		  
		 
<script type="text/javascript">
/* $(document).ready(function() { 
	$("#print").click(function(){ 
			LODOP=getLodop();  
			LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_整页表格");
			LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
			LODOP.ADD_PRINT_TABLE("2%","1%","96%","98%",document.getElementById("printdiv").innerHTML);
			LODOP.SET_PREVIEW_WINDOW(0,0,0,1000,700,"");
			LODOP.PREVIEW();		
	}) 
});  */
function print() {
    $("#printdiv").jqprint();
}
</script>
</body>
</html>