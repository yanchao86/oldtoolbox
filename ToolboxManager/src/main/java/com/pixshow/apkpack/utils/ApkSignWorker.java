package com.pixshow.apkpack.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.pixshow.apkpack.bean.ApkKeyBean;
import com.pixshow.apkpack.bean.ApkProductBean;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.framework.utils.UUIDUtility;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

public class ApkSignWorker extends Thread {

    public static interface HandlerCallback {
        public void callback(String msg, JobInfo job);
    };

    public static class JobInfo {
        public List<String>    channels = new ArrayList<String>();
        public int             id;
        public File            tempDir;
        public File            tempFile;
        public String          zipFolder;
        public String          zipFileName;
        public String          apkName;
        public String          suffix;
        public String          versioncode;
        public String          versionname;
        public ApkProductBean  product;
        public ApkKeyBean      apkKey;
        public HandlerCallback handlerCallback;
    }

    private static Queue<JobInfo> queue   = new LinkedList<JobInfo>();
    private static boolean        running = false;

    public synchronized static void addJob(JobInfo job) {
        queue.add(job);
        process();
    }

    private synchronized static void process() {
        if (!running) {
            running = true;
            ApkSignWorker worker = new ApkSignWorker();
            worker.start();
        }
    }

    public void run() {
        JobInfo job = null;
        while ((job = queue.poll()) != null) {
            try {
                sign(job);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        running = false;
    }

    public boolean sign(JobInfo job) throws Exception {
        List<File> signFiles = new ArrayList<File>();

        String zipFolder = job.zipFolder;
        String zipFileName = job.zipFileName;
        // 临时目录
        File tempDir = job.tempDir;
        // Align临时目录
        File tempAlignDir = FileUtility.createTempDir(UUIDUtility.uuid32());
        // 临时文件
        File tempFile = job.tempFile;
        // 获取临时文件的路径
        String tempApkPath = tempDir.getAbsolutePath() + "/" + job.apkName;
        if (job.handlerCallback != null) {
            job.handlerCallback.callback("<font class='sign_undone'>解压APK</font>", job);
        }
        // 解压APK
        ApkUtils.decode(tempFile.getAbsolutePath(), tempApkPath);
        if (job.handlerCallback != null) {
            job.handlerCallback.callback("<font class='sign_undone'>解压APK【完成】</font>", job);
        }
        // 原始XML文件
        String androidManifestXml = FileUtils.readFileToString(new File(tempApkPath + "/" + "AndroidManifest.xml"), "UTF-8");
        // 修改versioncode
        if (StringUtility.isNotEmpty(job.versioncode)) {
            androidManifestXml = ApkManifestUtils.replaceVersionCode(androidManifestXml, job.versioncode);
        }
        // 修改versionname
        if (StringUtility.isNotEmpty(job.versionname)) {
            androidManifestXml = ApkManifestUtils.replaceVersionName(androidManifestXml, job.versionname);
        }
        if (StringUtility.isNotEmpty(job.product.getAppKey())) {
            androidManifestXml = ApkManifestUtils.replaceMetaData(androidManifestXml, "UMENG_APPKEY", job.product.getAppKey());
        }
        if (job.handlerCallback != null) {
            job.handlerCallback.callback("<font class='sign_undone'>下载证书</font>", job);
        }
        // 获取key
        JSONObject key = getKey(tempDir, job.apkKey);
        String publicKeyFile = key.optString("publicKeyFile");
        String privateKeyFile = key.optString("privateKeyFile");
        if (job.handlerCallback != null) {
            job.handlerCallback.callback("<font class='sign_undone'>下载证书【完成】</font>", job);
        }
        for (int i = 0; i < job.channels.size(); i++) {
            String channel = job.channels.get(i);
            if (job.handlerCallback != null) {
                job.handlerCallback.callback("<font class='sign_undone'>打包签名渠道" + (i + 1) + "/" + job.channels.size() + "(" + channel + ")</font>", job);
            }
            // 修改channel
            if (StringUtility.isNotEmpty(channel)) {
                androidManifestXml = ApkManifestUtils.replaceMetaData(androidManifestXml, "UMENG_CHANNEL", channel);
            }
            // 写入xml
            FileUtils.write(new File(tempApkPath + "/" + "AndroidManifest.xml"), androidManifestXml, "UTF-8");

            // 打包APK
            String newApk = tempApkPath + "_" + channel + "_new" + job.suffix;
            ApkUtils.build(tempApkPath, newApk);

            // 对APK进行签名
            String signApk = tempDir.getAbsolutePath() + "/" + job.product.getEngName() + "_" + job.versioncode + "_" + channel + job.suffix;
            if (StringUtility.isEmpty(job.versioncode)) {
                signApk = tempDir.getAbsolutePath() + "/" + job.product.getEngName() + "_" + ApkManifestUtils.getVersionCode(androidManifestXml) + "_" + channel + job.suffix;
            }
            ApkSignUtils.sign(publicKeyFile, privateKeyFile, newApk, signApk, false, null);

            // 将生成的临时文件上传到文件服务器
            File signApkFile = new File(signApk);
            File signAlignApkFile = new File(tempAlignDir, signApkFile.getName());

            ZipalignUtils.exe(signApkFile, signAlignApkFile);

            signFiles.add(signAlignApkFile);

            //            ImageStorageTootl.upload(job.apkName + job.suffix, job.folder, tempFile);
            //            ImageStorageTootl.upload(newApkFile.getName(), job.folder, newApkFile);
            //            ImageStorageTootl.upload(signApkFile.getName(), job.folder, signApkFile);
            if (job.handlerCallback != null) {
                job.handlerCallback.callback("<font class='sign_undone'>打包签名渠道" + (i + 1) + "/" + job.channels.size() + "(" + channel + ")【完成】</font>", job);
            }
        }
        if (job.handlerCallback != null) {
            job.handlerCallback.callback("<font class='sign_undone'>压缩上传所有APK</font>", job);
        }
        File zipTempFile = File.createTempFile("ztmp", "");
        FileUtility.zip(signFiles, zipTempFile);
        //压缩上传
        String zipFileUrl = ImageStorageTootl.upload(zipFileName, zipFolder, zipTempFile);
        System.out.println("zipFileUrl >>> " + zipFileUrl);
        if (job.handlerCallback != null) {
            job.handlerCallback.callback("<font class='sign_done'>压缩上传所有APK【完成】</font>", job);
        }
        // 删除临时文件
        FileUtility.deleteDirectory(tempDir);
        FileUtility.deleteDirectory(tempAlignDir);
//        System.out.println("tempDir >>> " + tempDir.getAbsolutePath());
//        System.out.println("tempAlignDir >>> " + tempAlignDir.getAbsolutePath());
        return true;
    }

    /**
     * 获取key
     * 
     * @param apkName
     * @param tempDir
     * @return
     * @throws IOException
     */
    private static JSONObject getKey(File tempDir, ApkKeyBean apkKey) throws IOException {
        JSONObject json = new JSONObject();

        String publicKeyUrl = apkKey.getPublicKeyFile();
        File tempPublicKey = File.createTempFile(UUIDUtility.uuid32(), publicKeyUrl.substring(publicKeyUrl.lastIndexOf(".")), tempDir);
        FileUtility.copyURLToFile(new URL(apkKey.getPublicKeyFile()), tempPublicKey);
        String publicKeyFile = tempPublicKey.getAbsolutePath();

        String privateKeyUrl = apkKey.getPrivateKeyFile();
        File tempPrivateKey = File.createTempFile(UUIDUtility.uuid32(), privateKeyUrl.substring(privateKeyUrl.lastIndexOf(".")), tempDir);
        FileUtility.copyURLToFile(new URL(apkKey.getPrivateKeyFile()), tempPrivateKey);
        String privateKeyFile = tempPrivateKey.getAbsolutePath();

        json.put("publicKeyFile", publicKeyFile);
        json.put("privateKeyFile", privateKeyFile);
        return json;
    }

    public static void main(String[] args) {
        ImageStorageTootl.upload("1234568", "/program/", new File("C:/Users/4399-lvtu-8/Desktop/ztmp7957598786159127151.zip"));
    }

}
