package com.pixshow.toolboxmgr.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.HttpUtility;
import com.pixshow.framework.utils.PushUtility;
import com.pixshow.framework.utils.UUIDUtility;

public class LargeDataPush {

    public static void pushData(List<String> url, String apiKey, String secretKey, String tag) {
        String data = getData(url);
        int length = 3000;
        int pushCount = data.length() % length > 0 ? data.length() / length + 1 : data.length() / length;
        List<String> datas = new ArrayList<String>();
        for (int i = 0; i < pushCount; i++) {
            if (i == pushCount - 1) {
                datas.add(data.substring(i * length, data.length()));
            } else {
                datas.add(data.substring(i * length, (i + 1) * length));
            }
        }

        String uuid = UUIDUtility.uuid22();
        int pushDate = DateUtility.currentUnixTime();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> custom_content = new HashMap<String, String>();
            custom_content.put("uuid", uuid);
            custom_content.put("index", (i + 1) + "");
            custom_content.put("total", datas.size() + "");
            custom_content.put("content", datas.get(i));
            custom_content.put("pushDate", pushDate + "");
            custom_content.put("type", "1");

            PushUtility.pushByTag(apiKey, secretKey, tag, PushUtility.setMsg("优品", "优品小应用发布新版本啦！", custom_content));
        }
    }

    private static String getData(List<String> urls) {
        JSONObject json = new JSONObject();
        for (String url : urls) {
            String data = HttpUtility.get(url);
            json.put("http://toolbox.tv163.com/service/appConfigArray.do", data);
        }
        return json.toString();
    }

}
