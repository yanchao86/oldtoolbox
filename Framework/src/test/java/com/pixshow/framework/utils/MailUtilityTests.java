package com.pixshow.framework.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.pixshow.framework.utils.MailUtility.Mail;

public class MailUtilityTests {

    public static void test_html() {

    }

    public static void test_html_attachment() throws Exception {
        Mail mail = new Mail();
        mail.user = "apps@pixshow.net";
        mail.password = "lvtu1233211";
        mail.server = "smtp.exmail.qq.com";

        mail.title = "测试发送HTML带图片带附件";
        mail.content = FileUtils.readFileToString(getFile("MailUtilityTests_table.html"), "UTF-8");
        mail.attachment.put("up", getFile("MailUtilityTests_down.gif"));
        mail.attachment.put("down", getFile("MailUtilityTests_up.gif"));

        mail.to = "420666366@qq.com,27645806@qq.com";

        MailUtility.send(mail);
    }

    private static File getFile(String fileName) throws Exception {
        return new File(MailUtilityTests.class.getResource(fileName).toURI());
    }

    public static void main(String[] args) throws Exception {
        test_html_attachment();
    }
}
