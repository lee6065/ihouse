package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.vo.ContractProductVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ContractService {
    public List<Contract> findAll(ContractExample example);

    void save(Contract contract);

    Contract findById(String id);

    void update(Contract contract);

    void deleteById(String id);

    PageInfo<Contract> findByPage(ContractExample example, Integer pageNum, Integer pageSize);

    List<ContractProductVo> selectContractProductVoByShipTime(String inputDate);

    void updateState(String id);
}
