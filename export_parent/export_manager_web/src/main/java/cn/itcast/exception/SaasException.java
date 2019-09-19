package cn.itcast.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaasException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        //        判断是否为UnauthorizedException那么跳转到unauthorized.jsp页面
        if(ex instanceof UnauthorizedException){
//            unauthorized.jsp
            mv.setViewName("forward:/unauthorized.jsp");
        }else{
            mv.addObject("errorMsg","系统异常，请联系SaaS开发人员,异常信息是："+ex.getMessage());
            mv.setViewName("error");
        }
        return mv;
    }
}
