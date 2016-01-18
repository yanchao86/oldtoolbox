package com.pixshow.framework.utils.geo;

import java.util.HashSet;
import java.util.Set;

public class GeoHashUtility {
    /**
     * 
     * @param lat
     * @param lng
     * @param range 米
     * @return
     */
    public static Set<GeoHash> nearby(double lat, double lng, int range) {
        Set<GeoHash> geoHashList = new HashSet<GeoHash>();
        // bit 12=479232 14=239616 16=119808 18=59904 20=29952 22=14976 24=7488 26=3744 28=1875 30=937 32=469 34=234M 36=117
        // char 5=3746m 6=937m 7=117 8=29
        int baseDepth = 36;//117M
        int depth = getDepth(117, range);
        if (depth % 2 == 0) {
            GeoHash geoHash = GeoHash.withBitPrecision(lat, lng, baseDepth - depth);
            geoHashList.addAll(getAdjacentGeoHash(geoHash));
        } else {
            GeoHash geoHash = GeoHash.withBitPrecision(lat, lng, baseDepth - (depth - 1));
            for (GeoHash g : geoHash.getAdjacent()) {
                geoHashList.addAll(getAdjacentGeoHash(g));
            }
        }
        return geoHashList;
    }

    /**
     * bit 28=1875 30=937 32=469 34=234M 36=117
     * 
     * @param lat
     * @param lng
     * @param depthOfBit
     * @return
     */
    public static String geohashBit(double lat, double lng, int depthOfBit) {
        GeoHash geoHash = GeoHash.withBitPrecision(lat, lng, depthOfBit);
        return geoHash.toBinaryString();
    }

    /**
     * char 5=3746m 6=937m 7=117 8=29
     * 
     * @param lat
     * @param lng
     * @param depthOfChar
     * @return
     */
    public static String geohashBase32(double lat, double lng, int depthOfChar) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, lng, depthOfChar);
        return geoHash.toBase32();
    }

    public static Set<String> nearbyBit(double lat, double lng, int range) {
        Set<String> bitSet = new HashSet<String>();
        Set<GeoHash> geoHahsSet = nearby(lat, lng, range);
        for (GeoHash geoHash : geoHahsSet) {
            bitSet.add(geoHash.toBinaryString());
        }
        return bitSet;
    }

    private static Set<GeoHash> getAdjacentGeoHash(GeoHash geoHash) {
        Set<GeoHash> geoHashList = new HashSet<GeoHash>();
        geoHashList.add(geoHash);
        for (GeoHash g : geoHash.getAdjacent()) {
            geoHashList.add(g);
        }
        return geoHashList;
    }

    private static int getDepth(int base, double range) {
        int depth = 0, maxDepth = 100;
        for (int i = 0; i < maxDepth; i++, depth++) {
            double distance = base * Math.pow(2, i);
            double value = distance * 1;
            if (range <= value) break;
            depth++;
            value = distance * 2;
            if (range <= value) break;
        }
        return depth;
    }
}
