<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@page import="com.pixshow.toolboxmgr.bean.ToolboxBean"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%@page import="net.sf.json.JSONObject"%>
<%
    String basePath = WebUtility.getBasePath(request);
	ToolboxBean bean =(ToolboxBean) request.getAttribute("toolboxBean");
	
	String extInfo3 = bean.getExtInfo3();
	JSONObject json_3 = StringUtility.isEmpty(extInfo3)?new JSONObject():JSONObject.fromObject(extInfo3);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
		<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>js/json2.js"></script>
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
				$("#downloadUrl").val($.trim(downloadUrl));
				var detailUrl = $("#detailUrl").val();
				$("#detailUrl").val($.trim(detailUrl));
			}
			$(function(){
				$('#canMove').click(function() {
					if(this.checked) {
						$(this).val(1);
					} else {
						$(this).val(0);
					}
				});
				$.post('<%=basePath%>manager/customGridData.do', {'grid':'market_package'}, function(data) {
					var html = [];
					$.each(data.data, function(){
						var selected='';
						if('<%=json_3.optJSONObject("marketPackage")!=null?json_3.optJSONObject("marketPackage").optString("packageName"):""%>' == this.packageName) {
							selected='selected';
						}
						html.push("<option value='"+JSON.stringify(this)+"' "+selected+">"+this.marketName+"</option>");
					});
					$(html.join("")).appendTo("#marketPackage");
				});				
			});
		</script>
	</head>
	<body>
		<form action="<%=basePath%>manager/toolUpdate.do" method="post" enctype="multipart/form-data" onsubmit="return check1()">
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
					下载时间
					<input type="text" id="downloadTime" name="downloadTime" value="<%=json_3.optString("downloadTime")%>" />
					下载完成提示
					<select name="downloadDoneAlert">
						<option value="0" <%=json_3.optInt("downloadDoneAlert")==0 ? "selected" :"" %>>否</option>
						<option value="1" <%=json_3.optInt("downloadDoneAlert")==1 ? "selected" :"" %>>是</option>
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
				<th>应用详情</th>
				<td>
					<table>
						<tr>
							<th>第一张</th>
							<th>第二张</th>
							<th>第三张</th>
							<th>第四张</th>
							<th>第五张</th>
							<th>第六张</th>
						</tr>
						<tr align="center">
						<%
							JSONArray apkPic = json_3.containsKey("apkPic")?json_3.optJSONArray("apkPic"):new JSONArray();
							for(int i=0; i<6; i++) {
							    String picUrl = apkPic.optString(i);%>
							    
							<td>
								<input type="file" id="apkPic<%=i %>" name="apkPic<%=i %>"/>
								<img src="<%=picUrl%>" width="80px" height="120px">
							</td>
							<%}
						%>
						</tr>
						<tr>
							<th>应用大小</th>
							<td><input type="text" id="apkSize" name="apkSize" value="<%=json_3.optString("apkSize")%>"/>M</td>
							<th>应用描述</th>
							<td colspan="3"><textarea rows="3" cols="35" id="apkDescription" name="apkDescription"><%=json_3.optString("apkDescription") %></textarea></td>
						</tr>
					</table>
					
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
				<th>包名(packageName)</th>
				<td><input type="text" id="packageName" name="packageName" value="<%=bean.getPackageName()%>"/></td>
			</tr>
			<tr>
				<th>版本号(versionCode)</th>
				<td><input type="text" id="versionCode" name="versionCode" value="<%=bean.getVersionCode()%>"/></td>
			</tr>
			<tr>
				<th>渠道物料管理</th>
				<td>
					<textarea rows="3" cols="35" id="extInfo1" name="extInfo1"><%=bean.getExtInfo1()==null?"":bean.getExtInfo1()%></textarea>
					<select name="extInfo1_extField1">
						<option value="0" <%=json_3.optInt("extInfo1_extField1")==0 ? "selected" :"" %>>一个是只在哪显示</option>
						<option value="1" <%=json_3.optInt("extInfo1_extField1")==1 ? "selected" :"" %>>一个是不在哪显示</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>是否可移动</th>
				<td>
					<%JSONObject json = StringUtility.isEmpty(bean.getExtInfo2())?new JSONObject() : JSONObject.fromObject(bean.getExtInfo2());
					  int canMove = json.optInt("state", 0);
					  String moveIconUrl = json.optString("icon", "");
					%>
					<input type="checkbox" id="canMove" name="canMove" value="<%=canMove%>" <%=canMove==1?"checked":""%>/>
					<input type="file" id="moveIconUrl" name="moveIconUrl">
					<img alt="" src="<%=moveIconUrl%>" width="50px" height="50px">
				</td>
			</tr>
			<tr>
				<th>是否可推荐下载</th>
				<td>
					<select name="downloadRecommend">
						<option value="0" <%=json_3.optInt("downloadRecommend")==0 ? "selected" :"" %>>否</option>
						<option value="1" <%=json_3.optInt("downloadRecommend")==1 ? "selected" :"" %>>是</option>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><input type="submit" value="修改"/></th>
			</tr>
		</table>
		</form>
	</body>
</html>
