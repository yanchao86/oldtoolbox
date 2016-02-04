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
List<GameGlitterBean> gameGlitters = (List<GameGlitterBean>) result.get("gameGlitters");
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
	$.post("<%=basePath%>manager/restoreGlitter.do", {"id":id}, function(data) {
		window.location.reload(true);
	});
}
</script>
</head>
<body>

<table id="customers">
	<tr>
		<th>序号</th>
		<th>闪屏类型</th>
		<th>横屏图</th>
		<th>竖屏图</th>
		<th>留存时间</th>
		<th>按钮名称</th>
		<th>按钮链接</th>
		<th>执行时间</th>
		<th>操作</th>
	</tr>
<%
    for(GameGlitterBean bean : gameGlitters) {
%>
	<tr>
		<td><%=bean.getId()%></td>
		<td>
		<%
		    if(bean.getGlitterType()==0) {
		%>
			默认闪屏
		<%
		    } else {
		%>
			节日闪屏
		<%
		    }
		%>
		</td>
		<td><img src="<%=bean.getWidthPic()%>" width="120px" height="80px"></td>
		<td><img src="<%=bean.getHighPic()%>" width="80px" height="120px"></td>
		<td><%=bean.getDwellTime()%>秒</td>
		<td>
		<%
		    if(bean.getGlitterType()==GameGlitter.glitterType.isHoliday) {
						    out.print(bean.getButtonName()+"-");
						    String buttonType = "";
						    switch(bean.getButtonType()) {
						    	case GameGlitter.buttonType.url:
						    	    out.print("普通链接");
						    	    break;
						    	case GameGlitter.buttonType.info:
						    	    out.print("游戏详情");
						    	    break;
						    	case GameGlitter.buttonType.download:
						    	    out.print("直接下载");
						    	    break;
						    	default:out.print("无");
						    	    
						    }
						}
		%>
		</td>
		<td><%=bean.getGlitterType()==GameGlitter.glitterType.isHoliday?bean.getButtonUrl():""%></td>
		<td><%=bean.getGlitterType()==GameGlitter.glitterType.isHoliday?DateUtility.format(bean.getStartTime(), "yyyy-MM-dd") +" - "+DateUtility.format(bean.getEndTime(), "yyyy-MM-dd"):""%></td>
		<td>
			<a href="javascript:restoreGlitter(<%=bean.getId() %>)">还原</a>
		</td>
	</tr>
<%}%>
</table>
</body>
</html>