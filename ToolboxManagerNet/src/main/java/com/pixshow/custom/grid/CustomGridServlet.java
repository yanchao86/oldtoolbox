package com.pixshow.custom.grid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import com.pixshow.framework.utils.AppContextUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

public class CustomGridServlet extends HttpServlet {
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
        setValues(params);
        String tableName = (String) params.get("tableName");
        response.sendRedirect("./manager/gridDataPage.do?code="+tableName.replace(GridDataDao.TABLE_PREFIX, ""));
    }

    private void setValues(Map<String, Object> params) {
        String tableName = (String) params.get("tableName");
        String id = (String) params.get("id");

        Map<String, Object> fieldMap = new HashMap<String, Object>();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String field = it.next();
            Object value = params.get(field);
            if ("tableName".equals(field) || "id".equals(field) || field.indexOf("@fileName") > 0) {
                continue;
            }
            if (value instanceof File) {
                File file = (File) value;
                String fileName = params.get(field + "@fileName").toString();
                String endName = fileName.substring(fileName.lastIndexOf("."));
                fileName = UUID.randomUUID() + endName;
                fieldMap.put(field, ImageStorageTootl.upload(fileName, file));
            } else if (value instanceof Integer) {
                fieldMap.put(field, value != null ? (Integer) value : 0);
            } else if (value instanceof String) {
                fieldMap.put(field, (String) value);
            } else if (value instanceof Long) {
                fieldMap.put(field, (Long) value);
            } else if (value instanceof Double) {
                fieldMap.put(field, (Double) value);
            } else if (value instanceof Float) {
                fieldMap.put(field, (Float) value);
            }
        }
        List<String> fields = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        StringBuilder update = new StringBuilder();
        Iterator<String> map = fieldMap.keySet().iterator();
        while (map.hasNext()) {
            String field = map.next();
            Object value = fieldMap.get(field);
            fields.add(field);
            values.add(value);
            if (update.length() > 0) {
                update.append(",");
            }
            update.append(field + "='" + value + "'");
        }

        GridDataService gridDataService = AppContextUtility.getContext().getBean(GridDataService.class);

        if (StringUtility.isEmpty(id)) {
            gridDataService.insert(tableName, fields, values);
        } else {
            gridDataService.update(tableName, update.toString(), Integer.parseInt(id));
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
