package com.idata.common;

import com.idata.common.enums.SourceType;

public class ConnectorHelper {

    /**
     * todo
     * @param sourceType
     * @return
     */
    public static String getDriverClassName(SourceType sourceType) {
        if (sourceType == SourceType.MYSQL57) {
            return "com.mysql.jdbc.Driver";
        }

        return null;
    }
}
