//存放主要交互逻辑js代码
// javascript模块化
//eg : seckill.detail.init(params);
var seckill = {
    //封装秒杀相关ajax的url
    URL : {
        now : function () {
          return '/seckill/time/now';
        },
        exposer : function (seckillId) {
            return '/seckill/'+ seckillId + '/exposer';
        },
        execution : function (seckillId,md5) {
            return '/seckill/' + seckillId + '/' + md5 + 'execution';
        }
    },
    handlerSeckillkill : function (seckillId,node) {
        //获取秒杀地址，控制显示逻辑，执行秒杀
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //在回调函数中执行交互流程
            if (result && result['success']){
                var exposer = result['data'];
                if (exposer['exposed']){
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    //给按钮注册点击事件
                    //TODO
                }else{
                    //未开启秒杀，由于pc机与服务器计时不一致
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计时
                    seckill.countdown(seckillId,now,start,end);
                }
            }else{

            }
        });
    },
    //验证手机号
    validatePhone : function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)){
            return true;
        }else{
            return false;
        }
    },
    countdown : function (seckillId, nowTime, seckillStartTime, seckillEndTime) {
        var seckillBox = $('#seckill-box');
        //时间判断
        if (nowTime > seckillEndTime){
            //秒杀结束
            seckillBox.html('秒杀结束!');
        }else if (nowTime < seckillStartTime){
            //秒杀尚未开启,计时时间绑定
            var killTime = new Date(seckillStartTime + 1000);
            seckillBox.countdown(killTime,function (event) {
               //时间格式
               var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
               seckillBox.html(format);
               /*时间完成后回调事件*/
            }).on('finish.countdown',function () {
                //获取秒杀地址，控制显示逻辑，执行秒杀
                seckill.handlerSeckillkill(seckillId,seckillBox);
            });
        }else{
            //秒杀开启
            seckill.handlerSeckillkill(seckillId,seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail : {
        //详情页初始化
        init : function (params) {
            //手机验证和登录、计时交互
            //规划交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var seckillId = params['seckillId'];
            var seckillStartTime = params['seckillStartTime'];
            var seckillEndTime = params['seckillEndTime'];
            //验证手机号
            if (!seckill.validatePhone(killPhone)){
                //绑定phone
                //控制输出
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    //显示弹出层
                    show : true,
                    backdrop : 'static',//禁止位置关闭
                    keyboard : false //关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)){
                        //电话写入cookie
                        $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});//绑定cookie只在“/seckill”路径下生效
                        //刷新页面
                        window.location.reload();
                    }else{
                        //填写手机号错误
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }
            //已经登录
            //计时交互
            $.get(seckill.URL.now(),{},function (result) {
                if (result && result['success']){
                    var nowTime = result['data'];
                    //判断时间，计时交互
                    seckill.countdown(seckillId,nowTime,seckillStartTime,seckillEndTime);
                }else{
                    console.log("result : " + result);
                }
            });
        }
    }
}