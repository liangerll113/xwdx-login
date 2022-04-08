package com.xwdx.xwdxlogin.service;

import com.xwdx.xwdxlogin.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author metinkong
 * @date 2022/4/8 15:07
 **/
@Service
public class AlipayLoginService {

    @Value("${oauth2.alipayPubKey}")
    private String alipayPubKey;
    @Value("${oauth2.myPriKey}")
    private String myPriKey;

    public User alipayLogin(String code){
        User user = User.builder().username("208889797999").nickName("xxxxxx").userAvatar("xxxxx").build();
        return user;
    }

}
