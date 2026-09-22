package com.minispring.mvc.handler;

import com.minispring.mvc.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * A mapped controller method: bean instance + reflective {@link Method} + URL binding.
 */
public final class HandlerMethod {

    private final Object bean;
    private final Method method;
    private final String path;
    private final Set<RequestMethod> httpMethods;

    public HandlerMethod(Object bean, Method method, String path, Set<RequestMethod> httpMethods) {
        this.bean = bean;
        this.method = method;
        this.path = path;
        if (httpMethods == null || httpMethods.isEmpty()) {
            this.httpMethods = Collections.emptySet();
        } else {
            this.httpMethods = Collections.unmodifiableSet(EnumSet.copyOf(httpMethods));
        }
        this.method.setAccessible(true);
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    /** Empty set means all HTTP methods are accepted. */
    public Set<RequestMethod> getHttpMethods() {
        return httpMethods;
    }

    public boolean supportsHttpMethod(String requestMethod) {
        if (httpMethods.isEmpty()) {
            return true;
        }
        if (requestMethod == null || requestMethod.isEmpty()) {
            return false;
        }
        try {
            return httpMethods.contains(RequestMethod.valueOf(requestMethod.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public String getDescription() {
        return bean.getClass().getSimpleName() + "#" + method.getName()
                + " [" + String.join(",", httpMethodLabels()) + "] " + path;
    }

    private Iterable<String> httpMethodLabels() {
        if (httpMethods.isEmpty()) {
            return Set.of("*");
        }
        return httpMethods.stream().map(Enum::name).toList();
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
