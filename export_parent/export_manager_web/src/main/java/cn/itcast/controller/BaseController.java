package cn.itcast.controller;

import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;

    protected String companyId;
    protected String companyName;
    protected String userId;
    protected String deptId;
    protected User user;

//    初始化方法
    @ModelAttribute  //在运行Controller的方法之前会执行此方法
    public void initRequestAndResponseAndSession(){
       /* System.out.println("initRequestAndResponseAndSession");
        this.request=request;
        this.response=response;
        this.session=session;*/

        User user = (User) session.getAttribute("loginUser");
        if(user!=null){
            this.user=user;
            this.companyId=user.getCompanyId();
            this.companyName=user.getCompanyName();
            this.userId=user.getId();
            this.deptId=user.getDeptId();
        }
    }
}
