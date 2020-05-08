package f.example.ignite.handy.dao;

import f.example.ignite.handy.user.model.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author sunmoonone
 * @version 2019/01/02
 */
@Component
public class SimpleDao {
    private static Logger log = LoggerFactory.getLogger(SimpleDao.class);

    Map<Long, UserVo> map;

    {
        map = new HashMap<>();
        map.put(1L, new UserVo(1L, "zhangsan"));
        map.put(2L, new UserVo(2L, "lisi"));
    }

    public List<UserVo> selectByIds(Iterable<? extends Long> keys) {
        log.debug("in selectByIds");

        List<UserVo> list = new ArrayList<>();
        for(Long k: keys){
            if(map.containsKey(k)){
                list.add(map.get(k));
            }
        }
        return list;
    }

    public UserVo findById(Long key) {
        log.debug("in findById");

        return map.get(key);

    }

    public void updateById(Long key, UserVo value) {
        log.debug("in updateById");

        map.put(key,value);
    }

    public void delete(Long key) {

        log.debug("in delete");
        map.remove(key);
    }

    public void deleteByIds(Collection<?> keys) {

        log.debug("in deleteByIds");

        for(Object k:keys){
            map.remove(k);
        }
    }
}
