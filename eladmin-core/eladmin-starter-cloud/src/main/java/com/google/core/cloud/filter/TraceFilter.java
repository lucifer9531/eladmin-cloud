package com.google.core.cloud.filter;

import com.google.core.cloud.props.ElAdminRequestProperties;
import com.google.core.common.utils.TraceUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 日志链路追踪过滤器
 * @author iris
 */
@ConditionalOnClass(Filter.class)
public class TraceFilter extends OncePerRequestFilter {

    @Autowired
    private ElAdminRequestProperties elAdminRequestProperties;

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) throws ServletException {
        return !elAdminRequestProperties.getTrace();
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = TraceUtil.getTraceId(request);
            TraceUtil.mdcTraceId(traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
