package cn.itcast.cargo.service.impl;

import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.FactoryService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryDao factoryDao;
    @Override
    public List<Factory> findAll(FactoryExample example) {
        return factoryDao.selectByExample(example);
    }

    @Override
    public void save(Factory factory) {

    }

    @Override
    public Factory findById(String id) {
        return null;
    }

    @Override
    public void update(Factory factory) {

    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public PageInfo<Factory> findByPage(FactoryExample example, Integer pageNum, Integer pageSize) {
        return null;
    }
}
