package cn.sunzhichao.mall.service;

import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.vo.CartVo;

public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
}
