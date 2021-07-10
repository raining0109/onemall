package cn.sunzhichao.mall.service.impl;

import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.dao.UserMapper;
import cn.sunzhichao.mall.pojo.User;
import cn.sunzhichao.mall.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //todo 密码登录MD5

        User user = userMapper.selectLogin(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);//密码置空
        return ServerResponse.createBySuccess("登录成功",user);
    }
}
