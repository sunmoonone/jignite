package f.example.ignite.cache.starter;

import f.jhandy.ignite.CacheConfigurationCustomizer;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * @author sunmoonone
 * @version 2018/12/25
 */
@EnableCaching
@SpringBootApplication
public class IgniteSpringCacheTestApplication {
    private static Logger log = LoggerFactory.getLogger(IgniteSpringCacheTestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(IgniteSpringCacheTestApplication.class, args);
    }


    @Bean
    public CacheConfigurationCustomizer customizer(){

        return new CacheConfigurationCustomizer() {
            @Override
            public void customize(CacheConfiguration ccfg) {

                //ccfg.setIndexedTypes(...)
                log.debug("in my customizer for cache {}",ccfg.getName());
            }
        };

    }

}