package com.ggbg.note.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggbg.note.bean.Account;
import com.ggbg.note.bean.Role;
import com.ggbg.note.bean.Token;
import com.ggbg.note.repository.AccountRepo;
import com.ggbg.note.util.JwtTokenUtil;

@Service
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	JwtTokenUtil jtu;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private HttpServletRequest request;

	@Override
	public String logout(String email) {
		/*
		 * 1. refresh-token 삭제 2. access-token 쿠기가 있다면 삭제. 3. blacklist 삽입(10분) - 이미 발급한
		 * 토큰은 어떻게 할 수 없기 때문에 해당 토큰을 사용하지 못하도록 처리해야한다.
		 */

		// 1. redis에 저장된 refresh token 을 삭제시켜줌

		try {
			if (redisTemplate.opsForValue().get(email) != null) {
				redisTemplate.delete(email);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		// 2. 쿠키 삭제 및 token 값 반환
		String token = null;
		Cookie[] cookies = request.getCookies();

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("access-token")) {
				token = cookie.getValue();
				break;
			}
		}

		// 2.a.만약 토큰 자체가 없는 경우 그냥 return 시킨다.
		if (token == null) {
			return "success";
		}

		Cookie accessCookie = new Cookie("access-token", null);
		accessCookie.setMaxAge(0);
		accessCookie.setPath("/");
		response.addCookie(accessCookie);

		Cookie refreshCookie = new Cookie("refresh-token", null);
		accessCookie.setMaxAge(0);
		accessCookie.setPath("/");
		response.addCookie(refreshCookie);

		// 3. 만약 expire 되지 않은 토큰이라면 블랙리스트에 저장함.
		if (token != null || token != "") {
			if (!jwtTokenUtil.isTokenExpired(token)) {
				redisTemplate.opsForValue().set(token, true);
				redisTemplate.expire(token, 10 * 60, TimeUnit.SECONDS);

			}
		}
		return "success";
	}

	/*
	 * 반면 사용자가 접속을 뜸하게 하는 경우에도 RefreshToken의 만료 기간의 늘어나기 때문에, 핸드폰이 탈취되는 등의 경우에 지속적인
	 * 이용이 가능 할 수 있습니다. 이를 막는 방법으로 인증이 확실히 요구되는 경우 비밀번호를 한번 더 묻는다거나, 비밀번호 변경 등의 이벤트가
	 * 발생 할 때 강제로 RefreshToken을 만료시키는 처리를 해주는 것이 좋습니다.
	 */

	

	@Override
	public boolean validAccountCheck(String email, String password) {
		BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
		Optional<Account> optional = accountRepo.findAccountByEmail(email);
		Account account = new Account();
		boolean check = false;
		
		if (optional.isPresent()) {
			account = optional.get();
			check = bcryptPasswordEncoder.matches(password, account.getPassword());
			if (check) {
				return true;
			}
		}
		
		return false;
	}

	@Transactional
	@Override
	public boolean saveAccount(Account account) {
		BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
		account.setRole(Role.USER);
		account.setPassword(bcryptPasswordEncoder.encode(account.getPassword()));

		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		account.setCreateDate(simpleDateFormat.format(date));
		accountRepo.save(account); // 만약 db가 꺠져서 저장이 안되던가 하는 상황에서는 에러처리를 어떻게 해야하는지 jpa search
		return true;
	}
	
	@Transactional
	@Override
	public boolean deleteAccount(String email) {
		accountRepo.deleteById(email); // 여기서 에러 나면 false 출력하게 어떻게?
		
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		Token token = (Token) vop.get(email);
		if(token != null)
			redisTemplate.expire(email, 1, TimeUnit.MILLISECONDS);
		return true;
	}
}
