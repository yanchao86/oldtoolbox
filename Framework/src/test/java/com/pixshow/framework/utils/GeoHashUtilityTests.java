package com.pixshow.framework.utils;

import com.pixshow.framework.utils.geo.GeoHashUtility;

public class GeoHashUtilityTests {
    public static void main(String[] args) {
        System.out.println(GeoHashUtility.nearbyBit(61.433601, 98.655524, 1144243));
        //        System.out.println(GeoHashUtility.geohashBit(89, 89, 50));
        //11100111010010001110011111010010010111000000001101
    }
}
