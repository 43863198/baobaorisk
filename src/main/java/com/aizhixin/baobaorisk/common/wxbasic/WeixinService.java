package com.aizhixin.baobaorisk.common.wxbasic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WeixinService {
    private String weixinUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=";

    @Value("${wx.appId}")
    private String appId = "wx792b4b3ac23f9070";

    @Value("${wx.appSecret}")
    private String appSecurity = "8e5b9f4f439a3dad3a1a3c1580d0daab";
    //{"errcode":40163,"errmsg":"code been used, hints: [ req_id: hanENa0300th50 ]"}
    //{"session_key":"EBheKA10Xdg3nWOqA4UsqA==","openid":"o8OrG5RUasGr18b6o7tLVOBkwUFY"}
    public String getWeixinOpenInfo(String code) {
        RestTemplate rest = new RestTemplate();
        StringBuilder sb = new StringBuilder(weixinUrl);
        sb.append(appId).append("&secret=").append(appSecurity);
        sb.append("&js_code=").append(code).append("&grant_type=authorization_code");
        String json = rest.getForObject(sb.toString(), String.class);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            if (root.has("errcode")) {
                log.warn("获取OpenId失败，code:{},errcode:{}, errmsg:{}", code, root.get("errcode").intValue(), root.get("errmsg").textValue());
            } else {
                JsonNode node = root.get("openid");
                if (null != node) {
                    return node.textValue();
                }
            }

        } catch (Exception e) {
            log.warn("调用微信小程序获取openid失败：{}", e);
        }
        return null;
    }


//
//    public static void main(String[] args) {
//        WeixinService t = new WeixinService ();
////        System.out.println(t.getWeixinOpenInfo("071FV75x1TDHFa07FD3x1KT65x1FV75p"));
//        System.out.println(t.getWeixinOpenInfo("1234567"));
//    }
}
