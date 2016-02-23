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
List<GameOutGlitterBean> gameGlitters = (List<GameOutGlitterBean>) result.get("gameGlitters");
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

<form action="<%=basePath%>manager/saveOutGlitter.do" method="post" enctype="multipart/form-data">
	<table id="customers">
		<tr>
			<th>图片</th>
			<td>
				<input type="file" name="picture">
			</td>
		</tr>
		<tr>
			<th>按钮名称</th>
			<td>
				<input type="text" name="buttonName" size="33">
				<select name="buttonType">
					<option value="0">选择类型</option>
					<option value="1">应用类：下载/安装</option>
					<option value="2">资讯类：去看看</option>
				</select>
			</td>
		</tr>
		<tr>			
			<th>按钮链接</th>
			<td><input type="text" name="buttonUrl" size="44"></td>
		</tr>
		<tr>
			<th>执行时间</th>
			<td>
				<input type="text" name="startTime" id="startTime">
				-
				<input type="text" name="endTime" id="endTime">
			</td>
		</tr>
		<tr>
			<td>包名</td>
			<td><input type="text" name="packageName" size="44"></td>
		</tr>
		<tr>
			<td>版本号</td>
			<td><input type="text" name="versionCode" size="44"></td>
		</tr>		
		<tr>
			<th>优先级</th>
			<td><input type="text" name="indexNum" id="indexNum">(数值高的排前)</td>
		</tr>
		<tr>
			<td colspan="2"><button type="submit">提交</button></td>
		</tr>
	</table>
</form>
<br>
<table id="customers">
	<tr>
		<th>优先级</th>
		<th>图片</th>
		<th>包名/版本号</th>
		<th>按钮名称</th>
		<th>按钮链接</th>
		<th>执行时间</th>
		<th>启用状态</th>
		<th>操作</th>
	</tr>
<%
    for(GameOutGlitterBean bean : gameGlitters) {
%>
	<tr>
		<td><%=bean.getIndexNum()%></td>
		<td><%=bean.getPackageName()+" / "+bean.getVersionCode()%></td>
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
		<%if(bean.getUseType() == 0) {%>
			<font color="green">未启用</font>
			<a href="javascript:glitterUse(<%=bean.getId() %>)">启用</a>
		<%} else {%>
			<font color="red">已启用</font>
			<a href="javascript:glitterUse(<%=bean.getId() %>)">不启用</a>
		<%} %>
		</td>
		<td>
			<a href="<%=basePath %>manager/updateOutGlitterPage.do?id=<%=bean.getId() %>">编辑</a>
			<a href="javascript:delGlitter(<%=bean.getId() %>, <%=bean.getUseType()%>)">删除</a>
		</td>
	</tr>
<%}%>
</table>
</body>
</html>