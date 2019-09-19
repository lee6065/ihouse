package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface FactoryService {
    public List<Factory> findAll(FactoryExample example);

    void save(Factory factory);

    Factory findById(String id);

    void update(Factory factory);

    void deleteById(String id);

    PageInfo<Factory> findByPage(FactoryExample example, Integer pageNum, Integer pageSize);
}
