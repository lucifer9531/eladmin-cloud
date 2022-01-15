package com.google.core.redis.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis配置
 *
 * @author iris
 */
@Getter
@Setter
@ConfigurationProperties(RedisProperties.PREFIX)
public class RedisProperties {
	/**
	 * 前缀
	 */
	public static final String PREFIX = "eladmin.lettuce.redis";
	/**
	 * 是否开启Lettuce
	 */
	private Boolean enable = true;
}
