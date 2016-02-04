package com.pixshow.toolboxmgr.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.python.antlr.PythonParser.file_input_return;
import org.python.antlr.PythonParser.return_stmt_return;
import org.python.antlr.PythonParser.varargslist_return;
import org.python.modules.synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.util.WebUtils;

import redis.clients.jedis.Jedis;

import com.kenai.jffi.Function;
import com.pixshow.framework.exception.api.LogicException;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.ToolboxBean;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.service.ToolboxService;
import com.pixshow.toolboxmgr.tools.ImageCompressUtil;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;
import com.pixshow.toolboxmgr.tools.PngCompressUtil;
import com.pixshow.toolboxmgr.tools.RedisFactory;
import com.pixshow.toolboxmgr.tools.RedisUtil;
import com.sun.istack.internal.FinalArrayList;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class ToolboxUpdateAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ToolboxService toolboxService;
	@Autowired
	private PropertiesService propertiesService;

	private Integer id;
	private String name;
	private File icon;
	private String iconFileName;
	private int canMove;
	private File moveIconUrl;
	private String moveIconUrlFileName;
	private String downloadUrl;
	private String downloadTime;
	private int downloadAuto;
	private int downloadDoneAlert;
	private String detailUrl;
	private int detailOpen;
	private float rate;
	private String marketPackage;
	private String packageName;
	private String versionCode;
	private String extInfo1;
	private int extInfo1_extField1;
	private int downloadRecommend;
	private File apkPic0;
	private String apkPic0FileName;
	private File apkPic1;
	private String apkPic1FileName;
	private File apkPic2;
	private String apkPic2FileName;
	private File apkPic3;
	private String apkPic3FileName;
	private File apkPic4;
	private String apkPic4FileName;
	private File apkPic5;
	private String apkPic5FileName;
	private String apkSize;
	private String apkDescription;

	private void uploadToolboxIcon(final String iconFileName, File icon) {
		final File tempFile = new File(icon.getAbsoluteFile().getParent() + File.separator + "temp_" + iconFileName);
//		final File tinyFile = new File(icon.getAbsoluteFile().getParent() + File.separator + "tiny_" + iconFileName);
//		if (
				ImageCompressUtil.compressImage(iconFileName.substring(iconFileName.lastIndexOf(".")),icon, tempFile, 96, true);
//				) {
			ImageStorageTootl.upload(iconFileName, tempFile);
//		} else {
//			ImageStorageTootl.upload(iconFileName, icon);
//		}
//		final StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < 5; i++) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					while (sb.length() <= 0) {
//						ImageCompressUtil.tinyPngWhile(toPng, tinyFile, "https://api.tinify.com/shrink", "TlrMtUgnk8PqaWHQd8EN6pB4a-ByUEcy", sb);
//					}
//					synchronized (ToolboxUpdateAction.this) {
//						if (sb.charAt(0) != '2') {
//							ImageStorageTootl.upload(iconFileName, tinyFile);
//							tempFile.delete();
//							tinyFile.delete();
//							sb.setCharAt(0, '2');
//						}
//					}
//				}
//			}).start();
//		}
	}

	@Action(value = "toolInsert", results = { @Result(name = SUCCESS, type = "redirectAction", location = "toolSearch") })
	public String toolInsert() {
		if(!checkDownloadUrl(downloadUrl)){
			throw new LogicException(2, "下载地址无效,请检查确认!");
		}
		iconFileName = UUID.randomUUID() + iconFileName.substring(iconFileName.lastIndexOf("."));
		// ServletContext sc =
		// ContextLoader.getCurrentWebApplicationContext().getServletContext();
		// try {
		// String realPath = WebUtils.getRealPath(sc, "temp");
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		uploadToolboxIcon(iconFileName, icon);

		ToolboxBean bean = new ToolboxBean();
		bean.setName(name);
		bean.setSortIndex(0);
		bean.setIcon(iconFileName);
		bean.setDownloadUrl(downloadUrl);
		bean.setDownloadAuto(downloadAuto);
		bean.setDownloadCount(0);
		bean.setDetailUrl(detailUrl);
		bean.setDetailOpen(detailOpen);
		bean.setRate(rate);
		bean.setPackageName(packageName);
		bean.setVersionCode(versionCode);
		bean.setCreateDate(new Date());
		bean.setUpdateDate(new Date());
		bean.setExtInfo1(extInfo1);
		bean.setSortIndex(toolboxService.maxSortIndex() + 1);

		JSONObject extInfo3 = new JSONObject();
		extInfo3.put("extInfo1_extField1", extInfo1_extField1);
		extInfo3.put("marketPackage", StringUtility.isEmpty(marketPackage) ? null : marketPackage);
		bean.setExtInfo3(extInfo3.toString());

		toolboxService.insertTool(bean);

		return SUCCESS;
	}
	private boolean checkDownloadUrl(String downloadUrl){
		if(StringUtils.isEmpty(downloadUrl)){
			return false;
		}
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(downloadUrl);
			response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(statusCode==200){
				return true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	private void delApkFromRedis(String apkUrl){
		apkUrl = apkUrl.replaceAll("^.*/(.*?[.]apk)$", "$1");
		String config = null;
        String redisConfig = null;
        JSONArray redisArr = null;
        JSONArray arr = null;
		 try {
	            config = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_apk_upload.properties").getFile()), "UTF-8");
	            redisConfig = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_apk_upload_redis.properties").getFile()), "UTF-8");
	            redisArr = JSONArray.fromObject(redisConfig);
	            arr = JSONArray.fromObject(config);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        if(redisArr!=null){
	            for (int i = 0; i < redisArr.size(); i++) {
	                JSONObject json = redisArr.getJSONObject(i);
	                String host = json.getString("host");
	                int port = json.getInt("port");
	                RedisFactory redisFactory = new RedisFactory(host, port);
	        		Jedis jedis = redisFactory.getJedis();
	                try {
	                	jedis.del(apkUrl.getBytes());
	                	jedis.del(apkUrl+"_size");
	        		} catch (Exception e1) {
	        			e1.printStackTrace();
	        		}finally{
	        			RedisUtil.returnResource(jedis);
	        		}
	            }
	        }
	}
	@Action(value = "toolUpdate", results = { @Result(name = SUCCESS, type = "redirectAction", location = "toolSearch") })
	public String toolUpdate() {
		if(!checkDownloadUrl(downloadUrl)){
			throw new LogicException(2, "下载地址无效,请检查确认!");
		}
		ToolboxBean bean = toolboxService.searchByIDTool(id);
		String oldDownloadUrl = bean.getDownloadUrl();
		bean.setName(name);
		bean.setRate(rate);
		bean.setDownloadUrl(downloadUrl);
		bean.setDownloadAuto(downloadAuto);
		bean.setDetailUrl(detailUrl);
		bean.setDetailOpen(detailOpen);
		bean.setPackageName(packageName);
		bean.setVersionCode(versionCode);
		bean.setUpdateDate(new Date());
		bean.setExtInfo1(extInfo1);
		if (icon != null) {
//			if (StringUtility.isEmpty(bean.getIcon())) {
				bean.setIcon(UUID.randomUUID() + iconFileName.substring(iconFileName.lastIndexOf(".")));
//			}
			uploadToolboxIcon(bean.getIcon(), icon);
			// ImageStorageTootl.upload(bean.getIcon(), icon);
		}
//		bean.setIcon(iconFileName);
		if (moveIconUrl != null && canMove == 1) {
			JSONObject json = StringUtility.isEmpty(bean.getExtInfo2()) ? new JSONObject() : JSONObject.fromObject(bean.getExtInfo2());
			json.put("state", canMove);
			String name = UUID.randomUUID() + moveIconUrlFileName.substring(moveIconUrlFileName.lastIndexOf("."));
			json.put("icon", ImageStorageTootl.upload(name, moveIconUrl));
			bean.setExtInfo2(json.toString());
		}

		if (canMove == 0) {
			JSONObject moveJson = new JSONObject();
			moveJson.put("state", canMove);
			bean.setExtInfo2(moveJson.toString());
		}

		JSONObject extInfo3 = new JSONObject();
		extInfo3.put("extInfo1_extField1", extInfo1_extField1);
		if (StringUtility.isNotEmpty(marketPackage)) {
			extInfo3.put("marketPackage", marketPackage);
		}
		extInfo3.put("downloadDoneAlert", downloadDoneAlert);
		extInfo3.put("downloadTime", downloadTime);
		extInfo3.put("downloadRecommend", downloadRecommend);

		// apk start
		JSONArray apkPic = new JSONArray();
		if (apkPic0 != null) {
			String name = UUID.randomUUID() + apkPic0FileName.substring(apkPic0FileName.lastIndexOf("."));
			apkPic.add(ImageStorageTootl.upload(name, apkPic0));
		}
		if (apkPic1 != null) {
			String name = UUID.randomUUID() + apkPic1FileName.substring(apkPic1FileName.lastIndexOf("."));
			apkPic.add(ImageStorageTootl.upload(name, apkPic1));
		}
		if (apkPic2 != null) {
			String name = UUID.randomUUID() + apkPic2FileName.substring(apkPic2FileName.lastIndexOf("."));
			apkPic.add(ImageStorageTootl.upload(name, apkPic2));
		}
		if (apkPic3 != null) {
			String name = UUID.randomUUID() + apkPic3FileName.substring(apkPic3FileName.lastIndexOf("."));
			apkPic.add(ImageStorageTootl.upload(name, apkPic3));
		}
		if (apkPic4 != null) {
			String name = UUID.randomUUID() + apkPic4FileName.substring(apkPic4FileName.lastIndexOf("."));
			apkPic.add(ImageStorageTootl.upload(name, apkPic4));
		}
		if (apkPic5 != null) {
			String name = UUID.randomUUID() + apkPic5FileName.substring(apkPic5FileName.lastIndexOf("."));
			apkPic.add(ImageStorageTootl.upload(name, apkPic5));
		}
		extInfo3.put("apkPic", apkPic);
		extInfo3.put("apkSize", apkSize);
		extInfo3.put("apkDescription", apkDescription);
		// apk end

		bean.setExtInfo3(extInfo3.toString());
		toolboxService.updateTool(bean);
		if(!oldDownloadUrl.equals(downloadUrl)){
			delApkFromRedis(oldDownloadUrl);
		}
		return SUCCESS;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getIcon() {
		return icon;
	}

	public void setIcon(File icon) {
		this.icon = icon;
	}

	public String getIconFileName() {
		return iconFileName;
	}

	public void setIconFileName(String iconFileName) {
		this.iconFileName = iconFileName;
	}

	public int getCanMove() {
		return canMove;
	}

	public void setCanMove(int canMove) {
		this.canMove = canMove;
	}

	public File getMoveIconUrl() {
		return moveIconUrl;
	}

	public void setMoveIconUrl(File moveIconUrl) {
		this.moveIconUrl = moveIconUrl;
	}

	public String getMoveIconUrlFileName() {
		return moveIconUrlFileName;
	}

	public void setMoveIconUrlFileName(String moveIconUrlFileName) {
		this.moveIconUrlFileName = moveIconUrlFileName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}

	public int getDownloadAuto() {
		return downloadAuto;
	}

	public void setDownloadAuto(int downloadAuto) {
		this.downloadAuto = downloadAuto;
	}

	public int getDownloadDoneAlert() {
		return downloadDoneAlert;
	}

	public void setDownloadDoneAlert(int downloadDoneAlert) {
		this.downloadDoneAlert = downloadDoneAlert;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public int getDetailOpen() {
		return detailOpen;
	}

	public void setDetailOpen(int detailOpen) {
		this.detailOpen = detailOpen;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getMarketPackage() {
		return marketPackage;
	}

	public void setMarketPackage(String marketPackage) {
		this.marketPackage = marketPackage;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getExtInfo1() {
		return extInfo1;
	}

	public void setExtInfo1(String extInfo1) {
		this.extInfo1 = extInfo1;
	}

	public int getDownloadRecommend() {
		return downloadRecommend;
	}

	public void setDownloadRecommend(int downloadRecommend) {
		this.downloadRecommend = downloadRecommend;
	}

	public File getApkPic1() {
		return apkPic1;
	}

	public void setApkPic1(File apkPic1) {
		this.apkPic1 = apkPic1;
	}

	public File getApkPic2() {
		return apkPic2;
	}

	public void setApkPic2(File apkPic2) {
		this.apkPic2 = apkPic2;
	}

	public File getApkPic3() {
		return apkPic3;
	}

	public void setApkPic3(File apkPic3) {
		this.apkPic3 = apkPic3;
	}

	public File getApkPic4() {
		return apkPic4;
	}

	public void setApkPic4(File apkPic4) {
		this.apkPic4 = apkPic4;
	}

	public File getApkPic5() {
		return apkPic5;
	}

	public void setApkPic5(File apkPic5) {
		this.apkPic5 = apkPic5;
	}

	public File getApkPic0() {
		return apkPic0;
	}

	public void setApkPic0(File apkPic0) {
		this.apkPic0 = apkPic0;
	}

	public String getApkSize() {
		return apkSize;
	}

	public void setApkSize(String apkSize) {
		this.apkSize = apkSize;
	}

	public String getApkDescription() {
		return apkDescription;
	}

	public void setApkDescription(String apkDescription) {
		this.apkDescription = apkDescription;
	}

	public int getExtInfo1_extField1() {
		return extInfo1_extField1;
	}

	public void setExtInfo1_extField1(int extInfo1_extField1) {
		this.extInfo1_extField1 = extInfo1_extField1;
	}

	public String getApkPic1FileName() {
		return apkPic1FileName;
	}

	public void setApkPic1FileName(String apkPic1FileName) {
		this.apkPic1FileName = apkPic1FileName;
	}

	public String getApkPic2FileName() {
		return apkPic2FileName;
	}

	public void setApkPic2FileName(String apkPic2FileName) {
		this.apkPic2FileName = apkPic2FileName;
	}

	public String getApkPic3FileName() {
		return apkPic3FileName;
	}

	public void setApkPic3FileName(String apkPic3FileName) {
		this.apkPic3FileName = apkPic3FileName;
	}

	public String getApkPic4FileName() {
		return apkPic4FileName;
	}

	public void setApkPic4FileName(String apkPic4FileName) {
		this.apkPic4FileName = apkPic4FileName;
	}

	public String getApkPic5FileName() {
		return apkPic5FileName;
	}

	public void setApkPic5FileName(String apkPic5FileName) {
		this.apkPic5FileName = apkPic5FileName;
	}

	public String getApkPic0FileName() {
		return apkPic0FileName;
	}

	public void setApkPic0FileName(String apkPic0FileName) {
		this.apkPic0FileName = apkPic0FileName;
	}

}
