package com.idata.connectors.mysql;

import com.idata.common.ConnectorHelper;
import com.idata.common.ConnectorSourceConfigs;
import com.idata.common.annotations.Table;
import com.idata.common.enums.SourceType;
import com.idata.connectors.mysql.disruptor.RowObjectManager;
import com.idata.connectors.mysql.page.RowBounds;
import com.idata.core.Connector;
import com.idata.core.Convertor;
import com.idata.core.ResultSetExtractor;
import com.idata.core.sql.SqlTemplate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 这里默认使用 HikariDataSource
 */
public class MysqlConnector<T, R> implements Connector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlConnector.class);

    private final ConnectorSourceConfigs source;

    /**
     * 资源转换器
     */
    private Convertor<T, R> convertor;

    /**
     * 原始库的池
     */
    private DataSource originDataSource;

    /**
     * 目标库的池
     */
    private DataSource targetDataSource;

    private RowObjectManager<T> rowObjectManager;

    public MysqlConnector(ConnectorSourceConfigs source, Convertor<T, R> convertor) {
        this.source = source;
        this.convertor = convertor;
        init();
    }

    @Override
    public void init() {
        HikariConfig config = new HikariConfig();
        this.originDataSource = buildDatasource(this.source.getOriginSource());
        this.targetDataSource = buildDatasource(this.source.getTargetSource());
        this.rowObjectManager = new RowObjectManager<>(1024, this.targetDataSource);
        if (this.convertor == null) {
            throw new UnsupportedOperationException("No plugin support current convert, bizType: " + this.source.getBizType());
        }
        this.rowObjectManager.start("mysql-sync");
    }

    @Override
    public SourceType sourceType() {
        return SourceType.MYSQL57;
    }


    @Override
    public void sync(Class origin, Class target) {
        try {
            // 1、从原始库中读取数据，这里的读取是需要按照一定的模式来读取，比如这里默认使用的是读取前一天的数据
            Table declaredAnnotation = (Table) origin.getDeclaredAnnotation(Table.class);
            String tableName = declaredAnnotation.name();

            // 2、计算所有的条数，然后按照分页的方式进行 fetch
            SqlTemplate sqlTemplate = new SqlTemplate(this.originDataSource);
            //+ " WHERE TO_DAYS(NOW()) - TO_DAYS(`create_time`) = 1"
            int totalCount = sqlTemplate.count("SELECT COUNT(*) FROM " + tableName);
            RowBounds rowBounds = new RowBounds(totalCount);
            int totalPage = rowBounds.getTotalPage();
            // 这里是按分页批量拉取
            for (int i = 1; i <= totalPage; i++) {
                int offset = rowBounds.getOffset(i);
                String condition = " limit " + offset + "," + RowBounds.page_size;
                // + " WHERE TO_DAYS(NOW()) - TO_DAYS(`create_time`) = 1"
                ResultSet resultSet = sqlTemplate.select("SELECT * FROM " + tableName + condition);
                // 将 ResultSet 转成 R
                ResultSetExtractor<R> extractor = (ResultSetExtractor<R>) new ResultSetExtractor<>(origin);
                List<R> result = extractor.extractData(resultSet, extractor.getClassType());
                // 将 R 转成 T
                List<T> targetResult = convertor.batchConvertFrom(result);
                this.rowObjectManager.pushToQueue(targetResult);
            }
        } catch (Throwable ex) {
            LOGGER.error("error to sync data.", ex);
        }
    }

    /**
     * 分页同步
     */
    private void pageSync() throws SQLException {
        Connection connection = this.originDataSource.getConnection();
        // 1、查原始库的所有数据的条数


    }


    /**
     * 构建 datasource
     * todo 需要将参数补充的丰富些
     * todo 这里应该允许使用不同的连接池技术，是个扩展的机制
     *
     * @param originSource
     * @return
     */
    private HikariDataSource buildDatasource(ConnectorSourceConfigs.InnerSource originSource) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(originSource.getUrl());
        config.setUsername(originSource.getUsername());
        config.setPassword(originSource.getPassword());
        config.setDriverClassName(ConnectorHelper.getDriverClassName(source.getSourceType()));
        config.setMaximumPoolSize(1);
        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }
}
