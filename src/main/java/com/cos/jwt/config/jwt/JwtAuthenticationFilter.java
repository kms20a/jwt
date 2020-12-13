package com.cos.jwt.config.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
// /login 요청해서 username, password 전송하면(post)
// UsernamePasswordAuthenticationFilter 동작을 함.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	
	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : 로그인 시도중");
		
		//1. username, password 받아서
		
		//2. 정상인지 로그인 시도를 해보는 거에요. authenticationManager로 로그인을 시도를 하면!! 
		// PrincipalDetailsService가 호출 loadUserByUsername() 함수 실행됨.
		
		//3. PrincipalDetails를 세션을 담고
		//    (만약 권한 관리를 하지 않는다면 세션에 담을 필요 없음)
		
		//4. JWT 토근을 만들어서 응답해주면 됨
		return super.attemptAuthentication(request, response);
	}
}
