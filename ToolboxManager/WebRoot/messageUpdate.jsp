 <%@page import="com.pixshow.toolboxmgr.tools.CheckUrlUtil"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%
String basePath = WebUtility.getBasePath(request);
String appCode = (String) request.getAttribute("appCode");
String str = (String) request.getAttribute("config");
JSONObject json = JSONObject.fromObject(str);
JSONObject adjson = json.getJSONObject("toolbarAdvertisement");
JSONObject dljson = json.getJSONObject("download");
JSONObject dtjson = json.getJSONObject("detail");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/json2.js"></script>
<script type="text/javascript">
function check1() {
	var adUrl = document.getElementById('adUrl').value;
	var name = document.getElementById('name').value;
	var dlUrl = document.getElementById('dlUrl').value;
	var dtUrl = document.getElementById('dtUrl').value;
	
	if(name == ''){
		alert("名称不能为空！");
		return false;
	}
	if (adUrl == '') {
		alert("广告地址栏不能为空！");
		return false;
	}
	if (dlUrl == '') {
		alert("下载地址不能为空！");
		return false;
	}
	if (dtUrl == '') {
		alert("详细地址不能为空！");
		return false;
	}
}
$(function() {
	$.post('<%=basePath%>manager/customGridData.do', {'grid':'market_package'}, function(data) {
		var html = [];
		$.each(data.data, function(){
			var selected='';
			if('<%=json.optJSONObject("marketPackage")!=null?json.optJSONObject("marketPackage").optString("packageName"):""%>' == this.packageName) {
				selected='selected';
			}
			html.push("<option value='"+JSON.stringify(this)+"' "+selected+">"+this.marketName+"</option>");
		});
		$(html.join("")).appendTo("#marketPackage");
	});
	
})
</script>
<style>
.font3{font-size: 16px; color: red; font-style: italic}
</style>
</head>

<body>
	<form action="<%=basePath%>manager/messageUpdate.do" method="post" enctype="multipart/form-data" onsubmit="return check1()">
	<input type="hidden" id="appCode" name="appCode" value="<%=appCode%>" />
	<table id="customers" width="98%">
		<tr>
			<th>名称</th>
			<td><input type="text" id="name" name="name" value="<%=json.optString("name")%>" size="50"/></td>
		</tr>
		<tr>
			<th>图标</th>
			<td>
		 		修改：<input type="file" name="icon" id="icon" />
				<img src="<%=ImageStorageTootl.getUrl(json.optString("icon"))%>"  width="200" height="200"/> 
		 	</td>
		</tr>
		<tr>
			<th>工具箱图标</th>
			<td>
		 		修改：<input type="file" name="toolIcon" id="toolIcon" />
				<img src="<%=ImageStorageTootl.getUrl(json.optString("toolIcon"))%>"  width="200" height="200"/> 
		 	</td>
		</tr>
		<tr>
			<th>广告</th>
			<td>
				<table>
					<tr>
						<th>是否启用</th>
						<td><input type="checkbox" id="adCheckbox" name="adCheckbox" <%=adjson.optBoolean("active") ? "checked" : ""%> /></td>
					</tr>
					<tr>
						<th>图标</th>
						<td>
							修改：<input type="file" name="adicon" id="adicon"/>
							<img src="<%=ImageStorageTootl.getUrl(adjson.optString("icon"))%>" width="200" height="200"/>
						</td>
					</tr>
					<tr>
						<th>地址</th>
						<td><input type="text" id="adUrl" name="adUrl" value="<%=adjson.optString("url")%>" size="50"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<th>下载地址
			<%
			String downurl = dljson.getString("url");
			 JSONObject checkDown =  new CheckUrlUtil().checkDownloadUrl(downurl);
			 if(checkDown.containsKey("error")) {%>
			      <font class="font3">
				    	<%=checkDown.getString("error") %>
				    	<%=checkDown.containsKey("nodes")?checkDown.getJSONArray("nodes").toString():"" %>
				    </font>
			<%} %>
			</th>
			<td>
				<input type="text" id="dlUrl" name="dlUrl" value="<%=dljson.getString("url")%>" size="50"/>
				是否在wifi自动下载<input type="checkbox" id="dlCheckbox" name="dlCheckbox" <%=dljson.optBoolean("auto") ? "checked" : ""%> />
				下载时间<input type="text" id="dlTime" name="dlTime" value="<%=dljson.optString("dlTime")%>" />
				下载完成提示<input type="checkbox" id="dlDoneAlert" name="dlDoneAlert" <%=dljson.optBoolean("doneAlert") ? "checked" : ""%> />
			</td>
		</tr>
		<tr>
			<th>详细地址</th>
			<td>
				<input type="text" id="dtUrl" name="dtUrl" value="<%=dtjson.optString("url")%>" size="50"/>
				是否启用<input type="checkbox" id="dtCheck" name="dtCheck" <%=dtjson.optBoolean("open") ? "checked" : ""%> />
			</td>
		</tr>
		<tr>
			<th>导量市场包名</th>
			<td>
				<select name="marketPackage" id="marketPackage">
					<option value="">--不用市场--</option>
				</select>
				<a href="<%=basePath %>manager/gridDataPage.do?code=market_package">编辑数据</a>
			</td>
		</tr>
		<tr>
			<th>包名</th>
			<td><input type="text" id="packageName" name="packageName" value="<%=json.optString("packageName")%>" size="50" /></td>
		</tr>
		<tr>
			<th>版本号(versionCode)</th>
			<td><input type="text" name="versionCode" value="<%=json.optString("versionCode")%>" size="50"/></td>
		</tr>
		<tr>
			<th colspan="2"><input type="submit" value="确定修改" /></th>
		</tr>
	</table>
	</form>
</body>
</html>
