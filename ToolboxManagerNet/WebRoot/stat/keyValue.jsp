<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<Map<String, Object>> keyValues = (List<Map<String, Object>>) result.get("keyValues");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	
<script type="text/javascript">
	function codeChange(id) {
		$("#code_change_"+id).hide();
		$("#code_save_"+id).show();
	}
	function codeSave(id) {
		var name=$("#key_input_"+id).val();
		var value=$("#value_input_"+id).val();
		$.post("<%=basePath%>manager/updateKeyValue.do?=", {"id":id, "name":name, "value":value}, function(data) {
			window.location.reload(true);
		});
	}	
	function del(id) {
		if(confirm("确定删除 id="+id+"？")) {
			window.location.href="<%=basePath%>manager/deleteKeyValue.do?id="+id;
		}
	}
</script>	
</head>
<body>
<a href="javascript:window.history.back()">返回</a>
<form action="<%=basePath %>manager/saveKeyValue.do" method="post">
	<table id="customers">
		<tr>
			<th>code</th>
			<th>描述</th>
			<th>添加</th>
		</tr>
		<tr>
			<td><input type="text" name="name"></td>		
			<td><input type="text" name="value" ></td>
			<td><button>添加</button></td>		
		</tr>
	</table>
</form>

	<table id="customers">
		<tr>
			<th>序号</th>
			<th>code</th>
	    	<th>描述</th>
	    	<th>操作</th>
		</tr>
	<%for(int i=0;i<keyValues.size();i++) {
	    Map<String, Object> keyValue = keyValues.get(i);
	%>
		<tr>
			<td><%=keyValue.get("id") %></td>
			<td><%=keyValue.get("name") %></td>
			<td>
				<div id="code_change_<%=keyValue.get("id")%>">
					<font style="color: red; font-style: italic; font-size: 12px;"><%=keyValue.get("value") %></font>
					<a style="color: gray; font-style: italic; font-size: 12px;" href="javascript:codeChange(<%=keyValue.get("id")%>)">修改</a>
				</div>
				<div id="code_save_<%=keyValue.get("id")%>" style="display: none;">
					<input type="hidden" value="<%=keyValue.get("name") %>" id="key_input_<%=keyValue.get("id")%>">
					<input type="text" value="<%=keyValue.get("value") %>" id="value_input_<%=keyValue.get("id")%>" size="60">
					<a style="color: gray; font-style: italic; font-size: 12px;" href="javascript:codeSave(<%=keyValue.get("id")%>)">保存</a>
				</div>
			</td>
			<td><a href="javascript:del(<%=keyValue.get("id")%>)">删除</a></td>
		</tr>
	<%} %>
	</table>
	<br />
	<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto;"></div>
</body>
</html>