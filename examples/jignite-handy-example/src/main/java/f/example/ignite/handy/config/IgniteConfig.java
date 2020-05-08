package f.example.ignite.handy.config;

import f.jhandy.ignite.cache.CacheInstanceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sunmoonone
 * @version 2019/01/07
 */
@Component
@ConfigurationProperties(prefix="ignite")
public class IgniteConfig extends CacheInstanceProperties {
}
