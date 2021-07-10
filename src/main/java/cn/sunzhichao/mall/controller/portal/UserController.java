package cn.sunzhichao.mall.controller.portal;

import cn.sunzhichao.mall.common.Const;
import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.pojo.User;
import cn.sunzhichao.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    //这里@ResponseBody，通过@ResponseBody注解告知SpringMVC框架，
    // 方法返回的字符串不是跳转是直接在http响应体中返回。
    //加上mvc配置文件中的 <mvc:annotation-driven> 就可以自动json格式化了
    public ServerResponse login(String username, String password, HttpSession session) {
        //service --> mybatis  --> dao
        //重要的逻辑都在业务层service层
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            //把用户放在session里
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

}
