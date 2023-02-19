package com.bence.mate.spring.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/* @EnableGlobalMethodSecurity(
prePostEnabled = true,  // Enables @PreAuthorize@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") and @PostAuthorize("returnObject.userId == authentication.principal.userId")
securedEnabled = true,  // Enables @Secured("ROLE_MANAGER","ROLE_ADMIN")
jsr250Enabled = true    // Enables @RolesAllowed("ROLE_MANAGER","ROLE_ADMIN") (Ensures JSR-250 annotations are enabled)
) */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
		
	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
	return new BCryptPasswordEncoder();
	}
	
	/* @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.inMemoryAuthentication()
		.withUser("admin").password(getBCryptPasswordEncoder().encode("admin")).authorities("READ", "WRITE").roles("ROLE_ADMIN").and()
		.withUser("employee").password(getBCryptPasswordEncoder().encode("employee")).roles("ROLE_EMPLOYEE").authorities("READ").and()
		.withUser("manager").password(getBCryptPasswordEncoder().encode("manager")).roles("ROLE_MANAGER").authorities("READ");
	} */
	
	/* @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.jdbcAuthentication()
	.dataSource(dataSource)  
	.usersByUsernameQuery("select user_name,user_pwd,user_enabled from user where user_name=?")
	.authoritiesByUsernameQuery("select user_name,user_role from user where user_name=?")
	.passwordEncoder(getBCryptPasswordEncoder());
	} */
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getBCryptPasswordEncoder());
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

				.regexMatchers(".*/write").hasAuthority("WRITE")
				.regexMatchers(".*/read").hasAuthority("READ")

				// by default both form login and http login is enabled, but we can disabled it
				// http.formLogin();
				// http.httpBasic();
				
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
