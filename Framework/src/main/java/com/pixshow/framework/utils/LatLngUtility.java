/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:LatLngUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 15, 2013 10:38:32 AM
 * 
 */
package com.pixshow.framework.utils;

import java.awt.Point;
import java.awt.Polygon;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

public class LatLngUtility {

    public static class LatLng {
        private double lat;
        private double lng;

        public LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        @Override
        public String toString() {
            return lat + "," + lng;
        }
    }

    private static class OffsetLatLng {
        int lat;
        int lng;
    }

    private static Map<String, OffsetLatLng> offsetData = new HashMap<String, OffsetLatLng>();
    private static int                       scale      = 10000;

    static {
        init();
    }

    private static void init() {
        InputStream input = null;
        try {
            input = LatLngUtility.class.getResourceAsStream("LatLngUtility_offsetData.properties");
            Properties properties = new Properties();
            properties.load(input);
            for (Object key : properties.keySet()) {
                String value = properties.getProperty(key.toString());
                String[] latLng = value.split(",");
                OffsetLatLng offsetLatLng = new OffsetLatLng();
                offsetLatLng.lat = Integer.valueOf(latLng[0]);
                offsetLatLng.lng = Integer.valueOf(latLng[1]);
                offsetData.put((String) key, offsetLatLng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static LatLng offset(LatLng latLng) {
        double newLat = latLng.getLat();
        double newLng = latLng.getLng();
        String key = (int) (latLng.getLat() * 10) + "_" + (int) (latLng.getLng() * 10);
        OffsetLatLng offset = offsetData.get(key);
        if (offset != null) {
            newLat = latLng.getLat() + offset.lat * 0.0001;
            newLng = latLng.getLng() + offset.lng * 0.0001;
        }
        return new LatLng(newLat, newLng);
    }

    private static Point toPoint(LatLng ll) {
        return new Point((int) (ll.getLng() * scale), (int) (ll.getLat() * scale));
    }

    private static LatLng toLL(Point p) {
        return new LatLng((double) p.getY() / scale, (double) p.getX() / scale);
    }

    private static List<Point> toPoint(List<LatLng> llList) {
        List<Point> pList = new ArrayList<Point>();
        for (LatLng ll : llList) {
            pList.add(toPoint(ll));
        }
        return pList;
    }

    private static List<LatLng> toLL(List<Point> pList) {
        List<LatLng> llList = new ArrayList<LatLng>();
        for (Point p : pList) {
            llList.add(toLL(p));
        }
        return llList;
    }

    public static List<LatLng> getPolygon(List<LatLng> llList) {
        List<Point> pList = toPoint(llList);
        pList = convexHull(pList);
        return toLL(pList);
    }

    public static double[] calculateBounds(List<LatLng> list) {
        double boundsMinLng = -1;
        double boundsMaxLng = -1;

        double boundsMinLat = -1;
        double boundsMaxLat = -1;

        for (LatLng ll : list) {
            boundsMinLng = (boundsMinLng == -1 || boundsMinLng > ll.getLng()) ? ll.getLng() : boundsMinLng;
            boundsMaxLng = (boundsMaxLng == -1 || boundsMaxLng < ll.getLng()) ? ll.getLng() : boundsMaxLng;

            boundsMinLat = (boundsMinLat == -1 || boundsMinLat > ll.getLat()) ? ll.getLat() : boundsMinLat;
            boundsMaxLat = (boundsMaxLat == -1 || boundsMaxLat < ll.getLat()) ? ll.getLat() : boundsMaxLat;
        }
        return new double[] { boundsMinLng, boundsMaxLng, boundsMinLat, boundsMaxLat };
    }

    public static boolean contains(List<LatLng> list, LatLng targetLL) {
        Polygon polygon = new Polygon();
        for (LatLng ll : list) {
            polygon.addPoint((int) (ll.getLng() * scale), (int) (ll.getLat() * scale));
        }
        return polygon.contains(toPoint(targetLL));
    }

    /**
     * 
     * @param polygon
     * @return 1 = 100㎞
     *
     */
    public static double getPolygonArea(List<LatLng> polygon) {
        //S = 0.5 * ( (x0*y1-x1*y0) + (x1*y2-x2*y1) + ... + (xn*y0-x0*yn) )
        double area = 0.00;
        for (int i = 0; i < polygon.size(); i++) {
            if (i < polygon.size() - 1) {
                area += polygon.get(i).getLng() * polygon.get(i + 1).getLat() - polygon.get(i + 1).getLng() * polygon.get(i).getLat();
            } else {
                area += polygon.get(i).getLng() * polygon.get(0).getLat() - polygon.get(0).getLng() * polygon.get(i).getLat();
            }
        }
        area = area / 2.00;
        return (area < 0 ? -area : area);
    }

    private static List<Point> convexHull(List<Point> points) {
        List<Point> xSorted = new ArrayList<Point>(points);
        Collections.sort(xSorted, new XCompare());

        int n = xSorted.size();

        Point[] lUpper = new Point[n];

        lUpper[0] = xSorted.get(0);
        lUpper[1] = xSorted.get(1);

        int lUpperSize = 2;

        for (int i = 2; i < n; i++) {
            lUpper[lUpperSize] = xSorted.get(i);
            lUpperSize++;

            while (lUpperSize > 2 && !rightTurn(lUpper[lUpperSize - 3], lUpper[lUpperSize - 2], lUpper[lUpperSize - 1])) {
                // Remove the middle point of the three last
                lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
                lUpperSize--;
            }
        }

        Point[] lLower = new Point[n];

        lLower[0] = xSorted.get(n - 1);
        lLower[1] = xSorted.get(n - 2);

        int lLowerSize = 2;

        for (int i = n - 3; i >= 0; i--) {
            lLower[lLowerSize] = xSorted.get(i);
            lLowerSize++;

            while (lLowerSize > 2 && !rightTurn(lLower[lLowerSize - 3], lLower[lLowerSize - 2], lLower[lLowerSize - 1])) {
                // Remove the middle point of the three last
                lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
                lLowerSize--;
            }
        }

        ArrayList<Point> result = new ArrayList<Point>();

        for (int i = 0; i < lUpperSize; i++) {
            result.add(lUpper[i]);
        }

        for (int i = 1; i < lLowerSize - 1; i++) {
            result.add(lLower[i]);
        }

        return result;
    }

    private static boolean rightTurn(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) > 0;
    }

    private static class XCompare implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            return (new Integer(o1.x)).compareTo(new Integer(o2.x));
        }
    }

    public static double distanceSq(LatLng point1, LatLng point2) {
        double x1 = point1.lng, y1 = point1.lat, x2 = point2.lng, y2 = point2.lat;
        x1 -= x2;
        y1 -= y2;
        return x1 * x1 + y1 * y1;
    }

    /**
     * 返回两点的距离，单位米
     * @param point1
     * @param point2
     * @return
     *
     */
    public static double distanceM(LatLng point1, LatLng point2) {
        double lng1 = point1.lng, lat1 = point1.lat, lng2 = point2.lng, lat2 = point2.lat;
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + //
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        return dist;
    }

    public static double distance(LatLng point1, LatLng point2) {
        return Math.sqrt(distanceSq(point1, point2));
    }

    public static double distanceSq(LatLng p1, LatLng p2, LatLng pp) {
        double x1 = p1.lng, y1 = p1.lat, x2 = p2.lng, y2 = p2.lat, px = pp.lng, py = pp.lat;
        // Adjust vectors relative to x1,y1
        // x2,y2 becomes relative vector from x1,y1 to end of segment
        x2 -= x1;
        y2 -= y1;
        // px,py becomes relative vector from x1,y1 to test point
        px -= x1;
        py -= y1;
        double dotprod = px * x2 + py * y2;
        double projlenSq;
        if (dotprod <= 0.0) {
            // px,py is on the side of x1,y1 away from x2,y2
            // distance to segment is length of px,py vector
            // "length of its (clipped) projection" is now 0.0
            projlenSq = 0.0;
        } else {
            // switch to backwards vectors relative to x2,y2
            // x2,y2 are already the negative of x1,y1=>x2,y2
            // to get px,py to be the negative of px,py=>x2,y2
            // the dot product of two negated vectors is the same
            // as the dot product of the two normal vectors
            px = x2 - px;
            py = y2 - py;
            dotprod = px * x2 + py * y2;
            if (dotprod <= 0.0) {
                // px,py is on the side of x2,y2 away from x1,y1
                // distance to segment is length of (backwards) px,py vector
                // "length of its (clipped) projection" is now 0.0
                projlenSq = 0.0;
            } else {
                // px,py is between x1,y1 and x2,y2
                // dotprod is the length of the px,py vector
                // projected on the x2,y2=>x1,y1 vector times the
                // length of the x2,y2=>x1,y1 vector
                projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
            }
        }
        // Distance to line is now the length of the relative point
        // vector minus the length of its projection onto the line
        // (which is zero if the projection falls outside the range
        //  of the line segment).
        double lenSq = px * px + py * py - projlenSq;
        if (lenSq < 0) {
            lenSq = 0;
        }
        return lenSq;
    }

    public static double distance(LatLng p1, LatLng p2, LatLng pp) {
        return Math.sqrt(distanceSq(p1, p2, pp));
    }

    public static double distance(List<LatLng> polygon, LatLng point) {
        return Math.sqrt(distanceSq(polygon, point));
    }

    public static double distanceSq(List<LatLng> polygon, LatLng point) {
        if (polygon == null || polygon.size() == 0) {
            return Double.MAX_VALUE;
        }
        if (polygon.size() == 1) {
            return distanceSq(polygon.get(0), point);
        }

        LatLng prePoint = polygon.get(0);
        double minDistance = distanceSq(polygon.get(polygon.size() - 1), prePoint, point);
        for (int i = 1; i < polygon.size(); i++) {
            LatLng nextPoint = polygon.get(i);
            double distance = distanceSq(prePoint, nextPoint, point);
            minDistance = Math.min(minDistance, distance);
            prePoint = nextPoint;
        }
        return minDistance;
    }

}
