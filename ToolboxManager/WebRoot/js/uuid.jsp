<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    int total = NumberUtils.toInt(request.getParameter("total"), 500);
    StringBuilder uuidList = new StringBuilder();
    uuidList.append("[");
    for (int i = 0; i < total; i++) {
        if (i != 0) uuidList.append(",");
        uuidList.append("\"" + UUID.randomUUID().toString().replaceAll("-", "") + "\"");
    }
    uuidList.append("]");
%>

var uuid = function(){
	var uuidData = <%=uuidList.toString()%>;
	return function(){
		if(uuidData.length > 0){
			return uuidData.pop();
		}else{
			alert("UUID 已使用完!");
			return null;
		}
	}
}();
