package com.xwdx.xwdxlogin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author metinkong
 * @date 2022/4/8 14:14
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String username;

    private String password;

    private String nickName;

    private String role;

    private String userAvatar;

    private String extendMsg;

}
