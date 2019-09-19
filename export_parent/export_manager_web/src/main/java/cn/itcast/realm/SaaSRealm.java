package cn.itcast.realm;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SaaSRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

//    认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("+++++进入了认证方法AuthenticationInfo");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String email = token.getUsername();
        String password_page = new String(token.getPassword());
        User user = userService.findByEmail(email); //从数据库中查询
        if(user!=null){
//            匹配密码
            String password_db = user.getPassword();
            if(password_db.equals(password_page)){
                return new SimpleAuthenticationInfo(user,password_db,getName()); //Object principal, Object credentials 密码, String realmName 当前realm的类名
            }else{ //密码错误
                return null;  //在这里一旦return null 那么在subject.login(token)的方法那里就会报异常
            }
        }
        return null; //没有用户
    }

//    授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        告诉shiro框架 当前登录人有哪些菜单权限
        System.out.println("------进入了授权方法AuthorizationInfo");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        User user = (User) principalCollection.getPrimaryPrincipal();
//        根据用户查询所拥有的菜单权限
        List<Module> modules = moduleService.findByUser(user);
        for (Module module : modules) {
           authorizationInfo.addStringPermission(module.getName());
        }
        return authorizationInfo;
    }


}
