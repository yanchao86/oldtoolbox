package com.pixshow;

import com.pixshow.toolboxmgr.tools.RedisUtil;

import redis.clients.jedis.Jedis;

public class TestRedis {
    public static void main(String[] args) {
//    	String path = "/home/wingszheng/Work/testjedis";
//    	File dir = new File(path);
//    	File[] files = dir.listFiles();
//    	Jedis jedis = RedisUtil.getJedis();
//		for (File file : files) {
//			try {
//				byte[] bytes = FileUtils.readFileToByteArray(file);
//				jedis.set(file.getName().getBytes(), bytes);
//			} catch (IOException e) {
//				System.err.println("失败 : "+file.getAbsolutePath());
//			}
//		}
//		RedisUtil.returnResource(jedis);
    	Jedis jedis = RedisUtil.getJedis();
    	byte[] bs = jedis.get("dtrings_1.0.6_gjx.apk".getBytes());
    	System.out.println(bs.length);
    	
	}
}


