<!DOCTYPE html>
<#--此文档是freemarker指定ftl文件-->
<#--如果发送的是get请求的话，只会得到本页面的源码，而不能二维码，只能跳转，让浏览器-->
<html>
<head>
    <meta charset="UTF-8">
    <title>支付</title>
</head>
<body>
<p>欢迎光临本网站</p>
<div id="myQrcode"></div>
<div id="orderId">${orderId}</div>
<div id="myQrcode">${returnUrl}</div>

<#--jquery,query.qrcode将codeUrl转换成二维码-->
<script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<script>
    jQuery('#myQrcode').qrcode({
        // text	: "weixin://wxpay/bizpayurl?pr=099JoOuzz"
        text	: "${codeUrl}" //控制类里return new ModelAndView("create",map);，codeUrl在map里,通过模板渲染传进来
    });

    $(function () {
        //定时器，不停的请求后端的api
        setInterval(function () {
            console.log('开始查询支付状态...')
            $.ajax({
                url:'/pay/queryByOrderId',
                data:{
                    'orderId': $('#orderId').text()
                },
                success:function (result) {
                    //将结果打印在控制台
                    console.log(result)
                    if(result.platformStatus != null
                    && result.platformStatus === 'SUCCESS')//三个等号是加上了类型判断
                        //跳转
                    location.href = $('#returnUrl').text()
                },
                error:function (result) {
                    //出现错误就弹个窗
                    alert(result)
                }
            })
        },2000)
    });
</script>
</body>
</html>