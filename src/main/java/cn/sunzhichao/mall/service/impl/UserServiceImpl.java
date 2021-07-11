package cn.sunzhichao.mall.service.impl;

import cn.sunzhichao.mall.common.Const;
import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.dao.UserMapper;
import cn.sunzhichao.mall.pojo.User;
import cn.sunzhichao.mall.service.IUserService;
import cn.sunzhichao.mall.util.MD5Util;
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

        //使用md5加密后的密码进行查询数据库
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);//密码置空
        return ServerResponse.createBySuccess("登录成功",user);
    }


    public ServerResponse<String> register(User user) {

        int resultCount = userMapper.checkUsername(user.getUsername());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在");
        }

        //设置新用户角色——普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");


    }
}
