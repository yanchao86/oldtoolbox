<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.io.File"%>
<%@page import="com.pixshow.framework.utils.FileUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@page import="com.pixshow.login.tools.PermissionTool"%>
<%
String basePath = WebUtility.getBasePath(request);
String data = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_admin.json").getFile()), "UTF-8");
JSONObject json = JSONObject.fromObject(data);
JSONObject user = json.getJSONObject("user");
JSONObject group = json.getJSONObject("group");
JSONObject menu = json.getJSONObject("menu");

TreeMap<Integer, JSONObject> menus = new TreeMap<Integer, JSONObject>();
String username = (String) session.getAttribute("username");
JSONArray groupUser = user.getJSONObject(username).getJSONArray("groupUser");
for (int i = 0; i < groupUser.size(); i++) {
    String groupKey = groupUser.getString(i);
    JSONObject group_ = group.getJSONObject(groupKey);
    JSONArray menu_permission = group_.getJSONArray("menu_permission");
    if(menu_permission.contains("all")) {
        Iterator<String> it = menu.keys();
        while(it.hasNext()) {
            String menuKey = it.next();
            getMenus(menus, menuKey, menu);
        }
    } else {
        for (int j = 0; j < menu_permission.size(); j++) {
            String menuKey = menu_permission.optString(j);
            getMenus(menus, menuKey, menu);
        }
    }
}
%>
<%!
private void getMenus(TreeMap<Integer, JSONObject> menus, String key, JSONObject allMenu) {
	String[] key_s = key.split("_");
	String pKey = key_s[0]+"_"+key_s[1];
	JSONObject menu = allMenu.getJSONObject(pKey);
	String name = menu.getString("name");
	String url  = menu.optString("url");
	JSONObject json = new JSONObject();
	if(menus.containsKey(Integer.parseInt(key_s[1]))) {
	    json = menus.get(Integer.parseInt(key_s[1]));
	}
	json.put("name", name);
	json.put("url", url);
	JSONArray cMenus = json.optJSONArray("child");
	cMenus = cMenus==null?new JSONArray():cMenus;
	if(menu.containsKey("child")) {
		JSONObject child = menu.getJSONObject("child");
		if(key_s.length==3) {
		    cMenus.add(child.getJSONObject(key));
		} else {
		    Iterator<String> it = child.keys();
		    while(it.hasNext()) {
		        String k = it.next();
		        cMenus.add(child.getJSONObject(k));    
		    }
		}
	}
	json.put("child", cMenus);
	menus.put(Integer.parseInt(key_s[1]), json);
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>后台管理</title>
	<style type="text/css">
		body { font-family:Verdana; font-size:14px; margin:1;}
		.container {margin:0 auto; width:100%;}
		.header { height:12%; background:#6cf; margin-bottom:1px;}
		.mainContent { margin-bottom:1px;}
		.sidebar { float:left; width:15%; height:88%; background:#9ff;}
		.main { float:right; width:85%; height:88%; background:#cff; text-align: center;}/*因为是固定宽度，采用左右浮动方法可有效避免ie 3像素bug*/		
	</style>
	
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	<script type="text/javascript">
		function loadData(url) {
			$('#content').attr('src',url);
		}
	</script>
  </head>
  
  <body>
	<div class="container">
	  <div class="header">
	  	idotools后台配置-测试版
	  	<a href="<%=basePath %>logout.do">登出</a>	
	  </div>
	  <div class="mainContent">
	    <div class="sidebar">
    		<ul>
 <%	
 	Iterator<Integer> it = menus.keySet().iterator();
 	while(it.hasNext()) {
		int index = it.next();
		JSONObject content = menus.get(index);
		JSONArray arr = content.getJSONArray("child");
 	   %>
    		    <li><a href="javascript: loadData('<%=content.optString("url")%>')"><%=content.getString("name") %></a>
    		    	<ul>
 	    <%
 	    for(int i=0; i<arr.size(); i++) {
 	        JSONObject child = arr.getJSONObject(i);%>
    		    		<li><a href="javascript: loadData('<%=child.getString("url") %>')">
    		    		<%=child.getString("name") %></a></li>
	    <%}%>
    		    	</ul>
    		    </li>
	<%}%>
    		</ul>
	    </div>
	    <iframe id="content" frameborder="0" width="85%" height="88%" src="<%=basePath %>welcome.jsp"></iframe>
	  </div>
	</div>
  </body>
</html>
