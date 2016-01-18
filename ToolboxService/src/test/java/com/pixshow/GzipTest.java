package com.pixshow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import com.pixshow.framework.utils.CipherUtility;
import com.pixshow.framework.utils.FileUtility;

public class GzipTest {

    public static byte[] gzip(String foo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = null;

        try {
            gzos = new GZIPOutputStream(baos);
            gzos.write(foo.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzos != null)
                try {
                    gzos.close();
                } catch (IOException ignore) {
                }
        }

        return baos.toByteArray();
    }

    public static void main(String[] args) throws IOException {

        String str = FileUtility.readFileToString(new File("C:/Users/yanchao/Desktop/789456.txt"));
        byte[] gzip = gzip(str);
        String res = CipherUtility.AES.encrypt(new String(gzip, "UTF-8"), "dsjkfh824hnlkdfnmvo");
        byte[] result = gzip(res);

        System.out.println("原始大小：" + str.getBytes().length);
        System.out.println("压缩后大小：" + gzip.length);
        System.out.println("加密后大小：" + res.getBytes().length);
        System.out.println("加密压缩后大小：" + result.length);

        //        HttpClient httpclient = null;
        //        HttpResponse response;
        //        try {
        //            httpclient = new DefaultHttpClient();
        //            HttpPost httppost = new HttpPost("http://172.16.10.132:8080/ToolboxService/gzip");
        //            httppost.addHeader("Content-Encoding", "gzip");
        //
        //            InputStreamEntity httpentity = new InputStreamEntity(new ByteArrayInputStream(result), result.length);
        //            httpentity.setChunked(true);
        //            httppost.setEntity(httpentity);
        //
        ////            response = httpclient.execute(httppost);
        ////
        ////            System.out.println(response.toString());
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }

    }
}
