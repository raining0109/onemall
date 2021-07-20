package cn.sunzhichao.mall.service;

import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.vo.OrderVo;

public interface IOrderService {

    ServerResponse<OrderVo> createOrder(Integer userId, Integer shippingId);
}
