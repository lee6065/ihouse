package cn.itcast.controller.cargo;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.utils.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController{

    @Reference
    private ExtCproductService extCproductService;
    @Reference
    private FactoryService factoryService;

    @RequestMapping(value="/list" ,name="进入购销合同附件管理页面")
    public String findAll(String contractId,String contractProductId,@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){

        ExtCproductExample example = new ExtCproductExample();  //example是用来构建条件的
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractProductIdEqualTo(contractProductId); //根据货物的id查询此货物下的所有附件

        PageInfo<ExtCproduct> pageInfo = extCproductService.findByPage(example,pageNum,pageSize);
        request.setAttribute("page",pageInfo);


//        查询生产附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);



        request.setAttribute("contractId",contractId);  //合同id
        request.setAttribute("contractProductId",contractProductId); //货物id

        return "cargo/extc/extc-list";
    }
/*

    @RequestMapping(value="/toAdd" ,name="进入购销合同附件新增页面")
    public String toAdd(){

        return "cargo/extCproduct/extCproduct-add";
    }
*/

 @Autowired
 private FileUploadUtil fileUploadUtil;

    @RequestMapping(value="/edit" ,name="保存购销合同附件方法")
    public String edit(ExtCproduct extCproduct, MultipartFile productPhoto){
//        把图片上传到七牛云上
        String imageUrl = null;
        try {
            imageUrl = fileUploadUtil.upload(productPhoto);
        } catch (Exception e) {
            imageUrl=null;
            e.printStackTrace();
        }
//        返回的路径保存到附件表中
        extCproduct.setProductImage(imageUrl);
//        extCproduct的id如果为空就是新增，反之就是修改
        if(StringUtils.isEmpty(extCproduct.getId())){ //id为空
            //        新增时需要赋id
            extCproduct.setCompanyId(companyId);
            extCproduct.setCompanyName(companyName);
            extCproduct.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id

            extCproduct.setCreateDept(deptId);
            extCproduct.setCreateBy(userId);
            extCproduct.setCreateTime(new Date());
            extCproductService.save(extCproduct);
        }else {  //id不为空
            extCproduct.setUpdateBy(userId);
            extCproduct.setUpdateTime(new Date());
            extCproductService.update(extCproduct);
        }
//        /list.do?contractId=06472432-9cfd-4854-bb68-ffd43e6f8d9b&contractProductId=7c7a2222-0363-41dc-9d3c-bcf07967fe34
        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();  //重定向到列表数据
    }
    @RequestMapping(value="/toUpdate" ,name="进入修改购销合同附件页面")
    public String toUpdate(String id){
//         根据id查询购销合同附件
        ExtCproduct extCproduct = extCproductService.findById(id);
//        把extCproduct放入request域中
        request.setAttribute("extCproduct",extCproduct);

        //        查询生产附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);

//        修改页面和新增页面公用一套
        return "cargo/extc/extc-update";
    }


    @RequestMapping(value="/delete" ,name="删除购销合同附件方法")
//    @RequiresPermissions("删除购销合同附件")
    public String delete(String id,String contractId,String contractProductId){
        extCproductService.deleteById(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;  //重定向到列表数据
    }

}
