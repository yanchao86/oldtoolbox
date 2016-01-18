/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:RegexUtilityTests.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Nov 14, 2013 2:53:53 PM
 * 
 */
package com.pixshow.framework.utils;


/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Nov 14, 2013
 * 
 */

public class RegexUtilityTests {
    public static void main(String[] args) {
        String html = "<div>123456，</div>汉<a>字汉字汉<span uuu='lkkk;hjh-kjkj:lkskm;hjk'>字汉字。<br>汉字dadasdasdasdadsadda打死打死的汉字<br><br><br>汉字汉字汉字汉字汉字汉字阿斯达";
        html = html.replaceAll("<br>", "。").replaceAll("。+", "。").replaceAll("<[^>]*>", "");
        html = html.substring(0, 10);
        System.out.println(html);
        System.out.println(html.length());
    }
}
