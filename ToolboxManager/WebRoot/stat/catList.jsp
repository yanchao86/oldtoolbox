<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<Map<String, Object>> allCats = new ArrayList<Map<String, Object>>(); 
if(result != null) {
    allCats = (List<Map<String, Object>>) result.get("allCats");
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	<script type="text/javascript">
		function del(id) {
			if(confirm("删除该分类后，下面的子目录也将会删除！")) {
				window.location.href="<%=basePath%>manager/deleteStatCat.do?catId="+id;
			}
		}
	</script>
</head>
<body>

<form action="<%=basePath %>manager/addStatCat.do" method="post">
	<table id="customers">
		<tr>
			<td><input type="text" name="catName"></td>		
			<td><button>添加</button></td>		
		</tr>
	</table>
</form>

	<table id="customers">
		<tr>
			<th>名称</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
	<%for(Map<String, Object> map : allCats) {%>
		<tr>
			<td><%=map.get("name") %></td>
			<td><%=map.get("createDate") %></td>
			<td>
				<a href="<%=basePath%>manager/statCatCodeList.do?catId=<%=map.get("id") %>">查看</a> | 
				<a href="javascript:del(<%=map.get("id") %>)">删除</a>
			</td>
		</tr>
	<%} %>
	</table>
</body>
</html>