# MiniMVC

A minimal SpringMVC-style layer on top of:

- **[mini-servlet-api](https://github.com/HooYeecea/MiniServletAPI)** — `Servlet` / `HttpRequest` / `HttpResponse`
- **MiniIOCContainer** — IoC bean container

Chinese version: [README(CN).md](README(CN).md)

MiniMVC does **not** depend on a specific Tomcat. Wire `DispatcherServlet` into
[MiniTomcat](https://github.com/HooYeecea/MiniTomcat) or
[MiniTomcatNIO](https://github.com/HooYeecea/MiniTomcatNIO) at startup.

## Roadmap (step by step)

| Step | Goal | Status |
|------|------|--------|
| **1** | Annotations + `DispatcherServlet` skeleton | **done** |
| 2 | `HandlerMapping` — discover `@Controller` beans and build route table | pending |
| 3 | Invoke handler methods (reflect + write response) | pending |
| 4 | Demo app + plug into a Tomcat impl | pending |
| later | `@RequestParam`, return-value handlers, etc. | pending |

## Step 1 — what landed

Annotations in `com.minispring.mvc.annotation`:

- `@Controller`
- `@RequestMapping` / `RequestMethod`

Front controller:

- `com.minispring.mvc.servlet.DispatcherServlet` — implements `com.minispring.web.Servlet`,
  holds `MiniApplicationContext`, responds with a plain-text “skeleton is up” body.

**Note:** `@Controller` classes must also be IoC beans (e.g. `@MyComponent`) so step 2 can find them.

## Build

From the parent `mini-spring` reactor (installs `mini-servlet-api` and IoC first):

```bash
mvn -pl MiniMVC -am compile
```

## License

Personal practice project for learning SpringMVC internals.
