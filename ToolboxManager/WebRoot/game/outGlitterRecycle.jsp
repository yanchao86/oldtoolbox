<%@page import="com.pixshow.toolboxmgr.bean.GameOutGlitterBean"%>
<%@page import="com.pixshow.constant.GameGlitter"%>
<%@page import="com.pixshow.framework.utils.DateUtility"%>
<%@page import="com.pixshow.toolboxmgr.bean.GameGlitterBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<GameOutGlitterBean> gameGlitters = (List<GameOutGlitterBean>) result.get("gameGlitters");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
<script type="text/javascript">
function restoreGlitter(id) {
	$.post("<%=basePath%>manager/restoreOutGlitter.do", {"id":id}, function(data) {
		window.location.reload(true);
	});
}
</script>
</head>
<body>

<table id="customers">
	<tr>
		<th>优先级</th>
		<th>图片</th>
		<th>按钮名称</th>
		<th>按钮链接</th>
		<th>执行时间</th>
		<th>操作</th>
	</tr>
<%
    for(GameOutGlitterBean bean : gameGlitters) {
%>
	<tr>
		<td><%=bean.getIndexNum()%></td>
		<td><img src="<%=bean.getPicture()%>"></td>
		<td>
		<%
		    out.print(bean.getButtonName()+"【");
		    String buttonType = "";
		    switch(bean.getButtonType()) {
		    	case 0:
		    	    out.print("----");
		    	    break;
		    	case 1:
		    	    out.print("应用类：下载/安装");
		    	    break;
		    	case 2:
		    	    out.print("资讯类：去看看");
		    	    break;
		    	default:out.print("----");
		    	    
		    }
		    out.print("】");
		%>
		</td>
		<td><%=bean.getButtonUrl()%></td>
		<td><%=DateUtility.format(bean.getStartTime(), "yyyy-MM-dd") +" - "+DateUtility.format(bean.getEndTime(), "yyyy-MM-dd")%></td>
		<td>
			<a href="javascript:restoreGlitter(<%=bean.getId() %>)">还原</a>
		</td>
	</tr>
<%}%>
</table>
</body>
</html>