package com.google.uaa.service.impl;

import com.google.core.common.api.Result;
import com.google.core.common.constant.Oauth2Constant;
import com.google.core.redis.core.RedisService;
import com.google.uaa.service.ValidateService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author iris
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final RedisService redisService;

    @Override
    public Result<?> getCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(120, 40);
        captcha.getArithmeticString();  // 获取运算的公式
        // 获取运算的结果
        String text = captcha.text();
        redisService.set(Oauth2Constant.CAPTCHA_KEY + uuid, text, Duration.ofMinutes(10));
        Map<String, String> data = new HashMap<String, String>(2) {{
            put("key", uuid);
            put("codeUrl", captcha.toBase64());
        }};
        return Result.data(data);
    }

    @Override
    public Result<?> getSmsCode(String mobile) {
        String code = "1188";
        redisService.set(Oauth2Constant.SMS_CODE_KEY + mobile, code, Duration.ofMinutes(5));
        return Result.success("发送成功");
    }
}
