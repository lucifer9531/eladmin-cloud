package com.google.core.common.utils;

import com.google.core.common.constant.ElAdminConstant;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;

/**
 * 链路追踪工具类
 * @author iris
 */
public class TraceUtil {

    /**
     * 从header和参数中获取traceId
     * 从前端传入数据
     *
     * @param request　HttpServletRequest
     * @return traceId
     */
    public static String getTraceId(HttpServletRequest request) {
        String traceId = request.getParameter(ElAdminConstant.EL_ADMIN_TRACE_ID);
        if (StringUtil.isBlank(traceId)) {
            traceId = request.getHeader(ElAdminConstant.EL_ADMIN_TRACE_ID);
        }
        return traceId;
    }

    /**
     * 传递traceId至MDC
     * @param traceId　跟踪ID
     */
    public static void mdcTraceId (String traceId) {
        if (StringUtil.isNotBlank(traceId)) {
            MDC.put(ElAdminConstant.LOG_TRACE_ID, traceId);
        }
    }
}
