package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.wxbasic.Utility;
import com.aizhixin.baobaorisk.common.wxpay.WXPay;
import com.aizhixin.baobaorisk.common.wxpay.WXPayUtil;
import com.aizhixin.baobaorisk.redpackage.conf.WxConfig;
import com.aizhixin.baobaorisk.redpackage.entity.PayOrder;
import com.aizhixin.baobaorisk.redpackage.manager.PayOrderManager;
import com.aizhixin.baobaorisk.redpackage.vo.WxPrePayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class PayService {
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private PayOrderManager payOrderManager;

    public void createOrder() {
        WXPay wxpay = null;
        try {
            wxpay = new WXPay(wxConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> data = new HashMap<>();
        data.put("body", "包包大冒险-红包充值");
        data.put("out_trade_no", "2019010810595900000013");
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

    public WxPrePayVO createOrder(String openId, Double totalFee, Integer num) {
        WXPay wxpay = null;
        WxPrePayVO vo = new WxPrePayVO ();
        if (StringUtils.isEmpty(openId) || null == totalFee ||
                totalFee <= 0.01 || totalFee > 200 || null == num ||
                num <= 0 || num > 100 || totalFee/num < 0.01) {
            vo.setReturn_code("FAIL");
            return vo;
        }
        try {
            wxpay = new WXPay(wxConfig);
        } catch (Exception e) {
            log.warn("Create order fail.{}", e);
        }


        PayOrder o = new PayOrder ();
        o.setTradeName("包包大冒险-红包充值");//订单详情
        o.setTradeNo(Utility.generateUUID());//订单号
        o.setTotalFee((int)(totalFee * 100));
        o.setOpenId(openId);

        Map<String, String> data = new HashMap<>();
        data.put("body", o.getTradeName());
        data.put("out_trade_no", o.getTradeNo());
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("openid", o.getOpenId());
        data.put("total_fee", o.getTotalFee().toString());
        data.put("spbill_create_ip", "114.67.48.139");
        data.put("notify_url", "https://wx.aizhixin.com/open/v1/wxpay/notify");
        data.put("trade_type", "JSAPI");

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            if ("SUCCESS".equalsIgnoreCase(resp.get("result_code")) && !StringUtils.isEmpty(resp.get("prepay_id"))) {
                o.setPrepayId(resp.get("prepay_id"));
                o.setNonceStr(resp.get("nonce_str"));
                vo.setNonceStr(o.getNonceStr());
                vo.setPrepayId(o.getPrepayId());
                vo.setPayPackage("WXPay");
                vo.setTimestamp(Utility.getCurrentTimeStamp());
                vo.setSignType("MD5");
                vo.setAppId(wxConfig.getAppID());
                vo.setMchId(wxConfig.getMchID());

                Map<String,String> repData = new HashMap<>();
                repData.put("appId",vo.getAppId());
                repData.put("mchId",vo.getMchId());
                repData.put("prepayId",vo.getPrepayId());
                repData.put("package",vo.getPayPackage());
                repData.put("nonceStr",vo.getNonceStr());
                repData.put("timeStamp",vo.getTimestamp());
                String sign = WXPayUtil.generateSignature(repData,wxConfig.getKey()); //签名
                vo.setSign(sign);
                payOrderManager.save(o);
            } else {
                log.info("WEIXIN Order trade prepay fail. trade_no:{}, total_fee:{}", o.getTradeNo(), o.getTotalFee());
                vo.setReturn_code("FAIL");
            }
        } catch (Exception e) {
            log.warn("Invoke weixin pre pay fail.{}", e);
        }
        return vo;
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
