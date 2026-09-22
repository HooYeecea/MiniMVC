package com.minispring.mvc.servlet;

import com.miniioccontainer.context.MiniApplicationContext;
import com.minispring.web.HttpRequest;
import com.minispring.web.HttpResponse;
import com.minispring.web.Servlet;

/**
 * Front controller for MiniMVC (Spring {@code DispatcherServlet} analogue).
 * <p>
 * <b>Step 1:</b> skeleton only — holds the IoC context and proves the servlet
 * is reachable. Handler mapping / invocation come in later steps.
 */
public class DispatcherServlet implements Servlet {

    private final MiniApplicationContext applicationContext;

    public DispatcherServlet(MiniApplicationContext applicationContext) {
        if (applicationContext == null) {
            throw new IllegalArgumentException("applicationContext must not be null");
        }
        this.applicationContext = applicationContext;
    }

    public MiniApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void init() {
        System.out.println("[MiniMVC] DispatcherServlet init (step 1: skeleton)");
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.setStatus(200, "OK");
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        response.setBody(
                "MiniMVC DispatcherServlet is up (step 1: skeleton).\n"
                        + "method=" + request.getMethod() + "\n"
                        + "path=" + request.getPath() + "\n"
                        + "Next: HandlerMapping + invoke @Controller methods.\n");
    }

    @Override
    public void destroy() {
        System.out.println("[MiniMVC] DispatcherServlet destroy");
    }
}
