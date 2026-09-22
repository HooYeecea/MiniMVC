# MiniMVC

迷你 SpringMVC 风格层，建立在：

- **[mini-servlet-api](https://github.com/HooYeecea/MiniServletAPI)** — `Servlet` / `HttpRequest` / `HttpResponse`
- **MiniIOCContainer** — IoC 容器

英文版：[README.md](README.md)

MiniMVC **不依赖**具体某个 Tomcat。启动时把 `DispatcherServlet` 挂到
[MiniTomcat](https://github.com/HooYeecea/MiniTomcat) 或
[MiniTomcatNIO](https://github.com/HooYeecea/MiniTomcatNIO) 即可。

## 路线图（一步一步）

| 步骤 | 目标 | 状态 |
|------|------|------|
| 1 | 注解 + `DispatcherServlet` 骨架 | 已完成 |
| 2 | `HandlerMapping` — 发现 `@Controller` 并建路由表 | 已完成 |
| 3 | 反射调用处理方法并写回响应 | 已完成 |
| **4** | Demo + 接到 MiniTomcat | **已完成** |
| 后续 | `@RequestParam`、返回值处理、NIO 启动等 | 待做 |

## Step 4 — 本次交付

- Demo：`com.mvc.demo.HelloController`（`GET /mvc/hello`、`GET /mvc/echo?name=...`）
- 启动类：`com.mvc.demo.MvcApplication` — IoC 扫描 → `DispatcherServlet` → MiniTomcat `/*`
- MiniMVC 为 Demo 增加了 **MiniTomcat** 依赖（以后可同样方式挂 NIO）

### 运行

```bash
mvn -pl MiniMVC -am install -DskipTests
mvn -f MiniMVC/pom.xml exec:java
```

然后：

```bash
curl http://localhost:8080/mvc/hello
curl "http://localhost:8080/mvc/echo?name=MiniSpring"
```

**说明：** `@Controller` 类同时需要是 IoC Bean（例如再加 `@MyComponent`）。

## 构建

在父工程 `mini-spring` 下（会先编译 api 与 IoC）：

```bash
mvn -pl MiniMVC -am compile
```

## License

个人练习项目，用于学习 SpringMVC 原理。
