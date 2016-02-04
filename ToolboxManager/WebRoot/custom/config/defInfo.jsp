<%@page import="com.pixshow.framework.utils.AppContextUtility"%>
<%@page import="com.pixshow.custom.grid.GridDataService"%>
<%@page import="com.pixshow.framework.support.BaseDao"%>
<%@page import="org.springframework.context.support.ClassPathXmlApplicationContext"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);

Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
Map<String, Object> def = (Map<String, Object>) result.get("def");
Map<String, Object> defVal = (Map<String, Object>) result.get("defVal");
List<Map<String, Object>> allGrid = (List<Map<String, Object>>) result.get("allGrid");

JSONObject valJson = defVal != null ? JSONObject.fromObject(defVal.get("value")) : null;
JSONObject defJson = JSONObject.fromObject(def.get("definition"));
StringBuilder html = new StringBuilder();
parse(html, null, defJson, valJson, allGrid);
%>

<%!
	public void parse(StringBuilder html, String pKey, JSONObject data, JSONObject valJson, List<Map<String, Object>> allGrid) {
	    String id = data.optString("_id");
	   
	    if(!"root".equals(id)) {
		    html.append("<tr><th>"+data.optString("name")+"</th>");
		    html.append("<td><table>");
	    }
	    
    	if(data.containsKey("properties")) {
		    JSONArray properties = data.getJSONArray("properties");
		    for(int i=0; i<properties.size(); i++) {
		        JSONObject propertie = properties.getJSONObject(i);
		        String pName = propertie.optString("key");
		        pName = pKey==null?pName:pKey+"."+pName;
		        Object valueObj = getVal(pName, valJson);
			    html.append("<tr><th>"+propertie.optString("name")+"</th><td>");
		        int type = propertie.optInt("type", -1);
		        switch (type) {
		            case 0:
		                
 		                html.append(String.format("<textarea rows='2' cols='30' name='%s'>%s</textarea>", pName,valueObj));
		                break;
		            case 1:	//单选 
		                JSONArray radioList = propertie.getJSONArray("valueList");
			        	for(int m=0; m<radioList.size(); m++) {
			        	    JSONObject radio = radioList.getJSONObject(m);
			        	    String key = radio.optString("key");
			        	    String checked = "";
			        	    if(valueObj != null && key.equals(valueObj.toString())) {
			        	        checked = "checked";
			        	    }
			        	    html.append("<input type='radio' name='"+pName+"' value='"+key+"' "+checked+">");
			        	    html.append(radio.optString("name"));
			        	}
		                break;
		            case 2://多选 
		                JSONArray selectList = propertie.getJSONArray("valueList");
			        	for(int m=0; m<selectList.size(); m++) {
			        	    JSONObject select = selectList.getJSONObject(m);
			        	    String key = select.optString("key");
			        	    String checked = "";
			        	    if(valueObj != null) {
			        	        if(valueObj instanceof JSONArray) {
				                    JSONArray arr = (JSONArray) valueObj;
				                    for(int k=0;k<arr.size();k++) {
				        	            if(key.equals(arr.optString(k))) {
					        	        	checked = "checked";
					        	        	break;
				        	            }
				        	        }
				                } else {
				                    String value = valueObj.toString();
				                    if(value == key) {
				        	        	checked = "checked";
			        	            }
				                }        
			        	    }
			        	    html.append("<input type='checkbox' name='"+pName+"' value='"+key+"' "+checked+">");
			        	    html.append(select.optString("name"));
			        	}
		                break;
		            case 3://时间
		                html.append("<input type='text' name='"+pName+"' value='"+valueObj+"'>");
		                html.append("<script type='text/javascript'>");
		                html.append("$('#"+pName+"').calendar({format:'"+propertie.optString("time")+"'});");
		                html.append("</script>");
		                html.append("时间格式（"+propertie.optString("time")+"）");
		                break;
		            case 4://文件
		                html.append("<input type='file' name='"+pName+"'> 原文件：<a target='_blank' href='"+valueObj+"'><img width=100 hight=100 src='"+valueObj+"' /></a>");
		                break;
		            case 5://boolean
	                    int value = valueObj == null?0:Integer.parseInt(valueObj.toString());
		            	html.append("<select name='"+pName+"'>");
		                html.append("<option value='0'>关</option>");
		                html.append("<option value='1' "+(value==1?"selected":"")+">开</option>");
		                html.append("</select>");
		                break;
		            case 6://关联表
		                String grid = propertie.optString("grid", "");
						int gridType = propertie.optInt("gridType", 0);
						List<Map<String, Object>> gridData = gridData(grid);
						
						html.append("<table><tr>");
						html.append("<input type='hidden' name='"+pName+"_table' value='"+grid+"'>");
						if(gridType != 2 && gridData.size() > 0) {	//全选
							for(Map.Entry<String, Object> gridTable : gridData.get(0).entrySet()) {
								html.append("<td>"+gridTable.getKey()+"</td>");
							}
						}
						html.append("</tr>");
						if(gridType == 0) {	//单选
							for(Map<String, Object> map : gridData) {
							    html.append("<tr>");
							    for(Map.Entry<String, Object> gridTable : map.entrySet()) {
							        html.append("<td>");
							        if(gridTable.getKey().equals("id")) {
							            String checked = "";
							            if(valueObj!=null&&gridTable.getValue()!=null&&Integer.parseInt(gridTable.getValue().toString()) == Integer.parseInt(valueObj.toString())) {
							                checked = "checked";
							            }
							            html.append("<input type='radio' name='"+pName+"' value='"+gridTable.getValue()+"' "+checked+">");
							        }
							        html.append(gridTable.getValue());
								    html.append("</td>");
								}
							    html.append("</tr>");
							}
						} else if(gridType == 1) {	//多选
						    for(Map<String, Object> map : gridData) {
							    html.append("<tr>");
							    for(Map.Entry<String, Object> gridTable : map.entrySet()) {
							        html.append("<td>");
							        if(gridTable.getKey().equals("id")) {
							            String checked = "";
						        	    if(valueObj != null && gridTable.getValue() != null) {
						        	        if(valueObj instanceof JSONArray) {
							                    JSONArray arr = (JSONArray) valueObj;
							                    for(int k=0;k<arr.size();k++) {
							        	            if(gridTable.getValue().toString().equals(arr.optString(k))) {
								        	        	checked = "checked";
								        	        	break;
							        	            }
							        	        }
							                } else {
							                    if(Integer.parseInt(gridTable.getValue().toString()) == Integer.parseInt(valueObj.toString())) {
							        	        	checked = "checked";
						        	            }
							                }        
						        	    }
							            html.append("<input type='checkbox' name='"+pName+"' value='"+gridTable.getValue()+"' "+checked+">");
							        }
							        html.append(gridTable.getValue());
								    html.append("</td>");
								}
							    html.append("</tr>");
							}
						} else if(gridType == 2) {	//全选
						    for(Map<String, Object> map : gridData) {
							    html.append("<tr>");
							    for(Map.Entry<String, Object> gridTable : map.entrySet()) {
							        html.append("<td>");
							        if(gridTable.getKey().equals("id")) {
							            html.append("<input type='checkbox' checked disabled>");
							            html.append("<input type='hidden' name='"+pName+"' value='"+gridTable.getValue()+"'>");
							        }
							        html.append(gridTable.getValue());
								    html.append("</td>");
								}
							    html.append("</tr>");
							}
						}
						html.append("</table>");
		            	break;
		            default:
		                break;
		        }
		        html.append("</td></tr>");
		    }
    	}
    	if(data.containsKey("groups")) {
		    JSONArray groups = data.getJSONArray("groups");
		    for(int i=0; i<groups.size(); i++) {
		        JSONObject group = groups.getJSONObject(i);
		        
		        String name = group.optString("key");
				name = pKey==null?name:pKey+"."+name;
		        parse(html, name, group, valJson, allGrid);
		    }
    	}
    	if(!"root".equals(id)) {
    		html.append("</table></td></tr>");
    	}
	}

	public Object getVal(String pKey, JSONObject valJson) {
	    if(valJson == null || valJson.isNullObject() || valJson.isEmpty()) {
	        return null;
	    }
	    if(!pKey.contains(".")) {
	        return valJson.get(pKey);
	    }
	    String[] keys = pKey.split("\\.");
        String key = pKey.replace(keys[0]+".", "");
       return getVal(key, valJson.getJSONObject(keys[0]));
	}
	
	public List<Map<String, Object>> gridData(String grid) {
	    GridDataService service = AppContextUtility.getContext().getBean(GridDataService.class);
        return service.gridData(grid);
	}
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	<script type="text/javascript" src="<%=basePath%>plugin/date/lhgcalendar.min.js" language="JavaScript"></script>
  </head>
  
  <body>
  <div style="height: 100%">
  <form action="<%=basePath%>customConfig" method="post" enctype="multipart/form-data">
  	<input type="hidden" name="custom_config_code" value="<%=def.get("code") %>">
    <table id="customers">
    	<tr>
    		<th><%=def.get("name") %></th>
    		<td><button>保存配置</button></td>
    	</tr>
    	<%=html%>
    </table>
  </form>
  </div>
  </body>
</html>
