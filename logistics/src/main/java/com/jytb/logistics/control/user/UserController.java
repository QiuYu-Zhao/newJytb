package com.jytb.logistics.control.user;


import co.chexiao.base.contract.enums.common.ResultEnum;
import co.chexiao.common.util.StringUtil;
import com.jytb.logistics.bean.common.User;
import com.jytb.logistics.bean.role.Role;
import com.jytb.logistics.bean.vo.UserVO;
import com.jytb.logistics.control.ControllerTool;
import com.jytb.logistics.service.user.IUserService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyz on 2019/06/08.
 */
@Controller
@RequestMapping("mk/admin/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "list", produces = "text/html;charset=UTF-8")
    public String list(ModelMap model) {
        return "user/user_list";
    }

    /**
     * 通过省的id查询所属该省的市
     * @param aoData
     * @param request
     * @return
     */
    @RequestMapping(value = "list/page", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String listPage(@RequestParam String aoData,HttpServletRequest request) {
        ControllerTool.Page page = new ControllerTool().createPage(aoData);

        int totalCount = 0;
        List<UserVO> userVOList = new ArrayList<>();
        try {
            StringBuilder condition = new StringBuilder("1=1");

            String userName = StringUtil.g(request.getParameter("userName"));
            String routeName = StringUtil.g(request.getParameter("routeName"));


            if (!StringUtil.isEmpty(userName)) {
                condition.append(" AND username like'").append(userName).append("%' ");
            }

            if (!StringUtil.isEmpty(routeName)) {
                condition.append(" AND route_name like'").append(routeName).append("%' ");
            }


            totalCount = userService.getCountByCondition(condition.toString());
            if(totalCount > 0){
                List<User> userList = userService.getListByPage(condition.toString(),page.getCurrentPage(),page.iDisplayLength,"id desc");
                for(User user : userList){
                    userVOList.add(new UserVO(user));
                }
            }
        } catch (Exception e) {
            logger.error("查询用户工单列表出错", e);
        }
        JSONObject getObj = new JSONObject();
        getObj.put("sEcho", page.sEcho);
        getObj.put("iTotalRecords", totalCount);
        getObj.put("iTotalDisplayRecords", totalCount);

        getObj.put("aaData", userVOList);
        return getObj.toString();

    }

    @RequestMapping(value = "toCreate", produces = "text/html;charset=UTF-8")
    public String toCreate(ModelMap model) {
        return "user/user_create";
    }

    @RequestMapping(value = "checkExist", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String checkExist(@RequestParam String username) {

        User user = userService.findByUsername(username);
        JSONObject resultObj = new JSONObject();
        if (user == null) {
            resultObj.put("code", 1);
        } else {
            resultObj.put("code", 0);
        }
        return resultObj.toString();
    }

    @RequestMapping("create")
    public String create(User user, @RequestParam String role) {
        logger.info("user ==" + user.getUsername());

        try {
            if (!StringUtil.isEmpty(role)) {
                Role r = new Role();
                r.setRole(role);
                user.getRoleList().add(r);
            }
            userService.insert(user);
        } catch (Exception e) {
            logger.error("新增线路失败", e);
        }
        return "redirect:/mk/admin/user/list.html";
    }

    /**
     * 进入查看或编辑页面
     * @param model
     * @param userId
     * @param type 1.查看，2.编辑
     * @return
     */
    @RequestMapping(value = "toView", produces = "text/html;charset=UTF-8")
    public String toView(ModelMap model,@RequestParam long userId,@RequestParam String type) {
        try {
            User user = userService.findById(userId);
            if(user != null){
                UserVO vo = new UserVO(user);
                model.addAttribute("userModel",vo);
            }
        }catch (Exception e){
            logger.error("查看用户信息错误，",e);
        }
        //查看
        if(type.equals("1")){
            return "user/user_view";
        }else { //编辑
            return "user/user_update";
        }
    }

    @RequestMapping("update")
    public String update(ModelMap model, User updateUser, @RequestParam String role) {
        try {
            if (!StringUtil.isEmpty(role)) {
                Role r = new Role();
                r.setRole(role);
                updateUser.getRoleList().add(r);
            }

            userService.update(updateUser);
        } catch (Exception e) {
            logger.error("更新用户失败", e);
        }

        return "redirect:/mk/admin/user/list.html";
    }

    @RequestMapping(value = "delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String delete(HttpServletRequest request, @RequestParam long userId) {
        JSONObject result = new JSONObject();
        int code = ResultEnum.SUCCESS.getCode();
        String errormsg = "";
        try {
            userService.deleteByID(userId);
        }catch (Exception e){
            logger.error("删除用户错误，",e);
            code = ResultEnum.FAILED.getCode();
            errormsg = "删除用户失败，请联系管理员";
        }
        result.put("code",code);
        result.put("errormsg",errormsg);
        return result.toString();
    }
}
