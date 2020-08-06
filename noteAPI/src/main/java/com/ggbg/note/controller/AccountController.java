package com.ggbg.note.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggbg.note.bean.Account;
import com.ggbg.note.bean.SuccessResponse;
import com.ggbg.note.service.IAccountService;
import com.ggbg.note.util.JwtTokenUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("/account")
@RestController
public class AccountController {
	/*
	 * ?????? -->> 만약에 accessToken 이 만료되지 않은줄 알고 보냈는데 만료가 된 상태라면? -->> 지금은 그냥 10분
	 * 미만으로 내려오거나 이미 만료된 access token 이라면 프론드단에서 액세스 토큰을 재발급 해주고 있음.
	 * 
	 * 
	 * 
	 * success 했는데도 값이 없는경우도 존재함.
	 * 
	 * success 하는 경우와 fail 나는 경우 구분 가능한지
	 * 
	 * 만약 access token 으로 갱신하려했는데 안되면 refresh token 줘야함
	 * 
	 * refresh token 도 안되면 다시 로그인 요청해줘야함
	 */
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private IAccountService accountService;

	@Autowired
	private JwtTokenUtil jwt;
//	@ApiOperation(value = "logout", httpMethod = "GET", notes = "Hello this is logout")
//	@GetMapping("/logout")
//	public ResponseEntity logout(@RequestParam(required = true) String email) {
//		logger.debug("=============== signIn entered =============");
//		ResponseEntity response = null;
//		final SuccessResponse result = new SuccessResponse();
//		String msg = accountService.logout(email);
//		if("msg".equals("success")) {
//			result.status = true;
//			result.result = msg;
//			response = new ResponseEntity<>(result, HttpStatus.OK);
//		}else {
//			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//		}
//		
//		return response;
//	}

	
	// 받아야하는것 - access token , id, pw , new pw, new name + header에 Email
	@ApiOperation(value = "accountModify", httpMethod = "POST", notes = "Hello this is accountModify")
	@PostMapping("/v1/modify")
	public ResponseEntity accountModify(HttpServletRequest request,
			@RequestBody(required = true) Map<String, String> map) {
		logger.debug("=============== accountModify =============");
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();
		String email = map.get("email");
		String password = map.get("password");
		String newPassword = map.get("newPassword");
		String newName = map.get("newName");

		boolean isValidAccount = accountService.validAccountCheck(email, password);

		if (isValidAccount) {
			Account account = new Account();
			account.setEmail(email);
			account.setPassword(newPassword);
			account.setName(newName);
			boolean res = accountService.saveAccount(account);
			if (res) {
				result.status = true;
				result.result = "success";
				response = new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // NOT_FOUND
			}
		} else { // 유효한 회원정보가 아닐경우
			result.status = true;
			result.result = "fail";
			response = new ResponseEntity<>(result, HttpStatus.OK);
			return response; // fail이 발생하게 되고, 이 경우에는 비밀번호를 다시 확인하라는 문구를 출력한다.
		}

		return response;
	} // 만약 Unauthorized가 뜨면 access token 이 변조된것이다. 로그아웃 시켜야함.

	// email, password 넘겨주면 됨  + header에 Email
	@ApiOperation(value = "accountDelete", httpMethod = "POST", notes = "Hello this is accountDelete")
	@PostMapping("/v1/delete")
	public ResponseEntity accountDelete(HttpServletRequest request,
			@RequestBody(required = true) Map<String, String> map) {
		logger.debug("=============== accountModify =============");
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();
		String email = map.get("email");
		String password = map.get("password");
		boolean isValidAccount = accountService.validAccountCheck(email, password);

		if (isValidAccount) {
			Account account = new Account();
			boolean res = accountService.deleteAccount(email);
			if (res) {
				result.status = true;
				result.result = "success";
				response = new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		} else { // 유효한 회원정보가 아닐경우
			result.status = true;
			result.result = "fail";
			response = new ResponseEntity<>(result, HttpStatus.OK);
			return response; // fail이 발생하게 되고, 이 경우에는 비밀번호를 다시 확인하라는 문구를 출력한다.
		}

		return response;
	} // 만약 Unauthorized가 뜨면 access token 이 변조된것이다. 로그아웃 시켜야함.

	@ApiOperation(value = "test", httpMethod = "GET", notes = "Hello this is test")
	@GetMapping("/test")
	public ResponseEntity test(HttpServletRequest request) {
//		Map<String, Object> parseInfo = jwt.getUserParseInfo(request.getHeader("RefreshToken").substring(7));
//		List<String> rs = (List) parseInfo.get("role");
//		List<GrantedAuthority> tmp = new ArrayList<>();
//		System.out.println(parseInfo.get("email"));
//		for (String a : rs) {
//			System.out.println(a);
//			tmp.add(new SimpleGrantedAuthority(a));
//		}
//		
//		Map<String, Object> parseInfo2 = jwt.getUserParseInfo(request.getHeader("Authorization").substring(7));
//		List<String> rs2 = (List) parseInfo2.get("role");
//		List<GrantedAuthority> tmp2 = new ArrayList<>();
//		System.out.println(parseInfo2.get("email"));
//		for (String a : rs2) {
//			System.out.println(a);
//			tmp2.add(new SimpleGrantedAuthority(a));
//		}
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		return response;
	}

}
