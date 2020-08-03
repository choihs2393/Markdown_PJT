package com.ggbg.note.security;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.ggbg.note.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
    JwtTokenUtil jtu;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		System.out.println("[onLogoutSuccess] entered..");
		/*
		 * 1. refresh-token 삭제 2. access-token 쿠기가 있다면 삭제. 3. blacklist 삽입(10분) - 이미 발급한
		 * 토큰은 어떻게 할 수 없기 때문에 해당 토큰을 사용하지 못하도록 처리해야한다.
		 */
		// 1. redis에 저장된 refresh token 을 삭제시켜줌

		String accessToken = request.getHeader("Authorization").substring(7);
		
		String email = null;
		boolean check = false;

		try {
			email = jwtTokenUtil.getUsernameFromToken(accessToken);
			if (redisTemplate.opsForValue().get(email) != null) {
				redisTemplate.delete(email);
			} else {
				check = true;
			}
		} catch (ExpiredJwtException e) {
			email = e.getClaims().getSubject();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			if (!check) {
				redisTemplate.opsForValue().set(accessToken, true);
				redisTemplate.expire(accessToken, 10 * 60, TimeUnit.SECONDS);
			}
			response.setStatus(HttpStatus.OK.value());
		}
	}
}
