package com.aizhixin.baobaorisk.redpackage.controller;

import com.aizhixin.baobaorisk.redpackage.service.PayService;
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
public class PayNotifyController {

    @Autowired
    private PayService payService;

    @RequestMapping(value = "/notify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        log.info("Call back invoke....");
        String notifyData = "...."; // 支付结果通知的xml格式数据
        StringBuilder tStringBuffer = new StringBuilder();
        try {
            BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String sTempOneLine = new String("");
            while ((sTempOneLine = tBufferedReader.readLine()) != null) {
                tStringBuffer.append(sTempOneLine);
            }
        } catch (Exception e) {
            log.warn("Call back fail msg:{}", e);
        }
        notifyData = tStringBuffer.toString();
        payService.payNotify(notifyData);
        String rs = "<xml>" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>" +
                "  <return_msg><![CDATA[OK]]></return_msg>" +
                "</xml>";
        try {
            response.getOutputStream().write(rs.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
