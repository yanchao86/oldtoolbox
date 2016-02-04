<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");


StringBuilder html = new StringBuilder();
JSONObject definition = new JSONObject();
StringBuilder allGridHtml = new StringBuilder();
Map<String, Object> def = null;
String method = "customCreate.do";
String disabled = "";
if(result != null) {
    List<Map<String, Object>> allGrid = (List<Map<String, Object>>) result.get("allGrid");
    for(Map<String, Object> grid : allGrid) {
        allGridHtml.append("<option value='"+grid.get("name")+"'>"+grid.get("comment")+"</option>");
    }
    
    def = (Map<String, Object>) result.get("def");
    if(def != null) {
		definition = JSONObject.fromObject(def.get("definition"));
		parse(html, definition, "root", allGrid);
		method = "customUpdate.do";
		disabled = "disabled";
    }
}
%>

<%!
public void parse(StringBuilder html, JSONObject def, String pathId, List<Map<String, Object>> allGrid) {
    String id = def.optString("_id");
    String key = def.optString("key");
    String name = def.optString("name");
    
    if(!"root".equals(pathId)){
        html.append(String.format("<ul>"));
        html.append(String.format("<li id='%s'>", pathId));
        html.append(String.format("<table id='customers'><tr>"));
        html.append(String.format("<th>代码</th><td><input type='text' name='%s_key' id='%s_key' value='%s' class='readonly' disabled></td>",id,id,key));
        html.append(String.format("<th>描述</th><td><input type='text' name='%s_name' id='%s_name' value='%s'>",id,id,name));
        html.append(String.format("<button onclick=creP('%s')>属性</button>",pathId));
        html.append(String.format("<button onclick=creG('%s')>组</button></td>",pathId));
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
	        html.append(String.format("<table id='customers'><tr><th>代码</th>"));
	        html.append(String.format("<td><input type='text' name='%s_key' id='%s_key' value='%s' class='readonly' disabled></td>",pId,pId,pKey));
	        html.append(String.format("<th>描述</th>"));
	        html.append(String.format("<td><input type='text' name='%s_name' id='%s_name' value='%s'>",pId,pId,pName));
	        html.append(String.format("<th>类型</th>"));
	        html.append(String.format("<td><select name='%s_type' id='%s_type' onchange='typeChange(this)' class='readonly' disabled>",pId,pId));
	        html.append(String.format("<option value='0' %s>文本</option>",pType==0?"selected":""));
	        html.append(String.format("<option value='1' %s>单选</option>",pType==1?"selected":""));
	        html.append(String.format("<option value='2' %s>多选</option>",pType==2?"selected":""));
	        html.append(String.format("<option value='3' %s>时间</option>",pType==3?"selected":""));
	        html.append(String.format("<option value='4' %s>文件</option>",pType==4?"selected":""));
	        html.append(String.format("<option value='5' %s>BOOLEAN</option>",pType==5?"selected":""));
	        html.append(String.format("<option value='6' %s>关联表</option>",pType==6?"selected":""));
	        html.append(String.format("</select><span id='%s_type_span'>",pId));
	        if(property.containsKey("valueList")) {
		        JSONArray valueList = property.getJSONArray("valueList");
		        for(int m=0; m<valueList.size(); m++) {
		            JSONObject value = valueList.getJSONObject(m);
			        html.append(String.format("<table><tr>"));
			        html.append(String.format("<th>代码</th><td><input type='text' name='%s_type_code' value='%s' class='readonly' disabled></td>",pId,value.optString("key")));
			        html.append(String.format("<th>描述</th><td><input type='text' name='%s_type_name' value='%s'></td>",pId,value.optString("name")));
			        html.append(String.format("</tr></table>"));
		        }
	        }
	        if(property.containsKey("time")) {
	            html.append(String.format("<table><tr><th>时间格式</th><td><input type='text' id='%s_type_time' value='%s' class='readonly' disabled></td></tr></table>",pId,property.optString("time")));
	        }
	        if(property.containsKey("grid")) {
	            String value = property.optString("grid", "");
	            html.append(String.format("<select id='%s_type_grid' disabled>", pId));
	            for(Map<String, Object> grid : allGrid) {
	                html.append(String.format("<option value='"+grid.get("name")+"' %s>"+grid.get("comment")+"</option>", value.equals(grid.get("name"))?"selected":""));
	            }
				html.append("</select>");
				
				int gridType = property.optInt("gridType", 0);
				html.append(String.format("<select id='%s_type_grid_type' disabled>", pId));
				html.append(String.format("<option value='0' %s>单选</option>", gridType==0?"selected":""));
				html.append(String.format("<option value='1' %s>多选</option>", gridType==1?"selected":""));
				html.append(String.format("<option value='2' %s>全选</option>)", gridType==2?"selected":""));
				html.append("</select>");
				
	        }
	        html.append(String.format("</td></tr></table>"));
	        html.append(String.format("</li></ul>"));
	    }
    }
    
    JSONArray groups = def.optJSONArray("groups");
    if(groups != null){
	    for(int i=0; i<groups.size(); i++) {
	        JSONObject group = groups.getJSONObject(i);
	        parse(html,group,pathId + "_" + group.optString("_id"), allGrid);
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
ul li {
	list-style: none;
}

.readonly {
	border: none;
}
</style>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
<script type="text/javascript" src="<%=basePath%>js/uuid.jsp?total=2000" language="JavaScript"></script>
<script type="text/javascript">
		var _g_def = <%=definition%>;
		
		function arrayRemove(arr,key,value){
			var removeObj=null,n=0;
			for(var i=0;i<arr.length;i++){
				if(arr[i][key] == value){
					removeObj = arr[i];
				}else{
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
				    			"<th>代码</th>",
				    			"<td><input type='text' name='",newId,"_key' id='",newId,"_key'></td>",
					    		"<th>描述</th>",
					    		"<td><input type='text' name='",newId,"_name' id='",newId,"_name'></td>",
					    		"<th>类型</th><td>",
					    		"<select name='",newId,"_type' id='",newId,"_type' onchange='typeChange(this)'>",
					    		"<option value='0'>文本</option>",
					    		"<option value='1'>单选</option>",
					    		"<option value='2'>多选</option>",
					    		"<option value='3'>时间</option>",
					    		"<option value='4'>文件</option>",
					    		"<option value='5'>boolean</option>",
					    		"<option value='6'>关联表</option>",
				    			"</select><span id='",newId,"_type_span'>",
				    			"</td><td><button onclick=delProp('",pathId,"','",newId,"')>X</button></td></tr>",
			    		"</table>",
				    "</li></ul>"
			];
			$(html.join("")).appendTo("#"+pathId);
		}
	
		function delProp(pathId,propId){
			var group = getGroup(pathId);
			arrayRemove(group.properties,"_id",propId);
			$("#ul"+propId).remove();
		}
		
		function pushProp(pathId/*k.k.k.k*/,prop){
			var group = getGroup(pathId);
			if(group.properties == undefined){
				group.properties = [];
			}
			group.properties.push(prop);
		}
		
		function creG(pathId) {
			var newId = uuid();
			var newPathId = pathId + "_" + newId;
			pushGroup(pathId,{"_id":newId});
			
			var html = [
				"<ul id='ul",newId,"'>",
					"<li id='",newPathId,"'>",
						"<table id='customers'>",
							"<tr><th>代码</th>",
							"<td><input type='text' name='",newId,"_key' id='",newId,"_key'></td>",
							"<th>描述</th>",
							"<td><input type='text' name='",newId,"_name' id='",newId,"_name'>",
							"<button onclick=creP('",newPathId,"')>属性</button>",
							"<button onclick=creG('",newPathId,"')>组</button>",
							"</td><td><button onclick=delGroup('",pathId,"','",newId,"')>X</button></td></tr>",
						"</table>",
					"</li>",
				"</ul>"
			];
			$(html.join("")).appendTo("#"+pathId);
		}
		
		function delGroup(pathId,groupId){
			var group = getGroup(pathId);
			arrayRemove(group.groups,"_id",groupId);
			$("#ul"+groupId).remove();
		}
		
		function pushGroup(pathId/*k.k.k.k*/,clGroup){
			var group = getGroup(pathId);
			if(group.groups == undefined){
				group.groups = [];
			}
			group.groups.push(clGroup);
		}
		
		
		//root_uuid0_uuid1_uuid2_uuid3_uuid4_uuid5
		function getGroup(pathId/*k.k.k.k*/){
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
			//alert(JSON.stringify(gg));
			return gg;
		}
		
		function getValue(id, allowNull) {
			var val = $("#" + id).val();
			if(!allowNull && !val) {
				$("#"+id).focus().css("background-color", "red");
				throw id+" 值不能为空！！！！";				
			}
			$("#"+id).focus().css("background-color", "");
			return val;
		}
		
		function fillValue(def) {
			var properties = def.properties;
			def.key = getValue(def._id + "_key", false);
			def.name = getValue(def._id + "_name", false);
			if(properties){
				for(var i=0;i<properties.length;i++){
					var property = properties[i];
					property.key = getValue(property._id + "_key", false);
					property.name = getValue(property._id + "_name", false);
					property.type = getValue(property._id + "_type", false);
					
					if(property.type == 3) {	//时间
						property.time = getValue(property._id + "_type_time", false);
					}
					if(property.type == 1 || property.type == 2) {	//单选、多选
						var codes = document.getElementsByName(property._id + "_type_code");
						var names = document.getElementsByName(property._id + "_type_name");
						var valueList = [];
						for(var m=0;m<codes.length;m++) {
							var check = {};
							check.key = codes[m].value;
							check.name = names[m].value;
							valueList.push(check);
						}
						property.valueList = valueList;
					}
					if(property.type == 6) {
						property.grid = getValue(property._id + "_type_grid", false);
						property.gridType = getValue(property._id + "_type_grid_type", false);
						
					}
				}	
			}
			
			var groups = def.groups;
			if(groups){
				for(var i=0;i<groups.length;i++){
					var group = groups[i];
					fillValue(group);
				}	
			}
		}
		
		function typeChange(obj) {
			var val = $(obj).val();
			var id = $(obj).attr("id");
			var html = [];
			if(val == 0) {	//文本

			} else if(val == 1 || val == 2) {	//单选/多选
				html.push("<button onclick=addCheckbox('",id,"')>添加选项</button>");
			} else if(val == 3) {	//时间
				html.push("<table><tr><th>时间格式</th><td><input type='text' id='",id,"_time'></td></tr></table>");
			} else if(val == 4) {	//文件

			} else if(val == 6) {	//关联表
				html.push("<select name='",id,"_grid' id='",id,"_grid'>");
				html.push("<%=allGridHtml.toString()%>");
				html.push("</select>");
			
				html.push("<select name='",id,"_grid_type' id='",id,"_grid_type'>");
				html.push("<option value='0'>单选</option><option value='1'>多选</option><option value='2'>全选</option>");
				html.push("</select>");
			}
			$("#"+id+"_span").html(html.join(""));			
		}
		
		function addCheckbox(selectId) {
			var html = ["<table><tr>",
			            	"<th>代码</th><td><input type='text' name='",selectId,"_code'></td>",
			            	"<th>描述</th><td><input type='text' name='",selectId,"_name'></td>",
			            "</tr></table>"];
			$(html.join("")).appendTo("#"+selectId+"_span");
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
	<div>
		<ul>
			<li id="root">
				<table id="customers">
					<tr>
						<th>功能代码</th>
						<td><input type="text" name="root" id="root_key"
							value="<%=def != null ? def.get("code") : ""%>"
							class="<%=def != null ? "readonly" : ""%>" <%=disabled%>></td>
						<th>描述</th>
						<td><input type="text" name="name" id="root_name" value="<%=def != null ? def.get("name") : ""%>"></td>
						<td>
							<button onclick="creP('root')">属性</button>
							<button onclick="creG('root')">组</button>
						</td>
					</tr>
				</table> <%=html%>
			</li>
		</ul>
	</div>
</body>
</html>
