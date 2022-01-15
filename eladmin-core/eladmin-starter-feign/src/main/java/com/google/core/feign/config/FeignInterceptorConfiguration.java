package com.google.core.feign.config;

import com.google.core.common.constant.ElAdminConstant;
import com.google.core.common.constant.TenantConstant;
import com.google.core.common.context.TenantContextHolder;
import com.google.core.common.utils.StringUtil;
import com.google.core.common.utils.TraceUtil;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign拦截器
 * @author iris
 */
@Slf4j
public class FeignInterceptorConfiguration {

    /**
     * 使用feign client发送请求时，传递tenantId和traceId
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            //传递tenantId
            String tenantId = TenantContextHolder.getTenantId();
            if (StringUtil.isNotBlank(tenantId)) {
                requestTemplate.header(TenantConstant.MATE_TENANT_ID, tenantId);
            }
            //传递日志traceId
            String traceId = MDC.get(ElAdminConstant.LOG_TRACE_ID);
            if (StringUtil.isBlank(traceId)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    Enumeration<String> headerNames = request.getHeaderNames();
                    if (headerNames != null) {
                        String headerName = null;
                        while (headerNames.hasMoreElements()) {
                            headerName = headerNames.nextElement();
                            if (headerName.equalsIgnoreCase(ElAdminConstant.EL_ADMIN_TRACE_ID)) {
                                traceId = request.getHeader(headerName);
                                requestTemplate.header(ElAdminConstant.EL_ADMIN_TRACE_ID, traceId);
                                TraceUtil.mdcTraceId(traceId);
                            }
                            String values = request.getHeader(headerName);
                            requestTemplate.header(headerName, values);
                        }
                    }
                }
            } else {
                if (StringUtil.isNotBlank(traceId)) {
                    requestTemplate.header(ElAdminConstant.EL_ADMIN_TRACE_ID, traceId);
                }
            }
        };
    }
}
