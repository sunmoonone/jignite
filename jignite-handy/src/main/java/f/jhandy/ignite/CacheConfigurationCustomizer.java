package f.jhandy.ignite;

import org.apache.ignite.configuration.CacheConfiguration;


public interface CacheConfigurationCustomizer {
    void customize(CacheConfiguration ccfg);
}
