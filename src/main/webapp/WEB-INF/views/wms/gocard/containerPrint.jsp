<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center',title:'集装箱出门证打印浏览'">
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
			<td></td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
		    <td colspan='4'>&nbsp</td>
	        <td colspan='4'>${obj.carNum}</td>
	        <td colspan='6'>&nbsp</td>
	        <td colspan='3' >
	        	<p id="nowTime"></p>
	        </td>
	        <td colspan='2'>&nbsp</td>
	        <td colspan='3'>${appearTime}</td>
	    </tr>
		<tr>
			<td></td>
		</tr>
		<tr>
		    <td colspan='4'>&nbsp</td>
		    
		    <td colspan='1'>&nbsp</td>
			<%-- <td colspan='9' align='center'>${obj.billNum}</td> --%>
		</tr>
		<tr>
			<td></td>
		</tr>
	    <tr>
		    <td colspan='3'>&nbsp</td>
		    <td colspan='7' >${obj.shipNum}</td>
	        <td colspan='2' align='right'>${xianghao}</td>
			
	        <td colspan='7' align='center'>${obj.ctnNumOne}</td>
        
	    </tr>
	    <tr>
		    <td colspan='3'>&nbsp</td>
		    <td colspan='7' >${obj.shipNum}</td>
	        <td colspan='2' align='right'>${xianghao}</td>

	        <td colspan='7' align='center'>${obj.ctnNumTwo}</td>
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
			<td></td>
		</tr>
	    <tr>
		<td colspan='16'>&nbsp</td>
        <td colspan='9'>50 </td>
	    </tr>
	    
	</table>
</div>
</div>

<script type="text/javascript">
function print(){
   $("#home").jqprint();
}

/*$(function(){
	var curr_time = new Date();
		var strDate = curr_time.getFullYear()+"";
		strDate += (curr_time.getMonth()+1).length>1?curr_time.getMonth()+1:"0"+(curr_time.getMonth()+1);
		strDate += curr_time.getDate();
	    //$("#nowTime").datebox("setValue",strDate);
		//$("#nowTime").html(strDate);
});*/
</script>
</body>
</html>
