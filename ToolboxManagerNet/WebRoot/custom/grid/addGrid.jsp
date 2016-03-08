<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
StringBuilder html = new StringBuilder();;
JSONObject definition = new JSONObject();;
Map<String, Object> gridInfo = null;
String method = "addGrid.do";
String disabled = "";
if(result != null) {
    gridInfo = (Map<String, Object>) result.get("gridInfo");
	definition = JSONObject.fromObject(gridInfo.get("definition"));
	
	parse(html, definition, "root");
	method = "updateGrid.do";
	disabled = "disabled";
}
%>

<%!
public void parse(StringBuilder html, JSONObject def, String pathId) {
    String id = def.optString("_id");
    String key = def.optString("key");
    String name = def.optString("name");
    
    if(!"root".equals(pathId)){
        html.append(String.format("<ul>"));
        html.append(String.format("<li id='%s'>", pathId));
        html.append(String.format("<table id='customers'><tr>"));
        html.append(String.format("<th>名称</th><td><input type='text' name='%s_key' id='%s_key' value='%s' class='readonly' disabled></td>",id,id,key));
        html.append(String.format("<th>描述</th><td><input type='text' name='%s_name' id='%s_name' value='%s'>",id,id,name));
        html.append(String.format("<button onclick=creP('%s')>属性</button>",pathId));
        html.append(String.format("</tr></table>"));
        
    }
    
    JSONArray properties =  def.optJSONArray("properties");
    if(properties != null){
	    for(int index=0;index<properties.size();index++){
	        JSONObject property = properties.getJSONObject(index);
	        String pId = property.optString("_id");
	        String pKey = property.optString("key");
	        String pName = property.optString("name");
	        int pType = property.optInt("type");
	        
	        html.append(String.format("<ul><li>"));
	        html.append(String.format("<table id='customers'><tr><th>名称</th>"));
	        html.append(String.format("<td><input type='text' name='%s_key' id='%s_key' value='%s' class='readonly' disabled></td>",pId,pId,pKey));
	        html.append(String.format("<th>描述</th>"));
	        html.append(String.format("<td><input type='text' name='%s_name' id='%s_name' value='%s'>",pId,pId,pName));
	        html.append(String.format("<th>类型</th>"));
	        html.append(String.format("<td><select name='%s_type' id='%s_type' class='readonly' disabled>",pId,pId));
	        html.append(String.format("<option value='0' %s>数字</option>",pType==0?"selected":""));
	        html.append(String.format("<option value='1' %s>文本</option>",pType==1?"selected":""));
	        html.append(String.format("<option value='2' %s>时间</option>",pType==2?"selected":""));
	        html.append(String.format("<option value='3' %s>BOOLEAN</option>",pType==3?"selected":""));
	        html.append(String.format("<option value='4' %s>关联表</option>",pType==4?"selected":""));
	        html.append(String.format("<option value='5' %s>文件</option>",pType==5?"selected":""));
	        html.append(String.format("</select><span id='%s_type_span'>",pId));
	        if(property.containsKey("extKey")) {
		        html.append(String.format("<table><tr>"));
		        html.append(String.format("<th>关联表</th><td><input type='text' id='%s_type_extKey' name='%s_type_extKey' value='%s' class='readonly' disabled></td>",pId,pId,property.optString("extKey")));
		        html.append(String.format("<th>显示字段</th><td><input id='%s_type_extKey_view' name='%s_type_extKey_view' value='%s' class='readonly' disabled></td>", pId,pId,property.optString("extKeyView")));
		        html.append(String.format("</tr></table>"));
	        }
	        html.append(String.format("</td></tr></table>"));
	        html.append(String.format("</li></ul>"));
	    }
    }
    
    if(!"root".equals(pathId)){
        html.append(String.format("</li>"));
        html.append(String.format("</ul>"));
    }
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<style type="text/css">
ul li {list-style: none;}
.readonly {border: none;}
</style>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
<script type="text/javascript" src="<%=basePath%>js/json2.js" language="JavaScript"></script>
<script type="text/javascript" src="<%=basePath%>js/uuid.jsp?total=2000" language="JavaScript"></script>
<script type="text/javascript">
	var allTalbleHtml = [];
	var tableColumns = [];
	$(function() {
		$.post('<%=basePath%>manager/gridList.do', {}, function(data) {
			var gridList = data.data.gridList;
			for(var grid in gridList) {
				allTalbleHtml.push("<option value='"+gridList[grid].code+"'>"+gridList[grid].name+"</option>");
				
				var columnHtml = [];
				var properties = JSON.parse(gridList[grid].definition).properties;
				$.each(properties, function() {
					columnHtml.push("<option value='"+this.key+"'>"+this.name+"</option>");
				});
					
				var tableColumn = {};
				tableColumn.table = gridList[grid].code;
				tableColumn.columnHtml = columnHtml.join("");
				tableColumns.push(tableColumn);
			}
		});
		
		
	});
	var _g_def = <%=definition%>;
	
	function arrayRemove(arr,key,value) {
		var removeObj=null,n=0;
		for(var i=0;i<arr.length;i++){
			if(arr[i][key] == value){
				removeObj = arr[i];
			} else {
				arr[n]=arr[i];
				n++;
			}
		}
		arr.length=n;
		return removeObj;
	}
		
	function creP(pathId) {
		var newId = uuid();
		pushProp(pathId,{"_id":newId});
		var html = [
			"<ul id='ul",newId,"'>",
			    "<li>",
			    	"<table id='customers'>",
		    			"<tr>",
			    			"<th>名称</th>",
			    			"<td><input type='text' name='",newId,"_key' id='",newId,"_key'></td>",
				    		"<th>描述</th>",
				    		"<td><input type='text' name='",newId,"_name' id='",newId,"_name'></td>",
				    		"<th>类型</th><td>",
				    		"<select name='",newId,"_type' id='",newId,"_type' onchange='typeChange(this)'>",
				    		"<option value='0'>数字</option>",
				    		"<option value='1'>文本</option>",
				    		"<option value='2'>时间</option>",
				    		"<option value='3'>boolean</option>",
				    		"<option value='4'>关联表</option>",
				    		"<option value='5'>文件</option>",
			    			"</select><span id='",newId,"_type_span'>",
			    			"</td><td><button onclick=delProp('",pathId,"','",newId,"')>X</button></td></tr>",
		    		"</table>",
			    "</li></ul>"
		];
		$(html.join("")).appendTo("#"+pathId);
	}
		
	function pushProp(pathId,prop){
		var group = getGroup(pathId);
		if(group.properties == undefined){
			group.properties = [];
		}
		group.properties.push(prop);
	}
	
	function getGroup(pathId){
		var arr = pathId.split("_");
		var gg = _g_def;
		for(var i=1;i<arr.length;i++){
			var groupId = arr[i];
			var groups = gg.groups;
			for(var g=0;g<groups.length;g++){
				var group = groups[g];
				if(group["_id"] == groupId){
					gg = group;
				}
			}
		}
		return gg;
	}
	
	function delProp(pathId,propId){
		var group = getGroup(pathId);
		arrayRemove(group.properties,"_id",propId);
		$("#ul"+propId).remove();
	}

	function getValue(id, allowNull) {
		var val = $("#" + id).val();
		if(!allowNull && !val) {
			$("#"+id).focus().css("background-color", "red");
			throw id+" 值不能为空！！！！";				
		}
		if("id" == val) {
			$("#"+id).focus().css("background-color", "red");
			throw id+" 系统会默认创建id，请删除！ ";
		}
		$("#"+id).focus().css("background-color", "");
		return val;
	}
	
	function fillValue(def) {
		var properties = def.properties;
		def.key = getValue(def._id + "_key", false);
		def.name = getValue(def._id + "_name", false);
		if(properties) {
			for(var i=0;i<properties.length;i++){
				var property = properties[i];
				property.key = getValue(property._id + "_key", false);
				property.name = getValue(property._id + "_name", false);
				property.type = getValue(property._id + "_type", false);
				property.extKey = getValue(property._id + "_type_extKey", true);
				property.extKeyView = getValue(property._id + "_type_extKey_view", true);
			}	
		}
	}
		
	function typeChange(obj) {
		var val = $(obj).val();
		var id = $(obj).attr("id");
		var html = [];
		if(val == 4) {	//关联表
			html.push("<table><tr><td><select id='",id,"_extKey' onChange='gridChange(this)'>",allTalbleHtml.join(""),"</select></td>");
			html.push("<th>显示字段</th><td><select id='",id,"_extKey_view'>",tableColumns[0].columnHtml,"</select></td>");
			html.push("</tr></table>");
		}
		$("#"+id+"_span").html(html.join(""));
	}
	
	function gridChange(obj) {
		var id = $(obj).attr("id");
		var selectedIndex = $(obj).prop('selectedIndex');
		$("#"+id+"_view").html(tableColumns[selectedIndex].columnHtml);
		
	}
	
	function saveSetting() {
		_g_def._id = "root";
		try { 
			fillValue(_g_def);
			$.post("<%=basePath%>manager/<%=method%>", {
			"code" : _g_def.key,
			"name" : _g_def.name,
			"definition" : JSON.stringify(_g_def)
		}, function(data) {
			if(data.result == 0) {
				alert('成功！');
			} else{
				alert('失败！');
			}
		});
		} catch (er) {
			alert(er);
		}
	}
</script>
</head>

<body>
	<button onclick="saveSetting('start')">保存配置</button>
	<ul>
		<li id="root">
			<table id="customers">
				<tr>
					<th>表名</th>
					<td><input type="text" name="root" id="root_key"
						value="<%=gridInfo != null ? gridInfo.get("code") : ""%>"
						class="<%=gridInfo != null ? "readonly" : ""%>" <%=disabled%>></td>
					<th>描述</th>
					<td><input type="text" name="name" id="root_name" value="<%=gridInfo != null ? gridInfo.get("name") : ""%>"></td>
					<td>
						<button onclick="creP('root')">字段</button>
					</td>
				</tr>
			</table> <%=html%>
		</li>
	</ul>
</body>
</html>
