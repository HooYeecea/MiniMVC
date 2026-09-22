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
| 1 | Annotations + `DispatcherServlet` skeleton | done |
| **2** | `HandlerMapping` — discover `@Controller` beans and build route table | **done** |
| 3 | Invoke handler methods (reflect + write response) | pending |
| 4 | Demo app + plug into a Tomcat impl | pending |
| later | `@RequestParam`, return-value handlers, etc. | pending |

## Step 2 — what landed

- `HandlerMethod` — bean + `Method` + path + HTTP methods
- `HandlerMapping` / `RequestMappingHandlerMapping`
  - Scans IoC beans (`getBeansOfType(Object.class)`), unwraps AOP proxies
  - Registers `@Controller` types that also have `@RequestMapping` on methods
  - Combines class-level + method-level paths (exact match only)
- `DispatcherServlet.init()` builds the table; `service()` looks up and reports
  the match (or 404). **Does not invoke** the controller yet.

**Note:** `@Controller` classes must also be IoC beans (e.g. `@MyComponent`).

## Build

From the parent `mini-spring` reactor (installs `mini-servlet-api` and IoC first):

```bash
mvn -pl MiniMVC -am compile
```

## License

Personal practice project for learning SpringMVC internals.
