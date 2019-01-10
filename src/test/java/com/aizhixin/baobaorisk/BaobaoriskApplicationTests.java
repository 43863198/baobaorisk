package com.aizhixin.baobaorisk;

import com.aizhixin.baobaorisk.common.wxpay.WXPayUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class BaobaoriskApplicationTests {

//	@Test
//	public void contextLoads() {
//	}

	@Test
	public void testMD5() {
		Map<String,String> repData = new HashMap<>();
		repData.put("appId","wxe03f58cd79c2d8d9");
		repData.put("package","prepay_id=wx101544503372431aa8390cd73645977536");
		repData.put("nonceStr","SUu2RXnySbwGf08s");
		repData.put("signType","MD5");
		repData.put("timeStamp","1547106290");
		try {
			String sign = WXPayUtil.generateSignature(repData, "vTHcW3eLDppmGZ8gIODRHlFKSyGd0TQ9"); //签名
//			System.out.println();
//			System.out.println(sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

