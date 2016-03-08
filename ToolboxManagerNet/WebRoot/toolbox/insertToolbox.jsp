<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String basePath = WebUtility.getBasePath(request);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
	<script type="text/javascript">
		function check1(){
			var name = document.getElementById('name').value;
			var icon = document.getElementById('icon').value;
			var downloadUrl = document.getElementById('downloadUrl').value;
			var packageName = document.getElementById('packageName').value;
			var versionCode = document.getElementById('versionCode').value;
			
			if(versionCode == ''){
				alert("版本号不能为空");
				return false;
			}
			
			if(packageName == ''){
				alert("包名不能为空");
				return false;
			}
			
			if(name == ''){
				alert("名称不能为空！");
				return false;
			}
			if(icon == ''){
				alert("图标不能为空！");
				return false;
			}
			if(downloadUrl == ''){
				alert("下载地址不能为空！");
				return false;
			}
		}
	</script>
	</head>

	<body>
		<form action="<%=basePath%>manager/toolInsert.do" method="post" enctype="multipart/form-data" onsubmit="return check1()">
		<table id="customers" width="98%">
			<tr>
				<th>名称</th>
				<td><input type="text" id="name" name="name"/></td>
			</tr>
			<tr>
				<th>图标</th>
				<td><input type="file" id="icon" name="icon"/></td>
			</tr>
			<tr>
				<th>评级</th>
				<td>
					<select name="rate">
					 	<option value="1.0">☆</option>
					 	<option value="2.0">★</option>
					 	<option value="3.0">★☆</option>
					 	<option value="4.0">★★</option>
					    <option value="5.0">★★☆</option>
					 	<option value="6.0">★★★</option>
					 	<option value="7.0">★★★☆</option>
					 	<option value="8.0">★★★★</option>
					 	<option value="9.0">★★★★☆</option>
					 	<option value="10.0">★★★★★</option>
					 </select>
				</td>
			</tr>
			<tr>
				<th>下载地址</th>
				<td>
					<input type="text" id="downloadUrl" name="downloadUrl"/>
					wifi自动下载
					<select name="downloadAuto">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>详细地址</th>
				<td>
					<input type="text" id="detailUrl" name="detailUrl"/>
					是否启用
					<select name="detailOpen">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>包名</th>
				<td><input type="text" id="packageName" name="packageName"/></td>
			</tr>
			<tr>
				<th>版本号</th>
				<td><input type="text" id="versionCode" name="versionCode"/></td>
			</tr>
			<tr>
				<th colspan="2"><input type="submit" value="保存"/></th>
			</tr>
		</table>
		</form>
	</body>
</html>
