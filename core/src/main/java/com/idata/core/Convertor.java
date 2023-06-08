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

    /**
     * 批量转换
     * @param origin
     * @return
     * @throws SQLException
     */
    List<T> batchConvertFrom(List<R> origin) throws SQLException;

    /**
     * 将 R 转换成 T
     *
     * @param target
     * @return
     */
    List<R> batchConvertTo(List<T> target);

    /**
     * 业务类型，现在没用来之前考虑是通过 SPI 机制，在 ConvertorFactory 中直接 load 到所有的 Convertor
     *
     * @return
     */
    String bizType();
}
