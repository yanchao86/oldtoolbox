package com.pixshow.toolboxmgr.service;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.dao.StatDao;

@Service
public class StatService extends BaseService {

    @Resource
    private StatDao statDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void pvStat(String code, int count, Date date) {
        int day = Integer.parseInt(DateUtility.format(date, "yyyyMMdd"));
        statDao.countDayStat(code, day, count);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void pvStat(String code, int count, int day) {
        statDao.countDayStat(code, day, count);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void uvStat(String code, String uid, Date date) {
        if (StringUtility.isEmpty(uid)) {
            return;
        }
        int day = Integer.parseInt(DateUtility.format(date, "yyyyMMdd"));
        if (statDao.findDayStatUv(uid, code, day)) {
            statDao.countDayStat(code, day, 1);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void pvStat(String code, String productCode, String productVersion, String sdkVersion, int count, Date date) {
        int day = Integer.parseInt(DateUtility.format(date, "yyyyMMdd"));
        statDao.countDayStat(code, productCode, productVersion, sdkVersion, day, count);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void uvStat(String code, String productCode, String productVersion, String sdkVersion, String uid, Date date) {
        if (StringUtility.isEmpty(uid)) {
            return;
        }
        int day = Integer.parseInt(DateUtility.format(date, "yyyyMMdd"));
        if (statDao.findDayStatUv(uid, code, productCode, productVersion, sdkVersion, day)) {
            statDao.countDayStat(code, productCode, productVersion, sdkVersion, day, 1);
        }
    }

    public void bkw(String flag) {
        try {
            JSONObject json = JSONObject.fromObject(flag);
            String mac = json.optString("mac");
            String productCode = json.optString("productCode");
            String productVersion = json.optString("productVersion");
            String sdkVersion = json.optString("toolSdkVersion");
            JSONArray queue = json.optJSONArray("queue");
            for (int i = 0; i < queue.size(); i++) {
                int date = DateUtility.currentUnixTime();
                JSONObject item = queue.getJSONObject(i);

                String code = item.optString("code");
                String type = item.optString("type");
                int unixtime = item.optInt("unixtime", date);
                //传递时间大于当前两个小时 或者 小于七天的都不要
                if (unixtime > date + 2 * 60 * 60 || unixtime < date - 7 * 24 * 60 * 60) {
                    continue;
                }
                if ("uv".equals(type)) {// uv
                    if (StringUtility.isNotEmpty(productCode)) {
                        uvStat(code, productCode, productVersion, sdkVersion, mac, new Date(unixtime * 1000L));
                    }
                    uvStat(code, mac, new Date(unixtime * 1000L));
                } else { // pv
                    int count = item.optInt("count", 0);
                    if (StringUtility.isNotEmpty(productCode)) {
                        pvStat(code, productCode, productVersion, sdkVersion, count, new Date(unixtime * 1000L));
                    }
                    pvStat(code, count, new Date(unixtime * 1000L));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
