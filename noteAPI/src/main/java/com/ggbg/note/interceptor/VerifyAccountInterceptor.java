package com.ggbg.note.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ggbg.note.exception.ExpiredTokenException;
import com.ggbg.note.exception.UnAuthorizationException;
import com.ggbg.note.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class VerifyAccountInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("v1 handler");
		String email = request.getHeader("Email");
		String token = request.getHeader("Authorization").substring(7);
		String emailByToken = "";
		
		try {
			emailByToken = jwtTokenUtil.getUsernameFromToken(token);
		}catch (MalformedJwtException e) {
			throw new UnAuthorizationException(token);
		}catch (ExpiredJwtException e) {
			throw new ExpiredTokenException("AccessToken " + token);
		}
		if(email == null || email.equals(""))
			throw new UnAuthorizationException(token);
		
		if(email.equals(emailByToken)) {
			return true;
		}else {
			throw new UnAuthorizationException("AccessToken " + token);
		}
	}
}