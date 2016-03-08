<%@page import="com.pixshow.toolboxmgr.bean.DiyboxBean"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%
    String basePath = WebUtility.getBasePath(request);
	Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
    List<DiyboxBean> diyboxs = (List<DiyboxBean>) result.get("diyboxs");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
<style>
#sortable {
	list-style-type: none;
	margin: 0;
	padding: 0;
	width: 950px;
}

.oldCss {
	margin: 3px 3px 3px 0;
	padding: 1px;
	float: left;
	width: 293px;
	text-align: center;
	border: 1px solid gray;
}

.newCss {
	margin: 3px 3px 3px 0;
	padding: 1px;
	float: left;
	width: 293px;
	text-align: center;
	border: 1px solid red;
}
.font1 {font-size: 16px; font-style: italic}
.font2 {font-size: 12px; color: red; font-style: italic}
</style>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery-ui.js"></script>
	<script type="text/javascript">
	 	function edit() {
			$("#sortable").sortable({
				revert: true
			});
			$('#edit').html('保 存');
			$('#sortable li').attr('class', 'newCss');
			$('#edit').attr('onclick', 'save()');
		}
		function save() {
			var sortIds = $('#sortable').sortable('toArray').toString();
			$.post('<%=basePath%>manager/diySort.do', {'strID':sortIds}, function(data) {
				if(data.result == 0) {
					$("#sortable").sortable('destroy');
					$('#sortable li').attr('class', 'oldCss');
					$('#edit').html('编辑模式');
					$('#edit').attr('onclick', 'edit()');
					alert('排序成功');		
				} else {
					alert('排序失败');
				}
			});
		}
		function download(url){
			alert(url);
			window.location.href=url;
		}
		function deleteTool(id){
			if(confirm("确定要删除吗？？")){
				window.location.href="<%=basePath%>manager/diyDelete.do?id="+id;
			}
		}
	</script>
	</head>

	<body>
		<button onclick="edit()" id="edit">编辑模式</button>
		<ul id="sortable">
			<% for (DiyboxBean bean : diyboxs) {%>
			<li class="oldCss" id="<%=bean.getId()%>">
				<img width="200" height="200" src="<%=ImageStorageTootl.getUrl(bean.getIcon()) + "?" + System.currentTimeMillis()%>">
				<hr/>
				<font class="font1">名称：<%=bean.getName()%></font> 
				<hr/>
				 <% String xx="☆";
				 	switch ((int)bean.getRate()) {
					    case 1:xx="☆"; break;
					    case 2:xx="★";	break;
					    case 3:xx="★☆"; break;
					    case 4:xx="★★"; break;
					    case 5:xx="★★☆"; break;
					    case 6:xx="★★★"; break;
					    case 7:xx="★★★☆"; break;
					    case 8:xx="★★★★"; break;
					    case 9:xx="★★★★☆"; break;
					    case 10:xx="★★★★★";break;
					} 
				%>
				<font class="font1">评级：<%=xx %></font>
				<hr/>
				<font class="font1"><a href="<%=basePath%>manager/downloadDayStats.do?diyId=<%=bean.getId()%>">下载次数：<%=bean.getDownloadCount()%></a></font> 
				<input type="button" value= "下载" onclick="download('<%=bean.getDownloadUrl()%>')"/>
				<a href="<%=basePath%>manager/diyPreUpdate.do?id=<%=bean.getId()%>">
					<font class="font2">修改</font></a> 
				&nbsp;&nbsp;
				<a href="javascript:deleteTool(<%=bean.getId()%>)">
					<font class="font2">删除</font></a>
					
			</li>
			<%
			    }
			%>
		</ul>
	</body>
</html>
