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
| 2 | `HandlerMapping` — discover `@Controller` beans and build route table | done |
| 3 | Invoke handler methods (reflect + write response) | done |
| **4** | Demo app + plug into MiniTomcat | **done** |
| later | `@RequestParam`, return-value handlers, NIO bootstrap, etc. | pending |

## Step 4 — what landed

- Demo: `com.mvc.demo.HelloController` (`GET /mvc/hello`, `GET /mvc/echo?name=...`)
- Bootstrap: `com.mvc.demo.MvcApplication` — IoC scan → `DispatcherServlet` → MiniTomcat `/*`
- MiniMVC now depends on **MiniTomcat** for the demo runner only (swap to NIO later the same way)

### Run

```bash
mvn -pl MiniMVC -am install -DskipTests
mvn -f MiniMVC/pom.xml exec:java
```

Then:

```bash
curl http://localhost:8080/mvc/hello
curl "http://localhost:8080/mvc/echo?name=MiniSpring"
```

**Note:** `@Controller` classes must also be IoC beans (e.g. `@MyComponent`).

## Build

From the parent `mini-spring` reactor (installs `mini-servlet-api` and IoC first):

```bash
mvn -pl MiniMVC -am compile
```

## License

Personal practice project for learning SpringMVC internals.
