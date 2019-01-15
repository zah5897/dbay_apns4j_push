package com.zhan.app.push;

import java.util.concurrent.BlockingQueue;

import com.dbay.apns4j.IApnsConnection;
import com.dbay.apns4j.IApnsService;
import com.dbay.apns4j.impl.ApnsServiceImpl;
import com.dbay.apns4j.model.Payload;

public class PushRunable implements Runnable {

	private int pushCount = 0;

	String token;
	Payload payload;
	String appName;

	public PushRunable(String token, Payload payload, String appName) {
		this.token = token;
		this.payload = payload;
		this.appName = appName;
	}

	public void addToQueue(BlockingQueue<Runnable> queue) throws InterruptedException {
		if (pushCount > 3) {
			System.out.println("***********该token已被推送3次，任失败，请查看证书是否过去，appName=" + appName);
			return;
		}
		queue.put(this);
		pushCount++;

	}

	@Override
	public void run() {
		IApnsConnection conn = null;
		IApnsService service = ApnsServiceImpl.getCachedService(appName);
		try {
			conn = service.getConnection();
			conn.sendNotification(token, payload);
		} catch (Exception e) {
		} finally {
			if (conn != null) {
				service.getConnectPool().returnConn(conn);
			}
		}
	}

}
