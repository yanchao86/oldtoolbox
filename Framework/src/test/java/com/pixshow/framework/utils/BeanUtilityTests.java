/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:BeanUtilityTests.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 27, 2012 11:18:58 AM
 * 
 */
package com.pixshow.framework.utils;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 27, 2012
 * 
 */

public class BeanUtilityTests {

    public static class Type1 {
        private String name;
        private int    user_id;
        private String password;
        private String photo_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhoto_id() {
            return photo_id;
        }

        public void setPhoto_id(String photo_id) {
            this.photo_id = photo_id;
        }

    }

    public static class Type2 {
        private String name;
        private String userId;
        private String passWord;
        private String photoId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public String getPhotoId() {
            return photoId;
        }

        public void setPhotoId(String photoId) {
            this.photoId = photoId;
        }

    }

    public static void main(String[] args) throws Exception {
        Type1 type1 = new Type1();
        type1.setName("aaa");
        type1.setPassword("123456");
        type1.setUser_id(111000);
        type1.setPhoto_id("123123");
        Type2 type2 = new Type2();
        BeanUtility.copySameProperties(type2, type1);
        System.out.println(ToStringBuilder.reflectionToString(type2));

        Map<String, Object> map = BeanUtils.describe(type1);
        System.out.println(map);

    }
}
