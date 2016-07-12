package com.zhan.app.push;

import com.alibaba.fastjson.JSONObject;
import com.zhan.app.util.TextUtils;

public class PushManager {
	private NoopPushUtil pushUtil;

	private PushManager() {
		pushUtil = new NoopPushUtil();
	}

	private static PushManager pushManager;

	public static PushManager getInstance() {
		if (pushManager == null) {
			pushManager = new PushManager();
		}
		return pushManager;
	}

	public void push(PushMsg pushMsg) {
		pushUtil.pushNews(pushMsg);
	}

	public void commitTask(String message) {
		if (TextUtils.isEmpty(message)) {
			return;
		}
		push(prase(message));
	}

	private PushMsg prase(String message) {
		PushMsg pushMsg = null;
		try {
			JSONObject obj = JSONObject.parseObject(message);
			if (obj != null) {
				pushMsg = new PushMsg();
				pushMsg.id = obj.getString("id");
				pushMsg.token = obj.getString("token");
				pushMsg.alert = obj.getString("alert");
			}
		} catch (Exception e) {

		}
		return pushMsg;
	}
}
