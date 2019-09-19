package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;

import java.util.List;

public interface DeptDao {
    //部门管理是企业管理员在操作，所以一定需要带着companyId查询
    public List<Dept> findAll(String companyId);

    void insert(Dept dept);

    Dept findById(String id);

    void updateById(Dept dept);

    void deleteById(String id);

}
