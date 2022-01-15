package com.google.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.core.database.entity.Search;
import com.google.system.domain.User;

/**
 * @author iris
 */
public interface UserService extends IService<User> {
    /**
     * 分页查询
     * @param search
     * @param sysUser
     * @return
     */
    IPage<User> listPage(Search search, User sysUser);
}
