package cn.sunzhichao.mall.common;

public class Const {

    public static final String CURRENT_USER = "CURRENT_USER";

    //使用内部接口将常量分组
    public interface Role {
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }

}
