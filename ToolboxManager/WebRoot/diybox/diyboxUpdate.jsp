<%@page import="com.pixshow.toolboxmgr.bean.DiyboxBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%
    String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
DiyboxBean bean =(DiyboxBean) result.get("diybox");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
		<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
		<script type="text/javascript">
			function check1(){
				var name = document.getElementById('name').value;
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
					alert("名称不能为空");
					return false;
				}
				if(downloadUrl == ''){
					alert("下载地址不能为空");
					return false;
				}
			}
		</script>
	</head>
	<body>
		<form action="<%=basePath%>manager/diyUpdate.do" method="post" enctype="multipart/form-data" onsubmit="return check1()">
			 <input type="hidden" name="id" value="<%=bean.getId() %>">	
		<table id="customers" width="98%">
			<tr>
				<th>名称</th>
				<td><input type="text" id="name" name="name" value="<%=bean.getName() %>"/></td>
			</tr>
			<tr>
				<th>图标</th>
				<td>
					<input type="file" id="icon" name="icon"/>
					<img src="<%=ImageStorageTootl.getUrl(bean.getIcon())%>" width="50px" height="50px">
				</td>
			</tr>
			<tr>
				<th>评级</th>
				<td>
					<select name="rate">
					 	<option value="1.0" <%=bean.getRate()==1.0 ? "selected" :"" %>>☆</option>
					 	<option value="2.0" <%=bean.getRate()==2.0 ? "selected" :"" %>>★</option>
					 	<option value="3.0" <%=bean.getRate()==3.0 ? "selected" :"" %>>★☆</option>
					 	<option value="4.0" <%=bean.getRate()==4.0 ? "selected" :"" %>>★★</option>
					    <option value="5.0" <%=bean.getRate()==5.0 ? "selected" :"" %>>★★☆</option>
					 	<option value="6.0" <%=bean.getRate()==6.0 ? "selected" :"" %>>★★★</option>
					 	<option value="7.0" <%=bean.getRate()==7.0 ? "selected" :"" %>>★★★☆</option>
					 	<option value="8.0" <%=bean.getRate()==8.0 ? "selected" :"" %>>★★★★</option>
					 	<option value="9.0" <%=bean.getRate()==9.0 ? "selected" :"" %>>★★★★☆</option>
					 	<option value="10.0" <%=bean.getRate()==10.0 ? "selected" :"" %>>★★★★★</option>
					 </select>
				</td>
			</tr>
			<tr>
				<th>下载地址</th>
				<td>
					<input type="text" name="downloadUrl" id="downloadUrl" value="<%=bean.getDownloadUrl()%>"/>
					wifi自动下载
					<select name="downloadAuto">
						<option value="0" <%=bean.getDownloadAuto()==0 ? "selected" :"" %>>否</option>
						<option value="1" <%=bean.getDownloadAuto()==1 ? "selected" :"" %>>是</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>详细地址</th>
				<td>
					<input type="text" id="detailUrl" name="detailUrl" value="<%=bean.getDetailUrl()%>"/>
					是否启用
					<select name="detailOpen">
						<option value="0" <%=bean.getDetailOpen()==0 ? "selected" :"" %>>否</option>
						<option value="1" <%=bean.getDetailOpen()==1 ? "selected" :"" %>>是</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>包名</th>
				<td><input type="text" id="packageName" name="packageName" value="<%=bean.getPackageName()%>"/></td>
			</tr>
			<tr>
				<th>版本号</th>
				<td><input type="text" id="versionCode" name="versionCode" value="<%=bean.getVersionCode()%>"/></td>
			</tr>
			<tr>
				<th colspan="2"><input type="submit" value="修改"/></th>
			</tr>
		</table>
		</form>
	</body>
</html>
