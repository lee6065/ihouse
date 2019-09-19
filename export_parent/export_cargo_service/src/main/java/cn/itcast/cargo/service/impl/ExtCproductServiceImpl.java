package cn.itcast.cargo.service.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractDao contractDao;
    @Override
    public List<ExtCproduct> findAll(ExtCproductExample example) {
        return extCproductDao.selectByExample(example);
    }

    @Override
    public void save(ExtCproduct extCproduct) {
//        1、计算附件的小计金额
        double amount = extCproduct.getCnumber() * extCproduct.getPrice();
        extCproduct.setAmount(amount);
//        获取合同对象
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
//        2、重新计算合同的总金额+小计金额
        contract.setTotalAmount(contract.getTotalAmount()+amount);
//       3、计算合同上的附件数 +1
        contract.setExtNum(contract.getExtNum()+1);
        contractDao.updateByPrimaryKeySelective(contract);
        extCproductDao.insertSelective(extCproduct);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(ExtCproduct extCproduct) {
//        1、修改小计金额
        double amount_new = extCproduct.getCnumber() * extCproduct.getPrice();
        extCproduct.setAmount(amount_new);
        ExtCproduct extCproduct_old = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        Double amount_old = extCproduct_old.getAmount();
//        2、修改合同的总金额：原总金额-原小计金额+新的小计金额
//        获取合同对象
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()-amount_old+amount_new);
        contractDao.updateByPrimaryKeySelective(contract);

        extCproductDao.updateByPrimaryKeySelective(extCproduct);
    }

    @Override
    public void deleteById(String id) {
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        Double amount = extCproduct.getAmount();
//        更新合同的总金额 - 此附件的小计金额
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()-amount);
//        附件数-1
        contract.setExtNum(contract.getExtNum()-1);
        contractDao.updateByPrimaryKeySelective(contract);
//        删除货附件
        extCproductDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample example, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ExtCproduct> list = extCproductDao.selectByExample(example);
        return new PageInfo<>(list);
    }
}
