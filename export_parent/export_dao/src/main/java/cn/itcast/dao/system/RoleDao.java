package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {
    //根据 id 查询
    Role findById(String roleId);
    
    //查询全部用户
    List<Role> findAll(String companyId);
    //根据 id 删除
    void delete(String roleId);
    //添加用户
    void save(Role role);
    //更新用户
    void update(Role role);

    void insertRoleModule(@Param("roleid") String roleid,@Param("mId") String mId);

    void deleteModuleByRoleid(String roleid);

    List<Role> findByUserid(String userid);
}
