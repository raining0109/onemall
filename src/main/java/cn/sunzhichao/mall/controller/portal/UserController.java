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
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
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
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 检查用户名或邮箱是否已经存在，供前端实时调用检验用户填写的字段
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户登录信息
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }

    /**
     * 获取密码提示问题，用于找回密码
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 检验用户提交的密码提示问题答案是否正确
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 重置密码，需要从前端获取token与缓存中的比较是否一致，防止横向越权
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken) {
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

}
