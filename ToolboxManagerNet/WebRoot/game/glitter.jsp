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
<script type="text/javascript"src="<%=basePath%>plugin/dialog/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>plugin/date/lhgcalendar.min.js"></script>
<script type="text/javascript">
$(function() {
	$("#startTime").calendar({ format:'yyyy-MM-dd' });
	$("#endTime").calendar({ format:'yyyy-MM-dd' });
	
	$("input[name=glitterType]").change(function() {
		var val = $(this).val()==0?true:false;
		$("input[name=buttonName]").val("");
		$("select[name=buttonType]").val(0);
		$("input[name=buttonUrl]").val("");
		$("input[name=startTime]").val("");
		$("input[name=endTime]").val("");
		
		$("input[name=buttonName]").attr("disabled", val);
		$("select[name=buttonType]").attr("disabled", val);
		$("input[name=buttonUrl]").attr("disabled", val);
		$("input[name=startTime]").attr("disabled", val);
		$("input[name=endTime]").attr("disabled", val);
	});
});

function glitterUse(id) {
	$.post("<%=basePath%>manager/updatGlitterUseType.do", {"id":id}, function(data) {
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
		$.post("<%=basePath%>manager/deleteGlitter.do", {"id":id}, function(data) {
			window.location.reload(true);
		});
	}
}
</script>
</head>
<body>

<form action="<%=basePath%>manager/saveGlitter.do" method="post" enctype="multipart/form-data">	
	<table id="customers">
		<tr>
			<th>闪屏类型</th>
			<td>
				<input type="radio" value="0" name="glitterType" id="glitterType">默认闪屏
				<input type="radio" value="1" name="glitterType" id="glitterType" checked="checked">节日闪屏
			</td>
		</tr>
		<tr>
			<th>图片</th>
			<td>
				横屏图：<input type="file" name="widthPic">
				<br>
				竖屏图：<input type="file" name="highPic">
			</td>
		</tr>
		<tr>
			<th>留存时间</th>
			<td>
				<select name="dwellTime">
			<%
			    for(int i=0;i<11;i++) {
			%>
					<option value="<%=i%>"><%=i%></option>
			<%
			    }
			%>
				</select>秒
			</td>
		</tr>
		<tr>
			<th>按钮名称</th>
			<td>
				<input type="text" name="buttonName" size="33">
				<select name="buttonType">
					<option value="0">选择类型</option>
					<option value="1">普通链接</option>
					<option value="2">游戏详情</option>
					<option value="3">直接下载</option>
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
			<td colspan="2"><button type="submit">提交</button></td>
		</tr>
	</table>
</form>
<br>
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
		<th>启用状态</th>
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
						    out.print(bean.getButtonName()+"【");
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
						    out.print("】");
						}
		%>
		</td>
		<td><%=bean.getGlitterType()==GameGlitter.glitterType.isHoliday?bean.getButtonUrl():""%></td>
		<td><%=bean.getGlitterType()==GameGlitter.glitterType.isHoliday?DateUtility.format(bean.getStartTime(), "yyyy-MM-dd") +" - "+DateUtility.format(bean.getEndTime(), "yyyy-MM-dd"):""%></td>
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
			<a href="javascript:delGlitter(<%=bean.getId() %>, <%=bean.getUseType()%>)">删除</a>
		</td>
	</tr>
<%}%>
</table>
</body>
</html>