<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@page import="com.pixshow.login.tools.PermissionTool"%>
<%
String basePath = WebUtility.getBasePath(request);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>后台管理</title>
	<style type="text/css">
		body { font-family:Verdana; font-size:14px; margin:1;}
		.container {margin:0 auto; width:100%;}
		.header { height:12%; background:#6cf; margin-bottom:1px;}
		.mainContent { margin-bottom:1px;}
		.sidebar { float:left; width:15%; height:88%; background:#9ff;}
		.main { float:right; width:85%; height:88%; background:#cff; text-align: center;}/*因为是固定宽度，采用左右浮动方法可有效避免ie 3像素bug*/		
	</style>
	
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	<script type="text/javascript">
		function loadData(url) {
			$('#content').attr('src',url);
		}
	</script>
  </head>
  
  <body>
    
	<div class="container">
	  <div class="header">
	  	手机铃声后台管理系统
	  	<a href="<%=basePath %>logout.do">登出</a>
	  </div>
	  <div class="mainContent">
	    <div class="sidebar">
    		<ul>
    		<%if(PermissionTool.validate(session, "all")) {%>
    			<li><a href="javascript: loadData('<%=basePath%>updatePush.jsp')">更新推送</a></li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>
    			<li>手电筒
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>manager/messageSearch.do?appCode=APP1')">修改信息</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>		
    			<li>应用2
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>manager/messageSearch.do?appCode=APP2')">修改信息</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>	 	
    			<li>应用3
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>manager/messageSearch.do?appCode=APP3')">修改信息</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>	
    			<li>应用4
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>manager/messageSearch.do?appCode=APP4')">修改信息</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>	
    			<li>应用5
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>manager/messageSearch.do?appCode=APP5')">修改信息</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>	
    			<li>工具箱
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>toolbox/insertToolbox.jsp')">添加工具</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/toolSearch.do')">工具列表</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/toolboxInfo.do')">工具箱配置</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/channelToolSource.do')">工具箱渠道来源控制</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>toolbox/recycle.jsp')">回收站</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>	
    			<li>装机必备
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>diybox/insertDiybox.jsp')">添加工具</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/diySearch.do')">工具列表</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/diyboxInfo.do')">工具箱配置</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "stat")) {%>	
    			<li>统计管理
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>manager/statCodeKeyValues.do?')">code描述</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>stat/statSearch.jsp')">自定义查询</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/statCatList.do')">统计列表</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>stat/json.jsp')">JSON解密</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>wandoujia/weeklytopgame.jsp')">豌豆荚游戏周排行榜</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>stat/statistics.jsp')">最新统计计算</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/saveStatWaring.do')">统计预警设置</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>
    			<li>自定义配置
    				<ul>
    					<%if(PermissionTool.validate(session, "develop")) {%>
    					<li><a href="javascript: loadData('<%=basePath%>manager/defInfo.do')">创建一个配置</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/defList.do')">配置列表</a></li>
    					<%} %>
    					<li><a href="javascript: loadData('<%=basePath%>manager/customList.do')">编辑配置列表</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>
    			<li>自定义表格
    				<ul>
    					<%if(PermissionTool.validate(session, "develop")) {%>
    					<li><a href="javascript: loadData('<%=basePath%>custom/grid/addGrid.jsp')">创建一个表格</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>custom/grid/gridList.jsp')">表格列表</a></li>
    					<%} %>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>
    			<li>apk打包签名
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>manager/apkKey.do')">密钥维护</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/apkProduct.do')">产品维护</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/apkChannel.do')">渠道维护</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/apkSignView.do')">打包</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>
				<li><a href="javascript: loadData('<%=basePath%>manager/uploadHistory.do')">apk上传</a></li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>
    			<li>游戏闪屏
    				<ul>
    					<li><a href="javascript: loadData('<%=basePath%>manager/glitter.do')">配置闪屏</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/glitterRecycle.do')">闪屏回收站</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/suspend.do')">配置暂停</a></li>
    					<li><a href="javascript: loadData('<%=basePath%>manager/suspendRecycle.do')">闪屏回收站</a></li>
    				</ul>
    			</li>
    		<%}%>
    		<%if(PermissionTool.validate(session, "all")) {%>
    			<li><a href="">游戏暂停界面</a></li>
    		<%}%>
    		</ul>
	    </div>
	    <iframe id="content" frameborder="0" width="85%" height="88%" src="<%=basePath %>welcome.jsp"></iframe>
	  </div>
	</div>
  </body>
</html>
