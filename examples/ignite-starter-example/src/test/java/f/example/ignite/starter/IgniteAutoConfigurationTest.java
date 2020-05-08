package f.example.ignite.starter;

import org.apache.ignite.Ignite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunmoonone
 * @version 2018/12/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IgniteAutoConfigurationTest implements ApplicationContextAware {
    private ApplicationContext context;

    @Test
    public void test() {
        assertThat(context.getBean("igniteInstance")).isNotNull();
        Ignite ignite = context.getBean(Ignite.class);
        assertThat(ignite.name()).isEqualTo("helloIgnite");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}