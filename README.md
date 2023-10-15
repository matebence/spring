### Code snippets

```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=spring -e MYSQL_DATABASE=spring -e MYSQL_USER=spring -e MYSQL_PASSWORD=spring mysql/mysql-server:latest
```


## Websecurity implementation

- **@Bean**
- **WebSecurityConfigurerAdapter**

## Websecurity options

- **UserDetailsService** 
- **JdbcAuthentication**
- **inMemoryAuthentication**

**These are implemented by Spring, but we can create always our own based UserDetailsManager, UserDetailsService**
- 
- **InMemoryUserDetailsManager**
- **JdbcUserDetailsManager**
- **LDapUserDetailsManager**

**UserDetailsService, it has one method definition**

- **loadUserByUsername**

**UserDetailsManager it extends UserDetailsService**

- **createUser**
- **updateUser**
- **deleteUser**
- **changePassword**
- **userExists**

## Type of password encoder

- **NoOpPasswordEnoder**
- **StandardPasswordEncoder**
- **Pbkdf2PasswordEncoder**
- **BcrytPasswordEncoder**
- **SCryptPasswordEncoder**

## AuthenticationProvider
 
- The AuthenticationProvider in Spring Security takes care of the authentication logic. The default implementation of the AuthenticationProvider delegates the responsibility to find the user in the system to a UserDetailsService and PasswordEncoder for pasword validation
- Buf if we have a custom authentication requirement that is not fulfilled by Spring Security framework then we can build our own authentication logic by implementing the AuthenticationProvider interface

Principal -> Authentication -> AbstractAuthenticationToken -> UsernamePasswordAuthenticationToken

Authentication

- **getAuthorities**
- **getCredentials**
- **getDetails**
- **getPrincipal**
- **isAuthentication**
- **setAuthenticated**

Principal
- **getName**

```java
public interface AuthenticationProvider {

    Authentication authenticate(Auhentication authentication) throw AuthenticationException;
    boolean supports(Class<?> authentication);
}
```

```java
@Component
public class EazyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        List<Customer> customer = customerRepository.findByEmail(username);
        if (customer.size() > 0) {
            if (passwordEncoder.matches(pwd, customer.get(0).getPwd())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole()));
                return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
            } else {
                throw new BadCredentialsException("Invalid password!");
            }
        }else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
    }
}
```

## Attribute-Based authentication

- hasAuthority - Accepts a single authority for which the endpoint will be configured and uer will be validated against the single authority mentioned.
- hasAnyAuthority - Accepts multiple authorities for which the endpoint will be configured and user will be validated against the authorities mentioned
- access - using Spring Expression Language it provides you unlimited possibilities for configuring authorities which are not possible with the above methods

## Role based authentication

- hasRole() - Accepts a singe role name for which the endpoint be configured and user will be validated against the single role mentioned
- hasAnyRole() - Accepts multiple roles for which the endpoint will be configured and user will be validated agais the roles mentioned
- access() - Using Spring Expression Language it provides you unlimited possibilities for configuring roles which are nt possible with the above methods

### Prefixes

- for roles the prefix ROLE_
- for scopes the prefix SCOPE_
- for authorities, there is no prefix

```java
hasRole("ADMIN") 
hasAnyAuthority("ROLE_ADMIN")
hasAnyAuthority("SCOPE_ADMIN") 
hasAnyAuthority("ROLE_ADMIN", "SCOPE_portfolio-service-admin")
```

You can easily refer to the path variable by placing it in the pattern. For example, if you had a Bean with the name of webSecurity that contains the following method signature:

```java
public class WebSecurity {
    public boolean checkUserId(Authentication authentication, int id) {
    }
}
```

```java
public class SecurityConfig {
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/user/{userId}/**").access(new WebExpressionAuthorizationManager("@webSecurity.checkUserId(authentication,#userId)")));
    }
}
```

```java
@Configuration
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {
    
        /**
         * /myAccount - Secured /myBalance - Secured /myLoans - Secured /myCards -
         * Secured /notices - Not Secured /contact - Not Secured
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            /**
             * Default configurations which will secure all the requests
             */

            /*
            * http .authorizeRequests() .anyRequest().authenticated() .and()
            * .formLogin().and() .httpBasic();
            */

            /**
             * Custom configurations as per our requirement
             */

            /*
            * http .authorizeRequests() 
            * .antMatchers("/myAccount").authenticated()
            * .antMatchers("/myBalance").authenticated()
            * .antMatchers("/myLoans").authenticated()
            * .antMatchers("/myCards").authenticated() 
            * .antMatchers("/notices").permitAll()
            * .antMatchers("/contact").permitAll() 
            * .and() .formLogin().and() .httpBasic();
            */

            /**
             * Configuration to deny all the requests, even if the e credentials are OK, we dont let them go here
             */

            /*
            * http .authorizeRequests() .anyRequest().denyAll() .and() .formLogin().and()
            * .httpBasic();
            */

            /**
             * Configuration to permit all the requests
             */

            http .authorizeRequests() .anyRequest().permitAll().and() .formLogin().and()
            .httpBasic();
        }
}
```

## Customizing Spring security User Entity

```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String email;
	private String pwd;
	private String role;
}
```

```java
public class SecurityCustomer implements UserDetails {

	private final Customer customer;

	public SecurityCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(customer.getRole()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return customer.getPwd();
	}

	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
```

## Filters in Spring Security

Visualize filter order:

```properties
logging.level.org.springframework.security.web.FilterChainProxy=DEBUG
```

```java
public class RequestValidationBeforeFilter implements Filter {

	public static final String AUTHENTICATION_SCHEME_BASIC = "Basic";
	private Charset credentialsCharset = StandardCharsets.UTF_8;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String header = req.getHeader(AUTHORIZATION);
		if (header != null) {
			header = header.trim();
			if (StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BASIC)) {
				byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
				byte[] decoded;
				try {
					decoded = Base64.getDecoder().decode(base64Token);
					String token = new String(decoded, getCredentialsCharset(req));
					int delim = token.indexOf(":");
					if (delim == -1) {
						throw new BadCredentialsException("Invalid basic authentication token");
					}
					String email = token.substring(0, delim);
					if(email.toLowerCase().contains("test")) {
						res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
				} catch (IllegalArgumentException e) {
					throw new BadCredentialsException("Failed to decode basic authentication token");
				}
			}
		}
		chain.doFilter(request, response);
	}

	protected Charset getCredentialsCharset(HttpServletRequest request) {
		return getCredentialsCharset();
	}

	public Charset getCredentialsCharset() {
		return this.credentialsCharset;
	}
}
```

```java
public class AuthoritiesLoggingAfterFilter implements Filter {

	private final Logger LOG =
			Logger.getLogger(AuthoritiesLoggingAfterFilter.class.getName());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(null!=authentication) {
			LOG.info("User "+authentication.getName()+" is successfully authenticated and "
					+ "has the authorities "+authentication.getAuthorities().toString());
		}
		
		chain.doFilter(request, response);
	}
}
```

```java
public class AuthoritiesLoggingAtFilter implements Filter {

	private final Logger LOG =
			Logger.getLogger(AuthoritiesLoggingAtFilter.class.getName());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOG.info("Authentication Validation is in progress");
		chain.doFilter(request, response);
	}

}
```

```java
public class SecurityConfig {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .authorizeRequests();
    }
}
```


### OncePerRequestFilter?

- Let's first understand how filters work. A Filter can be called either before or after servlet execution. When a request is dispatched to a servlet, the RequestDispatcher may forward it to another servlet. There's a possibility that the other servlet also has the same filter. In such scenarios, the same filter gets invoked multiple times.
- But, we might want to ensure that a specific filter is invoked only once per request. A common use case is when working with Spring Security. When a request goes through the filter chain, we might want some of the authentication actions to happen only once for the request.
- We can extend the OncePerRequestFilter in such situations. Spring guarantees that the OncePerRequestFilter is executed only once for a given request.

```java
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
			String jwt = Jwts.builder().setIssuer("Eazy Bank").setSubject("JWT Token")
						.claim("username", authentication.getName())
					  .claim("authorities", populateAuthorities(authentication.getAuthorities()))
					  .setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime() + 3000000000))
					.signWith(key).compact();
			response.setHeader(SecurityConstants.JWT_HEADER, jwt);
		}

		chain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !request.getServletPath().equals("/user");
	}
	
	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
        	authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
	}
}
```

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