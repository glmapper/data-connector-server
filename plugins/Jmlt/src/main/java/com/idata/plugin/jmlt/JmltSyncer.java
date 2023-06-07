package com.idata.plugin.jmlt;

import com.idata.common.ConnectorSource;
import com.idata.common.model.StandardMdjfModel;
import com.idata.connectors.mysql.MysqlConnector;
import com.idata.core.Connector;
import com.idata.core.Convertor;
import com.idata.core.Syncer;
import com.idata.plugin.jmlt.model.JmltModel;

public class JmltSyncer extends Syncer {

    private Connector connector;

    public JmltSyncer(Convertor convertor, ConnectorSource source) {
        this.connector = new MysqlConnector(source, convertor);
    }

    @Override
    public void sync() {
        this.connector.sync(JmltModel.class, StandardMdjfModel.class);
    }

}
