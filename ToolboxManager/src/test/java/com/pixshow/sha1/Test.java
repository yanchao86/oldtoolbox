package com.pixshow.sha1;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.pixshow.framework.utils.DigestUtility;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.HttpUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class Test {

    public static void main(String[] args) {
        File folder = new File("/home/hope/Desktop/apk");
        File[] apks = folder.listFiles();
        for (int i = 0; i < apks.length; i++) {
            File apk = apks[i];
            String elementId = apk.getName().replace(".apk", "");

            try {
                String sha1 = FileUtility.sha1(apk);
                byte[] bytes = FileUtils.readFileToByteArray(apk);
                String md5 = DigestUtility.md5Hex(bytes);
                String sql = "db.getCollection('lockscreen').update({'elementId': '" + elementId + "'},{'$set': {'md5' :'" + md5 + "','sha1':'" + sha1 + "'}});";
                System.out.println(sql);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //db.getCollection('lockscreen').find({})

        }
    }

    public static void main1(String[] args) {
        try {
            List<String> lines = FileUtility.readLines(new File("/home/hope/MyWork/workspace/idotools_workspace/oldtoolbox/ToolboxManager/src/test/java/com/pixshow/sha1/list"));

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String id = line.split("@")[0];
                String apkurl = line.split("@")[3];
                try {
                    File apk = HttpUtility.download(apkurl);
                    if (apk == null || !apk.exists()) {
                        System.out.println(i + " > " + id + " > " + apkurl);
                        continue;
                    }
                    String sha1 = FileUtility.sha1(apk);
                    String sql = "update  tb_apk_upload set sha1='" + sha1 + "' where id=" + id + ";";
                    System.out.println(sql);
                } catch (Exception e) {
                }

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
