package cn.itcast.controller.cargo;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.cargo.Packing;
import cn.itcast.domain.cargo.Shipping;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.PackingService;
import cn.itcast.service.cargo.ShippingService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/cargo/shipping")
public class ShippingController extends BaseController {

    @Reference
    private ShippingService shippingService;
    @Reference
    private PackingService packingService;

    @RequestMapping(value = "/list", name = "查询托运管理")
    public String exportList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Shipping> page = shippingService.findByPage(companyId, pageNum, pageSize);
        request.setAttribute("page", page);
        return "cargo/shipping/shipping-list";
    }

    @RequestMapping(value = "/submit", name = "提交委托单方法")
    public String submit(String id) {
        Shipping shipping = shippingService.findById(id);
        shipping.setState(1);
        shippingService.update(shipping);
        return "redirect:/cargo/shipping/list.do";  //重定向到列表数据
    }

    @RequestMapping(value = "/cancel", name = "取消委托单状态")
    public String cancel(String id) {//

        Shipping shipping = shippingService.findById(id);
        shipping.setState(0);
        shippingService.update(shipping);

       // request.setAttribute("shipping", shipping);

        return "redirect:/cargo/shipping/list.do";
    }

    @RequestMapping(value = "/toView", name = "进入委托单查看页面")
    public String toView(String id) {//
//        回显数据-->委托单数据
        Shipping shipping = shippingService.findById(id);
        request.setAttribute("shipping", shipping);
        return "cargo/shipping/shipping-view";
    }

    @RequestMapping(value = "/delete", name = "删除委托单")
    public String delete(String id) {
        shippingService.delete(id);
        return "redirect:/cargo/shipping/list.do";
    }

    @RequestMapping(value="/toUpdate" ,name="进入修改委托单页面")
    public String toUpdate(String id){
//        委托单数据
        Shipping shipping = shippingService.findById(id);
        request.setAttribute("shipping",shipping);

        return "cargo/shipping/shipping-update";
    }

    @RequestMapping(value = "/edit", name = "修改委托单")
    public String edit(Shipping shipping,String packingListId) {

           // 修改
            shipping.setCreateBy(userId);
            shipping.setCreateTime(new Date());
            shippingService.update(shipping);



       // request.setAttribute("shipping", shipping);

        return "redirect:/cargo/shipping/list.do";
    }

    @RequestMapping(value="/toAdd" ,name="进入新增委托单页面")
    public String toAdd(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
//        委托单数据
//        List<Packing> packingList = packingService.findPackingByState(companyId,1);

//        request.setAttribute("packing",packingList);
        User user = (User) session.getAttribute("loginUser");
        PageInfo<Packing> pageInfo = packingService.findByPage(user.getCompanyId(), pageNum, pageSize);
        request.setAttribute("page", pageInfo);

        return "cargo/shipping/shipping-add";
    }

   
    @RequestMapping(value = "/edit-add", name = "增加委托单")
    public String editAdd(Shipping shipping) {
        shipping.setCreateBy(userId);
        shipping.setCreateDept(deptId);
        shipping.setCreateTime(new Date());
        shipping.setState(0);
        shippingService.save(shipping);

        return "redirect:/cargo/shipping/list.do";
    }
}
