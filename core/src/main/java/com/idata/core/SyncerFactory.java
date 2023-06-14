package com.idata.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SyncerFactory {

    private static Map<String, Syncer> SYNC_MAP = new ConcurrentHashMap<>();

    public static Syncer getSyncer(String type) {
        return SYNC_MAP.get(type);
    }

    public static void register(String type, Syncer syncer) {
        SYNC_MAP.put(type, syncer);
    }
}
