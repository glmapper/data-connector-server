package com.idata.common.enums;

/**
 * <p>
 * 数据同步的模式
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/10 4:11 PM
 * @since 1.0
 */
public enum SyncerMode {
    /**
     * 全量同步
     */
    ALL,
    /**
     * 按天同步，即仅同步前一天的
     */
    DAY;
}
