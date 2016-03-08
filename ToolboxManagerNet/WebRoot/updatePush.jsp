<%@page import="com.pixshow.framework.config.Config"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
<script type="text/javascript">
	function push() {
		if(confirm("确定推送么？")) {
			window.location.href="<%=basePath%>manager/updatePush.do";
		}
	}
	<%%>
	function pushData() {
		if(confirm("确定推送么？")) {
			$.post("<%=basePath%>manager/pushData.do", {"url":"<%=Config.getInstance().getString("toolbox.serviceUrl")%>service/appConfigArray.do"}, function(data) {
				if(data.result == 0) {
					alert("推送成功！");
				} else {
					alert("推送失败，error："+data.data.error);
				}
			});
		}
	}
	
</script>
</head>
<body>
	工具箱、工具栏更新推送。<hr />
	<button onclick="push()">旧版本推送</button>
	<button onclick="pushData()">新版推送（可随便点）</button>
</body>
</html>