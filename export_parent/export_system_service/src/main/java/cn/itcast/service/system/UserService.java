package cn.itcast.service.system;

import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

public interface UserService {
//保存用户
public void save(User user);
//更新用户
public void update(User user);
//删除用户
public void delete(String id);
//根据 id 查询用户
public User findById(String id);
//分页查询
public PageInfo findPage(String companyId, int page, int size);

    void changeRole(String userid, String[] roleIds);

    User findByEmail(String email);
}
