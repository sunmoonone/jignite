package jhandy.jignite.autoconfigure;

import f.jhandy.ignite.CacheConfigurationCustomizer;
import f.jhandy.ignite.IgniteBean;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSpringBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author sunmoonone
 * @version 2018/12/24
 */
@Configuration
@ConditionalOnClass(IgniteSpringBean.class)
@ConditionalOnMissingBean(Ignite.class)
@EnableConfigurationProperties(IgniteInstanceProperties.class)
public class IgniteAutoConfiguration {

    private IgniteInstanceProperties igniteProperties;
    private boolean runOnce;

    public IgniteAutoConfiguration(IgniteInstanceProperties igniteProperties){
        this.igniteProperties = igniteProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheConfigurationCustomizers buildCustomizer(ObjectProvider<List<CacheConfigurationCustomizer>> customizers){
        return new CacheConfigurationCustomizers(customizers.getIfAvailable());
    }

    @Bean("igniteInstance")
    public IgniteBean igniteInstance(CacheConfigurationCustomizers customizers) throws Exception {
        if(runOnce)return null;
        runOnce = true;

        return new IgniteBean(igniteProperties, customizers);
    }
}
