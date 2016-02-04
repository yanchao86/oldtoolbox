<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.pixshow.toolboxmgr.bean.LoveBean"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<script type="text/javascript"
			src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
		<script type="text/javascript">
				function delete1(id){
				
					if(confirm("确定要删除吗？")){
						window.location.href = "<%=basePath%>manager/loveDelete.do?id="+id;
					}
					
				}
	</script>

	</head>

	<body>
		<table align="center">
			<h2 align="center">
				爱心列表
			</h2>
		</table>
		<table align="center" border="1">
			<tr>
				<th>
					编号
				</th>
				<th>
					名称
				</th>
				<th>
					图片
				</th>
				<th>
					描述
				</th>
				<th>
					创建日期
				</th>
				<th>
					操作
				</th>
			</tr>
			<%
			    List<LoveBean> list = (List<LoveBean>) request.getAttribute("list");
			    for (LoveBean bean : list) {
			%>
			<tr>
				<td><%=bean.getId()%></td>
				<td><%=bean.getName()%></td>
				<td>
					<img src="<%=ImageStorageTootl.getUrl(bean.getImage())%>">
				</td>
				<td><%=bean.getDescription()%></td>
				<td><%=bean.getCreateDate()%></td>
				<td>
					<a href="javascript:delete1(<%=bean.getId()%>)">删除</a>
				</td>
			</tr>
			<%
			    }
			%>
		</table>
	</body>
</html>
