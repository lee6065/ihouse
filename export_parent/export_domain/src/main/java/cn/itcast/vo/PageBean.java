package cn.itcast.vo;

import java.io.Serializable;
import java.util.List;

//分页的工具类
public class PageBean implements Serializable {

//    1、当前页码
    private int pageNum;
//    2、每页显示条数
    private int pageSize;
//    3、每页的数据
    private List list;
//    4、总条数
    private Long total;
//    5、总页数
   private int totalPage;
//   6、上一页
    private int pre;
//    7、下一页
    private int next;
//    8、起始页码
    private int start;
//    9、结束页码
    private int end;
  /*
   页面上传过来的： 当前页码  每页显示条数
    数据库查询的：   每页的数据  总条数
    通过计算：      总页数  上一页  下一页  起始页码  结束页码
    */
  public PageBean(int pageNum,int pageSize,List list,Long total){
      this.pageNum=pageNum;
      this.pageSize=pageSize;
      this.list=list;
      this.total=total;
//   在构造方法中计算
//      上一页
       this.pre=pageNum-1;
//      下一页
       this.next=pageNum+1;
//      总页数  10条 每页显示2条 总共4页
      if(total%pageSize==0){
          this.totalPage=(int)(total/pageSize);
      }else{
          this.totalPage=(int)((total/pageSize)+1);
      }
//      起始页码  结束页码
//      如果总页数大于5时
      if(totalPage<=5){
//          起始页码=1  结束页码=总页数
          this.start=1;
          this.end=totalPage;
      }else{  //总页数大于5  页码一直显示5个数
//        情况1：当前页面如果是小于等于3  起始页1 结束页5
          if(pageNum<=3){
              this.start=1;
              this.end=5;
          }
//          情况2：如果总页数-当前页 小于等于2    起始页=总页数-4 结束页就是总页数
          else if((totalPage-pageNum)<=2){
              this.start=totalPage-4;
              this.end=totalPage;
          }else{
//              以当前页为中心 前面两个数 后面两个数
              this.start=pageNum-2;
              this.end=pageNum+2;
          }
      }
  }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPre() {
        return pre;
    }

    public void setPre(int pre) {
        this.pre = pre;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
