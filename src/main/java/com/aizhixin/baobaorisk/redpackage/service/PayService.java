package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.wxbasic.Utility;
import com.aizhixin.baobaorisk.common.wxpay.WXPay;
import com.aizhixin.baobaorisk.common.wxpay.WXPayUtil;
import com.aizhixin.baobaorisk.redpackage.conf.WxConfig;
import com.aizhixin.baobaorisk.redpackage.core.TradeStatus;
import com.aizhixin.baobaorisk.redpackage.entity.PayOrder;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.manager.PayOrderManager;
import com.aizhixin.baobaorisk.redpackage.manager.RedTaskManager;
import com.aizhixin.baobaorisk.redpackage.vo.WxPrePayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Transactional
public class PayService {
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private PayOrderManager payOrderManager;
    @Autowired
    private RedTaskManager redTaskManager;

    public WxPrePayVO createOrder(String openId, Double totalFee, Integer num, String taskName) {
        WXPay wxpay = null;
        WxPrePayVO vo = new WxPrePayVO ();
        if (StringUtils.isEmpty(openId) || null == totalFee ||
                totalFee <= 0.01 || totalFee > 200 || null == num ||
                num <= 0 || num > 100 || totalFee/num < 0.01) {
            log.info("Param validator fail.openId:{}, totoalFee:{}, num:{}", openId, totalFee, num);
            vo.setReturn_code("FAIL");
            return vo;
        }
        try {
            wxpay = new WXPay(wxConfig);
        } catch (Exception e) {
            log.warn("Create order fail.{}", e);
        }


        PayOrder o = new PayOrder ();
        o.setTaskName(taskName);
        o.setTradeName("包包大冒险-红包充值");//订单详情
        o.setTradeNo(Utility.generateUUID());//订单号
        o.setTotalFee((int)(totalFee * 100));//红包金额
        o.setNum(num);//红包数量
        o.setOpenId(openId);

        Map<String, String> data = new HashMap<>();
        data.put("body", o.getTradeName());
        data.put("out_trade_no", o.getTradeNo());
        data.put("fee_type", "CNY");
        data.put("openid", o.getOpenId());
        data.put("total_fee", o.getTotalFee().toString());
        data.put("spbill_create_ip", "114.67.48.139");
        data.put("notify_url", "https://wx.aizhixin.com/open/v1/wxpay/notify");
        data.put("trade_type", "JSAPI");

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.info("weixin prePay back msg:{}", resp);
            if ("SUCCESS".equalsIgnoreCase(resp.get("return_code")) && "SUCCESS".equalsIgnoreCase(resp.get("result_code")) && !StringUtils.isEmpty(resp.get("prepay_id"))) {
                o.setPrepayId(resp.get("prepay_id"));

                vo.setNonceStr(Utility.generateUUID());
                vo.setPayPackage("prepay_id=" + o.getPrepayId());
                vo.setTimestamp(Utility.getCurrentTimeStamp());
                vo.setSignType("MD5");
                vo.setAppId(wxConfig.getAppID());

                Map<String,String> repData = new HashMap<>();
                repData.put("appId",vo.getAppId());
                repData.put("package",vo.getPayPackage());
                repData.put("nonceStr",vo.getNonceStr());
                repData.put("signType", vo.getSignType());
                repData.put("timeStamp",vo.getTimestamp());
                String sign = WXPayUtil.generateSignature(repData,wxConfig.getKey()); //签名
                vo.setAppId(null);
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


    public void payNotify(String xml) throws Exception {
        log.info("Weixin pay notify xml data:{}", xml);
        WXPay wxpay = new WXPay(wxConfig);
        Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);  // 转换成map
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确 进行处理。
            // 暂不处理，注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
            String tradeNo = notifyMap.get("out_trade_no");
            if ("SUCCESS".equalsIgnoreCase(notifyMap.get("result_code")) &&
                    "SUCCESS".equalsIgnoreCase(notifyMap.get("return_code")) &&
                    !StringUtils.isEmpty(tradeNo)) {
                createTask(tradeNo, notifyMap);//创建红包任务
            } else {
                log.info("支付失败：{}", notifyMap);
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            log.info("签名错误:{}", notifyMap);
        }
    }

    private void createTask(String tradeNo, Map<String, String> notifyMap) {
        PayOrder o = payOrderManager.findByTradeNo(tradeNo);
        if (null != o) {
            if (TradeStatus.PrePaySuccess.getStateCode() != o.getPayStatus()) {//首次接收到成功支付回调
                o.setPayStatus(TradeStatus.PrePaySuccess.getStateCode());
                o.setTransactionId(notifyMap.get("transaction_id"));
                o.setTimeEnd(notifyMap.get("time_end"));
                o.setBankType(notifyMap.get("bank_type"));
                String t = notifyMap.get("cash_fee");
                if (null != t) {
                    o.setCashFee(new Integer(t));
                }
                t = notifyMap.get("total_fee");
                if (null != t) {
                    if (o.getTotalFee().intValue() != new Integer(t)) {
                        log.warn("Weixin pay notify order fee is:({}), but cash fee is:({})", o.getTotalFee(), t);
                    }
                }
                payOrderManager.save(o);

                RedTask r = new RedTask();
                r.setOpenId(o.getOpenId());//任务发起人
                r.setTradeNo(o.getTradeNo());//订单号
                r.setTaskName(o.getTaskName());//任务名称
                r.setNum(o.getNum());//红包数量
                r.setTotalFee(o.getCashFee());//红包金额
                redTaskManager.save(r);
            }//否则重复调用，不予理睬
        } else {
            log.warn("Weixin pay notify not found order by tradeNo:{}", tradeNo);
        }
    }
}
