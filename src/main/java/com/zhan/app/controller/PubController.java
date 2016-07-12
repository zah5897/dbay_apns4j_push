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
	public String pub(HttpServletRequest request, String content) {

		JSONObject object = new JSONObject();
		String title = content;
		object.put("id", "5776326d016d286fa9c71744");
		object.put("token", "d6207de6aa39c1517d315858500c7c8392ad81664ff3435220df515dbb408e2d");
		object.put("alert", title);

		redisTemplate.opsForList().leftPush(RedisKeys.KEY_NEWS_PUSH, object.toJSONString());
		// System.out.println("start:" + new Date().toLocaleString());
		// new Thread() {
		// @Override
		// public void run() {
		// int i = 0;
		// do {
		// JSONObject object = new JSONObject();
		// String title = "《所以和黑粉结婚了》：多少影片都毁在坑爹的电影名上了";
		// object.put("id", "5775b644016dfffe521f90cb");
		// object.put("index", i);
		// object.put("alert", title);
		// redisTemplate.opsForList().leftPush(RedisKeys.KEY_NEWS_PUSH,
		// object.toJSONString());
		// i++;
		// } while (i < 10000);
		// }
		// }.start();

		return "";
	}
}
