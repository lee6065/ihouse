package cn.itcast.controller.system;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.SysLog;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.SysLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController{

    @Autowired
    private SysLogService sysLogService;
    @RequestMapping(value="/list" ,name="进入日志查看页面")
    public String findAll(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){

        PageInfo<SysLog> pageInfo = sysLogService.findByPage(companyId,pageNum,pageSize);
        request.setAttribute("page",pageInfo);
        return "system/log/log-list";
    }


}
