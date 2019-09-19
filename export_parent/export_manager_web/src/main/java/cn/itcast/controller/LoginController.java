package cn.itcast.controller;


import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController{

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;
    @Autowired
    private ModuleService moduleService;

	@RequestMapping(value = "/login",name = "用户登录")
	public String login(String email, String password) {
//	    判断邮箱或密码是否为空
        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)){
//            跳转到登录页面，带一句话“邮箱或密码不能为空”
            request.setAttribute("error","邮箱或密码不能为空");
//            return "redirect:/login.jsp"; //重定向
            return "forward:/login.jsp"; //请求转发
        }
//        shiro的认证=登录
//        1、创建令牌
        password = new Md5Hash(password,email, 10).toString();//p1: 原密码  p2:盐 料  p3:散次列 加几次
        UsernamePasswordToken token = new UsernamePasswordToken(email,password);
//        2、获取主题
        Subject subject = SecurityUtils.getSubject();
//        3、开始认证
        try {
            subject.login(token);  //AuthenticationToken
        } catch (AuthenticationException e) {
            request.setAttribute("error","邮箱或密码输入有误");
            return "forward:/login.jsp"; //请求转发
        }

/*//        根据邮箱查询用户
        User user = userService.findByEmail(email);
        if(user==null){//         有可能查不到，说明邮箱错误
            request.setAttribute("error","邮箱输入有误");
            return "forward:/login.jsp"; //请求转发
        }
//     如果查到了 根据用户中的密码和页面上传过来的密码做比较
        String password_db = user.getPassword();
//        把页面上传过来的密码通过和新增用户同样的加密方式加密
        password = new Md5Hash(password,email, 10).toString();//p1: 原密码  p2:盐 料  p3:散次列 加几次
        if(!password_db.equals(password)){
            request.setAttribute("error","密码输入有误");
            return "forward:/login.jsp"; //请求转发
        }
//                        如果比较成功直接跳转主页面
//                        如果没有成功就是密码错误，调回到登录页面

//     把用户信息放入到session中
        session.setAttribute("loginUser",user);*/
        User user = (User) subject.getPrincipal(); //直接从SecurityManger中获取了当前登录人

        session.setAttribute("loginUser",user);
//        判断当前登录人有哪些菜单
       List<Module> moduleList = moduleService.findByUser(user);

       session.setAttribute("modules",moduleList);

		return "home/main";
	}

    //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home(){
	    return "home/home";
    }
}
