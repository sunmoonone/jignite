package jhandy.jignite.cache.autoconfigure;

import f.jhandy.ignite.CacheConfigurationCustomizer;
import f.jhandy.ignite.IgniteBean;
import f.jhandy.ignite.cache.CacheInstanceProperties;
import f.jhandy.ignite.cache.HandySpringCacheManager;
import jhandy.jignite.autoconfigure.IgniteAutoConfiguration;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author sunmoonone
 * @version 2018/12/24
 */
@Configuration
@ConditionalOnClass(SpringCacheManager.class)
@ConditionalOnMissingBean(CacheManager.class)
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "ignite")
@EnableConfigurationProperties(SubCacheInstanceProperties.class)
@AutoConfigureBefore({CacheAutoConfiguration.class, IgniteAutoConfiguration.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class SpringCacheAutoConfiguration {

    private final CacheInstanceProperties cacheInstanceProperties;
    private boolean runOnce;

    public SpringCacheAutoConfiguration(CacheInstanceProperties cacheInstanceProperties) {
        this.cacheInstanceProperties = cacheInstanceProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManagerCustomizers cacheManagerCustomizers(
            ObjectProvider<List<CacheManagerCustomizer<?>>> customizers) {
        return new CacheManagerCustomizers(customizers.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheConfigurationCustomizers buildCustomizer(ObjectProvider<List<CacheConfigurationCustomizer>> customizers) {
        return new CacheConfigurationCustomizers(customizers.getIfAvailable());
    }

    @Bean("igniteInstance")
    public IgniteBean igniteInstance(CacheConfigurationCustomizers cfgCustomizers) throws Exception {
        if (runOnce) return null;
        runOnce = true;

        return new IgniteBean(cacheInstanceProperties, cfgCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringCacheManager buildCacheManager(IgniteBean igniteBean, CacheManagerCustomizers managerCustomizers, CacheConfigurationCustomizers cfgCustomizers) {

        HandySpringCacheManager cacheManager = new HandySpringCacheManager();
        cacheManager.setIgniteInstanceName(cacheInstanceProperties.getIgniteInstanceName());

        igniteBean.addStartedListeners(cacheManager);

        for (Map.Entry<String, CacheConfiguration> item : igniteBean.getCacheConfigs().entrySet()) {
            if (item.getKey().equals("default")) continue;

            cacheManager.addCacheCfg(item.getKey(), item.getValue());
        }

        CacheConfiguration defaultCfg = igniteBean.buildCacheConfiguration("default", cacheInstanceProperties.getDefaultCacheConfig(), cfgCustomizers);

        cacheManager.setDynamicCacheConfiguration(defaultCfg);

        return managerCustomizers.customize(cacheManager);
    }

}
