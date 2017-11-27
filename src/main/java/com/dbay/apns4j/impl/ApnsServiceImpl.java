/*
 * Copyright 2013 DiscoveryBay Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dbay.apns4j.impl;

import static com.dbay.apns4j.model.ApnsConstants.ALGORITHM;
import static com.dbay.apns4j.model.ApnsConstants.KEYSTORE_TYPE;
import static com.dbay.apns4j.model.ApnsConstants.PROTOCOL;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dbay.apns4j.IApnsConnection;
import com.dbay.apns4j.IApnsFeedbackConnection;
import com.dbay.apns4j.IApnsService;
import com.dbay.apns4j.model.ApnsConfig;
import com.dbay.apns4j.model.Feedback;
import com.dbay.apns4j.model.Payload;
import com.dbay.apns4j.model.PushNotification;
import com.dbay.apns4j.tools.ApnsTools;

/**
 * The service should be created twice at most. One for the development env, and
 * the other for the production env
 * 
 * @author RamosLi
 *
 */
public class ApnsServiceImpl implements IApnsService {
	private static Log logger = LogFactory.getLog(ApnsServiceImpl.class);
	private ExecutorService service = null;
	private ApnsConnectionPool connPool = null;
	private IApnsFeedbackConnection feedbackConn = null;
    private String appName;
	private ApnsServiceImpl(ApnsConfig config,String appName) {
		this.appName=appName;
		service = initExecute();
		SocketFactory factory = ApnsTools.createSocketFactory(config.getKeyStore(), config.getPassword(), KEYSTORE_TYPE,
				ALGORITHM, PROTOCOL);
		connPool = ApnsConnectionPool.newConnPool(config, factory);
		feedbackConn = new ApnsFeedbackConnectionImpl(config, factory);
	}

	private ExecutorService initExecute() {
		int fixPool = getPoolSize();
		ExecutorService executor = new ThreadPoolExecutor(fixPool, fixPool, 1000L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(fixPool), new RejectedExecutionHandler() {

					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						if (!executor.isShutdown()) {
							try {
								System.out.println(
										"******************************线程池已经满了，请等待****************************");
								executor.getQueue().put(r);
							} catch (InterruptedException e) {
							}
						}
					}
				});
		return executor;
	}

	private int getPoolSize() {
		int numberOfCores = Runtime.getRuntime().availableProcessors(); // 获得核心数
		double blockingCoefficient = 0.9;// 阻塞系数
		int poolSize = (int) (numberOfCores / (1 - blockingCoefficient)); // 求得线程数大小
		return poolSize;
	}

	public void sendNotification(final String token, final Payload payload) {
		service.execute(new Runnable() {
			public void run() {
				IApnsConnection conn = null;
				try {
					conn = getConnection();
					conn.sendNotification(token, payload);
					System.out.println("push task complete!"+new Date().toLocaleString());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					if (conn != null) {
						connPool.returnConn(conn);
					}
				}
			}
		});
	}

	public void sendNotification(final PushNotification notification) {
		service.execute(new Runnable() {
			public void run() {
				IApnsConnection conn = null;
				try {
					conn = getConnection();
					conn.sendNotification(notification);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					if (conn != null) {
						connPool.returnConn(conn);
					}
				}
			}
		});
	}

	private IApnsConnection getConnection() {
		IApnsConnection conn = connPool.borrowConn();
		if (conn == null) {
			throw new RuntimeException("Can't get apns connection");
		}
		return conn;
	}

	private static void checkConfig(ApnsConfig config) {
		if (config == null || config.getKeyStore() == null || config.getPassword() == null
				|| "".equals(config.getPassword().trim())) {
			throw new IllegalArgumentException("KeyStore and password can't be null");
		}
		if (config.getPoolSize() <= 0 || config.getRetries() <= 0 || config.getCacheLength() <= 0) {
			throw new IllegalArgumentException("poolSize,retry, cacheLength must be positive");
		}
	}

	private static Map<String, IApnsService> serviceCacheMap = new HashMap<String, IApnsService>(3);

	public static IApnsService getCachedService(String name) {
		return serviceCacheMap.get(name);
	}

	public static IApnsService createInstance(ApnsConfig config) {
		checkConfig(config);
		String name = config.getName();
		IApnsService service = getCachedService(name);
		if (service == null) {
			synchronized (name.intern()) {
				service = getCachedService(name);
				if (service == null) {
					service = new ApnsServiceImpl(config,name);
					serviceCacheMap.put(name, service);
				}
			}
		}
		return service;
	}

	public void shutdown() {
		service.shutdown();
		try {
			service.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.warn("Shutdown ApnsService interrupted", e);
		}
		connPool.close();
	}

	public List<Feedback> getFeedbacks() {
		return feedbackConn.getFeedbacks();
	}
}
