package com.idata.connectors.mysql.page;

import com.idata.common.enums.SyncerMode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * 分页查询组件
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/10 4:19 PM
 * @since 1.0
 */
public class PageCondition<T> {

    private static String SQL_TEMPLATE = "select * from %s";

    private static String SQL_TEMPLATE_COUNT = "select count(*) from %s";
    private final SyncerMode syncerMode;
    private final String tableName;

    private final String baseSelectSql;
    private final String baseCountSelectSql;

    private int pageSize = 100;

    private int total = 0;

    private int currentPage = 0;

    private String timeConditionColName;

    public PageCondition(SyncerMode syncerMode, String tableName, String timeConditionColName) {
        this.syncerMode = syncerMode;
        this.tableName = tableName;
        this.timeConditionColName = timeConditionColName;
        this.baseSelectSql = String.format(SQL_TEMPLATE, this.tableName);
        this.baseCountSelectSql = String.format(SQL_TEMPLATE_COUNT, this.tableName);
    }

    private String buildExecuteSql(boolean isCount) {
        String finalSql = !isCount ? this.baseSelectSql : this.baseCountSelectSql;
        if (syncerMode == SyncerMode.ALL) {
            return finalSql;
        }
        if (syncerMode == SyncerMode.DAY) {
            return finalSql + " WHERE DATEDIFF(" + this.timeConditionColName + ",NOW())=-1;";
        }
        throw new UnsupportedOperationException("no syncerMode matched.");
    }

    /**
     * 查询总数
     *
     * @param pst
     * @return
     * @throws SQLException
     */
    private int getTotal(PreparedStatement pst) throws SQLException {
        ResultSet rs_total = pst.executeQuery(buildExecuteSql(true));
        rs_total.next();
        return rs_total.getInt(1);
    }

    private List<T> queryLimit(PreparedStatement pst) throws SQLException {
        String sql = buildExecuteSql(false);
        sql = sql + " limit " + getOffset() + "," + this.pageSize;
        ResultSet resultSet = pst.executeQuery(sql);
        return null;
    }


    private int getOffset() {
        return (this.currentPage - 1) * this.pageSize;
    }

    private int getTotalPage() {
        return (this.total + this.pageSize - 1) / this.pageSize;
    }


}
