package com.google.system.controller;

import com.google.core.auth.annotation.PreAuth;
import com.google.core.common.api.Result;
import com.google.core.database.entity.Search;
import com.google.system.domain.User;
import com.google.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author iris
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    private final UserService userService;

    // private final PasswordEncoder passwordEncoder;

    @PreAuth
    @GetMapping("/page")
    @ApiOperation(value = "用户列表", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", required = true, value = "当前页", paramType = "form"),
            @ApiImplicitParam(name = "size", required = true, value = "每页显示数据", paramType = "form"),
            @ApiImplicitParam(name = "keyword", required = true, value = "模糊查询关键词", paramType = "form"),
            @ApiImplicitParam(name = "startDate", required = true, value = "创建开始日期", paramType = "form"),
            @ApiImplicitParam(name = "endDate", required = true, value = "创建结束日期", paramType = "form"),
            @ApiImplicitParam(name = "prop", required = true, value = "排序属性", paramType = "form"),
            @ApiImplicitParam(name = "order", required = true, value = "排序方式", paramType = "form"),
    })
    public Result<?> page(Search search, User sysUser) {
        return Result.data(userService.listPage(search, sysUser));
    }
}
