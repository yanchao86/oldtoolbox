<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.constant.GameSuspend"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%@page import="com.pixshow.framework.utils.ListUtiltiy"%>
<%@page import="com.pixshow.toolboxmgr.bean.GameSuspendBean"%>
<%@page import="com.pixshow.toolboxmgr.bean.ToolboxBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<ToolboxBean> toolboxs = (List<ToolboxBean>) result.get("toolboxs");
Map<Integer, ToolboxBean> toolboxMap = ListUtiltiy.groupToObject(toolboxs, "id");
List<GameSuspendBean> gameSuspends = (List<GameSuspendBean>) result.get("gameSuspends");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
<script type="text/javascript">
function restoreSuspend(id) {
	$.post("<%=basePath%>manager/restoreSuspend.do", {"id":id}, function(data) {
		window.location.reload(true);
	});
}
</script>
</head>
<body>
<table id="customers">
	<tr>
		<th>序号</th>
		<th>暂停类型</th>
		<th>
			<table width="100%">
				<tr>
					<th>横屏图</th>
					<th>竖屏图</th>
					<th>图片链接</th>
					<th>链接类型</th>
				</tr>
				<tr>
					<th colspan="3" style="text-align: center;">游戏推荐</th>
				</tr>
			</table>
		</th>
		<th>操作</th>
	</tr>
<%for(GameSuspendBean bean : gameSuspends) {
    bean.getContent();
%>
  	<tr>	
  		<td><%=bean.getId() %></td>
		<td>
		<%if(bean.getSuspendType()==GameSuspend.suspendType.isPic) {%>
			只有图片
		<%} else {%>
			游戏推荐
		<%} %>
		</td>
		<td>
			<table>
		<%if(bean.getSuspendType()==GameSuspend.suspendType.isPic) {
			JSONObject json = JSONObject.fromObject(bean.getContent());
		%>
				<tr>
					<td><img src="<%=json.optString("widthPic") %>" width="120px" height="80px"></td>
					<td><img src="<%=json.optString("highPic") %>" width="80px" height="120px"></td>
					<td><%=json.optString("picUrl") %></td>
					<td>
						<%switch(json.optInt("picType")) {
					    	case GameSuspend.buttonType.url:
					    	    out.print("普通链接");
					    	    break;
					    	case GameSuspend.buttonType.info:
					    	    out.print("游戏详情");
					    	    break;
					    	case GameSuspend.buttonType.download:
					    	    out.print("直接下载");
					    	    break;
					    	default:out.print("无");
					    }%>					
					</td>
				</tr>
		<%} else {
		    out.print("<tr>");
			JSONArray arr = JSONArray.fromObject(bean.getContent());
			for(int i=0; i<arr.size(); i++) {
			    ToolboxBean box = toolboxMap.get(arr.getInt(i));%>
				<td>
					<img width="100" height="100" src="<%=ImageStorageTootl.getUrl(box.getIcon()) + "?" + System.currentTimeMillis()%>">
					<br>
					<%=box.getName() %>
					<br>
					<%=box.getPackageName() %>
				</td>
			<%}
			out.print("</tr>");
		}%>
			</table>
		</td>
		<td>
			<a href="javascript:restoreSuspend(<%=bean.getId() %>)">还原</a>
		</td>
  	</tr>  
<%} %>
</table>
</body>
</html>