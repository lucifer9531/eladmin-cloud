package com.google.gateway.entity;

import lombok.Data;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * 网关路由实例
 * @author iris
 */
@Data
public class GatewayRoute {

    private static final long serialVersionUID = 1L;

    List<RouteDefinition> routes;
}
