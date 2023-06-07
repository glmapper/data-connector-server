package com.idata.core;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这个不需要了，Convertor 通过在构建 Syncer 时完成构建和初始化
 */
@Deprecated
public class ConvertorFactory {

    public static Map<String, Convertor> C_MAP = new ConcurrentHashMap<>();

    public static void register(String type, Convertor convertor) {
        C_MAP.putIfAbsent(type, convertor);
    }

    public static Convertor getConvertor(String type) {
        return C_MAP.get(type);
    }

    /**
     * 返回所有的注册的插件类型
     *
     * @return
     */
    public static Set<String> types() {
        return C_MAP.keySet();
    }

    /**
     * 通过 SPI 机制将所有插件实现的 Convertor 实例全部注册进来
     */
    static {
        ServiceLoader<Convertor> serviceLoader = ServiceLoader.load(Convertor.class);
        Iterator<Convertor> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            Convertor convertor = iterator.next();
            register(convertor.bizType(), convertor);
        }
    }
}
