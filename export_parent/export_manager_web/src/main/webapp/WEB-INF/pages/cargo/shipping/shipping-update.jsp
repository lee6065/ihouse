<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../../base.jsp"%>
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
            委托单管理
            <small>委托单表单</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
            <li><a href="all-order-manage-list.html">委托单管理</a></li>
            <li class="active">委托单表单</li>
        </ol>
    </section>
    <!-- 内容头部 /-->

    <!-- 正文区域 -->
    <section class="content">

        <!--订单信息-->
        <div class="panel panel-default">
            <div class="panel-heading">委托单</div>
            <form id="editForm" action="${ctx}/cargo/shipping/edit.do" method="post">
                <input type="hidden" name="shippingOrderId" value="${shipping.shippingOrderId}">
                <div class="row data-type" style="margin: 0px">
                    <div class="col-md-2 title">海运/空运</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="海运/空运" name="offeror" value="${shipping.orderType}">
                    </div>

                    <div class="col-md-2 title">货主</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="货主" name="shipper" value="${shipping.shipper}">
                    </div>



                    <div class="col-md-2 title">提单抬头</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="提单抬头" name="consignee" value="${shipping.consignee}">
                    </div>

                    <div class="col-md-2 title">正本通知人</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="正本通知人" name="notifyParty" value="${shipping.notifyParty}">
                    </div>

                    <div class="col-md-2 title">信用证</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="信用证" name="lcNo" value="${shipping.lcNo}">
                    </div>

                    <div class="col-md-2 title">装运港</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="装运港" name="portOfLoading" value="${shipping.portOfLoading}">
                    </div>
                    <div class="col-md-2 title">转船港</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="转船港" name="portOfTrans" value="${shipping.portOfTrans}">
                    </div>
                    <div class="col-md-2 title">卸货港</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="卸货港" name="portOfDischarge" value="${shipping.portOfDischarge}">
                    </div>

                    <div class="col-md-2 title">装期</div>
                    <div class="col-md-4 data">
                        <div class="input-group date">
                            <div class="input-group-addon">
                                <i class="fa fa-calendar"></i>
                            </div>
                            <input type="text" placeholder="装期"  name="loadingDate" class="form-control pull-right"
                                   value="<fmt:formatDate value="${shipping.loadingDate}" pattern="yyyy-MM-dd"/>" id="signingDate">
                        </div>
                    </div>
                    <div class="col-md-2 title">效期</div>
                    <div class="col-md-4 data">
                        <div class="input-group date">
                            <div class="input-group-addon">
                                <i class="fa fa-calendar"></i>
                            </div>
                            <input type="text" placeholder="效期"  name="limitDate" class="form-control pull-right"
                                   value="<fmt:formatDate value="${shipping.limitDate}" pattern="yyyy-MM-dd"/>" id="shipTime">
                        </div>
                    </div>
                    <div class="col-md-2 title">是否分批</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="是否分批" name="isBatch" value="${shipping.isBatch}">
                    </div>
                    <div class="col-md-2 title">是否转船</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="是否转船" name="isTrans" value="${shipping.isTrans}">
                    </div>
                    <div class="col-md-2 title">份数</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="份数" name="copyNum" value="${shipping.copyNum}">
                    </div>
                    <div class="col-md-2 title">扼要说明</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="扼要说明" name="remark" value="${shipping.remark}">
                    </div>
                    <div class="col-md-2 title">运输要求</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="运输要求" name="specialCondition" value="${shipping.specialCondition}">
                    </div>
                    <div class="col-md-2 title">复核人</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="复核人" name="checkBy" value="${shipping.checkBy}">
                    </div>

<%--                    <div class="col-md-2 title">重要程度</div>--%>
<%--                    <div class="col-md-4 data">--%>
<%--                        <div class="form-group form-inline">--%>
<%--                            <div class="radio"><label><input type="radio" ${contract.importNum==3?'checked':''} name="importNum" value="3">★★★</label></div>--%>
<%--                            <div class="radio"><label><input type="radio" ${contract.importNum==2?'checked':''} name="importNum" value="2">★★</label></div>--%>
<%--                            <div class="radio"><label><input type="radio" ${contract.importNum==1?'checked':''} name="importNum" value="1">★</label></div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

                </div>
          </form>
        </div>
        <!--订单信息/-->

        <!--工具栏-->
        <div class="box-tools text-center">
            <button type="button" onclick='document.getElementById("editForm").submit()' class="btn bg-maroon">保存</button>
            <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
        </div>
        <!--工具栏/-->

    </section>
    <!-- 正文区域 /-->

</div>
<!-- 内容区域 /-->
</body>
<script src="../../plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="../../plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<link rel="stylesheet" href="../../css/style.css">
<script>
    $('#signingDate').datepicker({
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
    $('#deliveryPeriod').datepicker({
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
    $('#shipTime').datepicker({
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
</script>
</html>