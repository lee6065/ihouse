package cn.itcast.cargo.service.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductResult;
import cn.itcast.vo.ExportResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ExportProductDao exportProductDao;
    @Autowired
    private ExtEproductDao extEproductDao;
    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
//        涉及到6张表
//        数据来源
//        Contract 合同表  需要改变合同的状态
        String[] contractIds = export.getContractIds().split(",");//获取报运单里的合同id  数据是从页面传过来的
        StringBuffer contractNos = new StringBuffer(""); //用来接收每个合同的合同号 使用，隔开

        int totalProductSize = 0; //总的货物数
        int totalExtSize = 0; //总的附件数
        for (String contractId : contractIds) {
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            contract.setState(2); //更改合同的状态
            contractDao.updateByPrimaryKeySelective(contract);

            contractNos.append(contract.getContractNo()).append(",");  //链接合同号
//            查询此合同下的货物
            ContractProductExample example = new ContractProductExample();
            example.createCriteria().andContractIdEqualTo(contractId);
            List<ContractProduct> contractProducts = contractProductDao.selectByExample(example);//此合同下的货物
            int size = contractProducts.size(); //此合同下的货物数
            totalProductSize+=size;
//            保存报运单下的货物
            for (ContractProduct contractProduct : contractProducts) {
                ExportProduct exportProduct = new ExportProduct(); //报运单的货物
//                通过分析发现：报运单的货物对象中的值 除了id和exportId以外都可以从合同的货物对象中获取
//                source  target
                BeanUtils.copyProperties(contractProduct,exportProduct); //把contractProduct属性值拷贝到exportProduct上面
                exportProduct.setId(UUID.randomUUID().toString()); //重新设置exportProduct的id
                exportProduct.setExportId(export.getId());  //构建报运单货物和报运单的关系
                exportProductDao.insertSelective(exportProduct);
//                货物此合同货物下的附件
                List<ExtCproduct> extCproducts = contractProduct.getExtCproducts(); //因为在货物的对象中已经配置了和附件的一对多关系
                int size1 = extCproducts.size();  //此货物下的附件数据
                totalExtSize+=size1;
//                循环合同每个附件保存报运单的附件
                for (ExtCproduct extCproduct : extCproducts) {
                    ExtEproduct extEproduct = new ExtEproduct(); //报运单的附件
                    BeanUtils.copyProperties(extCproduct,extEproduct);  //把extCproduct属性值拷贝到extEproduct上面
                    extEproduct.setId(UUID.randomUUID().toString()); //重新设置extEproduct的id
                    extEproduct.setExportId(export.getId());  //构建报运单附件和报运单的关系
                    extEproduct.setExportProductId(exportProduct.getId());  //构建报运单附件和报运单货物的关系
                    extEproductDao.insertSelective(extEproduct);
                }
            }
        }
//         ContractProduct 合同的货物表
//          ExtCproduct 合同下的附件表
//      需要保存的表：
//         Export  报运单 需要的值：除了页面上传过来的还需要：  合同号用逗号隔开  货物数  附件数  制单日期
//         ExportProduct 报运单下的货物
//         ExtEproduct   报运单下货物的附件
//        "134523,1345653,"------>"134523,1345653"
        export.setCustomerContract(contractNos.toString().substring(0,contractNos.length()-1)); //合同号用逗号隔开
        export.setProNum(totalProductSize);//货物数
        export.setExtNum(totalExtSize);  //附件数
        export.setInputDate(new Date());//制单日期

         exportDao.insertSelective(export);
    }

    @Override
    public void update(Export export) {
//        需要修改两个表的数据
        List<ExportProduct> exportProducts = export.getExportProducts();
        if(exportProducts!=null){
            //        报运单货物的修改
            for (ExportProduct exportProduct : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
//        报运单数据修改
        exportDao.updateByPrimaryKeySelective(export);
    }

    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ExportExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<Export> list = exportDao.selectByExample(example);

        List<Export> shippingList = null;
        for (Export export : list) {

        }
        return new PageInfo(shippingList);
    }

    @Override
    public void updateE(ExportResult exportResult) {
//        修改两个表的
//                报运单表 状态和备注信息
//        获取报运单对象
        Export export = exportDao.selectByPrimaryKey(exportResult.getExportId());
        export.setState(exportResult.getState());//状态
        export.setRemark( exportResult.getRemark());//备注信息
        exportDao.updateByPrimaryKeySelective(export);
//                报运单的货物表  税
//        从返回结果中获取货物相关的数据
        Set<ExportProductResult> products = exportResult.getProducts();
        for (ExportProductResult product : products) {
//            根据货物id查询货物对象
            String exportProductId = product.getExportProductId();
            ExportProduct exportProduct = exportProductDao.selectByPrimaryKey(exportProductId);
//            设置税
            exportProduct.setTax(product.getTax());
            exportProductDao.updateByPrimaryKeySelective(exportProduct);
        }
    }
}
