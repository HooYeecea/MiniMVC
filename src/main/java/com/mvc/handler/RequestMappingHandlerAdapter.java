package com.mvc.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvc.annotation.PathVariable;
import com.mvc.annotation.RequestParam;
import com.mvc.annotation.ResponseBody;
import com.mvc.annotation.RestController;
import com.web.HttpRequest;
import com.web.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Invokes {@link HandlerMethod}s via reflection.
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(HandlerMethod handler) {
        return handler != null;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HandlerMethod handler) throws Exception {
        Object[] args = resolveArguments(handler, request, response);
        Object returnValue;
        try {
            returnValue = handler.getMethod().invoke(handler.getBean(), args);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause() == null ? ex : ex.getCause();
            if (cause instanceof Exception exception) {
                throw exception;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw new IllegalStateException(cause);
        }
        handleReturnValue(returnValue, response, handler);
    }

    private Object[] resolveArguments(HandlerMethod handler, HttpRequest request, HttpResponse response) {
        Method method = handler.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            if (HttpRequest.class.isAssignableFrom(type)) {
                args[i] = request;
            } else if (HttpResponse.class.isAssignableFrom(type)) {
                args[i] = response;
            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                args[i] = resolveRequestParam(parameter, request);
            } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                args[i] = resolvePathVariable(parameter, handler);
            } else {
                throw new IllegalStateException(
                        "Unsupported handler parameter type: " + type.getName()
                                + " on " + method
                                + " (use HttpRequest, HttpResponse, @RequestParam, or @PathVariable)");
            }
        }
        return args;
    }

    private Object resolvePathVariable(Parameter parameter, HandlerMethod handler) {
        PathVariable annotation = parameter.getAnnotation(PathVariable.class);
        String name = annotation.value();
        if (name == null || name.isEmpty()) {
            if (!parameter.isNamePresent()) {
                throw new IllegalStateException(
                        "Compile with -parameters, or set @PathVariable(\"name\") on " + parameter);
            }
            name = parameter.getName();
        }
        String raw = handler.getUriVariables().get(name);
        if (raw == null) {
            throw new IllegalArgumentException("Missing path variable: " + name);
        }
        return convert(raw, parameter.getType(), name);
    }

    private Object resolveRequestParam(Parameter parameter, HttpRequest request) {
        RequestParam annotation = parameter.getAnnotation(RequestParam.class);
        String name = annotation.value();
        if (name == null || name.isEmpty()) {
            if (!parameter.isNamePresent()) {
                throw new IllegalStateException(
                        "Compile with -parameters, or set @RequestParam(\"name\") on "
                                + parameter);
            }
            name = parameter.getName();
        }

        String raw = request.getParameter(name);
        if (raw == null || raw.isEmpty()) {
            if (!annotation.defaultValue().isEmpty()) {
                raw = annotation.defaultValue();
            } else if (annotation.required()) {
                throw new IllegalArgumentException("Missing required request parameter: " + name);
            } else {
                return defaultForMissing(parameter.getType());
            }
        }
        return convert(raw, parameter.getType(), name);
    }

    private static Object defaultForMissing(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == int.class || type == long.class || type == short.class || type == byte.class) {
            return 0;
        }
        if (type == double.class || type == float.class) {
            return 0.0;
        }
        if (type == char.class) {
            return '\0';
        }
        return null;
    }

    private static Object convert(String raw, Class<?> type, String name) {
        if (type == String.class) {
            return raw;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.valueOf(raw);
        }
        if (type == long.class || type == Long.class) {
            return Long.valueOf(raw);
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.valueOf(raw);
        }
        if (type == double.class || type == Double.class) {
            return Double.valueOf(raw);
        }
        throw new IllegalArgumentException(
                "Unsupported parameter type " + type.getName() + " for '" + name + "'");
    }

    private void handleReturnValue(Object returnValue, HttpResponse response, HandlerMethod handler)
            throws Exception {
        if (returnValue == null) {
            return;
        }
        byte[] existing = response.getBody();
        if (existing != null && existing.length > 0) {
            return;
        }
        if (wantsJson(handler)) {
            response.setHeader("Content-Type", "application/json; charset=UTF-8");
            response.setBody(objectMapper.writeValueAsString(returnValue));
            return;
        }
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        if (returnValue instanceof String text) {
            response.setBody(text);
            return;
        }
        response.setBody(String.valueOf(returnValue));
    }

    private static boolean wantsJson(HandlerMethod handler) {
        Method method = handler.getMethod();
        Class<?> beanClass = handler.getBean().getClass();
        return method.isAnnotationPresent(ResponseBody.class)
                || beanClass.isAnnotationPresent(ResponseBody.class)
                || beanClass.isAnnotationPresent(RestController.class);
    }
}
