package f.jhandy.ignite;

import f.jhandy.ignite.cache.CacheConfig;
import f.jhandy.ignite.cache.CacheInstanceProperties;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.lang.Nullable;

import java.util.*;

/**
 * @author sunmoonone
 * @version 2018/12/22
 */
public class IgniteBean extends IgniteSpringBean {

    private Map<String, CacheConfiguration> cacheCfgs = new HashMap<>();
    private Map<String, Boolean> initialized = new HashMap<>();

    private List<IgniteStartedListener> startedListeners;


    public IgniteBean() {

    }

    /**
     * 根据属性信息构造igniteConfiguration，然后构造ignite实例
     * @param igniteProperties
     * @throws Exception
     */
    public IgniteBean(IgniteProperties igniteProperties) throws Exception {
        IgniteConfiguration igniteConfiguration = igniteProperties.buildIgniteConfiguration();
        setConfiguration(igniteConfiguration);
    }

    /**
     * 根据实例属性信息和缓存属性信息构造igniteConfiguration 和　cacheConfigurations，然后构造ignite实例
     * @param cacheInstanceProperties
     * @throws Exception
     */
    public IgniteBean(CacheInstanceProperties cacheInstanceProperties, CacheConfigurationCustomizer customizer) throws Exception {
        IgniteConfiguration igniteConfiguration = cacheInstanceProperties.buildIgniteConfiguration();

        setConfiguration(igniteConfiguration);

        for (Map.Entry<String, CacheConfig> item : cacheInstanceProperties.getCaches().entrySet()) {
            if (item.getKey().equals("default")) continue;

            CacheConfiguration ccfg = item.getValue().buildCacheConfiguration(igniteConfiguration, item.getKey());
            if(customizer!=null){
                customizer.customize(ccfg);
            }

            addCacheCfg(item.getKey(),ccfg);
        }
    }

    public IgniteBean(CacheInstanceProperties cacheInstanceProperties) throws Exception {
        this(cacheInstanceProperties,null);
    }

    public void addCacheCfg(String name, CacheConfiguration ccfg) {

        cacheCfgs.put(name, ccfg);
    }

    public Map<String, CacheConfiguration> getCacheConfigs(){
        return Collections.unmodifiableMap(cacheCfgs);
    }

    @Override
    public <K, V> IgniteCache<K, V> cache(String name) {
        if(initialized==null || initialized.containsKey(name)){
            return super.cache(name);
        }

        if(cacheCfgs.containsKey(name)){
            IgniteCache ret= super.getOrCreateCache(cacheCfgs.get(name));
            initialized.put(name,true);
            return ret;
        }
        return super.cache(name);
    }


    public void addStartedListeners(IgniteStartedListener listener){
        if(startedListeners==null){
            startedListeners=new ArrayList<>();
        }
        startedListeners.add(listener);
    }


    @Override
    public void afterSingletonsInstantiated(){
        super.afterSingletonsInstantiated();

        if(null!=this.startedListeners){
            for(IgniteStartedListener listener:startedListeners){
                listener.onIgniteStarted(name());
            }
        }
    }

    public CacheConfiguration buildCacheConfiguration(String name, CacheConfig cacheConfig, @Nullable CacheConfigurationCustomizer customizer) {
        CacheConfiguration ccfg = cacheConfig.buildCacheConfiguration(this.getConfiguration(),name);

        if(customizer!=null){
            customizer.customize(ccfg);
        }
        return ccfg;
    }
}
