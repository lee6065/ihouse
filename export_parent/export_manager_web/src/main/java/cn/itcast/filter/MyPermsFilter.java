package cn.itcast.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class MyPermsFilter  extends AuthorizationFilter {

    //TODO - complete JavaDoc

    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;
//        数组中但凡有一个符合权限就直接放行
///system/dept/list.do = perms["部门管理","删除部门"]
        for (String perm : perms) {
            if(subject.isPermitted(perm)){
                return true;
            }
        }
        return false;
    }
}