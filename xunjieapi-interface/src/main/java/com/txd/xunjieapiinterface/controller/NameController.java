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

//    @PostMapping("/user")
//    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        String secretKey = request.getHeader("secretKey");
//        // 从数据库中查用户，校验accessKey和secretKey是否与数据库中的一致
//        // 省略
//        if (!("yupi".equals(accessKey) && "abcdefgh".equals(secretKey))) {
//            throw new RuntimeException("无权限");
//        }
//        return "POST 用户名字是" + user.getUserName();
//    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        // 从请求头中获取参数
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");

        // todo 实际情况应该是去数据库中查是否已分配给用户
        if (!accessKey.equals("yupi")){
            throw new RuntimeException("无权限");
        }
        // 校验随机数，模拟一下，直接判断nonce是否大于10000
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }

        // todo 时间和当前时间不能超过5分钟
//        if (timestamp) {}

        // 从数据库中查用户的secreytKey.
        String secretKey = "abcdefgh";
        String serverSign = genSign(body, secretKey);
        if (!serverSign.equals(sign)) {
            throw new RuntimeException("无权限");
        }

        return "POST 用户名字是" + user.getUserName();
    }

}
