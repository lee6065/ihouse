package cn.itcast.service.company;

import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CompanyService {
    public List<Company> findAll();

    void save(Company company);

    Company findById(String id);

    void update(Company company);

    void deleteById(String id);

    PageInfo<Company> findByPage(Integer pageNum, Integer pageSize);
//    PageBean findByPage(Integer pageNum, Integer pageSize);
}
