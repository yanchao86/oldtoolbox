package com.pixshow.toolboxmgr.action;

import java.util.Date;
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
import com.pixshow.toolboxmgr.bean.GameRecommendBean;
import com.pixshow.toolboxmgr.service.GameRecommendService;
import com.pixshow.toolboxmgr.service.ToolboxService;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class GameRecommendAction extends BaseAction {
    private static final long    serialVersionUID = 1L;
    @Autowired
    private GameRecommendService gameRecommendService;
    @Autowired
    private ToolboxService       toolboxService;

    private int                  id;
    private int                  useType;
    private List<String>         gameRecommend;

    private Map<String, Object>  result           = new HashMap<String, Object>();

    @Action(value = "gameRecommend", results = { @Result(name = SUCCESS, location = "/game/recommend.jsp") })
    public String recommend() {
        result.put("toolboxs", toolboxService.searchTool());
        result.put("gameRecommends", gameRecommendService.findsAll());
        return SUCCESS;
    }

    @Action(value = "saveRecommend", results = { @Result(name = SUCCESS, type = "redirectAction", location = "gameRecommend") })
    public String saveRecommend() {
        GameRecommendBean bean = new GameRecommendBean();
        bean.setContent(JSONArray.fromObject(gameRecommend).toString());
        bean.setCreateDate(new Date());
        gameRecommendService.save(bean);
        return SUCCESS;
    }

    @Action(value = "updatRecommendUseType", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String updatRecommendUseType() {
        result.put("result", gameRecommendService.updateUseType(id));
        return SUCCESS;
    }

    @Action(value = "deleteRecommend", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String deleteRecommend() {
        gameRecommendService.delete(id);
        return SUCCESS;
    }

    private String sortIds;

    @Action(value = "recommendSort", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String recommendSort() {
        gameRecommendService.updateContent(id, "[" + sortIds + "]");
        return SUCCESS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getGameRecommend() {
        return gameRecommend;
    }

    public void setGameRecommend(List<String> gameRecommend) {
        this.gameRecommend = gameRecommend;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getSortIds() {
        return sortIds;
    }

    public void setSortIds(String sortIds) {
        this.sortIds = sortIds;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

}
