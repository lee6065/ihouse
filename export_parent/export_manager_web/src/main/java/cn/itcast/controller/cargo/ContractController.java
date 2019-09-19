package cn.itcast.controller.cargo;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.utils.DownloadUtil;
import cn.itcast.vo.ContractProductVo;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController{

    @Reference
    private ContractService contractService;

    @RequestMapping(value="/list" ,name="进入购销合同管理页面")
    public String findAll(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){

        ContractExample example = new ContractExample();  //example是用来构建条件的
        ContractExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(companyId);  //根据companyId查询
        Integer degree = user.getDegree();
//        根据当前登录人的degree来判断
//        4、普通员工 xiaowang xiaozhang 只能看到自己的数据
//        3、部门经理 dawang  看到本部门的数据
//        2、总经理  dalaowang 能看到所有下属部门的数据
//        1、企业管理员 能看本公司所有数据
        switch (degree){
            case 4:{
                criteria.andCreateByEqualTo(userId); //追加当前登录人等于创建人 create_By=2
                break;
            }
            case 3:{
                criteria.andCreateDeptLike(deptId+"%");//追加当前登录门户和创建人所属部门一致 create_Dept like '100%'
                break;
            }
            case 2:{
                criteria.andCreateDeptLike(deptId+"%");//使用部门编号匹配 。要求：部门编号不能使用UUID
                break;
            }
        }

        example.setOrderByClause("create_time desc"); //根据时间排序
        PageInfo<Contract> pageInfo = contractService.findByPage(example,pageNum,pageSize);
        request.setAttribute("page",pageInfo);
        return "cargo/contract/contract-list";
    }

    @RequestMapping(value="/toAdd" ,name="进入购销合同新增页面")
    public String toAdd(){

        return "cargo/contract/contract-add";
    }

    @RequestMapping(value="/edit" ,name="保存购销合同方法")
    public String edit(Contract contract){

//        contract的id如果为空就是新增，反之就是修改

        if(StringUtils.isEmpty(contract.getId())){ //id为空
            //        新增时需要赋id
            contract.setCompanyId(companyId);
            contract.setCompanyName(companyName);
            contract.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id

            contract.setCreateDept(deptId);
            contract.setCreateBy(userId);
            contract.setCreateTime(new Date());
            contract.setState(0); // 新增的合同 状态时0：草稿
            contractService.save(contract);
        }else {  //id不为空
            contract.setUpdateBy(userId);
            contract.setUpdateTime(new Date());
            contractService.update(contract);
        }
        return "redirect:/cargo/contract/list.do";  //重定向到列表数据
    }
    @RequestMapping(value="/toUpdate" ,name="进入修改购销合同页面")
    public String toUpdate(String id){
//         根据id查询购销合同
        Contract contract = contractService.findById(id);
//        把contract放入request域中
        request.setAttribute("contract",contract);
//        修改页面和新增页面公用一套
        return "cargo/contract/contract-add";
    }


    @RequestMapping(value="/delete" ,name="删除购销合同方法")
//    @RequiresPermissions("删除购销合同")
    public String delete(String id){
        contractService.deleteById(id);
        return "redirect:/cargo/contract/list.do";  //重定向到列表数据
    }
    @RequestMapping(value="/print" ,name="跳转到下载出货表页面")
    public String print(){

        return "cargo/print/contract-print";  //重定向到列表数据
    }

    @Autowired
    private DownloadUtil downloadUtil;

    @RequestMapping(value="/printExcel" ,name="下载出货表数据")
    public void printExcel(String inputDate) throws Exception{

//       1、准备导出一个Excel
        XSSFWorkbook workbook = new XSSFWorkbook();
//        2、创建一个工作表
        XSSFSheet sheet = workbook.createSheet("出货数据");
//        3、设置列宽
        sheet.setColumnWidth(1,26*256);
        sheet.setColumnWidth(2,15*256);
        sheet.setColumnWidth(3,26*256);
        sheet.setColumnWidth(4,15*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        sheet.setColumnWidth(7,15*256);
        sheet.setColumnWidth(8,15*256);

//        大标题
        XSSFRow bigTitleRow = sheet.createRow(0);
        bigTitleRow.setHeightInPoints(36);  //行高
//        创建单元格
        for (int i = 0; i < 9; i++) {
            bigTitleRow.createCell(i);
        }
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        XSSFCell bigTitleRowCell = bigTitleRow.getCell(1);
        bigTitleRowCell.setCellStyle(bigTitle(workbook));
//        inputDate:2019年01    2018年10
        bigTitleRowCell.setCellValue(inputDate.replaceAll("-0","年").replaceAll("-","年")+"月份出货表");
//       小标题
//        客户	合同号	货号	数量	工厂	工厂交期	船期	贸易条款
        String[] titles = new String[]{"客户","合同号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        XSSFRow titleRow = sheet.createRow(1);
        for (int i = 0; i < titles.length; i++) {
            XSSFCell cell = titleRow.createCell(i+1);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(title(workbook));
        }
//        数据库中查询的需要出货的数据
        List<ContractProductVo> contractProductVoList = contractService.selectContractProductVoByShipTime(inputDate);

        int rowIndex = 2;
        XSSFRow row = null;
        XSSFCell cell = null;
        for (ContractProductVo cpvo : contractProductVoList) {
            row = sheet.createRow(rowIndex);
            cell = row.createCell(1);
            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getCustomName());

            cell = row.createCell(2);
            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getContractNo());

            cell = row.createCell(3);
            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getProductNo());
//{"客户","合同号","货号","数量","工厂","工厂交期","船期","贸易条款"};

            cell = row.createCell(4);
            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getCnumber());

            cell = row.createCell(5);
            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getFactoryName());


            cell = row.createCell(6);
            cell.setCellStyle(text(workbook));
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cpvo.getDeliveryPeriod()) );

            cell = row.createCell(7);
            cell.setCellStyle(text(workbook));
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cpvo.getShipTime()) );

            cell = row.createCell(8);
            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getTradeTerms());
            rowIndex++;
        }

//        文件的下载
//        一个流  文件的输出流
//        两个头  文件mime类型  文件的打开方式 in-line(在线打开) attachment
//        ServletOutputStream outputStream = response.getOutputStream();
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//设置MIME类型 常用的文件的mime类型可以省略
//        response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode("出货表.xlsx", "UTF-8"));
//        workbook.write(outputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //创建一个缓存流
//        byteArrayOutputStream.
        workbook.write(byteArrayOutputStream); //把workbook写入到缓存流中zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
        downloadUtil.download(byteArrayOutputStream,response,"出货表.xlsx");
    }

    @RequestMapping(value="/printExcelWithTemplate" ,name="使用模板下载出货表数据")
    public void printExcelWithTemplate(String inputDate) throws Exception{

//        获取模板路径
//        session.getServletContext().getRealPath("/") 获取根目录
        String realPath = session.getServletContext().getRealPath("/make/xlsprint/tOUTPRODUCT.xlsx");
//       1、准备导出一个Excel
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(realPath));
//        2、获取一个工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
//        3、获取大标题的单元格
        XSSFCell bigTitleRowCell = sheet.getRow(0).getCell(1);
//        向单元格中放入数据
        bigTitleRowCell.setCellValue(inputDate.replaceAll("-0","年").replaceAll("-","年")+"月份出货表");
//        数据库中查询的需要出货的数据
        List<ContractProductVo> contractProductVoList = contractService.selectContractProductVoByShipTime(inputDate);
        int rowIndex = 2;
        XSSFRow row = null;
        XSSFCell cell = null;

//        从模板中获取单元格样式
        XSSFRow titleRow = sheet.getRow(2);
        CellStyle[] cellStyles = new CellStyle[8]; //准备一个数组 用来存放模板中8个单元格样式   也可以用集合
        for (int i = 1; i <= 8; i++) {
            cellStyles[i-1] = titleRow.getCell(i).getCellStyle();
        }

        for (ContractProductVo cpvo : contractProductVoList) {
            row = sheet.createRow(rowIndex);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyles[0]);
            cell.setCellValue(cpvo.getCustomName());

            cell = row.createCell(2);
            cell.setCellStyle(cellStyles[1]);
            cell.setCellValue(cpvo.getContractNo());

            cell = row.createCell(3);
            cell.setCellStyle(cellStyles[2]);
            cell.setCellValue(cpvo.getProductNo());
//{"客户","合同号","货号","数量","工厂","工厂交期","船期","贸易条款"};

            cell = row.createCell(4);
            cell.setCellStyle(cellStyles[3]);
            cell.setCellValue(cpvo.getCnumber());

            cell = row.createCell(5);
            cell.setCellStyle(cellStyles[4]);
            cell.setCellValue(cpvo.getFactoryName());


            cell = row.createCell(6);
            cell.setCellStyle(cellStyles[5]);
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cpvo.getDeliveryPeriod()) );

            cell = row.createCell(7);
            cell.setCellStyle(cellStyles[6]);
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cpvo.getShipTime()) );

            cell = row.createCell(8);
            cell.setCellStyle(cellStyles[7]);
            cell.setCellValue(cpvo.getTradeTerms());
            rowIndex++;
        }

//        文件的下载
//        一个流  文件的输出流
//        两个头  文件mime类型  文件的打开方式 in-line(在线打开) attachment
//        ServletOutputStream outputStream = response.getOutputStream();
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//设置MIME类型 常用的文件的mime类型可以省略
//        response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode("出货表.xlsx", "UTF-8"));
//        workbook.write(outputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //创建一个缓存流
//        byteArrayOutputStream.
        workbook.write(byteArrayOutputStream); //把workbook写入到缓存流中zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
        downloadUtil.download(byteArrayOutputStream,response,"出货表.xlsx");
    }

    @RequestMapping(value="/printExcelMillion" ,name="使用模板下载百万数据")
    public void printExcelMillion(String inputDate) throws Exception{
//       1、准备导出一个Excel
        Workbook workbook = new SXSSFWorkbook();
//        2、创建一个工作表
        Sheet sheet = workbook.createSheet("出货数据");
//        3、设置列宽
        sheet.setColumnWidth(1,26*256);
        sheet.setColumnWidth(2,15*256);
        sheet.setColumnWidth(3,26*256);
        sheet.setColumnWidth(4,15*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        sheet.setColumnWidth(7,15*256);
        sheet.setColumnWidth(8,15*256);

//        大标题
        Row bigTitleRow = sheet.createRow(0);
        bigTitleRow.setHeightInPoints(36);  //行高
//        创建单元格
        for (int i = 0; i < 9; i++) {
            bigTitleRow.createCell(i);
        }
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        Cell bigTitleRowCell = bigTitleRow.getCell(1);
        bigTitleRowCell.setCellStyle(bigTitle(workbook));
//        inputDate:2019年01    2018年10
        bigTitleRowCell.setCellValue(inputDate.replaceAll("-0","年").replaceAll("-","年")+"月份出货表");
//       小标题
//        客户	合同号	货号	数量	工厂	工厂交期	船期	贸易条款
        String[] titles = new String[]{"客户","合同号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        Row titleRow = sheet.createRow(1);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i+1);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(title(workbook));
        }
//        数据库中查询的需要出货的数据
        List<ContractProductVo> contractProductVoList = contractService.selectContractProductVoByShipTime(inputDate);

        int rowIndex = 2;
        Row row = null;
        Cell cell = null;
        for (ContractProductVo cpvo : contractProductVoList) {
            for (int i = 0; i < 6000; i++) {

            row = sheet.createRow(rowIndex);
            cell = row.createCell(1);
//            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getCustomName());

            cell = row.createCell(2);
//            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getContractNo());

            cell = row.createCell(3);
//            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getProductNo());
//{"客户","合同号","货号","数量","工厂","工厂交期","船期","贸易条款"};

            cell = row.createCell(4);
//            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getCnumber());

            cell = row.createCell(5);
//            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getFactoryName());


            cell = row.createCell(6);
//            cell.setCellStyle(text(workbook));
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cpvo.getDeliveryPeriod()) );

            cell = row.createCell(7);
//            cell.setCellStyle(text(workbook));
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cpvo.getShipTime()) );

            cell = row.createCell(8);
//            cell.setCellStyle(text(workbook));
            cell.setCellValue(cpvo.getTradeTerms());
            rowIndex++;
            }
        }

//        文件的下载
//        一个流  文件的输出流
//        两个头  文件mime类型  文件的打开方式 in-line(在线打开) attachment
//        ServletOutputStream outputStream = response.getOutputStream();
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//设置MIME类型 常用的文件的mime类型可以省略
//        response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode("出货表.xlsx", "UTF-8"));
//        workbook.write(outputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //创建一个缓存流
//        byteArrayOutputStream.
        workbook.write(byteArrayOutputStream); //把workbook写入到缓存流中zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
        downloadUtil.download(byteArrayOutputStream,response,"出货表.xlsx");
    }



    //大标题的样式
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);				//横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线

        return style;
    }


    @RequestMapping(value="/submit" ,name="提交购销合同方法")
    public String submit(String id){
        contractService.updateState(id); //更新状态 状态改为提交
        return "redirect:/cargo/contract/list.do";  //重定向到列表数据
    }



}
