package com.chen.utils.util;

public class RedisConstants {

    /**
     * 验证码过期时间
     */
    public static final long DEFAULT_TIMEOUT_SECONDS=60L*5;

    /**
     * 短信验证码前缀
     */
    public static final String SMS_CAPTCHA_PREFIX_KEY="mobile_phone:";

    /**
     * 图形验证码前缀
     */
    public static final String IMAGE_CAPTCHA_PREFIX_KEY="image_captcha:";
    /**
     * 认证信息存储前缀
     */
    public static final String SECURITY_CONTEXT_PREFIX_KEY="security_context:";
    /**
     * jwk set缓存前缀
     */
    public static final String AUTHORIZATION_JWS_PREFIX_KEY="authorization_jws";
}
