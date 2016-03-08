<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.framework.utils.HttpUtility"%>
<%@page import="net.sf.json.JSONArray"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<pre>
<%
int pageSize = 20;
int count = 1;
for (int pageNum = 0; pageNum < 50; pageNum++) {
    String url = "http://apps.wandoujia.com/api/v1/apps?type=weeklytopgame&max=" + pageSize + "&start=" + (pageSize * pageNum);
    System.out.println(url);
    String jsonStr = HttpUtility.get(url);
    JSONArray jsonArray = JSONArray.fromObject(jsonStr);
    System.out.println(jsonArray.size());
    for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject jsonObj = jsonArray.getJSONObject(i);
        String title = jsonObj.getString("title");
        String packageName = jsonObj.getString("packageName");
        out.println((count ++) + " : " +  title + " | " + packageName + "\r\n");
    }
    out.flush();
}
%>
</pre>

<script type="text/javascript">
	alert("抓取完成");
</script>
</body>
</html>