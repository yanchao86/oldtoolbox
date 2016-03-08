<%@page import="com.pixshow.constant.GameGlitter"%>
<%@page import="com.pixshow.toolboxmgr.bean.GameOutGlitterBean"%>
<%@page import="com.pixshow.framework.utils.DateUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
GameOutGlitterBean gameGlitter = (GameOutGlitterBean) result.get("gameGlitter");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
<script type="text/javascript"src="<%=basePath%>plugin/dialog/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>plugin/date/lhgcalendar.min.js"></script>
<script type="text/javascript">
$(function() {
	$("#startTime").calendar({ format:'yyyy-MM-dd' });
	$("#endTime").calendar({ format:'yyyy-MM-dd' });
});

function glitterUse(id) {
	$.post("<%=basePath%>manager/updatOutGlitterUseType.do", {"id":id}, function(data) {
		alert(data.data.result);
		window.location.reload(true);
	});
}

function delGlitter(id, useType) {
	var msg = "确定删除正在启用的闪屏么？";
	if(useType == 0) {
		msg = "确定删除未启用的闪屏么？";	
	}
	if(confirm(msg)) {
		$.post("<%=basePath%>manager/deleteOutGlitter.do", {"id":id}, function(data) {
			window.location.reload(true);
		});
	}
}
</script>
</head>
<body>

<form action="<%=basePath%>manager/updateOutGlitter.do" method="post" enctype="multipart/form-data">
	<input type="hidden" name="id" value="<%=gameGlitter.getId()%>">
	<table id="customers">
		<tr>
			<th>图片</th>
			<td>
				<img src="<%=gameGlitter.getPicture()%>" width="100px" height="100px">
				<input type="file" name="picture">
			</td>
		</tr>
		<tr>
			<th>按钮名称</th>
			<td>
				<input type="text" name="buttonName" size="33" value="<%=gameGlitter.getButtonName()%>">
				
				<select name="buttonType">
					<option value="0" <%= gameGlitter.getButtonType()==0?"selected":""%>>选择类型</option>
					<option value="1" <%= gameGlitter.getButtonType()==1?"selected":""%>>应用类：下载/安装</option>
					<option value="2" <%= gameGlitter.getButtonType()==2?"selected":""%>>资讯类：去看看</option>
				</select>
			</td>
		</tr>
		<tr>			
			<th>按钮链接</th>
			<td><input type="text" name="buttonUrl" size="44" value="<%=gameGlitter.getButtonUrl()%>"></td>
		</tr>
		<tr>
			<th>执行时间</th>
			<td>
				<input type="text" name="startTime" id="startTime" value="<%=gameGlitter.getStartTime()%>">
				-
				<input type="text" name="endTime" id="endTime" value="<%=gameGlitter.getEndTime()%>">
			</td>
		</tr>
		<tr>
			<th>包名</th>
			<td><input type="text" name="packageName" size="44" value="<%=gameGlitter.getPackageName()%>"></td>
		</tr>
		<tr>
			<th>版本号</th>
			<td><input type="text" name="versionCode" size="44" value="<%=gameGlitter.getVersionCode()%>"></td>
		</tr>			
		<tr>
			<th>优先级</th>
			<td><input type="text" name="indexNum" id="indexNum" value="<%=gameGlitter.getIndexNum()%>">(数值高的排前)</td>
		</tr>
		<tr>
			<td colspan="2"><button type="submit">修改</button></td>
		</tr>
	</table>
</form>
</body>
</html>