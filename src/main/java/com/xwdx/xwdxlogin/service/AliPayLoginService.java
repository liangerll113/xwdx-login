package com.xwdx.xwdxlogin.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.xwdx.xwdxlogin.domain.User;
import com.xwdx.xwdxlogin.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author metinkong
 * @date 2022/4/8 15:07
 **/
@Service
@Slf4j
public class AliPayLoginService {

    @Value("${oauth2.aliPay.appId}")
    private String appId;
    @Value("${oauth2.aliPay.aliPayPubKey}")
    private String aliPayPubKey;
    @Value("${oauth2.aliPay.myPriKey}")
    private String myPriKey;
    @Value("${oauth2.aes.key}")
    private String aesKey;

    public void doOauthLoginAlipay(HttpServletResponse response) throws Exception {
        response.sendRedirect("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + AESUtil.decrypt(appId, aesKey) + "&scope=auth_user&redirect_uri=http%3A%2F%2Fwww.xwdx.site%2Fxwdx%2FalipayCallBack");
    }

    public User alipayLogin(String authCode) throws Exception {

        log.info("aesKey={}", aesKey);
        String accessToken = getAccessToken(authCode);
        log.info("accessToken={}", accessToken);
        User user = getUserInfo(accessToken);
        log.info("login from alipay, user={}", JSON.toJSON(user));
        return user;
    }


    public String getAccessToken(String code) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AESUtil.decrypt(appId, aesKey), AESUtil.decrypt(myPriKey, aesKey), "json", "GBK", AESUtil.decrypt(aliPayPubKey, aesKey), "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(code);
        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        return response.getAccessToken();
    }

    public User getUserInfo(String accessToken) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AESUtil.decrypt(appId, aesKey), AESUtil.decrypt(myPriKey, aesKey), "json", "GBK", AESUtil.decrypt(aliPayPubKey, aesKey), "RSA2");
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse response = alipayClient.execute(request, accessToken);
        User user = new User();
        user.setUsername(response.getUserId());
        user.setNickName(response.getNickName());
        user.setRole("支付宝用户");
        user.setUserAvatar(response.getAvatar());
        user.setExtendMsg(response.getBody());
        return user;
    }

}
