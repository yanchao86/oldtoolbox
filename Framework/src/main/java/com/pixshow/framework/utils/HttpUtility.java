/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:HttpUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Feb 25, 2013 4:27:10 PM
 * 
 */
package com.pixshow.framework.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Feb 25, 2013
 * 
 */

public class HttpUtility {

    private static Log log = LogFactory.getLog(HttpUtility.class);

    private static class GZIPAwareGetMethod extends GetMethod {

        public GZIPAwareGetMethod(String uri) {
            super(uri);
        }

        @Override
        protected void readResponseBody(HttpState state, HttpConnection conn) throws IOException, HttpException {
            super.readResponseBody(state, conn);
            Header contentEncodingHeader = getResponseHeader("Content-Encoding");
            if (contentEncodingHeader != null && contentEncodingHeader.getValue().equalsIgnoreCase("gzip")) {
                setResponseStream(new GZIPInputStream(getResponseStream()));
            }
        }
    }

    public static String urlEncode(String str, String charset) {
        try {
            return URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String post(String url, Map<String, String> params) {
        return post(url, params, null, "UTF-8", 0);
    }

    public static String post(String url, Map<String, String> params, String charset) {
        return post(url, params, null, charset, 0);
    }

    public static String post(String url, Map<String, String> params, String charset, int timeout) {
        return post(url, params, null, charset, timeout);
    }

    public static String post(String url, Map<String, String> params, Map<String, String> headers, String charset, int timeout) {
        SimpleHttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
        HttpClient httpClient = new HttpClient(connectionManager);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(50000);

        PostMethod method = new PostMethod(url);
        method.getParams().setSoTimeout(timeout == 0 ? 300000 : timeout);
        method.getParams().setContentCharset(charset);
        if (params != null) {
            for (String key : params.keySet()) {
                method.addParameter(key, params.get(key));
            }
        }
        if (headers != null) {
            for (String headerName : headers.keySet()) {
                method.addRequestHeader(headerName, headers.get(headerName));
            }
        }
        try {
            int statusCode = httpClient.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) { return method.getResponseBodyAsString(); }
            if ((statusCode == HttpStatus.SC_MOVED_TEMPORARILY) || (statusCode == HttpStatus.SC_MOVED_PERMANENTLY) || (statusCode == HttpStatus.SC_SEE_OTHER) || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                Header header = method.getResponseHeader("location");
                if (header != null) { return post(header.getValue(), params, charset); }
            }
        } catch (Exception e) {
            log.error("请求地址错误:" + url, e);
        } finally {
            try {
                if (method != null) method.releaseConnection();
            } catch (Exception e) {
            }
            try {
                if (connectionManager != null) connectionManager.shutdown();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String get(String url) {
        return get(url, null, null, "UTF-8", 0);
    }

    public static String get(String url, Map<String, String> params) {
        return get(url, params, null, "UTF-8", 0);
    }

    public static String get(String url, Map<String, String> params, String charset) {
        return get(url, params, null, charset, 0);
    }

    public static String get(String url, Map<String, String> params, String charset, int timeout) {
        return get(url, params, null, charset, timeout);
    }

    public static String get(String url, Map<String, String> params, Map<String, String> headers, String charset, int timeout) {
        return get(url, params, headers, charset, timeout, null, 0);
    }

    public static String get(String url, Map<String, String> params, Map<String, String> headers, String charset, int timeout, String proxyHost, int proxyPort) {
        if (params != null && params.size() > 0) {
            StringBuilder paramsUrl = new StringBuilder();
            for (String key : params.keySet()) {
                if (paramsUrl.length() > 0) {
                    paramsUrl.append("&");
                }
                paramsUrl.append(key).append("=").append(params.get(key));
            }
            if (url.indexOf('?') == -1) {
                url += "?" + paramsUrl.toString();
            } else {
                url += "&" + paramsUrl.toString();
            }
        }

        GetMethod method = new GZIPAwareGetMethod(url);

        if (headers != null) {
            for (String headerName : headers.keySet()) {
                method.addRequestHeader(headerName, headers.get(headerName));
            }
        }

        method.getParams().setContentCharset(charset);
        method.getParams().setSoTimeout(timeout == 0 ? 30000 : timeout);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new HttpMethodRetryHandler() {
            @Override
            public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {
                return executionCount > 2 ? false : true;
            }
        });

        SimpleHttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
        HttpClient httpClient = new HttpClient(connectionManager);
        if (StringUtility.isNotEmpty(proxyHost)) {
            httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
            //
            // UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("pixshow", "2778899");
            // httpClient.getState().setProxyCredentials(AuthScope.ANY, credentials);
        }

        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        try {

            int statusCode = httpClient.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) { return method.getResponseBodyAsString(); }
        } catch (Exception e) {
            log.error("请求地址错误:" + url, e);
        } finally {
            try {
                if (method != null) method.releaseConnection();
            } catch (Exception e) {
            }
            try {
                if (connectionManager != null) connectionManager.shutdown();
            } catch (Exception e) {
            }
        }
        return null;

    }

    public static String upload(String url, Map<String, String> params, List<FilePart> files) {
        return upload(url, params, files, "UTF-8");
    }

    public static String upload(String url, Map<String, String> params, List<FilePart> files, String charset) {
        return upload(url, params, null, files, charset);
    }

    public static String upload(String url, Map<String, String> params, Map<String, String> headers, List<FilePart> files) {
        return upload(url, params, headers, files, "UTF-8");
    }

    public static String upload(String url, Map<String, String> params, Map<String, String> headers, List<FilePart> files, String charset) {
        SimpleHttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
        HttpClient httpClient = new HttpClient(connectionManager);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        PostMethod method = new PostMethod(url);
        method.getParams().setSoTimeout(30000);
        try {
            List<Part> parts = new ArrayList<Part>();
            if (params != null) {
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    parts.add(new StringPart(key, value, charset));
                }
            }
            if (files != null) {
                parts.addAll(files);
            }

            if (parts != null) {
                method.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), new HttpMethodParams()));
            }
            method.getParams().setContentCharset(charset);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    String value = headers.get(key);
                    method.addRequestHeader(key, value);
                }
            }
            int statusCode = httpClient.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) { return method.getResponseBodyAsString(); }
        } catch (Exception e) {
            log.error("请求地址错误:" + url, e);
        } finally {
            try {
                if (method != null) method.releaseConnection();
            } catch (Exception e) {
            }
            try {
                if (connectionManager != null) connectionManager.shutdown();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static File download(String url) {
        File file = null;
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(url);
        try {
            client.executeMethod(get);
            file = File.createTempFile("file", ".temp");
            FileOutputStream output = new FileOutputStream(file);
            output.write(get.getResponseBody());
            output.close();
        } catch (Exception e) {
            if (file != null) {
                file.delete();
            }
            file = null;
            log.error("请求地址错误:" + url, e);
        } finally {
            get.releaseConnection();
        }
        return file;
    }

}
