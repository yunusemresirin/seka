package org.hbrs.seka.uebung1;

import java.util.List;
import java.util.Map;

public interface Caching {
    void cacheResult(String key, List<?> value);
    List<?> getCachedResult(String key);
    Map<String, List<?>> getCacheSnapshot();
//    void removeCachedResult(String key);
//    boolean contains(String key);
//    void clear();
}
