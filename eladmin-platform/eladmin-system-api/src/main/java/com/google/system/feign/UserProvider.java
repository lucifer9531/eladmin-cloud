package com.google.system.feign;

import com.google.core.common.api.Result;
import com.google.core.common.constant.ProviderConstant;
import com.google.core.feign.constant.FeignConstant;
import com.google.system.domain.User;
import com.google.system.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author iris
 */
@FeignClient(value = FeignConstant.EL_ADMIN_CLOUD_SYSTEM)
public interface UserProvider {
    /**
     * 根据id查询用户信息
     *
     * @param id id
     * @return Result
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_ID)
    Result<User> getUserById(@RequestParam("id") Long id);

    /**
     * 根据userName查询用户信息
     * @param userName　用户名
     * @return Result
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    Result<UserInfo> getUserByUserName(@RequestParam("userName") String userName);

    /**
     * 根据手机号查询用户信息
     * @param mobile　手机号码
     * @return Result
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_MOBILE)
    Result<UserInfo> getUserByMobile(@RequestParam("mobile") String mobile);

}
