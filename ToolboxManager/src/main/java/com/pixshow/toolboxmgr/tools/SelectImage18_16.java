package com.pixshow.toolboxmgr.tools;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class SelectImage18_16 {
	private static final int OUT_WIDTH = 720;
	private static final int OUT_HEIGHT = 1280;
	private static int threadCount = 0;
	private static final String OUT_PATH = "/home/wingszheng/Downloads/hiapk/hiapk_18_16_new";

	public static void main(String[] args) {
		File file = new File("/home/wingszheng/Downloads/hiapk/hiapk");
		digui(file);
//		System.out.println(1.125==1440*1.0/1280);
	}

	private static void digui(File srcFile) {
		File[] files = srcFile.listFiles();

		for (final File file : files) {
			if (file.isDirectory()) {
				System.out.println("thread:"+(threadCount++)+" : "+file.getPath());
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						digui(file);						
					}
				}).start();
				
			} else {
				Image oldImage;
				try {
					oldImage = ImageIO.read(file);
					int width = oldImage.getWidth(null);
					int height = oldImage.getHeight(null);
					if ((double)width/height>=1.1) {
						String fileName = file.getName();
						String filePrex = fileName.substring(0, fileName.indexOf('.'));
						/** 压缩后的文件名 */
/*						String dir = OUT_PATH+"/"+width+"_"+height;
						File dirFile = new File(dir);
						if(!dirFile.exists() || !dirFile.isDirectory()){
							dirFile.mkdirs();
						}*/
						String newImage = OUT_PATH + "/" + filePrex + "_"+width+"_"+height+"_" + fileName.substring(filePrex.length());
						FileOutputStream out = null;
						try {
							out = new FileOutputStream(newImage);
							
//							if (width == 720 && height == 1280) {
								FileUtils.copyFile(file, out);
								System.out.print(file.getPath() + " -> " + newImage+" success by copy");
/*							} else {
								*//** 宽,高设定 *//*
								BufferedImage tag = new BufferedImage(OUT_WIDTH, OUT_HEIGHT, BufferedImage.TYPE_INT_RGB);
								tag.getGraphics().drawImage(oldImage, 0, 0, OUT_WIDTH, OUT_HEIGHT, null);

								*//** 压缩之后临时存放位置 *//*

								JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
								JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
								*//** 压缩质量 *//*
								jep.setQuality(1.0f, true);
								encoder.encode(tag, jep);
								System.out.println(" success by compress");
							}*/

						} catch (Exception e) {
							System.out.println(" [failed]");
							e.printStackTrace();
						} finally {
							if (out != null) {
								out.close();
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
