package com.idata.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RowMapperFactory {

    public static Map<Class, RowMapper> RowMapper_MAP = new ConcurrentHashMap<>();

    public static RowMapper getRowMapper(Class type) {
        return RowMapper_MAP.get(type);
    }

    public static void register(Class type, RowMapper rowMapper) {
        RowMapper_MAP.putIfAbsent(type, rowMapper);
    }
}
