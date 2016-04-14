package com.pixshow.alicdn;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cdn.model.v20141111.DescribeCdnServiceRequest;
import com.aliyuncs.cdn.model.v20141111.RefreshObjectCachesRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.pixshow.toolboxmgr.tools.AliyunTool;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class Test {
    //    AccessKeyId               AccessKeySecret
    //    splY0eww6iiZNJmq    S75dPwDabs1jVTGvrZipnp4Fm1STxf
    private static IAcsClient client = null;

    public Test() {
        if (null == client) {
            try {
                init();
                System.out.println(111);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() throws ClientException {
        //      初始化IAcsClient regionId
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "splY0eww6iiZNJmq", "S75dPwDabs1jVTGvrZipnp4Fm1STxf");
        System.out.println(profile.getRegionId());
        //DefaultProfile.addEndpoint("cn-hangzhou-2","cn-hangzhou-2","Cdn","cdn.aliyuncs.com"); //添加自定义endpoint。
        client = new DefaultAcsClient(profile);
        //System.setProperty("http.proxyHost","127.0.0.1"); //此设置用于设置代理，可用fiddler拦截查看http请求，便于调试。
        //System.setProperty("http.proxyPort","8888");
    }

    public void refreshCDN() {
        RefreshObjectCachesRequest refresh = new RefreshObjectCachesRequest();
        refresh.setObjectType("File");
        refresh.setObjectPath("apk.idotools.com/fz_2.0.1_c4017_20160315171210.apk");
        refresh.setProtocol(ProtocolType.HTTPS); //指定访问协议
        refresh.setAcceptFormat(FormatType.JSON); //指定api返回格式
        refresh.setMethod(MethodType.POST); //指定请求方法
        try {
            HttpResponse httpResponse = client.doAction(refresh);

            System.out.println(httpResponse.getUrl());
            System.out.println(new String(httpResponse.getContent()));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private static String[] cdns = { //
            "apk.idotools.com/IfengNewsV446_V4.4.6_3580.apk",//
            "apk.idotools.com/IfengNewsV451_V4.5.1_2522_20151222095132_20160203033643.apk"
    };

    public static void main(String[] args) {
        for (String cdn : cdns) {
            AliyunTool.refreshCDN("File", cdn);
            System.out.println(cdn+" > SUCCESS");
        }
    }

    public void requestInitSample() {
        DescribeCdnServiceRequest describeCdnServiceRequest = new DescribeCdnServiceRequest();
        //describeCdnServiceRequest.setProtocol(ProtocolType.HTTPS); //指定访问协议
        //describeCdnServiceRequest.setAcceptFormat(FormatType.JSON); //指定api返回格式
        //describeCdnServiceRequest.setMethod(MethodType.POST); //指定请求方法
        //describeCdnServiceRequest.setRegionId("cn-hangzhou");//指定要访问的Region,仅对当前请求生效，不改变client的默认设置。
        try {
            HttpResponse httpResponse = client.doAction(describeCdnServiceRequest);

            System.out.println(httpResponse.getUrl());
            System.out.println(new String(httpResponse.getContent()));
            //todo something.
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
