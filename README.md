# MiniMVC

A minimal SpringMVC-style layer on top of:

- **[mini-servlet-api](https://github.com/HooYeecea/MiniServletAPI)** — `Servlet` / `HttpRequest` / `HttpResponse`
- **MiniIOCContainer** — IoC bean container

Chinese version: [README(CN).md](README(CN).md)

MiniMVC does **not** hard-depend on one Tomcat. Demo bootstraps wire `DispatcherServlet` into
[MiniTomcat](https://github.com/HooYeecea/MiniTomcat) (BIO) or
[MiniTomcatNIO](https://github.com/HooYeecea/MiniTomcatNIO).

## Roadmap

| Step | Goal | Status |
|------|------|--------|
| 1 | Annotations + `DispatcherServlet` skeleton | done |
| 2 | `HandlerMapping` | done |
| 3 | `HandlerAdapter` invoke | done |
| 4 | Demo on MiniTomcat | done |
| 5 | `@RequestParam` | done |
| 6 | JSON (`@ResponseBody` / `@RestController`) | done |
| 7 | Demo on MiniTomcatNIO | done |
| 8 | `@PathVariable` + `HandlerInterceptor` | done |

## Features

- `@Controller` / `@RestController` / `@RequestMapping` / `@RequestParam` / `@PathVariable` / `@ResponseBody`
- `DispatcherServlet` → mapping → interceptors → adapter → JSON or plain text
- Demo controllers under `com.mvc.demo`

**Note:** controller classes also need `@MyComponent` (IoC).

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

## License

Personal practice project for learning SpringMVC internals.
