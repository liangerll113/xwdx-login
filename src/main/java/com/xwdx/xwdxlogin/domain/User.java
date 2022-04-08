package com.xwdx.xwdxlogin.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author metinkong
 * @date 2022/4/8 14:14
 **/
@Data
@Builder
public class User {

    private String username;

    private String password;

    private String nickName;

    private String role;

    private String userAvatar;

}
