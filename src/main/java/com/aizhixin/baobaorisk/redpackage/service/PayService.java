package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.wxbasic.Utility;
import com.aizhixin.baobaorisk.common.wxpay.WXPay;
import com.aizhixin.baobaorisk.common.wxpay.WXPayUtil;
import com.aizhixin.baobaorisk.redpackage.conf.WxConfig;
import com.aizhixin.baobaorisk.redpackage.core.TradeStatus;
import com.aizhixin.baobaorisk.redpackage.core.WeixinContants;
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

    /**
     * 创建红包订单任务（用于记录支付情况）
     */
    public WxPrePayVO createOrder(String openId, Double totalFee, Integer num, String taskName) {
        WXPay wxpay;//微信预支付对象
        WxPrePayVO vo = new WxPrePayVO ();//小程序用户支付确认所需数据对象

        /***********************用户输入参数的简单验证***********************/
        if (StringUtils.isEmpty(openId) || null == totalFee ||
                totalFee <= 0.01 || totalFee > 200 || null == num ||
                num <= 0 || num > 100 || totalFee/num < 0.01) {
            log.info("Param validator fail.openId:{}, totoalFee:{}, num:{}", openId, totalFee, num);
            vo.setReturn_code(WeixinContants.FAIL);
            return vo;
        }

        /***********************用户支付订单数据构造***********************/
        PayOrder o = new PayOrder ();
        o.setTaskName(taskName);
        o.setTradeName(wxConfig.getTradeName());//订单详情
        o.setTradeNo(Utility.generateUUID());//订单号
        o.setTaskName(taskName);//红包要求的说明
        o.setTotalFee((int)(totalFee * 100));//红包金额
        o.setNum(num);//红包数量
        o.setOpenId(openId);

        /***********************预支付数据构造及调用***********************/
        Map<String, String> data = new HashMap<>();
        data.put("body", o.getTradeName());
        data.put("out_trade_no", o.getTradeNo());
        data.put("fee_type", WeixinContants.FEE_TYPE);
        data.put("openid", o.getOpenId());
        data.put("total_fee", o.getTotalFee().toString());
        data.put("spbill_create_ip", wxConfig.getCreateIp());
        data.put("notify_url", wxConfig.getNotifyUrl());
        data.put("trade_type", WeixinContants.TRADE_TYPE);

        try {
            wxpay = new WXPay(wxConfig);
            Map<String, String> resp = wxpay.unifiedOrder(data);//调用微信预支付
            log.info("Weixin Order create prePay back msg:{}", resp);
            if (WeixinContants.SUCCESS.equalsIgnoreCase(resp.get("return_code")) &&
                    WeixinContants.SUCCESS.equalsIgnoreCase(resp.get("result_code")) &&
                    !StringUtils.isEmpty(resp.get("prepay_id"))) {//预支付成功
                o.setPrepayId(resp.get("prepay_id"));
                payOrderManager.save(o);//支付订单数据保存

                /***********************小程序用户支付确认所需数据对象数据构造***********************/
                vo.setNonceStr(Utility.generateUUID());
                vo.setPayPackage("prepay_id=" + o.getPrepayId());
                vo.setTimestamp(Utility.getCurrentTimeStamp());
                vo.setSignType(WeixinContants.SIGN_TYPE);
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
            } else {
                log.info("Weixin Order create prepay fail. trade_no:{}, total_fee:{}", o.getTradeNo(), o.getTotalFee());
                vo.setReturn_code(WeixinContants.FAIL);
            }
        } catch (Exception e) {
            log.warn("Weixin Order create Invoke weixin pre pay fail.{}", e);
            vo.setReturn_code(WeixinContants.FAIL);
        }
        return vo;
    }

    /**
     * 支付扣款完成回调操作
     */
    public void payNotify(String xml) throws Exception {
        WXPay wxpay = new WXPay(wxConfig);
        Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);  // 转换成map
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名， 签名正确 进行处理。
            // 暂不处理，注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
            String tradeNo = notifyMap.get("out_trade_no");
            if (WeixinContants.SUCCESS.equalsIgnoreCase(notifyMap.get("result_code")) &&
                    WeixinContants.SUCCESS.equalsIgnoreCase(notifyMap.get("return_code")) &&
                    !StringUtils.isEmpty(tradeNo)) {
                log.info("Weixin pay notify map data:{}", notifyMap);
                createTask(tradeNo, notifyMap);//创建红包任务
            } else {
                log.info("支付失败：{}", notifyMap);
            }
        } else {
            log.info("签名错误:{}", notifyMap);// 签名错误，如果数据里没有sign字段，也认为是签名错误
        }
    }

    /**
     * 更新支付订单数据，
     * 正在创建红包任务
     */
    private void createTask(String tradeNo, Map<String, String> notifyMap) {
        PayOrder o = payOrderManager.findByTradeNo(tradeNo);
        if (null != o) {
            if (TradeStatus.PrePaySuccess.getStateCode() != o.getPayStatus()) {//首次接收到成功支付回调
                /***********************更新用户支付订单信息***********************/
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

                /***********************创建红包任务信息***********************/
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
