package com.ggbg.note.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggbg.note.bean.Account;
import com.ggbg.note.bean.BasicResponse;
import com.ggbg.note.service.IAccountService;
import com.ggbg.note.service.INonMemberService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@RequestMapping("/nonmember")
@RestController
public class NoneMemberController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private INonMemberService nonMemberService;

	@ApiOperation(value = "signUp", httpMethod = "POST", notes = "Hello this is signUp")
	@PostMapping("/signUp")
	public ResponseEntity signUp(@RequestBody(required = true) Account account) {
		logger.debug("=============== signUp entered =============");
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		String msg = nonMemberService.signUp(account);
		if (msg.equals("success")) {
			result.status = true;
			result.result = msg;
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		return response;
	}

	@ApiOperation(value = "email duplicated check", httpMethod = "POST", notes = "Hello this is email duplicated check")
	@PostMapping("/email")
	public ResponseEntity emailCheck(@RequestBody Map<String, String> map) {
		logger.debug("=============== email check entered =============");
		System.out.println("=============== email check entered =============");
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		String msg = nonMemberService.emailCheck(map.get("email"));
		result.status = true;
		if (msg.equals("success")) {
			result.result = msg;
		} else {
			result.result = msg;
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}

	// https://www.google.com/settings/security/lesssecureapps 들어가서 보안이 낮은 앱 차단
	@ApiOperation(value = "email auth send", httpMethod = "POST", notes = "Hello this is email authentication(SEND)")
	@PostMapping("/email/authSend")
	public ResponseEntity emailAuth(@RequestBody Map<String, String> map) {
		logger.debug("=============== email auth entered =============");
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		String msg = nonMemberService.emailAuthSend(map.get("email"));
		result.status = true;
		if (msg.equals("success")) {
			result.result = msg;
		} else {
			result.result = msg;
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@ApiOperation(value = "email auth check", httpMethod = "POST", notes = "Hello this is email authentication(CHECK)")
	@PostMapping("/email/authCheck")
	public ResponseEntity emailAuthCheck(@RequestBody Map<String, String> map) {
		logger.debug("=============== email auth check =============");
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		String msg = nonMemberService.emailAuthCheck(map.get("email"), map.get("authNum"));
		result.status = true;
		if (msg.equals("success")) {
			result.result = msg;
		} else {
			result.result = msg;
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

}
