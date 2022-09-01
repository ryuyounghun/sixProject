package com.abc.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity hs) throws Exception {
		hs.csrf().disable()
		.authorizeRequests()
		.antMatchers(
				"/",
				"/images/**",
				"/css/**",
				"/js/**"
				)
		.permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/member/login")
		.loginProcessingUrl("/member/login")
		.permitAll()
		.usernameParameter("memberId")
		.passwordParameter("memberPw")
		.and()
		.logout()
		.logoutSuccessUrl("/")
		.permitAll()
		.and()
		.cors()
		.and()
		.httpBasic();
		
		return hs.build();
	}
	
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		String userNameQueryForEnabled =
				"SELECT MEMBERID username, MEMBERPW password, ENABLED " +
				"FROM MEMBERS " +
				"WHERE MEMBERID = ?";
		
		String userNameQueryForRole =
				"SELECT MEMBERID username, ROLENAME roleName " +
				"FROM MEMBERS " +
				"WHERE MEMBERID = ?";
		
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery(userNameQueryForEnabled)
		.authoritiesByUsernameQuery(userNameQueryForRole);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
