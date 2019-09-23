package me.wuwenbin.noteblogv5.util;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 本项目使用的是springboot默认的ConcurrentMapCacheManager
 * created by Wuwenbin on 2018/8/16 at 11:33
 *
 * @author wuwenbin
 */
public class CacheUtils {

    private static final String PARAM_CACHE = "paramCache";

    private static Cache getCache() {
        return NbUtils.getBean(CacheManager.class).getCache(CacheUtils.PARAM_CACHE);
    }

    /**
     * 获取参数缓存对象
     *
     * @return
     */
    public static Cache getParamCache() {
        return getCache();
    }


    public static void putIntoParamCache(Object key, Object value) {
        getParamCache().put(key, value);
    }

    public static <T> T fetchFromParamCache(Object key, Class<T> clazz) {
        return getParamCache().get(key, clazz);
    }

    public static void removeParamCache(Object key) {
        getParamCache().evict(key);
    }

    public static void clearAllParamCache() {
        getParamCache().clear();
    }


}
