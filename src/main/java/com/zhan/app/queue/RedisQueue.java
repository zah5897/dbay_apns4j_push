package com.zhan.app.queue;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;

import com.zhan.app.util.RedisKeys;

public class RedisQueue<T> implements InitializingBean, DisposableBean {

	@Resource
	protected RedisTemplate<String, String> redisTemplate;
	@Resource
	private QueueListener jedisQueueListener;

	public void destroy() throws Exception {

	}

	public void afterPropertiesSet() throws Exception {
		if (redisTemplate != null) {
			startPoll();
		}
	}

	private void startPoll() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					System.out.println(redisTemplate.isExposeConnection());
					try {
						System.out.println("before rightPop msg");
						String msg = redisTemplate.opsForList().rightPop(RedisKeys.KEY_NEWS_PUSH, 0, TimeUnit.SECONDS);
						if (msg != null) {
							System.out.println("new push task.");
							if (jedisQueueListener != null) {
								jedisQueueListener.onMessage(msg);
							}
							System.out.println("new push task over.");
						}
						continue;
					} catch (Exception e) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.err.println(e);
						System.out.println("while true out error.");
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

	public QueueListener getJedisQueueListener() {
		return jedisQueueListener;
	}

	public void setJedisQueueListener(QueueListener jedisQueueListener) {
		this.jedisQueueListener = jedisQueueListener;
	}

}
