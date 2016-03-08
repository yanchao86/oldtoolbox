<%@page import="com.pixshow.framework.utils.DateUtility"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
Map<String, Object> code = (Map<String, Object>) result.get("code"); 
List<Map<String, Object>> stats = (List<Map<String, Object>>) result.get("stats");
JSONArray series = new JSONArray();

Calendar sc = Calendar.getInstance();
if(stats.size() > 0) {
    Map<String, Integer> dayCountMap = new HashMap<String, Integer>(); 
    for(Map<String, Object> map : stats) {
        String day = map.get("day").toString();
        int count = (Integer)map.get("count");
        dayCountMap.put(day, count);
    }
    
    String start = stats.get(stats.size()-1).get("day").toString();
    String end = stats.get(0).get("day").toString();
    Date sd = DateUtility.parseDate(start, "yyyyMMdd");
    sc.setTime(sd);
    Date ed = DateUtility.parseDate(end, "yyyyMMdd");
	Date date = sd;
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    while(date.before(ed) || date.equals(ed)) {
        String d = DateUtility.format(date, "yyyyMMdd");
        if(dayCountMap.containsKey(d)) {
            series.add(dayCountMap.get(d));
        } else {
            series.add(0);
        }
        c.add(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime();
    }
    
    
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	<script src="<%=basePath%>js/highcharts/highcharts.js"></script>
	<script src="<%=basePath%>js/highcharts/exporting.js"></script>
	<script type="text/javascript">
	$(function () {
        $('#container').highcharts({
            chart: {
                zoomType: 'x',
                spacingRight: 20
            },
            title: {
                text: '<%=code.get("name")%>日统计'
            },
            xAxis: {
                type: 'datetime',
                maxZoom: 14 * 24 * 3600000, // fourteen days
                title: {
                    text: null
                },
	            labels: {
	                format: '{value:%Y-%m-%d}',
	                rotation: 45,
	                align: 'left'
	            }

            },
            yAxis: {
                title: {
                    text: '数量统计'
                },
                min:0
            },
            tooltip: {
                xDateFormat: "%Y-%m-%d",
                pointFormat: "数量: {point.y}"
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    lineWidth: 1,
                    marker: {
                        enabled: false
                    },
                    shadow: false,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },
    
            series: [{
            	type: 'area',
                name: 'USD to EUR',
                pointInterval: 24 * 3600 * 1000,
                pointStart: Date.UTC(<%=sc.get(Calendar.YEAR)%>, <%=sc.get(Calendar.MONTH)%>, <%=sc.get(Calendar.DAY_OF_MONTH)%>),
                data:<%=series%>
            }]
        });
    });
    	
	</script>	
</head>
<body>
	<a href="javascript:window.history.back()">返回</a>
	<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto;"></div>
</body>
</html>