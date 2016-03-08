<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String basePath = WebUtility.getBasePath(request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>My JSP 'customConfig.jsp' starting page</title>
</head>

<body>
	<form action="<%=basePath%>customConfig" method="post" enctype="multipart/form-data">
		<input type="text" id="name1" name="name1" />
		<input type="file" id="image1" name="image1" />
		<input type="submit" value="提交"/>
	</form>
</body>
</html>
