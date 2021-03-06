<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../../base.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 页面meta /-->
</head>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
    <!-- 内容头部 -->
    <section class="content-header">
        <h1>
            货运管理
            <small>合同下货物详情</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
            <li><a href="all-order-manage-list.html">货运管理</a></li>
            <li class="active">货物添加及列表</li>
        </ol>
    </section>
    <!-- 内容头部 /-->


    <!-- 正文区域 -->
    <section class="content">

        <!--订单信息-->

        <div class="panel panel-default">
            <div class="panel-heading">新增委托单</div>

            <div class="row data-type" style="margin: 0px">

                <div class="row data-type" style="margin: 0px">
                    <form id="editForm" action="${ctx}/cargo/shipping/edit.do" method="post" onsubmit="return check()">
                        <input type="hidden" name="shippingOrderId" value="${shipping.shippingOrderId}">

                        <div class="col-md-2 title">海运/空运</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="海运/空运" name="offeror"
                                   value="${shipping.orderType}">
                        </div>
                        <div class="col-md-2 title">货主</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="货主" name="shipper"
                                   value="${shipping.shipper}">
                        </div>
                        <div class="col-md-2 title">提单抬头</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="提单抬头" name="consignee"
                                   value="${shipping.consignee}">
                        </div>
                        <div class="col-md-2 title">正本通知人</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="正本通知人" name="notifyParty"
                                   value="${shipping.notifyParty}">
                        </div>

                        <div class="col-md-2 title">信用证</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="信用证" name="lcNo"
                                   value="${shipping.lcNo}">
                        </div>

                        <div class="col-md-2 title">装运港</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="装运港" name="portOfLoading"
                                   value="${shipping.portOfLoading}">
                        </div>
                        <div class="col-md-2 title">转船港</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="转船港" name="portOfTrans"
                                   value="${shipping.portOfTrans}">
                        </div>
                        <div class="col-md-2 title">卸货港</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="卸货港" name="portOfDischarge"
                                   value="${shipping.portOfDischarge}">
                        </div>

                        <div class="col-md-2 title">装期</div>
                        <div class="col-md-4 data">
                            <div class="input-group date">
                                <div class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </div>
                                <input type="text" placeholder="装期" name="loadingDate" class="form-control pull-right"
                                       value="<fmt:formatDate value="${shipping.loadingDate}" pattern="yyyy-MM-dd"/>"
                                       id="signingDate">
                            </div>
                        </div>
                        <div class="col-md-2 title">效期</div>
                        <div class="col-md-4 data">
                            <div class="input-group date">
                                <div class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </div>
                                <input type="text" placeholder="效期" name="limitDate" class="form-control pull-right"
                                       value="<fmt:formatDate value="${shipping.limitDate}" pattern="yyyy-MM-dd"/>"
                                       id="shipTime">
                            </div>
                        </div>
                        <div class="col-md-2 title">是否分批</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="是否分批" name="isBatch"
                                   value="${shipping.isBatch}">
                        </div>
                        <div class="col-md-2 title">是否转船</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="是否转船" name="isTrans"
                                   value="${shipping.isTrans}">
                        </div>
                        <div class="col-md-2 title">份数</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="份数" name="copyNum"
                                   value="${shipping.copyNum}">
                        </div>
                        <div class="col-md-2 title">扼要说明</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="扼要说明" name="remark"
                                   value="${shipping.remark}">
                        </div>
                        <div class="col-md-2 title">运输要求</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="运输要求" name="specialCondition"
                                   value="${shipping.specialCondition}">
                        </div>
                        <div class="col-md-2 title">复核人</div>
                        <div class="col-md-4 data">
                            <input type="text" class="form-control" placeholder="复核人" name="checkBy"
                                   value="${shipping.checkBy}">
                        </div>
                    </form>
                </div>
            </div>
            <!--订单信息/-->

            <!--工具栏-->
            <div class="box-tools text-center">
                <button type="button" onclick='document.getElementById("editForm").submit()' class="btn bg-maroon">保存
                </button>
                <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
            </div>
        </div>
        <!--工具栏/-->

    </section>
    <!-- 正文区域 /-->

    <section class="content">

        <!-- .box-body -->
        <div class="box box-primary">
            <div class="box-header with-border">
                <h3 class="box-title">货物列表</h3>
            </div>

            <div class="box-body">

                <!-- 数据表格 -->
                <div class="table-box">
                    <!--数据列表-->
                    <table id="dataList" class="table table-bordered table-striped table-hover dataTable">
                        <thead>
                        <tr class="rowTitle" align="middle">
                            <td class="tableHeader"><input type="checkbox" name="selid" onclick="checkAll('id',this)">
                            </td>
                            <td width="33">序号</td>
                            <td width="60px">卖方</td>
                            <td width="90px">买方</td>
                            <td width="90px">发票号</td>
                            <td width="90px">发票日期</td>
                            <td width="90px">唛头</td>
                            <td width="90px">描述</td>
                        </tr>
                        </thead>
                        <tbody class="tableBody">
                        ${links}
                        <c:forEach items="${page.list}" var="o" varStatus="status">
                            <tr class="odd" onmouseover="this.className='highlight'" onmouseout="this.className='odd'">
                                <td><input type="checkbox" name="id" value="${o.shippingOrderId}"/></td>
                                <td>${status.index+1}</td>
                                <td>${o.seller}</td>
                                <td>
                                    <input style="width: 90px" name="buyer" value="${o.buyer}">
                                </td>
                                <td>
                                    <input style="width: 90px" name="invoiceNo" value="${o.invoiceNo}">
                                </td>
                                <td>
                                    <input style="width: 90px" name="invoiceDate" value="${o.invoiceDate}">
                                </td>
                                <td>
                                    <input style="width: 90px" name="marks" value="${o.marks}">
                                </td>
                                <td>
                                    <input style="width: 90px" name="descriptions" value="${o.descriptions}">
                                </td>

                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <!--数据列表/-->
                    <!--工具栏/-->
                </div>
                <!-- 数据表格 /-->
            </div>
            <!-- /.box-body -->

            <!-- .box-footer-->
            <div class="box-footer">
                <jsp:include page="../../common/page.jsp">
                    <jsp:param value="/cargo/shipping/list.do?" name="pageUrl"/>
                </jsp:include>
            </div>
            <!-- /.box-footer-->
        </div>

    </section>

</div>
<!-- 内容区域 /-->
</body>
<script>
    function check() {
        var id = ${}

    }
</script>

</html>