package com.idata.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ResultSetMapper
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/8 8:50 AM
 * @since 1.0
 */
public class ResultSetMapper<T> {

    /**
     * 将 ResultSet 映射到 Java Object
     *
     * @param resultSet
     * @param objectType
     * @return
     * @throws Exception
     */
    public T resultSetToJavaObject(ResultSet resultSet, Class<T> objectType) throws Exception {
        Map<String, Object> resultSetMap = resultSetToMap(resultSet);
        return mapResultSetToObject(resultSetMap, objectType);
    }

    /**
     * 将 map 转换成 object
     *
     * @param resultMap
     * @param objectType
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private T mapResultSetToObject(Map<String, Object> resultMap, Class<T> objectType) throws Exception {
        T object = objectType.newInstance();

        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();

            try {
                objectType.getDeclaredField(fieldName).set(object, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return object;
    }

    /**
     * 将 resultSet 转成 Map
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Map<String, Object> resultSetToMap(ResultSet resultSet) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        // 获取 ResultSet 的元数据
        int columnCount = resultSet.getMetaData().getColumnCount();

        // 遍历每一列，将列名和值存储到 Map 中
        for (int i = 1; i <= columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i);
            Object value = resultSet.getObject(i);
            resultMap.put(columnName, value);
        }
        return resultMap;
    }
}
