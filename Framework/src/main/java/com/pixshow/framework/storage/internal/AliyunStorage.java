package com.pixshow.framework.storage.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.pixshow.framework.config.Config;
import com.pixshow.framework.storage.api.Storage;
import com.pixshow.framework.thread.api.ThreadManager;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.StringUtility;

/**
 * 
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 22, 2013
 *
 */
public class AliyunStorage implements Storage {

    private Log                        log       = LogFactory.getLog(getClass());

    private OSSClient                  client    = null;
    private String                     endpoint  = "http://oss.aliyuncs.com/";
    private String                     address   = "oss.aliyuncs.com";
    private String                     accessKey = null;
    private String                     accessId  = null;

    private File                       uploadDir = null;

    private Timer                      timer     = null;
    //

    private Hashtable<String, Boolean> uploading = new Hashtable<String, Boolean>();

    public AliyunStorage() {

        accessId = Config.getInstance().getString("aliyun.oss.accessId");
        accessKey = Config.getInstance().getString("aliyun.oss.accessKey");

        if (!StringUtility.isNotEmpty(accessId, accessKey)) {
            log.warn("存储管理，配置文件缺少，初始化失败，不能使用。");
            return;
        }

        ClientConfiguration config = new ClientConfiguration();// 可以使用ClientConfiguration对象设置代理服务器、最大重试次数等参数。
        client = new OSSClient(endpoint, accessId, accessKey, config);

        String uploadDirPath = Config.getInstance().getString("aliyun.oss.uploadDir");
        // 异步上传
        if (StringUtility.isNotEmpty(uploadDirPath)) {
            this.uploadDir = new File(uploadDirPath);
            if (!this.uploadDir.exists()) {
                log.warn("历史文件目录不存在，创建文件夹。");
                this.uploadDir.mkdirs();
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    AliyunStorage.this.checkFile();
                }
            }, 0, 1000 * 60 * 5);
        } else {
            log.warn("异步临时文件夹，未进行配置，使用同步提交。");
        }
    }

    public void destroy() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public String upload(String storageSpace, String fileName, File file, String fileType) {
        try {

            if (client == null) {
                log.warn("阿里云客户端未初始化，功能不可用。");
                return null;
            }

            //取出 开头的 /
            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1);
            }

            if (log.isInfoEnabled()) {
                log.info("正在上传 文件大小[" + file.length() + "]  bucketName=" + storageSpace + " key=" + fileName);
            }

            long time1 = System.currentTimeMillis();

            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(file.length());
            // 可以在metadata中标记文件类型
            objectMeta.setContentType(fileType);
            objectMeta.setCacheControl("max-age=31536000");
            InputStream input = new FileInputStream(file);
            client.putObject(storageSpace, fileName, input, objectMeta);
            IOUtils.closeQuietly(input);

            long time2 = System.currentTimeMillis();

            if (log.isInfoEnabled()) {
                log.info("上传完成 耗时[" + (time2 - time1) + "] 文件大小[" + file.length() + "]  bucketName=" + storageSpace + " key=" + fileName);
            }

            String resourceAddress = "http://" + storageSpace + "." + address + "/" + fileName;
            return resourceAddress;
        } catch (Exception e) {
            log.error(e, e);
            return null;
        }
    }

    public String uploadAsync(String storageSpace, String fileName, File file, String fileType) {
        try {

            if (client == null) {
                log.warn("阿里云客户端未初始化，功能不可用。");
                return null;
            }
            if (uploadDir == null) {
                log.warn("异步临时文件夹，未进行配置，使用同步提交。");
                return upload(storageSpace, fileName, file, fileType);
            }

            //取出 开头的 /
            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1);
            }

            final String tempFileName = UUID.randomUUID().toString();

            PropertiesConfiguration config = new PropertiesConfiguration();
            config.setProperty("bucketName", storageSpace);
            config.setProperty("key", fileName);
            config.setProperty("fileType", fileType);
            config.save(getFile(tempFileName + ".config"));

            File uploadFile = getFile(tempFileName + ".upload");
            FileUtility.copyFile(file, uploadFile);

            ThreadManager.getInstance().execute(new Runnable() {
                public void run() {
                    AliyunStorage.this.uploadFromStack(tempFileName);
                }
            });

            String resourceAddress = "http://" + storageSpace + "." + address + "/" + fileName;
            return resourceAddress;
        } catch (Exception e) {
            log.error(e, e);
            return null;
        }
    }

    private void checkFile() {
        File[] files = this.uploadDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.endsWith(".upload")) {
                    return true;
                }
                return false;
            }
        });
        if (files != null)
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                String fileName = name.substring(0, name.length() - ".upload".length());
                uploadFromStack(fileName);
            }
    }

    private void uploadFromStack(String fileName) {
        if (uploading.get(fileName) == null && uploading.put(fileName, true) == null) {
            try {
                File uploadFile = getFile(fileName + ".upload");
                File configFile = getFile(fileName + ".config");

                if (!configFile.exists() || !configFile.exists()) {
                    return;
                }

                PropertiesConfiguration config = new PropertiesConfiguration();
                config.load(configFile);

                String bucketName = config.getString("bucketName");
                String key = config.getString("key");
                String fileType = config.getString("fileType");

                if (log.isInfoEnabled()) {
                    log.info("正在上传  临时文件" + fileName);
                }

                upload(bucketName, key, uploadFile, fileType);

                FileUtility.deleteQuietly(uploadFile);
                FileUtility.deleteQuietly(configFile);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                uploading.remove(fileName);
            }
        }
    }

    private File getFile(String name) {
        return new File(this.uploadDir.getAbsolutePath() + "/" + name);
    }

}
