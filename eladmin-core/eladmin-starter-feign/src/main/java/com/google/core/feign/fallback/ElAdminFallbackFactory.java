package com.google.core.feign.fallback;

import lombok.AllArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cloud.openfeign.FallbackFactory;

import feign.Target;

/**
 * @author iris
 */
@AllArgsConstructor
public class ElAdminFallbackFactory<T> implements FallbackFactory<T> {

    private final Target<T> target;

    @Override
    @SuppressWarnings("unchecked")
    public T create(Throwable cause) {
        final Class<T> targetType = target.type();
        final String targetName = target.name();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetType);
        enhancer.setUseCache(true);
        enhancer.setCallback(new com.google.core.feign.fallback.ElAdminFeignFallback<>(targetType, targetName, cause));
        return (T) enhancer.create();
    }
}
