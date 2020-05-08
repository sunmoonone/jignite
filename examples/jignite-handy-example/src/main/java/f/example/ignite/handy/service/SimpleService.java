package f.example.ignite.handy.service;

import f.example.ignite.handy.user.model.UserVo;
import f.jhandy.ignite.IgniteBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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



    public UserVo getUserVo(Long id){

        return (UserVo) ignite.cache("uservoCache").get(id);

    }

    public void updateUserVo(Long id, UserVo userVo){
        ignite.cache("uservoCache").put(id, userVo);
    }

}
