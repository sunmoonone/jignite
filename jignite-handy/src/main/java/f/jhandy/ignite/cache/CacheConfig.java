package f.jhandy.ignite.cache;


import f.jhandy.ignite.cache.store.CacheStoreFactoryConfig;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.eviction.fifo.FifoEvictionPolicyFactory;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicyFactory;
import org.apache.ignite.configuration.*;
import org.springframework.util.ClassUtils;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author sunmoonone
 * @version 2018/12/25
 */
public class CacheConfig {
    final public static String DEFAULT_CACHE_NAME="default";

    private boolean writeBehindEnabled;
    private int writeBehindFlushSize;
    private long writeBehindFlushFrequency;
    private int writeBehindBatchSize;
    private long timeout;
    private boolean onHeap;
    private long maxMemSize;

    private CacheMode cacheMode;

    private EvictionPolicy evictionPolicy;
    private ExpiryPolicy expiryPolicy;
    private CacheStoreFactoryConfig cacheStoreFactory;


    private String indexKeyType;
    private String indexValueType;

    public CacheStoreFactoryConfig getCacheStoreFactory() {
        return cacheStoreFactory;
    }

    public void setCacheStoreFactory(CacheStoreFactoryConfig cacheStoreFactory) {
        this.cacheStoreFactory = cacheStoreFactory;
    }

    public EvictionPolicy getEvictionPolicy() {
        return evictionPolicy;
    }

    public ExpiryPolicy getExpiryPolicy() {
        return expiryPolicy;
    }

    public void setExpiryPolicy(ExpiryPolicy expiryPolicy) {
        this.expiryPolicy = expiryPolicy;
    }

    public void setEvictionPolicy(EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
    }

    public boolean isWriteBehindEnabled() {
        return writeBehindEnabled;
    }

    public void setWriteBehindEnabled(boolean writeBehindEnabled) {
        this.writeBehindEnabled = writeBehindEnabled;
    }

    public int getWriteBehindFlushSize() {
        return writeBehindFlushSize;
    }

    public void setWriteBehindFlushSize(int writeBehindFlushSize) {
        this.writeBehindFlushSize = writeBehindFlushSize;
    }

    public long getWriteBehindFlushFrequency() {
        return writeBehindFlushFrequency;
    }

    public void setWriteBehindFlushFrequency(long writeBehindFlushFrequency) {
        this.writeBehindFlushFrequency = writeBehindFlushFrequency;
    }

    public int getWriteBehindBatchSize() {
        return writeBehindBatchSize;
    }

    public void setWriteBehindBatchSize(int writeBehindBatchSize) {
        this.writeBehindBatchSize = writeBehindBatchSize;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isOnHeap() {
        return onHeap;
    }

    public void setOnHeap(boolean onHeap) {
        this.onHeap = onHeap;
    }

    public long getMaxMemSize() {
        return maxMemSize;
    }

    public void setMaxMemSize(long maxMemSize) {
        this.maxMemSize = maxMemSize;
    }

    public CacheMode getCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }

    public String getIndexKeyType() {
        return indexKeyType;
    }

    public void setIndexKeyType(String indexKeyType) {
        this.indexKeyType = indexKeyType;
    }

    public String getIndexValueType() {
        return indexValueType;
    }

    public void setIndexValueType(String indexValueType) {
        this.indexValueType = indexValueType;
    }

    public CacheConfiguration buildCacheConfiguration(IgniteConfiguration igniteConfiguration, String cacheName) {
        CacheConfiguration ccfg = new CacheConfiguration();
        ccfg.setName(cacheName);
        if(cacheMode!=null){
            ccfg.setCacheMode(cacheMode);
        }

        applyIndexedTypes(ccfg);

        ccfg.setWriteBehindEnabled(writeBehindEnabled);

        if (writeBehindBatchSize > 0)
            ccfg.setWriteBehindBatchSize(writeBehindBatchSize);
        if (writeBehindFlushSize > 0)
            ccfg.setWriteBehindFlushSize(writeBehindFlushSize);
        if (writeBehindFlushFrequency > 0)
            ccfg.setWriteBehindFlushFrequency(writeBehindFlushFrequency);

        applyCacheStoreFactory(ccfg, cacheName);


        applyExpiryPolicy(ccfg);

        ccfg.setOnheapCacheEnabled(onHeap);

        if (onHeap) {
            applyEvictionPolicy(ccfg);
        } else {
            configDataRegion(igniteConfiguration, cacheName);
            if(cacheName.equals(DEFAULT_CACHE_NAME)){
                ccfg.setDataRegionName(DataStorageConfiguration.DFLT_DATA_REG_DEFAULT_NAME);
            }else{
                ccfg.setDataRegionName(cacheName + "_region");
            }
        }

        return ccfg;
    }

    private void applyIndexedTypes(CacheConfiguration ccfg) {
        if(indexKeyType==null || "".equals(indexKeyType) || indexValueType==null || "".equals(indexValueType))
            return;

        ccfg.setIndexedTypes(resolveType(indexKeyType),resolveType(indexValueType));
    }

    private Class<?> resolveType(String className) {
        Class<?> type;
        try {

            if (className.equals("AffinityKey")) {
                type = AffinityKey.class;
            } else {
                type = ClassUtils.forName(className, null);
            }
        }catch (ClassNotFoundException e){
            type = null;
        }

        if(type == null){
            throw new IllegalStateException("applyIndexedTypes unknown class "+className);
        }
        return type;
    }

    private void applyCacheStoreFactory(CacheConfiguration ccfg, String cacheName) {
        if(cacheStoreFactory==null)return;

        ccfg.setWriteThrough(true);
        ccfg.setReadThrough(true);

        ccfg.setCacheStoreFactory(cacheStoreFactory.buildCacheStoreFactory(cacheName));
    }

    private void configDataRegion(IgniteConfiguration igniteConfiguration, String cacheName) {
        DataStorageConfiguration storageCfg = igniteConfiguration.getDataStorageConfiguration();
        if (storageCfg == null) {
            storageCfg = new DataStorageConfiguration();
            igniteConfiguration.setDataStorageConfiguration(storageCfg);
        }
        //no specific region for default cache config
        if(cacheName.equals(DEFAULT_CACHE_NAME))return;

        DataRegionConfiguration regionCfg = new DataRegionConfiguration();

        regionCfg.setName(cacheName + "_region");
        if (maxMemSize > 0) {
            regionCfg.setMaxSize(maxMemSize);
        }
        applyDataRegionEvictionPolicy(regionCfg);

        DataRegionConfiguration[] regions = storageCfg.getDataRegionConfigurations();
        if (regions == null) {
            regions = new DataRegionConfiguration[1];
            regions[0] = regionCfg;
            storageCfg.setDataRegionConfigurations(regions);
        } else {
            DataRegionConfiguration[] newRegions = new DataRegionConfiguration[regions.length + 1];
            System.arraycopy(regions, 0, newRegions, 0, regions.length);
            newRegions[regions.length] = regionCfg;

            storageCfg.setDataRegionConfigurations(newRegions);
        }
    }

    private void applyDataRegionEvictionPolicy(DataRegionConfiguration regionCfg) {
        if (null == evictionPolicy) return;

        if (evictionPolicy.getName().equals(EvictionPolicy.POLICY_LRU)) {
            regionCfg.setPageEvictionMode(DataPageEvictionMode.RANDOM_2_LRU);

        } else if (evictionPolicy.getName().equals(EvictionPolicy.POLICY_NONE)) {
            regionCfg.setPageEvictionMode(DataPageEvictionMode.DISABLED);
        } else {
            throw new IllegalStateException("eviction policy not supported, only lru for off-heap cache");
        }
    }


    private void applyExpiryPolicy(CacheConfiguration ccfg) {
        if(null!=expiryPolicy){
            ccfg.setExpiryPolicyFactory(expiryPolicy.buildFactory());

        }else if (timeout > 0) {
            ccfg.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, timeout)));
        }
    }

    private void applyEvictionPolicy(CacheConfiguration ccfg) {
        if (null == evictionPolicy) return;

        if (evictionPolicy.getName().equals(EvictionPolicy.POLICY_FIFO)) {
            if (evictionPolicy.getMaxMemSize() > 0) {

                ccfg.setEvictionPolicyFactory(new FifoEvictionPolicyFactory<>(
                        evictionPolicy.getMaxEntryCount(), 1, evictionPolicy.getMaxMemSize()));
            } else {
                ccfg.setEvictionPolicyFactory(new FifoEvictionPolicyFactory<>(evictionPolicy.getMaxEntryCount()));
            }
        } else if (evictionPolicy.getName().equals(EvictionPolicy.POLICY_LRU)) {
            if (evictionPolicy.getMaxMemSize() > 0) {
                ccfg.setEvictionPolicyFactory(new LruEvictionPolicyFactory<>(evictionPolicy.getMaxEntryCount(),
                        1, evictionPolicy.getMaxMemSize()));
            } else {
                ccfg.setEvictionPolicyFactory(new LruEvictionPolicyFactory<>(evictionPolicy.getMaxEntryCount()));
            }
        } else if (evictionPolicy.getName().equals(EvictionPolicy.POLICY_NONE)) {

        } else {

            throw new IllegalStateException("Not supported eviction policy " + evictionPolicy.getName());
        }
    }
}
