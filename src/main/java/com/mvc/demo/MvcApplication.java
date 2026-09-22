package com.mvc.demo;

import com.miniioccontainer.context.MiniApplicationContext;
import com.minitomcat.HandleRequest;
import com.minitomcat.HttpServer;
import com.mvc.servlet.DispatcherServlet;

/**
 * Boots MiniIOC + MiniMVC {@link DispatcherServlet} on MiniTomcat (BIO).
 * <p>
 * Run from the parent reactor so dependencies resolve:
 * {@code mvn -pl MiniMVC -am exec:java -Dexec.mainClass=com.mvc.demo.MvcApplication}
 */
public class MvcApplication {

    public static void main(String[] args) throws Exception {
        MiniApplicationContext context = new MiniApplicationContext("com.mvc.demo");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        dispatcherServlet.init();

        HandleRequest.resetMappings();
        HandleRequest.registerServlet("/*", dispatcherServlet);

        System.out.println("[MiniMVC] Demo routes:");
        System.out.println("  GET http://localhost:8080/mvc/hello");
        System.out.println("  GET http://localhost:8080/mvc/echo?name=MiniSpring");
        HttpServer.main(args);
    }
}
