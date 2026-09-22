# MiniMVC

迷你 SpringMVC 风格层，建立在：

- **[mini-servlet-api](https://github.com/HooYeecea/MiniServletAPI)** — `Servlet` / `HttpRequest` / `HttpResponse`
- **MiniIOCContainer** — IoC 容器

英文版：[README.md](README.md)

## 处理管道（更接近 Spring MVC）

```text
DispatcherServlet
  → HandlerMapping
  → 拦截器（preHandle / postHandle / afterCompletion，可配路径）
  → HandlerAdapter
       → 参数解析器链
       → 调用 Controller
       → 返回值处理器链
```

### 参数解析器
`HttpRequest` / `HttpResponse` / `@RequestParam` / `@PathVariable`  
（可通过 `RequestMappingHandlerAdapter#addArgumentResolver` 扩展）

### 返回值处理器
`@ResponseBody` JSON → `String` 纯文本 → 其它 `toString()`  
（可通过 `addReturnValueHandler` 扩展）

### 拦截器
`HandlerInterceptor` + `MappedInterceptor`（`/**`、`/api/**`、精确路径）

## 运行（BIO）

```bash
mvn -pl MiniMVC -am install -DskipTests
mvn -f MiniMVC/pom.xml exec:java
```

## 运行（NIO）

```bash
mvn -pl MiniMVC -am install -DskipTests
mvn -f MiniMVC/pom.xml exec:java -Dexec.mainClass=com.mvc.demo.MvcNioApplication
```

## 试一下

```bash
curl http://localhost:8080/mvc/hello
curl "http://localhost:8080/mvc/echo?name=MiniSpring"
curl "http://localhost:8080/mvc/add?a=1&b=2"
curl http://localhost:8080/api/ping
curl "http://localhost:8080/api/user?id=7"
curl http://localhost:8080/api/users/7
```

Controller 需同时有 `@MyComponent` 与 `@Controller` / `@RestController`。

## License

个人练习项目，用于学习 SpringMVC 原理。
