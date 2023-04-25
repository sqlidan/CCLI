<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center',title:'散杂货出门证打印浏览'">
		<div style="padding:5px;height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印</a>
		</div>
<div id="home" class="tab-pane" >
	<table>
	    <tr>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
	    <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
        <td width=20>&nbsp</td>
	    </tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td></td>
		</tr>
		
		<tr>
			<td colspan='10' align='center'>${obj.carNum}</td>
		    <td colspan='7'>&nbsp</td>
			<td colspan='1'></td>	
			<td colspan='1'>&nbsp&nbsp&nbsp${year}${month}${day}</td>
			<td colspan='2' clign='center'></td>
		</tr>
		<tr>
			<td></td>
		</tr>
	    <tr>
		    <td colspan='3'>&nbsp</td>
	        <td colspan='9' align='center'>${obj.takeName}</td>
	        <td colspan='2'>&nbsp</td>
	        
	    </tr>
	    
	    <tr>
		    <td colspan='3'></td>
	    </tr>
	    <tr>
			<td></td>
		</tr>
				<tr>
			<td></td>
		</tr>
		<tr>
			<td></td>
		</tr>
		
	    <tr>
	    	<td colspan='2'>&nbsp</td>
		    <td colspan='8' >${obj.shipNum}</td>
	    	<td colspan='3' >${obj.billNum}</td>
	        <td colspan='4' align='right'>${obj.cargoNameOne}</td>
	        <td colspan='3' align='center'>${obj.packTypeOne}</td>
	        <td colspan='2' align='center'>${obj.pieceOne}</td>
	        <td colspan='3' >${obj.cargoWeightOne}</td>
	    </tr>
	    
	    <tr>
	     	<td colspan='2'>&nbsp</td>
		    <td colspan='8' >${obj.shipNum}</td>
	    	<td colspan='3' >${obj.billNum}</td>
	        <td colspan='4' align='right'>${obj.cargoNameTwo}</td>
	        <td colspan='3' >${obj.packTypeTwo}</td>
	        <td colspan='2' align='center'>${obj.pieceTwo}</td>
	        <td colspan='3' >${obj.cargoWeightTwo}</td>
	    </tr>
	    <tr>
			<td></td>
		</tr>
		<tr>
			<td></td>
		</tr>
		
		<tr>
			<td></td>
		</tr>
	    <tr>
			<td colspan='10' align='center'>${obj.takeMan}</td>
		</tr>
		<tr>
			<td colspan='10'>${obj.remark}</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td colspan='18'>&nbsp</td>
			<td colspan='3' >50</td>
		</tr>
	    <tr>
			<td colspan='16'>&nbsp</td>
			<td colspan='3' align='center'>${user}</td>
			<td colspan='2'>&nbsp</td>	
			<!-- <td colspan='1'>&nbsp&nbsp&nbsp${hour}&nbsp&nbsp${minute}</td> -->
		</tr>
	</table>
</div>
</div>

<script type="text/javascript">
function print(){
$("#home").jqprint();
}
</script>
</body>
</html>
