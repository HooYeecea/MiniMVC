# MiniMVC

迷你 SpringMVC 风格层，建立在：

- **[mini-servlet-api](https://github.com/HooYeecea/MiniServletAPI)** — `Servlet` / `HttpRequest` / `HttpResponse`
- **MiniIOCContainer** — IoC 容器

英文版：[README.md](README.md)

MiniMVC **不绑死**某一个 Tomcat。Demo 可把 `DispatcherServlet` 挂到
[MiniTomcat](https://github.com/HooYeecea/MiniTomcat)（BIO）或
[MiniTomcatNIO](https://github.com/HooYeecea/MiniTomcatNIO)。

## 路线图

| 步骤 | 目标 | 状态 |
|------|------|------|
| 1 | 注解 + `DispatcherServlet` 骨架 | 已完成 |
| 2 | `HandlerMapping` | 已完成 |
| 3 | `HandlerAdapter` 调用 | 已完成 |
| 4 | Demo 挂 MiniTomcat | 已完成 |
| 5 | `@RequestParam` | 已完成 |
| 6 | JSON（`@ResponseBody` / `@RestController`） | 已完成 |
| 7 | Demo 挂 MiniTomcatNIO | 已完成 |
| 8 | `@PathVariable` + `HandlerInterceptor` | 已完成 |

## 能力

- `@Controller` / `@RestController` / `@RequestMapping` / `@RequestParam` / `@PathVariable` / `@ResponseBody`
- `DispatcherServlet` → 映射 → 拦截器 → 适配器 → JSON 或纯文本
- Demo：`com.mvc.demo`

**说明：** Controller 还需 `@MyComponent`（进 IoC）。

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

## License

个人练习项目，用于学习 SpringMVC 原理。
