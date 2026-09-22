package com.mvc.demo;

import com.miniioccontainer.annotation.MyComponent;
import com.mvc.annotation.Controller;
import com.mvc.annotation.RequestMapping;
import com.mvc.annotation.RequestMethod;
import com.web.HttpRequest;

/**
 * Sample controller for MiniMVC step 4.
 * Must be an IoC bean ({@link MyComponent}) so DispatcherServlet can discover it.
 */
@MyComponent
@Controller
@RequestMapping("/mvc")
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello from MiniMVC!\n";
    }

    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    public String echo(HttpRequest request) {
        String name = request.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "world";
        }
        return "echo: " + name + "\n";
    }
}
