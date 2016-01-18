/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:BaseDaoTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 8, 2013 9:48:51 AM
 * 
 */
package com.pixshow.framework.support;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Mar 8, 2013
 * 
 */
@Repository
public class BaseDaoTests extends BaseDao {

    public static enum Type implements BaseEnum<Integer> {
        T1(1, "类型1"), T2(2, "类型2"), T3(3, "类型3"), T4(4, "类型4");

        private int    value;
        private String label;

        private Type(int value, String label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String getLable() {
            return label;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }

    public static enum Code implements BaseEnum<String> {
        C1("X1", "类型1"), C2("X2", "类型2"), C3("X3", "类型3"), C4("X4", "类型4");

        private String value;
        private String label;

        private Code(String value, String label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String getLable() {
            return label;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public static class TestBean extends BaseBean {
        private static final long serialVersionUID = 1L;
        private int               id;
        private String            name;
        private Date              date;
        private double            money;
        private JSONObject        json;
        private Type              type;
        private Code              code;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public JSONObject getJson() {
            return json;
        }

        public void setJson(JSONObject json) {
            this.json = json;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public Code getCode() {
            return code;
        }

        public void setCode(Code code) {
            this.code = code;
        }

    }

    public void query() {
        List<TestBean> list = queryForList("select * from t1", TestBean.class);
        for (TestBean bean : list) {
            System.out.println(bean);
        }
    }

    public void insert() {
        TestBean bean = new TestBean();
        bean.setName("test-" + System.currentTimeMillis());
        bean.setDate(new Date());
        bean.setMoney(234.323);
        JSONObject json = new JSONObject();
        json.put("A", 1);
        json.put("B", new String[] { "B1", "B2" });
        json.put("A", "xx");
        bean.setJson(json);
        bean.setType(Type.T3);
        bean.setCode(null);
        System.out.println(insertBean("t1", bean));
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/config/framework/spring.xml");
        BaseDaoTests dao = context.getBean(BaseDaoTests.class);
        dao.query();
//        dao.insert();
    }

}
