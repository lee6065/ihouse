package cn.itcast.cargo.service.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.vo.ContractProductVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    @Override
    public List<Contract> findAll(ContractExample example) {
        return contractDao.selectByExample(example);
    }

    @Override
    public void save(Contract contract) {
        contractDao.insertSelective(contract);
    }

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void deleteById(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Contract> findByPage(ContractExample example, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Contract> list = contractDao.selectByExample(example);
        return new PageInfo<Contract>(list);
    }

    @Override
    public List<ContractProductVo> selectContractProductVoByShipTime(String inputDate) {
        return contractDao.selectContractProductVoByShipTime(inputDate);
    }

    @Override
    public void updateState(String id) {
        Contract contract = contractDao.selectByPrimaryKey(id);
        contract.setState(1);
        contractDao.updateByPrimaryKeySelective(contract);
    }
}
