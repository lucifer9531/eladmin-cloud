package com.google.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.core.database.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author iris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mate_sys_role")
@ApiModel(value = "SysRole对象", description = "角色表")
public class Role extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码")
    private String roleCode;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private int sort;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 删除标识
     */
    @ApiModelProperty(value = "删除标识")
    private String isDeleted;
    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;

    /**
     * 菜单列表
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "菜单列表")
    private List<String> menu;
}
