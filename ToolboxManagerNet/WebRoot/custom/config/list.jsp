<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<Map<String, Object>> defList = (List<Map<String, Object>>) result.get("defList");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
  </head>
  
  <body>
    <table id="customers" width="98%">
    	<tr>
    		<th>表名</th>
    		<th>描述</th>
    		<th>更改设置</th>
    	</tr>
    <%for(int i=0; i<defList.size(); i++) {
        Map<String, Object> def = defList.get(i);%>
        <tr>
        	<td><%=def.get("code") %></td>
        	<td><%=def.get("name") %></td>
        	<td><a href="<%=basePath%>manager/customInfo.do?code=<%=def.get("code")%>">编辑</a></td>
        </tr>
    <%}%>
    </table>
  </body>
</html>
