package com.pixshow.custom.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class GridDataAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private GridDataService     gridDataService;
    @Autowired
    private CustomGridService   customGridService;
    //////////////////////////////////////////////
    private Map<String, Object> result           = new HashMap<String, Object>();

    private String              code;
    private String              order;

    @Action(value = "gridDataPage", results = { @Result(name = SUCCESS, location = "/custom/grid/gridData.jsp") })
    public String addDataToGrid() {
        result.put("gridTable", customGridService.findGridByCode(code));
        result.put("gridData", gridDataService.getData(code, order));
        return SUCCESS;
    }

    private String tableName;
    private String data;

    @Action(value = "addGridData", results = { @Result(name = SUCCESS, type = "json") })
    public String addGridData() {
        List<String> names = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        JSONArray arr = JSONArray.fromObject(data);
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = arr.getJSONObject(i);
            String name = json.optString("name");
            Object value = json.opt("value");
            names.add(name);
            values.add(value);
        }
        gridDataService.insert(tableName, names, values);
        return SUCCESS;
    }

    private int dataId;

    @Action(value = "delGridData", results = { @Result(name = SUCCESS, type = "json") })
    public String delGridData() {
        gridDataService.delete(tableName, dataId);
        return SUCCESS;
    }

    private String updateSql;

    @Action(value = "updateGridData", results = { @Result(name = SUCCESS, type = "json") })
    public String updateGridData() {
        
        gridDataService.update(tableName, updateSql, dataId);
        return SUCCESS;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

}
