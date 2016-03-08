<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<script type="text/javascript"
			src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>js/jquery-ui.js"></script>

		<script type="text/javascript">
			function check1(){
				var imageName = document.getElementById('name').value;
				var imageUrl = document.getElementById('image').value;
				var description = document.getElementById('description').value;
				
				if(imageName == ''){
					alert("名称不能为空");
					return false;
				}
				if(imageUrl == ''){
					alert("图片不能为空");
					return false;
				}
				if(description == ''){
					alert("描述不能为空");
					return false;
				}
			}
		</script>
	</head>

	<body>
		<table align="center">
			<h2 align="center">
				添加爱心
			</h2>
		</table>
		<br />
		<form action="<%=basePath%>manager/loveInsert.do" method="post" enctype="multipart/form-data"
			onsubmit="return check1()">
			名称：
			<input type="text" id="name" name="name" />
			<br />
			图片:
			<input type="file" id="image" name="image" />
			<br />
			描述:
			<input type="text" id="description" name="description" />
			<br />
			<input type="submit" value="保存" />
		</form>
	</body>
</html>
