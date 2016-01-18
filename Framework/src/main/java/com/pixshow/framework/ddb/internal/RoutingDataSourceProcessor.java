/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:RoutingDataSourceProcessor.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Aug 6, 2012 5:28:42 PM
 * 
 */
package com.pixshow.framework.ddb.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.mvel2.MVEL;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.ddb.api.DDBCentext;
import com.pixshow.framework.ddb.api.DDBCentextHolder;
import com.pixshow.framework.ddb.api.DDBRoutingRuleAdapter;
import com.pixshow.framework.ddb.api.Sharding;
import com.pixshow.framework.ddb.api.annotation.DistributedDataSource;
import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.plugin.api.PluginRegisterManager;
import com.pixshow.framework.utils.AnnotationUtility;
import com.pixshow.framework.utils.ClassUtility;
import com.pixshow.framework.utils.StringUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.1 $ $Date: 2012/08/09 07:22:02 $
 * @since Aug 6, 2012
 * 
 */
@Aspect
@Component
public class RoutingDataSourceProcessor implements Ordered {
    private Log                        log    = LogFactory.getLog(getClass());
    private static Map<String, Object> expMap = new HashMap<String, Object>();

    private Object getValue(Object root, String key) {
        try {
            Object exprObject = expMap.get(key);
            if (exprObject == null) {
                exprObject = MVEL.compileExpression(key);
                synchronized (expMap) {
                    expMap.put(key, exprObject);
                }
            }
            return MVEL.executeExpression(exprObject, root);
        } catch (Exception e) {
            log.error("Error Key = " + key);
        }
        return null;
    }

    @Around("atMethodDistributedDataSource() || atClassDistributedDataSource()")
    public Object process(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        Class<?> cls = ClassUtility.getUserClass(pjp.getTarget().getClass());

        DistributedDataSource ann = AnnotationUtility.findAnnotation(method, DistributedDataSource.class);
        if (ann == null) {
            ann = AnnotationUtility.findAnnotation(cls, DistributedDataSource.class);
        }

        // 
        Object[] args = null;
        if (ann.args() != null) {
            String[] argNames = ann.args();
            args = new Object[argNames.length];
            for (int i = 0; i < args.length; i++) {
                args[i] = getValue(pjp, argNames[i]);
            }
        }

        // 
        Sharding sharding = null;
        if (StringUtility.isNotEmpty(ann.rule())) {// 基于规则 获得 Sharding
            DDBRoutingRuleAdapter adapter = PluginRegisterManager.getInstance().getUniquePlugin(DDBRoutingRuleAdapter.class, ann.rule());
            if (adapter != null) {
                sharding = adapter.getSharding(ann, args);
            } else {
                throw new SysException("没有找到的规则定义[" + ann.rule() + "]");
            }
        } else {// 指定 Sharding
            String dbName = ann.dbName();
            String schema = ann.schema();

            if (StringUtility.isNotEmpty(dbName) && dbName.startsWith("${cfg:")) {
                dbName = Config.getInstance().getString(dbName.substring("${cfg:".length(), dbName.length() - 1));
            }
            if (StringUtility.isNotEmpty(schema) && schema.startsWith("${cfg:")) {
                schema = Config.getInstance().getString(schema.substring("${cfg:".length(), schema.length() - 1));
            }
            sharding = new Sharding();
            sharding.setDbName(dbName);
            sharding.setSchema(schema);
        }

        //
        DDBCentext oldDDBCentext = DDBCentextHolder.get();

        DDBCentext newCentext = new DDBCentext();
        newCentext.setRule(ann.rule());
        newCentext.setArgs(args);
        newCentext.setSharding(sharding);

        DDBCentextHolder.set(newCentext);

        Object rs = null;
        try {
            rs = pjp.proceed();
        } finally {
            DDBCentextHolder.set(oldDDBCentext);
        }

        return rs;
    }

    @Pointcut("@annotation(com.pixshow.framework.ddb.api.annotation.DistributedDataSource)")
    public void atMethodDistributedDataSource() {
    }

    @Pointcut("@within(com.pixshow.framework.ddb.api.annotation.DistributedDataSource)")
    public void atClassDistributedDataSource() {
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
