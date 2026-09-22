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
| **3** | 反射调用处理方法并写回响应 | **已完成** |
| 4 | Demo + 接到某个 Tomcat 实现 | 待做 |
| 后续 | `@RequestParam`、返回值处理等 | 待做 |

## Step 3 — 本次交付

- `HandlerAdapter` / `RequestMappingHandlerAdapter`
  - 反射调用匹配到的 `HandlerMethod`
  - 支持方法参数注入 `HttpRequest` / `HttpResponse`
  - 返回 `String`（或其他）→ 写入纯文本 body；`void` 则假定方法已通过 `HttpResponse` 写过
- `DispatcherServlet.service()`：查找 → 适配 → 调用（失败时 404 / 500）

**说明：** `@Controller` 类同时需要是 IoC Bean（例如再加 `@MyComponent`）。

## 构建

在父工程 `mini-spring` 下（会先编译 api 与 IoC）：

```bash
mvn -pl MiniMVC -am compile
```

## License

个人练习项目，用于学习 SpringMVC 原理。
