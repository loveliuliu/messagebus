/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.messagebus.infrastructure.thread;

import java.util.HashMap;
import java.util.Map;


/**
 * 信号量管理器
 * 
 * @author wangxudong 2016年8月16日 下午7:41:52
 *
 */
public class SemaphorManager {

    /**
     * 信号量列表：key = {consumerId}
     */
    private static Map<String, AdjustableSemaphore> semaphoreMap = new HashMap<String, AdjustableSemaphore>();


    /**
     * 获取信号量
     * 
     * @param key
     * @return
     */
    public static AdjustableSemaphore get(String key) {
        return semaphoreMap.get(key);
    }

    /**
     * 添加信号量
     * 
     * @param key
     * @param semaphore
     */
    public static void put(String key, AdjustableSemaphore semaphore) {
        semaphoreMap.put(key, semaphore);
    }
}