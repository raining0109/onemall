package cn.sunzhichao.mall.service.impl;

import cn.sunzhichao.mall.common.Const;
import cn.sunzhichao.mall.common.ResponseCode;
import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.dao.CartMapper;
import cn.sunzhichao.mall.dao.ProductMapper;
import cn.sunzhichao.mall.pojo.Cart;
import cn.sunzhichao.mall.pojo.Product;
import cn.sunzhichao.mall.service.ICartService;
import cn.sunzhichao.mall.util.BigDecimalUtil;
import cn.sunzhichao.mall.util.PropertiesUtil;
import cn.sunzhichao.mall.vo.CartProductVo;
import cn.sunzhichao.mall.vo.CartVo;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {

        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc()
            );
        }

        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //这个产品不在购物车里，需要新增一个该产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);

            cartMapper.insert(cartItem);
        } else {
            //这个产品已经在购物车里面了
            //如果产品已经存在，数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {

        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc()
            );
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);

        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }


    /**
     * 根据mmall_cart里面的数据，返回购物车全部状态的通用方法
     */
    private CartVo getCartVoLimit(Integer userId) {

        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);

        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        //注意：要使用string的构造器,这个是整个购物车清单的总价
        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartList)) {
            //该遍历的目的是将属于特定用户的Cart对象从数据库中查询出来
            //通过productId从数据库中查询对应的product，结合两个对象，将所有的
            //Cart对象转换成CartProductVo这个联动的对象
            //在这个过程中，除了简单的赋值一下商品的Cart的状态，还要判断库存是否充足
            //如果库存充足，CartProductVo的quantity就是Cart里面的数量
            //如果库存不充足，CartProductVo的quantity就是product的库存量，并且更新Cart对象里面的数量属性
            for (Cart cartItem : cartList) {
                //新建一个CartProductVo对象并把各种信息set上
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    //product不为空，继续组装cartProductVo
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        //产品库存数大于等于购买数量,库存充足
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        //限制
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //更新"购物车"中更新有效库存(注意这里不是更新product!!!)
                        Cart cartForQuantity = new Cart();//专门用于更新quantity的对象
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算这个产品的总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(
                            product.getPrice().doubleValue(),
                            cartProductVo.getQuantity()
                    ));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }

                //如果这个cartItem被勾选了，就计入计算整个购物车清单的总价中
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    cartTotalPrice = BigDecimalUtil.add(
                            cartTotalPrice.doubleValue(),
                            cartProductVo.getProductTotalPrice().doubleValue());
                }

                //将cartProductVo放入到cartProductVoList中
                cartProductVoList.add(cartProductVo);
            }
        }
        //设置最终要返回的整个CartVo对象
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("aliyunoss.filePath"));

        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
}
