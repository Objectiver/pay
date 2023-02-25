package com.imooc.pay.controller;

import com.imooc.pay.pojo.PayInfo;
import com.imooc.pay.service.impl.PayService;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.lang.model.element.VariableElement;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangzhao
 * @Date 2022/12/2
 * @description
 */
@Controller   //不用RestController，因为要渲染一个网页
@RequestMapping("/pay")
@Slf4j
public class PayController {
    @Autowired
    private PayService payService;

    @Autowired
    private WxPayConfig wxPayConfig;
    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId")String orderId,
                                @RequestParam("amount") BigDecimal amount,
                                @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum){
        PayResponse response = payService.create(orderId, amount,bestPayTypeEnum);//在此已经将支付信息写入数据库


        Map<String,String> map = new HashMap<>();
        //支付方式不同，渲染就不同, WXPAY_NATIVE使用codeUrl,  ALIPAY_PC使用body
        if(bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE){
            //微信返回codeUrl，再生成二维码
            map.put("codeUrl",response.getCodeUrl() );
            map.put("orderId",orderId);
            map.put("returnUrl",wxPayConfig.getReturnUrl());//在wxPayConfig里
            return new ModelAndView("createForWxNative02",map);
        }else if(bestPayTypeEnum ==  BestPayTypeEnum.ALIPAY_PC){
            map.put("body",response.getBody() );
            return new ModelAndView("createForAlipayPc",map);
        }
        throw new RuntimeException("暂不支持的支付类型");


    }

    @PostMapping("/notify")
    @ResponseBody  //不加会报404
    public String asyncNotify(@RequestBody String notifyData){
        //log.info("notifyData{}",notifyData);
        return payService.asyncNotify(notifyData);
    }

    @GetMapping("/queryByOrderId")
    @ResponseBody//要么controller加RestController,要么这里加，不然页面报404
    public PayInfo queryByOrderId(@RequestParam String orderId){
        log.info("查询支付记录..." );
        return payService.queryByOrderId(orderId);
    }
}
