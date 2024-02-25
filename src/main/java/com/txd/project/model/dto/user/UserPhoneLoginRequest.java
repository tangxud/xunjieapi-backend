package com.txd.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户短信登录请求体
 * @author tangx
 */
@Data
public class UserPhoneLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String phone;

    private String captcha;
}
