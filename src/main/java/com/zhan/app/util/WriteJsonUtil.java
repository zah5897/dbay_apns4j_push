package com.zhan.app.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zhan.app.exception.AppException;
import com.zhan.app.exception.ERROR;

public class WriteJsonUtil {
	public static void write(HttpServletResponse response, Exception ex) {
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			ERROR err;
			if (ex instanceof AppException) {
				err = ((AppException) ex).getError();
			} else {
				err = ERROR.ERR_SYS;
			}
			JSONObject json = new JSONObject();
			json.put("code", err.getValue());
			json.put("msg", err.getErrorMsg());
			writer.write(json.toString());
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write(HttpServletResponse response, ERROR error) {
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("code", error.ordinal());
			json.put("msg", error.getErrorMsg());
			writer.write(json.toString());
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
