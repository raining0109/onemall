package cn.sunzhichao.mall.controller.backend;

import cn.sunzhichao.mall.common.Const;
import cn.sunzhichao.mall.common.ResponseCode;
import cn.sunzhichao.mall.common.ServerResponse;
import cn.sunzhichao.mall.pojo.Product;
import cn.sunzhichao.mall.pojo.User;
import cn.sunzhichao.mall.service.IFileService;
import cn.sunzhichao.mall.service.IProductService;
import cn.sunzhichao.mall.service.IUserService;
import cn.sunzhichao.mall.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充新增产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 设置产品的销售状态
     */
    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充设置产品销售状态的业务逻辑
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 获取产品详情
     */
    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //业务逻辑
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 后台获取产品列表
     */
    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //业务逻辑
            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 后台产品搜索
     */
    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //业务逻辑
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 上传图片
     */
    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //业务逻辑

            String path = request.getSession().getServletContext().getRealPath("upload");

            String targetFileName = iFileService.upload(file, path);

            String url = PropertiesUtil.getProperty("aliyunoss.filePath") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);

            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 上传图片
     */
    @RequestMapping(value = "richtext_img_upload.do", method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {

        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        //注意:富文本中的图片上传对于返回值有自己的要求
        //我们使用的是simditor,所以按照simditor的要求返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //业务逻辑

            String path = request.getSession().getServletContext().getRealPath("upload");

            String targetFileName = iFileService.upload(file, path);

            String url = PropertiesUtil.getProperty("aliyunoss.filePath") + targetFileName;

            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            //和前端的约定
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
    }
}
