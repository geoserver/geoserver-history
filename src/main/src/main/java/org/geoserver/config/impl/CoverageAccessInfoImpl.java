/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.impl;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.geoserver.config.CoverageAccessInfo;

public class CoverageAccessInfoImpl implements Serializable, CoverageAccessInfo {

    private static final long serialVersionUID = 8909514231467268331L;

    transient ThreadPoolExecutor threadPoolExecutor;
    
    public static final int DEFAULT_MaxPoolSize = 10;
    int maxPoolSize = DEFAULT_MaxPoolSize;

    public static final int DEFAULT_CorePoolSize = 5;
    int corePoolSize = DEFAULT_CorePoolSize;

    public static final int DEFAULT_KeepAliveTime = 30000;
    int keepAliveTime = DEFAULT_KeepAliveTime;

    public static final QueueType DEFAULT_QUEUE_TYPE = QueueType.UNBOUNDED;
    QueueType queueType = DEFAULT_QUEUE_TYPE;

    public CoverageAccessInfoImpl(){
        threadPoolExecutor = new ThreadPoolExecutor(
                DEFAULT_CorePoolSize, 
                DEFAULT_MaxPoolSize, 
                DEFAULT_KeepAliveTime, 
                TimeUnit.MILLISECONDS, 
                new LinkedBlockingQueue<Runnable>());
    }
    
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public QueueType getQueueType() {
        return queueType;
    }

    public void setQueueType(QueueType queueType) {
        this.queueType = queueType;
    }
    
    public void dispose(){
        if (threadPoolExecutor != null){
            threadPoolExecutor.shutdown();
            threadPoolExecutor.shutdownNow();
        }
    }
}
