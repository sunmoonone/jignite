package f.example.ignite.cache.starter;

import f.example.ignite.cache.starter.user.model.User;
import f.example.ignite.cache.starter.user.model.UserVo;
import f.jhandy.ignite.IgniteBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author sunmoonone
 * @version 2018/12/25
 */
@Service
public class SimpleService {
    private static Logger logger = LoggerFactory.getLogger(SimpleService.class);
    @Autowired
    IgniteBean ignite;

    /**
     * 经测试，仅仅通过注解,在缓存没有对应数据的情况下, ignite会自动从ｄｂ加载数据,缓存有数据的情况下直接返回
     * 方法提不需要读取逻辑
     * @param id
     * @return
     */
    @Cacheable(value = "userCache",key = "#id")
    public User getUser(Long id){
//        return (User) ignite.cache("userCache").get(id);
        return null;
    }


    private String goodsName;

    public void setGoodsName(String name){
        goodsName = name;
    }

    @Cacheable(value ="goodsCache",key="#id")
    public String getGoodsName(Long id){
        logger.debug("get from backend store");
        return goodsName;
    }


    public UserVo getUserVo(Long id){

        return (UserVo) ignite.cache("uservoCache").get(id);

    }

    public void updateUserVo(Long id, UserVo userVo){
        ignite.cache("uservoCache").put(id, userVo);
    }

}
