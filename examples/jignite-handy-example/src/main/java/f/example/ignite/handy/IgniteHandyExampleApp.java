package f.example.ignite.handy;

import f.example.ignite.handy.config.IgniteConfig;
import f.jhandy.ignite.IgniteBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author sunmoonone
 * @version 2019/01/07
 */
@SpringBootApplication
public class IgniteHandyExampleApp {

    public static void main(String[] args) {
        SpringApplication.run(IgniteHandyExampleApp.class, args);
    }


    @Bean
    IgniteBean igniteBean(IgniteConfig igniteConfig) throws Exception {
        IgniteBean bean = new IgniteBean(igniteConfig);
        return bean;
    }


}
