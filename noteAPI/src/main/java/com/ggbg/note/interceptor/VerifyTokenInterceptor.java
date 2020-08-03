package com.ggbg.note.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ggbg.note.bean.Token;
import com.ggbg.note.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class VerifyTokenInterceptor implements HandlerInterceptor {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	protected JwtTokenUtil jtu;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("[token_preHandle] entered..");
		String URI = request.getRequestURI();

		if (URI.equals("/noteAPI/token/v2/newATBA")) {
			String accessToken = request.getHeader("Authorization").substring(7);

			try {
				Map<String, Object> parseInfo = jtu.getUserParseInfo(accessToken);
				String emailFromAccessToken = String.valueOf(parseInfo.get("email"));
				List<String> rs = (List) parseInfo.get("role");
				String authorityFromAccessToken = rs.get(0);

				ValueOperations<String, Object> vop = redisTemplate.opsForValue();
				Token token = (Token) vop.get(emailFromAccessToken);

				Map<String, Object> parseInfo2 = jtu.getUserParseInfo(token.getToken());
				String emailFromRefreshToken = String.valueOf(parseInfo.get("email"));
				List<String> rs2 = (List) parseInfo.get("role");
				String authorityFromRefreshToken = rs.get(0);
				
				boolean checkEmail = emailFromAccessToken.equals(emailFromRefreshToken) ? true : false;
				boolean checkAuthority = authorityFromAccessToken.equals(authorityFromRefreshToken) ? true : false;
				
				if (checkEmail && checkAuthority) {
					return true;
				}
				else {
					response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Not_acceptable"); // 에러 이게 맞는지?
					return false;
				}
			} catch (ExpiredJwtException e) {
				e.printStackTrace();
			}
		} else {
			String refreshToken = request.getHeader("RefreshToken").substring(7);
			try {
				String email = jwtTokenUtil.getUsernameFromToken(refreshToken);
				
				ValueOperations<String, Object> vop = redisTemplate.opsForValue();
				Token token = (Token) vop.get(email);
				if(token != null) {
					String redisRefreshToken = token.getToken();
					if (refreshToken.equals(redisRefreshToken)) {
						return true;
					}else {
						response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Not_acceptable"); // 에러 이게 맞는지?
						return false;
					}
				}
				
			} catch (ExpiredJwtException e) {
				e.printStackTrace();
			}
		}
		response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Not_acceptable"); // 에러 이게 맞는지?
		return false;
	}

}