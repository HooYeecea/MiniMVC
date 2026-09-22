package com.mvc.handler;

import com.web.HttpRequest;
import com.web.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Invokes {@link HandlerMethod}s via reflection.
 * <p>
 * <b>Step 3 argument support:</b> {@link HttpRequest}, {@link HttpResponse}, or no-arg.
 * <b>Return values:</b> {@code void} (handler wrote the response), {@link String} / other
 * → written as plain-text body. {@code @RequestParam} comes later.
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(HandlerMethod handler) {
        return handler != null;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HandlerMethod handler) throws Exception {
        Object[] args = resolveArguments(handler.getMethod(), request, response);
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
        handleReturnValue(returnValue, response);
    }

    private Object[] resolveArguments(Method method, HttpRequest request, HttpResponse response) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> type = parameters[i].getType();
            if (HttpRequest.class.isAssignableFrom(type)) {
                args[i] = request;
            } else if (HttpResponse.class.isAssignableFrom(type)) {
                args[i] = response;
            } else {
                throw new IllegalStateException(
                        "Unsupported handler parameter type: " + type.getName()
                                + " on " + method
                                + " (step 3 supports HttpRequest / HttpResponse only)");
            }
        }
        return args;
    }

    private void handleReturnValue(Object returnValue, HttpResponse response) {
        if (returnValue == null) {
            return;
        }
        byte[] existing = response.getBody();
        if (existing != null && existing.length > 0) {
            // Handler already wrote the body via HttpResponse
            return;
        }
        ensureContentType(response);
        if (returnValue instanceof String text) {
            response.setBody(text);
            return;
        }
        response.setBody(String.valueOf(returnValue));
    }

    private static void ensureContentType(HttpResponse response) {
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
    }
}
