<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../base.jsp"%>
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
    <section class="content-header">
        <h1>
            统计分析
            <small>系统访问压力图</small>
        </h1>
    </section>
    <section class="content">
        <div class="box box-primary">
            <div id="main" style="width: 600px;height:400px;"></div>
        </div>
    </section>
</div>
</body>

<script src="../plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="../../plugins/echarts/echarts.min.js"></script>
<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    // 指定图表的配置项和数据
    $.get('/stat/onlineCharts.do').done(function (data) {
        // 使用刚指定的配置项和数据显示图表。
        // data：[{name:00,value:1},{name:01,value:2},{name:02,value:10}]
        let titles=[];
        let datas=[];
        for (let i = 0; i < data.length; i++) {
            titles[i] = data[i].name;
            datas[i] = data[i].value;

        }
        myChart.setOption(
            {
                title: {
                    left: 'center',
                    text: '系统访问压力图',
                },
                xAxis: {
                    type: 'category',
                    data: titles
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    data: datas,
                    type: 'line'
                }]
            }
        )
    });
</script>

</html>