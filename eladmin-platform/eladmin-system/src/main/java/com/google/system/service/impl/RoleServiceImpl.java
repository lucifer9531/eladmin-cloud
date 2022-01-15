package com.google.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.system.domain.Role;
import com.google.system.mapper.RoleMapper;
import com.google.system.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author iris
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
