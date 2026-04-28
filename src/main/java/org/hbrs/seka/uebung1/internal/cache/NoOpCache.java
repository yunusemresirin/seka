package org.hbrs.seka.uebung1.internal.cache;

import org.hbrs.seka.uebung1.Caching;

import java.util.List;
import java.util.Map;

public class NoOpCache implements Caching {

    @Override
    public void cacheResult(String key, List<?> value) {

    }

    @Override
    public List<?> getCachedResult(String key) {
        return null;
    }

    @Override
    public Map<String, List<?>> getCacheSnapshot() {
        return null;
    }

//    @Override
//    public void removeCachedResult(String name) {
//
//    }
//
//    @Override
//    public boolean contains(String key) {
//        return false;
//    }
//
//    @Override
//    public void clear() {
//
//    }
}
