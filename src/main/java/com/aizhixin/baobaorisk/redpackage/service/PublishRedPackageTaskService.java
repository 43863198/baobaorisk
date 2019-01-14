package com.aizhixin.baobaorisk.redpackage.service;

import com.aizhixin.baobaorisk.common.core.ErrorCode;
import com.aizhixin.baobaorisk.common.exception.CommonException;
import com.aizhixin.baobaorisk.common.qiniu.QiniuHelper;
import com.aizhixin.baobaorisk.common.rest.RestUtil;
import com.aizhixin.baobaorisk.common.tools.PageData;
import com.aizhixin.baobaorisk.common.tools.PageUtil;
import com.aizhixin.baobaorisk.common.wxbasic.Utility;
import com.aizhixin.baobaorisk.redpackage.conf.WxConfig;
import com.aizhixin.baobaorisk.redpackage.core.WeixinContants;
import com.aizhixin.baobaorisk.redpackage.dto.RedPackageCountDTO;
import com.aizhixin.baobaorisk.redpackage.entity.RedTask;
import com.aizhixin.baobaorisk.redpackage.manager.GrapRedTaskManager;
import com.aizhixin.baobaorisk.redpackage.manager.RedTaskManager;
import com.aizhixin.baobaorisk.redpackage.vo.PublishRedPackageCountVO;
import com.aizhixin.baobaorisk.redpackage.vo.PublishRedPackageDetailVO;
import com.aizhixin.baobaorisk.redpackage.vo.PublishRedPackageVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Transactional
public class PublishRedPackageTaskService {
    @Autowired
    private RedTaskManager redTaskManager;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private RestUtil restUtil;
    @Autowired
    private ServletContext servletContext;
//    @Value("${pic.basePath}")
//    private String basePath;
    @Autowired
    private QiniuHelper qiniuHelper;
    @Autowired
    private GrapRedTaskManager grapRedTaskManager;

    private RestTemplate restTemplate = new RestTemplate();

    private String getAccessToken() {
        String accessToken = null;
        String json = restUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxConfig.getAppID() + "&secret=" + wxConfig.getAppSecret(), null);
        ObjectMapper o = new ObjectMapper();
        try {
            StringBuilder sb = new StringBuilder();
            JsonNode root = o.readTree(json);
            if (root.has("access_token")) {
                accessToken = root.get("access_token").textValue();
                sb.append(accessToken);
            }
            if (root.has("expires_in")) {
                sb.append(":").append(root.get("expires_in").longValue() + System.currentTimeMillis() / 1000);
            }
            servletContext.setAttribute(WeixinContants.BAOBAO_WX_TOKEN, sb.toString());
        } catch (Exception e) {
            log.warn("调用微信获取访问token失败", e);
        }
        return accessToken;
    }

    private String getUserInfo(String accessToken, String openId) {
        return restUtil.get("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN", null);
    }

    /**
     * 获取微信访问token
     * 优先从缓存获取
     */
    public String getWeixinAccessToken() {
        String t = (String)servletContext.getAttribute(WeixinContants.BAOBAO_WX_TOKEN);
        String accessToken = null;

        if (null == t) {
            accessToken = getAccessToken();
        } else {
            int p = t.indexOf(":");
            if (p > 0) {
                accessToken = t.substring(0, p);
                long time = new Long(t.substring(p + 1));
                if (System.currentTimeMillis()/1000 - time >= 0) {
                    accessToken = getAccessToken();
                }
            }
        }
        return accessToken;
    }

    /**
     * 已发布红包任务列表的查询
     */
    @Transactional(readOnly = true)
    public PageData<PublishRedPackageVO> queryPublish(String openId, Integer pageNumber, Integer pageSize) {
        PageData<PublishRedPackageVO> page = new PageData<>();
        PageRequest pageRequest = PageUtil.createNoErrorPageRequest(pageNumber, pageSize);
        page.getPage().setPageNumber(pageRequest.getPageNumber() + 1);
        page.getPage().setPageSize(pageRequest.getPageSize());
        Page<RedTask> p = redTaskManager.findByOpenId(pageRequest, openId);
        if (null != p) {
            page.getPage().setTotalElements(p.getTotalElements());
            page.getPage().setTotalPages(p.getTotalPages());
            List<PublishRedPackageVO> list = new ArrayList<>();
            Map<String, PublishRedPackageVO> map = new HashMap<>();
            for (RedTask r : p.getContent()) {
                PublishRedPackageVO v = new PublishRedPackageVO(r.getId(), r.getTaskName(), null == r.getTotalFee()? 0.0: r.getTotalFee()/100.0, r.getNum(), r.getCreatedDate(), r.getRedStatus());
                switch (r.getRedStatus()) {
                    case 20:
                        map.put(r.getId(), v);
                        break;
                    case 80:
                        v.setCompleteNum(v.getNum());
                        break;
                    default:
                        v.setCompleteNum(0);
                }
                list.add(v);
            }
            //计算任务中红包完成数量，并且回填数量
            if (!map.isEmpty()) {
            }
            page.setData(list);
        }
        return page;
    }

    @Transactional(readOnly = true)
    public PublishRedPackageCountVO countPublish(String openId) {
        return redTaskManager.countByOpenId(openId);
    }

    public String getWxCode(String taskId, String path, Integer width) {
        String filename = null;

        try {
            String accessToken = getWeixinAccessToken();
            if (null != accessToken) {
                String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + accessToken;
                Map<String, Object> param = new HashMap<>();
                if (null != path) {
                    param.put("path", path);
                } else {
                    param.put("path", "pages/index/index");
                }
                param.put("scene", taskId);
                param.put("width", (null == width || width <= 10) ? 430 : width); //二维码的宽度
                param.put("auto_color", false);  //自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
                param.put("is_hyaline", false);  //是否需要透明底色， is_hyaline 为true时，生成透明底色的小程序码
                Map<String, Integer> line_color = new HashMap<>();
                line_color.put("r", 0);
                line_color.put("g", 0);
                line_color.put("b", 0);
                param.put("line_color", line_color);
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                HttpEntity requestEntity = new HttpEntity(param, headers);
                ResponseEntity<byte[]> entity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
                byte[] result = entity.getBody();
                filename = "M" + Utility.generateUUID();//二维码标识
                qiniuHelper.uploadQiniu(result, filename);

                RedTask r = redTaskManager.findById(taskId);
                if (null != r) {
                    r.setPicname(filename);
                    redTaskManager.save(r);
                }
            }
        }catch (Exception e){
            log.error("调用小程序生成微信永久小程序码URL接口异常: "+e.getMessage());
        }
        return filename;
    }



//    @Transactional(readOnly = true)
//    public void outPic(String picname, HttpServletResponse response) {
//        File f = new File(basePath, picname + ".jpeg");
//        if (f.exists()) {
//            response.setContentType("image/jpeg");
//            FileInputStream is = null;
//            try {
//                is = new FileInputStream(f);
//                byte[] buf = new byte[8192];
//                int p = is.read(buf);
//                while (p > 0) {
//                    response.getOutputStream().write(buf, 0, p);
//                    p = is.read(buf);
//                }
//                response.getOutputStream().flush();
//            } catch (IOException e) {
//                log.warn("Out pic fail.", e);
//            } finally {
//                if (null != is) {
//                    try {
//                        is.close();
//                    } catch (IOException e) {
//                        log.warn("Out pic fail.", e);
//                    }
//                }
//            }
//        }
//    }

    @Transactional(readOnly = true)
    public String getUserInfo(String openId) {
        return getUserInfo(getWeixinAccessToken(), openId);
    }

    @Transactional(readOnly = true)
    public PublishRedPackageDetailVO getPublishRedPackageTask(String taskId) {
        PublishRedPackageDetailVO vo = new PublishRedPackageDetailVO ();
        RedTask r = redTaskManager.findById(taskId);
        if (null != r) {
            vo.setId(r.getId());
            vo.setAvatar(r.getAvatar());
            vo.setNick(r.getNick());
            vo.setCreateDate(r.getCreatedDate());
            vo.setNum(null == r.getNum()? 0 : r.getNum());
            vo.setPicName(r.getPicname());
            vo.setTotalFee(null == r.getTotalFee() ? 0.0 : r.getTotalFee()/100.0);
            vo.setRemark(r.getTaskName());
            vo.setStatus(r.getRedStatus());

            RedPackageCountDTO d = grapRedTaskManager.countGrapRedTask(taskId);
            vo.setCompleteNum(d.getGrapNums());
            vo.setGrapTotalFee(d.getTotalFee()/100.0);
            vo.setVerifyNum(d.getVerifyNums());
            vo.setCount(d.getCountNums());
        }
        return vo;
    }
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getTaskAvatar(String taskId) {
        RedTask r = redTaskManager.findById(taskId);
        if (null != r && !StringUtils.isEmpty(r.getAvatar())) {
            DataInputStream din = null;
            ByteArrayOutputStream os = null;
            byte[] buff = new byte[8092];
            int len = 0;
            try {
                URL url = new URL(r.getAvatar());
                din = new DataInputStream(url.openStream());
                os = new ByteArrayOutputStream();
                while ((len = din.read(buff)) != -1) {
                    os.write(buff, 0, len);
                }
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(os.toByteArray());
            } catch (IOException e) {
                if (null != din) {
                    try {
                        din.close();
                    } catch (IOException e1) {
                        throw new CommonException(ErrorCode.SYSTEM_EXCEPTION_CODE.intValue(), null != e1 ? e1.getMessage() : "NulllPointException");
                    }
                }
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e1) {
                        throw new CommonException(ErrorCode.SYSTEM_EXCEPTION_CODE.intValue(), null != e1 ? e1.getMessage() : "NulllPointException");
                    }
                }
            }
        }
        return ResponseEntity.ok().body(new byte[0]);
    }
}
