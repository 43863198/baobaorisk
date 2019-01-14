package com.aizhixin.baobaorisk.redpackage.controller;

import com.aizhixin.baobaorisk.redpackage.service.PayService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller
@RequestMapping("/open/v1/wxpay")
@Slf4j
@Api(description = "提供给微信支付后台回调的API")
public class PayNotifyController {
    private final static String SUCCESS_STR = "<xml>" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>" +
            "  <return_msg><![CDATA[]]></return_msg>" +
            "</xml>";
    private final static String FAIL_STR = "<xml>" +
            "  <return_code><![CDATA[FAIL]]></return_code>" +
            "  <return_msg><![CDATA[支付数据处理失败]]></return_msg>" +
            "</xml>";
    @Autowired
    private PayService payService;
//    @Autowired
//    private PublishRedPackageTaskService publishRedPackageTaskService;

    @RequestMapping(value = "/notify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        String rs = SUCCESS_STR;
        try {
            StringBuilder xmlBuffer = new StringBuilder();
            BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            while ((line = tBufferedReader.readLine()) != null) {
                xmlBuffer.append(line);
            }
            String notifyXmlData = xmlBuffer.toString();// 支付结果通知的xml格式数据
            payService.payNotify(notifyXmlData);
        } catch (Exception e) {
            rs = FAIL_STR;
            log.warn("Weixin pay notify process data fail msg:{}", e);
        }
        try {
            response.getOutputStream().write(rs.getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.warn("Weixin pay notify out process result fail:{}", e);
        }
    }


//    @RequestMapping(value = "/pic")
//    public void pic(HttpServletRequest request, HttpServletResponse response) {
//        String picname = request.getParameter("picname");
//        if (null != picname) {
//            publishRedPackageTaskService.outPic(picname, response);
//        }
//    }
}
