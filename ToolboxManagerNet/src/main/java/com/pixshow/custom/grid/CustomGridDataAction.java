package com.pixshow.custom.grid;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class CustomGridDataAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private GridDataService   gridDataService;
    
    private String            grid;
    private String            filters;

    private JSONArray        result           = new JSONArray();

    @Action(value = "customGridData", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String customGridData() {
        filters = StringUtility.isEmpty(filters) ? "*" : filters;
        result = JSONArray.fromObject(gridDataService.customGridData(grid, filters));
        
        return SUCCESS;
    }

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public JSONArray getResult() {
        return result;
    }

    public void setResult(JSONArray result) {
        this.result = result;
    }

}
