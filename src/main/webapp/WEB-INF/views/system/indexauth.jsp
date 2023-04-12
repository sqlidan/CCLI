<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title></title>
<style type="text/css">
        body {
            opacity: 0;
        }
</style>	
</head>
<body>
	auth login ...


 <script type="text/javascript">
	//document.cookie="x-access-token="+"${access_token}"+";path=/;domain=.sdland-sea.com; Max-Age=86400";
	isExistToken=document.cookie.indexOf("x-access-token="); 
	if(isExistToken == -1)
	{
		document.cookie="x-access-token="+"${accessToken}"+";path=/;domain=.sdland-sea.com; Max-Age=86400";
	}
	/* 单点登录配置 */
	/*  window.location.href="/"; */
	</script>
	<%-- <jsp:forward page="/index.jsp"></jsp:forward> --%>

</body>
</html>