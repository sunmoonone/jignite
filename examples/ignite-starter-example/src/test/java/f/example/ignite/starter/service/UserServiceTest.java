package f.example.ignite.starter.service;

import f.example.ignite.starter.model.UserVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunmoonone
 * @version 2019/01/07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void test() {

        UserVo uv = userService.getUserVo(1L);

        assertThat(uv).isNotNull();

        uv.setName("hello");
        userService.updateUserVo(1L, uv);

        uv = userService.getUserVo(1L);
        assertThat(uv.getName()).isEqualTo("hello");
    }
}