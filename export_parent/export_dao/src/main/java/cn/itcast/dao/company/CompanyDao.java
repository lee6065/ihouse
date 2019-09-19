package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CompanyDao {
    public List<Company> findAll();

    void insert(Company company);

    Company findById(String id);

    void updateById(Company company);

    void deleteById(String id);

    Long findCount();

    List<Company> findByPage(@Param("start") int start,@Param("size") Integer pageSize);
    /*Map map = new HashMap<>();
    map.put("start",start);
    map.put("size",pageSize);*/
}
