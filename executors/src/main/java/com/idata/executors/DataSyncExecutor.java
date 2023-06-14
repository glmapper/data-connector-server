package com.idata.executors;

import com.idata.common.ConnectorSourceConfigs;
import com.idata.common.enums.SourceType;
import com.idata.core.Syncer;
import com.idata.core.SyncerFactory;

public class DataSyncExecutor {

    public static void start(ConnectorSourceConfigs connectorSourceConfigs) {
        if (connectorSourceConfigs.getSourceType() == SourceType.MYSQL57) {
            Syncer syncer = SyncerFactory.getSyncer(connectorSourceConfigs.getBizType());
            ThreadPoolManager.submit(syncer);
        }
    }
}
