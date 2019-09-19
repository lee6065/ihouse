package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao deptDao;
    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    @Override
    public void save(Dept dept) {
        deptDao.insert(dept);
    }

    @Override
    public Dept findById(String id) {
        return  deptDao.findById(id);
    }

    @Override
    public void update(Dept dept) {
        deptDao.updateById(dept);
    }

    @Override
    public void deleteById(String id) {
        deptDao.deleteById(id);
    }

//    PageInfo等同于我们自定义的pageBean 并且比pageBean更完善
    public PageInfo<Dept> findByPage(String companyId,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);  //使用分页插件 一定要紧跟一个查询方法
        List<Dept> list = deptDao.findAll(companyId); //表面上是查询所有，但是执行时已经分页了
        return new PageInfo<Dept>(list,5);
    }

}
