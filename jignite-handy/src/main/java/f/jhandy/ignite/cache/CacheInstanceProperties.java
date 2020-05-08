package f.jhandy.ignite.cache;

import f.jhandy.ignite.IgniteProperties;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sunmoonone
 * @version 2018/12/24
 */
public class CacheInstanceProperties extends IgniteProperties {

    private Map<String, CacheConfig> caches;

    public List<String> getCacheNames() {
        if (null != caches) {
            return caches.keySet().stream().filter(s -> !s.equals("default")).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public Map<String, CacheConfig> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, CacheConfig> caches) {
        this.caches = caches;
    }

    public CacheConfiguration buildDefaultCacheConfiguration(IgniteConfiguration configuration) {
        CacheConfig config = getDefaultCacheConfig();
        return config.buildCacheConfiguration(configuration, CacheConfig.DEFAULT_CACHE_NAME);
    }

    public CacheConfig getDefaultCacheConfig(){
        CacheConfig config;
        if (caches.containsKey(CacheConfig.DEFAULT_CACHE_NAME)) {
            config = caches.get(CacheConfig.DEFAULT_CACHE_NAME);
        } else {
            config = new CacheConfig();
            config.setEvictionPolicy(new EvictionPolicy(EvictionPolicy.POLICY_LRU));
        }
        return config;
    }

}
