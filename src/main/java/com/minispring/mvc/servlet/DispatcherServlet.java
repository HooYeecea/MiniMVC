package com.minispring.mvc.servlet;

import com.miniioccontainer.context.MiniApplicationContext;
import com.minispring.mvc.handler.HandlerMapping;
import com.minispring.mvc.handler.HandlerMethod;
import com.minispring.mvc.handler.RequestMappingHandlerMapping;
import com.minispring.web.HttpRequest;
import com.minispring.web.HttpResponse;
import com.minispring.web.Servlet;

/**
 * Front controller for MiniMVC (Spring {@code DispatcherServlet} analogue).
 * <p>
 * <b>Step 2:</b> builds {@link RequestMappingHandlerMapping} on init and looks up
 * handlers. Invocation is still deferred to the next step.
 */
public class DispatcherServlet implements Servlet {

    private final MiniApplicationContext applicationContext;
    private final RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();

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

    @Override
    public void init() {
        handlerMapping.init(applicationContext);
        System.out.println("[MiniMVC] DispatcherServlet init (step 2: HandlerMapping)");
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HandlerMethod handler = handlerMapping.getHandler(request);
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        if (handler == null) {
            response.setStatus(404, "Not Found");
            response.setBody(
                    "404 Not Found\n"
                            + "method=" + request.getMethod() + "\n"
                            + "path=" + request.getPath() + "\n"
                            + "(step 2: mapping only — no handler matched)\n");
            return;
        }
        response.setStatus(200, "OK");
        response.setBody(
                "Handler matched (step 2: mapping only, not invoked yet).\n"
                        + "handler=" + handler.getDescription() + "\n"
                        + "method=" + request.getMethod() + "\n"
                        + "path=" + request.getPath() + "\n"
                        + "Next: reflectively invoke the controller method.\n");
    }

    @Override
    public void destroy() {
        System.out.println("[MiniMVC] DispatcherServlet destroy");
    }
}
