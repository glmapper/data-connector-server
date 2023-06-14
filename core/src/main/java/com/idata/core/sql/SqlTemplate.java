package com.idata.core.sql;

import com.idata.common.annotations.Colum;
import com.idata.common.annotations.Table;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlTemplate<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlTemplate.class);

    private static Map<Class, Field[]> cacheFieldMap = new ConcurrentHashMap<>();

    private static String SQL = "INSERT INTO %s";

    private final DataSource dataSource;

    public SqlTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private T obj;

    public void setObj(T obj) {
        this.obj = obj;
    }

    /**
     * 查询条数
     *
     * @param sql
     * @return
     */
    public int count(String sql) throws SQLException {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            LOGGER.error("error to count, sql: " + sql, e);
            return 0;
        } finally {
            // 这里需要释放链接
            if (connection != null) {
                connection.close();
            }
        }
    }

    public ResultSet select(String sql) throws SQLException {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            LOGGER.error("error to execute select, sql: " + sql, e);
            return null;
        } finally {
            // 这里不能关闭，因为 RS 还没有处理，如果这里关闭可能会抛出 Operation not allowed after ResultSet closed 异常
        }
    }

    /**
     * 创建出 insert into sql 语句
     *
     * @return
     * @throws Exception
     */
    public String createBaseSql() throws Exception {
        String insertSql = SQL;
        Table declaredAnnotation = obj.getClass().getDeclaredAnnotation(Table.class);
        if (declaredAnnotation != null) {
            String tableName = declaredAnnotation.name();
            insertSql = String.format(SQL, tableName);
            System.out.println(insertSql);
        }
        Field[] fields = null;
        if (cacheFieldMap.containsKey(this.obj.getClass())) {
            fields = cacheFieldMap.get(this.obj.getClass());
        } else {
            fields = this.obj.getClass().getDeclaredFields();
        }
        // 可以用缓存, Class -> Fields
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            System.out.println("-----" + i);
            Field field = fields[i];
            Colum filedNameAnno = field.getDeclaredAnnotation(Colum.class);
            if (filedNameAnno == null) {
                continue;
            }
            String columName = filedNameAnno.name();
            fieldNames[i] = columName;
        }
        SqlModel sqlModel = new SqlModel();
        sqlModel.setFields(fieldNames);
        String pstmSql = buildInsertStatement(insertSql, sqlModel);
        System.out.println(pstmSql);
        return pstmSql;

    }

    /**
     * 获取对象的值列表
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public Object[] createInsertSql(T obj) throws Exception {
        Field[] fields = null;
        if (cacheFieldMap.containsKey(this.obj.getClass())) {
            fields = cacheFieldMap.get(this.obj.getClass());
        } else {
            fields = this.obj.getClass().getDeclaredFields();
        }
        Object[] fieldValues = new Object[fields.length];

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Object value = field.get(this.obj);
            fieldValues[i] = value;
        }

        return fieldValues;
    }

    private String buildInsertStatement(String insertSql, SqlModel sqlModel) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append(insertSql).append(" (");

        boolean isFirstField = true;
        String[] fieldNames = sqlModel.getFields();
        for (String fieldName : fieldNames) {
            if (!isFirstField) {
                sb.append(", ");
            }
            sb.append(fieldName);
            isFirstField = false;
        }

        sb.append(") VALUES (");


        for (int i = 0; i < fieldNames.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(")");
        System.out.println(fieldNames);
        ;
        return sb.toString();
    }


    @Data
    public static class SqlModel {
        private String sql;
        private Map<String, Object> colFieldsVal;
        private String[] fields;
        private Object[] values;
    }
}
