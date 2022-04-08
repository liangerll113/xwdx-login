package com.xwdx.xwdxlogin.controller;

import com.xwdx.xwdxlogin.constant.CommonConstant;
import com.xwdx.xwdxlogin.domain.User;
import com.xwdx.xwdxlogin.dto.ResponseDTO;
import com.xwdx.xwdxlogin.service.AlipayLoginService;
import com.xwdx.xwdxlogin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author metinkong
 * @date 2022/4/8 9:42
 **/
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private AlipayLoginService alipayLoginService;

    @GetMapping("/")
    public String defaultPage() {
        return "login/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login/login";
    }

    @PostMapping("doLogin")
    @ResponseBody
    public ResponseDTO doLogin(@RequestParam String username, @RequestParam String password, HttpSession httpSession, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResponseDTO.nullParam();
        }
        User user = userService.loginWithPassword(username, password);
        if (user == null) {
            return ResponseDTO.response(CommonConstant.CODE_ERROR_PASSWORD, CommonConstant.MSG_ERROR_PASSWORD);
        }

        // 写session到登录页面
        httpSession.setAttribute("user", user);
        return ResponseDTO.success();

    }

    @GetMapping("/index")
    public String index(HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (StringUtils.isEmpty(user)) {
            return "login/login";
        }
        model.addAttribute("username", user.getNickName());
        model.addAttribute("userAvatar", user.getUserAvatar());
        return "index";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.setAttribute("user", null);
        return "/login/login";
    }

    @RequestMapping("/doOauthLoginAlipay")
    public void doOauthLoginAlipay(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        try {
            response.sendRedirect("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/alipayCallBack")
    public void alipayCallBack(String code, HttpServletResponse httpServletResponse, HttpSession httpSession) {
        try {
            // 获取到授权码code，然后通过code获取支付宝用户信息
            User user = alipayLoginService.alipayLogin(code);
            if (user == null) {
                httpServletResponse.sendRedirect("/login/login");
            }
            // 写session到登录页面
            httpSession.setAttribute("user", user);
            httpServletResponse.sendRedirect("/index");
        } catch (IOException e) {
            log.error("alipayCallBack error");
        }
    }

}
