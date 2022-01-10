package com.google.system.feign;

import com.google.core.common.constant.ProviderConstant;
import com.google.core.feign.constant.FeignConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 角色权限远程调用接口
 * @author iris
 */
@FeignClient(value = FeignConstant.EL_ADMIN_CLOUD_SYSTEM)
public interface ISysRolePermissionProvider {

    /**
     * 通过roleId获取权限列表
     * @param roleId　角色ID
     * @return List
     */
    @GetMapping(ProviderConstant.PROVIDER_ROLE_PERMISSION)
    List<String> getMenuIdByRoleId(@RequestParam("roleId") String roleId);
}
