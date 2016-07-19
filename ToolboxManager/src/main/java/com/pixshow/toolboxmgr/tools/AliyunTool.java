package com.pixshow.toolboxmgr.tools;

import java.util.ArrayList;
import java.util.List;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cdn.model.v20141111.RefreshObjectCachesRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class AliyunTool {
    private static final AliyunTool instance = new AliyunTool();
    private final List<IAcsClient>  clients  = new ArrayList<IAcsClient>();

    private AliyunTool() {
        //      初始化IAcsClient regionId
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "splY0eww6iiZNJmq", "S75dPwDabs1jVTGvrZipnp4Fm1STxf");
        //DefaultProfile.addEndpoint("cn-hangzhou-2", "cn-hangzhou-2", "Cdn", "cdn.aliyuncs.com"); //添加自定义endpoint。
        clients.add(new DefaultAcsClient(profile));
        //System.setProperty("http.proxyHost", "127.0.0.1"); //此设置用于设置代理，可用fiddler拦截查看http请求，便于调试。
        //System.setProperty("http.proxyPort", "8888");
    }

    private static AliyunTool getInstance() {
        return instance;
    }

    private IAcsClient getClient() {
        return clients.get(0);
    }

    public static String refreshCDN(String type, String path) {
        RefreshObjectCachesRequest refresh = new RefreshObjectCachesRequest();
        refresh.setObjectType(type);
        refresh.setObjectPath(path);
        refresh.setProtocol(ProtocolType.HTTPS); //指定访问协议
        refresh.setAcceptFormat(FormatType.JSON); //指定api返回格式
        refresh.setMethod(MethodType.POST); //指定请求方法
        try {
            HttpResponse httpResponse = AliyunTool.getInstance().getClient().doAction(refresh);
//            System.out.println(httpResponse.getUrl());
            return new String(httpResponse.getContent());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

}
