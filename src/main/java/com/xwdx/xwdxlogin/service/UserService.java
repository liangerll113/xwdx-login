package com.xwdx.xwdxlogin.service;

import com.xwdx.xwdxlogin.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author metinkong
 * @date 2022/4/8 14:14
 **/
@Service
public class UserService {

    private Map<String, User> userMap = new HashMap<>();

    @PostConstruct
    public void init() {
        userMap.put("admin", User.builder().username("admin").password("113711").nickName("超级管理员").role("super admin").userAvatar("https://wx.qlogo.cn/mmopen/w0d1JOQLJMYHl4UHOBVgh1TBOjlcsUotoKrvGgTxDgKyMf6kXSocpOCAaVUia8JvLRxPzFxjjPQ73f3xGt5f7A3zYO3V82QicQ/64").build());
        userMap.put("xwdx", User.builder().username("xwdx").password("113711").nickName("普通管理员").role("admin").userAvatar("https://wx.qlogo.cn/mmopen/w0d1JOQLJMYHl4UHOBVgh1TBOjlcsUotoKrvGgTxDgKyMf6kXSocpOCAaVUia8JvLRxPzFxjjPQ73f3xGt5f7A3zYO3V82QicQ/64").build());
        userMap.put("metin", User.builder().username("metin").password("113711").nickName("普通用户").role("normal user").userAvatar("https://wx.qlogo.cn/mmopen/w0d1JOQLJMYHl4UHOBVgh1TBOjlcsUotoKrvGgTxDgKyMf6kXSocpOCAaVUia8JvLRxPzFxjjPQ73f3xGt5f7A3zYO3V82QicQ/64").build());
    }

    public User loginWithPassword(String username, String password) {
        User user = userMap.get(username);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

}
