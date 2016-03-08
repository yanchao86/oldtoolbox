<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.pixshow.framework.utils.DateUtility"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<String> apps = (List<String>) result.get("apps");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	
	<script type="text/javascript">
		function loadUrl(url) {
			$("#content").attr("src", url);
		}
	</script>
</head>
<body>
<a href="javascript:window.history.back()">返回</a>
	
	<table id="customers">
		<tr>
	<%for(int i=0;i<apps.size();i++) {
	    String code = apps.get(i);
	    JSONObject json = JSONObject.fromObject(code);
	%>
			<th colspan="2" align="center"><%=json.opt("name")%></th>
	<%} %>
		</tr>
		<tr>
	<%for(int i=0;i<apps.size();i++) {
	    String code = apps.get(i);
	    JSONObject json = JSONObject.fromObject(code);
	%>
			<td>
				<a href="javascript:loadUrl('<%=basePath%>manager/appDownloadStat.do?code=<%=json.opt("packageName")%>@frame_download_start_num')">下载开始</a>
			</td>
			<td>
				<a href="javascript:loadUrl('<%=basePath%>manager/appDownloadStat.do?code=<%=json.opt("packageName")%>@frame_download_finish_num')">下载完成</a>
			</td>
	<%} %>
		</tr>
	</table>
	<iframe id="content" name="content" scrolling="no" width="100%" frameborder="0" onload="this.height=this.contentWindow.document.documentElement.scrollHeight"></iframe>
	
</body>
</html>