package jhandy.jignite.cache.autoconfigure;

import f.jhandy.ignite.CacheConfigurationCustomizer;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.boot.util.LambdaSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sunmoonone
 * @version 2018/12/28
 */
public class CacheConfigurationCustomizers implements CacheConfigurationCustomizer{
    private List<CacheConfigurationCustomizer> customizers;

    public CacheConfigurationCustomizers(List<? extends CacheConfigurationCustomizer> customizers){
        this.customizers = (customizers != null) ? new ArrayList<>(customizers)
                : Collections.emptyList();
    }


    public void customize(CacheConfiguration ccfg) {
        LambdaSafe.callbacks(CacheConfigurationCustomizer.class, this.customizers, ccfg)
                .withLogger(CacheConfigurationCustomizers.class)
                .invoke((customizer) -> customizer.customize(ccfg));
    }
}
