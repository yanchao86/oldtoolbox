<%@page import="com.pixshow.toolboxmgr.bean.GameRecommendBean"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%@page import="com.pixshow.framework.utils.ListUtiltiy"%>
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
List<List<ToolboxBean>> toolboxs_ = ListUtiltiy.split(toolboxs, 6);
Map<Integer, ToolboxBean> toolboxMap = ListUtiltiy.groupToObject(toolboxs, "id");
List<GameRecommendBean> gameRecommends = (List<GameRecommendBean>) result.get("gameRecommends");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<style>
#sortable {
	list-style-type: none;
	margin: 0;
	padding: 0;
	width: 650px;
}

.oldCss {
	margin: 3px 3px 3px 0;
	padding: 1px;
	float: left;
	width: 110px;
	text-align: center;
	border: 1px solid gray;
}

.newCss {
	margin: 3px 3px 3px 0;
	padding: 1px;
	float: left;
	width: 110px;
	text-align: center;
	border: 1px solid red;
}
.font1 {font-size: 16px; font-style: italic}
.font2 {font-size: 12px; color: red; font-style: italic}
</style>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery-ui.js"></script>
<script type="text/javascript">
function edit(id) {
	$("#sortable_"+id).sortable({
		revert: false
	});
	$("#edit_"+id).html("保 存");
	$("#sortable_"+id+" li").attr("class", "newCss");
	$("#edit_"+id).attr("onclick", "save("+id+")");
}
function save(id) {
	var sortIds = $("#sortable_"+id).sortable("toArray").toString();
	$.post("<%=basePath%>manager/recommendSort.do", {"id":id, "sortIds":sortIds}, function(data) {
		if(data.result == 0) {
			$("#sortable_"+id).sortable("destroy");
			$("#sortable_"+id+" li").attr("class", "oldCss");
			$("#edit_"+id).html("编辑模式");
			$("#edit_"+id).attr("onclick", "edit()");
			alert("排序成功");		
		} else {
			alert("排序失败");
		}
	});
}
function suspendUse(id) {
	$.post("<%=basePath%>manager/updatRecommendUseType.do", {"id":id}, function(data) {
		alert(data.data.result);
		window.location.reload(true);
	});
}
function delRecommend(id) {
	if(confirm("确定删除么？")) {
		$.post("<%=basePath%>manager/deleteRecommend.do", {"id":id}, function(data) {
			window.location.reload(true);
		});
	}
}
</script>
</head>
<body>

<form action="<%=basePath %>manager/saveRecommend.do" method="post">	
	<table id="customers">
		<tr class="tr_recommend">
			<th>推荐选择</th>
			<td>
				<table>
		<%for(List<ToolboxBean> boxs : toolboxs_) {
	    	out.print("<tr>");
		    for(ToolboxBean box : boxs) {%>
					<td>
						<input type="checkbox" value="<%=box.getId()%>" name="gameRecommend">
						<img width="100" height="100" src="<%=ImageStorageTootl.getUrl(box.getIcon()) + "?" + System.currentTimeMillis()%>">
						<br>
						<%=box.getName() %>
					</td>
			<%}
		    out.print("</tr>");
		}%>
				</table>
			</td>
		</tr>
		<tr>
			<td><button type="submit">保  存</button></td>
		</tr>
	</table>
</form>
<br>
<table id="customers">
	<tr>
		<th>序号</th>
		<th>游戏推荐</th>
		<th>启用状态</th>
		<th>操作</th>
	</tr>
<%for(GameRecommendBean bean : gameRecommends) {%>
  	<tr>	
  		<td><%=bean.getId() %></td>
		<td>
			<table>
			<%out.print("<tr><td><ul id='sortable_"+bean.getId()+"'>");
			JSONArray arr = StringUtility.isEmpty(bean.getContent())?new JSONArray():JSONArray.fromObject(bean.getContent());
			for(int i=0; i<arr.size(); i++) {
			    ToolboxBean box = toolboxMap.get(arr.getInt(i));%>
				<li class="oldCss" id="<%=box.getId()%>">
					<img width="100" height="100" src="<%=ImageStorageTootl.getUrl(box.getIcon()) + "?" + System.currentTimeMillis()%>">
					<br>
					<%=box.getName() %>
				</li>
			<%}
			out.print("</ul>");%>
			<button onclick="edit(<%=bean.getId() %>)" id="edit_<%=bean.getId() %>">编辑模式</button>
			<%out.print("</td></tr>");
		%>
			</table>
		</td>
		<td>
		<%if(bean.getUseType() == 0) {%>
			<font color="green">未启用</font>
			<a href="javascript:suspendUse(<%=bean.getId() %>)">启用</a>
		<%} else {%>
			<font color="red">已启用</font>
			<a href="javascript:suspendUse(<%=bean.getId() %>)">不启用</a>
		<%} %>
		</td>		
		<td>
			<a href="javascript:delRecommend(<%=bean.getId() %>)">删除</a>
		</td>
	</tr>
<%} %>
</table>
</body>
</html>