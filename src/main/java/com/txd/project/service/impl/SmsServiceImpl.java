package com.txd.project.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.txd.project.common.ErrorCode;
import com.txd.project.exception.BusinessException;
import com.txd.project.service.SmsService;
import com.txd.project.utils.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        // 生成随机验证码（这里简化为6位数字）
        String captcha = RandomUtil.randomNumbers(6);
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
