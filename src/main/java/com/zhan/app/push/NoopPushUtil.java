package com.zhan.app.push;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.dbay.apns4j.IApnsService;
import com.dbay.apns4j.demo.Apns4jDemo;
import com.dbay.apns4j.impl.ApnsServiceImpl;
import com.dbay.apns4j.model.ApnsConfig;
import com.dbay.apns4j.model.Payload;
import com.zhan.app.util.TextUtils;

public class NoopPushUtil {
	private static IApnsService getApnsService(int type, String app_name) {
		IApnsService apnsService = ApnsServiceImpl.getCachedService(app_name);
		if (apnsService == null) {
			String signName = null;
			if (type == 0) {
				signName = "dis_" + app_name + ".p12";
			} else {
				signName = "dev_" + app_name + ".p12";
			}

			ApnsConfig config = new ApnsConfig();
			InputStream is = Apns4jDemo.class.getClassLoader().getResourceAsStream(signName);
			config.setKeyStore(is);
			config.setDevEnv(type != 0);
			config.setPassword("magicpush");
			config.setPoolSize(10);
			// 假如需要在同个java进程里给不同APP发送通知，那就需要设置为不同的name
			config.setName(app_name);
			apnsService = ApnsServiceImpl.createInstance(config);
		}
		return apnsService;
	}

	public boolean pushNews(PushMsg msg) {
		if (msg == null) {
			return false;
		}
		if (TextUtils.isEmpty(msg.app_name)) {
			return false;
		}
		IApnsService service = getApnsService(msg.type, msg.app_name);
		Map<String, String> info = new HashMap<String, String>();
		info.put("type", "0");
		info.put("id", msg.id);
		info.put("msg_id", String.valueOf(msg.msg_id));
		Payload payload = new Payload();
		payload.setAlert(msg.alert);
		// send notification
		// String token = "e8acacd7f67a13464f36b2b64e9393ae5d77331d";
		String token = msg.token;

		// If this property is absent, the badge is not changed. To remove the
		// badge, set the value of this property to 0
		payload.setBadge(1);
		// set sound null, the music won't be played
		// payload.setSound(null);
		payload.setSound("default");
		payload.addParam("info", info);
		service.sendNotification(token, payload);
		return true;
	}

	public static void main(String[] args) {
		PushMsg pm = new PushMsg();
		pm.app_name = "news";
		pm.alert = "test";
		pm.token = "e8acacd7f67a13464f36b2b64e9393ae5d7733ee"; // 假token
		pm.id = "123"; // 假token
		new NoopPushUtil().pushNews(pm);
	}
}
