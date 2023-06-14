package com.idata.connectors.mysql.disruptor;

import java.util.List;

/**
 * <p>
 * RowObjectEvent
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/13 10:31 AM
 * @since 1.0
 */
public class RowObjectEvent<T> {
    private volatile List<T> rowObject;

    public List<T> getRowObject() {
        return rowObject;
    }

    public void setRowObject(List<T> rowObject) {
        this.rowObject = rowObject;
    }
}
