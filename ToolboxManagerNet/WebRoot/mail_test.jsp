<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="java.util.Date"%>
<%@page import="com.pixshow.framework.utils.DateUtility"%>
<%@page import="java.net.URISyntaxException"%>
<%@page import="java.io.File"%>
<%@page import="com.pixshow.framework.utils.MailUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String mailTo = request.getParameter("to");
if(StringUtility.isEmpty(mailTo)) {
    out.print("请带上 to ~");
    return;
}

MailUtility.Mail mail = new MailUtility.Mail();
mail.user = "apps@pixshow.net";
mail.password = "lvtu1233211";
mail.server = "smtp.exmail.qq.com";
mail.attachment.put("down", new File(getClass().getResource("/img/down.gif").toURI()));
mail.attachment.put("up", new File(getClass().getResource("/img/up.gif").toURI()));

mail.to = mailTo;
mail.title = "↑↓" + DateUtility.format(new Date(), "yyyy-MM-dd") + "邮件测试";
mail.content = DateUtility.format(new Date(), "yyyy-MM-dd HH:mm:ss");
MailUtility.sendSingle(mail);



%>


