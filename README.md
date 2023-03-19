### Code snippets

```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=spring -e MYSQL_DATABASE=spring -e MYSQL_USER=spring -e MYSQL_PASSWORD=spring mysql/mysql-server:latest
```

```sql
create database spring;
use spring;
create table user (user_id int, user_name varchar(30), user_pwd varchar(100), user_role varchar(20), user_enabled int);

INSERT INTO user values(501,'admin','$2a$10$ZMvqN2ZtqR6JCTqUYTSq9u41iDmhrUC10fFzWiG0UP9s7Nl9WJp4W','ROLE_ADMIN',1);
INSERT INTO user values(502,'employee','$2a$10$/1mVz4TShikyjxoKihcZjubWfM083QCwn/nowfPeaS5AqXPyMNZ4C','ROLE_EMPLOYEE',1);
INSERT INTO user values(503,'manager','$2a$10$NMIsSf40rIQxfcU0pdhTNOx3AFEM18B7.mU1o9dVXcs6lDLPzLa0y','ROLE_MANAGER',1);
```

## Websecurity implementation

- **@Bean**
- **WebSecurityConfigurerAdapter**

## Websecurity options

- **UserDetailsService** 
- **JdbcAuthentication**
- **inMemoryAuthentication**

## Session creation

We can control exactly when our session gets created and how Spring Security will interact with it:
- **always** – A session will always be created if one doesn't already exist.
- **ifRequired** – A session will be created only if required (default).
- **never** – The framework will never create a session itself, but it will use one if it already exists.
- **stateless** – No session will be created or used by Spring Security.

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    return http.build();
}
```

- It's very important to understand that this configuration only controls what Spring Security does, not the entire application. Spring Security won't create the session if we instruct it not to, but our app might!
- By default, Spring Security will create a session when it needs one — this is “ifRequired“.
- For a more stateless application, the “never” option will ensure that Spring Security itself won't create any session. But if the application creates one, Spring Security will make use of it.
- Finally, the strictest session creation option, “stateless“, is a guarantee that the application won't create any session at all.

## Concurrent Session Control

When a user that is already authenticated tries to authenticate again, the application can deal with that event in one of a few ways. It can either invalidate the active session of the user and authenticate the user again with a new session, or allow both sessions to exist concurrently.

```java
@Bean
public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
}
```

This is essential to make sure that the Spring Security session registry is notified when the session is destroyed.

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.sessionManagement().maximumSessions(2)
}
```

## Handling the Session Timeout

```java
http.sessionManagement()
  .expiredUrl("/sessionExpired.html")
  .invalidSessionUrl("/invalidSession.html");
```

## Configure the Session Timeout With Spring Boot

```properties
server.servlet.session.timeout=15m
```

## Prevent Using URL Parameters for Session Tracking

Exposing session information in the URL is a growing security risk (from seventh place in 2007 to second place in 2013 on the OWASP Top 10 List).

```java
servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
```

## Session Fixation Protection With Spring Security

The framework offers protection against typical Session Fixation attacks by configuring what happens to an existing session when the user tries to authenticate again:

```java
http.sessionManagement()
  .sessionFixation().migrateSession()
```
By default, Spring Security has this protection enabled **migrateSession**. On authentication, a new HTTP Session is created, the old one is invalidated and the attributes from the old session are copied over.

If this is not what we want, two other options are available:
- When **none** is set, the original session will not be invalidated.
- When **newSessio** is set, a clean session will be created without any of the attributes from the old session being copied over.

## Secure Session Cookie

We can use the httpOnly and secure flags to secure our session cookie:
- **httpOnly** if true then browser script won't be able to access the cookie
- **secure** if true then the cookie will be sent only over HTTPS connection

```properties
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=true
```

## Working With the Session

```java
@RequestMapping(..)
public void fooMethod(HttpSession session) {
    session.setAttribute(Constants.FOO, new Foo());
    //...
    Foo foo = (Foo) session.getAttribute(Constants.FOO);
}
```

```java
ServletRequestAttributes attr = (ServletRequestAttributes) 
    RequestContextHolder.currentRequestAttributes();
HttpSession session= attr.getRequest().getSession(true); // true == allow create
```