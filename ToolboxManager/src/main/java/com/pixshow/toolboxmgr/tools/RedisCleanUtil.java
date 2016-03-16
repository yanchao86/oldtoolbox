package com.pixshow.toolboxmgr.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;

import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

import redis.clients.jedis.Jedis;

public class RedisCleanUtil {
	public static void main(String[] args) {
		/*URL resource = Thread.currentThread().getClass().getResource("/");
		String path = resource.toString().replace("file:", "") + "tmpapks/";*/
		String path = args[0];
		File tmpapks = new File(path);
		if (!tmpapks.exists()) {
			tmpapks.mkdirs();
		}
		System.out.println(path);
		// String[] redisArr = {"10.0.0.4","10.0.0.5","10.0.0.7"};
//		String[] redisArr = { "127.0.0.1" };
//		String[] redisArr = args;
		// int port = 8378;
//		int port = 6379;
		
		for (int i = 1; i < args.length; i++) {
			Map<String, Map<String, Map<String, Object>>> packageMap = new HashMap<String, Map<String, Map<String, Object>>>();
			System.out.println("current redis server->"+args[i]);
			String[] split = args[i].split(":");
			String host = split[0];
			int port = Integer.valueOf(split[1]);
			System.out.println("host & port = "+host+":"+port);
			RedisFactory redisFactory = new RedisFactory(host, port);
			Jedis jedis = redisFactory.getJedis();
			try {
				Set<String> keys = jedis.keys("*");
				for (String key : keys) {
/*					if(!"hehe.apk".equalsIgnoreCase(key) && !"haha.apk".equalsIgnoreCase(key) ){
						continue;
					}*/
					if(key.endsWith("_size")){
						continue;
					}
					System.out.println("handling server:"+args[i]+"->key:"+key);
					byte[] keyBytes = key.getBytes();
					ApkParser apkParser=null;
					try {
						byte[] bytes = jedis.get(keyBytes);
						String fileName = path + key;
						String manifestFileName = path + "manifest.xml";
						FileUtils.writeByteArrayToFile(new File(fileName), bytes);
						apkParser = new ApkParser(fileName);
						ApkMeta apkMeta = apkParser.getApkMeta();
						String packageName = apkMeta.getPackageName();
						Long versionCode = apkMeta.getVersionCode();
						String manifestXml = apkParser.getManifestXml();
						File manifestFile = new File(manifestFileName);
						FileUtils.write(manifestFile, manifestXml);
						SAXReader reader = new SAXReader();
						Document androidManifestDoc = null;

						androidManifestDoc = reader.read(new InputStreamReader(new FileInputStream(manifestFile), "UTF-8"));
						Element manifest = androidManifestDoc.getRootElement();
						Element application = manifest.element("application");
						List<Element> elements = application.elements("meta-data");
						String channel = null;
						for (Element element : elements) {
							Attribute attribute = element.attribute(QName.get("name", manifest.getNamespaceForPrefix("android")));
							if ("UMENG_CHANNEL".equalsIgnoreCase(attribute.getValue())) {
								channel = element.attribute(QName.get("value", manifest.getNamespaceForPrefix("android"))).getValue();
								break;
							}
						}
						if(channel==null){
							System.out.println("channel is null key:"+key);
						}
						Map<String, Map<String, Object>> channelMap = packageMap.get(packageName);
						if (channelMap == null) {
//							System.out.println("not mapped key:" + key);
							channelMap = new HashMap<String, Map<String, Object>>();
							packageMap.put(packageName, channelMap);
							Map<String, Object> versionCodeMap = new HashMap<String, Object>();
							channelMap.put(channel, versionCodeMap);
							versionCodeMap.put("redisKey", key);
							versionCodeMap.put("versionCode", versionCode);
						} else {
//							System.out.println("mapped key:" + key);
							Map<String, Object> versionCodeMap = channelMap.get(channel);
							if (versionCodeMap == null) {
								versionCodeMap = new HashMap<String, Object>();
								versionCodeMap.put("versionCode", versionCode);
								versionCodeMap.put("redisKey", key);
							} else {
								if (channel != null) {
									Long vc = (Long) versionCodeMap.get("versionCode");
									String redisKey = (String) versionCodeMap.get("redisKey");
									if (vc > versionCode) {
										System.out.println("will del key:" + key);
										// jedis.del(keyBytes);
										// jedis.del(key+"_size");
									} else {
										System.out.println("will del key:" + redisKey);
										// jedis.del(redisKey.getBytes());
										// jedis.del(redisKey+"_size");
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Failed to handle key:" + key);
					}finally{
						if(apkParser!=null){
							apkParser.close();
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				RedisUtil.returnResource(jedis);
			}
		}
	}
}
