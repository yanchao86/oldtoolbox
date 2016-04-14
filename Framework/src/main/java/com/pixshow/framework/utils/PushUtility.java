package com.pixshow.framework.utils;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.channel.model.SetTagRequest;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

import net.sf.json.JSONObject;

public class PushUtility {
    private static Log log = LogFactory.getLog(PushUtility.class);

    public static void setTag(String apiKey, String secretKey, String userId, String tag) {
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                log.warn(event.getMessage());
            }
        });

        SetTagRequest request = new SetTagRequest();
        request.setTag(tag);
        request.setUserId(userId);
        try {
            channelClient.setTag(request);
        } catch (ChannelClientException e) {
            e.printStackTrace();
        } catch (ChannelServerException e) {
            log.error(String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }
    }

    public static String setMsg(String title, String message, Map<String, String> custom_content) {
        JSONObject msg = new JSONObject();
        msg.put("title", title);
        msg.put("description", message);
        msg.put("notification_builder_id", 0);
        msg.put("notification_basic_style", 1);
        /**
         * open_type 点击通知后的行为 1: 表示打开Url 2: 表示打开应用
         * i.如果pkg_content有定义，则按照自定义行为打开应用 ii.如果pkg_content无定义，则启动app的launcher
         * activity
         */
        msg.put("open_type", 2);
        if (custom_content != null) {
            msg.put("custom_content", custom_content);
        }
        return msg.toString();
    }

    public static int pushMessage(String apiKey, String secretKey, String message, String userId) {
        return push(apiKey, secretKey, 3, message, userId);
    }

    public static int pushIOSMessage(String apiKey, String secretKey, String message, String userId) {
        return push(apiKey, secretKey, 4, message, userId);
    }

    private static int push(String apiKey, String secretKey, int deviceType, String message, String userId) {
        /*
         * @brief 推送单播消息(消息类型为透传，由开发方应用自己来解析消息内容) message_type = 0 (默认为0)
         */
        // 1. 设置developer平台的ApiKey/SecretKey
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                log.warn(event.getMessage());
            }
        });

        int res = -999;
        try {
            // 4. 创建请求类对象
            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
            request.setDeviceType(deviceType); // device_type => 1: web 2: pc 3:android 4:ios 5:wp
            //            request.setChannelId(11111111111L);
            request.setUserId(userId);
            request.setMessage(message);
            // 5. 调用pushMessage接口
            PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
            // 6. 认证推送成功
            res = response.getSuccessAmount();
        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
            log.error(String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }
        return res;
    }

    public static int pushByTag(String apiKey, String secretKey, String tag, String message) {
        return byTag(apiKey, secretKey, 3, tag, message);
    }

    public static int pushIOSByTag(String apiKey, String secretKey, String tag, String message) {
        return byTag(apiKey, secretKey, 4, tag, message);
    }

    private static int byTag(String apiKey, String secretKey, int deviceType, String tag, String message) {
        /*
         * @brief 推送组播消息(消息类型为透传，由开发方应用自己来解析消息内容) message_type = 0 (默认为0)
         */
        // 1. 设置developer平台的ApiKey/SecretKey
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                log.warn(event.getMessage());
            }
        });
        int res = -999;
        try {
            // 4. 创建请求类对象
            PushTagMessageRequest request = new PushTagMessageRequest();
            request.setDeviceType(deviceType); // device_type => 1: web 2: pc 3:android 4:ios 5:wp
            request.setTagName(tag);
            request.setMessage(message);
            // 若要通知，
            // request.setMessageType(1);
            // request.setMessage("{\"title\":\"Notify_title_danbo\",\"description\":\"Notify_description_content\",\"custom_content\":{...}}");

            // 5. 调用pushMessage接口
            PushTagMessageResponse response = channelClient.pushTagMessage(request);
            // 6. 认证推送成功
            res = response.getSuccessAmount();
        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
            log.error(String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }
        return res;
    }

    public static void main(String[] args) {
        String apiKey = "jflXWjyPsSQ513mlAuHLKiSX";
        String secretKey = "MTwhbli9POAxaaPLSfx9YDI5mmTeNK2N";
        String userId = "588596910732119502";
//        PushUtility.setTag(apiKey, secretKey, userId, "yc_android");
        PushUtility.pushByTag(apiKey, secretKey, "yc_android", PushUtility.setMsg("test", "12345678", null));
    }
}
