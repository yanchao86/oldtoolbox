/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:MultiActionExcuteServlet.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Nov 21, 2013 6:26:44 PM
 * 
 */
package com.pixshow.framework.ext.struts;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.pixshow.framework.utils.StringUtility;

import net.sf.json.JSONObject;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Nov 21, 2013
 * 
 */

public class MultiStrutsExecuteFilter extends StrutsPrepareAndExecuteFilter {

    private static class P {
        private String              uri;
        private String              queryString;
        private Map<String, Object> parameterMap = new HashMap<String, Object>();
    }

    private String multiActionName = "doMulti.do";
    private String encoding        = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (StringUtility.isNotEmpty(filterConfig.getInitParameter("multiActionName"))) {
            multiActionName = filterConfig.getInitParameter("multiActionName");
        }
        if (StringUtility.isNotEmpty(filterConfig.getInitParameter("encoding"))) {
            encoding = filterConfig.getInitParameter("encoding");
        }
        super.init(filterConfig);
    }

    private Pattern pattern = Pattern.compile("act\\.(\\w+)(\\.(\\w+)){0,1}");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType("application/json");
        response.setCharacterEncoding(encoding);
        String servletPath = request.getServletPath();
        if (servletPath.equals("/" + multiActionName)) {
            Enumeration parameterNames = request.getParameterNames();
            Map<String, P> pMap = splitUri(request, parameterNames);

            JSONObject jsonMultiResponse = new JSONObject();
            Iterator<String> it = pMap.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                P p = pMap.get(key);
                MockHttpServletRequest mockRequest = new MockHttpServletRequest();
                mockRequest.setParameters(p.parameterMap);
                mockRequest.setRequestURI(p.uri);
                mockRequest.setServletPath(p.uri.substring(request.getContextPath().length()));
                //mockRequest.setQueryString(request.getQueryString());
                setMockRequest(mockRequest, request);

                MockHttpServletResponse mockResponse = new MockHttpServletResponse();
                MockFilterChain mockChain = new MockFilterChain();
                super.doFilter(mockRequest, mockResponse, mockChain);

                JSONObject act = new JSONObject();
                act.put("status", mockResponse.getStatus());
                act.put("response", mockResponse.getContentAsString());
                jsonMultiResponse.put("act." + key, act);
            }
            response.getWriter().print(jsonMultiResponse.toString());
        } else {
            super.doFilter(req, res, chain);
        }
    }

    public Map<String, P> splitUri(HttpServletRequest request, Enumeration parameterNames) {
        Map<String, P> pMap = new HashMap<String, P>();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement().toString();
            String value = request.getParameter(name);
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                String uriKey = matcher.group(1);
                String parameterKey = matcher.group(3);
                P p = pMap.get(uriKey);
                if (p == null) {
                    p = new P();
                    pMap.put(uriKey, p);
                }
                if (parameterKey == null) {
                    if (value.indexOf("?") > 0) {
                        String[] url = value.split("[\\?]");
                        p.uri = url[0];
                        if (url.length > 0) {
                            p.queryString = url[1];
                            String[] ps = p.queryString.split("&");
                            for (int i = 0; i < ps.length; i++) {
                                String[] kv = ps[i].split("=");
                                if (kv.length > 0) {
                                    p.parameterMap.put(ps[i].split("=")[0], ps[i].split("=")[1]);
                                }
                            }
                        }
                    } else {
                        p.uri = value;
                    }
                } else {
                    p.parameterMap.put(parameterKey, value);
                }
            }
        }
        return pMap;
    }

    public void setMockRequest(MockHttpServletRequest mockRequest, HttpServletRequest request) {
        mockRequest.setAuthType(request.getAuthType());
        mockRequest.setCharacterEncoding(request.getCharacterEncoding());
        mockRequest.setContextPath(request.getContextPath());
        mockRequest.setCookies(request.getCookies());
        mockRequest.setLocalAddr(request.getLocalAddr());
        mockRequest.setLocalName(request.getLocalName());
        mockRequest.setLocalPort(request.getLocalPort());
        mockRequest.setMethod(request.getMethod());
        mockRequest.setPathInfo(request.getPathInfo());
        mockRequest.setProtocol(request.getProtocol());
        mockRequest.setRemoteAddr(request.getRemoteAddr());
        mockRequest.setRemoteHost(request.getRemoteHost());
        mockRequest.setRemoteUser(request.getRemoteUser());
        mockRequest.setRequestedSessionId(request.getRequestedSessionId());
        mockRequest.setRequestedSessionIdFromCookie(request.isRequestedSessionIdFromCookie());
        mockRequest.setRequestedSessionIdValid(request.isRequestedSessionIdValid());
        mockRequest.setScheme(request.getScheme());
        mockRequest.setSecure(request.isSecure());
        mockRequest.setServerName(request.getServerName());
        mockRequest.setServerPort(request.getServerPort());
        mockRequest.setSession(request.getSession());
        mockRequest.setUserPrincipal(request.getUserPrincipal());
        //mockRequest.addPreferredLocale()
        //mockRequest.addUserRole(role)
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement().toString();
            mockRequest.addHeader(name, request.getHeader(name));
        }
    }

}
