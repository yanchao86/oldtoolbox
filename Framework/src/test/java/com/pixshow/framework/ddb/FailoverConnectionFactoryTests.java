/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FailoverConnectionFactoryTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 18, 2013 3:08:39 PM
 * 
 */
package com.pixshow.framework.ddb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.pixshow.framework.ddb.internal.FailoverConnectionFactory;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 18, 2013
 * 
 */

public class FailoverConnectionFactoryTests {
    public static void main(String[] args) {

        List<Properties> serverList = new ArrayList<Properties>();
        Properties server1 = new Properties();
        server1.put("driverClassName", "com.mysql.jdbc.Driver");
        server1.put("url", "jdbc:mysql://127.0.0.1:3306/test_1?useUnicode=true&characterEncoding=utf-8");
        server1.put("user", "root");
        server1.put("password", "root");

        Properties server2 = new Properties();
        server2.put("driverClassName", "com.mysql.jdbc.Driver");
        server2.put("url", "jdbc:mysql://192.168.1.211:3306/test_1?useUnicode=true&characterEncoding=utf-8");
        server2.put("user", "root");
        server2.put("password", "root");

        serverList.add(server1);
        serverList.add(server2);

        FailoverConnectionFactory factory = new FailoverConnectionFactory(serverList);
        try {
            Connection connection = factory.createConnection();
            QueryRunner runner = new QueryRunner();
            System.out.println(runner.query(connection, "select * from t1", new MapListHandler()));
            DbUtils.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
