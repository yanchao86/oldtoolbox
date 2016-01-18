/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:LatLngUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 15, 2013 10:38:58 AM
 * 
 */
package com.pixshow.framework.utils;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.LatLngUtility.LatLng;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Mar 15, 2013
 * 
 */
public class LatLngUtilityTests {

    @Component
    public static class OffData extends BaseDao {
        public void savaFile() throws Exception {
            List<Map<String, Object>> data = getJdbcTemplate().queryForList("select * from gpst");
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new File("E:/latlngoffdata.properties"));
                for (Map<String, Object> row : data) {
                    writer.println(row.get("lat") + "_" + row.get("log") + "=" + row.get("offLat") + "," + row.get("offLog"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            }
        }
    }

    public static void test_distanceSq() {
        //        LatLng point = new LatLng(39.80973932856527, 116.7681884765625);//0.0036069523077 0.0600579079527 5公里
        //        LatLng point = new LatLng(39.88502646177363, 116.3177490234375);//0.0071780193146 0.0847231923062 10公里
        LatLng point = new LatLng(39.59039433192809, 116.3232421875);//0.0264919962761 0.1627636208621 20公里
        List<LatLng> polygon = new ArrayList<LatLng>();
        polygon.add(new LatLng(40.10028233352754, 116.77780151367188));
        polygon.add(new LatLng(39.97937275738245, 116.33834838867188));
        polygon.add(new LatLng(39.94358452206439, 116.74484252929688));
        polygon.add(new LatLng(39.76436248471943, 116.22848510742188));
        polygon.add(new LatLng(39.72423711100816, 116.59927368164062));
        polygon.add(new LatLng(39.96884875032064, 116.84097290039062));
        polygon.add(new LatLng(39.6925426809666, 116.96731567382812));
        polygon.add(new LatLng(39.8508691855493, 117.15408325195312));
        polygon.add(new LatLng(39.97832042960035, 116.9879150390625));
        polygon.add(new LatLng(40.14963581975146, 117.14309692382812));
        polygon.add(new LatLng(40.22097743090861, 116.883544921875));
        polygon.add(new LatLng(40.16223096750591, 116.81488037109375));
        polygon.add(new LatLng(40.21153946705412, 116.57730102539062));
        polygon.add(new LatLng(40.12233831926826, 116.40975952148438));

        System.out.println(new DecimalFormat("#0.0000000000000").format(LatLngUtility.distanceSq(polygon, point)));
        System.out.println(new DecimalFormat("#0.0000000000000").format(LatLngUtility.distance(polygon, point)));
    }

    public static void test_offset() {
        System.out.println(LatLngUtility.offset(new LatLng(39.906535, 116.391183)));
        System.out.println(LatLngUtility.offset(new LatLng(35.585302, 116.996369)));
        System.out.println(LatLngUtility.offset(new LatLng(40.689856, -74.045041)));
        System.out.println(LatLngUtility.offset(new LatLng(29.655715, 91.117982)));
    }

    public static void distance() {
        LatLng point1 = new LatLng(39.913828,116.315732);
        LatLng point2 = new LatLng(39.910729,116.467509);
        double dis = LatLngUtility.distanceM(point1, point2);
        System.out.println(dis);
    }
    public static void main(String[] args) {
        //        test_distanceSq();
//        double d = 0;
//        for (int i = 0; i < 100; i++) {
//            d += 0.01;
//            System.out.println(d + " = " + new DecimalFormat("#0.0000000000000").format(d * d));
//        }
        distance();
    }

}
