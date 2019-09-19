package cn.itcast.service.system;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {
//保存用户
public void save(Role role);
//更新用户
public void update(Role role);
//删除用户
public void delete(String id);
//根据 id 查询用户
public Role findById(String id);
//分页查询
public PageInfo findPage(String companyId, int page, int size);

    void updateRoleModule(String roleid, String moduleIds);

    List<Role> findAll(String companyId);

    List<Role> findByUserid(String id);
}
