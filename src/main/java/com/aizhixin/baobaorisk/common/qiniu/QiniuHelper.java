package com.aizhixin.baobaorisk.common.qiniu;

import com.aizhixin.baobaorisk.common.core.PublicErrorCode;
import com.aizhixin.baobaorisk.common.exception.CommonException;
import com.qiniu.http.Response;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.Crc32;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class QiniuHelper {
	@Value("${baobao.pic.qiniu.accesskey}")
	private String accessKey;
	@Value("${baobao.pic.qiniu.secretkey}")
	private String secretKey;
	@Value("${baobao.pic.qiniu.bucket}")
	private String bucket;
	@Value("${baobao.pic.qiniu.url}")
	private String url;
	@Value("${baobao.pic.qiniu.tmpDir}")
	private String tmpDir;

	private Auth auth = null;

	@PostConstruct
	private void initAuth() {
		auth = Auth.create(accessKey, secretKey);
	}
	
	public String getToken() {
		return auth.uploadToken(bucket);
	}
	
	public String getToken(Long expires, StringMap policy) {
		if(null != expires && expires > 0) {
			if(policy.size() > 0) {
				return auth.uploadToken(bucket, null, expires, policy);
			}
			return auth.uploadToken(bucket, null, expires, null);
		} else if(policy.size() > 0) {
			return auth.uploadToken(bucket, null, 3600, policy);
		}
		return getToken();
	}

	public String uploadQiniu(byte[] data, String key) {
		if (null == data || data.length <= 0 || StringUtils.isEmpty(key)) {
			return null;
		}
		StringMap params = new StringMap();
		StringMap map = null;
		String token = getToken();
		Response res = null;

		long crc32 = Crc32.bytes(data);
		params.put("crc32", crc32);
		try {
			Recorder recorder = new FileRecorder(tmpDir);
			UploadManager uploadManager = new UploadManager(recorder);
			res = uploadManager.put(data, key, token, params, null, true);

			if (null == res) {
				throw new CommonException(PublicErrorCode.SAVE_EXCEPTION.getIntValue(), "上传数据失败，七牛服务器未返回任何数据.");
			}
			int count = 1;
			while (!res.isOK() && count < 11) {
				log.warn("尝试第({})次上传七牛错误码:({})，错误原因:({})", count++, res.statusCode, res.error);
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (Exception e) {
					log.warn("休眠异常", e);
				}

				res = uploadManager.put(data, key, token, params, null, true);
			}
			map = res.jsonToMap();
		} catch (IOException e) {
			throw new CommonException(PublicErrorCode.SAVE_EXCEPTION.getIntValue(), null == e ? "NullPointException" : e.getMessage());
		}
		if (null != map) {
			return (String) map.get("key");
		}
		return null;
	}
}
