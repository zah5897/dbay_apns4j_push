package com.zhan.app.queue;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;

import com.zhan.app.push.PushManager;
import com.zhan.app.util.RedisKeys;

public class RedisQueue<T> implements InitializingBean, DisposableBean {

	@Resource
	protected RedisTemplate<String, String> redisTemplate;

	private int i = 0;

	public void destroy() throws Exception {

	}

	public void afterPropertiesSet() throws Exception {
		if (redisTemplate != null) {
			startPoll();
		}
	}

	private void startPoll() {
		newsPoll();
		nearbyPool();
	}

	// 鏂伴椈鎺ㄩ��
	private void newsPoll() {
		// 鏂伴椈鎺ㄩ��
		new Thread() {
			@Override
			public void run() {
				System.out.println("start ready to push task.");
				while (true) {
					try {
						String msg = redisTemplate.opsForList().rightPop(RedisKeys.KEY_NEWS_PUSH, 0, TimeUnit.SECONDS);
						if (msg != null) {
							PushManager.getInstance().commitTask(msg);
							if (i == 100) {
								System.out.println("----------new push---------");
								i = 0;
							} else {
								i++;
							}

						}
						continue;
					} catch (Exception e) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
						System.out.println("while true out error.");
					}
					continue;
				}

			}
		}.start();
	}
	// 闄勮繎鎺ㄩ��

	private void nearbyPool() {
		// 闄勮繎鎺ㄩ��
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {

						String msg = redisTemplate.opsForList().rightPop(RedisKeys.KEY_NEARBY_PUSH, 0,
								TimeUnit.SECONDS);
						if (msg != null) {
							PushManager.getInstance().commitTask(msg);
						}
						continue;
					} catch (Exception e) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					System.out.println("while true out error.");
					continue;
				}

			}
		}.start();
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
