<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<div class="pull-left">
    <div class="form-group form-inline">
        总共${page.totalPage} 页，共${page.total} 条数据。
    </div>
</div>

<div class="box-tools pull-right">
    <ul class="pagination" style="margin: 0px;">
        <li >
            <a href="javascript:goPage(1)" aria-label="Previous">首页</a>
        </li>
        <li><a href="javascript:goPage(${page.pre})">上一页</a></li>
        <c:forEach begin="${page.start}" end="${page.end}" var="i">
            <li class="paginate_button ${page.pageNum==i ? 'active':''}"><a href="javascript:goPage(${i})">${i}</a></li>
        </c:forEach>
        <li><a href="javascript:goPage(${page.next})">下一页</a></li>
        <li>
            <a href="javascript:goPage(${page.totalPage})" aria-label="Next">尾页</a>
        </li>
    </ul>
</div>
<form id="pageForm" action="${param.pageUrl}" method="post">
    <input type="hidden" name="pageNum" id="pageNum">
</form>
<script>
    function goPage(page) {  //page就是即将跳转页面的页码
        document.getElementById("pageNum").value = page
        document.getElementById("pageForm").submit()
    }
</script>
</body>
</html>
