package f.example.ignite.cache.starter;

import org.apache.ignite.Ignite;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunmoonone
 * @version 2018/12/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheAutoConfigureTest implements ApplicationContextAware {
    private ApplicationContext context;

    @Test
    public void test() {
        assertThat(context.getBean("igniteInstance")).isNotNull();
        Ignite bean = context.getBean(Ignite.class);
//        assertThat(bean.name()).isEqualTo("helloIgnite");

        assertThat(context.getBean(CacheManager.class)).isNotNull();
        assertThat(context.getBean(SpringCacheManager.class)).isNotNull();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}