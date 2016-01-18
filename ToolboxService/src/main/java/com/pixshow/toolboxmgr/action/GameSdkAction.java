package com.pixshow.toolboxmgr.action;

import java.util.ArrayList;
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

import com.pixshow.constant.GameSuspend;
import com.pixshow.framework.config.Config;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.ListUtiltiy;
import com.pixshow.toolboxmgr.bean.GameGlitterBean;
import com.pixshow.toolboxmgr.bean.GameOutGlitterBean;
import com.pixshow.toolboxmgr.bean.GameRecommendBean;
import com.pixshow.toolboxmgr.bean.GameSuspendBean;
import com.pixshow.toolboxmgr.bean.ToolboxBean;
import com.pixshow.toolboxmgr.service.GameGlitterService;
import com.pixshow.toolboxmgr.service.GameOutGlitterService;
import com.pixshow.toolboxmgr.service.GameRecommendService;
import com.pixshow.toolboxmgr.service.GameSuspendService;
import com.pixshow.toolboxmgr.service.ToolboxService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/service/game")
public class GameSdkAction extends BaseAction {
    private static final long     serialVersionUID = 1L;
    @Autowired
    private GameGlitterService    gameGlitterService;
    @Autowired
    private GameOutGlitterService gameOutGlitterService;
    @Autowired
    private GameSuspendService    gameSuspendService;
    @Autowired
    private GameRecommendService  gameRecommendService;
    @Autowired
    private ToolboxService        toolboxService;

    private JSONObject            result           = new JSONObject();

    @Action(value = "glitter", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String glitter() {
        List<GameGlitterBean> list = gameGlitterService.findsUse();
        JSONArray arr = new JSONArray();
        for (GameGlitterBean bean : list) {
            JSONObject json = JSONObject.fromObject(bean);
            json.put("createDate", DateUtility.parseUnixTime(bean.getCreateDate()));
            json.put("startTime", DateUtility.parseUnixTime(bean.getStartTime()));
            json.put("endTime", DateUtility.parseUnixTime(bean.getEndTime()));
            arr.add(json);
        }
        result.put("glitter", arr);
        return SUCCESS;
    }

    @Action(value = "outGlitter", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String outGlitter() {
        List<GameOutGlitterBean> list = gameOutGlitterService.findsUse();
        JSONArray arr = new JSONArray();
        for (GameOutGlitterBean bean : list) {
            JSONObject json = JSONObject.fromObject(bean);
            json.put("createDate", DateUtility.parseUnixTime(bean.getCreateDate()));
            json.put("startTime", DateUtility.parseUnixTime(bean.getStartTime()));
            json.put("endTime", DateUtility.parseUnixTime(bean.getEndTime()));
            arr.add(json);
        }
        result.put("glitter", arr);
        return SUCCESS;
    }

    @Action(value = "suspend", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String suspend() {
        GameSuspendBean bean = gameSuspendService.findUse();
        result.put("suspendType", bean.getSuspendType());
        if (bean.getSuspendType() == GameSuspend.suspendType.isPic) {
            result.put("content", JSONObject.fromObject(bean.getContent()));
        } else {
            JSONArray arr = JSONArray.fromObject(bean.getContent());
            List<Integer> ids = new ArrayList<Integer>();
            for (int i = 0; i < arr.size(); i++) {
                ids.add(arr.getInt(i));
            }
            List<ToolboxBean> list = toolboxService.searchToolByIds(ids);
            Map<Integer, ToolboxBean> map = ListUtiltiy.groupToObject(list, "id");
            JSONArray data = new JSONArray();
            for (int i = 0; i < arr.size(); i++) {
                int id = arr.getInt(i);
                ToolboxBean box = map.get(id);
                JSONObject json = new JSONObject();
                json.put("id", box.getId());
                json.put("name", box.getName());
                json.put("sortIndex", box.getSortIndex());
                json.put("icon", ImageStorageTootl.getUrl(box.getIcon()));
                json.put("downloadUrl", Config.getInstance().getString("toolbox.download.baseUrl") + "service/download.do?appId=" + box.getId());
                json.put("downloadAuto", box.getDownloadAuto() == 0 ? false : true);
                json.put("downloadCount", box.getDownloadCount());
                json.put("detailUrl", box.getDetailUrl());
                json.put("detailOpen", box.getDetailOpen() == 0 ? false : true);
                json.put("rate", box.getRate());
                json.put("extInfo1", box.getExtInfo1());
                json.put("extInfo2", box.getExtInfo2());
                json.put("extInfo3", box.getExtInfo3());
                json.put("packageName", box.getPackageName());
                json.put("versionCode", box.getVersionCode());
                json.put("createDate", DateUtility.format(box.getCreateDate()));
                json.put("updateDate", DateUtility.format(box.getUpdateDate()));
                data.add(json);
            }
            result.put("content", data);
        }
        return SUCCESS;
    }

    @Action(value = "allPackageName", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String allPackageName() {
        List<Map<String, Object>> allPackageName = toolboxService.allPackageName();
        for (Map<String, Object> map : allPackageName) {
            String icon = (String) map.get("icon");
            icon = ImageStorageTootl.getUrl(icon);
            map.put("icon", icon);
        }
        result.put("size", allPackageName.size());
        result.put("content", allPackageName);
        return SUCCESS;
    }

    @Action(value = "recommend", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String recommend() {
        GameRecommendBean bean = gameRecommendService.findUse();
        JSONArray arr = JSONArray.fromObject(bean.getContent());
        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < arr.size(); i++) {
            ids.add(arr.getInt(i));
        }
        List<ToolboxBean> list = toolboxService.searchToolByIds(ids);
        Map<Integer, ToolboxBean> map = ListUtiltiy.groupToObject(list, "id");
        JSONArray data = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            int id = arr.getInt(i);
            ToolboxBean box = map.get(id);
            JSONObject json = new JSONObject();
            json.put("id", box.getId());
            json.put("name", box.getName());
            json.put("sortIndex", box.getSortIndex());
            json.put("icon", ImageStorageTootl.getUrl(box.getIcon()));
            json.put("downloadUrl", Config.getInstance().getString("toolbox.download.baseUrl") + "service/download.do?appId=" + box.getId());
            json.put("downloadAuto", box.getDownloadAuto() == 0 ? false : true);
            json.put("downloadCount", box.getDownloadCount());
            json.put("detailUrl", box.getDetailUrl());
            json.put("detailOpen", box.getDetailOpen() == 0 ? false : true);
            json.put("rate", box.getRate());
            json.put("extInfo1", box.getExtInfo1());
            json.put("extInfo2", box.getExtInfo2());
            json.put("extInfo3", box.getExtInfo3());
            json.put("packageName", box.getPackageName());
            json.put("versionCode", box.getVersionCode());
            json.put("createDate", DateUtility.format(box.getCreateDate()));
            json.put("updateDate", DateUtility.format(box.getUpdateDate()));
            data.add(json);
        }
        result.put("content", data);
        return SUCCESS;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

}
