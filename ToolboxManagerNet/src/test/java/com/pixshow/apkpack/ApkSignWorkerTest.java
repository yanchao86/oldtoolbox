package com.pixshow.apkpack;

import java.io.File;

import com.pixshow.apkpack.bean.ApkKeyBean;
import com.pixshow.apkpack.bean.ApkProductBean;
import com.pixshow.apkpack.utils.ApkSignWorker;
import com.pixshow.apkpack.utils.ApkSignWorker.JobInfo;
import com.pixshow.framework.utils.UUIDUtility;

public class ApkSignWorkerTest {

    public static void main(String[] args) {
        try {
            ApkProductBean product = new ApkProductBean();
            product.setAppKey("52d792b156240b63fd00bc4d");
            product.setEngName("FlashLight");
            ApkKeyBean apkKey = new ApkKeyBean();
            apkKey.setPublicKeyFile("http://oss.tv163.com//apk/key/7f45ffc6331d401f8768dbbafff24b03.pem");
            apkKey.setPrivateKeyFile("http://oss.tv163.com//apk/key/11dc6ba0764b4d219d88a6bf019f6dcd.pk8");

            ApkSignWorker.JobInfo job = new ApkSignWorker.JobInfo();
            String apkName = UUIDUtility.uuid32();
            File tempDir = new File("C:/ApkPack");
            File tempFile = new File("C:/ApkPack/FlashLight_1.6.1.apk");

            job.zipFolder = "apk";
            job.zipFileName = UUIDUtility.uuid32() + ".zip";
            job.tempDir = tempDir;
            job.tempFile = tempFile;
            job.apkName = apkName;
            job.product = product;
            job.apkKey = apkKey;
            job.suffix = ".apk";
            job.channels.add("360");
            job.channels.add("baidu");
            job.channels.add("google");
            job.handlerCallback = new ApkSignWorker.HandlerCallback() {
                @Override
                public void callback(String msg, JobInfo job) {
                    System.out.println(msg);
                }
            };
            ApkSignWorker.addJob(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
