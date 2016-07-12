package com.zhan.app.util;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.*;

/**
 * 图片压缩处理
 * 
 */
public class ImageCompressUtil {
	// private Image img;
	// private int width;
	// private int height;
	// @SuppressWarnings("deprecation")
	// public static void main(String[] args) throws Exception {
	// System.out.println("开始：" + new Date().toLocaleString());
	// ImgCompress imgCom = new ImgCompress("C:\\temp\\pic123.jpg");
	// imgCom.resizeFix(400, 400);
	// System.out.println("结束：" + new Date().toLocaleString());
	// }
	/**
	 * 构造函数
	 */
	// public ImgCompress(String fileName) throws IOException {
	// File file = new File(fileName);// 读入文件
	// img = ImageIO.read(file); // 构造Image对象
	// width = img.getWidth(null); // 得到源图宽
	// height = img.getHeight(null); // 得到源图长
	// }
	/**
	 * 按照宽度还是高度进行压缩
	 * 
	 * @param w
	 *            int 最大宽度
	 * @param h
	 *            int 最大高度
	 */
	public static void resizeFix(String bigImage, int targetWidth, int targetHeight, String smallImage)
			throws IOException {

		File file = new File(bigImage);// 读入文件
		Image img = ImageIO.read(file); // 构造Image对象
		int width = img.getWidth(null); // 得到源图宽
		int height = img.getHeight(null); // 得到源图长

		if (width / height > targetWidth / targetHeight) {

			int h = (int) (height * targetWidth / width);
			resize(img, targetWidth, h, smallImage);

			// resizeByWidth(targetWidth);
		} else {
			// resizeByHeight(targetHeight);
			int w = (int) (width * targetHeight / height);
			resize(img, w, targetHeight, smallImage);
		}
	}

	/**
	 * 以宽度为基准，等比例放缩图片
	 * 
	 * @param w
	 *            int 新宽度
	 */
	public static void resizeByWidth(String bigImage, int w, String smallImage) throws IOException {

		File file = new File(bigImage);// 读入文件
		Image img = ImageIO.read(file); // 构造Image对象
		int width = img.getWidth(null); // 得到源图宽
		int height = img.getHeight(null); // 得到源图长
		int h = (int) (height * w / width);
		resize(img, w, h, smallImage);
	}

	/**
	 * 以高度为基准，等比例缩放图片
	 * 
	 * @param h
	 *            int 新高度
	 */
	public static void resizeByHeight(String bigImage, int h, String smallImage) throws IOException {

		File file = new File(bigImage);// 读入文件
		Image img = ImageIO.read(file); // 构造Image对象
		int width = img.getWidth(null); // 得到源图宽
		int height = img.getHeight(null); // 得到源图长
		int w = (int) (width * h / height);
		resize(img, w, h, smallImage);
	}

	/**
	 * 强制压缩/放大图片到固定的大小
	 * 
	 * @param w
	 *            int 新宽度
	 * @param h
	 *            int 新高度
	 */
	@SuppressWarnings("restriction")
	public static void resize(Image img, int w, int h, String smallImage) throws IOException {
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		File destFile = new File(smallImage);
		destFile.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		// 可以正常实现bmp、png、gif转jpg
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(image); // JPEG编码
		out.close();
	}
}