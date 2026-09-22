# MiniMVC

A minimal SpringMVC-style layer on top of mini-servlet-api + MiniIOCContainer.

Chinese version: [README(CN).md](README(CN).md)

## Pipeline

```text
DispatcherServlet
  → HandlerMapping
  → Interceptors (pre / post / afterCompletion)
  → HandlerAdapter
       → ArgumentResolver chain (@RequestParam / @PathVariable / @RequestBody+@Valid / ...)
       → invoke controller
       → ReturnValueHandler chain
  → on error: @ControllerAdvice @ExceptionHandler
```

## Run (BIO)

```bash
mvn -pl MiniMVC -am install -DskipTests
mvn -f MiniMVC/pom.xml exec:java
```

## Try

```bash
curl http://localhost:8080/api/ping
curl http://localhost:8080/api/users/7
curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d "{\"name\":\"Tom\",\"age\":20}"
curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d "{\"name\":\"\",\"age\":0}"
```

## License

Personal practice project.
