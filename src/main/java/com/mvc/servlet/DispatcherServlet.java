package com.mvc.servlet;

import com.miniioccontainer.context.MiniApplicationContext;
import com.mvc.handler.HandlerAdapter;
import com.mvc.handler.HandlerMapping;
import com.mvc.handler.HandlerMethod;
import com.mvc.handler.RequestMappingHandlerAdapter;
import com.mvc.handler.RequestMappingHandlerMapping;
import com.mvc.interceptor.HandlerInterceptor;
import com.mvc.interceptor.MappedInterceptor;
import com.web.HttpRequest;
import com.web.HttpResponse;
import com.web.Servlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Front controller for MiniMVC.
 */
public class DispatcherServlet implements Servlet {

    private final MiniApplicationContext applicationContext;
    private final RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
    private final RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
    private final List<MappedInterceptor> interceptors = new ArrayList<>();

    public DispatcherServlet(MiniApplicationContext applicationContext) {
        if (applicationContext == null) {
            throw new IllegalArgumentException("applicationContext must not be null");
        }
        this.applicationContext = applicationContext;
    }

    public MiniApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public HandlerMapping getHandlerMapping() {
        return handlerMapping;
    }

    public HandlerAdapter getHandlerAdapter() {
        return handlerAdapter;
    }

    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return handlerAdapter;
    }

    /** Register an interceptor for all paths. */
    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(new MappedInterceptor(interceptor, "/**"));
    }

    /** Register an interceptor for the given include patterns. */
    public void addInterceptor(HandlerInterceptor interceptor, String... pathPatterns) {
        interceptors.add(new MappedInterceptor(interceptor, pathPatterns));
    }

    @Override
    public void init() {
        handlerMapping.init(applicationContext);
        System.out.println("[MiniMVC] DispatcherServlet init");
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        HandlerMethod handler = handlerMapping.getHandler(request);
        if (handler == null) {
            response.setStatus(404, "Not Found");
            response.setHeader("Content-Type", "text/plain; charset=UTF-8");
            response.setBody(
                    "404 Not Found\n"
                            + "method=" + request.getMethod() + "\n"
                            + "path=" + request.getPath() + "\n");
            return;
        }
        if (!handlerAdapter.supports(handler)) {
            response.setStatus(500, "Internal Server Error");
            response.setHeader("Content-Type", "text/plain; charset=UTF-8");
            response.setBody("No HandlerAdapter for " + handler.getDescription() + "\n");
            return;
        }

        List<MappedInterceptor> chain = matchingInterceptors(request.getPath());
        int preHandleIndex = -1;
        Exception dispatchException = null;
        try {
            for (int i = 0; i < chain.size(); i++) {
                if (!chain.get(i).preHandle(request, response, handler)) {
                    triggerAfterCompletion(chain, preHandleIndex, request, response, handler, null);
                    return;
                }
                preHandleIndex = i;
            }
            handlerAdapter.handle(request, response, handler);
            for (int i = chain.size() - 1; i >= 0; i--) {
                chain.get(i).postHandle(request, response, handler);
            }
        } catch (Exception ex) {
            dispatchException = ex;
            response.setStatus(500, "Internal Server Error");
            response.setHeader("Content-Type", "text/plain; charset=UTF-8");
            String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
            response.setBody("500 Internal Server Error\n" + message + "\n");
            System.err.println("[MiniMVC] handler failed: " + handler.getDescription());
            ex.printStackTrace(System.err);
        } finally {
            triggerAfterCompletion(chain, preHandleIndex, request, response, handler, dispatchException);
        }
    }

    private List<MappedInterceptor> matchingInterceptors(String path) {
        List<MappedInterceptor> matched = new ArrayList<>();
        for (MappedInterceptor interceptor : interceptors) {
            if (interceptor.matches(path)) {
                matched.add(interceptor);
            }
        }
        return matched;
    }

    private static void triggerAfterCompletion(List<MappedInterceptor> chain,
                                               int preHandleIndex,
                                               HttpRequest request,
                                               HttpResponse response,
                                               HandlerMethod handler,
                                               Exception ex) {
        for (int i = preHandleIndex; i >= 0; i--) {
            try {
                chain.get(i).afterCompletion(request, response, handler, ex);
            } catch (Exception afterEx) {
                System.err.println("[MiniMVC] afterCompletion error: " + afterEx.getMessage());
                afterEx.printStackTrace(System.err);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("[MiniMVC] DispatcherServlet destroy");
    }
}
