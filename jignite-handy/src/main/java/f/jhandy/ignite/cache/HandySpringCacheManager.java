package f.jhandy.ignite.cache;

import f.jhandy.ignite.IgniteStartedListener;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.cache.Cache;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunmoonone
 * @version 2018/12/27
 */
public class HandySpringCacheManager extends SpringCacheManager implements IgniteStartedListener {
    private Map<String, CacheConfiguration> cacheCfgs=new HashMap<>();
    private Map<String, Boolean> initialized = new HashMap<>();

    public void addCacheCfg(String name, CacheConfiguration ccfg){
        cacheCfgs.put(name, ccfg);
    }


    @Override
    public Cache getCache(String name) {
        if(initialized.containsKey(name)){
            return super.getCache(name);
        }


        boolean needRestore = false;
        CacheConfiguration dcfg=null;

        if(cacheCfgs.containsKey(name)){
            setDynamicCacheConfiguration(cacheCfgs.get(name));
            needRestore = true;
            dcfg = getDynamicCacheConfiguration();
        }

        Cache cache = super.getCache(name);

        if(needRestore){
            setDynamicCacheConfiguration(dcfg);
        }

        initialized.put(name,true);

        return cache;
    }



    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    @Override
    public void onIgniteStarted(String instanceName) {

        super.onApplicationEvent(null);

    }

}
