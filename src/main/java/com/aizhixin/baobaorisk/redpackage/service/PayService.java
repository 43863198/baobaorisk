package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.core.PublicErrorCode;
import com.aizhixin.baobaorisk.common.exception.CommonException;
import com.aizhixin.baobaorisk.common.vo.MsgVO;
import com.aizhixin.baobaorisk.common.wxbasic.Utility;
import com.aizhixin.baobaorisk.common.wxpay.WXPay;
import com.aizhixin.baobaorisk.common.wxpay.WXPayConstants;
import com.aizhixin.baobaorisk.common.wxpay.WXPayUtil;
import com.aizhixin.baobaorisk.redpackage.conf.WxConfig;
import com.aizhixin.baobaorisk.redpackage.core.RedPackageTaskStatus;
import com.aizhixin.baobaorisk.redpackage.core.TradeStatus;
import com.aizhixin.baobaorisk.redpackage.core.TradeType;
import com.aizhixin.baobaorisk.redpackage.core.WeixinContants;
import com.aizhixin.baobaorisk.redpackage.dto.GrapPackageCountDTO;
import com.aizhixin.baobaorisk.redpackage.dto.PublishPackageCountDTO;
import com.aizhixin.baobaorisk.redpackage.dto.WithDrawCountDTO;
import com.aizhixin.baobaorisk.redpackage.entity.PayOrder;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.entity.TradeRecord;
import com.aizhixin.baobaorisk.redpackage.entity.WeixinUser;
import com.aizhixin.baobaorisk.redpackage.manager.*;
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
    @Autowired
    private GrapRedTaskManager grapRedTaskManager;
    @Autowired
    private WeixinUserManager weixinUserManager;
    @Autowired
    private TradeRecordManager tradeRecordManager;

    /**
     * 创建红包订单任务（用于记录支付情况）
     */
    public WxPrePayVO createOrder(String openId, Double totalFee, Integer num, String taskName, String nick, String avatar) {
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
        o.setAvatar(avatar);
        o.setNick(nick);

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

        /***********************更新用户信息***********************/
        WeixinUser u = weixinUserManager.findByOpenId(openId);
        if (null == u) {
            u = new WeixinUser();
            u.setOpenId(openId);
        }
        u.setAvatar(avatar);
        u.setNick(nick);

        try {
            wxpay = new WXPay(wxConfig);
            Map<String, String> resp = wxpay.unifiedOrder(data);//调用微信预支付
            log.info("Weixin Order create prePay back msg:{}", resp);
            if (WeixinContants.SUCCESS.equalsIgnoreCase(resp.get("return_code")) &&
                    WeixinContants.SUCCESS.equalsIgnoreCase(resp.get("result_code")) &&
                    !StringUtils.isEmpty(resp.get("prepay_id"))) {//预支付成功
                o.setPrepayId(resp.get("prepay_id"));
                o = payOrderManager.save(o);//支付订单数据保存

                /***********************创建红包任务信息***********************/
                RedTask r = new RedTask();
                r.setOpenId(o.getOpenId());//任务发起人
                r.setTradeNo(o.getTradeNo());//订单号
                r.setTaskName(o.getTaskName());//任务名称
                r.setNum(o.getNum());//红包数量
                r.setTotalFee(o.getCashFee());//红包金额
                r.setAvatar(o.getAvatar());
                r.setNick(o.getNick());
                r = redTaskManager.save(r);
                vo.setPublishTaskId(r.getId());//红包任务ID

                /***********************************用户信息发布任务统计*************************************/
                PublishPackageCountDTO d = redTaskManager.countByOpenId(openId);
                if (null != d) {
                    u.setPublishRedNums(d.getReds());
                    u.setPublishTaskNums(d.getTasks());
                    u.setPublishTotalFee(d.getFees());
                }
                weixinUserManager.save(u);

                /***********************小程序用户支付确认所需数据对象数据构造***********************/
                vo.setNonceStr(Utility.generateUUID());
                vo.setPayPackage("prepay_id=" + o.getPrepayId());
                vo.setTimestamp(Utility.getCurrentTimeStamp());
                vo.setSignType(WeixinContants.SIGN_TYPE);
//                vo.setAppId(wxConfig.getAppID());

                Map<String,String> repData = new HashMap<>();
                repData.put("appId",wxConfig.getAppID());
                repData.put("package",vo.getPayPackage());
                repData.put("nonceStr",vo.getNonceStr());
                repData.put("signType", vo.getSignType());
                repData.put("timeStamp",vo.getTimestamp());
                String sign = WXPayUtil.generateSignature(repData,wxConfig.getKey()); //签名

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
                log.info("微信小程序付款回调，map data:{}", notifyMap);
                createTask(tradeNo, notifyMap);//创建红包任务
            } else {
                log.info("微信小程序付款回调，支付失败：{}", notifyMap);
            }
        } else {
            log.info("微信小程序付款回调，签名错误:{}", notifyMap);// 签名错误，如果数据里没有sign字段，也认为是签名错误
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
                        log.warn("微信小程序付款回调，订单金额:({}), 实际付款金额:({})", o.getTotalFee(), t);
                    }
                }
                payOrderManager.save(o);

                RedTask r = redTaskManager.findByTradeNo(tradeNo);
                if (null != r) {
                    r.setTotalFee(o.getCashFee());
                    r.setRedStatus(RedPackageTaskStatus.TASKING.getStateCode());
                    redTaskManager.save(r);

                    /***********************************生成交易记录*************************************/
                    TradeRecord tradeRecord = tradeRecordManager.findByOpenIdAndTradeNo(r.getOpenId(), r.getTradeNo());
                    if (null == tradeRecord) {
                        tradeRecord = new TradeRecord();
                        tradeRecord.setOpenId(r.getOpenId());
                        tradeRecord.setTradeNo(r.getTradeNo());
                        tradeRecord.setTradeName("包包大冒险充值:" + r.getTaskName());
                        tradeRecord.setTotalFee(o.getTotalFee());
                        tradeRecord.setCashFee(r.getTotalFee());
                        tradeRecord.setTransactionId(o.getTransactionId());
                        tradeRecord.setBankType(o.getBankType());
                        tradeRecord.setTimeEnd(o.getTimeEnd());
                        tradeRecord.setTradeType(TradeType.WX_PAY.getStateCode());
                        tradeRecordManager.save(tradeRecord);
                    }

                    /***********************************用户信息发布任务统计*************************************/
                    WeixinUser u = weixinUserManager.findByOpenId(r.getOpenId());
                    if (null != u) {
                        PublishPackageCountDTO d = redTaskManager.countByOpenId(r.getOpenId());
                        if (null != d) {
                            u.setPublishRedNums(d.getReds());
                            u.setPublishTaskNums(d.getTasks());
                            u.setPublishTotalFee(d.getFees());
                        }
                        weixinUserManager.save(u);
                    }
                } else {
                    log.warn("订单号:({})无对应的发布红包数据", tradeNo);
                }
            }//否则重复调用，不予理睬
        } else {
            log.warn("微信小程序付款回调，没有找到订单号:{}", tradeNo);
        }
    }


    public MsgVO payToWeixinOne(String openId, Integer fee) {
        MsgVO vo = new MsgVO ();
        if (null == fee || fee < 100) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "提现至少1元及以上额度");
        }
        GrapPackageCountDTO gd = grapRedTaskManager.countByOpenId(openId);//之前所有抢到的红包总额
        WithDrawCountDTO ct = tradeRecordManager.countFeeByOpenId(openId);//之前所有的提现
        int discountFee = fee / 100 + (fee % 100 >= 50 ? 1 : 0);//手续费1%，四舍五入
        if (gd.getFees() < fee + discountFee + ct.getDiscount() + ct.getFee()) {//红包总额 < 本次支付金额 + 手续费 + 历史累计的提现总额 + 历史提现的总手续费
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "余额不足");
        }
        /***********************微信企业付款到用户零钱数据构造及调用***********************/
        Map<String, String> data = new HashMap<>();
        data.put("partner_trade_no", Utility.generateUUID());
        data.put("openid", openId);
        data.put("check_name", "NO_CHECK");
        data.put("amount", "" + fee);
        data.put("desc", wxConfig.getWithDrawName());
        data.put("spbill_create_ip", wxConfig.getCreateIp());
        data.put("mch_appid", wxConfig.getAppID());
        data.put("mchid", wxConfig.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());

        try {
            data.put("sign", WXPayUtil.generateSignature(data, wxConfig.getKey(), WXPayConstants.SignType.MD5));
            WXPay wxpay = new WXPay(wxConfig);
            Map<String, String> resp = wxpay.enterprisePay(data);//调用微信预支付
            log.info("用户提现返回数据:{}", resp);
            //{nonce_str=G4cRziWosUg1u5uiSMeIgvf4QCX7gNFL, mchid=1488609932, partner_trade_no=6b9bde071e924a648db95117ecc9960a, payment_time=2019-01-16 11:29:12, mch_appid=wxe03f58cd79c2d8d9, payment_no=1488609932201901167624116076, return_msg=, result_code=SUCCESS, return_code=SUCCESS}
            if (WeixinContants.SUCCESS.equalsIgnoreCase(resp.get("return_code")) &&
                    WeixinContants.SUCCESS.equalsIgnoreCase(resp.get("result_code"))) {//调用企业付款成功
                String tradeNo = resp.get("partner_trade_no");
                String paymentTime = resp.get("payment_time");//2019-01-16 11:29:12
                if (null != paymentTime) {
                    paymentTime = paymentTime.replaceAll("-", "");
                    paymentTime = paymentTime.replaceAll(" ", "");
                    paymentTime = paymentTime.replaceAll(":", "");
                }
                String payment_No = resp.get("payment_no");
                if (!data.get("partner_trade_no").equals(tradeNo)) {
                    log.info("用户提现支付失败，原订单号:{}， 返回订单号:{}, 错误说明:{}", data.get("partner_trade_no"), tradeNo, resp.get("err_code_des"));
                } else {
                    /***********************************生成交易记录*************************************/
                    TradeRecord tradeRecord = new TradeRecord();
                    tradeRecord.setOpenId(openId);
                    tradeRecord.setTradeNo(tradeNo);
                    tradeRecord.setTradeName("包包大冒险提现");
                    tradeRecord.setTotalFee(fee);
                    tradeRecord.setCashFee(fee);
                    tradeRecord.setDiscountFee(discountFee);//手续费1%，四舍五入
                    tradeRecord.setTransactionId(payment_No);
                    tradeRecord.setTimeEnd(paymentTime);
                    tradeRecord.setTradeType(TradeType.WX_WITHDRAW.getStateCode());
                    tradeRecordManager.save(tradeRecord);

                    /***********************************用户信息发布任务统计*************************************/
                    WeixinUser u = weixinUserManager.findByOpenId(openId);
                    if (null != u) {
                        WithDrawCountDTO c = tradeRecordManager.countFeeByOpenId(openId);
                        u.setCashFee(c.getFee());
                        u.setDiscountFee(c.getDiscount());
                        weixinUserManager.save(u);
                    }
                }
            } else {
                vo.setMsg(WeixinContants.FAIL);
                throw new CommonException(PublicErrorCode.SAVE_EXCEPTION.getIntValue(), "提现失败,调用提现失败");
            }
        } catch (Exception e) {
            log.warn("用户提现失败.", e);
            vo.setMsg(WeixinContants.FAIL);
            throw new CommonException(PublicErrorCode.SAVE_EXCEPTION.getIntValue(), "提现失败,出错");
        }
        return vo;
    }
}
