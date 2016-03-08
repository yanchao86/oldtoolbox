package com.pixshow.toolboxmgr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.dao.CustomGridDao;

@Service
public class CustomGridService {

    @Autowired
    private CustomGridDao customGridDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> customGridSql(String gridTble, String sql) {
        eachGrid(gridTble);

        StringBuilder query = new StringBuilder("select * from " + CustomGridDao.grid_table_prefix + gridTble + " as " + gridTble);
        StringBuilder where = new StringBuilder();
        if (StringUtility.isNotEmpty(sql)) {
            String[] parms = sql.split("and");
            for (int i = 0; i < parms.length; i++) {
                String parm = parms[i];
                if (!parm.contains(".")) {
                    if (where.length() > 0) {
                        where.append(" and ");
                    }
                    where.append(parm);
                } else {
                    String table = parm.split("\\.")[0].trim();
                    String parmVal = parm.replace(table + ".", "").trim();
                    parmVal = parmVal.replace("=", "='") + "'";
                    query.append(" join " + CustomGridDao.grid_table_prefix + table + " as " + table + "_" + i + 
                            " on (" + gridTble + "." + table + "=" + table + "_" + i + ".id and " + table + "_" + i + "." + parmVal + ")");
                }
            }
        }
        List<Map<String, Object>> datas = customGridDao.customGridSql(query.toString());
        reformData(gridTble, datas);
        return datas;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> getData(String gridTble, List<Object> ids) {
        List<Map<String, Object>> datas = customGridDao.getData(gridTble, ids);
        reformData(gridTble, datas);
        return datas;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String gridConfig(String gridTble) {
        return customGridDao.gridConfig(gridTble);
    }

    /**
     * 数据重组
     */
    private void reformData(String gridTble, List<Map<String, Object>> datas) {
        eachGrid(gridTble);
        eachData(gridTble, datas);
    }

    /**
     * 存放自定义表格中关联的表格
     */
    private static Map<String, Map<String, String>> eachGridMap = new HashMap<String, Map<String, String>>();

    private void eachGrid(String gridTble) {
        String definition = gridConfig(gridTble.replace(CustomGridDao.grid_table_prefix, ""));
        if (StringUtility.isEmpty(definition)) { return; }

        Map<String, String> column_table = new HashMap<String, String>();
        JSONObject json = JSONObject.fromObject(definition);
        JSONArray properties = json.getJSONArray("properties");
        for (int i = 0; i < properties.size(); i++) {
            JSONObject propertie = properties.getJSONObject(i);
            if (propertie.optInt("type") != 4) {
                continue;
            }
            String column = propertie.optString("key");
            String table = propertie.optString("extKey");
            column_table.put(column, table);
        }
        if (column_table.size() > 0) {
            eachGridMap.put(gridTble, column_table);
        }
        for (Map.Entry<String, String> map : column_table.entrySet()) {
            eachGrid(map.getValue());
        }

    }

    /**
     * 递归取数据
     * 
     * @param gridTble
     * @param datas
     */
    private void eachData(String gridTble, List<Map<String, Object>> datas) {
        Map<String, String> column_table = eachGridMap.get(gridTble);
        if (column_table == null || column_table.size() == 0) { return; }
        for (Map<String, Object> data : datas) {
            Iterator<String> it = data.keySet().iterator();
            while (it.hasNext()) {
                String column = it.next();
                Object value = data.get(column);
                if (column_table.containsKey(column)) {
                    String table = column_table.get(column);

                    List<Object> ids = new ArrayList<Object>();
                    ids.add(value);
                    List<Map<String, Object>> list = customGridDao.getData(CustomGridDao.grid_table_prefix + table, ids);
                    data.put(column, list);

                    eachData(table, list);
                }
            }
        }

    }
}
