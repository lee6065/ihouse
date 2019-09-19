package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;
    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public void save(Company company) {
        companyDao.insert(company);
    }

    @Override
    public Company findById(String id) {
        return  companyDao.findById(id);
    }

    @Override
    public void update(Company company) {
        companyDao.updateById(company);
    }

    @Override
    public void deleteById(String id) {
        companyDao.deleteById(id);
    }

//    PageInfo等同于我们自定义的pageBean 并且比pageBean更完善
    public PageInfo<Company> findByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);  //使用分页插件 一定要紧跟一个查询方法
        List<Company> list = companyDao.findAll(); //表面上是查询所有，但是执行时已经分页了
        return new PageInfo<Company>(list,5);
    }

   /* @Override
   传统的分页方法
    public PageBean findByPage(Integer pageNum, Integer pageSize) {
//        当前页的数据 select * from ss_company limit (pageNum-1)*pageSize,pageSize
//         总条数：select count(*) from ss_company
        List<Company> list = companyDao.findByPage((pageNum-1)*pageSize,pageSize);
        Long total = companyDao.findCount();
        return new PageBean(pageNum,pageSize,list,total);

    }*/
}
