<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="com.pixshow.framework.utils.AppContextUtility"%>
<%@page import="com.pixshow.custom.grid.GridDataService"%>
<%@page import="com.pixshow.custom.grid.CustomGridService"%>
<%@page import="org.springframework.context.support.ClassPathXmlApplicationContext"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");

Map<String, Object> gridTable = (Map<String, Object> )result.get("gridTable");
JSONObject definition = JSONObject.fromObject(gridTable.get("definition"));
JSONArray pros = definition.getJSONArray("properties");
List<Map<String, Object>> gridData = (List<Map<String, Object>>) result.get("gridData");
%>
<%!
public String getExtKey(String selectName, String extKey, String extKeyView, String selectId,  int dataId) {
    GridDataService service = AppContextUtility.getContext().getBean(GridDataService.class);
    List<Map<String, Object>> list = service.getData(extKey, "asc");
    if(list.size() == 0) {
        return "-关联表("+extKey+")无数据-";
    }
    StringBuilder html = new StringBuilder("<select id='"+selectId+"' name='"+selectName+"'>");
    for(int i=0;i<list.size();i++) {
        Map<String, Object> map = list.get(i);
        int value = (Integer) map.get("id");
        String selected = value==dataId?"selected":"";
        html.append("<option value='"+value+"' "+selected+">"+(StringUtility.isNotEmpty(extKeyView)?map.get(extKeyView):map)+"</option>");
    }
    return html.append("</select>").toString();
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>plugin/dialog/jquery-ui.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js"></script>
<script type="text/javascript"src="<%=basePath%>plugin/dialog/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>plugin/date/lhgcalendar.min.js"></script>
<script type="text/javascript">
	$(function() {
		$("#zutiao").dialog({
			minWidth: 500,
			autoOpen: false,
			modal: true,
			buttons: {
				"保存": function() {
					var data = [];
					$("input[id=zutiaodata]").each(function() {
						var json = {};
						json.name=$(this).attr("name");
						json.value=$(this).val();
						data.push(json);
					});
					$("select[id=zutiaodata]").each(function() {
						var json = {};
						json.name=$(this).attr("name");
						json.value=$(this).val();
						data.push(json);
					});
					$("textarea[id=zutiaodata]").each(function() {
						var json = {};
						json.name=$(this).attr("name");
						json.value=$(this).val();
						data.push(json);
					});
					$.post("<%=basePath%>manager/addGridData.do", {"tableName":"<%=gridTable.get("code") %>", "data":JSON.stringify(data)}, function(data) {
						if(data.result == 0) {
							alert("保存成功！");
							window.location.reload();
						} else {
							alert("保存失败！");
						}
					});
					$('#zutiaodata_from').submit();
				},
			}
		});
	});
	function zutiao() {
		$("#zutiao").dialog("open");
	}
	function delData(id) {
		if(confirm("确定删除么？")) {
			$.post("<%=basePath%>manager/delGridData.do", {"tableName":"<%=gridTable.get("code") %>", "dataId":id}, function(data) {
				if(data.result == 0) {
					alert("删除成功！");
					window.location.reload();
				} else {
					alert("删除失败！");
				}
			});
		}
	}
	function updateData(id, filters) {
		var updateSql = [];
		var arr = filters.split(",");
		for(var i in arr) {
			updateSql.push(arr[i]+"='"+$("#"+arr[i]+"_"+id).val()+"'");
		}
		$.post("<%=basePath%>manager/updateGridData.do", {"tableName":"<%=gridTable.get("code") %>", "updateSql":updateSql.join(","), "dataId":id}, function(data) {
			if(data.result == 0) {
				alert("修改成功！");
				window.location.reload();
			} else {
				alert("修改失败！");
			}
		});
	}
</script>
</head>
<body>
<div id="zutiao" title="逐条添加">
<form action="<%=basePath %>customGridConfig" method="post" enctype="multipart/form-data" id="zutiaodata_from">
<input type="hidden" name="tableName" value="<%=gridTable.get("code") %>">
	<table id="customers">
	<%for(int i=0;i<pros.size();i++) {
	    JSONObject pro = pros.getJSONObject(i);
	    int type = pro.getInt("type");
	    String typyStr = "";
	    switch(type) {
	    	case 0: typyStr = "数字"; break;
	    	case 1: typyStr = "文本"; break;
	    	case 2: typyStr = "时间"; break;
	    	case 3: typyStr = "BOOLEAN"; break;
	    	case 4: typyStr = "关联表"; break;
	    	default: typyStr = "无";
	    }%>
		<tr>
		    <th><%=pro.get("key") %></th>
		<%if(type == 0) {%>
		    <td><input type="text" id="zutiaodata" name="<%=pro.get("key") %>" onkeyup="value=value.replace(/[^\d]/g,'')"></td>
		<%} else if(type == 1) {%>
		    <td><textarea rows="3" cols="20" id="zutiaodata" name="<%=pro.get("key") %>"></textarea></td>
		<%} else if(type == 2) {%>
			<script type="text/javascript">
				$(function() {
					$('.zutiaodata_<%=i %>').calendar({ format:'yyyy-MM-dd HH:mm:ss' });
				});
			</script>
		    <td><input type="text" id="zutiaodata" class="zutiaodata_<%=i %>" name="<%=pro.get("key") %>"></td>
        <%} else if(type == 3) {%>
		  	  <td>
		  	  	<select id="zutiaodata" name="<%=pro.get("key") %>">
		  	  		<option value="0">FALSE</option>
		  	  		<option value="1">TRUE</option>
		  	  	</select>
		  	  </td>
		<%} else if(type == 4) {
			String html = getExtKey(pro.getString("key"), pro.optString("extKey"), pro.optString("extKeyView"), "zutiaodata", -1);
		%>
		    <td><%=html %></td>
		<%} else if(type == 5) {%>
		    <td>
		    	<input type="file" id="zutiaodata" name="<%=pro.get("key") %>"> 
		    </td>
		<%}%>
		</tr>
    <%}%>
	</table>
</form>
</div>
	<table id="customers">
		<tr>
			<th>表名</th>
			<td><%=gridTable.get("code") %></td>
		</tr>
		<tr>
			<th>描述</th>
			<td><%=gridTable.get("name") %></td>
		</tr>
		<tr>
			<th>排序</th>
			<td><a href="<%=basePath%>manager/gridDataPage.do?code=<%=gridTable.get("code") %>&order=asc">正序</a>
			<a href="<%=basePath%>manager/gridDataPage.do?code=<%=gridTable.get("code") %>&order=desc">倒序</a></td>
		</tr>
		<tr>
			<th>添加</th>
			<td><button onclick="zutiao()">逐条添加</button><button>批量添加</button></td>
		</tr>
	</table>

	<table id="customers">
		<tr>
			<th>id</th>
		<%
		for(int i=0;i<pros.size();i++) {
		    JSONObject pro = pros.getJSONObject(i);
		    int type = pro.getInt("type");
		    String typyStr = "";
		    switch(type) {
		    	case 0: typyStr = "数字"; break;
		    	case 1: typyStr = "文本"; break;
		    	case 2: typyStr = "时间"; break;
		    	case 3: typyStr = "BOOLEAN"; break;
		    	case 4: typyStr = "关联表"; break;
		    	case 5: typyStr = "文件"; break;
		    	default: typyStr = "无";
		    }%>
			<th><a title="<%=typyStr+"("+pro.get("name")+")" %>"><%=pro.get("key") %></a></th>
		<%}%>
			<th>操作</th>
		</tr>
		<%for(int i=0;i<gridData.size();i++) {
		    Map<String, Object> data = gridData.get(i);
		    int id = (Integer) data.get("id");%>
		<tr>
<form action="<%=basePath %>customGridConfig" method="post" enctype="multipart/form-data">
<input type="hidden" name="tableName" value="<%=gridTable.get("code") %>">
<input type="hidden" name="id" value="<%=id %>">
		    <td><%=id %></td>
		    <%for(int j=0;j<pros.size();j++) {
			    JSONObject pro = pros.getJSONObject(j);
			    String key = pro.getString("key");
			    int type = pro.getInt("type");
			    String idName = String.format("id='%s' name='%s'", key, key);
			    %>
			<td>
				<%if(type == 0) {%>
					<input type="text" <%=idName%> value="<%=data.get(key)%>"> 
				<%} else if(type == 1) {%>
					<textarea rows="2" cols="20" <%=idName%>><%=data.get(key)%></textarea>
				<%} else if(type == 2) {%>
					<script type="text/javascript">
						$(function() {
							$('.<%=key+"_"+id %>').calendar({ format:'yyyy-MM-dd HH:mm:ss' });
						});
					</script>
				    <input type="text" <%=idName%> class="<%=key+"_"+id %>" value="<%=data.get(key)%>">					
				<%} else if(type == 3) {%>
					<input type="radio" <%=idName%> value="0" <%=data.get(key)==(Object)0?"checked":"" %>>FALSE
					<input type="radio" <%=idName%> value="1" <%=data.get(key)==(Object)1?"checked":"" %>>TRUE
				<%} else if(type == 4) {%>
					<%= getExtKey(key, pro.getString("extKey"), pro.optString("extKeyView"), key, (Integer) data.get(key)) %>
				<%} else if(type == 5) {%>
					<input type="file" <%=idName%>>
					原文件：<a target="_blank" href="<%=data.get(key)%>"><img width="100" hight="100" src="<%=data.get(key)%>" /></a>
				<%}%>
			</td>    
		    <%}%>
		    <td>
		    	<button type="submit">修改</button>
				<button type="button" onclick="delData(<%=id %>)">删除</button>
		    </td>
</form>	
		</tr>
		<%}%>
	</table>
	
</body>
</html>