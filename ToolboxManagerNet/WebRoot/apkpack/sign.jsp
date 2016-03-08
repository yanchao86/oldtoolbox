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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<style type="text/css">
.sign_undone{color: red}
.sign_done{color: green}
</style>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
<script type="text/javascript">
	function checkAllChannel() {
		$("input[name='channelId']").attr("checked",true);  
	}
	function formSubmit(obj) {
		var apk = $("#apk").val();
		var productId = $("select[name=productId]:selected").val();
		var channelId =$("input[name='channelId']:checked").val(); 
		alert(apk);
		alert(productId);
		alert(channelId);
		//$(obj).submit();
	}
	$(function() {
		loadData();
		var id = setInterval(loadData, 3000);
	});
	function loadData() {
		$("#signTable").load("<%=basePath%>manager/apkSignTable.do");
	}
	
</script>
</head>
<body>
APK打包
<form action="<%=basePath%>manager/apkSign.do" method="post" enctype="multipart/form-data" >
<table id="customers">
	<tr>
		<th>选择文件</th>
		<td><input type="file" name="apk" id="apk"></td>
	</tr>
	<tr>
		<th>选择产品</th>
		<td>
			<select name="productId">
				<option value="0">--选择产品--</option>
		<%for(ApkProductBean product : products ) {%>
		  		<option value="<%=product.getId()%>"><%=product.getName() %></option>  
		<%}%>		
			</select>
			<font color="red">(必选项)</font>
			<a href="<%=basePath%>manager/apkProduct.do">添加产品</a>
		</td>
	</tr>
	<tr>
		<th>选择渠道</th>
		<td>
	<%for(Map.Entry<Integer, List<ApkChannelBean>> list: channelappKeyMap.entrySet()) {%>
		<%for(ApkChannelBean channel : channelappKeyMap.get(list.getKey()) ) {%>
			<input type="checkbox" value="<%=channel.getId() %>" name="channelId"><%=channel.getName() %>  
		<%} %>
	<%}%>
			<hr>
			<button type="button" onclick="checkAllChannel()">全选</button>
			<a href="<%=basePath%>manager/apkChannel.do">添加渠道</a>
		</td>
	</tr>
	<tr>
		<th>versionCode</th>
		<td><input type="text" name="versioncode"></td>
	</tr>
	<tr>
		<th>versionName</th>
		<td><input type="text" name="versionname"></td>
	</tr>
	<tr>
		<td colspan="2"><button type="submit">打  包</button></td>
	</tr>
</table>
</form>
<div id="signTable"></div>

</body>
</html>