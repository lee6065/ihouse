<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <script src="${ctx}/plugins/jQuery/jquery-2.2.3.min.js"></script>
</head>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
    <!-- 内容头部 -->
    <section class="content-header">
        <h1>
            委托单管理
            <small>委托单</small>
        </h1>
        <ol class="breadcrumb"></ol>
    </section>

    <!-- 正文区域 -->
    <section class="content">
        <div class="panel panel-default">
            <div class="panel-heading">委托单详情</div>

                <div class="row data-type" style="margin: 0px">
                    <div class="col-md-2 title">提单抬头</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.consignee}
                    </div>

                    <div class="col-md-2 title">海运/空运</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.orderType}
                    </div>

                    <div class="col-md-2 title">货主</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.shipper}
                    </div>

                    <div class="col-md-2 title">正本通知人</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.notifyParty}
                    </div>

                    <div class="col-md-2 title">信用证</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.lcNo}
                    </div>

                    <div class="col-md-2 title">装运港</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.portOfLoading}
                    </div>

                    <div class="col-md-2 title">转船港</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.portOfTrans}
                    </div>

                    <div class="col-md-2 title">卸货港</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.portOfDischarge}
                    </div>

                    <div class="col-md-2 title">装期</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        <fmt:formatDate value="${shipping.loadingDate}" pattern="yyyy-MM-dd"/>

                    </div>
                    <div class="col-md-2 title">效期</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        <fmt:formatDate value="${shipping.limitDate}" pattern="yyyy-MM-dd"/>

                    </div>
                    <div class="col-md-2 title ">是否分批</div>
                    <div class="col-md-4 data " style="line-height:34px">
                        ${shipping.isBatch}
                    </div>
                    <div class="col-md-2 title ">是否转船</div>
                    <div class="col-md-4 data " style="line-height:34px">
                        ${shipping.isTrans}
                    </div>
                    <div class="col-md-2 title">份数</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.copyNum}
                    </div>
                    <div class="col-md-2 title">扼要说明</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.remark}
                    </div>
                    <div class="col-md-2 title">运输要求</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.specialCondition}
                    </div>

                    <div class="col-md-2 title">复核人</div>
                    <div class="col-md-4 data" style="line-height:34px">
                        ${shipping.checkBy}
                    </div>
                </div>
        </div>
    </section>
</div>
<!-- 内容区域 /-->
</body>

</html>