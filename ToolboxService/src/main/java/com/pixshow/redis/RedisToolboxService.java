package com.pixshow.redis;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.service.DownloadService;
import com.pixshow.toolboxmgr.service.StatService;

@Service
public class RedisToolboxService extends AbstractRedisService<String, String> {
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private StatService     statService;

    public void downLoadStat(Integer appId, Integer diyId, String code) {
        Date date = new Date();
        if (appId != null) {
            String key = "tb_toolbox_dl_day_stat@" + appId;
            stat(key);

            downloadService.addDownloadCount("tb_toolbox", appId);
        }
        if (diyId != null) {
            String key = "tb_diybox_dl_day_stat@" + diyId;
            stat(key);

            downloadService.addDownloadCount("tb_diybox", diyId);
        }
        if (StringUtility.isNotEmpty(code)) {
            String day = DateUtility.format(date, "yyyyMMdd");
            String key = "tb_toolbox_day_stat@" + code + "@" + day;
            stat(key);

            statService.pvStat(code, 1, date);
        }
    }
    
//  @Scheduled(fixedRate = 5000)
    public void mysql2redis() {
        String pattern = "tb_toolbox_dl_day_stat@";
        Set<String> keys = redisTemplate.keys(pattern);
        
        
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