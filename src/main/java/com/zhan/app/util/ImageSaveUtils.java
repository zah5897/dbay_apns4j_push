package com.zhan.app.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.web.multipart.MultipartFile;

public class ImageSaveUtils {

	// 头像压缩 按此宽度
	public static final int PRESS_AVATAR_WIDTH = 240;
	// 用户上传按此宽度
	public static final int PRESS_IMAGE_WIDTH = 180;

	private static final String ROOT_PATH = "love_upload";
	// 用户上传的图片路径
	public static final String FILE_ROOT_IMAGES_ORIGIN = "/images/origin/";
	public static final String FILE_ROOT_IMAGES_THUMB = "/images/thumb/";

	// 用户头像图片路径
	public static final String FILE_ROOT_AVATAR_ORIGIN = "/avatar/origin/";
	public static final String FILE_ROOT_AVATAR_THUMB = "/avatar/thumb/";

	private static String WEBAPP_PATH;

	private static String getRootPath(ServletContext servletContext) {
		if (WEBAPP_PATH != null) {
			return WEBAPP_PATH;
		}
		WEBAPP_PATH = servletContext.getRealPath("/");
		return WEBAPP_PATH;
	}

	public static String getOriginAvatarPath(ServletContext servletContext) {
		return getRootPath(servletContext) + ROOT_PATH + FILE_ROOT_AVATAR_ORIGIN;
	}

	public static String getThumbAvatarPath(ServletContext servletContext) {
		return getRootPath(servletContext) + ROOT_PATH + FILE_ROOT_AVATAR_THUMB;
	}

	public static String getOriginImagesPath(ServletContext servletContext) {
		return getRootPath(servletContext) + ROOT_PATH + FILE_ROOT_IMAGES_ORIGIN;
	}

	public static String getThumbImagesPath(ServletContext servletContext) {
		return getRootPath(servletContext) + ROOT_PATH + FILE_ROOT_IMAGES_THUMB;
	}

	public static String saveAvatar(MultipartFile file, ServletContext servletContext)
			throws IllegalStateException, IOException {
		String filePath = getOriginAvatarPath(servletContext);
		String shortName = file.getOriginalFilename();
		if (!TextUtils.isEmpty(shortName)) {
			String fileShortName = null;
			if (shortName.contains(".")) {
				fileShortName = UUID.randomUUID() + "." + shortName.split("\\.")[1];
			} else {
				fileShortName = UUID.randomUUID().toString();
			}
			File uploadFile = new File(filePath + fileShortName);
			uploadFile.mkdirs();
			file.transferTo(uploadFile);// 保存到一个目标文件中。

			String thumbFile = getThumbAvatarPath(servletContext) + fileShortName;
			pressImageByWidth(uploadFile.getAbsolutePath(), PRESS_AVATAR_WIDTH, thumbFile);
			return fileShortName;
		}
		return null;
	}

	public static String saveUserImages(MultipartFile file, ServletContext servletContext)
			throws IllegalStateException, IOException {
		String filePath = getOriginImagesPath(servletContext);
		String shortName = file.getOriginalFilename();
		if (!TextUtils.isEmpty(shortName)) {
			String fileShortName = null;
			if (shortName.contains(".")) {
				fileShortName = UUID.randomUUID() + "." + shortName.split("\\.")[1];
			} else {
				fileShortName = UUID.randomUUID().toString();
			}
			File uploadFile = new File(filePath + fileShortName);
			uploadFile.mkdirs();
			file.transferTo(uploadFile);// 保存到一个目标文件中。

			String thumbFile = getThumbImagesPath(servletContext) + fileShortName;
			pressImageByWidth(uploadFile.getAbsolutePath(), PRESS_IMAGE_WIDTH, thumbFile);
			return fileShortName;
		}
		return null;
	}

	/**
	 * 删除用户旧头像
	 * 
	 * @param servletContext
	 * @param oldFileName
	 *            旧头像名称
	 */
	public static void removeAcatar(ServletContext servletContext, String oldFileName) {
		// 删除大图

		String filePath = getOriginAvatarPath(servletContext);
		File uploadFile = new File(filePath + oldFileName);
		if (uploadFile.exists()) {
			uploadFile.delete();
		}

		// 删除小图
		String smallPath = getThumbAvatarPath(servletContext);
		File uploadSmallFile = new File(smallPath + oldFileName);
		if (uploadSmallFile.exists()) {
			uploadSmallFile.delete();
		}
	}

	/**
	 * 删除用户上传的图片
	 * 
	 * @param servletContext
	 * @param oldFileName
	 *            要删除的图片名称
	 */
	public static void removeUserImages(ServletContext servletContext, String oldFileName) {
		// 删除大图

		String filePath = getOriginImagesPath(servletContext);
		File uploadFile = new File(filePath + oldFileName);
		if (uploadFile.exists()) {
			uploadFile.delete();
		}

		// 删除小图
		String smallPath = getThumbImagesPath(servletContext);
		File uploadSmallFile = new File(smallPath + oldFileName);
		if (uploadSmallFile.exists()) {
			uploadSmallFile.delete();
		}
	}
	private static void pressImageByWidth(String origin, int minWidth, String thumb) throws IOException {
		ImageCompressUtil.resizeByWidth(origin, minWidth, thumb);
	}
}
