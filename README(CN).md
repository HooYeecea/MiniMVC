# MiniMVC

迷你 SpringMVC 风格层（mini-servlet-api + MiniIOCContainer）。

英文版：[README.md](README.md)

## 处理管道

```text
DispatcherServlet
  → HandlerMapping
  → 拦截器（pre / post / afterCompletion）
  → HandlerAdapter
       → 参数解析器链（@RequestParam / @PathVariable / @RequestBody+@Valid / ...）
       → 调用 Controller
       → 返回值处理器链
  → 异常时：@ControllerAdvice + @ExceptionHandler
```

## 运行（BIO）

```bash
mvn -pl MiniMVC -am install -DskipTests
mvn -f MiniMVC/pom.xml exec:java
```

## 试一下

```bash
curl http://localhost:8080/api/ping
curl http://localhost:8080/api/users/7
curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d "{\"name\":\"Tom\",\"age\":20}"
curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d "{\"name\":\"\",\"age\":0}"
```

## License

个人练习项目。
