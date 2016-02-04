<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);

Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<Map<String, Object>> values = (List<Map<String, Object>>) result.get("values");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	<script type="text/javascript">
		function channelChange(key, value) {
			value = value=='0'?'1':'0';
			$.post('<%=basePath%>manager/channelChange.do', {'key':key, 'value':value}, function(data) {
				window.location.reload(true);
			});
		}
		function delchannel(key) {
			$.post('<%=basePath%>manager/delchannel.do', {'key':key}, function(data) {
				window.location.reload(true);
			});
		}
	</script>
  </head>
  
  <body>
  <form action="<%=basePath %>manager/saveChannelToolSource.do" method="post">
    <table id="customers" width="98%">
    	<tr>
    		<th>channel code</th>
    		<td><input type="text" name="key"></td>
    	</tr>
    	<tr>
    		<th>是否选用</th>
    		<td>
    			不选用<input type="radio" name="value" value="0" checked>
    			选用<input type="radio" name="value" value="1">
    		</td>
    	</tr>
    	<tr>
    		<th colspan="2"><button>新   增</button></th>
    	</tr>
    </table>
  </form>

    <table id="customers" width="98%">
    	<tr>
    		<th>channel code</th>
    		<th>是否选用</th>
    		<th>删除</th>
    	</tr>
    <%for(int i=0; i<values.size(); i++) {
        Map<String, Object> val = values.get(i);
        String value = val.get("value").toString();%>
    	<tr>
    		<td><%=val.get("type") %></td>
    		<td>
    			<input type="checkbox" value="<%=value%>" <%="0".equals(value)?"":"checked" %> onchange="channelChange('<%=val.get("type") %>', '<%=value%>')">
    		</td>
    		<td><button onclick="delchannel('<%=val.get("type") %>')">删除</button></td>
    	</tr>
    <%}%>
    </table>
  </body>
</html>
