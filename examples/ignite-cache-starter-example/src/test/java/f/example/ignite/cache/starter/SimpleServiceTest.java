package f.example.ignite.cache.starter;

import f.example.ignite.cache.starter.user.model.User;
import f.example.ignite.cache.starter.user.model.UserVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunmoonone
 * @version 2018/12/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleServiceTest {

    private static Logger logger = LoggerFactory.getLogger(SimpleServiceTest.class);

    @Autowired
    SimpleService simpleService;

    @Test
    public void test() {
        int round=2;
        while (round-- >0){
            for(int i=100;i<200;i++){
                User value = simpleService.getUser((long) i);
                logger.debug("value of {} is {}", i, value==null?null:value.getNick());
            }
        }
    }

    @Test
    public void testExpiryPolicy() throws InterruptedException {
        String name = "apple";
        simpleService.setGoodsName(name);
        String gn = simpleService.getGoodsName(1L);
        assertThat(gn).isEqualTo(name);

        //from cache
        gn = simpleService.getGoodsName(1L);
        assertThat(gn).isEqualTo(name);

        //wait for timeout
        Thread.sleep(6000);
        simpleService.setGoodsName("banana");
        //read from store on cache timeout
        gn = simpleService.getGoodsName(1L);
        assertThat(gn).isEqualTo("banana");
    }

    @Test
    public void testCustomCacheStore(){
        UserVo uv = simpleService.getUserVo(1L);

        assertThat(uv).isNotNull();

        uv.setName("hello");
        simpleService.updateUserVo(1L, uv);

        uv = simpleService.getUserVo(1L);
        assertThat(uv.getName()).isEqualTo("hello");
    }
}