# MiniMVC

A minimal SpringMVC-style layer on top of:

- **[mini-servlet-api](https://github.com/HooYeecea/MiniServletAPI)** — `Servlet` / `HttpRequest` / `HttpResponse`
- **MiniIOCContainer** — IoC bean container

Chinese version: [README(CN).md](README(CN).md)

## Pipeline (closer to Spring MVC)

```text
DispatcherServlet
  → HandlerMapping
  → Interceptors (preHandle / postHandle / afterCompletion, path patterns)
  → HandlerAdapter
       → ArgumentResolver chain
       → invoke controller
       → ReturnValueHandler chain
```

### Argument resolvers
`HttpRequest` / `HttpResponse` / `@RequestParam` / `@PathVariable`  
(add more via `RequestMappingHandlerAdapter#addArgumentResolver`)

### Return value handlers
`@ResponseBody` JSON → `String` plain text → object `toString()` fallback  
(add more via `addReturnValueHandler`)

### Interceptors
`HandlerInterceptor` + `MappedInterceptor` (`/**`, `/api/**`, exact)

## Run (BIO)

```bash
mvn -pl MiniMVC -am install -DskipTests
mvn -f MiniMVC/pom.xml exec:java
```

## Run (NIO)

```bash
mvn -pl MiniMVC -am install -DskipTests
mvn -f MiniMVC/pom.xml exec:java -Dexec.mainClass=com.mvc.demo.MvcNioApplication
```

## Try

```bash
curl http://localhost:8080/mvc/hello
curl "http://localhost:8080/mvc/echo?name=MiniSpring"
curl "http://localhost:8080/mvc/add?a=1&b=2"
curl http://localhost:8080/api/ping
curl "http://localhost:8080/api/user?id=7"
curl http://localhost:8080/api/users/7
```

Controllers need `@MyComponent` as well as `@Controller` / `@RestController`.

## License

Personal practice project for learning SpringMVC internals.
