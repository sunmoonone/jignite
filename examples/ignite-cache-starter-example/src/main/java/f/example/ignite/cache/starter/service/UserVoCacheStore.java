package f.example.ignite.cache.starter.service;

import f.example.ignite.cache.starter.dao.SimpleDao;
import f.example.ignite.cache.starter.user.model.UserVo;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.resources.SpringResource;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunmoonone
 * @version 2018/12/29
 */
public class UserVoCacheStore extends CacheStoreAdapter<Long, UserVo> implements Serializable {
    @SpringResource(resourceClass=SimpleDao.class)
    private transient SimpleDao simpleDao;


    @Override
    public Map<Long, UserVo> loadAll(Iterable<? extends Long> keys) {
        List<UserVo> ret = simpleDao.selectByIds(keys);
        Map<Long,UserVo> map = new HashMap<>();
        for(UserVo uv: ret){
            map.put(uv.getId(), uv);

        }
        return map;
    }

    @Override
    public UserVo load(Long key) throws CacheLoaderException {
        return simpleDao.findById(key);
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends UserVo> entry) throws CacheWriterException {

        simpleDao.updateById(entry.getKey(), entry.getValue());

    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        simpleDao.delete((Long) key);
    }


    @Override
    public void deleteAll(Collection<?> keys) {
        simpleDao.deleteByIds(keys);
    }
}
