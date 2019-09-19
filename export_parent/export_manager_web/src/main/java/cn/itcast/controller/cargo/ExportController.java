package cn.itcast.controller.cargo;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.utils.BeanMapUtils;
import cn.itcast.utils.DownloadUtil;
import cn.itcast.vo.ExportProductVo;
import cn.itcast.vo.ExportResult;
import cn.itcast.vo.ExportVo;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {
//    contractList
//    查询的所有已上报的购销合同
    @Reference
    private ContractService contractService;
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;



    @RequestMapping(value="/list" ,name="查询的报运单")
    public String exportList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize){
        ExportExample example = new ExportExample();  //example是用来构建条件的
        ExportExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(companyId);  //根据companyId查询
        example.setOrderByClause("create_time desc"); //根据时间排序
        PageInfo<Export> pageInfo = exportService.findAll(example,pageNum,pageSize);
        request.setAttribute("page",pageInfo);
        return "cargo/export/export-list";
    }

    @RequestMapping(value="/contractList" ,name="查询的所有已上报的购销合同")
    public String contractList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize){

        ContractExample example = new ContractExample();  //example是用来构建条件的
        ContractExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo(1);
        criteria.andCompanyIdEqualTo(companyId);  //根据companyId查询
        example.setOrderByClause("create_time desc"); //根据时间排序
        PageInfo<Contract> pageInfo = contractService.findByPage(example,pageNum,pageSize);
        request.setAttribute("page",pageInfo);
        return "cargo/export/export-contractList";
    }


    @RequestMapping(value="/toExport" ,name="进入购销合同去生成报运单页面")
    public String toExport(String id){  //"12345yu6,erttui"
        request.setAttribute("id",id);
        return "cargo/export/export-toExport";
    }

    @RequestMapping(value="/edit" ,name="保存报运单方法")
    public String edit(Export export){
//        export的id如果为空就是新增，反之就是修改
        if(StringUtils.isEmpty(export.getId())){ //id为空
            //        新增时需要赋id
            export.setCompanyId(companyId);
            export.setCompanyName(companyName);
            export.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id

            export.setCreateDept(deptId);
            export.setCreateBy(userId);
            export.setCreateTime(new Date());
            export.setState(0); // 新增的报运单 状态时0：草稿
            exportService.save(export);
        }else {  //id不为空
            export.setUpdateBy(userId);
            export.setUpdateTime(new Date());
            exportService.update(export);
        }
        return "redirect:/cargo/export/list.do";  //重定向到列表数据
    }

    @RequestMapping(value="/toUpdate" ,name="进入修改报运单页面")
    public String toUpdate(String id){  //"12345yu6,erttui"]
//        报运单数据
        Export export = exportService.findById(id);
        request.setAttribute("export",export);
//                报运单下货物的数据
        List<ExportProduct> exportProductList = exportProductService.findByExportId(id);
        request.setAttribute("eps",exportProductList);
        return "cargo/export/export-update";
    }


    @RequestMapping(value="/submit" ,name="提交报运单方法")
    public String submit(String id){
        Export export = exportService.findById(id);
        export.setState(1);
        exportService.update(export);
        return "redirect:/cargo/export/list.do";  //重定向到列表数据
    }

    @RequestMapping(value="/exportE" ,name="电子报运（调用海关的项目）")
    public String exportE(String id){  // 报运单的id
        Export export = exportService.findById(id); //报运单对象

        List<ExportProduct> exportProductList= exportProductService.findByExportId(id);  //当前报运单的货物对象

//        把export和exportProductList统一放入到ExportVo中
        ExportVo exportVo = new ExportVo();
//        发现exportVo属性都可以从export获取
        BeanUtils.copyProperties(export,exportVo);
        exportVo.setExportId(id); //构建exportVo和export的关系
//        向exportVo中放入货物数据
        for (ExportProduct exportProduct : exportProductList) {
            ExportProductVo exportProductVo = new ExportProductVo();
            BeanUtils.copyProperties(exportProduct,exportProductVo);
            exportProductVo.setEid(exportVo.getId()); //设置exportVo的id
            exportProductVo.setExportProductId(exportProduct.getId()); //构建exportProductVo和exportProduct的关系
            exportVo.getProducts().add(exportProductVo);
        }
//        调用海关项目 RS

//        String address = "http://ip:端口/webxml中的url-pattern/applicationContext中的address/接口上的path"
        String address = "http://127.0.0.1:9090/ws/export/ep/";
        WebClient.create(address)
//                .type(MediaType.APPLICATION_XML) //请求参数的类型
                .post(exportVo);
        ExportResult exportResult = WebClient.create(address  + id)
                .type(MediaType.APPLICATION_JSON) //请求参数的类型
                .get(ExportResult.class);

        exportService.updateE(exportResult);

        return "redirect:/cargo/export/list.do";  //重定向到列表数据
    }

    @Autowired
    private DownloadUtil downloadUtil;


    @RequestMapping(value="/exportPdf" ,name="下载报运单pdf")
    public void exportPdf(String id) throws Exception{
//        报运单
        Export export = exportService.findById(id);

//        读取模板的路径
        String filePath = session.getServletContext().getRealPath("/template/export.jasper");
//        文件的输入流
        InputStream inputStream = new FileInputStream(filePath);
//        准备模板中需要的参数
//        parameters中放报运单的数据
//        把bean对象转成map
        Map<String, Object> parameters = BeanMapUtils.beanToMap(export);
//        报运单的货物
        List<ExportProduct> exportProducts = exportProductService.findByExportId(id);

//        准备一个集合的数据源
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(exportProducts);
//        填充模板数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, dataSource);

        byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
//        输出
//        JasperExportManager.exportReportToPdfStream(jasperPrint,new FileOutputStream("d:\\demo4.pdf"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        把pdf字节数组写入到缓存流中
        byteArrayOutputStream.write(bytes);
        downloadUtil.download(byteArrayOutputStream,response,"报运单.pdf");

    }
}
