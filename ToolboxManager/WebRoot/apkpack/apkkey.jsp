<%@page import="com.pixshow.apkpack.bean.ApkKeyBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<ApkKeyBean> apkKeys = (List<ApkKeyBean>) result.get("apkKeys");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>密钥上传</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
<script type="text/javascript">
	function delKey(id) {
		
	}
</script>
</head>
<body>

密钥配置
<form action="<%=basePath%>manager/apkKey.do" method="post" enctype="multipart/form-data">
<table id="customers">
	<tr>
		<th>名称</th>
		<td><input type="text" name="name"></td>
	</tr>
	<tr>
		<th>公钥</th>
		<td><input type="file" name="publicKey"></td>
	</tr>
	<tr>
		<th>私钥</th>
		<td><input type="file" name="privateKey"></td>
	</tr>
	<tr>
		<td colspan="2"><button type="submit">上  传</button></td>
	</tr>
</table>	
</form>

<table id="customers">
	<tr>
		<th>序号</th>
		<th>名称</th>
		<th>文件</th>
		<th>创建时间</th>
		<th>操作</th>
	</tr>
<%for(ApkKeyBean key : apkKeys) {%>
	<tr>
		<td><%=key.getId() %></td>
		<td><%=key.getName() %></td>
		<td>
			<a href="<%=key.getPublicKeyFile()%>">公钥</a>
			<a href="<%=key.getPrivateKeyFile()%>">私钥</a>
		</td>
		<td><%=key.getCreateDate() %></td>
		<td><a href="javascript:delKey(<%=key.getId()%>)">删除</a></td>
	</tr>
<%}%>
</table>
</body>
</html>