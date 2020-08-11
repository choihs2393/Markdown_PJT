package com.ggbg.note.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.ggbg.note.domain.Role;
import com.ggbg.note.domain.Token;
import com.ggbg.note.domain.dto.AccountDTO;
import com.ggbg.note.domain.dto.BandDTO;
import com.ggbg.note.domain.entity.AccountEntity;
import com.ggbg.note.domain.entity.BandEntity;
import com.ggbg.note.exception.ExpiredTokenException;
import com.ggbg.note.exception.UnAuthorizationException;
import com.ggbg.note.repository.AccountRepo;
import com.ggbg.note.repository.BandRepo;
import com.ggbg.note.util.JwtTokenUtil;
import com.ggbg.note.util.MapperUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Service
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private BandRepo bandRepo;
	
	@Autowired
	private MapperUtil mapperUtil;
	
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
		Optional<AccountEntity> optional = accountRepo.findAccountByEmail(email);
		AccountEntity account = new AccountEntity();
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
	public boolean saveAccount(AccountDTO accountDTO) {
		int ret = -1;
		BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
		accountDTO.setPassword(bcryptPasswordEncoder.encode(accountDTO.getPassword()));
		
		ret = accountRepo.updateAccount(accountDTO.getEmail(), accountDTO.getName(), accountDTO.getPassword());
		System.out.println(ret);
		if(ret == 1)
			return true;
		else
			return false;
		
	}

	@Transactional
	@Override
	public boolean deleteAccount(String email) {
		accountRepo.deleteById(email); // 여기서 에러 나면 false 출력하게 어떻게?

		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		Token token = (Token) vop.get(email);
		if (token != null)
			redisTemplate.expire(email, 1, TimeUnit.MILLISECONDS);
		return true;
	}

	@Override
	public Map<String, Object> onLocalInit(String accessToken) {
		String name = "";
		String email = "";
		int no = -1;

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			email = jwtTokenUtil.getUsernameFromToken(accessToken);
		} catch (MalformedJwtException e) {
			throw new UnAuthorizationException(accessToken);
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException("AccessToken " + accessToken);
		}
		if (email == null || email.equals(""))
			throw new UnAuthorizationException(accessToken);

		Map<String, Object> map2 = accountRepo.findByEmail(email);
		name = (String) map2.get("account_name");
		no = (int) map2.get("account_no");
		if (name == null || name.equals(""))
			throw new UnAuthorizationException(email);

		List<BandEntity> list = bandRepo.findAllBandStatusByAccountNo(no);

		map.put("status", list);
		map.put("email", email);
		map.put("name", name);

		// local 일때 반환하는 정보 : name, email, status(초대 현황)
		return map;
	}

	@Override
	public Map<String, Object> onServerInit(String accessToken) {
		String name = "";
		String email = "";
		int no = -1;

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			email = jwtTokenUtil.getUsernameFromToken(accessToken);
		} catch (MalformedJwtException e) {
			throw new UnAuthorizationException(accessToken);
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException("AccessToken " + accessToken);
		}
		if (email == null || email.equals(""))
			throw new UnAuthorizationException(accessToken);

		Map<String, Object> map2 = accountRepo.findByEmail(email);
		name = (String) map2.get("account_name");
		no = (int) map2.get("account_no");
		if (name == null || name.equals(""))
			throw new UnAuthorizationException(email);

		List<BandEntity> list1 = bandRepo.findAllBandStatusByAccountNo(no);
		List<BandEntity> list2 = bandRepo.findAllBandByAccountNo(no);

		List<BandDTO> statusList = new ArrayList<BandDTO>();
		List<BandDTO> groupList = new ArrayList<BandDTO>();

		for(BandEntity be : list1) {
			BandDTO bd = mapperUtil.convertToDTO(be, BandDTO.class);
			statusList.add(bd);
		}
		
		for(BandEntity be : list2) {
			BandDTO bd = mapperUtil.convertToDTO(be, BandDTO.class);
			groupList.add(bd);
		}
		
		map.put("status", statusList);
		map.put("group", groupList);
		map.put("email", email);
		map.put("name", name);
		map.put("no", no);

		// 서버일때 받아와야하는것
		// email, name, group, status, no

		return map;
	}

	@Override
	public List<BandDTO> statusList(String accessToken) {
		String email = "";
		int no = -1;

		email = jwtTokenUtil.getUsernameFromToken(accessToken);

		Map<String, Object> map2 = accountRepo.findByEmail(email);

		if (!map2.containsKey("account_no"))
			throw new UnAuthorizationException(email);

		no = (int) map2.get("account_no");

		List<BandEntity> list = bandRepo.findAllBandStatusByAccountNo(no);

		List<BandDTO> statusList = new ArrayList<BandDTO>();
		
		for(BandEntity be : list) {
			BandDTO bd = mapperUtil.convertToDTO(be, BandDTO.class);
			statusList.add(bd);
		}
		
		return statusList;
	}
	
	

}
