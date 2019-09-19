package cn.itcast.controller.stat;

import cn.itcast.controller.BaseController;
import cn.itcast.stat.service.StatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stat")
public class StatController extends BaseController {

    @Reference
    private StatService statService;

    @RequestMapping("/toCharts")
    public String toCharts(String chartsType){

        return "stat/stat-"+chartsType;
    }


    @RequestMapping(value = "/factoryCharts",name="获取厂家销售额")
    @ResponseBody
    public List<Map> factoryCharts(){
        List<Map> list = statService.factoryCharts(companyId);
        return list;
    }
    @RequestMapping(value = "/sellCharts",name="获取货物销售量")
    @ResponseBody
    public List<Map> sellCharts(){
        List<Map> list = statService.sellCharts(companyId);
        return list;
    }
    @RequestMapping(value = "/onlineCharts",name="获取24小时系统访问量")
    @ResponseBody
    public List<Map> onlineCharts(){
        List<Map> list = statService.onlineCharts(companyId);
        return list;
    }

}
