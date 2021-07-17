package cn.sunzhichao.mall.controller.portal;

import cn.sunzhichao.mall.common.Const;
import cn.sunzhichao.mall.common.ResponseCode;
import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.pojo.Shipping;
import cn.sunzhichao.mall.pojo.User;
import cn.sunzhichao.mall.service.IShippingService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 收货地址表的增删改查(CRUD)
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     * 增加地址
     */
    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping) {

        //判断是否登录样板代码
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.add(user.getId(), shipping);
    }

    /**
     * 删除地址
     */
    @RequestMapping(value = "del.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse del(HttpSession session, Integer shippingId) {

        //判断是否登录样板代码
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.del(user.getId(), shippingId);
    }

    /**
     * 修改地址
     */
    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping) {

        //判断是否登录样板代码
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.update(user.getId(), shipping);
    }

    /**
     * 查询地址
     */
    @RequestMapping(value = "select.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Shipping> select(HttpSession session, Integer shippingId) {

        //判断是否登录样板代码
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //判断是否登录样板代码
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc()
            );
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
