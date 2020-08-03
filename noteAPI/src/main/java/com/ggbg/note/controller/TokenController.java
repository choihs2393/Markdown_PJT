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

import com.ggbg.note.bean.BasicResponse;
import com.ggbg.note.service.ITokenService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@RequestMapping("/token")
@RestController
public class TokenController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private ITokenService tokenService;

	// newAccessTokenByAccessToken
	@ApiOperation(value = "newAccessTokenByAccessToken", httpMethod = "POST", notes = "Hello this is newAccessTokenByAccessToken")
	@PostMapping("/v2/newATBA")
	public ResponseEntity newAccessTokenByAccessToken(HttpServletRequest request) {
		logger.debug("=============== newAccessTokenByAccessToken =============");
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();

		String accessToken = request.getHeader("Authorization").substring(7);
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
		logger.debug("=============== newAccessTokenByAccessToken =============");
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();

		String refreshToken = request.getHeader("RefreshToken").substring(7);
		boolean res = tokenService.newAccessTokenByRefreshToken(refreshToken);
		result.status = true;
		if (res) {
			result.result = "success";
		} else {
			result.result = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
//				response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

		return response;

	}

	// newRefreshTokenByRefreshToken
	@ApiOperation(value = "newAccessTokenByRefreshToken", httpMethod = "POST", notes = "Hello this is newRefreshTokenByRefreshToken")
	@PostMapping("/v2/newRTBR")
	public ResponseEntity newRefreshTokenByRefreshToken(HttpServletRequest request) {
		logger.debug("=============== newAccessTokenByAccessToken =============");
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();

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
