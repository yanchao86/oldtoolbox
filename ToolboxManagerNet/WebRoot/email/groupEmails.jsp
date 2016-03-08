<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>)request.getAttribute("result");
List<Map<String, Object>> emails = (List<Map<String, Object>>) result.get("emailInGroup"); 
%>
<ul>
<%for(Map<String, Object> email : emails) {%>
	<li><%=email.get("id") %> <%=email.get("name") %>:<%=email.get("email") %></li>  
<%} %>		
</ul>