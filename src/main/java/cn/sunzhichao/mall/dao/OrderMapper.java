package cn.sunzhichao.mall.dao;

import cn.sunzhichao.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    List<Order> selectByUserId(Integer userId);

    Order selectByOrderNo(Long orderNo);

    //注意是管理员后台查询所有订单，不用传入参数
    List<Order> selectAllOrder();

}