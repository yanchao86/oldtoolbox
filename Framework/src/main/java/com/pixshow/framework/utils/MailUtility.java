package com.pixshow.framework.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtility {

    public static class Mail {
        public String to;
        public String title;
        public String content;
        public String user;
        public String password;
        public String server;
        public Map<String, File> attachment = new HashMap<String, File>();
    }

    public static void sendSingle(Mail mail) {
        String to = mail.to;
        String[] arr = to.split(",");
        for (String t : arr) {
            t = t.trim();
            if (!"".equals(t)) {
                mail.to = t;
                send(mail);
            }
        }
        mail.to = to;
    }

    public static boolean send(Mail mail) {
        try {
            // 建立邮件会话
            Properties props = new Properties(); // 用来在一个文件中存储键-值对的，其中键和值是用等号分隔的，
            // 存储发送邮件服务器的信息
            props.setProperty("email.smtp.host", mail.server);
            // 同时通过验证
            props.setProperty("mail.smtp.auth", "true");
            // 根据属性新建一个邮件会话
            Session session = Session.getInstance(props);
            session.setDebug(false); // 有他会打印一些调试信息。
            // 由邮件会话新建一个消息对象
            MimeMessage message = new MimeMessage(session);
            // 设置邮件
            message.setFrom(new InternetAddress(mail.user)); // 设置发件人的地址
            // 设置收件人,并设置其接收类型为TO
            message.setRecipients(Message.RecipientType.TO, getTo(mail.to));
            // 设置标题
            message.setSubject(mail.title, "UTF-8");
            
            // 设置信件内容
            if (mail.attachment == null || mail.attachment.isEmpty()) {
                message.setContent(mail.content, "text/html;charset=UTF-8"); // 发送HTML邮件
            } else {
                Multipart content = createAttachmentContent(mail);
                message.setContent(content);
            }
            
            // 设置发信时间
            message.setSentDate(new Date());
            // 存储邮件信息
            message.saveChanges();
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            // 以smtp方式登录邮箱,第一个参数是发送邮件用的邮件服务器SMTP地址,第二个参数为用户名,第三个参数为密码
            transport.connect(mail.server, mail.user, mail.password);
            // 发送邮件,其中第二个参数是所有已设好的收件人地址
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Multipart createAttachmentContent(Mail mail) throws MessagingException {
        Multipart multipart = new MimeMultipart("related");

        BodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(mail.content, "text/html;charset=UTF-8");
        multipart.addBodyPart(htmlPart);

        for (String key : mail.attachment.keySet()) {
            BodyPart imgPart = new MimeBodyPart();
            DataSource ds = new FileDataSource(mail.attachment.get(key));
            imgPart.setDataHandler(new DataHandler(ds));
            imgPart.setHeader("Content-ID", key);
            multipart.addBodyPart(imgPart);
        }

        return multipart;
    }

    private static InternetAddress[] getTo(String to) {
        List<InternetAddress> list = new ArrayList<InternetAddress>();
        String[] arr = to.split(",");
        for (String t : arr) {
            if (!"".equals(t.trim())) {
                try {
                    list.add(new InternetAddress(t));
                } catch (AddressException e) {
                    e.printStackTrace();
                }
            }
        }
        return list.toArray(new InternetAddress[list.size()]);
    }

}
