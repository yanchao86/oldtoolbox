package com.pixshow.toolboxmgr.action;

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
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.StatWarningBean;
import com.pixshow.toolboxmgr.service.StatCodeService;
import com.pixshow.toolboxmgr.service.StatWarningService;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class StatWarningAction extends BaseAction {
    private static final long  serialVersionUID = 1L;

    @Autowired
    private StatWarningService statWarningService;
    @Autowired
    private StatCodeService    statCodeService;

    private String             code;
    private String             emails;
    private JSONArray          result           = new JSONArray();

    @Action(value = "saveStatWaring", results = { @Result(name = SUCCESS, location = "/stat/warning/config.jsp") })
    public String saveStatWaring() {
        if (StringUtility.isNotEmpty(code)) {
            StatWarningBean bean = statWarningService.findByCode(code);
            if (bean == null) {
                bean = new StatWarningBean();
                bean.setCode(code);
                bean.setEmail(emails);
                statWarningService.save(bean);
            } else {
                bean.setEmail(bean.getEmail() + "\n" + emails);
                statWarningService.update(bean);
            }
        }
        return SUCCESS;
    }

    @Action(value = "updaeStatWaring", results = { @Result(name = SUCCESS, location = "/stat/warning/config.jsp") })
    public String updaeStatWaring() {
        StatWarningBean bean = statWarningService.findByCode(code);
        if (bean != null) {
            bean.setEmail(emails);
            statWarningService.update(bean);
        }
        return SUCCESS;
    }

    @Action(value = "deleteStatWaring", results = { @Result(name = SUCCESS, location = "/stat/warning/config.jsp") })
    public String deleteStatWaring() {
        statWarningService.delete(code);
        return SUCCESS;
    }


    public StatCodeService getStatCodeService() {
        return statCodeService;
    }

    public void setStatCodeService(StatCodeService statCodeService) {
        this.statCodeService = statCodeService;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public void setResult(JSONArray result) {
        this.result = result;
    }

    public JSONArray getResult() {
        List<StatWarningBean> statWarnings = statWarningService.findAll();
        List<Map<String, Object>> codeNames = statCodeService.findKeyValue();
        for (StatWarningBean bean : statWarnings) {
            String codeName = bean.getCode();
            for (Map<String, Object> keyMap : codeNames) {
                String name = (String) keyMap.get("name");
                String value = (String) keyMap.get("value");
                codeName = codeName.replace(name, value);
            }
            JSONObject data = JSONObject.fromObject(bean);
            data.put("codeName", codeName);
            result.add(data);
        }
        return result;
    }

   

}
