package com.pixshow.toolboxmgr.action;

import java.net.URLDecoder;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.service.CustomGridService;

@Controller
@Scope("prototype")
@Namespace("/service")
public class CustomGridAction extends BaseAction {
    private static final long serialVersionUID = 1L;

    @Autowired
    private CustomGridService customGridService;
    /////////////////////////////////////////////////////////////
    private String            code;
    private String            sql;
    /////////////////////////////////////////////////////////////
    private JSONArray         result           = new JSONArray();

    @Action(value = "customGridSql", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String JSONArray() throws Exception {
        result = JSONArray.fromObject(customGridService.customGridSql(code, StringUtility.isNotEmpty(sql) ? URLDecoder.decode(sql, "UTF-8") : sql));
        return SUCCESS;
    }

    public JSONArray getResult() {
        return result;
    }

    public void setResult(JSONArray result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
