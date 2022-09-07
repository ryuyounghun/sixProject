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


/**
 * 스프링 시큐리티 관련 설정을 하는 클래스
 */

@Configuration // 설정하는 클래스
public class WebSecurityConfig {

	@Autowired
	private DataSource dataSource;
	// application.properties에있는 datasource정보
	
	/**
	 * fileter : 어떤 기능을 위한 전처리/후처리
	 * interceptor : 
	 */
	// 스프링에서 사용 할 수 있는 객체
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity hs) throws Exception {
		
		hs.csrf().disable() //인증방법 무효 
		.authorizeRequests() // 권한 또는 인증 요청
		.antMatchers(
				"/**",
				"/images/**",
				"/css/**",
				"/js/**"
				)
		.permitAll() // 인증없이 접근 가능
		.anyRequest().authenticated() // 위의 주소가 아니면 인증해야함
		.and()
		.formLogin() // 내가만든 폼으로 로그인 시키기
		.loginPage("/member/login") // 로그인 페이지를 여는 주소
		.loginProcessingUrl("/member/login") // 로그인 페이지에서 실행할 주소
		.permitAll()
		.usernameParameter("memberId") // 사용자 정의 로그인 폼에서 사용자 식별자로 사용하는 필드의 네임 속석
		.passwordParameter("memberPw") 
		.and()
		.logout() // 로그아웃설정
		.logoutSuccessUrl("/") // 로그아웃 성공시 이동할 주소
		.permitAll()
		.and()
		.cors()
		.and()
		.httpBasic();
		
		return hs.build(); // 위의 긴 객체에 설정된 결과 리턴
	}
	
	//인증용 쿼리
	// throws : 예외발생시처리 vs throw : 예외발생시키기 
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		String userNameQueryForEnabled =
				// 열을 지정하고 공백을 넣자 ( FORM KEYWORD MISSING ERROR 방지)
				"SELECT MEMBERID username, MEMBERPW password, ENABLED "+
				"FROM DELIVERY_MEMBER "+
				"WHERE MEMBERID = ?"; // ? : memberId가 들어갈 곳
		
		String userNameQueryForRole = 
				"SELECT MEMBERID username, ROLENAME role_name "+
				"FROM DELIVERY_MEMBER "+
				"WHERE MEMBERID = ?";
		
		auth.jdbcAuthentication()
		.dataSource(dataSource) // db에 접속하기 위한 정보
	.usersByUsernameQuery(userNameQueryForEnabled) // Enabled용 쿼리		
	.authoritiesByUsernameQuery(userNameQueryForRole); // rolename용쿼리
	}
	
	// 단방향 비밀번호 암호화
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
	
	
	
	
	
	
}
