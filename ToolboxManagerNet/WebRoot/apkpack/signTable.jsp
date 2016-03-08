<%@page import="com.pixshow.apkpack.bean.ApkKeyBean"%>
<%@page import="com.pixshow.apkpack.bean.ApkSignBean"%>
<%@page import="com.pixshow.framework.utils.ListUtiltiy"%>
<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="com.pixshow.apkpack.bean.ApkChannelBean"%>
<%@page import="com.pixshow.apkpack.bean.ApkProductBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String urls = (String) request.getAttribute("urls");
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");

List<ApkProductBean> products = (List<ApkProductBean>) result.get("products"); 
Map<Integer, ApkProductBean> productMap = ListUtiltiy.groupToObject(products, "id");

List<ApkKeyBean> keys = (List<ApkKeyBean>) result.get("keys"); 
Map<Integer, ApkKeyBean> keyMap = ListUtiltiy.groupToObject(keys, "id");

List<ApkChannelBean> channels = (List<ApkChannelBean>) result.get("channels");
Map<Integer, ApkChannelBean> channelMap = ListUtiltiy.groupToObject(channels, "id");
Map<Integer, List<ApkChannelBean>> channelappKeyMap = ListUtiltiy.groupToList(channels, "productId");

List<ApkSignBean> signs = (List<ApkSignBean>) result.get("signs");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<style type="text/css">
.sign_undone{color: red}
.sign_done{color: green}
</style>
</head>
<body>

<table id="customers">
	<tr>
		<th>序号</th>
		<th>APK名称</th>
		<th>产品</th>
		<th>密钥</th>
		<th>versioncode</th>
		<th>versionname</th>
		<th>文件下载</th>
		<th>打包进度</th>
		<th>操作者</th>
		<th>IP地址</th>
		<th>创建时间</th>
		<th>操作</th>
	</tr>
<%for(ApkSignBean sign : signs) {%>
	<tr>
		<td><%=sign.getId() %></td>
		<td><%=sign.getName() %></td>
		<td><%=productMap.containsKey(sign.getProductId())?productMap.get(sign.getProductId()).getName():"无" %></td>
		<td><%=keyMap.containsKey(sign.getApkKeyId())?keyMap.get(sign.getApkKeyId()).getName():"无" %></td>
		<td><%=sign.getVersioncode() %></td>
		<td><%=sign.getVersionname() %></td>
		<td>
			<a href="<%=sign.getSignApkFile()+"?"+System.currentTimeMillis() %>">签名文件</a>
		</td>
		<td>
			<%=sign.getMsg() %>
		</td>
		<td><%=sign.getUploadUser() %></td>
		<td><%=sign.getUploadIp() %></td>
		<td><%=sign.getCreateDate() %></td>
		<td><a href="javascript:delSign(<%=sign.getId() %>)">删除</a></td>
	</tr>
<%} %>	
</table>

</body>
</html>