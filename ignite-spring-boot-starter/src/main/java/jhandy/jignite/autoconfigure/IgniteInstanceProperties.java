package jhandy.jignite.autoconfigure;

import f.jhandy.ignite.cache.CacheInstanceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author sunmoonone
 * @version 2018/12/25
 */
@ConfigurationProperties("ignite")
public class IgniteInstanceProperties extends CacheInstanceProperties {
}
