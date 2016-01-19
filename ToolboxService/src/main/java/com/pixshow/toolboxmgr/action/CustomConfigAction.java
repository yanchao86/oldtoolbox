package com.pixshow.toolboxmgr.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.redis.RedisToolboxService;
import com.pixshow.toolboxmgr.dao.CustomGridDao;
import com.pixshow.toolboxmgr.service.CustomGridService;
import com.pixshow.toolboxmgr.service.CustomService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
@Namespace("/service")
public class CustomConfigAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    private final static Log  log              = LogFactory.getLog(CustomConfigAction.class);

    @Autowired
    private CustomService       customService;
    @Autowired
    private CustomGridService   customGridService;
    @Autowired
    private RedisToolboxService redisToolboxService;

    /////////////////////////////////////////////////////////////
    private String     code;
    /////////////////////////////////////////////////////////////
    private JSONObject result = new JSONObject();

    @Action(value = "customConfig", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String customConfig() throws Exception {
        String rKey = "customConfig@" + code;
        String res = redisToolboxService.get(rKey);
        boolean fromRedis = false;
        if (StringUtility.isNotEmpty(res)) {
            try {
                result = JSONObject.fromObject(res);
                fromRedis = true;
            } catch (Exception e) {
                log.error("customConfig redis get " + rKey + " error");
            }
        }

        if (fromRedis) {
            return SUCCESS;
        }

        String[] codes = StringUtility.split(code, ",");
        if (codes != null && codes.length > 0) {
            List<String> values = customService.getValue(codes);
            List<String> configs = customService.getConfig(codes);
            if (values != null && values.size() > 0) {
                for (int i = 0; i < codes.length; i++) {
                    String config = configs.get(i);
                    getGridMap(JSONObject.fromObject(config));

                    JSONObject value = JSONObject.fromObject(values.get(i));
                    getData(value);

                    result.put(codes[i], value.toString());
                }
            }
        }
        try {
            redisToolboxService.set(rKey, result.toString(), 1000 * 3);
        } catch (Exception e) {
            log.error("customConfig redis set " + rKey + " error");
        }

        return SUCCESS;
    }

    /**
     * 自定义配置中关联的表格
     */
    private static Map<String, String> gridMap = new HashMap<String, String>();

    private void getGridMap(Object data) {
        if (data instanceof JSONObject) {
            JSONObject config = (JSONObject) data;
            if (config.isEmpty() || config.isNullObject()) {
                return;
            }
            Iterator<String> it = config.keys();
            while (it.hasNext()) {
                String key = it.next();
                if (config.optInt("type", -1) == 6) {
                    gridMap.put(config.optString("key"), config.optString("grid"));

                    eachGrid(config.optString("grid"));
                    break;
                }
                getGridMap(config.get(key));
            }
        } else if (data instanceof JSONArray) {
            JSONArray configs = (JSONArray) data;
            for (int i = 0; i < configs.size(); i++) {
                getGridMap(configs.get(i));
            }
        }
    }

    /**
     * 自定义表格中关联的表格
     */
    private static Map<String, List<String>> eachGridMap = new HashMap<String, List<String>>();

    private void eachGrid(String gridTble) {
        String definition = customGridService.gridConfig(gridTble.replace(CustomGridDao.grid_table_prefix, ""));
        if (StringUtility.isEmpty(definition)) {
            return;
        }

        List<String> tables = new ArrayList<String>();
        JSONObject json = JSONObject.fromObject(definition);
        JSONArray properties = json.getJSONArray("properties");
        for (int i = 0; i < properties.size(); i++) {
            JSONObject propertie = properties.getJSONObject(i);
            if (propertie.optInt("type") != 4) {
                continue;
            }
            tables.add(propertie.optString("extKey"));
        }
        if (tables.size() > 0) {
            eachGridMap.put(gridTble, tables);
        }
    }

    private void getData(Object data) {
        if (data instanceof JSONObject) {
            JSONObject json = (JSONObject) data;
            if (json.isEmpty() || json.isNullObject()) {
                return;
            }
            Iterator<String> it = json.keys();
            while (it.hasNext()) {
                String key = it.next();
                if (gridMap.containsKey(key)) {
                    List<Object> ids = new ArrayList<Object>();
                    if (json.get(key) instanceof String) {
                        ids.add(json.get(key).toString());
                        //} else if (json.get(key) instanceof ArrayList) {
                    } else {
                        ids = Arrays.asList(json.getJSONArray(key).toArray());
                    }
                    List<Map<String, Object>> value = customGridService.getData(gridMap.get(key), ids);
                    List<String> tables = eachGridMap.get(gridMap.get(key));
                    for (Map<String, Object> map : value) {
                        Iterator<String> keys = map.keySet().iterator();
                        while (keys.hasNext()) {
                            String k = keys.next();
                            Object v = map.get(k);
                            if (v instanceof Date) {
                                map.put(k, DateUtility.format((Date) v));
                            }
                            if (tables != null && tables.contains(k)) {
                                List<Object> ids_ = new ArrayList<Object>();
                                ids_.add(v);
                                map.put(k, customGridService.getData(CustomGridDao.grid_table_prefix + k, ids_));
                            }
                        }
                    }

                    json.put(key, value);
                }
                getData(json.get(key));
            }
        } else if (data instanceof JSONArray) {
            JSONArray json = (JSONArray) data;
            for (int i = 0; i < json.size(); i++) {
                getData(json.get(i));
            }
        }

    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
