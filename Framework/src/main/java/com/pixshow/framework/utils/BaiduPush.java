package com.pixshow.framework.utils;

/*
 *
 * File:BaiduPush.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 19, 2013 3:48:46 PM
 * 
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jun 19, 2013
 * 
 */

public class BaiduPush {
    private Log log = LogFactory.getLog(BaiduPush.class);
    
    public static class RestApi extends TreeMap<String, String> {

        private static final long  serialVersionUID          = 1L;

        public final static String _METHOD                   = "method";
        public final static String _APIKEY                   = "apikey";
        public final static String _TIMESTAMP                = "timestamp";
        public final static String _SIGN                     = "sign";
        public final static String _EXPIRES                  = "expires";
        public final static String _V                        = "v";

        public final static String _USER_ID                  = "user_id";
        public final static String _CHANNEL_ID               = "channel_id";

        public final static String _PUSH_TYPE                = "push_type";
        public final static String _DEVICE_TYPE              = "device_type";

        public final static String _MESSAGE_TYPE             = "message_type";
        public final static String _MESSAGES                 = "messages";
        public final static String _MESSAGE_KEYS             = "msg_keys";
        public final static String _MESSAGE_EXPIRES          = "message_expires";
        public final static String _MESSAGE_IDS              = "msg_ids";

        public final static String _NAME                     = "name";
        public final static String _START                    = "start";
        public final static String _LIMIT                    = "limit";
        public final static String _TAG                      = "tag";

        public final static String METHOD_QUERY_BIND_LIST    = "query_bindlist";
        public final static String METHOD_PUSH_MESSAGE       = "push_msg";

        public final static String METHOD_VERIFY_BIND        = "verify_bind";

        public final static String METHOD_SET_TAG            = "set_tag";
        public final static String METHOD_FETCH_TAG          = "fetch_tag";
        public final static String METHOD_DELETE_TAG         = "delete_tag";
        public final static String METHOD_QUERY_USER_TAG     = "query_user_tags";

        public final static String METHOD_FETCH_MESSAGE      = "fetch_msg";
        public final static String METHOD_FETCH_MSG_COUNT    = "fetch_msgcount";
        public final static String METHOD_DELETE_MESSAGE     = "delete_msg";

        public final static String METHOD_QUERY_DEVICE_TYPE  = "query_device_type";

        public final static String PUSH_TYPE_USER            = "1";
        public final static String PUSH_TYPE_TAG             = "2";
        public final static String PUSH_TYPE_ALL             = "3";

        public final static String DEVICE_TYPE_BROWSER       = "1";
        public final static String DEVICE_TYPE_PC            = "2";
        public final static String DEVICE_TYPE_ANDROID       = "3";
        public final static String DEVICE_TYPE_IOS           = "4";
        public final static String DEVICE_TYPE_WINDOWS_PHONE = "5";

        public final static String MESSAGE_TYPE_MESSAGE      = "0";                //穿透
        public final static String MESSAGE_TYPE_NOTIFY       = "1";

        public static String       mApiKey                   = "";

        public RestApi(String method) {
            put(_METHOD, method);
            put(_APIKEY, mApiKey);
        }

        @Override
        public String put(String key, String value) {
            if ((value == null) || value.isEmpty())
                return null;
            return super.put(key, value);
        }

    }

    public String              mHttpMethod;
    public String              mSecretKey;

    public final static String mUrl                 = "http://channel.api.duapp.com/rest/2.0/channel/";

    public final static String HTTP_METHOD_POST     = "POST";
    public final static String HTTP_METHOD_GET      = "GET";

    private final static int   HTTP_CONNECT_TIMEOUT = 5000;
    private final static int   HTTP_READ_TIMEOUT    = 5000;

    public BaiduPush(String http_mehtod, String secret_key, String api_key) {
        mHttpMethod = http_mehtod;
        mSecretKey = secret_key;
        RestApi.mApiKey = api_key;
    }

    //
    // string convert
    //

    private String urlencode(String str) throws UnsupportedEncodingException {
        String rc = URLEncoder.encode(str, "utf-8");
        return rc.replace("*", "%2A");
    }

    public String jsonencode(String str) {
        String rc = str.replace("\\", "\\\\");
        rc = rc.replace("\"", "\\\"");
        rc = rc.replace("\'", "\\\'");
        return rc;
    }

    //
    // POST REST API to Baidu Push Server
    //

    public String PostHttpRequest(RestApi data) {

        StringBuilder sb = new StringBuilder();

        String channel = data.remove(RestApi._CHANNEL_ID);
        if (channel == null)
            channel = "channel";

        try {
            data.put(RestApi._TIMESTAMP, Long.toString(System.currentTimeMillis() / 1000));
            data.remove(RestApi._SIGN);

            sb.append(mHttpMethod);
            sb.append(mUrl);
            sb.append(channel);
            for (Map.Entry<String, String> i : data.entrySet()) {
                sb.append(i.getKey());
                sb.append('=');
                sb.append(i.getValue());
            }
            sb.append(mSecretKey);

            //System.out.println( "PRE: " + sb.toString() );
            //System.out.println( "UEC: " + URLEncoder.encode(sb.toString(), "utf-8") );

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            //md.update( URLEncoder.encode(sb.toString(), "utf-8").getBytes() );
            md.update(urlencode(sb.toString()).getBytes());
            byte[] md5 = md.digest();

            sb.setLength(0);
            for (byte b : md5)
                sb.append(String.format("%02x", b & 0xff));
            data.put(RestApi._SIGN, sb.toString());

            //System.out.println( "MD5: " + sb.toString());

            sb.setLength(0);
            for (Map.Entry<String, String> i : data.entrySet()) {
                sb.append(i.getKey());
                sb.append('=');
                //sb.append(i.getValue());
                //sb.append(URLEncoder.encode(i.getValue(), "utf-8"));
                sb.append(urlencode(i.getValue()));
                sb.append('&');
            }
            sb.setLength(sb.length() - 1);

            //System.out.println( "PST: " + sb.toString() );
            //System.out.println( mUrl + "?" + sb.toString() );

        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder response = new StringBuilder();
        HttpRequest(mUrl + channel, sb.toString(), response);
        return response.toString();
    }

    //
    // HTTP form POST
    //

    private int HttpRequest(String url, String query, StringBuilder out) {
        URL urlobj;
        HttpURLConnection connection = null;

        try {
            urlobj = new URL(url);
            connection = (HttpURLConnection) urlobj.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + query.length());
            connection.setRequestProperty("charset", "utf-8");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
            connection.setReadTimeout(HTTP_READ_TIMEOUT);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(query.toString());
            wr.flush();
            wr.close();

            //Get Response  
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = rd.readLine()) != null) {
                out.append(line);
                out.append('\r');
            }
            rd.close();

        } catch (Exception e) {
            log.warn("百度推送异常:"+e.getMessage());
        }

        if (connection != null)
            connection.disconnect();

        return 0;
    }

    //
    // REST APIs
    //

    public String QueryBindlist(String userid, String channelid) {
        RestApi ra = new RestApi(RestApi.METHOD_QUERY_BIND_LIST);
        ra.put(RestApi._USER_ID, userid);
        //ra.put(RestApi._DEVICE_TYPE, RestApi.DEVICE_TYPE_ANDROID);
        ra.put(RestApi._CHANNEL_ID, channelid);
        //ra.put(RestApi._START, "0");
        //ra.put(RestApi._LIMIT, "10");
        return PostHttpRequest(ra);
    }

    public String VerifyBind(String userid, String channelid) {
        RestApi ra = new RestApi(RestApi.METHOD_VERIFY_BIND);
        ra.put(RestApi._USER_ID, userid);
        //ra.put(RestApi._DEVICE_TYPE, RestApi.DEVICE_TYPE_ANDROID);
        ra.put(RestApi._CHANNEL_ID, channelid);
        return PostHttpRequest(ra);
    }

    public String SetTag(String tag, String userid) {
        RestApi ra = new RestApi(RestApi.METHOD_SET_TAG);
        ra.put(RestApi._USER_ID, userid);
        ra.put(RestApi._TAG, tag);
        return PostHttpRequest(ra);
    }

    public String FetchTag(String tag) {
        RestApi ra = new RestApi(RestApi.METHOD_FETCH_TAG);
        //        ra.put(RestApi._NAME, tag);
        //        ra.put(RestApi._START, "0");
        //        ra.put(RestApi._LIMIT, "10");
        return PostHttpRequest(ra);
    }

    public String DeleteTag(String tag, String userid) {
        RestApi ra = new RestApi(RestApi.METHOD_DELETE_TAG);
        //ra.put(RestApi._USER_ID, userid);
        ra.put(RestApi._TAG, tag);
        return PostHttpRequest(ra);
    }

    public String QueryUserTag(String userid) {
        RestApi ra = new RestApi(RestApi.METHOD_QUERY_USER_TAG);
        ra.put(RestApi._USER_ID, userid);
        return PostHttpRequest(ra);
    }

    public String QueryDeviceType(String channelid) {
        RestApi ra = new RestApi(RestApi.METHOD_QUERY_DEVICE_TYPE);
        ra.put(RestApi._CHANNEL_ID, channelid);
        return PostHttpRequest(ra);
    }

    // Message Push 

    private final static String MSGKEY = "msgkey";

    public String PushMessage(String title, String message, String userid) {
        return PushMessage(title, message, null, userid);
    }

    public String PushMessage(String title, String message, Map<String, String> custom_content, String userid) {
        JSONObject msg = new JSONObject();
        msg.put("title", title);
        msg.put("description", message);
        msg.put("notification_builder_id", 0);
        msg.put("notification_basic_style", 1);
        /**
         * open_type
         * 点击通知后的行为
            1: 表示打开Url
            2: 表示打开应用
            i.如果pkg_content有定义，则按照自定义行为打开应用
            ii.如果pkg_content无定义，则启动app的launcher activity
         */
        msg.put("open_type", 2);
        if (custom_content != null) {
            msg.put("custom_content", custom_content);
        }

        RestApi ra = new RestApi(RestApi.METHOD_PUSH_MESSAGE);
        ra.put(RestApi._MESSAGE_TYPE, RestApi.MESSAGE_TYPE_MESSAGE);
        ra.put(RestApi._MESSAGES, msg.toString());
        ra.put(RestApi._MESSAGE_KEYS, MSGKEY);
        //ra.put(RestApi._MESSAGE_EXPIRES, "86400");
        //ra.put(RestApi._CHANNEL_ID, "");
        ra.put(RestApi._PUSH_TYPE, RestApi.PUSH_TYPE_USER);
        //ra.put(RestApi._DEVICE_TYPE, RestApi.DEVICE_TYPE_ANDROID);
        ra.put(RestApi._USER_ID, userid);
        return PostHttpRequest(ra);
    }

    public String PushTagMessage(String title, String message, Map<String, String> custom_content, String tag) {
        JSONObject msg = new JSONObject();
        msg.put("title", title);
        msg.put("description", message);
        msg.put("notification_builder_id", 0);
        msg.put("notification_basic_style", 1);
        msg.put("open_type", 2);
        if (custom_content != null) {
            msg.put("custom_content", custom_content);
        }

        RestApi ra = new RestApi(RestApi.METHOD_PUSH_MESSAGE);
        ra.put(RestApi._MESSAGE_TYPE, RestApi.MESSAGE_TYPE_MESSAGE);
        ra.put(RestApi._MESSAGES, msg.toString());
        ra.put(RestApi._MESSAGE_KEYS, MSGKEY);
        //ra.put(RestApi._MESSAGE_EXPIRES, "86400");
        ra.put(RestApi._PUSH_TYPE, RestApi.PUSH_TYPE_TAG);
        //ra.put(RestApi._DEVICE_TYPE, RestApi.DEVICE_TYPE_ANDROID);
        ra.put(RestApi._TAG, tag);
        return PostHttpRequest(ra);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    public String PushNotify(String title, String message, String userid, Map<String, String> custom_content) {
        JSONObject msg = new JSONObject();
        msg.put("title", title);
        msg.put("description", message);
        msg.put("notification_builder_id", 0);
        msg.put("notification_basic_style", 1);
        msg.put("open_type", 2);
        if (custom_content != null) {
            msg.put("custom_content", custom_content);
        }
        RestApi ra = new RestApi(RestApi.METHOD_PUSH_MESSAGE);
        ra.put(RestApi._MESSAGE_TYPE, RestApi.MESSAGE_TYPE_NOTIFY);
        ra.put(RestApi._MESSAGES, msg.toString());
        ra.put(RestApi._MESSAGE_KEYS, MSGKEY);
        ra.put(RestApi._PUSH_TYPE, RestApi.PUSH_TYPE_USER);
        ra.put(RestApi._USER_ID, userid);
        return PostHttpRequest(ra);

    }

    public String PushNotify(String title, String message, String userid) {
        return PushNotify(title, message, userid, null);
    }

    public String PushTagNotify(String title, String message, Map<String, String> custom_content, String tag) {
        JSONObject msg = new JSONObject();
        msg.put("title", title);
        msg.put("description", message);
        msg.put("notification_builder_id", 0);
        msg.put("notification_basic_style", 1);
        msg.put("open_type", 2);
        if (custom_content != null) {
            msg.put("custom_content", custom_content);
        }
        RestApi ra = new RestApi(RestApi.METHOD_PUSH_MESSAGE);
        ra.put(RestApi._MESSAGE_TYPE, RestApi.MESSAGE_TYPE_NOTIFY);
        ra.put(RestApi._MESSAGES, msg.toString());
        ra.put(RestApi._MESSAGE_KEYS, MSGKEY);
        //ra.put(RestApi._MESSAGE_EXPIRES, "86400");
        ra.put(RestApi._PUSH_TYPE, RestApi.PUSH_TYPE_TAG);
        //ra.put(RestApi._DEVICE_TYPE, RestApi.DEVICE_TYPE_ANDROID);
        ra.put(RestApi._TAG, tag);
        return PostHttpRequest(ra);
    }

    //  

    public String FetchMessage(String userid) {
        RestApi ra = new RestApi(RestApi.METHOD_FETCH_MESSAGE);
        ra.put(RestApi._USER_ID, userid);
        //ra.put(RestApi._START, "0");
        //ra.put(RestApi._LIMIT, "10");
        return PostHttpRequest(ra);
    }

    public String FetchMessageCount(String userid) {
        RestApi ra = new RestApi(RestApi.METHOD_FETCH_MSG_COUNT);
        ra.put(RestApi._USER_ID, userid);
        return PostHttpRequest(ra);
    }

    public String DeleteMessage(String userid, String msgids) {
        RestApi ra = new RestApi(RestApi.METHOD_DELETE_MESSAGE);
        ra.put(RestApi._USER_ID, userid);
        ra.put(RestApi._MESSAGE_IDS, msgids);
        return PostHttpRequest(ra);
    }

}
