package com.idata.core;

import com.idata.common.enums.SourceType;

public interface Connector<T,R> {
    /**
     * 用于初始化资源
     */
    void init();

    /**
     * 用于确定当前 Connector 的类型
     * @return
     */
    SourceType sourceType();

    void sync(Class<T> origin, Class<R> target);
}
