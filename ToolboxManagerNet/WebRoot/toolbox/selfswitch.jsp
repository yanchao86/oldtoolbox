<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="java.io.File"%>
<%@page import="com.pixshow.framework.utils.FileUtility"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);

String selfswitch_pro = request.getParameter("selfswitch_pro");
if(selfswitch_pro != null) {
    File selfswitch_file = new File(this.getClass().getResource("/config/selfswitch.properties").getFile());
    FileUtility.writeStringToFile(selfswitch_file, selfswitch_pro);
}

String data = FileUtility.readFileToString(new File(this.getClass().getResource("/config/selfswitch.properties").getFile()), "UTF-8");


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
	<script type="text/javascript">
		function selfswitch(obj) {
			var val = $(obj).val();
			var msg = val=="open"?"确定开启？":"确定关闭？";
			if(confirm(msg)) {
				window.location.href="<%=basePath%>toolbox/selfswitch.jsp?selfswitch_pro="+val;
			}
		}
	</script>
  </head>
  
  <body>
<%
boolean selected = false;
if(StringUtility.isNotEmpty(data)) {
    selected = true;
}
%>	
<select onchange="selfswitch(this)">
	<option value="">关闭</option>
	<option value="open" <%=selected?"selected":"" %>>开启</option>
</select>
  </body>
</html>
