package com.idata.connectors.mysql.disruptor;

import java.util.concurrent.ThreadFactory;

/**
 * <p>
 *  RowObjectThreadFactory
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/13 10:36 AM
 * @since 1.0
 */
public class RowObjectThreadFactory implements ThreadFactory {
    private String workName;

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread worker = new Thread(runnable, "row-object-Thread-" + workName);
        worker.setDaemon(true);
        return worker;
    }
}
