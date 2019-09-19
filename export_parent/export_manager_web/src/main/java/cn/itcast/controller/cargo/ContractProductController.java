package cn.itcast.controller.cargo;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.utils.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController{

    @Reference
    private ContractProductService contractProductService;
    @Reference
    private FactoryService factoryService;

    @RequestMapping(value="/list" ,name="进入购销合同货物管理页面")
    public String findAll(String contractId,@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){

        ContractProductExample example = new ContractProductExample();  //example是用来构建条件的
        ContractProductExample.Criteria criteria = example.createCriteria();
        criteria.andContractIdEqualTo(contractId);

        PageInfo<ContractProduct> pageInfo = contractProductService.findByPage(example,pageNum,pageSize);
        request.setAttribute("page",pageInfo);


//        查询生产货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);



        request.setAttribute("contractId",contractId);

        return "cargo/product/product-list";
    }
/*

    @RequestMapping(value="/toAdd" ,name="进入购销合同货物新增页面")
    public String toAdd(){

        return "cargo/contractProduct/contractProduct-add";
    }
*/

 @Autowired
 private FileUploadUtil fileUploadUtil;

    @RequestMapping(value="/edit" ,name="保存购销合同货物方法")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto){
//        把图片上传到七牛云上
        String imageUrl = null;
        try {
            imageUrl = fileUploadUtil.upload(productPhoto);
        } catch (Exception e) {
            imageUrl=null;
            e.printStackTrace();
        }
//        返回的路径保存到货物表中
        contractProduct.setProductImage(imageUrl);
//        contractProduct的id如果为空就是新增，反之就是修改
        if(StringUtils.isEmpty(contractProduct.getId())){ //id为空
            //        新增时需要赋id
            contractProduct.setCompanyId(companyId);
            contractProduct.setCompanyName(companyName);
            contractProduct.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id

            contractProduct.setCreateDept(deptId);
            contractProduct.setCreateBy(userId);
            contractProduct.setCreateTime(new Date());
            contractProductService.save(contractProduct);
        }else {  //id不为空
            contractProduct.setUpdateBy(userId);
            contractProduct.setUpdateTime(new Date());
            contractProductService.update(contractProduct);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractProduct.getContractId();  //重定向到列表数据
    }
    @RequestMapping(value="/toUpdate" ,name="进入修改购销合同货物页面")
    public String toUpdate(String id){
//         根据id查询购销合同货物
        ContractProduct contractProduct = contractProductService.findById(id);
//        把contractProduct放入request域中
        request.setAttribute("contractProduct",contractProduct);

        //        查询生产货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);

//        修改页面和新增页面公用一套
        return "cargo/product/product-update";
    }


    @RequestMapping(value="/delete" ,name="删除购销合同货物方法")
//    @RequiresPermissions("删除购销合同货物")
    public String delete(String id,String contractId){
        contractProductService.deleteById(id);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;  //重定向到列表数据
    }
    @RequestMapping(value="/toImport" ,name="进入上传购销合同货物页面")
    public String toImport(String contractId){

        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }

    @RequestMapping(value="/import" ,name="上传购销合同货物方法")
    public String importXls(String contractId,MultipartFile file) throws Exception {
        //        1、创建一个的工作薄
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//        2、获取一个工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowIndex = sheet.getLastRowNum(); //读取sheet的最后一行索引值
//        生产厂家	货号	数量	包装单位(PCS/SETS)	装率	箱数	单价	货物描述	要求
        List<ContractProduct> list = new ArrayList<>(); //准备接收所有的需要保存的货物对象 一次性提交事务
        for (int i = 1; i <= lastRowIndex; i++) {
            XSSFRow row = sheet.getRow(i);  //获取行
            ContractProduct product = new ContractProduct(); //货物
            String factoryName = row.getCell(1).getStringCellValue();//生产厂家
            product.setFactoryName(factoryName);
            String productNo = row.getCell(2).getStringCellValue();//货号
            product.setProductNo(productNo);
            Integer cnumber = ((Double) row.getCell(3).getNumericCellValue()).intValue();//数量
            product.setCnumber(cnumber);
            String packingUnit = row.getCell(4).getStringCellValue();//包装单位(PCS/SETS)
            product.setPackingUnit(packingUnit);
            String loadingRate = row.getCell(5).getNumericCellValue() + ""; //装率
            product.setLoadingRate(loadingRate);
            Integer boxNum = ((Double) row.getCell(6).getNumericCellValue()).intValue();//数量
            product.setBoxNum(boxNum);
            Double price = row.getCell(7).getNumericCellValue(); //单价
            product.setPrice(price);
            String productDesc = row.getCell(8).getStringCellValue(); // 货物描述
            product.setProductDesc(productDesc);
            String productRequest = row.getCell(9).getStringCellValue(); // 要求
            product.setProductRequest(productRequest);
            product.setContractId(contractId); // 设置合同id

            //        新增时需要赋id
            product.setCompanyId(companyId);
            product.setCompanyName(companyName);
            product.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id

            product.setCreateDept(deptId);
            product.setCreateBy(userId);
            product.setCreateTime(new Date());

//            contractProductService.save(product);
            list.add(product);
        }
        contractProductService.saveList(list); //一次性保存
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;  //重定向到列表数据
    }

}
