package com.google.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.core.common.constant.SystemConstant;
import com.google.core.database.entity.Search;
import com.google.core.database.enums.OrderTypeEnum;
import com.google.core.database.utils.PageUtil;
import com.google.system.domain.User;
import com.google.system.mapper.UserMapper;
import com.google.system.service.RoleService;
import com.google.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author iris
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RoleService roleService;

    @Override
    public IPage<User> listPage(Search search, User user) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.between(StrUtil.isNotBlank(search.getStartDate()), User::getCreateTime, search.getStartDate(), search.getEndDate());
        boolean isKeyword = StrUtil.isNotBlank(search.getKeyword());
        queryWrapper.like(isKeyword, User::getName, search.getKeyword()).or(isKeyword)
                .like(isKeyword, User::getId, search.getKeyword());

        queryWrapper.eq(user.getDepartId() != null, User::getDepartId, user.getDepartId());

        if (StrUtil.isNotBlank(search.getProp())) {
            if (OrderTypeEnum.ASC.getValue().equalsIgnoreCase(search.getOrder())) {
                queryWrapper.orderByAsc(User::getId);
            } else {
                queryWrapper.orderByDesc(User::getId);
            }
        }

        // 分页查询
        IPage<User> userPage = this.baseMapper.selectPage(PageUtil.getPage(search), queryWrapper);

        // 拼装转换为中文字段的数据
         List<User> userList = userPage.getRecords().stream().peek(t -> {
            // TODO 设置部门名称  状态值
            // 判断如果roleId为0， 则赋值一个默认值
            if (SystemConstant.ROLE_DEFAULT_ID.equals(user.getRoleId())) {
                t.setRoleName(SystemConstant.ROLE_DEFAULT_VALUE);
            } else {
                t.setRoleName(roleService.getById(user.getRoleId()).getRoleName());
            }
        }).collect(Collectors.toList());

         userPage.setRecords(userList);
        return userPage;
    }
}
