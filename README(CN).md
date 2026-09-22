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
| **1** | 注解 + `DispatcherServlet` 骨架 | **已完成** |
| 2 | `HandlerMapping` — 发现 `@Controller` 并建路由表 | 待做 |
| 3 | 反射调用处理方法并写回响应 | 待做 |
| 4 | Demo + 接到某个 Tomcat 实现 | 待做 |
| 后续 | `@RequestParam`、返回值处理等 | 待做 |

## Step 1 — 本次交付

注解（`com.minispring.mvc.annotation`）：

- `@Controller`
- `@RequestMapping` / `RequestMethod`

前端控制器：

- `com.minispring.mvc.servlet.DispatcherServlet` — 实现 `com.minispring.web.Servlet`，
  持有 `MiniApplicationContext`，目前返回纯文本「骨架已启动」。

**说明：** `@Controller` 类同时需要是 IoC Bean（例如再加 `@MyComponent`），Step 2 才能扫到。

## 构建

在父工程 `mini-spring` 下（会先编译 api 与 IoC）：

```bash
mvn -pl MiniMVC -am compile
```

## License

个人练习项目，用于学习 SpringMVC 原理。
