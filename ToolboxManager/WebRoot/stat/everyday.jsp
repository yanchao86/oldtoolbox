<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.pixshow.framework.utils.DateUtility"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
/* Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<String> apps = (List<String>) result.get("apps"); */

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	
	<script type="text/javascript">
		function loadUrl(url) {
			$("#content").attr("src", url);
		}
		function everydayData(queryType){
			$.ajax({
				dataType : 'json',
				type : 'post',
				cache : false,
				url : '<%=basePath%>manager/everydayData.do',
				data:{daykey:$("#daykey").val(),queryType:queryType},
				success : function(data) {
					if(data.result!=0){
						$("#"+queryType).html(JSON.stringify(data));
						return;
					}
					var html="";
					var arr = data.data[queryType];
					for(var i=0;i<arr.length;i++){
						html+="<tr>";
						html+="<td>"+arr[i].day+"</td>";
						html+="<td>"+arr[i].code+"</td>";
						html+="<td>"+arr[i].counts+"</td>";
					html+="</tr>";
					}
					$("#"+queryType).html(html);
				},
				error : function() {
					alert("操作失败！");
				}
			});
		}
	</script>
</head>
<body>
	<input type="text" id="daykey"/><br/>
	<input type="button" value="点击获取iBoxOpen" onclick="everydayData('iBoxOpen');"/><table border="1"><thead><tr><th>day</th><th>code</th><th>counts</th></tr></thead><tbody id="iBoxOpen"></tbody></table><br/>
	<input type="button" value="点击获取notificationPV" onclick="everydayData('notificationPV');"/><table border="1"><thead><tr><th>day</th><th>code</th><th>counts</th></tr></thead><tbody id="notificationPV"></tbody></table><br/>
	<input type="button" value="点击获取notificationUV" onclick="everydayData('notificationUV');"/><table border="1"><thead><tr><th>day</th><th>code</th><th>counts</th></tr></thead><tbody id="notificationUV"></tbody></table><br/>
	<input type="button" value="点击获取appsPV" onclick="everydayData('appsPV');"/><table border="1"><thead><tr><th>day</th><th>code</th><th>counts</th></tr></thead><tbody id="appsPV"></tbody></table><br/>
	<input type="button" value="点击获取appsUV" onclick="everydayData('appsUV');"/><table border="1"><thead><tr><th>day</th><th>code</th><th>counts</th></tr></thead><tbody id="appsUV"></tbody></table><br/>
</body>
</html>