<%@page import="com.pixshow.framework.utils.AppContextUtility"%>
<%@page import="org.apache.james.mime4j.util.ContentUtil"%>
<%@page import="com.pixshow.toolboxmgr.service.ToolboxService"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.toolboxmgr.bean.ToolboxBean"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String basePath = WebUtility.getBasePath(request);
	ToolboxService toolboxService = AppContextUtility.getContext().getBean(ToolboxService.class);

	String selectInto_id = request.getParameter("selectInto_id");
	if(StringUtility.isNotEmpty(selectInto_id)) {
	    toolboxService.setlctInto(Integer.parseInt(selectInto_id));
	}
	List<ToolboxBean> recycles = toolboxService.findAllRecycle();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery-ui.js"></script>
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

<script type="text/javascript">
	function selectInto(id) {
		if(confirm("确定恢复么？")) {
			$("#selectInto_"+id).submit();
		}
	}
</script>
</head>
<body>
<h3>回收站列表</h3>
<ul id="sortable">
	<%
	    for (ToolboxBean bean : recycles) {
	        JSONObject json = StringUtility.isEmpty(bean.getExtInfo2())?new JSONObject() : JSONObject.fromObject(bean.getExtInfo2());
	        int canMove = json.optInt("state", 0);
	%>
	<li class="oldCss" id="<%=bean.getId()%>" canMove="<%=canMove%>">
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
		<font class="font1">下载次数：<%=bean.getDownloadCount()%></font>
		&nbsp;&nbsp;
		<input type="button" value="恢&nbsp;&nbsp;复" onclick="selectInto(<%=bean.getId()%>)"/>
		
		<form action="#" method="post" id="selectInto_<%=bean.getId()%>">
			<input type="hidden" value="<%=bean.getId()%>" name="selectInto_id">
		</form>	
	</li>
	<%}%>
</ul>
</body>
</html>