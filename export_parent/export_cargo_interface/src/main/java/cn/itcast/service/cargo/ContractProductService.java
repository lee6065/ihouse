package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ContractProductService {
    public List<ContractProduct> findAll(ContractProductExample example);

    void save(ContractProduct product);

    ContractProduct findById(String id);

    void update(ContractProduct product);

    void deleteById(String id);

    PageInfo<ContractProduct> findByPage(ContractProductExample example, Integer pageNum, Integer pageSize);

    void saveList(List<ContractProduct> list);
}
