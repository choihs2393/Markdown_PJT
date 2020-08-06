package com.ggbg.note.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ggbg.note.exception.UnAuthorizationException;
import com.ggbg.note.util.JwtTokenUtil;

@Component
public class VerifyAccountInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("[v1_preHandle] entered..");
		String email = request.getHeader("Email");
		String token = request.getHeader("Authorization").substring(7);
		String emailByToken = jwtTokenUtil.getUsernameFromToken(token);
		if(email.equals(emailByToken)) {
			return true;
		}else {
			throw new UnAuthorizationException(email);
		}
	}
}