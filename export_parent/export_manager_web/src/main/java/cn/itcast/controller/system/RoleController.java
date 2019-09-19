package cn.itcast.controller.system;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController{

    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private ModuleService moduleService;
    @RequestMapping(value="/list" ,name="进入角色管理页面")
    public String findAll(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Role> pageInfo = roleService.findPage(companyId,pageNum,pageSize);
        request.setAttribute("page",pageInfo);
        return "system/role/role-list";
    }

    @RequestMapping(value="/toAdd" ,name="进入角色新增页面")
    public String toAdd(){
//        查询所有本角色的部门数据
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);
        return "system/role/role-add";
    }

    @RequestMapping(value="/edit" ,name="保存角色方法")
    public String edit(Role role){

//        role的id如果为空就是新增，反之就是修改
        if(StringUtils.isEmpty(role.getId())){ //id为空
            //        新增时需要赋id
            role.setCompanyId(companyId);
            role.setCompanyName(companyName);
            role.setCreateBy(userId);
            role.setCreateDept(deptId);
            role.setCreateTime(new Date());
            role.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id
            roleService.save(role);
        }else {  //id不为空
            role.setUpdateBy(userId);
            role.setUpdateTime(new Date());
            roleService.update(role);
        }

        return "redirect:/system/role/list.do";  //重定向到列表数据
    }
    @RequestMapping(value="/toUpdate" ,name="进入修改角色页面")
    public String toUpdate(String id){
//         根据id查询角色
        Role role = roleService.findById(id);
//        把role放入request域中
        request.setAttribute("role",role);

        //        查询所有本角色的部门数据
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);
//        修改页面和新增页面公用一套
        return "system/role/role-add";
    }


    @RequestMapping(value="/delete" ,name="删除角色方法")
    public String delete(String id){
        roleService.delete(id);
        return "redirect:/system/role/list.do";  //重定向到列表数据
    }

    @RequestMapping(value="/roleModule" ,name="进入角色分配权限的页面")
    public String roleModule(String roleid){
        Role role = roleService.findById(roleid);
        request.setAttribute("role",role);
        return "system/role/role-module";
    }


    @RequestMapping(value="/getZtreeNodes" ,name="请求构建ztree的节点数据")
    @ResponseBody // 1、把数据转成json  2、回显到浏览器
    public List<Map> getZtreeNodes(String roleid){
//        准备一个集合
        List<Map> mapList = new ArrayList<Map>();

//        查询此角色拥有的权限  --- 根据角色查询权限
//        select m.* from pe_role_module rm ,ss_module m
//        where rm.module_id=m.module_id and role_id='4815080e-06ee-4f97-af33-7aa3585a4d12'
       List<Module> moduleListByRole  = moduleService.findByRoleId(roleid);

      /* 需要的数据的格式是：
        [
        {id:1,pId:0,name:"test1"},
        {id:2,pId:0,name:"test2"},  map
        {id:11,pId:1,name:"test1_1"},
        {id:12,pId:1,name:"test1_2"},
        ]*/
//      数据是来自模块表
        List<Module> moduleList = moduleService.findAll();
//        构建简单json数据
        Map map = null;
        for (Module module : moduleList) {  //循环菜单表中的所有数据
            map = new HashMap();
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());
//            map.put("checked",true); // 此角色下之前选中的菜单数据moduleListByRole =[ SaaS管理 的对象 模块管理的对象 企业管理的对象]  应该有勾选的状态
            if(moduleListByRole.contains(module)){  //要重写Module类中的equals方法
                map.put("checked",true);
            }
            mapList.add(map);
        }

        return mapList;
    }

    @RequestMapping(value="/updateRoleModule" ,name="修改角色的权限的方法")
    public String updateRoleModule(String roleid,String moduleIds){ //moduleIds:"1,2,4,5"

//        保存中间表的数据
        roleService.updateRoleModule(roleid,moduleIds);

        return "redirect:/system/role/list.do";

    }


}
