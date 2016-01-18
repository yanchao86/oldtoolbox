package com.pixshow.framework.support.test;

import org.apache.struts2.StrutsJUnit4TestCase;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.ActionProxy;

@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/config/framework/spring.xml" })
public abstract class BaseTestCase extends StrutsJUnit4TestCase implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    public Object getAction(String uri) {
        ActionProxy proxy = getActionProxy(uri);
        return proxy.getAction();
    }

    protected void initServletMockObjects() {
        
        servletContext = new MyMockServletContext(resourceLoader);
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        pageContext = new MockPageContext(servletContext, request, response);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void setupBeforeInitDispatcher() throws Exception {
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
    }
}
