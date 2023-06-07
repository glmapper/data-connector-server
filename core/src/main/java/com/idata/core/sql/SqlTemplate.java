package com.idata.core.sql;

import com.idata.common.annotations.Colum;
import com.idata.common.annotations.Table;
import lombok.Data;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlTemplate<T> {

    private static Map<Class, Field[]> cacheFieldMap = new ConcurrentHashMap<>();

    private static String SQL = "INSERT INTO %s";

    private T obj;

    public void setObj(T obj) {
        this.obj = obj;
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
            Field field = fields[i];
            Colum filedNameAnno = field.getDeclaredAnnotation(Colum.class);
            String columName = filedNameAnno.name();
            Object value = field.get(this.obj);
            fieldNames[i] = columName;
        }

        SqlModel sqlModel = new SqlModel();
        sqlModel.setFields(fieldNames);
        return buildInsertStatement(insertSql, sqlModel);
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
