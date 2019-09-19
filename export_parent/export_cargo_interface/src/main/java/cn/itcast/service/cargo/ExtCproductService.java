package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ExtCproductService {
    public List<ExtCproduct> findAll(ExtCproductExample example);

    void save(ExtCproduct product);

    ExtCproduct findById(String id);

    void update(ExtCproduct product);

    void deleteById(String id);

    PageInfo<ExtCproduct> findByPage(ExtCproductExample example, Integer pageNum, Integer pageSize);
}
