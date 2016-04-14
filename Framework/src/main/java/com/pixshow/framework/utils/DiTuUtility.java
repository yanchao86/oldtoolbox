/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:MapUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 16, 2013 2:26:31 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 16, 2013
 * 
 */

public class DiTuUtility {

    public static class Address {
        /**国*/
        private String countryName;
        /**省*/
        private String administrativeAreaName;
        /**市*/
        private String localityName;
        /**县*/
        private String dependentLocalityName;

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getAdministrativeAreaName() {
            return administrativeAreaName;
        }

        public void setAdministrativeAreaName(String administrativeAreaName) {
            this.administrativeAreaName = administrativeAreaName;
        }

        public String getLocalityName() {
            return localityName;
        }

        public void setLocalityName(String localityName) {
            this.localityName = localityName;
        }

        public String getDependentLocalityName() {
            return dependentLocalityName;
        }

        public void setDependentLocalityName(String dependentLocalityName) {
            this.dependentLocalityName = dependentLocalityName;
        }

        @Override
        public String toString() {
            return countryName + "/" + administrativeAreaName + "/" + localityName + "/" + dependentLocalityName;
        }

    }

    public static Address getAddressFromGoogoleMap(double lat, double lng, String language) {
        String url = "http://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng=" + lat + "," + lng + "&language=" + language;
        JSONObject json = null;
        int maxRetryCount = 3;
        for (int retryCount = 0; retryCount < maxRetryCount; retryCount++) {
            String response = HttpUtility.get(url, null);
            System.out.println(response);
            try {
                JSONObject rs = JSONObject.fromObject(response);
                if ("OVER_QUERY_LIMIT".equalsIgnoreCase(rs.getString("status"))) {
                    Thread.sleep(100);
                    continue;
                }
                if ("OK".equalsIgnoreCase(rs.getString("status"))) {
                    json = rs;
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (json != null) {
            Map<String, String> address_map = new HashMap<String, String>();
            JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < results.size(); i++) {
                JSONObject result = results.getJSONObject(i);
                JSONArray address_components = result.getJSONArray("address_components");
                for (int j = 0; j < address_components.size(); j++) {
                    JSONObject address_component = address_components.getJSONObject(j);
                    JSONArray types = address_component.getJSONArray("types");
                    String long_name = address_component.getString("long_name");
                    String key = "";
                    for (int k = 0; k < types.size(); k++) {
                        String type = types.getString(k);
                        if (key.length() > 0) {
                            key += "+";
                        }
                        key += type;
                    }
                    if (!address_map.containsKey(key) || (address_map.containsKey(key) && long_name.length() > address_map.get(key).length())) {
                        address_map.put(key, long_name);
                    }
                }
            }

            Address address = new Address();
            address.setCountryName(address_map.get("country+political"));
            address.setAdministrativeAreaName(findOneValue(address_map, //
                    "administrative_area_level_1+political",//
                    "administrative_area_level_2+political",//
                    "administrative_area_level_3+political",//
                    "colloquial_area+political",//
                    "locality+political"));
            address.setLocalityName(findOneValue(address_map, //
                    "locality+political",//
                    "colloquial_area+political",//
                    "administrative_area_level_3+political",//
                    "administrative_area_level_2+political",//
                    "administrative_area_level_1+political"));
            address.setDependentLocalityName(address_map.get("sublocality+political"));
            return address;
        }
        return null;
    }

    private static String findOneValue(Map<String, String> map, String... keys) {
        for (String key : keys) {
            String value = map.get(key);
            if (value != null) {
                map.remove(key);
                return value;
            }
        }
        return null;
    }

    public static Address getAddressFromBaiduMap(double lat, double lng) {
        String url = "http://api.map.baidu.com/geocoder?location=" + lat + "," + lng + "&output=json&key=b72307eceed5b86dd8870afbbc9b0745";
        String response = HttpUtility.get(url);
        if (StringUtils.isEmpty(response)) {
            return null;
        }
        JSONObject json = JSONObject.fromObject(response);
        String status = json.optString("status");
        if ("OK".equals(status)) {
            net.sf.json.JSONObject result = json.optJSONObject("result");
            String formatted_address = result.optString("formatted_address");
            if (StringUtils.isEmpty(formatted_address)) {
                // 查询不到地址，属于国外。
                return null;
            }
            Address address = new Address();
            address.setCountryName("中国");
            JSONObject addressComponent = result.optJSONObject("addressComponent");
            address.setAdministrativeAreaName(addressComponent.optString("province"));
            address.setLocalityName(addressComponent.optString("city"));
            address.setDependentLocalityName(addressComponent.optString("district"));
            return address;
        }
        return null;
    }
}
