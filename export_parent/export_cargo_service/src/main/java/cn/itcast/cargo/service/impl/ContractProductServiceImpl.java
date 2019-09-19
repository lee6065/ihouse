package cn.itcast.cargo.service.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.service.cargo.ContractProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;
    @Override
    public List<ContractProduct> findAll(ContractProductExample example) {
        return contractProductDao.selectByExample(example);
    }

    @Override
    public void save(ContractProduct product) {
        /*1、计算此货物的小计金额 单价*数量 cnumber*price
        2、重新计算合同的总金额 当前合同的总金额+货物的小计金额
        3、还需要计算合同中货物的种类数       */

//        1、计算此货物的小计金额 单价*数量 cnumber*price
        double amount = product.getCnumber() * product.getPrice();
        product.setAmount(amount); //小计金额

//        根据货物中的合同id查询合同对象
        String contractId = product.getContractId();
        Contract contract = contractDao.selectByPrimaryKey(contractId);
//        2、重新计算合同的总金额 当前合同的总金额+货物的小计金额
        contract.setTotalAmount(contract.getTotalAmount()+amount);
//        3、还需要计算合同中货物的种类数
        contract.setProNum(contract.getProNum()+1);
        contractDao.updateByPrimaryKeySelective(contract);//更新合同

        contractProductDao.insertSelective(product);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(ContractProduct product) {
        ContractProduct contractProduct_old = contractProductDao.selectByPrimaryKey(product.getId());
        Double amount_old = contractProduct_old.getAmount();

//        1、重置设置此货物的小计金额
        double amount_now = product.getCnumber() * product.getPrice();
        product.setAmount(amount_now);

//        2、合同上总金额 = 原总金额-货物的旧小计金额+货物的新小计金额
//        根据货物中的合同id查询合同对象
        String contractId = product.getContractId();
        Contract contract = contractDao.selectByPrimaryKey(contractId);
        contract.setTotalAmount(contract.getTotalAmount()-amount_old+amount_now);

        contractDao.updateByPrimaryKeySelective(contract);//更新合同

        contractProductDao.updateByPrimaryKeySelective(product);
    }

    @Override
    public void deleteById(String id) {
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        Double amount = contractProduct.getAmount(); //货物的小计金额

//        先查询此货物下的所有附件
        List<ExtCproduct> extCproducts = contractProduct.getExtCproducts(); //实体类中已经构建和货物和附件的一对多关系
//        select * from 附件表 where 货物id=？

//        附件种类数据
        int size = extCproducts.size();

//        准备一个变量接收每个附件的小计金额总和
        Double extTotalAmount = 0.0;
        for (ExtCproduct extCproduct : extCproducts) {
            extTotalAmount += extCproduct.getAmount();
        }
        String contractId = contractProduct.getContractId();
        Contract contract = contractDao.selectByPrimaryKey(contractId);
        contract.setTotalAmount(contract.getTotalAmount()-amount-extTotalAmount);
//        更新合同上的总金额  合同原总金额-此货物小计金额-此货物下所有附件的小计金额

//        更新合同上的货物种类数和附件种类数  货物种类数-1   附件种类数-货物下的附件数
        contract.setProNum(contract.getProNum()-1);
        contract.setExtNum(contract.getExtNum()-size);
        contractDao.updateByPrimaryKeySelective(contract);


//        删除此货物下的所有附件
        for (ExtCproduct extCproduct : extCproducts) {
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }
//        删除货物
        contractProductDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample example, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContractProduct> list = contractProductDao.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public void saveList(List<ContractProduct> list) {
        for (ContractProduct product : list) {
//            contractProductDao.insertSelective(product);
            this.save(product); //此方法 有计算小计金额 有更新合同总金额和货物数
        }
    }
}
