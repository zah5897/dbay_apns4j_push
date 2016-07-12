package com.zhan.app.util;

import org.springframework.ui.ModelMap;

import com.zhan.app.exception.ERROR;

public class ResultUtil {
	public static ModelMap getResultMap(ERROR error, String msg) {
		ModelMap result = new ModelMap();
		result.addAttribute("code", error.getValue());
		result.addAttribute("msg", msg);
		return result;
	}

	public static ModelMap getResultMap(ERROR error) {
		ModelMap result = new ModelMap();
		result.addAttribute("code", error.getValue());
		result.addAttribute("msg", error.getErrorMsg());
		return result;
	}

	public static ModelMap getResultOKMap(String msg) {
		ModelMap result = new ModelMap();
		result.addAttribute("code", ERROR.ERR_NO_ERR.getValue());
		result.addAttribute("msg", msg);
		return result;
	}

	public static ModelMap getResultOKMap() {
		ModelMap result = new ModelMap();
		result.addAttribute("code", ERROR.ERR_NO_ERR.getValue());
		result.addAttribute("msg", ERROR.ERR_NO_ERR.getErrorMsg());
		return result;
	}
}
