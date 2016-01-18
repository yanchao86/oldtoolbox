/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:SecurityHandlerRegisterManager.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 1, 2012 9:26:48 AM
 * 
 */
package com.pixshow.framework.plugin.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.plugin.api.annotation.Plugin;
import com.pixshow.framework.utils.AppContextUtility;
import com.pixshow.framework.utils.StringUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: gaopeng.300.cn $
 * @version $Revision: 1.4 $ $Date: 2012/07/27 05:42:48 $
 * @since Jun 1, 2012
 * 
 */

public class PluginRegisterManager {

    private static PluginRegisterManager          manager           = new PluginRegisterManager();

    private Map<Class<?>, List<PluginDefinition>> definitionMapping = new HashMap<Class<?>, List<PluginDefinition>>();

    private PluginRegisterManager() {
    }

    public static PluginRegisterManager getInstance() {
        return manager;
    }

    public <T> T getPlugin(PluginDefinition definition) {
        return (T) AppContextUtility.getContext().getBean(definition.getBeanName());
    }

    public <T> T getUniquePlugin(Class<T> cls) {
        T plugin = null;
        List<PluginDefinition> all = definitionMapping.get(cls);
        if (all != null) {
            if (all.size() != 1) {
                throw new SysException("重复定义插件 [" + cls.getName() + "] size:" + all.size());
            }
            plugin = (T) getPlugin(all.get(0));
        }
        return plugin;
    }

    public <T> T getUniquePlugin(Class<?> cls, String name) {
        T plugin = null;
        List<PluginDefinition> all = definitionMapping.get(cls);
        for (PluginDefinition adapterDefinition : all) {
            if (adapterDefinition.getAnnotation().name().equals(name)) {
                if (plugin != null) {
                    throw new SysException("重复定义插件 [" + name + "] -> " + adapterDefinition.getBeanName());
                }
                plugin = (T) getPlugin(adapterDefinition);
            }
        }
        return plugin;
    }

    public List<PluginDefinition> getDefinitions(Class<?> cls) {
        return definitionMapping.get(cls);
    }

    private List<PluginDefinition> getDefinitions(Class<?> cls, String name) {
        List<PluginDefinition> defs = new ArrayList<PluginDefinition>();
        List<PluginDefinition> all = definitionMapping.get(cls);
        if (all != null) {
            if (StringUtility.isEmpty(name)) {
                defs.addAll(all);
            } else {
                for (PluginDefinition def : all) {
                    if (def.getAnnotation().name().equals(name)) {
                        defs.add(def);
                    }
                }
            }
        }
        return defs;
    }

    public <T> boolean hasPlugin(Class<T> cls) {
        return hasPlugin(cls, null);
    }

    public <T> boolean hasPlugin(Class<T> cls, String name) {
        List<PluginDefinition> defs = getDefinitions(cls, name);
        return defs.size() > 0 ? true : false;
    }

    public synchronized void register(String beanName, Plugin annotation) {
        List<PluginDefinition> definitions = definitionMapping.get(annotation.type());
        if (definitions == null) {
            definitions = new ArrayList<PluginDefinition>();
        }
        definitions.add(new PluginDefinition(beanName, annotation));
        Collections.sort(definitions, new Comparator<PluginDefinition>() {
            @Override
            public int compare(PluginDefinition o1, PluginDefinition o2) {
                return o1.getAnnotation().priority() - o2.getAnnotation().priority();
            }
        });
        definitionMapping.put(annotation.type(), definitions);
    }
}
