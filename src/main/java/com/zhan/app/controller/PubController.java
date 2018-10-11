package com.zhan.app.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zhan.app.util.RedisKeys;

@RestController
@RequestMapping("/pub")
public class PubController {
	@Resource
	protected RedisTemplate<String, String> redisTemplate;

	@RequestMapping("topub")
	public String pub(HttpServletRequest request,String token, String content) {
		org.springframework.web.util.Log4jConfigListener sl;
		JSONObject object = new JSONObject();
		String title = content;
		object.put("id", "5776326d016d286fa9c71744");
		object.put("token", token);
		object.put("alert", title);
		object.put("app_name", "news_look_for");
		redisTemplate.opsForList().leftPush(RedisKeys.KEY_NEWS_PUSH, object.toJSONString());
		return "";
	}
}
