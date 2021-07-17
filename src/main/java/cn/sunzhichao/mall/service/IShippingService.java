package cn.sunzhichao.mall.service;

import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.pojo.Shipping;
import com.github.pagehelper.PageInfo;

public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse del(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
