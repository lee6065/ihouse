package cn.itcast.dao.system;

import cn.itcast.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    //根据 id 查询
    User findById(String userId);
    
    //查询全部用户
    List<User> findAll(String companyId);
    //根据 id 删除
    void delete(String userId);
    //添加用户
    void save(User user);
    //更新用户
    void update(User user);

    void changeRole(@Param("userid") String userid,@Param("roleid")  String roleId);

    void deleteRoleByUserid(String userid);

    User findByEmail(String email);
}
