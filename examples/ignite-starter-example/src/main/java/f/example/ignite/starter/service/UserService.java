package f.example.ignite.starter.service;

import f.example.ignite.starter.model.UserVo;
import f.jhandy.ignite.IgniteBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sunmoonone
 * @version 2019/01/07
 */
@Service
public class UserService {

    @Autowired
    IgniteBean igniteBean;

    public UserVo getUserVo(Long id){
        return (UserVo) igniteBean.cache("uservoCache").get(id);
    }

    public void updateUserVo(Long id, UserVo userVo){
        igniteBean.cache("uservoCache").put(id, userVo);
    }

}
