<%@page import="com.pixshow.framework.utils.ListUtiltiy"%>
<%@page import="com.pixshow.apkpack.bean.ApkKeyBean"%>
<%@page import="com.pixshow.apkpack.bean.ApkProductBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<ApkKeyBean> apkKeys = (List<ApkKeyBean>) result.get("apkKeys");
Map<Integer, ApkKeyBean> appKeyMap = ListUtiltiy.groupToObject(apkKeys, "id");
List<ApkProductBean> apkProducts = (List<ApkProductBean>) result.get("apkProducts");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>产品维护</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
<script type="text/javascript">
	function delPro(id) {
		
	}
</script>
</head>
<body>
产品维护
<form action="<%=basePath%>manager/apkProduct.do" method="post">
<table id="customers">
	<tr>
		<th>名称</th>
		<td><input type="text" name="name"></td>
	</tr>
	<tr>
		<th>英文名称</th>
		<td><input type="text" name="engName"></td>
	</tr>
	<tr>
		<th>选择密钥</th>
		<td>
			<select name="apkKeyId">
				<option value="0">--选择密钥--</option>
				<%for(ApkKeyBean key : apkKeys) {%>
				<option value="<%=key.getId()%>"><%=key.getName() %></option>
				<%}%>
			</select>
			<a href="<%=basePath%>manager/apkKey.do">添加密钥</a>
		</td>
	</tr>
	<tr>
		<th>appKey</th>
		<td><input type="text" name="appKey"></td>
	</tr>
	<tr>
		<td colspan="2"><button type="submit">提  交</button></td>
	</tr>
</table>	
</form>

<table id="customers">
	<tr>
		<th>序号</th>
		<th>名称</th>
		<th>英文名称</th>
		<th>密钥</th>
		<th>appKey</th>
		<th>创建时间</th>
		<th>操作</th>
	</tr>
<%for(ApkProductBean product : apkProducts) {%>
	<tr>
		<td><%=product.getId() %></td>
		<td><%=product.getName() %></td>
		<td><%=product.getEngName() %></td>
		<td><%=appKeyMap.containsKey(product.getApkKeyId())?appKeyMap.get(product.getApkKeyId()).getName():"-暂无-" %></td>
		<td><%=product.getAppKey() %></td>
		<td><%=product.getCreateDate() %></td>
		<td><a href="javascript:delPro(<%=product.getId()%>)">删除</a></td>
	</tr>
<%}%>
</table>
</body>
</html>