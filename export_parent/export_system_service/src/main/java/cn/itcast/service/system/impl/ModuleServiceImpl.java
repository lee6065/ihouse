package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleDao moduleDao;
    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public void save(Module module) {
        moduleDao.save(module);
    }

    @Override
    public Module findById(String id) {
        return   moduleDao.findById(id);
    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    @Override
    public void deleteById(String id) {
        moduleDao.deleteById(id);
    }

    public PageInfo findPage(Integer page, int size) {
        PageHelper.startPage(page,size);
        List<Module> list = moduleDao.findAll();
        return new PageInfo(list,5);
    }

    @Override
    public List<Module> findByRoleId(String roleid) {
        return moduleDao.findByRoleId(roleid);
    }

    @Override
    public List<Module> findByUser(User user) {
//        根据登录人查询菜单
        Integer degree = user.getDegree();
        List<Module> moduleList = null;
//        saas内部员工   admin
        if(degree==0){
//            查询属于SaaS内部的菜单 ：select * from ss_module where belong=0
            moduleList = moduleDao.findByBelong("0");
        }else if(degree==1){
            //        企业管理员     laowang
//            查询属于企业内部的菜单 ：select * from ss_module where belong=1
            moduleList = moduleDao.findByBelong("1");
        }else{
//            其他员工 根据分配的权限展示
//            select  distinct m.* from pe_user u,pe_role_user ru, pe_role r,pe_role_module rm , ss_module m
//            where u.user_id=ru.user_id and ru.role_id=r.role_id and r.role_id=rm.role_id and rm.module_id=m.module_id
//            and u.user_id='58986b46-17f0-49d7-8b32-8273fc305ca4'
            moduleList = moduleDao.findByUserId(user.getId());
        }


        return moduleList;
    }


}
