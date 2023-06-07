package com.idata.executors;

import com.idata.common.ConnectorSource;
import com.idata.common.enums.SourceType;
import com.idata.core.Syncer;
import com.idata.core.SyncerFactory;

public class DataSyncExecutor {

    public static void start(ConnectorSource connectorSource) {
        if (connectorSource.getSourceType() == SourceType.MYSQL57) {
            Syncer syncer = SyncerFactory.getSyncer(connectorSource.getBizType());
            ThreadPoolManager.submit(syncer);
        }
    }
}
