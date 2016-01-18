/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ScriptManager.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 6, 2013 2:54:24 PM
 * 
 */
package com.pixshow.framework.script.api;

import java.util.Map;

import org.python.util.PythonInterpreter;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jun 6, 2013
 * 
 */

public class ScriptManager {
    public static void exec(String fileName, Map<String, Object> appContext) {
        String scriptHome = ScriptManager.class.getResource("/").getPath();
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("import sys");
        interpreter.exec("sys.path.append('" + scriptHome + "')");
        interpreter.set("appContext", appContext);
        interpreter.execfile(scriptHome + fileName);
    }
}
