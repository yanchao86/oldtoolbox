package com.pixshow.custom.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import com.pixshow.framework.utils.AppContextUtility;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

public class CustomConfigServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> params = null;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            try {
                params = getParams4Mult(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            params = getParams(request);
        }
        CustomConfigService customConfigService = AppContextUtility.getContext().getBean(CustomConfigService.class);
        String custom_config_code = params.get("custom_config_code").toString();
        JSONObject def = JSONObject.fromObject(customConfigService.findDefByCode(custom_config_code).get("definition").toString());//数据库查询
        Map<String, Object> dataMap = customConfigService.findValsByDefCode(custom_config_code);
        JSONObject data = dataMap != null ? JSONObject.fromObject(dataMap.get("value").toString()) : null;//数据库查询出来

        if (data == null) {
            data = new JSONObject();//没有记录，创建一个
        }

        setValues(null, def, data, params);
        if (dataMap == null) {
            customConfigService.saveVal(custom_config_code, data.toString());
        } else {
            customConfigService.updateVal(custom_config_code, data.toString());
        }
        response.sendRedirect("./manager/customInfo.do?code=" + custom_config_code);
    }

    private void setValues(String parentCode, JSONObject def, JSONObject data, Map<String, Object> params) {
        if (def.containsKey("properties")) {
            JSONArray properties = def.optJSONArray("properties");
            for (int i = 0; i < properties.size(); i++) {
                JSONObject propertyDef = properties.getJSONObject(i);
                String propertyKey = propertyDef.getString("key");
                int type = propertyDef.getInt("type");
                Object value = params.get(parentCode == null ? propertyKey : (parentCode + "." + propertyKey));

                if (4 == type) { //文件
                    if (value != null) {
                        File file = (File) value;
                        String fileName = params.get(parentCode == null ? propertyKey : (parentCode + "." + propertyKey) + "@fileName").toString();
                        String endName = fileName.substring(fileName.lastIndexOf("."));
                        fileName = UUID.randomUUID() + endName;
                        ImageStorageTootl.upload(fileName, file);
                        data.put(propertyKey, ImageStorageTootl.getUrl(fileName));
                    }
                } else {
                    data.put(propertyKey, value);
                }
            }
        }
        if (def.containsKey("groups")) {
            JSONArray groups = def.optJSONArray("groups");
            for (int i = 0; i < groups.size(); i++) {
                JSONObject groupDef = groups.getJSONObject(i);
                String groupKey = groupDef.getString("key");
                JSONObject groupData = data.optJSONObject(groupKey);//取出旧数据
                if (groupData == null) {
                    groupData = new JSONObject();
                }
                //递归
                setValues(parentCode == null ? groupKey : (parentCode + "." + groupKey), groupDef, groupData, params);
                data.put(groupKey, groupData);
            }
        }
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        return params;
    }

    private Map<String, Object> getParams4Mult(HttpServletRequest request) throws Exception {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(servletContext);

        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        factory.setSizeThreshold(1024 * 1);
        factory.setFileCleaningTracker(fileCleaningTracker);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        Map<String, Object> params = new HashMap<String, Object>();
        // Parse the request
        List<?> items = upload.parseRequest(request);
        for (Object obj : items) {
            DiskFileItem item = (DiskFileItem) obj;
            if (item.isFormField()) {
                if (params.containsKey(item.getFieldName())) {
                    List<Object> valueList = new ArrayList<Object>();
                    Object oldValue = params.get(item.getFieldName());
                    if (oldValue instanceof List) {
                        valueList.addAll((List<?>) oldValue);
                    } else {
                        valueList.add(oldValue);
                    }
                    valueList.add(item.getString("UTF-8"));
                    params.put(item.getFieldName(), valueList);
                } else {
                    params.put(item.getFieldName(), item.getString("UTF-8"));
                }
            } else {
                if (item.getSize() > 0) {
                    params.put(item.getFieldName(), item.getStoreLocation());
                    params.put(item.getFieldName() + "@fileName", item.getName());
                }
            }
        }
        return params;
    }
}
