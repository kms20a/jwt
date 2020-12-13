package com.cos.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		try {
			
//			BufferedReader br = request.getReader();
//			
//			String input = null;
//			while((input = br.readLine()) != null) {
//				System.out.println(input);
//			}
			
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			//PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨
			//DB에 있는 username과 password가 일치한다.
			Authentication authentication =
					authenticationManager.authenticate(authenticationToken);
			
			//=> 로그인이 되었다는 것
			PrincipalDetails principalDetails =(PrincipalDetails) authentication.getPrincipal();
			System.out.println("로그인 완료됨 : " + principalDetails.getUser().getUsername());
			// authentication 객체가 session 영역에 저장을 해야하고 그 방법이 return 해주면 됨
			// 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는 것임
			// 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리 때문에 session에 넣어 줍니다.
			//
			return authentication;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("=============================");
		
		//2. 정상인지 로그인 시도를 해보는 거에요. authenticationManager로 로그인을 시도를 하면!! 
		// PrincipalDetailsService가 호출 loadUserByUsername() 함수 실행됨.
		
		//3. PrincipalDetails를 세션을 담고
		//    (만약 권한 관리를 하지 않는다면 세션에 담을 필요 없음)
		
		//4. JWT 토근을 만들어서 응답해주면 됨
		return null;
	}
	
	//attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨
	//JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었다는 뜻임");
		super.successfulAuthentication(request, response, chain, authResult);
	}
}
