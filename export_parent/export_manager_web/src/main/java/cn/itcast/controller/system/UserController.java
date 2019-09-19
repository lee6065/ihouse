package cn.itcast.controller.system;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;
    @RequestMapping(value="/list" ,name="进入用户管理页面")
    public String findAll(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<User> pageInfo = userService.findPage(companyId,pageNum,pageSize);
        request.setAttribute("page",pageInfo);
        return "system/user/user-list";
    }

    @RequestMapping(value="/toAdd" ,name="进入用户新增页面")
    public String toAdd(){
//        查询所有本用户的部门数据
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);
        return "system/user/user-add";
    }

    @RequestMapping(value="/edit" ,name="保存用户方法")
    public String edit(User user){

 /* `create_by` varchar(40) DEFAULT NULL COMMENT '登录人编号',
  `create_dept` varchar(40) DEFAULT NULL COMMENT '登录人所属部门编号',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(40) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,*/

//        user的id如果为空就是新增，反之就是修改
        if(StringUtils.isEmpty(user.getId())){ //id为空
            //        新增时需要赋id
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setCreateBy(userId);
            user.setCreateDept(deptId);
            user.setCreateTime(new Date());
            user.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id
            userService.save(user);
        }else {  //id不为空
            user.setUpdateBy(userId);
            user.setUpdateTime(new Date());
            userService.update(user);
        }

        return "redirect:/system/user/list.do";  //重定向到列表数据
    }
    @RequestMapping(value="/toUpdate" ,name="进入修改保存页面")
    public String toUpdate(String id){
//         根据id查询用户
        User user = userService.findById(id);
//        把user放入request域中
        request.setAttribute("user",user);

        //        查询所有本用户的部门数据
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);
//        修改页面和新增页面公用一套
        return "system/user/user-add";
    }


    @RequestMapping(value="/delete" ,name="删除用户方法")
    public String delete(String id){
        userService.delete(id);
        return "redirect:/system/user/list.do";  //重定向到列表数据
    }
    @RequestMapping(value="/roleList" ,name="进入用户分配角色页面")
    public String roleList(String id){
        User user = userService.findById(id);
        request.setAttribute("user",user);

//        查询所有的角色
       List<Role> roleList = roleService.findAll(companyId);
        request.setAttribute("roleList",roleList);


//        查询此用户下的角色，把每个角色的id拼成一个字符串
       List<Role> roleListByUser =  roleService.findByUserid(id);

//       List<String> roleIds = roleService.findRoleIdsByUserId(id);

       StringBuffer sb = new StringBuffer("");
        for (Role role : roleListByUser) {
            sb.append(role.getId()).append(",");
        }

        request.setAttribute("userRoleStr",sb.toString());

        return "system/user/user-role";
    }

    @RequestMapping(value="/changeRole" ,name="用户分配角色方法")
    public String changeRole(String userid,String[] roleIds){
        userService.changeRole(userid,roleIds);

        return "redirect:/system/user/list.do";  //重定向到列表数据
    }



}
