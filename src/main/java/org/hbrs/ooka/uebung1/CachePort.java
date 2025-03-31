package org.hbrs.ooka.uebung1;


public class CachePort {
    private static CachePort instance;
    private Cache cache;

    private CachePort() {
    }

    public static CachePort getInstance() {
        if (instance == null) {
            instance = new CachePort();
        }
        return instance;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
