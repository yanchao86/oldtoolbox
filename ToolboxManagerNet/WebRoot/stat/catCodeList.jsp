<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.pixshow.framework.utils.DateUtility"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
Map<String, Object> cat = new HashMap<String, Object>(); 
List<Map<String, Object>> codes = new ArrayList<Map<String, Object>>(); 
Map<String, Integer> weekMap  = new HashMap<String, Integer>();
Map<String, Integer> cuntMap  = new HashMap<String, Integer>();
JSONArray codeNames = new JSONArray();
if(result != null) {
    codes = (List<Map<String, Object>>) result.get("codes");
    cat = (Map<String, Object>) result.get("cat");
    weekMap = (Map<String, Integer>) result.get("weekMap");
    cuntMap = (Map<String, Integer>) result.get("cuntMap");
    codeNames = (JSONArray) result.get("codeNames");
}
////////////////////////////////////////////
JSONArray jDays = new JSONArray();
for(int i=0;i<7;i++) {
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, 0-i);
	String day = DateUtility.format(c, "yyyy-MM-dd");
	jDays.add(day);
}

JSONArray series = new JSONArray();
for(Map<String, Object> code : codes) {
    JSONObject serie = new JSONObject();
    Object total = cuntMap.get(code.get("code"))!=null?cuntMap.get(code.get("code")):"-";
    serie.put("name", code.get("name")+"("+total+")");
    JSONArray data = new JSONArray();
    
    for(int j=0;j<7;j++) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 0-j);
		String day = DateUtility.format(c, "yyyyMMdd");
		Integer count = weekMap.get(day+"_"+code.get("code"));
		data.add(count==null?0:count);
    }
    serie.put("data", data);
    series.add(serie);
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<link rel="stylesheet" href="<%=basePath%>js/jquery-ui-1.10.4.custom.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery-ui.js" language="JavaScript"></script>
	<script src="<%=basePath%>js/highcharts/highcharts.js"></script>
	<script src="<%=basePath%>js/highcharts/exporting.js"></script>
	<script type="text/javascript">
		function del(codeId, catId) {
			if(confirm("确定删除该分类么？")) {
				window.location.href="<%=basePath%>manager/deleteStatCatCode.do?codeId="+codeId+"&catId="+catId;
			}
		}
		 $(function() {
			 //select 框
			 var availableTags = <%=codeNames%>;
			 $("#catCode").autocomplete({
			 	source: availableTags
			 });
			
			 //报表
			 $('#container').highcharts({
	            chart: {
	                type: 'column'
	            },
	            title: {
	                text: '<%=cat.get("name")%>',
	                x: -20 //center
	            },
	            subtitle: {
	                text: '',
	                x: -20
	            },
	            xAxis: {
	                categories: <%=jDays%>
	            },
	            yAxis: {
	                title: {
	                    text: '数量统计'
	                },
	                plotLines: [{
	                    value: 0,
	                    width: 1,
	                    color: '#808080'
	                }]
	            },
	            tooltip: {
	                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                    '<td style="padding:0"><b>{point.y}</b></td></tr>',
	                footerFormat: '</table>',
	                shared: true,
	                useHTML: true
	            },
	            plotOptions: {
	                column: {
	                    pointPadding: 0.2,
	                    borderWidth: 0
	                }
	            },
	            series: <%=series%>
	        });
	    });
		function codeChange(id) {
			$("#code_change_"+id).hide();
			$("#code_save_"+id).show();
		}	 
		function codeSave(id) {
			var codeName=$("#code_input_"+id).val();
			$.post("<%=basePath%>manager/updateStatCatCode.do?=", {"codeId":id, "catCodeName":codeName}, function(data) {
				//$("#code_change_"+id).show();
				//$("#code_save_"+id).hide();
				window.location.reload(true);
			});
		}
		function selectAll(obj) {
				//alert(obj.checked);
				
			if(obj.checked) {
				$('#abcd').prop('checked', true);
				$('input[name=statCatCodeId]').each(function() {
					$(this).prop('checked', true);
				});
			} else{
				$('#abcd').prop('checked', false);
				$('input[name=statCatCodeId]').each(function() {
					$(this).prop('checked', false);
					//$(this).removeAttr('checked');
				});
			}
		}
		function delAll() {
			var ids = [];
			$('input[name=statCatCodeId]').each(function() {
				if(this.checked) {
					ids.push($(this).val());
				}
			});
			var catId = $('#catId').val();
			if(confirm('确定删除选中的么？')) {
				$.post("<%=basePath%>manager/deleteStatCatCodes.do", {"statCatCodeIds":ids.join(","), "catId":catId}, function(data) {
					window.location.reload();
				});
			}
		}
	</script>
</head>
<body>
<a href="javascript:window.history.back()">返回</a>
<form action="<%=basePath %>manager/addStatCatCode.do" method="post">
	<input type="hidden" name="catId" id="catId" value="<%=cat.get("id")%>">		
	<table id="customers">
		<tr>
			<th>分类名称</th>
			<th>code描述</th>
			<th>code(如果结尾是‘%’，表示模糊匹配，没有就是表示一条)</th>
			<th>添加</th>
		</tr>
		<tr>
			<td><%=cat.get("name")%></td>		
			<td><input type="text" name="catCodeName"></td>		
			<td>
				<div class="ui-widget">
					<input type="text" name="catCode" id="catCode" size="100" >
				</div>
			</td>		
			<td><button>添加</button></td>		
		</tr>
	</table>
</form>

	<table id="customers">
		<tr>
			<th>全选<input type="checkbox" onchange="selectAll(this)"></th>
			<th>名称</th>
	<%for(int i=0;i<jDays.size();i++) {
	    String day = jDays.getString(i);
	%>
	    	<th><%=day %></th>
	<%}%>
	    	<th>总量</th>
	    	<th>操作</th>
		</tr>
	<%for(int i=0;i<codes.size();i++) {
	    Map<String, Object> code = codes.get(i);
	%>
		<tr>
			<td>
				<%=(i+1) %> <input type="checkbox" name="statCatCodeId" value="<%=code.get("id")%>">
			</td>
			<td>
				<div id="code_change_<%=code.get("id")%>">
					<a href="<%=basePath%>manager/catCodeStats.do?codeId=<%=code.get("id")%>" title="<%=code.get("code") %>"><%=code.get("name") %></a>
					<a style="color: gray; font-style: italic; font-size: 12px;" href="javascript:codeChange(<%=code.get("id")%>)">修改</a>
					<p style="color: red; font-style: italic; font-size: 12px;"><%=code.get("code") %></p>
				</div>
				<div id="code_save_<%=code.get("id")%>" style="display: none;">
					<input type="text" value="<%=code.get("name") %>" id="code_input_<%=code.get("id")%>" size="100">
					<a style="color: gray; font-style: italic; font-size: 12px;" href="javascript:codeSave(<%=code.get("id")%>)">保存</a>
				</div>
			</td>
		<%for(int j=0;j<jDays.size();j++) {
			String day = jDays.getString(j).replaceAll("-", "");
			Integer count = weekMap.get(day+"_"+code.get("code"));
		%>
			<td><%=count!=null?count:0 %></td>
		<%}%>
			<td><%=cuntMap.get(code.get("code"))!=null?cuntMap.get(code.get("code")):"-" %></td>
			<td><a href="javascript:del(<%=code.get("id")%>, <%=cat.get("id")%>)">删除</a></td>
		</tr>
	<%} %>
		<tr>
			<td colspan="11">
				<button onclick="delAll()">删除选中</button>
			</td>
		</tr>
	</table>
	<br />
	<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto;"></div>
</body>
</html>