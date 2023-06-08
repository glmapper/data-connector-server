package com.idata.connectors.mysql;

import com.idata.common.ConnectorHelper;
import com.idata.common.ConnectorSource;
import com.idata.common.annotations.Table;
import com.idata.common.enums.SourceType;
import com.idata.core.Connector;
import com.idata.core.Convertor;
import com.idata.core.ResultSetExtractor;
import com.idata.core.sql.SqlTemplate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * 这里默认使用 HikariDataSource
 */
public class MysqlConnector<T, R> implements Connector {

    private final ConnectorSource source;

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

    public MysqlConnector(ConnectorSource source, Convertor<T, R> convertor) {
        this.source = source;
        this.convertor = convertor;
        init();
    }

    @Override
    public void init() {
        HikariConfig config = new HikariConfig();
        this.originDataSource = buildDatasource(this.source.getOriginSource());
        this.targetDataSource = buildDatasource(this.source.getTargetSource());
        if (this.convertor == null) {
            throw new UnsupportedOperationException("No plugin support current convert, bizType: " + this.source.getBizType());
        }
    }

    @Override
    public SourceType sourceType() {
        return SourceType.MYSQL57;
    }


    @Override
    public void sync(Object origin, Object target) {
        try {
            // 从原始库中读取数据
            Connection connection = this.originDataSource.getConnection();
            Table declaredAnnotation = origin.getClass().getDeclaredAnnotation(Table.class);
            String tableName = declaredAnnotation.name();
            // todo 这里可以将 SQL 提出去，给出一组模板，比如 1、按照天拉取/每次拉取 100 条 2、按小时拉取，每次拉取100条 --> 按 天/小时 的分页查询
            PreparedStatement preparedStatement = connection.prepareStatement("select * from " + tableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            // 将 ResultSet 转成 R
            ResultSetExtractor<R> extractor = (ResultSetExtractor<R>) new ResultSetExtractor<>(origin.getClass());
            List<R> result = extractor.extractData(resultSet, extractor.getClassType());

            // 将 R 转成 T
            List<T> targetResult = convertor.batchConvertFrom(result);

            // 将 T 写到 目标库
            Connection tc = this.targetDataSource.getConnection();
            SqlTemplate<T> sqlTemplate = new SqlTemplate<>();
            sqlTemplate.setObj(targetResult.get(0));
            String sql = sqlTemplate.createBaseSql();
            PreparedStatement pstm = tc.prepareStatement(sql);

            for (T item : targetResult) {
                Object[] objects = sqlTemplate.createInsertSql(item);
                for (int i = 0; i < objects.length; i++) {
                    pstm.setObject(i, objects[i]);
                }
                pstm.addBatch();
            }
            pstm.executeUpdate();

        } catch (Exception ex) {

        }
    }

    /**
     * 构建 datasource
     * todo 需要将参数补充的丰富些
     * todo 这里应该允许使用不同的连接池技术，是个扩展的机制
     *
     * @param originSource
     * @return
     */
    private HikariDataSource buildDatasource(ConnectorSource.InnerSource originSource) {
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
