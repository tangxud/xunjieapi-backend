package com.txd.project.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.txd.project.common.ErrorCode;
import com.txd.project.exception.BusinessException;
import com.txd.project.service.SmsService;
import com.txd.project.utils.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.txd.project.constant.RedisConstant.CAPTCHA_CACHE_KEY;
import static com.txd.project.constant.RedisConstant.SMS_CODE_EXPIRATION_SECONDS;

/**
 * @author tangx
 * @date 2024/2/24
 */

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private MessageTemplate messageTemplate;

    // 正则表达式，用于匹配电话号码格式
    private static final String PHONE_NUMBER_REGEX = "^1[3-9]\\d{9}$"; // 以1开头，后面跟着9位数字

    // Pattern 对象，用于编译正则表达式
    private static final Pattern PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    // 定义 Lua 脚本字符串
    private static final String LUA_SCRIPT =
            "local phone_number = KEYS[1]\n" +
            "local current_time = tonumber(ARGV[1])\n" +
            "local minute_key = 'api:captcha:' .. phone_number .. ':' .. math.floor(current_time / 60) .. ':60'\n" +
            "local hour_key = 'api:captcha:' .. phone_number .. ':' .. math.floor(current_time / 3600) .. ':3600'\n" +
            "local day_key = 'api:captcha:' .. phone_number .. ':' .. math.floor(current_time / 86400) .. ':86400'\n" +
            "local minute_count = tonumber(redis.call('INCR', minute_key))\n" +
            "local hour_count = tonumber(redis.call('INCR', hour_key))\n" +
            "local day_count = tonumber(redis.call('INCR', day_key))\n" +
            "if minute_count == 1 then\n" +
            "    redis.call('EXPIRE', minute_key, 60)\n" +
            "end\n" +
            "if hour_count == 1 then\n" +
            "    redis.call('EXPIRE', hour_key, 3600)\n" +
            "end\n" +
            "if day_count == 1 then\n" +
            "    redis.call('EXPIRE', day_key, 86400)\n" +
            "end\n" +
            "if minute_count > 1 or hour_count > 5 or day_count > 10 then\n" +
            "    return 0\n" +
            "end\n" +
            "return 1 ";







    @Override
    public void sendSmsCaptcha(String phone) {
        // 检验号码是否为空
        if (StringUtils.isEmpty(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "电话号码不能为空");
        }
        // 检验号码格式是否正确
        if (!isPhoneValid(phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "电话格式有误");
        }
        // 检验号码是否有短信发送资格
        if (!canSendSms(phone)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "短信发送频繁，请稍后再试");
        }
        // 先判断redis中是否有生效的验证码，有就发送原验证码
        String captcha = (String) redisTemplate.opsForValue().get(CAPTCHA_CACHE_KEY + phone);
        // redis中没有就生成新的随机验证码（这里简化为6位数字）
        if (StringUtils.isEmpty(captcha)) {
            captcha = RandomUtil.randomNumbers(6);
        }
        try {
            // 发送验证码
            messageTemplate.doSendSMSCode(phone, captcha);
            // 将验证码缓存入redis,有效期5 * 60秒
            redisTemplate.opsForValue().set(CAPTCHA_CACHE_KEY + phone, captcha, SMS_CODE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("【发送验证码失败】" + e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证码获取失败");
        }
    }

    /**
     * 判断phoneNumber是否具备发送短信的条件
     * 1. 一分钟只能发一个
     * 2. 一小时只能发五条
     * 3. 一天只能发10条
     * @param phoneNumber 电话号码
     * @return
     */
    public boolean canSendSms(String phoneNumber) {
        // 定义 Lua 脚本
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
        List<String> keys = Collections.singletonList(phoneNumber);
        String currentTimeArg = String.valueOf(System.currentTimeMillis() / 1000);
        // 执行 Lua 脚本，这里将 currentTimeArg 作为参数传递
        Long result = redisTemplate.execute(script, keys, currentTimeArg);
        // 根据 Lua 脚本的返回值判断是否可以发送短信
        return result != null && result == 1;
    }




    /**
     * 校验电话号码是否合理
     *
     * @param phoneNumber 要校验的电话号码
     * @return true，如果电话号码合理；否则返回 false
     */
    public static boolean isPhoneValid(String phoneNumber) {
        Matcher matcher = PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }
}
