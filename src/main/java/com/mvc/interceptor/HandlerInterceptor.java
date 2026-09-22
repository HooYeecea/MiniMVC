package com.mvc.interceptor;

import com.mvc.handler.HandlerMethod;
import com.web.HttpRequest;
import com.web.HttpResponse;

/**
 * Spring-style handler interceptor (pre / post only for now).
 */
public interface HandlerInterceptor {

    /** @return {@code false} to abort the request */
    default boolean preHandle(HttpRequest request, HttpResponse response, HandlerMethod handler)
            throws Exception {
        return true;
    }

    default void postHandle(HttpRequest request, HttpResponse response, HandlerMethod handler)
            throws Exception {
    }
}
