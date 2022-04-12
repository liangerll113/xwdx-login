package com.xwdx.xwdxlogin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xwdx.xwdxlogin.domain.User;
import com.xwdx.xwdxlogin.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

/**
 * @author metinkong
 * @date 2022/4/12 10:19
 **/
@Service
@Slf4j
public class QQLoginService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${oauth2.qq.appId}")
    private String qqAppId;
    @Value("${oauth2.qq.appKey}")
    private String qqAppKey;
    @Value("${oauth2.aes.key}")
    private String aesKey;

    public void doOauthLoginQQ(HttpServletResponse response) throws Exception {
        response.sendRedirect("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + AESUtil.decrypt(qqAppId, aesKey) + "&redirect_uri=http%3A%2F%2Fwww.xwdx.site%2Fxwdx%2FqqCallBack&state=enable&scope=get_user_info");
    }

    public User qqLogin(String code) throws Exception {
        String accessToken = getAccessToken(code);
        return getUserInfo(accessToken);
    }


    public String getAccessToken(String code) throws Exception {
        String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=" + AESUtil.decrypt(qqAppId, aesKey) + "&client_secret=" + AESUtil.decrypt(qqAppKey, aesKey) + "&code=" + code + "&redirect_uri=http://www.xwdx.site/xwdx/qqCallBack&fmt=json";
        String response = restTemplate.getForObject(url, String.class);
        log.info("get access_token from qq, code={}, response={}", code, response);
        return JSON.parseObject(response).getString("access_token");
    }

    public User getUserInfo(String accessToken) throws Exception {

        // 获取openId
        String openIdUrl = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken + "&fmt=json";
        String openIdStr = restTemplate.getForObject(openIdUrl, String.class);
        log.info("get open id, access_token={}, openIdStr={}", accessToken, openIdStr);
        JSONObject openIdObj = JSON.parseObject(openIdStr);
        String openId = openIdObj.getString("openid");

        String url = "https://graph.qq.com/user/get_user_info?access_token=" + accessToken + "&oauth_consumer_key=" + AESUtil.decrypt(qqAppId, aesKey) + "&openid=" + openId;
        String userStr = restTemplate.getForObject(url, String.class);
        log.info("get user info, access_token={}, openid={}, userStr={}", accessToken, openId, userStr);
        JSONObject userJsonObj = JSON.parseObject(userStr);
        User user = new User();
        user.setUsername(userJsonObj.getString("nickname"));
        user.setNickName(userJsonObj.getString("nickname"));
        user.setRole("QQ用户");
        user.setUserAvatar(userJsonObj.getString("figureurl"));
        user.setExtendMsg(userJsonObj.toString());
        return user;
    }

}
