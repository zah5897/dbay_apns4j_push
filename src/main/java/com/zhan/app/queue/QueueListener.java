package com.zhan.app.queue;

import com.zhan.app.push.PushManager;

public class QueueListener {
	public void onMessage(String message) {
		PushManager.getInstance().commitTask(message);
	}
}
