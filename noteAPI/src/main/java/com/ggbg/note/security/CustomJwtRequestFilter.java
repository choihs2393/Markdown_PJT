package com.ggbg.note.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ggbg.note.service.IAccountService;
import com.ggbg.note.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class CustomJwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private IAccountService accountServiceImpl;

	@Autowired
	private CustomAuthentication customAuthentication;

	@Bean
	public FilterRegistrationBean JwtRequestFilterRegistration(CustomJwtRequestFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(false);
		return registration;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		logger.info("JwtRequestFilter Entered...");
		logger.info("[JwtRequestFilter] requestUrl : " + request.getRequestURI());

		String token = request.getHeader("Authorization");
		
		String email = null;
		String accessToken = null;
		if(token != null && token.startsWith("Bearer ")) {
			accessToken = token.substring(7);
			try {
				email = jwtTokenUtil.getUsernameFromToken(accessToken);
			} catch (IllegalArgumentException e) {
				logger.warn("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.warn("ExpiredJwtException");
			} catch (MalformedJwtException e) {
				logger.warn("MalformedJwtException");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		
//		String email = null;
//		String accessToken = null;
//		String refreshToken = null;
//		Cookie[] cookies = request.getCookies();
//		if (cookies != null) { // store token
//			for (Cookie cookie : cookies) {
//				if (accessToken != null && refreshToken != null) {
//					break;
//				}
//				if (cookie.getName().equals("access-token")) {
//					accessToken = cookie.getValue();
//				} else if (cookie.getName().equals("refresh-token")) {
//					refreshToken = cookie.getValue();
//				}
//
//			}
//		}
//
//		/*
//		 * Sequence
//		 * 0. refresh token + access token이 모두 만료되었다면 그냥 다시 로그인을 유도한다. 
//		 * 1. refresh token이 만료되었는가? 
//		 * 	- refresh token 이 만료되지 않았고, refresh token이 만료되기 일주일전이라면 refresh token 을 이용해서 갱신한다. 
//		 * 	- refresh token 이 만료되었다면 다시 로그인을 하도록 유도한다. 
//		 * 2. access token이 만료되었는가? 
//		 * 	- access token 이 만료되지 않았고, access token 이 만료되기 5분전이라면 access token 을 이용해서 갱신한다. 
//		 * 	- access token 이 만료되었다면 refresh token 을 통해서 갱신한다.
//		 */
//
//		if (refreshToken != null) {
//			boolean isRefreshTokenExpired = jwtTokenUtil.isTokenExpired(refreshToken);
//
//			Date refreshTokenExpirationDate = jwtTokenUtil.getExpirationDateFromToken(refreshToken);
//			Date curDate = new Date();
//
//			Calendar tokenCal = Calendar.getInstance();
//			Calendar cmpCal = Calendar.getInstance();
//
//			tokenCal.setTime(refreshTokenExpirationDate);
//			cmpCal.setTime(curDate);
//
//			cmpCal.add(Calendar.DATE, 7);
//
//			boolean refreshTokenCheck = tokenCal.before(cmpCal);
//
////        	System.out.println("isRefreshTokenExpired" + isRefreshTokenExpired);
////        	System.out.println("tokenCal" + tokenCal);
////        	System.out.println("cmpCal" + cmpCal);
////        	System.out.println("refreshTokenCheck" + refreshTokenCheck);
//			if (isRefreshTokenExpired) { // refresh token 이 만료되었는가?
//				// 다시 로그인하도록 요청
//			} else {
//				if (refreshTokenCheck) { // refresh token 이 만료되기 일주일 전인가?
//					accountServiceImpl.newRefreshToken(refreshToken);
//				}
//			}
//		} else {
//			// 다시 로그인하도록 요청한다.
//		}
//
//		if (accessToken != null) {
//			boolean isAccessTokenExpired = false;
//			try{
//				isAccessTokenExpired = jwtTokenUtil.isTokenExpired(accessToken); // 이부분 만료되면 exception 띄워야함
//			} catch (ExpiredJwtException e) {
//				logger.warn("Expired Account id : " + e.getClaims().getSubject());
//			}
//			
//			if (isAccessTokenExpired) {
//				accountServiceImpl.newAccessTokenByRefreshToken(refreshToken);
//			} else {
//				accountServiceImpl.newAccessTokenByAccessToken(accessToken);
//			}
//			
//			try {
//				email = jwtTokenUtil.getUsernameFromToken(accessToken);
////            	logger.info("token in requestfilter: " + accessToken);
//				logger.info("Account email : " + email);
//			} catch (IllegalArgumentException e) {
//				logger.warn("Unable to get JWT Token");
//			} catch (ExpiredJwtException e) {
//				logger.warn("Expired Account id : " + e.getClaims().getSubject());
//
//			}
//		}
//
		if (email == null) {
			logger.info("token maybe expired: username is null.");
		} else if (redisTemplate.opsForValue().get(accessToken) != null) { // 이런 부분들을 어떻게 처리하면 좋을지에 대한 고민해봐야할것같음
			logger.warn("this token already logout!");
		} else {
			Authentication authen = customAuthentication.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authen);
			logger.info("Authentication create success!!");
		}
		chain.doFilter(request, response);
	}
}