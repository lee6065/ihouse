package cn.itcast.dao.system;

import cn.itcast.domain.system.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleDao {

    public List<Module> findAll();

    void save(Module module);

    Module findById(String id);

    void update(Module module);

    void deleteById(String id);

    Long findCount();

    List<Module> findPage(@Param("page") int page, @Param("size") int size);

    List<Module> findByRoleId(String roleid);

    List<Module> findByBelong(String s);

    List<Module> findByUserId(String id);
}
