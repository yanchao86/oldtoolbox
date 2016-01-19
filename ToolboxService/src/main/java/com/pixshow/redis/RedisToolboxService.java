package com.pixshow.redis;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.service.DownloadService;
import com.pixshow.toolboxmgr.service.StatService;

import net.sf.json.JSONArray;

@Service
public class RedisToolboxService extends AbstractRedisService<String, String> {
    private final static Log log = LogFactory.getLog(RedisToolboxService.class);

    @Autowired
    private DownloadService downloadService;
    @Autowired
    private StatService     statService;

    public void downLoadStat(Integer appId, Integer diyId, String code) {
        Date date = new Date();
        String day = DateUtility.format(date, "yyyyMMdd");
        if (appId != null) {
            String key = "downLoadStat@tb_toolbox@" + appId + "@" + day;
            stat(key);
        }
        if (diyId != null) {
            String key = "downLoadStat@tb_diybox@" + diyId + "@" + day;
            stat(key);
        }
        if (StringUtility.isNotEmpty(code)) {
            String key = "downLoadStat@tb_toolbox_day_stat@" + code + "@" + day;
            stat(key);
        }
    }

    @Scheduled(fixedRate = 1000 * 5)
    public void mysql2redis() {
        String pattern = "downLoadStat@*";
        Set<String> keys = redisTemplate.keys(pattern);
        System.out.println("stat start, keys=" + JSONArray.fromObject(keys).toString());
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            try {
                String key = it.next();
                String[] arr = key.split("@");
                String table = arr[1];
                String toolId = arr[2];
                String day = arr[3];
                String count = this.get(key);
                this.set(key, "0");

                if ("tb_toolbox_day_stat".equals(table)) {
                    statService.pvStat(toolId, Integer.parseInt(count), Integer.parseInt(day));
                } else {
                    downloadService.addDownloadCount(table, Integer.parseInt(toolId), Integer.parseInt(day), Integer.parseInt(count));
                }
            } catch (Exception e) {
                log.info("error > mysql2redis > downLoadStat " + e.getMessage());
            }

        }

    }

    private void stat(final String key) {
        SessionCallback callback = new SessionCallback<Object>() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                do {
                    operations.watch(key);
                    if (operations.hasKey(key)) {
                        BoundValueOperations<String, String> valueOper = operations.boundValueOps(key);
                        String val = valueOper.get();
                        operations.multi();
                        String nVal = (Integer.parseInt(val) + 1) + "";
                        valueOper.set(nVal);
                    } else {
                        operations.multi();
                        BoundValueOperations<String, String> valueOper = operations.boundValueOps(key);
                        valueOper.set(1 + "");
                    }
                } while (operations.exec() == null);
                return null;
            }
        };
        redisTemplate.execute(callback);
    }
}