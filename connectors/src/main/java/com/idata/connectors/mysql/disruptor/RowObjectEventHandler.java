package com.idata.connectors.mysql.disruptor;

import com.idata.core.sql.SqlTemplate;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * RowObjectEventHandler 这里用来消费，并且将队列中的数据写到目标库
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/13 10:32 AM
 * @since 1.0
 */
public class RowObjectEventHandler<T> implements EventHandler<RowObjectEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RowObjectEventHandler.class);

    private DataSource targetDataSource;

    public RowObjectEventHandler(DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
    }

    @Override
    public void onEvent(RowObjectEvent rowObjectEvent, long sequence, boolean endOfBatch) {
        // 这里需要具体处理数据的写
        List<T> targetResult = (List) rowObjectEvent.getRowObject();
        // 将 T 写到 目标库
        Connection tc = null;
        PreparedStatement pstm = null;
        try {
            tc = this.targetDataSource.getConnection();
            SqlTemplate<T> sqlTemplate = new SqlTemplate<>(this.targetDataSource);
            sqlTemplate.setObj(targetResult.get(0));
            String sql = sqlTemplate.createBaseSql();
            pstm = tc.prepareStatement(sql);
            for (T item : targetResult) {
                Object[] objects = sqlTemplate.createInsertSql(item);
                for (int i = 1; i <= objects.length; i++) {
                    if (objects[i - 1] instanceof Long) {
                        objects[i - 1] = (Long) objects[i - 1] + 1;
                    }
                    pstm.setObject(i, objects[i - 1]);
                }
                pstm.addBatch();
            }
            pstm.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("failed to execute pstm", e);
        } finally {
            // 释放连接给连接池 tc.close 会释放 PreparedStatement
            if (tc != null) {
                try {
                    tc.close();
                } catch (SQLException e) {
                    LOGGER.error("failed to close connection", e);
                }
            }
        }

    }
}
