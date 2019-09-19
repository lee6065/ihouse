package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;
    @Override
    public void save(Role role) {
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public PageInfo findPage(String companyId, int page, int size) {
        PageHelper.startPage(page,size);
        List<Role> list = roleDao.findAll(companyId);
        return new PageInfo(list);
    }

    @Override
    public void updateRoleModule(String roleid, String moduleIds) { //moduleIds:'1,3,4,6'
//        把之前此角色的权限数据删除
        roleDao.deleteModuleByRoleid(roleid);

        if(!StringUtils.isEmpty(moduleIds)){
            String[] mIds = moduleIds.split(",");
            for (String mId : mIds) {
                roleDao.insertRoleModule(roleid,mId);
    //            insert into pe_role_module (role_id,module_id) values(roleid,mId);
            }
        }
    }

    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }

    @Override
    public List<Role> findByUserid(String userid) {
//        select * from pe_role_user ru,pe_role r where
//        ru.role_id=r.role_id and ru.user_id=?
        return roleDao.findByUserid(userid);
    }

    public static void main(String[] args) {
        String string = new Md5Hash("123456", "laowang@export.com", 10).toString();
        System.out.println(string);
    }

//    22f1719dfc7634fde662a6c0c43ac6a4
}
