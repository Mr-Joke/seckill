<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>秒杀详情页</title>
    <%@include file="common/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="panel-heading">
            <h1>${seckill.seckillName}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
                <%-- 显示timer图标 --%>
                <span class="glyphicon glyphicon-time"></span>
                <%-- 展示倒计时 --%>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>
<%-- 登录弹出层，输入电话号码 --%>
<div id="killPhoneModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="mopal-title modal-center">
                    <span class="glyphicon glyphicon-phone"></span>秒杀电话：
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killPhone" id="killPhoneKey"
                               placeholder="填写手机号^_^" class="form-control"/>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <%-- 验证信息 --%>
                <span id="killPhoneMessage" class="glyphicon"></span>
                <button id="killPhoneBtn" type="button" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    Submit
                </button>
            </div>
        </div>
    </div>
</div>
</body>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<%-- jQuery cookie 操作插件 --%>
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<%-- jQuery countdown 倒计时插件--%>
<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
<script src="/resources/js/seckill.js"></script>
<script type="text/javascript">
    $(function () {
       seckill.detail.init({
           seckillId : ${seckill.seckillId},
           seckillStartTime : ${seckill.seckillStartTime.time},//毫秒形式
           seckillEndTime : ${seckill.seckillEndTime.time}
       });
    });
</script>
</html>
