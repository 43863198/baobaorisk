package com.aizhixin.baobaorisk.redpackage.conf;

import com.aizhixin.baobaorisk.common.wxpay.IWXPayDomain;
import com.aizhixin.baobaorisk.common.wxpay.WXPayConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class WxConfig extends WXPayConfig {

    private byte[] certData;

    @Value("${wx.appId}")
    @Getter  private String appID;
    @Value("${wx.mchId}")
    @Getter  private String mchID;
    @Value("${wx.appSecret}")
    @Getter  private String key;


    public WxConfig(@Value("${wx.certPath}") String certPath) throws Exception {
//        String certPath = "/path/to/apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
