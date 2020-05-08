package jhandy.jignite.cache.autoconfigure;

import f.jhandy.ignite.cache.CacheInstanceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author sunmoonone
 * @version 2018/12/24
 */
@ConfigurationProperties("spring.cache.ignite")
public class SubCacheInstanceProperties extends CacheInstanceProperties {
}
