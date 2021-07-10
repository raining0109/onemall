package cn.sunzhichao.mall.service;

import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.pojo.User;

public interface IUserService {

    ServerResponse<User> login(String username, String password);
}
