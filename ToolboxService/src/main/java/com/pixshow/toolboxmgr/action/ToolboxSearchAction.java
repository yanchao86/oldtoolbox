package com.pixshow.toolboxmgr.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.redis.RedisToolboxService;
import com.pixshow.toolboxmgr.service.ToolboxService;

@Controller
@Scope("prototype")
@Namespace("/service")
public class ToolboxSearchAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(ToolboxSearchAction.class);

	@Autowired
	private ToolboxService toolboxService;
	@Autowired
	private RedisToolboxService redisToolboxService;

	private int index = -1;
	private int items = 9999;

	private JSONArray result;

	@Action(value = "toolSearch", results = { @Result(name = SUCCESS, type = "json", params = {
			"root", "result" }) })
	public String execute() throws Exception {
		boolean fromredis = false;
		String rKey = "toolSearch@" + index + "_" + items;
		String str = redisToolboxService.get(rKey);
		if (str != null) {
			try {
				result = JSONArray.fromObject(str);
				return SUCCESS;
			} catch (Exception e) {
				log.info(rKey + " to jsonarr error");
			}
		}
		result = toolboxService.searchToolsUpdate2Redis();
		return SUCCESS;
	}

	public JSONArray getResult() {
		return result;
	}

	public void setResult(JSONArray result) {
		this.result = result;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

}
