package com.google.uaa.controller;

import com.google.core.common.api.Result;
import com.google.core.common.constant.Oauth2Constant;
import com.google.core.common.entity.LoginUser;
import com.google.core.common.utils.SecurityUtil;
import com.google.core.common.utils.StringPool;
import com.google.core.common.utils.StringUtil;
import com.google.core.redis.core.RedisService;
import com.google.system.dto.UserInfo;
import com.google.system.feign.RolePermissionProvider;
import com.google.system.feign.UserProvider;
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
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 认证控制类
 * @author iris
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Api(tags = "认证管理")
public class AuthController {

    @Qualifier("consumerTokenServices")
     private final ConsumerTokenServices consumerTokenServices;

    private final ValidateService validateService;

    private final UserProvider userProvider;

    private final RolePermissionProvider rolePermissionProvider;

    private final AuthRequestFactory factory;

    private final SocialConfig socialConfig;

    private final RedisService redisService;

    @GetMapping("/get/user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "El-Admin-Auth", required = true, value = "授权类型", paramType = "header")
    })
    @ApiOperation(value = "用户信息", notes = "用户信息")
    public Result<?> getUser(HttpServletRequest request) {

        LoginUser loginUser = SecurityUtil.getUsername(request);
        UserInfo userInfo = null;
        /**
         * 根据type来判断调用哪个接口登录，待扩展社交登录模式
         * type 1:用户名和密码登录　2：手机号码登录
         */
        if (loginUser.getType() == LoginType.MOBILE.getType()) {
            userInfo = userProvider.getUserByMobile(loginUser.getAccount()).getData();
        } else {
            userInfo = userProvider.getUserByUserName(loginUser.getAccount()).getData();
        }

        Map<String, Object> data = new HashMap<>(7);
        data.put("username", loginUser.getAccount());
        data.put("avatar", userInfo.getSysUser().getAvatar());
        data.put("roleId", userInfo.getSysUser().getRoleId());
        data.put("departId", userInfo.getSysUser().getDepartId());
        data.put("tenantId", userInfo.getSysUser().getTenantId());
        data.put("realName", userInfo.getSysUser().getRealName());
        data.put("nickName", userInfo.getSysUser().getName());
        List<String> stringList = rolePermissionProvider.getMenuIdByRoleId(String.valueOf(userInfo.getSysUser().getRoleId()));
        data.put("permissions", stringList);
        // 存入redis,以用于eladmin-starter-auth的PreAuthAspect查询权限使用
        redisService.set(Oauth2Constant.EL_ADMIN_PERMISSION_PREFIX + loginUser.getAccount()
                + StringPool.DOT + userInfo.getSysUser().getRoleId(), data);
        return Result.data(data);
    }

    @GetMapping("/code")
    @ApiOperation(value = "验证码获取", notes = "验证码获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "授权类型", paramType = "header")
    })
    public Result<?> authCode() {
        return validateService.getCode();
    }

    @PostMapping("/logout")
    @ApiOperation(value = "退出登录", notes = "退出登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "El-Admin-Auth", required = true, value = "授权类型", paramType = "header")
    })
    public Result<?> logout(HttpServletRequest request) {
        if (StringUtil.isNotBlank(SecurityUtil.getHeaderToken(request))) {
            consumerTokenServices.revokeToken(SecurityUtil.getToken(request));
        }
        return Result.success("操作成功");
    }

    @ApiOperation(value = "手机验证码下发", notes = "手机验证码下发")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "授权类型", paramType = "header")
    })
    @GetMapping("/sms-code")
    public Result<?> smsCode(String mobile) {
        return validateService.getSmsCode(mobile);
    }

    @GetMapping("/list")
    @ApiOperation(value = "登录类型", notes = "登录类型")
    public Map<String, String> loginType() {
        List<String> oauthList = factory.oauthList();
        return oauthList.stream().collect(Collectors.toMap(oauth -> oauth.toLowerCase() + "登录", oauth -> "http://localhost:10086/eladmin-uaa/auth/login/" + oauth.toLowerCase()));
    }

    @ApiOperation(value = "第三方登录", notes = "第三方登录")
    @PostMapping("/login/{oauthType}")
    public void login(@PathVariable String oauthType, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(oauthType);
        response.sendRedirect(authRequest.authorize(oauthType + "::" + AuthStateUtils.createState()));
    }

    @ApiOperation(value = "第三方登录回调", notes = "第三方登录回调")
    @GetMapping("/callback/{oauthType}")
    public void callback(@PathVariable String oauthType, AuthCallback callback, HttpServletResponse httpServletResponse) throws IOException {
        String url = socialConfig.getUrl() + "?code=" + oauthType + "-" + callback.getCode() + "&state=" + callback.getState();
        log.debug("url:{}", url);
        //跳转到指定页面
        httpServletResponse.sendRedirect(url);
    }
}
