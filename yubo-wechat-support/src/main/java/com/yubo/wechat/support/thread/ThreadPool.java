package com.yubo.wechat.support.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPool {
	
	/**
	 * 固定线程池，总大小为10，处理相对非紧急的小型业务
	 * 
	 */
	public static ExecutorService FIXED_POOL = Executors.newFixedThreadPool(10);

}
