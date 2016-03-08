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
List<List<ToolboxBean>> toolboxs_ = ListUtiltiy.split(toolboxs, 6);
Map<Integer, ToolboxBean> toolboxMap = ListUtiltiy.groupToObject(toolboxs, "id");
List<GameSuspendBean> gameSuspends = (List<GameSuspendBean>) result.get("gameSuspends");
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
$(function() {
	$(".tr_pic").hide();
	$(".tr_recommend").show();
	$("input[name=suspendType]").change(function() {
		if($(this).val()==0) {
			$(".tr_pic").show();
			$(".tr_recommend").hide();
		} else {
			$(".tr_pic").hide();
			$(".tr_recommend").show();
		}
	});
});
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
	$.post("<%=basePath%>manager/suspendSort.do", {"id":id, "sortIds":sortIds}, function(data) {
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
	$.post("<%=basePath%>manager/updatSuspendUseType.do", {"id":id}, function(data) {
		alert(data.data.result);
		window.location.reload(true);
	});
}
function delSuspend(id, useType) {
	var msg = "确定删除正在启用的么？";
	if(useType == 0) {
		msg = "确定删除未启用的么？";	
	}
	if(confirm(msg)) {
		$.post("<%=basePath%>manager/deleteSuspend.do", {"id":id}, function(data) {
			window.location.reload(true);
		});
	}
}
</script>
</head>
<body>

<form action="<%=basePath %>manager/saveSuspend.do" method="post" enctype="multipart/form-data">	
	<table id="customers">
		<tr>
			<th>暂停类型</th>
			<td>
				<input type="radio" value="0" name="suspendType" id="suspendType">只有图片
				<input type="radio" value="1" name="suspendType" id="suspendType" checked="checked">游戏推荐
			</td>
		</tr>
		<tr class="tr_pic">
			<th>图片</th>
			<td>
				横屏图：<input type="file" name="widthPic">
				<br>
				竖屏图：<input type="file" name="highPic">
			</td>
		</tr>
		<tr class="tr_pic">			
			<th>图片链接</th>
			<td>
				<input type="text" name="picUrl" size="44">
				<br>
				<select name="picType">
					<option value="0">链接类型</option>
					<option value="1">普通链接</option>
					<option value="2">游戏详情</option>
					<option value="3">直接下载</option>
				</select>
			</td>
		</tr>
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
			<td colspan="2"><button type="submit">保  存</button> </td>
		</tr>
	</table>
</form>
<br>
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
		<th>启用状态</th>
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
		    out.print("<tr><td><ul id='sortable_"+bean.getId()+"'>");
			JSONArray arr = JSONArray.fromObject(bean.getContent());
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
		}%>
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
			<a href="javascript:delSuspend(<%=bean.getId() %>, <%=bean.getUseType()%>)">删除</a>
		</td>
	</tr>
<%} %>
</table>
</body>
</html>