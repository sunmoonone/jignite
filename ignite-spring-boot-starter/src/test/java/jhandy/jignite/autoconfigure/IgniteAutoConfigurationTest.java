package jhandy.jignite.autoconfigure;

import org.apache.ignite.Ignite;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author sunmoonone
 * @version 2018/12/25
 */
public class IgniteAutoConfigurationTest {

    @Test
    public void testAutoConfigIgnite(){
        ApplicationContextRunner contextRunner = load("ignite.igniteInstanceName","helloIgnite");
        contextRunner.withPropertyValues("ignite.igniteInstanceName","helloIgnite").run(context -> {

            assertThat(context).hasSingleBean(Ignite.class);
            assertThat(context.getBean("igniteInstance")).isNotNull();
            Ignite bean = context.getBean(Ignite.class);
            assertThat(bean.name()).isEqualTo("helloIgnite");
        });

    }


    private ApplicationContextRunner load(String... environment) {
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(IgniteAutoConfiguration.class));

        contextRunner.withPropertyValues(environment);

        return contextRunner;

    }
}
