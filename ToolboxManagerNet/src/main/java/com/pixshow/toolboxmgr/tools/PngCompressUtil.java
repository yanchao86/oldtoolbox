package com.pixshow.toolboxmgr.tools;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PngCompressUtil {
	/**
	 * 将背景为黑色不透明的图片转化为背景透明的图片
	 * 
	 * @param image
	 *            背景为黑色不透明的图片（用555格式转化后的都是黑色不透明的）
	 * @return 转化后的图片
	 */
	private static BufferedImage getConvertedImage(BufferedImage image) {
		 int width = image.getWidth();
		 int height = image.getHeight();
		BufferedImage convertedImage = null;
		Graphics2D g2D = null;
		// 采用带1 字节alpha的TYPE_4BYTE_ABGR，可以修改像素的布尔透明
		convertedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		g2D = (Graphics2D) convertedImage.getGraphics();
		g2D.drawImage(image, 0, 0, null);
		// 像素替换，直接把背景颜色的像素替换成0
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = convertedImage.getRGB(i, j);
				if (isBackPixel(rgb)) {
					convertedImage.setRGB(i, j, 0);
				}
			}
		}
		g2D.drawImage(convertedImage, 0, 0, null);
		return convertedImage;
	}

	/**
	 * 图片压缩
	 * 
	 * @param sourceImage
	 *            要压缩的图片
	 * @return 压缩后的图片
	 * @throws IOException
	 *             图片读写异常
	 */
	public static BufferedImage compressImage(BufferedImage sourceImage) throws IOException {
		if (sourceImage == null)
			throw new NullPointerException("空图片");
		BufferedImage cutedImage = null;
		BufferedImage tempImage = null;
		BufferedImage compressedImage = null;
		Graphics2D g2D = null;
		// 图片自动裁剪
		cutedImage = cutImageAuto(sourceImage);
		int width = cutedImage.getWidth();
		int height = cutedImage.getHeight();
		// 图片格式为555格式
		tempImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB);
		g2D = (Graphics2D) tempImage.getGraphics();
		g2D.drawImage(sourceImage, 0, 0, null);
		compressedImage = getConvertedImage(tempImage);
		// 经过像素转化后的图片
		compressedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		g2D = (Graphics2D) compressedImage.getGraphics();
		g2D.drawImage(tempImage, 0, 0, null);
		int pixel[] = new int[width * height];
		int sourcePixel[] = new int[width * height];
		int currentPixel[] = new int[width * height];
		sourcePixel = cutedImage.getRGB(0, 0, width, height, sourcePixel, 0, width);
		currentPixel = tempImage.getRGB(0, 0, width, height, currentPixel, 0, width);
		for (int i = 0; i < currentPixel.length; i++) {
			if (i == 0 || i == currentPixel.length - 1) {
				pixel[i] = 0;
				// 内部像素
			} else if (i > width && i < currentPixel.length - width) {
				int bef = currentPixel[i - 1];
				int now = currentPixel[i];
				int aft = currentPixel[i + 1];
				int up = currentPixel[i - width];
				int down = currentPixel[i + width];
				// 背景像素直接置为0
				if (isBackPixel(now)) {
					pixel[i] = 0;
					// 边框像素和原图一样
				} else if ((!isBackPixel(now) && isBackPixel(bef)) || (!isBackPixel(now) && isBackPixel(aft)) || (!isBackPixel(now) && isBackPixel(up)) || (!isBackPixel(now) && isBackPixel(down))) {
					pixel[i] = sourcePixel[i];
					// 其他像素和555压缩后的像素一样
				} else {
					pixel[i] = now;
				}
				// 边界像素
			} else {
				int bef = currentPixel[i - 1];
				int now = currentPixel[i];
				int aft = currentPixel[i + 1];
				if (isBackPixel(now)) {
					pixel[i] = 0;
				} else if ((!isBackPixel(now) && isBackPixel(bef)) || (!isBackPixel(now) && isBackPixel(aft))) {
					pixel[i] = sourcePixel[i];
				} else {
					pixel[i] = now;
				}
			}
		}
		compressedImage.setRGB(0, 0, width, height, pixel, 0, width);
		g2D.drawImage(compressedImage, 0, 0, null);
		// ImageIO.write(cutedImage, "png", new File("cut/a_cut.png"));
		// ImageIO.write(tempImage, "png", new File("cut/b_555.png"));
		// ImageIO.write(compressedImage, "png", new File("cut/c_com.png"));
		return compressedImage;
	}

	/**
	 * 图片自动裁剪
	 * 
	 * @param image
	 *            要裁剪的图片
	 * @return 裁剪后的图片
	 */
	public static BufferedImage cutImageAuto(BufferedImage image) {
		Rectangle area = getCutAreaAuto(image);
		return image.getSubimage(area.x, area.y, area.width, area.height);
	}

	/**
	 * 获得裁剪图片保留区域
	 * 
	 * @param image
	 *            要裁剪的图片
	 * @return 保留区域
	 */
	private static Rectangle getCutAreaAuto(BufferedImage image) {
		if (image == null)
			throw new NullPointerException("图片为空");
		int width = image.getWidth();
		int height = image.getHeight();
		int startX = width;
		int startY = height;
		int endX = 0;
		int endY = 0;
		int[] pixel = new int[width * height];

		pixel = image.getRGB(0, 0, width, height, pixel, 0, width);
		for (int i = 0; i < pixel.length; i++) {
			if (isCutBackPixel(pixel[i]))
				continue;
			else {
				int w = i % width;
				int h = i / width;
				startX = (w < startX) ? w : startX;
				startY = (h < startY) ? h : startY;
				endX = (w > endX) ? w : endX;
				endY = (h > endY) ? h : endY;
			}
		}
		if (startX > endX || startY > endY) {
			startX = startY = 0;
			endX = width;
			endY = height;
		}
		return new Rectangle(startX, startY, endX - startX, endY - startY);
	}

	/**
	 * 当前像素是否为背景像素
	 * 
	 * @param pixel
	 * @return
	 */
	private static boolean isCutBackPixel(int pixel) {
		int back[] = { 0, 8224125, 16777215, 8947848, 460551, 4141853, 8289918 };
		for (int i = 0; i < back.length; i++) {
			if (back[i] == pixel)
				return true;
		}
		return false;
	}

	/**
	 * 判断当前像素是否为黑色不透明的像素（-16777216）
	 * 
	 * @param pixel
	 *            要判断的像素
	 * @return 是背景像素返回true，否则返回false
	 */
	private static boolean isBackPixel(int pixel) {
		int back[] = { -16777216 };
		for (int i = 0; i < back.length; i++) {
			if (back[i] == pixel)
				return true;
		}
		return false;
	}
	public static void resizePNG(File fromFile, File toFile, int outputWidth, int outputHeight, boolean proportion){
		try {

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

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
	public static void resizePNG(String fromFilePath, String toFilePath, int outputWidth, int outputHeight, boolean proportion) {
		File fromFile = new File(fromFilePath);
		File toFile = new File(toFilePath);
		resizePNG(fromFile, toFile, outputWidth, outputHeight, proportion);
	}

	public static void main(String[] args) throws Exception {
		ImageCompressUtil.saveMinPhoto("/home/wingszheng/Desktop/8a3cf870-a2f5-419c-83cf-08fe85a5cb1d.jpg", "/home/wingszheng/Desktop/abcf30bf-9e85-4982-867c-1ff32b654c5e_small.jpg", 128, 1d);
		
		String imgPath = "/home/wingszheng/Desktop/abcf30bf-9e85-4982-867c-1ff32b654c5e_small.png";
		BufferedImage sourceImage = ImageIO.read(new FileInputStream(imgPath));
		BufferedImage compressImage = compressImage(sourceImage);
		ImageIO.write(compressImage, "png", new File("/home/wingszheng/Desktop/abcf30bf-9e85-4982-867c-1ff32b654c5e_small.png"));
	}
}
