<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="com.pixshow.framework.utils.CipherUtility"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String content = request.getParameter("content");
String decContent = "";
if(StringUtility.isNotEmpty(content)) {
    decContent = CipherUtility.AES.decrypt(content, "dsjkfh824hnlkdfnmvo");
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
</head>
<body>

	<form action="" method="post">
		<table id="customers">
			<tr>
				<td><textarea rows="10" cols="20" name="content"><%=content==null?"":content %></textarea></td>		
				<td><input type="submit" value="解密" /></td>		
			</tr>
		</table>
	</form>

	<table id="customers">
		<tr>
			<th>解密后</th>
		</tr>
		<tr>
			<td><%=decContent==null?"没解出来!":decContent %></td>
		</tr>
	</table>
</body>
</html>