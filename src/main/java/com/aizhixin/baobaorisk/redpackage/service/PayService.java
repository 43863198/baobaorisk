package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.wxpay.WXPay;
import com.aizhixin.baobaorisk.common.wxpay.WXPayUtil;
import com.aizhixin.baobaorisk.redpackage.conf.WxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PayService {
    @Autowired
    private WxConfig wxConfig;

    public void createOrder() {
        WXPay wxpay = null;
        try {
            wxpay = new WXPay(wxConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> data = new HashMap<>();
        data.put("body", "包包大冒险-红包充值");
        data.put("out_trade_no", "2019010810595900000011");
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("openid", "oIqbS5GGXlcHxC4W9RIsbTKY4zkg");
        data.put("total_fee", "1");
        data.put("spbill_create_ip", "114.67.48.139");
        data.put("notify_url", "https://wx.aizhixin.com/open/v1/wxpay/notify");
        data.put("trade_type", "JSAPI");
//        data.put("product_id", "12");

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void payNotify(String xml) {
        System.out.println(xml);
        try {
            WXPay wxpay = new WXPay(wxConfig);
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);  // 转换成map
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                // 进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                System.out.println("签名正确");
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                System.out.println("签名错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
