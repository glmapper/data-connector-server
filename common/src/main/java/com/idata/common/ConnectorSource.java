package com.idata.common;

import com.idata.common.enums.SourceType;
import lombok.Data;

import java.io.FileInputStream;

/**
 * 连接源
 */
@Data
public class ConnectorSource {
    /**
     * 原始源
     */
    private InnerSource originSource;

    /**
     * 目标源
     */
    private InnerSource targetSource;

    private String bizType;

    private SourceType sourceType;

    @Data
    public static class InnerSource {
        private String url;
        private String username;
        private String password;
        private String driverClass;
        private String database;
        private String tableName;
        private FileInputStream stream;
    }
}
