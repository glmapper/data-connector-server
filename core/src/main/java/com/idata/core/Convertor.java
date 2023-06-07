package com.idata.core;

import java.sql.SQLException;
import java.util.List;

public interface Convertor<T, R> {

    /**
     * 将 T 转换成 R
     *
     * @param origin
     * @return
     */
    T convertFrom(R origin) throws SQLException;

    /**
     * 将 R 转换成 T
     *
     * @param target
     * @return
     */
    R convertTo(T target);

    List<T> batchConvertFrom(List<R> origin) throws SQLException;

    /**
     * 将 R 转换成 T
     *
     * @param target
     * @return
     */
    List<R> batchConvertTo(List<T> target);

    String bizType();
}
