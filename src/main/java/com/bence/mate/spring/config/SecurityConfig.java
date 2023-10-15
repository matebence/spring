package com.bence.mate.spring.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* @EnableGlobalMethodSecurity(
prePostEnabled = true,  // Enables @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") and @PostAuthorize("returnObject.userId == authentication.principal.userId")
securedEnabled = true,  // Enables @Secured("ROLE_MANAGER","ROLE_ADMIN")
jsr250Enabled = true    // Enables @RolesAllowed("ROLE_MANAGER","ROLE_ADMIN") (Ensures JSR-250 annotations are enabled)
) */
/* @Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// we configure it here
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("admin").password(getBCryptPasswordEncoder().encode("admin")).authorities("READ", "WRITE").roles("ROLE_ADMIN").and()
				.withUser("employee").password(getBCryptPasswordEncoder().encode("employee")).roles("ROLE_EMPLOYEE").authorities("READ").and()
				.withUser("manager").password(getBCryptPasswordEncoder().encode("manager")).roles("ROLE_MANAGER").authorities("READ");
	}

	// we configure it here
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select user_name,user_pwd,user_enabled from user where user_name=?")
				.authoritiesByUsernameQuery("select user_name,user_role from user where user_name=?")
				.passwordEncoder(getBCryptPasswordEncoder());
	}

	// we configure it here
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getBCryptPasswordEncoder());
	}

	// we expose it here
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// declares which Page(URL) will have What access type
		http.authorizeRequests()
				// antMatchers("/home") matches only the exact URL
				// mvcMatchers("/home") matches /home /home/ home.*
				// mvcMatchers("/admin").access("hasRole('ADMIN') or hasAuthority('READ')")

				.mvcMatchers("/home").permitAll()
				.mvcMatchers("/welcome").authenticated()
				.mvcMatchers("/admin").hasRole("ADMIN")
				.mvcMatchers("/emp").hasRole("EMPLOYEE")
				.mvcMatchers("/mgr").hasRole("MANAGER")
				.mvcMatchers("/common").hasAnyRole("EMPLOYEE", "MANAGER")

				.regexMatchers(".*\/write").hasAuthority("WRITE")
				.regexMatchers(".*\/read").hasAuthority("READ")

				// by default both form login and http login is enabled, but we can disabled it
 				// http.formLogin();
				// http.httpBasic();
				// Disabling CORS AND CSRF
				// http.cors().and().csrf().disable()

				// Any other URLs which are not configured in above mvcMatchers
				// generally declared authenticated() in real time
				.anyRequest().authenticated()

				// Login Form Details
				.and().formLogin().defaultSuccessUrl("/welcome", true)

				// Logout Form Details
				.and().logout().logoutUrl("/logout").invalidateHttpSession(true)

				// Exception Details
				.and().exceptionHandling().accessDeniedPage("/accessDenied");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers("/common");
	}
}
*/

@Configuration
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	protected InMemoryUserDetailsManager configAuthentication() {
//		List<UserDetails> users = new ArrayList<>();
//
//		List<GrantedAuthority> adminAuthority = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("READ"), new SimpleGrantedAuthority("WRITE"));
//		UserDetails admin = new User("admin", getBCryptPasswordEncoder().encode("admin"), adminAuthority);
//
//		List<GrantedAuthority> employeeAuthority = List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"), new SimpleGrantedAuthority("READ"));
//		UserDetails employee = new User("employee", getBCryptPasswordEncoder().encode("employee"), employeeAuthority);
//
//		List<GrantedAuthority> managerAuthority = List.of(new SimpleGrantedAuthority("ROLE_MANAGER"), new SimpleGrantedAuthority("READ"));
//		UserDetails manager = new User("manager", getBCryptPasswordEncoder().encode("manager"), managerAuthority);
//
//		users.add(admin);
//		users.add(employee);
//		users.add(manager);
//
//		return new InMemoryUserDetailsManager(users);
//	}

//	@Bean
//	public UserDetailsManager authenticateUsers() {
//		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//		users.setAuthoritiesByUsernameQuery("select user_name,user_role from user where user_name=?");
//		users.setUsersByUsernameQuery("select user_name,user_pwd,user_enabled from user where user_name=?");
//		return users;
//	}

// User details service is automatically picked up

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// The cors() method will add the Spring-provided CorsFilter to the application context, bypassing the authorization checks for OPTIONS requests.
		return http.cors().configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setMaxAge(3600L);
						return config;
					} // it will be stored in a Cookie called XSRF-TOKEN, this can be then used in a SPA app
				}).and().csrf().ignoringRequestMatchers("/common").csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.maximumSessions(2).and()
				// By default, Spring Security has this protection enabled (“migrateSession“). On authentication, a new HTTP Session is created, the old one is invalidated and the attributes from the old session are copied over.
				.sessionFixation().migrateSession()
				.invalidSessionUrl("/invalidSession").and()
				.authorizeHttpRequests()
				.requestMatchers("/home").permitAll()
				.requestMatchers("/welcome").authenticated()
				.requestMatchers("/admin").hasRole("ADMIN")
				.requestMatchers("/emp").hasRole("EMPLOYEE")
				.requestMatchers("/mgr").hasRole("MANAGER")
				.requestMatchers("/common").hasAnyRole("EMPLOYEE", "MANAGER")

				.requestMatchers(RegexRequestMatcher.regexMatcher(".*/write")).hasAuthority("WRITE")
				.requestMatchers(RegexRequestMatcher.regexMatcher(".*/read")).hasAuthority("READ")

				.anyRequest().authenticated()

				.and().formLogin().defaultSuccessUrl("/welcome", true)

				.and().logout().logoutUrl("/logout").invalidateHttpSession(true)

				.and().exceptionHandling().accessDeniedPage("/accessDenied").and().build();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		// Concurrent Session Control
		return new HttpSessionEventPublisher();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/common");
	}
}
