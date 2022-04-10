package com.xwdx.xwdxlogin.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author: kongmingliang
 * @create: 2022-04-10 22:53:04
 */
public class AESUtilTest {

    String str = "xxxxx";

    @Test
    public void encrypt() throws Exception {
        String key = "123456";
        String encrypt = AESUtil.encrypt(str, key);
        String decrypt = AESUtil.decrypt(encrypt, key);
        Assert.assertEquals(str, decrypt);
    }
}
