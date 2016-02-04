<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>)request.getAttribute("result");
List<Map<String, Object>> groups = (List<Map<String, Object>>) result.get("emailGroups"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<style type="text/css">
#content {width: 100%}
#group {width: 18%; float: left;}
#groupEmail {width: 80%; float: right;}
</style>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>

<script type="text/javascript">
	function loadEmails(id, name) {
		$("#groupEmail").load("<%=basePath%>manager/findEmailByGroup.do?groupId="+id);
		$("#groupId").val(id);
		$("#groupName_email").val(name);
	}
	function addGroup() {
		var groupName = $("#groupName").val();
		$.post('<%=basePath%>manager/addEmailGroup.do', {"groupName":groupName}, function(data) {
			window.location.reload(true);
		});
	}
	function addEmail() {
		var groupId = $("#groupId").val();
		var email = $("#email").val();
		var emailName = $("#emailName").val();
		$.post('<%=basePath%>manager/addEmailInGroup.do', {"groupId":groupId, "email":email, "emailUser":emailName}, function(data) {
			window.location.reload(true);
		});
	}
</script>
</head>
<body>
<table>
	<tr>
		<td><input type="text" id="groupName"></td>
		<td><button onclick="addGroup()">添加组</button></td>
	</tr>
</table>
<table>
	<tr>
		<input type="hidden" id="groupId">
		<td><input type="text" id="groupName_email"></td>
		<td><input type="text" id="email" value="email"></td>
		<td><input type="text" id="emailName" value="昵称"></td>
		<td><button onclick="addEmail()">添加邮件</button></td>
	</tr>
</table>
<div id="content">
	<div id="group">
		<ul>
<%for(Map<String, Object> group : groups) {%>
  			<li><a href="javascript:loadEmails(<%=group.get("id")%>, '<%=group.get("name")%>')"><%=group.get("name") %></a></li>  
<%} %>		
		</ul>
	</div>
	<div id="groupEmail">
		欢迎来到管理员邮件管理中心！
	</div>
</div>


</body>
</html>