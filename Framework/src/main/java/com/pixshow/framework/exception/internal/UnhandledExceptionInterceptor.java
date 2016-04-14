package com.pixshow.framework.exception.internal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.pixshow.framework.exception.api.DoNotCatchException;
import com.pixshow.framework.exception.api.LogicException;
import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.log.api.SysLog;
import com.pixshow.framework.log.api.SysLogFactory;

import net.sf.json.JSONObject;

public class UnhandledExceptionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -1692106466913967143L;
	private static SysLog log = SysLogFactory.getLog(UnhandledExceptionInterceptor.class);

	/**
	 * result:<br>
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = null;
		try {
			result = invocation.invoke();
		} catch (Throwable e) {
			Map<String, Object> info = new HashMap<String, Object>();
			info.put("url", ServletActionContext.getRequest().getRequestURL().toString());
			info.put("parameters", ServletActionContext.getRequest().getQueryString());
			if (e instanceof SysException) {
				SysException sysException = (SysException) e;
				if (sysException.getInfo() != null) {
					info.putAll(sysException.getInfo());
				}
				log.log("sys_error_" + sysException.getCode(), sysException.getMessage(), info, e);
			} else if (e instanceof LogicException) {
			} else {
				log.log("sys_error", e.getMessage(), info, e);
			}

			ServletActionContext.getRequest().setAttribute(ExceptionConstants.FRAMEWORK_EXCEPTION_KEY, e);
			ServletActionContext.getResponse().setStatus(HttpStatus.SC_OK);
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			ServletActionContext.getResponse().setContentType("application/json");

			JSONObject response = new JSONObject();
			HttpServletResponse httpServletResponse = ServletActionContext.getResponse();
			if (e instanceof LogicException) {
				LogicException logicException = (LogicException) e;
				response.put("result", logicException.getCode());
				response.put("errorIofo", logicException.getMessage());
				httpServletResponse.getWriter().append(response.toString());
			} else if (e instanceof DoNotCatchException) {
				httpServletResponse.setStatus(500);
			} else {
				response.put("result", 2);
				response.put("errorIofo", "服务器错误");
				httpServletResponse.getWriter().append(response.toString());
			} 
//			httpServletResponse.getWriter().append(response.toString());
		}
		return result;
	}
}
