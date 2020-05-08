package f.example.ignite.handy;

import f.example.ignite.handy.dao.SimpleDao;
import f.example.ignite.handy.service.SimpleService;
import f.example.ignite.handy.user.model.UserVo;
import f.jhandy.ignite.IgniteBean;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunmoonone
 * @version 2019/01/07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlTest {
    @Autowired
    IgniteBean ignite;

    @Autowired
    SimpleService simpleService;

    @Autowired
    SimpleDao simpleDao;

    @Test
    public void test() {
        //init cache values
        UserVo uv = simpleService.getUserVo(1L);

        //get cache
        IgniteCache cache =ignite.cache("uservoCache");

        FieldsQueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery("select id, name from UserVo where id=1"));
        Iterator<List<?>> iterator = cursor.iterator();
        while (iterator.hasNext()) {
            List<?> row = iterator.next();

            System.out.println(">>>    " + row.get(0) + ", " + row.get(1));
        }

        SqlFieldsQuery updater = new SqlFieldsQuery("update UserVo set name = ? where id = ?");

        cache.query(updater.setArgs("Master", 1));

        uv = simpleService.getUserVo(1L);
        assertThat(uv).isNotNull();
        assertThat(uv.getName()).isEqualTo("Master");

        uv = simpleDao.findById(1L);
        assertThat(uv.getName()).isEqualTo("Master");

    }
}