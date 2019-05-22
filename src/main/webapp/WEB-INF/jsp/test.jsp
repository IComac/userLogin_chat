<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>???????</title>
<script type="text/javascript">
	function ajaxReq(){
		xmlHttp = new XMLHttpRequest();
		xmlhttp.open("POST","",true);
		xmlhttp.send();
	}
</script>
</head>
<body>
<input type="button" value="请求是否是ajax请求" onclink="ajaxReq()"><br/>
<span id="rs"></span>
</body>
</html>