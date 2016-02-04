package com.pixshow.toolboxmgr.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.service.StatCodeService;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class ToolboxStatAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private StatCodeService     statCodeService;
    ///////////////////////////////////////////////
    private String              catName;
    private String              catCode;
    private String              catCodeName;
    private String              statCatCodeIds;
    private int                 catId;
    private int                 codeId;
    ///////////////////////////////////////////////
    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "statCatList", results = { @Result(name = SUCCESS, location = "/stat/catList.jsp") })
    public String statCatList() {
        result.put("allCats", statCodeService.findAllCats());
        return SUCCESS;
    }

    @Action(value = "statCatCodeList", results = { @Result(name = SUCCESS, location = "/stat/catCodeList.jsp") })
    public String statCatCodeList() {
        result.put("cat", statCodeService.findCatById(catId));
        List<Map<String, Object>> codes = statCodeService.findCatCodes(catId);
        result.put("codes", codes);

        List<Map<String, Object>> codeNames = statCodeService.findAllDayStatCodes();
        JSONArray codeNameArr = new JSONArray();
        for (Map<String, Object> map : codeNames) {
            codeNameArr.add(map.get("code"));
        }
        result.put("codeNames", codeNameArr);
        if (codes.size() == 0) { return SUCCESS; }

        List<Object> codesList = new ArrayList<Object>();
        for (int i = 0; i < codes.size(); i++) {
            codesList.add((String) codes.get(i).get("code"));
        }
        List<Map<String, Object>> weekByCode = statCodeService.statWeekByCode(codesList);
        //天+code>Object
        Map<String, Integer> weekMap = new HashMap<String, Integer>();
        for (Map<String, Object> week : weekByCode) {
            int day = (Integer) week.get("day");
            String code = (String) week.get("code");
            int count = (Integer) week.get("count");
            weekMap.put(day + "_" + code, count);
        }
        result.put("weekMap", weekMap);

        List<Map<String, Object>> countByCode = statCodeService.statCountByCode(codesList);
        Map<String, Integer> cuntMap = new HashMap<String, Integer>();
        for (Map<String, Object> count : countByCode) {
            String code = (String) count.get("code");
            int total = count.get("count") == null ? 0 : Integer.parseInt(count.get("count").toString());
            cuntMap.put(code, total);
        }
        result.put("cuntMap", cuntMap);

        return SUCCESS;
    }

    @Action(value = "catCodeStats", results = { @Result(name = SUCCESS, location = "/stat/catCodeStats.jsp") })
    public String catCodeStats() {
        Map<String, Object> code = statCodeService.findCatCodeById(codeId);
        if (code != null) {
            result.put("code", code);
            result.put("stats", statCodeService.statByCode(code.get("code").toString()));
        }
        return SUCCESS;
    }

    @Action(value = "addStatCat", results = { @Result(name = SUCCESS, type = "redirectAction", location = "statCatList") })
    public String addStatCat() {
        if (StringUtility.isNotEmpty(catName)) {
            statCodeService.insertCat(catName);
        }
        return SUCCESS;
    }

    @Action(value = "addStatCatCode", results = { @Result(name = SUCCESS, type = "redirectAction", location = "statCatCodeList?catId=${catId}") })
    public String addStatCatCode() {
        if (StringUtility.isEmpty(catCode)) { return SUCCESS; }
        catCode = catCode.trim();
        if (!catCode.contains("%")) {
            statCodeService.insertCatCode(catId, catCodeName, catCode);
            return SUCCESS;
        }
        List<Map<String, Object>> codes = statCodeService.findCodesNotInCatIdAndLikeByCode(catId, catCode);
        List<Map<String, Object>> keys = statCodeService.findKeyValue();
        for (Map<String, Object> codeMap : codes) {
            String code = (String) codeMap.get("code");
            String codeName = code;
            for (Map<String, Object> keyMap : keys) {
                String name = (String) keyMap.get("name");
                String value = (String) keyMap.get("value");
                codeName = codeName.replace(name, value);
            }
            statCodeService.insertCatCode(catId, codeName, code);
        }
        return SUCCESS;
    }

    @Action(value = "deleteStatCat", results = { @Result(name = SUCCESS, type = "redirectAction", location = "statCatList") })
    public String deleteStatCat() {
        statCodeService.deleteCat(catId);
        return SUCCESS;
    }

    @Action(value = "deleteStatCatCode", results = { @Result(name = SUCCESS, type = "redirectAction", location = "statCatCodeList?catId=${catId}") })
    public String deleteStatCatCode() {
        statCodeService.deleteCatCode(codeId);
        return SUCCESS;
    }

    @Action(value = "deleteStatCatCodes", results = { @Result(name = SUCCESS, type = "redirectAction", location = "statCatCodeList?catId=${catId}") })
    public String deleteStatCatCodes() {
        statCodeService.deleteCatCodes(statCatCodeIds);
        return SUCCESS;
    }

    @Action(value = "updateStatCatCode", results = { @Result(name = SUCCESS, type = "json") })
    public String updateStatCatCode() {
        statCodeService.updateCatCodeName(codeId, catCodeName);
        return SUCCESS;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getCatCodeName() {
        return catCodeName;
    }

    public void setCatCodeName(String catCodeName) {
        this.catCodeName = catCodeName;
    }

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public String getStatCatCodeIds() {
        return statCatCodeIds;
    }

    public void setStatCatCodeIds(String statCatCodeIds) {
        this.statCatCodeIds = statCatCodeIds;
    }

}
