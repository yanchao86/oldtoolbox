package com.pixshow.toolboxmgr.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;

public class ImageCompressUtil {

	/**
	 * 等比例压缩算法： 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
	 * 
	 * @param srcURL
	 *            原图地址
	 * @param deskURL
	 *            缩略图地址
	 * @param comBase
	 *            压缩基数
	 * @param scale
	 *            压缩限制(宽/高)比例 一般用1：
	 *            当scale>=1,缩略图height=comBase,width按原图宽高比例;若scale
	 *            <1,缩略图width=comBase,height按原图宽高比例
	 * @throws Exception
	 * @author shenbin
	 * @throws IOException
	 * @createTime 2014-12-16
	 * @lastModifyTime 2014-12-16
	 */
	public static void saveMinPhoto(File srcFile, File deskFile, double comBase, double scale) throws IOException {
		Image src = ImageIO.read(srcFile);
		int srcHeight = src.getHeight(null);
		int srcWidth = src.getWidth(null);
		int deskHeight = 0;// 缩略图高
		int deskWidth = 0;// 缩略图宽
		double srcScale = (double) srcHeight / srcWidth;
		/** 缩略图宽高算法 */
		if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
			if (srcScale >= scale || 1 / srcScale > scale) {
				if (srcScale >= scale) {
					deskHeight = (int) comBase;
					deskWidth = srcWidth * deskHeight / srcHeight;
				} else {
					deskWidth = (int) comBase;
					deskHeight = srcHeight * deskWidth / srcWidth;
				}
			} else {
				if ((double) srcHeight > comBase) {
					deskHeight = (int) comBase;
					deskWidth = srcWidth * deskHeight / srcHeight;
				} else {
					deskWidth = (int) comBase;
					deskHeight = srcHeight * deskWidth / srcWidth;
				}
			}
		} else {
			deskHeight = srcHeight;
			deskWidth = srcWidth;
		}
		BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
		tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); // 绘制缩小后的图
		FileOutputStream deskImage = new FileOutputStream(deskFile); // 输出到文件流
		ImageIO.write(tag, "JPEG", deskFile);
		// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
		// encoder.encode(tag); // 近JPEG编码
		deskImage.close();
	}

	public static void saveMinPhoto(String srcURL, String deskURL, double comBase, double scale) throws IOException {
		saveMinPhoto(new File(srcURL), new File(deskURL), comBase, scale);
	}

	public static boolean compressImage(String fileSuffix, File input, File output, int size) {
		return compressImage(fileSuffix, input, output, size, true);
	}

	public static boolean compressImage(String fileSuffix, File input, File output, int size, boolean doYouDeleteTempFile) {

		String fileName = input.getName();
		int index = fileName.lastIndexOf(".");
		// String fileSuffix = fileName.substring(index, fileName.length());
		String absolutePath = input.getAbsolutePath();
		String absoluteDir = absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1);
		File tempFile = new File(absoluteDir + "temp_" + fileName.substring(0, index) + fileSuffix);
		FileInputStream tempFileInputStream = null;
		try {
			if (".png".equalsIgnoreCase(fileSuffix)) {
				Tinify.setKey("TlrMtUgnk8PqaWHQd8EN6pB4a-ByUEcy");
				Source source = Tinify.fromFile(input.getAbsolutePath());
				Options options = new Options().with("method", "fit").with("width", size).with("height", size);
				Source resized = source.resize(options);
				resized.toFile(output.getPath());

			} else {
				saveMinPhoto(input, output, size, 1d);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (tempFileInputStream != null) {
				try {
					tempFileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (doYouDeleteTempFile) {
				tempFile.delete();
			}
		}
	}

	public static void main(String args[]) throws Exception {

		ImageCompressUtil.compressImage(".png", new File("/home/hope/Desktop/apk/23-1210231G308.png"), new File("/home/hope/Desktop/apk/23-1210231G308_new.png"), 96, true);

	}

}