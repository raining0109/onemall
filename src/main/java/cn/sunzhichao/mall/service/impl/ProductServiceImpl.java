package cn.sunzhichao.mall.service.impl;

import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.dao.ProductMapper;
import cn.sunzhichao.mall.pojo.Product;
import cn.sunzhichao.mall.service.IProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 新增或更新产品,通过前端传来的productId是否为空判断
     */
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {

            //如果子图不为空的话，就取第一个子图做为主图
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            //如果productId是空，则说明该产品是新增的，如果不为空，则是更新
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        } else {
            return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
        }
    }
}
