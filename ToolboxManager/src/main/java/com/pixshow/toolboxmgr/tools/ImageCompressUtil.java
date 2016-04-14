package com.pixshow.toolboxmgr.tools;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;

public class ImageCompressUtil {
	/**
	 * 直接指定压缩后的宽高： (先保存原文件，再压缩、上传)
	 * 
	 * @param oldFile
	 *            要进行压缩的文件全路径
	 * @param width
	 *            压缩后的宽度
	 * @param height
	 *            压缩后的高度
	 * @param quality
	 *            压缩质量
	 * @param smallIcon
	 *            文件名的小小后缀(注意，非文件后缀名称),入压缩文件名是yasuo.jpg,则压缩后文件名是yasuo(+smallIcon
	 *            ).jpg
	 * @return 返回压缩后的文件的全路径
	 */
	public static String zipImageFile(String oldFile, int width, int height, float quality, String smallIcon) {
		if (oldFile == null) {
			return null;
		}
		String newImage = null;
		try {
			/** 对服务器上的临时文件进行处理 */
			Image srcFile = ImageIO.read(new File(oldFile));
			/** 宽,高设定 */
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);
			String filePrex = oldFile.substring(0, oldFile.indexOf('.'));
			/** 压缩后的文件名 */
			newImage = filePrex + smallIcon + oldFile.substring(filePrex.length());
			/** 压缩之后临时存放位置 */
			FileOutputStream out = new FileOutputStream(newImage);
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
//			/** 压缩质量 */
//			jep.setQuality(quality, true);
//			encoder.encode(tag, jep);
			
			ImageIO.write(tag, "JPEG", out);
			tag.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newImage;
	}

	/**
	 * 保存文件到服务器临时路径(用于文件上传)
	 * 
	 * @param fileName
	 * @param is
	 * @return 文件全路径
	 */
	public static String writeFile(String fileName, InputStream is) {
		if (fileName == null || fileName.trim().length() == 0) {
			return null;
		}
		try {
			/** 首先保存到临时文件 */
			FileOutputStream fos = new FileOutputStream(fileName);
			byte[] readBytes = new byte[512];// 缓冲大小
			int readed = 0;
			while ((readed = is.read(readBytes)) > 0) {
				fos.write(readBytes, 0, readed);
			}
			fos.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

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
//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
//		encoder.encode(tag); // 近JPEG编码
		deskImage.close();
	}

	public static void saveMinPhoto(String srcURL, String deskURL, double comBase, double scale) throws IOException {
		saveMinPhoto(new File(srcURL), new File(deskURL), comBase, scale);
	}

	public static void resizePNG(File fromFile, File toFile, int outputWidth, int outputHeight, boolean proportion) throws IOException {

		BufferedImage bi2 = ImageIO.read(fromFile);
		int newWidth;
		int newHeight;
		// 判断是否是等比缩放
		if (proportion == true) {
			// 为等比缩放计算输出的图片宽度及高度
			double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth + 0.1;
			double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight + 0.1;
			// 根据缩放比率大的进行缩放控制
			double rate = rate1 < rate2 ? rate1 : rate2;
			newWidth = (int) (((double) bi2.getWidth(null)) / rate);
			newHeight = (int) (((double) bi2.getHeight(null)) / rate);
		} else {
			newWidth = outputWidth; // 输出的图片宽度
			newHeight = outputHeight; // 输出的图片高度
		}
		BufferedImage to = new BufferedImage(newWidth, newHeight,

		BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = to.createGraphics();

		to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight,

		Transparency.TRANSLUCENT);

		g2d.dispose();

		g2d = to.createGraphics();

		Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
		g2d.drawImage(from, 0, 0, null);
		g2d.dispose();

		ImageIO.write(to, "png", toFile);

	}

	public static void resizePNG(String fromFilePath, String toFilePath, int outputWidth, int outputHeight, boolean proportion) throws IOException {
		File fromFile = new File(fromFilePath);
		File toFile = new File(toFilePath);
		resizePNG(fromFile, toFile, outputWidth, outputHeight, proportion);
	}

	/**
	 * 
	 * @param input
	 *            原始图像文件
	 * @param output
	 *            压缩后保存的图像文件
	 * @param size
	 *            压缩后的分辨率,如96 ,则压缩后的图片不大于96x96
	 * @param doYouDeleteTempFile
	 *            是否删除文件,如果无该参数,则默认为true(删除临时文件)
	 */

	public static boolean compressImageStronger(File input, File output, int size) {
		return compressImageStronger(input, output, size, true);
	}

	/**
	 * 
	 * @param input
	 *            原始图像文件
	 * @param output
	 *            压缩后保存的图像文件
	 * @param size
	 *            压缩后的分辨率,如96 ,则压缩后的图片不大于96x96
	 * @param doYouDeleteTempFile
	 *            是否删除文件,如果无该参数,则默认为true(删除临时文件)
	 */
	public static boolean compressImageStronger(File input, File output, int size, boolean doYouDeleteTempFile) {
		String fileName = input.getName();
		int index = fileName.lastIndexOf(".");
		String fileSuffix = fileName.substring(index, fileName.length());
		String absolutePath = input.getAbsolutePath();
		String absoluteDir = absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1);
		File tempFile = new File(absoluteDir + "temp_" + fileName);
		try {
			if (".png".equalsIgnoreCase(fileSuffix)) {
				resizePNG(input, tempFile, size, size, true);
			} else {
				saveMinPhoto(input, tempFile, size, 1d);
			}
			tinyPng(tempFile, output, "https://api.tinify.com/shrink", "TlrMtUgnk8PqaWHQd8EN6pB4a-ByUEcy");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (doYouDeleteTempFile) {
				tempFile.delete();
			}
		}

	}

	public static boolean compressImage(String fileSuffix,File input, File output, int size) {
		return compressImage(fileSuffix,input, output, size, true);
	}

	public static boolean compressImage(String fileSuffix,File input, File output, int size, boolean doYouDeleteTempFile) {

		String fileName = input.getName();
		int index = fileName.lastIndexOf(".");
//		String fileSuffix = fileName.substring(index, fileName.length());
		String absolutePath = input.getAbsolutePath();
		String absoluteDir = absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1);
		File tempFile = new File(absoluteDir + "temp_" + fileName.substring(0,index)+fileSuffix);
		FileInputStream tempFileInputStream = null;
		try {
			if (".png".equalsIgnoreCase(fileSuffix)) {
				resizePNG(input, tempFile, size, size, true);
				tempFileInputStream = new FileInputStream(tempFile);
				BufferedImage sourceImage = ImageIO.read(tempFileInputStream);
				BufferedImage compressImage = PngCompressUtil.compressImage(sourceImage);
				ImageIO.write(compressImage, "png", output);
			} else {
				saveMinPhoto(input, output, size, 1d);
//				tempFile = output;
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
		// ImageCompressUtil.zipImageFile("/home/wingszheng/Desktop/abcf30bf-9e85-4982-867c-1ff32b654c5e.png",
		// 48, 48, 1f, "x2");

		// ImageCompressUtil.saveMinPhoto("/home/wingszheng/Desktop/abcf30bf-9e85-4982-867c-1ff32b654c5e.png",
		// "/home/wingszheng/Desktop/abcf30bf-9e85-4982-867c-1ff32b654c5e_small.png",
		// 48, 0.9d);
		// File input = new
		// File("/home/wingszheng/toolboximg_test/test/709b8520-e6c1-4d9d-83ad-80ac23b33f84.png");
		// File input = new
		// File("/home/wingszheng/toolboximg_test/test/ac654123-6bae-490f-9ca7-4a8855f72895.jpg");
		// System.out.println(input.getParent());
		// File output = new
		// File("/home/wingszheng/toolboximg_test/709b8520-e6c1-4d9d-83ad-80ac23b33f84_tiny_bad.png");
		// // compressImageStronger(input, output, 96);
		// compressImage(input, input, 96);
		final File tempFile = new File("/home/wingszheng/toolboximg_test/test/709b8520-e6c1-4d9d-83ad-80ac23b33f84.png");
		final File outputFile = new File("/home/wingszheng/toolboximg_test/test/709b8520-e6c1-4d9d-83ad-80ac23b33f84_new.png");
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (sb.length() <= 0) {
						tinyPngWhile(tempFile, outputFile, "https://api.tinify.com/shrink", "TlrMtUgnk8PqaWHQd8EN6pB4a-ByUEcy", sb);
					}
				}
			}).start();
		}

		// tinyPngWhile(tempFile, tempFile, "https://api.tinify.com/shrink",
		// "TlrMtUgnk8PqaWHQd8EN6pB4a-ByUEcy");
	}

	public void test1() throws IOException {
		int size = 96;
		String tempDir = "/home/wingszheng/toolboximg/magic_temp/";

		String outDir = "/home/wingszheng/toolboximg/magic1";
		String inputDir = "/home/wingszheng/toolboximg";
		File file = new File(inputDir);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				continue;
			}
			String fileName = f.getName();
			int index = fileName.lastIndexOf(".");
			String fileSuffix = fileName.substring(index, fileName.length());
			String tempFile = tempDir + fileName;
			if (".png".equalsIgnoreCase(fileSuffix)) {
				PngCompressUtil.resizePNG(f, new File(tempFile), size, size, true);
				// BufferedImage sourceImage = ImageIO.read(new
				// FileInputStream(tempFile)); // BufferedImage compressImage =
				// PngCompressUtil.compressImage(sourceImage); //
				// ImageIO.write(compressImage, "png", new File(outDir + //
				// fileName));
			} else {
				ImageCompressUtil.saveMinPhoto(f.getAbsolutePath(), tempFile, size, 1d);
			}
			System.out.println(fileName + "\t" + fileSuffix);

		}
	}

	public static void tinyPng(File input, File output, String apiUrl, String key) {
		// String apiUrl = "https://api.tinify.com/shrink";
		// final String input = "large-input.png";
		// final String output = "tiny-output.png";
		OutputStream request = null;
		InputStream response = null;

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
			String auth = DatatypeConverter.printBase64Binary(("api:" + key).getBytes("UTF-8"));
			connection.setRequestProperty("Authorization", "Basic " + auth);
			connection.setDoOutput(true);
			request = connection.getOutputStream();
			FileUtils.copyFile(input, request);
			if (connection.getResponseCode() == 201) {
				final String url = connection.getHeaderField("location");
				connection = (HttpURLConnection) new URL(url).openConnection();
				response = connection.getInputStream();
				FileUtils.copyInputStreamToFile(response, output);
			} else {
				String responseMessage = connection.getResponseMessage();
				System.out.println("Compression failed.");
				System.out.println(responseMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				try {
					request.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void tinyPngWhile(File input, File output, String apiUrl, String key, StringBuilder sb) {
		int connectTimeout = 10000;
		int readTimeout = 60000;
		OutputStream request = null;
		InputStream response = null;
		try {
			if (sb.length() <= 0) {
				HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
				connection.setConnectTimeout(connectTimeout);
				connection.setReadTimeout(readTimeout);
				String auth = DatatypeConverter.printBase64Binary(("api:" + key).getBytes("UTF-8"));
				connection.setRequestProperty("Authorization", "Basic " + auth);
				connection.setDoOutput(true);
				if (sb.length() <= 0) {
					request = connection.getOutputStream();
					System.out.println("0");
					if (sb.length() <= 0) {
						FileUtils.copyFile(input, request);
						System.out.println("1");
						if (sb.length() <= 0 && connection.getResponseCode() == 201) {
							System.out.println("2");
							final String url = connection.getHeaderField("location");
							connection = (HttpURLConnection) new URL(url).openConnection();
							connection.setConnectTimeout(connectTimeout);
							connection.setReadTimeout(readTimeout);
							if (sb.length() <= 0) {
								response = connection.getInputStream();
								System.out.println("3");
								synchronized (ImageCompressUtil.class) {
									if (sb.length() <= 0) {
										FileUtils.copyInputStreamToFile(response, output);
										System.out.println("4");
										sb.setCharAt(0, '1');
									}
								}
							}

						}
					}

				}

			}

		} catch (Exception e) {
		} finally {
			if (request != null) {
				try {
					request.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}
}