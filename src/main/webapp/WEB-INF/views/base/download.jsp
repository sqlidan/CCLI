<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
</div>
<div data-options="split:true,border:false" title="明细"  style="height:200px">
		<form id="downForm" method="post" >
		</form>
		<div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
			<div>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="down(1)">下载打印控件</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		<!--	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="down(2)">下载打印控件(64位)</a>
			<span class="toolbar-item dialog-tool-separator"></span> -->
	    	</div>
		</div>
</div>

<script type="text/javascript">

//下载
function down(state){
	var url = "${ctx}/base/down/download/"+state;
	$("#downForm").attr("action",url).submit();
}

</script>
</body>
</html>