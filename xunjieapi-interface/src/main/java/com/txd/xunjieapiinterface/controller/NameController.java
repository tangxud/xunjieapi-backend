package com.txd.xunjieapiinterface.controller;

import com.txd.xunjieapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.txd.xunjieapiclientsdk.util.SignUtils.genSign;


/**
 * 名称 API
 *
 * @author tangx
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/")
    public String getNameByGet(String username) {
        return "GET 你的名字是：" + username;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String username) {
        return  "POST 你的名字是：" + username;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        return "POST 用户名字是" + user.getUserName();
    }

}
