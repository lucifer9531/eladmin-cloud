package com.google.uaa.controller;

import com.google.core.common.api.Result;
import com.google.core.common.entity.LoginUser;
import com.google.core.common.utils.SecurityUtil;
import com.google.core.redis.core.RedisService;
import com.google.system.dto.UserInfo;
import com.google.system.feign.ISysUserProvider;
import com.google.uaa.config.SocialConfig;
import com.google.uaa.enums.LoginType;
import com.google.uaa.service.ValidateService;
import com.xkcoding.justauth.AuthRequestFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author iris
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Api(tags = "认证管理")
public class AuthController {

    @Qualifier("consumerTokenServices")
    private final ConsumerTokenServices customerTokenServices;

    private final ValidateService validateService;

    private final ISysUserProvider sysUserProvider;

    private final AuthRequestFactory authRequestFactory;

    private final SocialConfig socialConfig;

    private final RedisService redisService;

    @GetMapping("/get/user")
    @ApiOperation(value = "用户信息", notes = "用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "El-Admin-Auth", required = true, value = "授权类型", paramType = "header")
    })
    public Result<?> getUser(HttpServletRequest request) {
        LoginUser loginUser = SecurityUtil.getUsername(request);
        UserInfo userInfo = null;
        /**
         * 根据type来判断调用哪个接口登录，待扩展社交登录模式
         * type 1:用户名和密码登录　2：手机号码登录
         */
        if (loginUser.getType() == LoginType.MOBILE.getType()) {
           userInfo = sysUserProvider.getUserByMobile(loginUser.getAccount()).getData();
        } else {
            userInfo = sysUserProvider.getUserByUserName(loginUser.getAccount()).getData();
        }
        Map<String, Object> data = new HashMap<>(7);
        data.put("username", loginUser.getAccount());
        data.put("avatar", userInfo.getSysUser().getAvatar());
        data.put("roleId", userInfo.getSysUser().getRoleId());
        data.put("departId", userInfo.getSysUser().getDepartId());
        data.put("tenantId", userInfo.getSysUser().getTenantId());
        data.put("realName", userInfo.getSysUser().getRealName());
        data.put("nickName", userInfo.getSysUser().getName());
        return null;
    }

    @GetMapping("/code")
    @ApiOperation(value = "验证码获取", notes = "验证码获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "授权类型", paramType = "header")
    })
    public Result<?> authCode() {
        return validateService.getCode();
    }
}
