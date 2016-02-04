/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:DlDayStatBean.java Project: ToolboxManager
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年2月17日 下午2:47:21
 * 
 */
package com.pixshow.toolboxmgr.bean;


/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since 2014年2月17日
 */
public class DlDayStatBean extends ToolboxBean {
    private int toolId;
    private int day;
    private int count;

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
