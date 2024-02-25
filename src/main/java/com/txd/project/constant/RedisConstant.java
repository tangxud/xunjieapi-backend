package com.txd.project.constant;

/**
 * @author tangx
 * @date 2024/2/24
 */


public interface RedisConstant {
    /**
     *  短信登录验证码短信缓存键
     */
    String CAPTCHA_CACHE_KEY = "api:captcha:";

    /**
     * 短信登录验证码有效期
     */
    long SMS_CODE_EXPIRATION_SECONDS = 5 * 60;
}
