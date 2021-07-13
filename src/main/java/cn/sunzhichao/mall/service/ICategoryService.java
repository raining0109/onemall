package cn.sunzhichao.mall.service;

import cn.sunzhichao.mall.common.ServerResponse;

public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);
}
