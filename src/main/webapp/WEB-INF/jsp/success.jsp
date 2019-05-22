<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>success</title>
</head>
<body>

	<div>
		<strong> welcome,${sessionScope.user.username}! </strong>
	</div>
	this is success page!

	<a href="/userLogin_ssm/user/anotherpage">点我跳到另一个页面</a>

	<form action="/userLogin_ssm/user/outLogin">
		<table>
			<tr>
				<td><input type="submit" value="退出登录"></td>
			</tr>
		</table>
	</form>
</body>
</html>
