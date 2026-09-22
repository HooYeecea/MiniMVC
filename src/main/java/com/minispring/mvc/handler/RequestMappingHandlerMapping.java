package com.minispring.mvc.handler;

import com.miniioccontainer.aop.MiniAopInterceptor;
import com.miniioccontainer.context.MiniApplicationContext;
import com.minispring.mvc.annotation.Controller;
import com.minispring.mvc.annotation.RequestMapping;
import com.minispring.mvc.annotation.RequestMethod;
import com.minispring.web.HttpRequest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Scans IoC beans for {@link Controller} + {@link RequestMapping} and builds an exact-path table.
 * <p>
 * Path variables and pattern matching are deferred.
 */
public class RequestMappingHandlerMapping implements HandlerMapping {

    /** path → handlers (may differ by HTTP method) */
    private final Map<String, List<HandlerMethod>> registry = new LinkedHashMap<>();

    public void init(MiniApplicationContext applicationContext) {
        registry.clear();
        Map<String, Object> beans = applicationContext.getBeansOfType(Object.class);
        for (Object bean : beans.values()) {
            Object target = MiniAopInterceptor.unwrap(bean);
            Class<?> clazz = target.getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }
            registerController(target, clazz);
        }
        System.out.println("[MiniMVC] HandlerMapping registered " + totalMappings() + " handler(s)");
        for (List<HandlerMethod> handlers : registry.values()) {
            for (HandlerMethod handler : handlers) {
                System.out.println("  -> " + handler.getDescription());
            }
        }
    }

    private void registerController(Object bean, Class<?> clazz) {
        RequestMapping typeMapping = clazz.getAnnotation(RequestMapping.class);
        String typePath = typeMapping == null ? "" : typeMapping.value();

        for (Method method : clazz.getDeclaredMethods()) {
            RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
            if (methodMapping == null) {
                continue;
            }
            String path = combinePaths(typePath, methodMapping.value());
            Set<RequestMethod> httpMethods = toHttpMethods(typeMapping, methodMapping);
            HandlerMethod handlerMethod = new HandlerMethod(bean, method, path, httpMethods);
            register(handlerMethod);
        }
    }

    private void register(HandlerMethod handlerMethod) {
        List<HandlerMethod> existing = registry.computeIfAbsent(handlerMethod.getPath(), k -> new ArrayList<>());
        for (HandlerMethod other : existing) {
            if (overlaps(other, handlerMethod)) {
                throw new IllegalStateException(
                        "Ambiguous mapping: " + handlerMethod.getDescription()
                                + " conflicts with " + other.getDescription());
            }
        }
        existing.add(handlerMethod);
    }

    private static boolean overlaps(HandlerMethod a, HandlerMethod b) {
        if (a.getHttpMethods().isEmpty() || b.getHttpMethods().isEmpty()) {
            return true;
        }
        for (RequestMethod method : a.getHttpMethods()) {
            if (b.getHttpMethods().contains(method)) {
                return true;
            }
        }
        return false;
    }

    private static Set<RequestMethod> toHttpMethods(RequestMapping typeMapping, RequestMapping methodMapping) {
        EnumSet<RequestMethod> methods = EnumSet.noneOf(RequestMethod.class);
        if (methodMapping.method().length > 0) {
            for (RequestMethod method : methodMapping.method()) {
                methods.add(method);
            }
            return methods;
        }
        if (typeMapping != null && typeMapping.method().length > 0) {
            for (RequestMethod method : typeMapping.method()) {
                methods.add(method);
            }
        }
        return methods;
    }

    static String combinePaths(String typePath, String methodPath) {
        String left = normalize(typePath);
        String right = normalize(methodPath);
        if ("/".equals(left)) {
            return right;
        }
        if ("/".equals(right)) {
            return left;
        }
        return left + right;
    }

    static String normalize(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        String normalized = path.startsWith("/") ? path : "/" + path;
        while (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    @Override
    public HandlerMethod getHandler(HttpRequest request) {
        String path = normalize(request.getPath());
        List<HandlerMethod> handlers = registry.get(path);
        if (handlers == null || handlers.isEmpty()) {
            return null;
        }
        String httpMethod = request.getMethod();
        for (HandlerMethod handler : handlers) {
            if (handler.supportsHttpMethod(httpMethod)) {
                return handler;
            }
        }
        return null;
    }

    public int totalMappings() {
        int total = 0;
        for (List<HandlerMethod> handlers : registry.values()) {
            total += handlers.size();
        }
        return total;
    }

    /** Exposed for tests / debugging. */
    public Map<String, List<HandlerMethod>> getRegistry() {
        return java.util.Collections.unmodifiableMap(registry);
    }
}
