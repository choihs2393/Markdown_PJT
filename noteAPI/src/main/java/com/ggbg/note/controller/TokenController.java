package com.ggbg.note.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;


import com.ggbg.note.domain.SuccessResponse;
import com.ggbg.note.service.ITokenService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("/token")
@CrossOrigin(exposedHeaders = "AccessTokenExpiraionDate, Authorization")
@RestController
public class TokenController {

	@Autowired
	private ITokenService tokenService;

	// newAccessTokenByAccessToken
	@ApiOperation(value = "newAccessTokenByAccessToken", httpMethod = "POST", notes = "Hello this is newAccessTokenByAccessToken")
	@PostMapping("/v2/newATBA")
	public ResponseEntity newAccessTokenByAccessToken(HttpServletRequest request) {
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		String accessToken = request.getHeader("Authorization").substring(7); // 7글자 이상일경우만 조사하도록 변경
		boolean res = tokenService.newAccessTokenByAccessToken(accessToken);
		result.status = true;
		if (res) {
			result.result = "success";
		} else {
			result.result = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;

	}

	// newAccessTokenByRefreshToken
	@ApiOperation(value = "newAccessTokenByRefreshToken", httpMethod = "POST", notes = "Hello this is newAccessTokenByRefreshToken")
	@PostMapping("/v2/newATBR")
	public ResponseEntity newAccessTokenByRefreshToken(HttpServletRequest request) {
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		String refreshToken = request.getHeader("RefreshToken").substring(7);
		boolean res = tokenService.newAccessTokenByRefreshToken(refreshToken);
		result.status = true;
		if (res) {
			result.result = "success";
		} else {
			result.result = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		return response;

	}

	// newRefreshTokenByRefreshToken
	@ApiOperation(value = "newRefreshTokenTokenByRefreshToken", httpMethod = "POST", notes = "Hello this is newRefreshTokenByRefreshToken")
	@PostMapping("/v2/newRTBR")
	public ResponseEntity newRefreshTokenByRefreshToken(HttpServletRequest request) {
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		String refreshToken = request.getHeader("RefreshToken").substring(7);
		boolean res = tokenService.newRefreshTokenByRefreshToken(refreshToken);
		result.status = true;
		if (res) {
			result.result = "success";
		} else {
			result.result = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		return response;

	}
}
