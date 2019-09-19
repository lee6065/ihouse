package cn.itcast.controller.company;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController{

    @Reference
    private CompanyService companyService;
    @RequestMapping(value="/list" ,name="进入企业管理页面")
    @RequiresPermissions("企业管理")  // 表示 有“企业管理”权限才能进入此方法
    public String findAll(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "2") Integer pageSize){
//        List<Company> companyList = companyService.findAll();
//        PageBean pageBean = companyService.findByPage(pageNum,pageSize);
        PageInfo<Company> pageInfo = companyService.findByPage(pageNum,pageSize);
        request.setAttribute("page",pageInfo);



        return "company/company-list";
    }

    @RequestMapping(value="/toAdd" ,name="进入企业新增页面")
    public String toAdd(){

        return "company/company-add";
    }

    @RequestMapping(value="/edit" ,name="保存企业方法")
    public String edit(Company company){

//        company的id如果为空就是新增，反之就是修改

        if(StringUtils.isEmpty(company.getId())){ //id为空
            //        新增时需要赋id
            company.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id
            companyService.save(company);
        }else {  //id不为空
            companyService.update(company);
        }

        return "redirect:/company/list.do";  //重定向到列表数据
    }
    @RequestMapping(value="/toUpdate" ,name="进入修改企业页面")
    public String toUpdate(String id){
//         根据id查询企业
        Company company = companyService.findById(id);
//        把company放入request域中
        request.setAttribute("company",company);
//        修改页面和新增页面公用一套
        return "company/company-add";
    }


    @RequestMapping(value="/delete" ,name="删除企业方法")
    public String delete(String id){
        companyService.deleteById(id);
        return "redirect:/company/list.do";  //重定向到列表数据
    }

}
