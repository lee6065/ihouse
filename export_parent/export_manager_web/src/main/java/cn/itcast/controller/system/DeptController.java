package cn.itcast.controller.system;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController{

    @Autowired
    private DeptService deptService;
    @RequestMapping(value="/list" ,name="进入部门管理页面")
    public String findAll(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){


        PageInfo<Dept> pageInfo = deptService.findByPage(companyId,pageNum,pageSize);
        request.setAttribute("page",pageInfo);
        return "system/dept/dept-list";
    }

    @RequestMapping(value="/toAdd" ,name="进入部门新增页面")
    public String toAdd(){
//        查询所有本部门的部门数据
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);

        return "system/dept/dept-add";
    }

    @RequestMapping(value="/edit" ,name="保存部门方法")
    public String edit(Dept dept){

        if(dept.getParent().getId().equals("")){
            dept.getParent().setId(null);
        }
//        dept的id如果为空就是新增，反之就是修改

        if(StringUtils.isEmpty(dept.getId())){ //id为空
            //        新增时需要赋id
            dept.setCompanyId(companyId);
            dept.setCompanyName(companyName);
            dept.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id
            deptService.save(dept);
        }else {  //id不为空
            deptService.update(dept);
        }
        return "redirect:/system/dept/list.do";  //重定向到列表数据
    }
    @RequestMapping(value="/toUpdate" ,name="进入修改部门页面")
    public String toUpdate(String id){
//         根据id查询部门
        Dept dept = deptService.findById(id);
//        把dept放入request域中
        request.setAttribute("dept",dept);

        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);
//        修改页面和新增页面公用一套
        return "system/dept/dept-add";
    }


    @RequestMapping(value="/delete" ,name="删除部门方法")
//    @RequiresPermissions("删除部门")
    public String delete(String id){
        deptService.deleteById(id);
        return "redirect:/system/dept/list.do";  //重定向到列表数据
    }

}
